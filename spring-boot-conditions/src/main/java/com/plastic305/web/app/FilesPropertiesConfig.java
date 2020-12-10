package com.plastic305.web.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({ @PropertySource("classpath:messages.properties")
                  /* Separados por , pueden ir otros ficheros */})
public class FilesPropertiesConfig {

}
