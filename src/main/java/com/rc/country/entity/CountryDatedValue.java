package com.rc.country.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "country_time_value_index", columnList = "country_id,type,timeStamp DESC", unique = true)
})
@JsonIgnoreProperties("createdDate,lastModifiedDate")
public class CountryDatedValue extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

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

}
