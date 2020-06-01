package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.projectfinalwebscrappingbot.function.Elasticsearch;

public class Makro {
	private List<JSONObject> list = new ArrayList();
	
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
            Logger.getLogger(Elasticsearch.class.getName()).log(Level.SEVERE, null, ex);
        }

    	return elsValue;	
    }
    
    public String getMenuId(String category) {
    	String menuId = null ;
        switch(category) 
        { 
            case "ผักและผลไม้": 
            	menuId = "3874";
                break; 
            case "เนื้อสัตว์": 
            	menuId = "3896";
                break; 
            case "ปลาและอาหารทะเล": 
            	menuId = "4147";
                break; 
            case "นม เนย ไข่ และผลิตภัณฑ์แช่เย็น": 
            	menuId = "3353";
                break; 
            case "เบเกอรี่": 
            	menuId = "3803";
                break; 
            case "อาหารแช่แข็ง": 
            	menuId = "3932";
                break; 
            case "อาหารแห้ง": 
            	menuId = "2465";
                break; 
            case "เครื่องดื่มและขนมขบเคี้ยว": 
            	menuId = "2462";
                break; 
            case "อุปกรณ์และของใช้ในครัวเรือน": 
            	menuId = "2460";
                break; 
            case "ผลิตภัณฑ์ทำความสะอาด": 
            	menuId = "4112";
                break; 
            case "เครื่องเขียนและอุปกรณ์สำนักงาน": 
            	menuId = "2464";
                break; 
            case "เครื่องใช้ไฟฟ้า": 
            	menuId = "2461";
                break; 
            case "สุขภาพและความงาม": 
            	menuId = "2466";
                break; 
            case "สมาร์ทและไลฟ์สไตล์": 
            	menuId = "4056";
                break; 
            case "แม่และเด็ก": 
            	menuId = "2467";
                break; 
            case "ผลิตภัณฑ์สำหรับสัตว์เลี้ยง": 
            	menuId = "2468";
                break; 
            default: 
                System.out.println("no match"); 
        } 
    	return menuId;
    }
	
    public void getCategory(String url) throws IOException, InterruptedException {   
    	JSONObject json = new JSONObject();
    	try {
    		Document doc = Jsoup.connect(url)
		            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) snap Chromium/83.0.4103.61 Chrome/83.0.4103.61 Safari/537.36")
		            .timeout(60000)
		            .maxBodySize(0)
		            .get();//
            Elements eles = doc.select(".MenuCategoryPopOver__MenuListView-sc-77t7qb-2"); //.ref-menuDropdown.p-0
            for (Element ele : eles) {
            	json = new JSONObject();
	            String category = ele.select("p").html();
	            //System.out.println(category);
	            String menuId = this.getMenuId(category);
	            //String elsValue = this.makroApi(menuId, "1");
	            //System.out.println(elsValue);
	            json.put("category", category);
	            json.put("menuId", menuId);
	            this.list.add(json);
            }	
            System.out.println(this.list.toString());
    	}catch(Exception e) {
    		System.out.println("error => "+e.getMessage());
    	}
    }

	
	
    public static void main(String[] args) throws IOException, InterruptedException{
        String url = "https://www.makroclick.com/th";
    	//String url = "https://www.makroclick.com/th/category/vegetable-fruit";
        Makro m = new Makro();
        m.getCategory(url);
    }
}
