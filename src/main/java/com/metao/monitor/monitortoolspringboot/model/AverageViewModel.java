package com.metao.monitor.monitortoolspringboot.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Table(value = "average_view_model")
public class AverageViewModel implements Serializable {

    @NotBlank
    @Column("id")
    private Long timestamp; // primary key timesatmp

    @NotBlank
    @Column("window_size")
    private int windowSize;

    @NotBlank
    @Column("moving_average_response_time")
    private double movingAverageResponseTime;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AverageViewModel other = (AverageViewModel) obj;
        if (windowSize != other.windowSize) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }    
        if (movingAverageResponseTime != other.movingAverageResponseTime) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + windowSize;
        hash = 31 * hash + (int) (timestamp ^ (timestamp >>> 32));
        hash = 31 * hash + (int) (Double.doubleToLongBits(movingAverageResponseTime) ^ (Double.doubleToLongBits(movingAverageResponseTime) >>> 32));
        return hash;
    }
}
