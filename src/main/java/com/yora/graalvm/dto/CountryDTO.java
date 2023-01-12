package com.yora.graalvm.dto;

import com.opencsv.bean.CsvBindByName;

public class CountryDTO extends BaseDto {

    @CsvBindByName(column = "Country")
    private String country;
    @CsvBindByName(column = "Alpha-2 code")
    private String alphaTwoCode;
    @CsvBindByName(column = "Alpha-3 code")
    private String alphaThreeCode;
    @CsvBindByName(column = "Numeric")
    private int numeric;

    public CountryDTO() {
        super();
    }

    public CountryDTO(String country, String alphaTwoCode, String alphaThreeCode, int numeric) {
        this.country = country;
        this.alphaTwoCode = alphaTwoCode;
        this.alphaThreeCode = alphaThreeCode;
        this.numeric = numeric;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAlphaTwoCode() {
        return alphaTwoCode;
    }

    public void setAlphaTwoCode(String alphaTwoCode) {
        this.alphaTwoCode = alphaTwoCode;
    }

    public String getAlphaThreeCode() {
        return alphaThreeCode;
    }

    public void setAlphaThreeCode(String alphaThreeCode) {
        this.alphaThreeCode = alphaThreeCode;
    }

    public int getNumeric() {
        return numeric;
    }

    public void setNumeric(int numeric) {
        this.numeric = numeric;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alphaThreeCode == null) ? 0 : alphaThreeCode.hashCode());
        result = prime * result + ((alphaTwoCode == null) ? 0 : alphaTwoCode.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + numeric;
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
        CountryDTO other = (CountryDTO) obj;
        if (alphaThreeCode == null) {
            if (other.alphaThreeCode != null)
                return false;
        } else if (!alphaThreeCode.equals(other.alphaThreeCode))
            return false;
        if (alphaTwoCode == null) {
            if (other.alphaTwoCode != null)
                return false;
        } else if (!alphaTwoCode.equals(other.alphaTwoCode))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (numeric != other.numeric)
            return false;
        return true;
    }


}
