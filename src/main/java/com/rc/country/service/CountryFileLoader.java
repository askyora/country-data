package com.rc.country.service;

import com.rc.country.dao.CountryDAO;
import com.rc.country.dto.CountryDTO;
import com.rc.country.entity.Country;
import com.rc.country.entity.CountryCode;
import com.rc.country.entity.CountryCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;


@Slf4j
@Service
public class CountryFileLoader extends AbstractDataLoader<Country, CountryDTO> {

    public static final String COUNTRY = "country";

    private final CountryDAO countryDAO;

    @Autowired
    public CountryFileLoader(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    @Override
    protected Predicate<CountryDTO> predicates() {
        return Objects::nonNull;
    }

    @Override
    public Country map(@Valid CountryDTO dto) {

        Optional<Country> orderEntityOptional = countryDAO
                .findByName(dto.getCountry());

        orderEntityOptional.ifPresent(i -> {
            log.error(" Id : {}  Conflicting With Existing values. [ {} ]", dto.getAlphaThreeCode(), dto);
            throw new HttpClientErrorException(HttpStatus.CONFLICT, String.format("Row Id : {%s}  Conflicting With Existing values. [ {%s} ]",
                    dto.getAlphaTwoCode(), dto));
        });
        return orderEntityOptional.orElseGet(() -> createNew(dto));
    }


    @Override
    String getName() {
        return COUNTRY;
    }

    @Override
    public List<Country> save(@Valid List<Country> list) {
        return countryDAO.saveAll(list);
    }


    private Country createNew(CountryDTO dto) {
        Country country = new Country();
        country.setName(dto.getCountry());

        List<CountryCode> codeList = new ArrayList<>();
        CountryCode alpha2code = new CountryCode();
        alpha2code.setCode(dto.getAlphaTwoCode());
        alpha2code.setType(CountryCodeType.ALPHA2);
        alpha2code.setCountry(country);

        CountryCode alpha3code = new CountryCode();
        alpha3code.setCode(dto.getAlphaThreeCode());
        alpha3code.setType(CountryCodeType.ALPHA3);
        alpha3code.setCountry(country);

        CountryCode numaric = new CountryCode();
        numaric.setCode(String.valueOf(dto.getNumeric()));
        numaric.setType(CountryCodeType.NUMARIC);
        numaric.setCountry(country);

        codeList.add(alpha2code);
        codeList.add(alpha3code);
        codeList.add(numaric);
        country.setCodesList(codeList);
        return country;
    }

}
