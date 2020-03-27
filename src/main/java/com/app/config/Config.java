package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "apputils.dirdel")
@Component
public class Config {

    private String folder;

    private String keyword;

    private String negaword;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getNegaword() {
        return negaword;
    }

    public void setNegaword(String negaword) {
        this.negaword = negaword;
    }

}