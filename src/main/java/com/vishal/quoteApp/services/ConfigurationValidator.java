package com.vishal.quoteApp.services;

import com.vishal.quoteApp.models.PriceConfiguration;

public interface ConfigurationValidator {
    boolean isValid(PriceConfiguration priceConfiguration);
}
