package com.huseyin.sainsburys.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.*;

import com.huseyin.sainsburys.scraper.ScraperUtils;


@Path("/items")
public class RetrieveItemsRest {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/all")
  public String getItemsJson() throws IOException, JAXBException {
    String items = ScraperUtils.scrapeItemsFromUrl(url).toString();
    return items;
  }

}

