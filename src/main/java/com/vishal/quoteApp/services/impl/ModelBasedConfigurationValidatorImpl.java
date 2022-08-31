package com.vishal.quoteApp.services.impl;

import com.vishal.quoteApp.services.ConfigurationValidator;
import com.vishal.quoteApp.models.PriceConfiguration;
import com.vishal.quoteApp.models.PriceModel;
import com.vishal.quoteApp.models.Tiers;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ModelBasedConfigurationValidatorImpl implements ConfigurationValidator
{
    private final ConfigurationValidator delegate;

    public ModelBasedConfigurationValidatorImpl(ConfigurationValidator delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isValid(PriceConfiguration priceConfiguration) {

        //  Graduated pricing must be the only pricing model in a configuration
        Set typesPresentOtherThanGraduated = new HashSet();

        for(Tiers t: priceConfiguration.getTiers()){
            if(t.getPriceModel() == PriceModel.GRADUATED && typesPresentOtherThanGraduated.size() > 0){
                log.error("GRADUATED pricing model is not allowed with any other type of model");
                return  false;
            } else if (t.getPriceModel() != PriceModel.GRADUATED ){
                typesPresentOtherThanGraduated.add(t.getPriceModel());
            }
        }
        return delegate.isValid(priceConfiguration);
    }
}
