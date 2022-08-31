package com.vishal.quoteApp.config;

import com.vishal.quoteApp.services.ConfigurationValidator;
import com.vishal.quoteApp.services.impl.ModelBasedConfigurationValidatorImpl;
import com.vishal.quoteApp.services.impl.DisjointIntervalConfigurationValidatorImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigurationValidatorConfig {

    @Bean("valueBasedValidation")
    public ConfigurationValidator valueBasedValidation() {
        return new DisjointIntervalConfigurationValidatorImpl();
    }

    @Bean
    @Primary
    public ConfigurationValidator modelBasedValidation(@Qualifier("valueBasedValidation") ConfigurationValidator valueBasedValidation) {
        return new ModelBasedConfigurationValidatorImpl(valueBasedValidation);
    }

}