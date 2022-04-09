package com.metao.monitor.monitortoolspringboot.model;

import java.io.Serializable;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Table(value = "average_view_model")
public class AverageViewModel implements Serializable {
 
    @Column("id")
    private Long id;

    @Column("window_size")
    private int windowSize;

    @Column("moving_average_response_time")
    private double movingAverageResponseTime;

}
