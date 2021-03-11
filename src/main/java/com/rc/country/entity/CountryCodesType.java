package com.rc.country.entity;

import org.apache.commons.lang3.StringUtils;

public enum CountryCodesType {
    ALPHA3,ALPHA2,NUMARIC;

    public static CountryCodesType getTypeByValue(String value){

        if (StringUtils.isNumeric(value)){
            return NUMARIC;
        }
        if(StringUtils.length(value)==2){
            return ALPHA2;
        }
        if(StringUtils.length(value)==3){
            return ALPHA3;
        }
        throw new RuntimeException("Unsupported Code : "+value);
    }
}
