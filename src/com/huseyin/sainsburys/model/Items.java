package com.huseyin.sainsburys.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

public class Items {
  
    private List<Item> items = new ArrayList<Item>();
    private TotalCost totalCost;

      /**
       * Takes a list of products and calculates the relevant totals.
       * @param products The list of products
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
          HashMap<String, Object> results = new LinkedHashMap<>(); // Linked map to preserve order in JSON output
          results.put("results", items);
          results.put("totalCost", totalCost);
          try {
              return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
          } catch (IOException e) {
              return "JSON parse error";
          }
      }

      public List<Item> getProducts() {
          return items;
      }

      public double getGross() {
          return totalCost.getGross();
      }

      public double getVat() {
          return totalCost.getVat();
      }
  
 
}
