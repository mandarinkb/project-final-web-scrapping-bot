package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Makro {
	public String icon =  "https://www.makroclick.com/static/images/logo.png";
    public String urlDetail = "https://www.makroclick.com/th/products/";
	private List<JSONObject> list = new ArrayList();
	
    public String changeCategory(String category) {
        String elsValue = null;
        try {
        	Unirest.setTimeouts(0, 0);
        	HttpResponse<String> response = Unirest.post("http://127.0.0.1:9200/web_scrapping_categories/_search")
        	  .header("Content-Type", "application/json")
        	  .body("{\"query\": {\"bool\": {\"must\": {\"match_phrase\": {\"tag\": \""+category+"\"}}}}}")
        	  .asString();

            elsValue = response.getBody();
        } catch (UnirestException ex) {
            //Logger.getLogger(Elasticsearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    	
        JSONObject objResultsValue;
        String newCategory = null;
		try {
			objResultsValue = new JSONObject(elsValue);
	        JSONObject objHits = objResultsValue.getJSONObject("hits");
	        JSONArray arrHits = objHits.getJSONArray("hits");

	        
	        for (int i = 0; i < arrHits.length(); i++) {
	            JSONObject objSource = arrHits.getJSONObject(i).getJSONObject("_source");	            
	            newCategory = objSource.getString("category");
	        }
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return newCategory;	
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
	
    public String getCategory(String url){   
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
	            String menuId = this.getMenuId(category);
	            String newCategory = this.changeCategory(category);

	            System.out.println(category);
	            System.out.println("change => "+newCategory);
	            
	            
	            json.put("category", category);
	            json.put("menuId", menuId);
	            this.list.add(json);
            }	
    	}catch(Exception e) {
    		System.out.println("error => "+e.getMessage());
    	}
    	return this.list.toString();
    }
    
    public void getContent(String listStr) {
    	try {
			JSONArray arr = new JSONArray(listStr);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String category = obj.getString("category");
				String menuId = obj.getString("menuId");
				String elas = this.makroApi(menuId, "1");
				int total = this.totalPage(elas); // หา page ทั้งหมดก่อน
				//System.out.println(category);
				//System.out.println(total);
				
				for(int j = 1; j < total; j++) {
					String elasValue = this.makroApi(menuId, Integer.toString(j));
					//System.out.println(elasValue);
					JSONObject objValue = new JSONObject(elasValue);
					JSONArray arrContent = objValue.getJSONArray("content");
					for (int k = 0; k < arrContent.length(); k++) {
						JSONObject objItems = arrContent.getJSONObject(k);
						Double originalPrice = objItems.getDouble("inVatPrice");
						Double price = objItems.getDouble("inVatSpecialPrice");
						
						//เก็บเฉพาะที่มีส่วนลด
						if(!originalPrice.equals(price)) {
							String image = objItems.getString("image");
							String name = objItems.getString("productName");
							// category ;
							String productUrl = this.urlDetail + objItems.getString("productCode");
							//String icon =  https://www.makroclick.com/static/images/logo.png;
							//price
							//originalPrice
				            double discount = (((originalPrice - price) / originalPrice) * 100);  // หา % ของส่วนลด
				            DecimalFormat df = new DecimalFormat("#"); // #.# แปลงทศนิยม 1 ตำแหน่ง
				            discount = Double.parseDouble(df.format(discount));
				            //webName
				            
				            System.out.println(image);
				            System.out.println(name);
				            System.out.println(category);
				            System.out.println(productUrl);
				            System.out.println(this.icon);
				            System.out.println(price);
				            System.out.println(originalPrice);
				            System.out.println(discount);
				            System.out.println();
							
						}
						
					}
				}

			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    public int totalPage(String elasValue) {
    	int total = 0;
    	try {
			JSONObject json = new JSONObject(elasValue);
			total = json.getInt("totalPages"); // แปลงเป็น String ในตัว
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return total;
    }

	
	
    public static void main(String[] args) throws IOException, InterruptedException{
        String url = "https://www.makroclick.com/th";
        Makro m = new Makro();
        m.getCategory(url);
        //m.getContent(m.list.toString());
    }
}
