package com.yora.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "type_code_index", columnList = "type,code", unique = true),
        @Index(name = "country_code_types_index", columnList = "type,code,country_id", unique = true),
        @Index(name = "country_code_index", columnList = "code", unique = false)
})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties("country,createdDate,lastModifiedDate")
public class CountryCode extends BaseObject {

    @Enumerated(EnumType.ORDINAL)
    private CountryCodeType type;

    private String code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public CountryCode() {
    }

    public CountryCode(CountryCodeType type, String code, Country country) {
        this.type = type;
        this.code = code;
        this.country = country;
    }

    public CountryCodeType getType() {
        return type;
    }

    public void setType(CountryCodeType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        CountryCode other = (CountryCode) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
