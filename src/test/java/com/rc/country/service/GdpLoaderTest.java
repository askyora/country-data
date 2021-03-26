package com.rc.country.service;

import com.rc.country.dao.CountryDAO;
import com.rc.country.dao.CountryDatedValueDAO;
import com.rc.country.entity.*;
import com.rc.country.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GdpLoaderTest {


    @Mock
    private CountryDAO countryDAO;

    @Mock
    private CountryDatedValueDAO datedValueDAO;

    @InjectMocks
    public GdpLoader service;
    File validFile = new File("src/test/resources/gdp-valid.csv");
    File duplicateRecords = new File("src/test/resources/gdp-duplicate.csv");
    File oneGDPFile = new File("src/test/resources/gdp-valid.csv");

    @DisplayName("Should Throw HttpClientErrorException When Country Not Existing")
    @Test
    public void should_Throw_HttpClientErrorException_When_Country_Is_Existing() throws FileNotFoundException {
        InputStream targetStream = new FileInputStream(oneGDPFile);
        when(countryDAO.findByCode("AUS")).thenReturn(Optional.empty());

        assertThrows(HttpClientErrorException.class, () -> {
            service.load(targetStream);
        });
        verify(countryDAO).findByCode("AUS");
        Assertions.assertEquals(service.getName(),"gdp");
    }


    @DisplayName("Should Throw HttpClientErrorException When Duplicate record found In the file.")
    @Test
    public void should_Throw_RemoteClientException_When_Duplicate_Records_Found() throws FileNotFoundException{
        InputStream targetStream = new FileInputStream(duplicateRecords);

        Country country= new Country("Australia", List.of(new CountryCode(CountryCodeType.ALPHA3,"AUS",null)));
        country.setId(1l);

        when(countryDAO.findByCode(Mockito.anyString())).thenReturn(Optional.ofNullable(country));

        when(datedValueDAO.findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class))).thenReturn(Optional.empty());

        assertThrows(HttpClientErrorException.class, () -> {
            service.load(targetStream);
        });
        verify(countryDAO,Mockito.times(2)).findByCode(Mockito.anyString());
        verify(datedValueDAO,Mockito.times(2)).findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class));
    }

    @DisplayName("Should Throw HttpClientErrorException When Duplicate record found In the Database.")
    @Test
    public void should_Throw_RemoteClientException_When_Duplicate_Records_Found_In_DB() throws FileNotFoundException{
        InputStream targetStream = new FileInputStream(duplicateRecords);

        Country country= new Country("Australia", List.of(new CountryCode(CountryCodeType.ALPHA3,"AUS",null)));
        country.setId(1l);

        when(countryDAO.findByCode(Mockito.anyString())).thenReturn(Optional.ofNullable(country));

        when(datedValueDAO.findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class))).thenReturn(Optional.ofNullable(new CountryDatedValue()));

        assertThrows(HttpClientErrorException.class, () -> {
            service.load(targetStream);
        });
        verify(countryDAO,Mockito.times(1)).findByCode(Mockito.anyString());
        verify(datedValueDAO,Mockito.times(1)).findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class));
    }


    @DisplayName("Should_Invoke_Save_All_When_File_Is_Valid")
    @Test
    public void should_Invoke_Save_All_When_File_Is_Valid() throws FileNotFoundException{
        InputStream targetStream = new FileInputStream(validFile);

        Country country= new Country("Australia", List.of(new CountryCode(CountryCodeType.ALPHA3,"AUS",null)));
        country.setId(100l);

        when(countryDAO.findByCode(Mockito.anyString())).thenReturn(Optional.ofNullable(country));

        when(datedValueDAO.findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class))).thenReturn(Optional.empty());


        CountryDatedValue val1= new CountryDatedValue();
        val1.setCountry(country);
        val1.setValue( new BigDecimal("853764622753"));
        val1.setTimeStamp(DateUtil.getDateByYear(2007));
        val1.setType(CountryValueTypes.GDP);

        CountryDatedValue val2= new CountryDatedValue();
        val2.setCountry(country);
        val2.setValue( new BigDecimal("1055334825425"));
        val2.setTimeStamp(DateUtil.getDateByYear(2008));
        val2.setType(CountryValueTypes.GDP);


        when(datedValueDAO.saveAll(List.of(val1 , val2 ))).thenReturn(List.of(val1 , val2 ));


        service.load(targetStream);

        verify(countryDAO,Mockito.times(2)).findByCode(Mockito.anyString());
        verify(datedValueDAO,Mockito.times(2)).findByCountryAndTypeAndYear(Mockito.anyLong(),
                Mockito.any(CountryValueTypes.class),Mockito.any(Date.class));
        verify(datedValueDAO,Mockito.times(1)).saveAll(List.of(val1 , val2 ));
    }



}
