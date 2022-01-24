package com.yora.data.service;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.yora.data.dao.CountryDAO;
import com.yora.data.dao.CountryDatedValueDAO;
import com.yora.data.dto.GdpDTO;
import com.yora.data.entity.Country;
import com.yora.data.entity.CountryDatedValue;
import com.yora.data.entity.CountryValueTypes;
import com.yora.data.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class GdpLoader extends AbstractDataLoader<CountryDatedValue, GdpDTO> {

    public static final String GDP = "gdp";

    private CountryDatedValueDAO datedValueDao;

    private CountryDAO countryDAO;
    @Value("${GdpLoader.Year.min:2007}")
    private int yearMin = 2007;
    @Value("${GdpLoader.Year.max:2016}")
    private int yearMax = 2016;

    @Autowired
    public GdpLoader(CountryDatedValueDAO datedValueDao, CountryDAO countryDAO) {
        this.datedValueDao = datedValueDao;
        this.countryDAO = countryDAO;
    }

    @Override
    List<GdpDTO> parseList(Reader reader) {
        CSVIterator iterator;
        List<GdpDTO> list = new ArrayList<>();
        try {
            iterator = new CSVIterator(new CSVReader(reader));
            iterator.next();
            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();

                list.add(new GdpDTO(
                        nextLine[0],
                        nextLine[1],
                        Integer.valueOf(nextLine[2]),
                        new BigDecimal(nextLine[3])
                ));
            }

        } catch (CsvValidationException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    protected Predicate<GdpDTO> predicates() {
        return p -> p != null && (p.getYear() >= getYearMin()
                && p.getYear() <= getYearMax());
    }

    @Override
    public CountryDatedValue map(GdpDTO dto) {

        Optional<Country> countryOptional = countryDAO
                .findByCode(dto.getCountryCode());

        if (!countryOptional.isPresent()) {
            log.error("Country Code Not Found : {}", dto.getCountryCode());
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("Country Code : {%s}  Not Found.",
                    dto.toString()));
        }

        if (!countryOptional.get().getName().equalsIgnoreCase(dto.getCountry())) {
            log.debug("Country Name Mismatch : {} =>  With existing : {}", dto.getCountry(), countryOptional.get().getName());
        }

        Optional<CountryDatedValue> datedValue = datedValueDao.findByCountryAndTypeAndYear(countryOptional.get().getId(), CountryValueTypes.GDP
                , DateUtil.getDateByYear(dto.getYear()));

        datedValue.ifPresent(i -> {
            log.error(" Id : {}  Conflicting With Existing values. [ {} ]", dto.toString(), dto);
            throw new HttpClientErrorException(HttpStatus.CONFLICT, String.format("Id : {%s}  Conflicting With Existing values.",
                    dto.toString()));
        });

        return datedValue.orElseGet(() ->
                createNew(dto, countryOptional.get(), DateUtil.getDateByYear(dto.getYear())));

    }

    private CountryDatedValue createNew(GdpDTO dto, Country country, Date timeStamp) {
        CountryDatedValue val = new CountryDatedValue();
        val.setCountry(country);
        val.setType(CountryValueTypes.GDP);
        val.setTimeStamp(timeStamp);
        val.setValue(dto.getValue());
        return val;
    }

    @Override
    List<CountryDatedValue> save(@Valid List<CountryDatedValue> list) {
        return datedValueDao.saveAll(list);
    }

    @Override
    String getName() {
        return GDP;
    }

    public int getYearMin() {
        return yearMin;
    }

    public void setYearMin(int yearMin) {
        this.yearMin = yearMin;
    }

    public int getYearMax() {
        return yearMax;
    }

    public void setYearMax(int yearMax) {
        this.yearMax = yearMax;
    }


}
