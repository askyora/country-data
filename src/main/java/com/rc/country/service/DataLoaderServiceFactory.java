package com.rc.country.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.rc.country.dao.CountryDAO;
import com.rc.country.dto.CountryDTO;
import com.rc.country.entity.Country;
import com.rc.country.entity.CountryCodes;
import com.rc.country.entity.CountryCodesType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DataLoaderServiceFactory {

    @Autowired
    private CountryDAO dao;


    public List<Country> load(InputStream inputStream, String type) {
        Reader reader = new InputStreamReader(inputStream);
        Set<CountryDTO> existingSet = new HashSet<>();
        List<Country>  listOfCountries =    new CsvToBeanBuilder<CountryDTO>
                (reader)
                .withType(CountryDTO.class)
                .build()
                .stream()
                .map(current -> {
                    if (existingSet.contains(current)) {
                        throw new HttpClientErrorException(HttpStatus.CONFLICT,
                                String.format("[Alpha 3 Code : %s] [{%s}] Conflicting another with a prior row.", current.getAlphaThreeCode(),
                                        current.toString()));
                    }
                    existingSet.add(current);
                    return current;
                }).map(this::validatedAndMap).collect(Collectors.toList());

         return dao.saveAll(listOfCountries);
    }


    private Country validatedAndMap(@Valid CountryDTO dto) {

        Optional<Country> orderEntityOptional = dao
                .findByName(dto.getCountry());

        orderEntityOptional.ifPresent(i -> {
            log.error(" Id : {}  Conflicting With Existing values. [ {} ]", dto.getAlphaThreeCode(), dto);
            throw new HttpClientErrorException(HttpStatus.CONFLICT, String.format("Row Id : {%s}  Conflicting With Existing values. [ {%s} ]",
                    dto.getAlphaTwoCode(), dto));
        });
        return orderEntityOptional.orElseGet(() -> createNew(dto));
    }

    private Country createNew(CountryDTO dto) {
        Country country = new Country();
        country.setName(dto.getCountry());

        List<CountryCodes> codeList = new ArrayList<>();
        CountryCodes alpha2code = new CountryCodes();
        alpha2code.setCode(dto.getAlphaTwoCode());
        alpha2code.setType(CountryCodesType.ALPHA2);
        alpha2code.setCountry(country);

        CountryCodes alpha3code = new CountryCodes();
        alpha3code.setCode(dto.getAlphaThreeCode());
        alpha3code.setType(CountryCodesType.ALPHA3);
        alpha3code.setCountry(country);

        CountryCodes numaric = new CountryCodes();
        numaric.setCode(String.valueOf(dto.getNumeric()));
        numaric.setType(CountryCodesType.NUMARIC);
        numaric.setCountry(country);

        codeList.add(alpha2code);
        codeList.add(alpha3code);
        codeList.add(numaric);

        country.setCodesList(codeList);
        return country;
    }

}
