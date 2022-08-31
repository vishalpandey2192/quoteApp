package com.vishal.quoteApp.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class PriceConfiguration {
    String productId;
    List<Tiers> tiers;
}
