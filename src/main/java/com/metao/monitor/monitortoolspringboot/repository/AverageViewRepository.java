package com.metao.monitor.monitortoolspringboot.repository;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface AverageViewRepository extends R2dbcRepository<AverageViewModel, Long> {

    @Query("select * from average_view where window_size = :windowSize")
    public Flux<AverageViewModel> findByWindowSize(int windowSize);
}
