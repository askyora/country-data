package com.rc.country.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GdpDTO {

    @CsvBindByName(column = "Country Name")
    private String country;
    @CsvBindByName(column = "Country Code")
    private String countryCode;
    @CsvBindByName(column = "Year")
    private int year;
    @CsvBindByName(column = "Value")
    private BigDecimal value;
}
