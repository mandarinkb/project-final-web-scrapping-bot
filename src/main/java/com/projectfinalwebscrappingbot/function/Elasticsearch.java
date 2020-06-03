package com.projectfinalwebscrappingbot.function;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Elasticsearch {
    @Value("${elasticsearch_ip}")
    private String elasticsearch_ip;  
    
    public void inputElasticsearch(String body,String index) {
        try {
            HttpResponse<String> response = Unirest.post("http://"+elasticsearch_ip+":9200/"+index+"/text")
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .body(body)
                    .asString();
        } catch (UnirestException ex) {
            Logger.getLogger(Elasticsearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteIndex(String index) {
        try {
            HttpResponse<String> response = Unirest.delete("http://"+elasticsearch_ip+":9200/"+index)
                    .header("Accept", "*/*")
                    .header("cache-control", "no-cache")
                    .asString();
        } catch (UnirestException ex) {
            Logger.getLogger(Elasticsearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String lazadaApi(String url) {
        String elsValue = null;
        try {
        	Unirest.setTimeouts(0, 0);
        	HttpResponse<String> response = Unirest.get(url + "?ajax=true")
        	  .header("Cookie", "JSESSIONID=DEB7EF01043BE33D0E4CD93A0029BB02")
        	  .asString();
            elsValue = response.getBody();
        } catch (UnirestException ex) {
            Logger.getLogger(Elasticsearch.class.getName()).log(Level.SEVERE, null, ex);
        }

    	return elsValue;	
    }
    
    public String makroApi(String menuId ,String page) {
        String elsValue = null;
        try {
        	Unirest.setTimeouts(0, 0);
        	HttpResponse<String> response = Unirest.post("https://ocs-prod-api.makroclick.com/next-product/public/api/product/search")
        	  .header("Content-Type", "application/json")
        	  .body("{\"locale\":\"th_TH\","
        	  		+ "\"minPrice\":null,"
        	  		+ "\"maxPrice\":null,"
        	  		+ "\"menuId\":"+menuId+","
        	  		+ "\"hierarchies\":[],"
        	  		+ "\"customerType\":\"MKC\","
        	  		+ "\"page\":"+page+","
        	  		+ "\"pageSize\":32,"
        	  		+ "\"sorting\":\"SORTING_LAST_UPDATE\","
        	  		+ "\"reloadPrice\":true}")
        	  .asString();

            elsValue = response.getBody();
        } catch (UnirestException ex) {
        	System.out.println(ex.getMessage());
        }

    	return elsValue;	
    }
}
