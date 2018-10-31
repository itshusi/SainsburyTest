package com.huseyin.sainsburys.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Items {
  
    List<Item> items = new ArrayList<Item>();
    public TotalCost totalCost;

      /**
       * Takes a list of items and calculates the relevant totals.
       * @param items The list of items
       */
      public Items(List<Item> items) {
          this.items = items;

          double gross = items.stream().map(Item::getUnitPrice).mapToDouble(Double::doubleValue).sum();
          double vat = gross * 0.2;

          this.totalCost = new TotalCost(gross, vat);
      }

      @Override
      public String toString() {
          ObjectMapper mapper = new ObjectMapper();
          HashMap<String, Object> itemsList = new LinkedHashMap<>(); // Linked map to preserve order in JSON output
          itemsList.put("items", itemsList);
          itemsList.put("totalCost", totalCost);
          try {
              return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
          } catch (IOException e) {
              return "JSON parse error";
          }
      }

      public List<Item> getItems() {
        return totalCost.getItems(this);
      }

      public double getGross() {
          return totalCost.getGross();
      }

      public double getVat() {
          return totalCost.getVat();
      }
  
 
}
