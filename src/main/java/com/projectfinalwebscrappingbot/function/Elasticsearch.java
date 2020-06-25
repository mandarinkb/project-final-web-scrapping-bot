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
            HttpResponse<String> response = Unirest.post(elasticsearch_ip+index+"/text")
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
            HttpResponse<String> response = Unirest.delete(elasticsearch_ip+index)
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
    
    public String bigCApi(String cateId ,String page) {
    	String elsValue = null;
    	try {
    		Unirest.setTimeouts(0, 0);
    		HttpResponse<String> response = Unirest.post("https://www.bigc.co.th/api/categories/getproductListBycateId?_store=2&stock_id=1")
    		  .header("Content-Type", "application/json")
    		  .header("Cookie", "__cfduid=d2a52d4a4656e2822d5fa36c91c494f2c1590843646; __cfruid=2cc422a0cc7785dccdce2bc2e0546577f16d6f00-1591341055")
    		  .body("{\"cate_id\": \""+cateId+"\","
    		  		+ "\"ignore_items\": \"\","
    		  		+ "\"page_no\": "+page+","
    		  		+ "\"page_size\": 36,"
    		  		+ "\"selected_categories\": \"\","
    		  		+ "\"selected_brands\": \"\","
    		  		+ "\"sort_by\": \"bestsellers:desc\","
    		  		+ "\"price_from\": \"\","
    		  		+ "\"price_to\": \"\","
    		  		+ "\"filter\": [],"
    		  		+ "\"stock_id\": 1}")
    		  .asString();

    		elsValue = response.getBody();
    	}catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }
    	
    	return elsValue;
    }
}
