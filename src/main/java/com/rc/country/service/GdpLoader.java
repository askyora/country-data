package com.rc.country.service;

import com.rc.country.dao.CountryDAO;
import com.rc.country.dao.CountryDatedValueDAO;
import com.rc.country.dto.GdpDTO;
import com.rc.country.entity.Country;
import com.rc.country.entity.CountryDatedValue;
import com.rc.country.entity.CountryValueTypes;
import com.rc.country.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j
public class GdpLoader extends AbstractDataLoader<CountryDatedValue, GdpDTO> {

    public static final String GDP = "gdp";
    @Autowired
    DateUtil dateUtil;
    @Autowired
    private CountryDatedValueDAO datedValueDao;
    @Autowired
    private CountryDAO countryDAO;
    @Value("${GdpLoader.Year.min:2007}")
    private int yearMin = 2007;

    @Value("${GdpLoader.Year.max:2016}")
    private int yearMax = 2016;

    @Override
    protected Predicate<CountryDatedValue> predicates() {
        return p -> p != null && (p.getTimeStamp().compareTo(dateUtil.getDateByYear(yearMin)) >= 0
                && p.getTimeStamp().compareTo(dateUtil.getDateByYear(yearMax)) <= 0);
    }

    @Override
    public CountryDatedValue map(GdpDTO dto) {

        Optional<Country> countryOptional = countryDAO
                .findByCode(dto.getCountryCode());

        if (!countryOptional.isPresent()) {
            log.error("Country Code Not Found : {}", dto.getCountryCode());
            return null;
        }

        if (!countryOptional.get().getName().equalsIgnoreCase(dto.getCountry())) {
            log.debug("Country Name Mismatch : {} =>  With existing : {}", dto.getCountry(), countryOptional.get().getName());
        }

        Optional<CountryDatedValue> datedValue = datedValueDao.findByCountryAndTypeAndYear(countryOptional.get().getId(), CountryValueTypes.GDP
                , dateUtil.getDateByYear(dto.getYear()));

        datedValue.ifPresent(i -> {
            log.error(" Id : {}  Conflicting With Existing values. [ {} ]", dto.toString(), dto);
            throw new HttpClientErrorException(HttpStatus.CONFLICT, String.format("Id : {%s}  Conflicting With Existing values.",
                    dto.toString()));
        });

        return datedValue.orElseGet(() ->
                createNew(dto, countryOptional.get(), dateUtil.getDateByYear(dto.getYear())));

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
}
