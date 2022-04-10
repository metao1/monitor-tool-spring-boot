package com.metao.monitor.monitortoolspringboot.model.DataTransferObject;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AverageViewModelDTO implements Serializable {

    @JsonProperty("window_size")
    private int windowSize;

    @JsonProperty("moving_average_response_time")
    private double movingAverageResponseTime;

    @JsonProperty("timestamp")
    private long timestamp;


}
