import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.huseyin.sainsburys.model.Item;
import com.huseyin.sainsburys.model.Items;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.huseyin.sainsburys.scraper.ScrapingUtils;

import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class ScrapingUtilsTest {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());
  
    private Optional<Items> getItemsFromStub(String responseBody, String extraInfoBody) {
        stubFor(get(urlEqualTo("/test_resource"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(responseBody)));

        stubFor(get(urlEqualTo("/extra"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(extraInfoBody)));

        int port = wireMockRule.port();

        try {
            return Optional.of( ScrapingUtils.scrapeItemsFromUrl("http://localhost:" + port + "/test_resource") );
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Test
    public void testEmpty() {
        String blank = "";
        Optional<Items> items = getItemsFromStub(blank, blank);
        Assert.assertTrue( items.isPresent() );
        String expectedResponse = "[ ]";
        Assert.assertEquals(expectedResponse, items.get().toString());
    }

    @Test
    public void testOneItem() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Yoghurt</a>" +
                "            <p class=\"pricePerUnit\">£2.50</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <div class=\"productText\">" +
                "                   <p>A yoghurt</p>" +
                "                   <p>Information to ignore</p>" +
                "               </div>" +
                "           </div>" +
                "    </body>" +
                "</html>";

        Optional<Items> items = getItemsFromStub(response, extraInfo);
        Assert.assertTrue( items.isPresent() );
        Assert.assertEquals(1, items.get().totalCost.getItems(items.get()).size());
        Item item = items.get().totalCost.getItems(items.get()).get(0);
        Assert.assertEquals("Yoghurt", item.getTitle());
        Assert.assertEquals(2.50, item.getUnitPrice(), 0.0001);
        Assert.assertEquals("A yoghurt", item.getDescription());
        Assert.assertNull(item.getKcalPer100g());

        Assert.assertEquals(2.50, items.get().getGross(), 0.0001 );
        Assert.assertEquals(2.50 * 0.2, items.get().getVat(), 0.0001 );
    }

    @Test
    public void testItemtWithCalories() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Apples</a>" +
                "            <p class=\"pricePerUnit\">£5.50</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <p class=\"productText\">Some apples</p>" +
                "           </div>" +
                "                <table class=\"nutritionTable\">" +
                "                        <thead>" +
                "                        <tr class=\"tableTitleRow\">" +
                "                        <th scope=\"col\">Per 100g</th><th scope=\"col\">Per 100g&nbsp;</th><th scope=\"col\">% based on RI for Average Adult</th>" +
                "                        </tr>" +
                "                        </thead>" +
                "                        <tbody><tr class=\"tableRow1\">" +
                "                        <th scope=\"row\" class=\"rowHeader\" rowspan=\"2\">Energy</th><td class=\"tableRow1\">250kJ</td><td class=\"tableRow1\">-</td>" +
                "                        </tr>" +
                "                        <tr class=\"tableRow0\">" +
                "                        <td class=\"tableRow0\">22kcal</td><td class=\"tableRow0\">2%</td>" +
                "                        </tr>" +
                "                        </tbody>" +
                "                    </table>" + 
                "    </body>" +
                "</html>";

        Optional<Items> items = getItemsFromStub(response, extraInfo);
        Assert.assertTrue( items.isPresent() );
        Assert.assertEquals(1, items.get().getItems().size());
        Item item = items.get().getItems().get(0);
        Assert.assertEquals("Apples", item.getTitle());
        Assert.assertEquals(5.50, item.getUnitPrice(), 0.0001);
        Assert.assertEquals("Some apples", item.getDescription());
        Assert.assertEquals(22, item.getKcalPer100g(), 0.0001);

        Assert.assertEquals(5.50, items.get().getGross(), 0.0001 );
        Assert.assertEquals(5.50 * 0.2, items.get().getVat(), 0.0001 );
    }

    @Test
    public void testManyItems() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Lettuce</a>" +
                "            <p class=\"pricePerUnit\">£1.50</p>" +
                "        </div>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Bread</a>" +
                "            <p class=\"pricePerUnit\">£10.50</p>" +
                "        </div>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Cheese</a>" +
                "            <p class=\"pricePerUnit\">£2.50</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <p class=\"productText\">Extra information</p>" +
                "           </div>" +
                "    </body>" +
                "</html>";

        Optional<Items> items = getItemsFromStub(response, extraInfo);
        Assert.assertTrue( items.isPresent() );
        Assert.assertEquals(3, items.get().getItems().size());

        String[] titles = { "Lettuce", "Bread", "Cheese" };
        double[] prices = { 1.50, 10.50, 2.50 };

        for(int i=0; i < items.get().getItems().size(); ++i) {
            Item item = items.get().getItems().get(i);
            Assert.assertEquals(titles[i], item.getTitle());
            Assert.assertEquals(prices[i], item.getUnitPrice(), 0.0001);
            Assert.assertEquals("Extra information", item.getDescription());
            Assert.assertNull(item.getKcalPer100g());
        }

        Assert.assertEquals(14.50, items.get().getGross(), 0.0001 );
        Assert.assertEquals(14.50 * 0.2, items.get().getVat(), 0.0001 );
    }

}