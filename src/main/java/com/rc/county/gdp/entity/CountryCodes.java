package com.rc.county.gdp.entity;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "country_code_types_index", columnList = "type,code", unique = true)
})
public class CountryCodes {

    @Id
    private Long id;

    private String type;

    private String code;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
