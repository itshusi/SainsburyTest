package com.huseyin.sainsburys.scraper;
import com.huseyin.sainsburys.model.Items;
import com.huseyin.sainsburys.model.Item;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of static methods that carry out the actual scraping
 */
public class ScrapingUtils {

    /**
     *
     * @param s A string containing one or more doubles
     * @return The first double in the string
     */
    private static Double getDoubleFromString(String s) {
        s = s.split(" ")[0];
        String cleanString = s.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleanString);
    }

    /**
     * Take an HTML element and create a Product object. Uses the link to fetch other information.
     * @param product The HTML element of the product
     * @param baseUrl The base url, used to calculate the hyperlink for extra information.
     * @return A Product object.
     * @throws IOException If the hyperlink cannot be accessed throws exception.
     */
    private static Item processItemElement(Element product, String baseUrl) throws IOException {
        Element linkTo = product.selectFirst("a");
        String title = product.selectFirst("a").text();
        String unitPriceString = product.selectFirst("p.pricePerUnit").text();
        Double unitPrice = getDoubleFromString(unitPriceString);

        String extraInfoLink = linkTo.attr("href");
        // Go to details page and get more information
        extraInfoLink = (new URL( new URL(baseUrl), extraInfoLink)).toString();
        Document detailSoup = Jsoup.connect(extraInfoLink).get();
        Element infoElement  = detailSoup.getElementById("information");
        Element productText = infoElement.selectFirst(".productText");
        String description = "";
        for (Element e : productText.getElementsByTag("p") ) {
            if (e.text().length() > 0) {
                description = e.text();
                break;
            }
        }

        Element nutritionTable = detailSoup.selectFirst(".nutritionTable");
        Double kCalPer100g = null;
        if (nutritionTable != null) {
            Element calorieCell = nutritionTable.selectFirst("table > tbody > tr:nth-child(2) > td:nth-child(1)");
            if (calorieCell != null) {
                String calorieString = calorieCell.text();
                kCalPer100g = getDoubleFromString(calorieString);
            }
        }
        
        return new Item(title, unitPrice, kCalPer100g, description );
    }

    /**
     * Connects to the base url and constructs a Items object
     * @param url The base url to scrape
     * @return The Items of the scrape
     * @throws IOException If the URL or any child URLs are not valid will throw an exception
     */
    public static Items scrapeItemsFromUrl(String url) throws IOException {
        List<Item> itemsList = new ArrayList<>();
        Document soup = Jsoup.connect(url).get();
        Elements items = soup.getElementsByClass("product");

        for(Element itemElement : items) {
            itemsList.add( processItemElement(itemElement, url) );
        }
        return new Items(itemsList);
    }
}