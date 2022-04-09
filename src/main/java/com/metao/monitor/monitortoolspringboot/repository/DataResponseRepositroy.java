package com.metao.monitor.monitortoolspringboot.repository;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface DataResponseRepositroy extends R2dbcRepository<ResponseData, Long> {

    @Query("select * from response_data where url like :url")
    public Flux<ResponseData> findByUrlContains(String url);

    @Query("select * from response_data LIMIT :limit")
    public Flux<ResponseData> findAllWithLimit(int limit);

}
