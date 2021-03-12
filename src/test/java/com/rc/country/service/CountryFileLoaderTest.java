package com.rc.country.service;

import com.rc.country.dao.CountryDAO;
import com.rc.country.entity.Country;
import com.rc.country.entity.CountryCode;
import com.rc.country.entity.CountryCodeType;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryFileLoaderTest {

    @Mock
    private CountryDAO countryDAO;

    @InjectMocks
    public CountryFileLoader service;
    File initialFile = new File("src/test/resources/3-country-valid.csv");
    File duplicateRecords = new File("src/test/resources/10-country-duplicate.csv");
    File oneCountryFile = new File("src/test/resources/1-country-valid.csv");



    @DisplayName("Should Throw HttpClientErrorException When Country Is Existing")
    @Test
    public void should_Throw_HttpClientErrorException_When_Country_Is_Existing() throws FileNotFoundException {
        InputStream targetStream = new FileInputStream(oneCountryFile);
        when(countryDAO.findByName("Anguilla")).thenReturn(Optional.ofNullable(new Country()));

        assertThrows(HttpClientErrorException.class, () -> {
            service.load(targetStream);
        });
        verify(countryDAO).findByName("Anguilla");
        Assertions.assertEquals(service.getName(),"country");
    }

    @DisplayName("Should Throw HttpClientErrorException When Duplicate record found In the file.")
    @Test
    public void should_Throw_RemoteClientException_When_Duplicate_Records_Found() throws FileNotFoundException{
        InputStream targetStream = new FileInputStream(duplicateRecords);

        when(countryDAO.findByName(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

        assertThrows(HttpClientErrorException.class, () -> {
            service.load(targetStream);
        });
        verify(countryDAO,Mockito.times(9)).findByName(Mockito.anyString());
    }

    @DisplayName("Should Invoke Repository.SaveAll() When Country Object Is NotExisting")
    @Test
    public void shouldInvokeStoreOrderRepositorySaveAllWhenOrderEntityIsNotExisting() throws FileNotFoundException {
        InputStream targetStream = new FileInputStream(initialFile);

        when(countryDAO.findByName(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

        Country ce1 = new Country();
        ce1.setName("Albania");
        ce1.setCodesList(
                List.of(new CountryCode( CountryCodeType.ALPHA2, "AL",ce1),
                new CountryCode( CountryCodeType.ALPHA3, "ALB",ce1),
                new CountryCode( CountryCodeType.NUMARIC, "8",ce1)
                ));

        Country ce2 = new Country();
        ce2.setName("Algeria");
        ce2.setCodesList(
                List.of(new CountryCode( CountryCodeType.ALPHA2, "DZ",ce2),
                        new CountryCode( CountryCodeType.ALPHA3, "DZA",ce2),
                        new CountryCode( CountryCodeType.NUMARIC, "12",ce2)
                ));

        Country ce3 = new Country();
        ce3.setName("American Samoa");
        ce3.setCodesList(
                List.of(new CountryCode( CountryCodeType.ALPHA2, "AS",ce3),
                        new CountryCode( CountryCodeType.ALPHA3, "ASM",ce3),
                        new CountryCode( CountryCodeType.NUMARIC, "12",ce3)
                ));



        when(countryDAO.saveAll(List.of(ce1, ce2,ce3))).thenReturn(List.of(ce1, ce2,ce3));

        List<Country> list = service.load(targetStream);

        verify(countryDAO,Mockito.times(3)).findByName(Mockito.anyString());

    }


}
