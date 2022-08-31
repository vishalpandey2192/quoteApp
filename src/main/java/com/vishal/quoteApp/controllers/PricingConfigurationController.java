package com.vishal.quoteApp.controllers;

import com.vishal.quoteApp.models.PriceConfiguration;
import com.vishal.quoteApp.models.PriceModel;
import com.vishal.quoteApp.models.Tiers;
import com.vishal.quoteApp.services.ConfigurationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
class PricingConfigurationController {

    private final ConfigurationValidator validator;


    private Map<String, List<Tiers>> repository;

    PricingConfigurationController(ConfigurationValidator validator) {
        this.validator = validator;
        this.repository = new HashMap<>();
    }

    @GetMapping("/configuration")
    ResponseEntity all() {
        return ResponseEntity.ok(repository);
    }

    @PostMapping("/configuration")
    ResponseEntity newConfiguration(@RequestBody PriceConfiguration priceConfiguration) {


        // validate resp
        if(!validator.isValid(priceConfiguration)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Value provided");
        }

        // Create a stream...
        var sortedList = priceConfiguration.getTiers()
                .stream()
                // sort it (does not sort the original list)...
                .sorted(Comparator.comparing(Tiers::getTo)
                        .thenComparing(Tiers::getFrom))
                // and collect to a new list
                .collect(Collectors.toList());

        priceConfiguration.setTiers(sortedList);

        // Add to database
        repository.put(priceConfiguration.getProductId(), sortedList);
        log.error(priceConfiguration.toString());
        return ResponseEntity.ok(repository);
    }

    @GetMapping("/api/price")
    ResponseEntity getCost(@RequestParam("product") String productId, @RequestParam("quantity") int quantity) {

        if(!repository.containsKey(productId)){
            return ResponseEntity.badRequest().body("ProductId not found");
        }

        List<Tiers> tiers = repository.get(productId);

        // if any of is gradual means gradual
        if(tiers.get(0).getPriceModel() == PriceModel.GRADUATED) {
            final int[] availableQuanity = {quantity};
            final int[] finalValue = {0};

            tiers.stream()
                    // since its sorted
                    .filter(tier -> tier.hasAtleast(availableQuanity[0]))
                    .map(tier -> {
                        int curr = 0;

                        // get remaining
                        if(availableQuanity[0] > tier.getFrom()){
                            availableQuanity[0] = availableQuanity[0] - tier.getFrom();
                            curr = tier.getFrom();
                        } else {
                            // all will be covered by this segment
                            curr = availableQuanity[0];
                            availableQuanity[0] = 0;
                        }

                        // Add cost
                        finalValue[0] += (curr * tier.getValue());
                        return tier;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(finalValue);
        }

        // If type is simple FLAT or VOLUME,
        Optional<Integer> result = tiers
                .stream()
                .filter(tier -> tier.isBetween(quantity))
                .map(
                        tier -> {
                            if (tier.getPriceModel() == PriceModel.FLAT) return tier.getValue();
                            else return quantity * tier.getValue();
                        }
                )
                // Since only one configuration will be valid
                .findFirst();

        if (result.isEmpty()) {
            return ResponseEntity.ok().body("No eligible tier for given product");
        }

        return ResponseEntity.ok(result.get());

    }



}