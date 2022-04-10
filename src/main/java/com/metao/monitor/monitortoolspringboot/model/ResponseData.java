package com.metao.monitor.monitortoolspringboot.model;

import java.io.Serializable;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Table(value = "response_data")
public class ResponseData implements Serializable {

    @Column("url")
    String url;

    @Column("status")
    int status;

    @Column("response_time")
    long responseTime;

    @Column("timestamp")    
    private Long timestamp;

    public ResponseData(String url, int status, long responseTime, long timestamp) {
        this.url = url;
        this.status = status;
        this.timestamp = timestamp;
        this.responseTime = responseTime;
    }
}
