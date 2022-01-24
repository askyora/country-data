package com.yora.data.dto;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;


public class GdpDTO extends BaseDto {

    @CsvBindByName(column = "Country Name")
    private String country;
    @CsvBindByName(column = "Country Code")
    private String countryCode;
    @CsvBindByName(column = "Year")
    private int year;
    @CsvBindByName(column = "Value")
    private BigDecimal value;

    public GdpDTO() {
        super();
    }

    public GdpDTO(String country, String countryCode, int year, BigDecimal value) {
        this();
        this.country = country;
        this.countryCode = countryCode;
        this.year = year;
        this.value = value;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GdpDTO other = (GdpDTO) obj;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (countryCode == null) {
            if (other.countryCode != null)
                return false;
        } else if (!countryCode.equals(other.countryCode))
            return false;
        if (year != other.year)
            return false;
        return true;
    }

}