package com.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "apputils.dirdel")
@Component
@Data
public class Config {

    private String folder;

    private String keyword;

    private String negaword;

}