package com.rc.country.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(indexes = {
        @Index(name = "country_time_value_index", columnList = "country_id,type,timeStamp DESC", unique = true)
})
@EqualsAndHashCode(of = {"timeStamp", "type", "country"})
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

}
