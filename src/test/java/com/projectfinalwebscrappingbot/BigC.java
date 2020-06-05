package com.projectfinalwebscrappingbot;

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

public class BigC {
	public List<JSONObject> list = new ArrayList<>();
    public String getCateId(String category) {
    	String cate_id = null ;
        switch(category) 
        { 
            case "สินค้ารับเปิดเทอม": 
            	cate_id = "9590";
                break; 
            case "สินค้าบริการส่งด่วน": 
            	cate_id = "7680";
                break; 
            case "อาหารสด": 
            	cate_id = "2062";
                break; 
            case "อาหารแห้ง": 
            	cate_id = "3";
                break; 
            case "เครื่องดื่ม": 
            	cate_id = "60";
                break; 
            case "ขนมขบเคี้ยวและลูกอม": 
            	cate_id = "108";
                break; 
            case "สุขภาพและความงาม": 
            	cate_id = "143";
                break; 
            case "แม่และเด็ก": 
            	cate_id = "191";
                break; 
            case "ของใช้ในครัวเรือน": 
            	cate_id = "233";
                break; 
            case "เครื่องใช้ไฟฟ้า และอิเล็กทรอนิกส์": 
            	cate_id = "299";
                break; 
            case "เครื่องเขียน และอุปกรณ์สำนักงาน": 
            	cate_id = "344";
                break; 
            case "เครื่องแต่งกาย": 
            	cate_id = "7348";
                break; 
            case "อุปกรณ์กีฬา/ แคมปิ้ง/ เดินทาง": 
            	cate_id = "7383";
                break; 
            case "ยานยนต์": 
            	cate_id = "7384";
                break; 
            case "สัตว์เลี้ยง/ สินค้าเทศกาล": 
            	cate_id = "383";
                break; 
            case "ร้าน Pure": 
            	cate_id = "6716";
                break; 
            case "สินค้าแบรนด์เบสิโค": 
            	cate_id = "6791";
                break; 
            default: 
                System.out.println("no match"); 
        } 
    	return cate_id;
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
    
    public int lastPage(String elasValue) {
    	int last = 0;
    	try {
			JSONObject json = new JSONObject(elasValue);
			JSONObject result = json.getJSONObject("result");
			last = result.getInt("lastPage");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

    	return last;
    }
    public void getCategory(String url){   
    	JSONObject json = new JSONObject();
    	try {
    		Document doc = Jsoup.connect(url)
		            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) snap Chromium/83.0.4103.61 Chrome/83.0.4103.61 Safari/537.36")
		            .timeout(60000)
		            .maxBodySize(0)
		            .get();//
            Elements eles = doc.select(".swiper-wrapper");
            for (Element ele : eles.select("a")) {
            	json = new JSONObject();
            	String category = ele.text();
            	//get cate_id
            	String cateId = this.getCateId(category);
            	json.put("category", category);
            	json.put("cateId", cateId);
            	list.add(json);
            	// push ลง redis 
            }
    	}catch(Exception e) {
    		System.out.println("error => "+e.getMessage());
    	}
    }
    public void getContent(String listStr) {
    	String baseUrl = "https://www.bigc.co.th/";
     	try {
			JSONArray arr = new JSONArray(listStr);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String category = obj.getString("category");
				String cateId = obj.getString("cateId");
            	//call bigCapi
            	String elasValue = this.bigCApi(cateId, "1");
            	//get last page
                int lastPage = this.lastPage(elasValue);
                //ตัด สินค้าบริการส่งด่วน ออกไป
                if(!category.matches("สินค้าบริการส่งด่วน")) {
                    // get all page and next page เริ่มตั้งแต่หน้าที่ 1
                    for(int j = 1; j < lastPage; j++) {
                    	String bigCValue = this.bigCApi(cateId, Integer.toString(j));
                    	
                    	// ดึงข้อมูล
            			JSONObject json = new JSONObject(bigCValue);
            			JSONObject result = json.getJSONObject("result");
            			JSONArray arrItems = result.getJSONArray("items");
            			for (int k = 0; k < arrItems.length(); k++) {
            				JSONObject objItems = arrItems.getJSONObject(k);
            				double price = objItems.getDouble("final_price_incl_tax");
            				double originalPrice = objItems.getDouble("price_incl_tax");

            				// เก็บเฉพาะที่มีส่วนลด
            				if(price != originalPrice) {
                				String image = objItems.getString("image");
                				String name = objItems.getString("name");
                				String productUrl = baseUrl + objItems.getString("url_key");
                				//int discount = objItems.getInt("discount");
                				double discount = (((originalPrice - price) / originalPrice) * 100);
    				            DecimalFormat df = new DecimalFormat("#"); // #.# แปลงทศนิยม 1 ตำแหน่ง
    				            discount = Double.parseDouble(df.format(discount));
                				
    				            System.out.println(image);//
    				            System.out.println(name);//
    				            System.out.println(category);//
    				            System.out.println(productUrl);//
    				            //System.out.println(this.icon);
    				            System.out.println(price);//
    				            System.out.println(originalPrice);
    				            System.out.println(discount);
    				            System.out.println();
            				}
            			}
                    }	
                }
				
			}
    		
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	
    }
    
    public static void main(String[] args) {
    	String url = "https://www.bigc.co.th/";
    	BigC b = new BigC();
    	b.getCategory(url);
    	b.getContent(b.list.toString());
    	
    }
    

}
