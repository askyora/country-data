package com.rc.country.entity;

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
        @Index(name = "country_code_types_index", columnList = "type,code,country_id", unique = true)
})
@JsonIgnoreProperties("country")
public class CountryCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private CountryCodesType type;

    private String code;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
