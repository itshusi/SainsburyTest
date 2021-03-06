package com.huseyin.sainsburys.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Item {
  private String title;
  private Double unitPrice;
  @JsonInclude(Include.NON_NULL)
  private Double kcalPer100g;
  private String description;


  public Item(String title) {
    this.title = title;
  }
  public Item() {}
  
  public Item(String title, Double unitPrice, Double kcalPer100g, String description) {
    super();
    this.title = title;
    this.unitPrice = unitPrice;
    this.kcalPer100g = kcalPer100g;
    this.description = description;
  }
  
  public Item(String title, Double unitPrice, String description) {
    super();
    this.title = title;
    this.unitPrice = unitPrice;
    this.description = description;
  }
  
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public Double getUnitPrice() {
    return unitPrice;
  }
  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }
  public Double getKcalPer100g() {
    return kcalPer100g;
  }
  public void setKcalPer100g(Double kcalPer100g) {
    this.kcalPer100g = kcalPer100g;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  @Override
  public String toString() {
    return "Item [title=" + title + ", unitPrice=" + unitPrice + ", kcalPer100g=" + kcalPer100g + ", description="
        + description + "]";
  }
}
