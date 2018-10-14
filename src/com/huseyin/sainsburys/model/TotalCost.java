package com.huseyin.sainsburys.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Basic POJO to hold information about totals for the JSON.
 */
class TotalCost {

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
}