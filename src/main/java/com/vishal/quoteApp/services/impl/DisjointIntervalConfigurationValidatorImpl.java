package com.vishal.quoteApp.services.impl;

import com.vishal.quoteApp.models.Tiers;
import com.vishal.quoteApp.services.ConfigurationValidator;
import com.vishal.quoteApp.models.PriceConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DisjointIntervalConfigurationValidatorImpl implements ConfigurationValidator {
    @Override
    public boolean isValid(PriceConfiguration priceConfiguration) {
        // In the sorted array, if start time of an interval
        // is less than end of previous interval, then there
        // is an overlap
        List<Tiers> arr = priceConfiguration.getTiers();
        for(int i = 1; i < arr.size(); i++)
            if (arr.get(i-1).getTo() > arr.get(i).getFrom()){
                log.error("Overlapping intervals are not allowed");
                return false;
            }

        // If we reach here, then no overlap
        return true;
    }
}
