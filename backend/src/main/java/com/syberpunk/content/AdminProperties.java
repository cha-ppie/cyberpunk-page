package com.syberpunk.content;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("app.admin")
public record AdminProperties(@DefaultValue("") String username, @DefaultValue("") String password) {}
