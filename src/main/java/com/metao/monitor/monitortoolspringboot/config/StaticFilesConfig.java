package com.metao.monitor.monitortoolspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class StaticFilesConfig {
    
    @Bean
    RouterFunction<ServerResponse> staticResourceRouter(){
        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
    }

}
