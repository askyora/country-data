package com.yora.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "country_time_value_index", columnList = "country_id,type,timeStamp DESC", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties("createdDate,lastModifiedDate")
public class CountryDatedValue extends BaseObject {

    @Column
    private Date timeStamp;
    @Enumerated(EnumType.ORDINAL)
    @Column
    private CountryValueTypes type;
    @Column
    private BigDecimal value;
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public CountryDatedValue() {
    }

    public CountryDatedValue(Date timeStamp, CountryValueTypes type, BigDecimal value, Country country) {
        this.timeStamp = timeStamp;
        this.type = type;
        this.value = value;
        this.country = country;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public CountryValueTypes getType() {
        return type;
    }

    public void setType(CountryValueTypes type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
        CountryDatedValue other = (CountryDatedValue) obj;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
