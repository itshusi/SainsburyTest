package com.huseyin.sainsburys.scraper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.huseyin.sainsburys.model.Items;

/**
 * Class to hold the main method
 */
public class Scraper {

    private static final Logger LOGGER = Logger.getLogger( ScraperUtils.class.getName() );

    public static void main(String[] args) {
        String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        try {
            Items items = ScraperUtils.scrapeItemsFromUrl(url);
            System.out.println(items);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read from url: " + url);
        }
    }
}