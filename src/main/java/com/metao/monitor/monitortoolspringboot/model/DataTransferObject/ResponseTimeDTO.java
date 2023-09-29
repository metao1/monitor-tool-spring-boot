package com.metao.monitor.monitortoolspringboot.model.DataTransferObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTimeDTO {
    
    @JsonProperty("url")
    private String url;

    @JsonProperty("status")
    private int status;

    @JsonProperty("response_time")
    private long responseTime;

    @JsonProperty("timestamp")  
    private long timestamp;

}
