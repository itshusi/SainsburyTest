package com.huseyin.sainsburys.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic POJO to hold information about totals for the JSON.
 */
public class TotalCost {

    @JsonProperty("gross")
    private double gross;
    @JsonProperty("vat")
    private double vat;

    TotalCost(double gross, double vat) {
        this.gross = gross;
        this.vat = vat;
    }

    public double getGross() {
        return gross;
    }

    public double getVat() {
        return vat;
    }

    public List<Item> getItems(Items items) {
        return items.items;
    }
}