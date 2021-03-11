package com.rc.country.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    @CsvBindByName(column = "Country")
    private String country;
    @CsvBindByName(column = "Alpha-2 code")
    private String alphaTwoCode;
    @CsvBindByName(column = "Alpha-3 code")
    private String alphaThreeCode;
    @CsvBindByName(column = "Numeric")
    private int numeric;
}
