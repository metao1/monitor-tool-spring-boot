package com.metao.monitor.monitortoolspringboot.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
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

    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column("url")
    String url;

    @Column("status")
    int status;

    @Column("response_time")
    long responseTime;

    @Column("created_at")
    @CreationTimestamp
    private Long createdAt;

    public ResponseData(String url, int status, long responseTime, long createdAt) {
        this.url = url;
        this.status = status;
        this.createdAt = createdAt;
        this.responseTime = responseTime;
    }
}
