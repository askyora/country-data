package com.rc.country.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(indexes = {
        @Index(name = "type_code_index", columnList = "type,code", unique = false),
        @Index(name = "country_code_types_index", columnList = "type,code,country_id", unique = true)
})
@JsonIgnoreProperties("country,createdDate,lastModifiedDate")
public class CountryCode extends BaseObject {

    @Enumerated(EnumType.ORDINAL)
    private CountryCodeType type;

    private String code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
