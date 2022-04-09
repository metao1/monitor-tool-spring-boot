package com.metao.monitor.monitortoolspringboot.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(value= "classpath:config.yml",factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "monitor")
public class MovingAverageConfig {
    
    private int interval;
    private String url;
    private List<Integer> windowSizes;

}
