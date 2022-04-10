package com.metao.monitor.monitortoolspringboot.repository;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import reactor.test.StepVerifier;

@DataR2dbcTest
public class DataResponseRepositoryTest {

        @Autowired
        DataResponseRepositroy repository;

        @Autowired
        R2dbcEntityTemplate template;

        @BeforeEach
        public void setup() {
                this.template.delete(ResponseData.class).all().block(Duration.ofSeconds(5));
        }

        @Test
        void testFindAll() {
                var data = IntStream.range(1, 101)
                                .mapToObj(i -> new ResponseData("http://httpstat.us/200", 200, 10,
                                                Instant.now().toEpochMilli()))
                                .toList();
                repository.saveAll(data)
                                .log()
                                .then()
                                .thenMany(this.repository.findByUrl("%httpstat%", 10, 0))
                                .log()
                                .as(StepVerifier::create)
                                .expectNextCount(10)
                                .verifyComplete();
        }

        @Test
        void testFindByUrlContains() {
                var data = IntStream.rangeClosed(1, 10)
                                .mapToObj(i -> new ResponseData("http://httpstat.us/200", 200, 10,
                                                Instant.now().toEpochMilli()))
                                .toList();

                var many = repository.saveAll(data);

                many.then()
                                .thenMany(this.repository.findAllWithLimit(1))
                                .as(StepVerifier::create)
                                .expectNextCount(1)
                                .verifyComplete();

                many.then()
                                .thenMany(this.repository.findAllWithLimit(10))
                                .as(StepVerifier::create)
                                .expectNextCount(10)
                                .verifyComplete();
        }
}
