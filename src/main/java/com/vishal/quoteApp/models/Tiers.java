package com.vishal.quoteApp.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@EqualsAndHashCode
public class Tiers {

    InnerRange range;
    int value;
    Currency currency;
    PriceModel priceModel;

    @Value
    @ToString
    @AllArgsConstructor
    public static class InnerRange {

        int from;
        int to;
    }

    public int getTo(){
        return range.getTo();
    }

    public int getFrom(){
        return range.getFrom();
    }

    public boolean isBetween(int quantity) {
        return getFrom() <= quantity && quantity<= getTo();
    }

    public boolean hasAtleast(int quantity) {
        return getFrom() <= quantity;
    }

//    @Override
//    public int compareTo(Tiers u) {
//
//        int from_res = new Integer(getRange().from).compareTo(new Integer(u.getRange().from));
//
//        if(from_res != 0) {
//            return from_res;
//        }
//
//        return new Integer(getRange().from).compareTo(new Integer(u.getRange().from));
//    }

}


