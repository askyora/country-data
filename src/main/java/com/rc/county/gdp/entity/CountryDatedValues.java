package com.rc.county.gdp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "country_time_value_index", columnList = "country_id,type,timeStamp DESC", unique = true)
})
public class CountryDatedValues {

    @Id
    private Long id;

    @Column
    private Date timeStamp;

    @Column
    private String type;

    @Column
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
