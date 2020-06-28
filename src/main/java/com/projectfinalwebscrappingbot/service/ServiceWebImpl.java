package com.projectfinalwebscrappingbot.service;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.projectfinalwebscrappingbot.dao.Redis;
import com.projectfinalwebscrappingbot.function.DateTimes;
import com.projectfinalwebscrappingbot.function.Elasticsearch;
import com.projectfinalwebscrappingbot.function.OtherFunc;

import redis.clients.jedis.Jedis;


@Service
public class ServiceWebImpl implements ServiceWeb {
    @Value("${db_1}")
    private String db_1;
    
    @Value("${db_2}")
    private String db_2;
	
    @Autowired
    private Redis rd;  
    
    @Autowired
    private DateTimes dateTimes;

    @Autowired
    private Elasticsearch els;    
    
    @Autowired
    private OtherFunc otherFunc;

	@Override
	public void tescolotus(String obj) {
		Jedis redis = rd.connect();
		JSONObject json = new JSONObject(obj);
		JSONObject jsonEls = new JSONObject();
		String url = json.getString("url");
    	try {
        	Document doc = Jsoup.connect(url)
		                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) snap Chromium/83.0.4103.61 Chrome/83.0.4103.61 Safari/537.36")
		                        .timeout(600000)
		                        .maxBodySize(0)
		                        .get();//
        	Elements elesUrlDetail = doc.select(".product-image__container");
        	//find image
            for (Element ele : elesUrlDetail) {
                Element eleUrl = ele.select("img").first();
                String image = eleUrl.attr("src");      
                jsonEls.put("image",image);    
        	}
            //find name
            String name = doc.select(".product-details-tile__title").html();
            jsonEls.put("name",name);
            
            //find price
            String priceAll = doc.select(".offer-text").first().html();
            String[] parts = priceAll.split("บาท");
            String priceStr = parts[0].replace("ราคาพิเศษ ", "");
            String originalPriceStr = parts[1].replace(" จากราคาปกติ ", "");
            //String save = parts[2].replace(" ประหยัด ", "");
            double price = Double.parseDouble(priceStr);
            double originalPrice = Double.parseDouble(originalPriceStr);
            double discountFull = (((originalPrice - price) / originalPrice) * 100);  // หา % ของส่วนลด
            DecimalFormat df = new DecimalFormat("#"); // #.# แปลงทศนิยม 1 ตำแหน่ง
            double discount = Double.parseDouble(df.format(discountFull));
            
            jsonEls.put("category",json.getString("category"));
            jsonEls.put("productUrl",url);
            jsonEls.put("icon",json.getString("icon_url"));
            jsonEls.put("price",price);
            jsonEls.put("originalPrice",originalPrice);
            jsonEls.put("discountFull",discountFull);
            jsonEls.put("discount",discount);
            jsonEls.put("webName",json.getString("web_name"));
            jsonEls.put("review","-");
            jsonEls.put("ratingScore","-");
            
            String db = json.getString("database");
            // ตรวจสอบ db แล้วทำการลง db นั้นๆ เช่น database1 database2
            if(db.matches(db_1)) {
            	els.inputElasticsearch(jsonEls.toString(), json.getString("database"));
            }else if(db.matches(db_2)){
            	els.inputElasticsearch(jsonEls.toString(), json.getString("database"));
            }
            
            System.out.println(dateTimes.thaiDateTime() +" web scrapping ==> "+url); 
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    		//redis.rpush("detailUrl", obj); //กรณี error ให้ยัดลง redis ที่รับมาอีกรอบ
    	}	
	}

	@Override
	public void lazada(String objStr) {
		Jedis redis = rd.connect();
		JSONObject json = new JSONObject(objStr);
		JSONObject jsonEls = new JSONObject();
		String url = json.getString("url");
        try {
        	// old
    		Document doc = Jsoup.connect(url)
    				            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) snap Chromium/83.0.4103.61 Chrome/83.0.4103.61 Safari/537.36")
    				            .maxBodySize(0)
    				            .timeout(600 * 1000)
    				            .get();
            Elements eles = doc.select("head");
            String detail = eles.select("script").get(3).html();
            detail = detail.replace("window.pageData=", "");
                   	
        	// test new
        	/*String detail = null;
        	detail = els.lazadaApi(url);
        	*/

            JSONObject obj = new JSONObject(detail);
            JSONObject objMods = obj.getJSONObject("mods");
            JSONArray arrListItems = objMods.getJSONArray("listItems");
            for (int i = 0; i < arrListItems.length(); i++) {
            	JSONObject objItems = arrListItems.getJSONObject(i);
            	
            	String image = objItems.getString("image");
            	
            	//เช็คว่ามี key หรือไม่
            	String originalPrice = null;
            	if (objItems.has("originalPrice")) { 
            		originalPrice = objItems.getString("originalPrice");
            	}
            	String price = objItems.getString("price");
            	String name = objItems.getString("name");
            	
            	String discount = null;  
            	if (objItems.has("discount")) { 
            		discount = objItems.getString("discount");
            		discount = discount.replace("-", "");
            		discount = discount.replace("%", "");
            	}

            	String productUrl = objItems.getString("productUrl");
            	productUrl = "https:" + productUrl;
        
            	// เก็บเฉพาะที่มีส่วนลด
            	if(discount != null) {
            		jsonEls.put("image",image);  
            		jsonEls.put("name",name);  
                    jsonEls.put("category",json.getString("category"));  
                    jsonEls.put("productUrl",productUrl);  
                    jsonEls.put("icon",json.getString("icon_url"));
                    jsonEls.put("price",Double.parseDouble(price)); 
                    jsonEls.put("originalPrice",Double.parseDouble(originalPrice));  
                    jsonEls.put("discount",Double.parseDouble(discount));  
                    jsonEls.put("webName",json.getString("web_name")); 
                    jsonEls.put("review",objItems.getString("review"));
                    jsonEls.put("ratingScore",objItems.getString("ratingScore"));
                    
                    String db = json.getString("database");
                    // ตรวจสอบ db แล้วทำการลง db นั้นๆ เช่น database1 database2
                    if(db.matches(db_1)) {
                    	els.inputElasticsearch(jsonEls.toString(), json.getString("database"));
                    }else if(db.matches(db_2)){
                    	els.inputElasticsearch(jsonEls.toString(), json.getString("database"));
                    }
                    System.out.println(dateTimes.thaiDateTime() +" web scrapping ==> "+productUrl); 
            	}
            }   
        }catch(Exception e) {
        	System.out.println("error => " + e.getMessage());
        	//redis.rpush("detailUrl", objStr); //กรณี error ให้ยัดลง redis ที่รับมาอีกรอบ
        }	
	}

	@Override
	public void makroclick(String objStr) {
	    String urlDetail = "https://www.makroclick.com/th/products/";
		try {
			JSONObject jsonEls = new JSONObject();
			JSONObject obj = new JSONObject(objStr);
			String category = obj.getString("category");
			String menuId = obj.getString("menuId");
			String elas = els.makroApi(menuId, "1");
			int total = otherFunc.totalPage(elas); // หา page ทั้งหมดก่อน
			
			// วนหา pagination ของ page นั้นๆ
			for(int j = 1; j < total; j++) {
				String elasValue = els.makroApi(menuId, Integer.toString(j));
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
						String productUrl = urlDetail + objItems.getString("productCode");
						
			            double discount = (((originalPrice - price) / originalPrice) * 100);  // หา % ของส่วนลด
			            DecimalFormat df = new DecimalFormat("#"); // #.# แปลงทศนิยม 1 ตำแหน่ง
			            discount = Double.parseDouble(df.format(discount));
			            
	            		jsonEls.put("image",image);  
	            		jsonEls.put("name",name);  
	                    jsonEls.put("category",category);  
	                    jsonEls.put("productUrl",productUrl);  
	                    jsonEls.put("icon",obj.getString("icon_url"));
	                    jsonEls.put("price",price); 
	                    jsonEls.put("originalPrice",originalPrice);  
	                    jsonEls.put("discount",discount);  
	                    jsonEls.put("webName",obj.getString("web_name"));  
	                    jsonEls.put("review","-");
	                    jsonEls.put("ratingScore","-");
						
			            String db = obj.getString("database");
			            // ตรวจสอบ db แล้วทำการลง db นั้นๆ เช่น database1 database2
			            if(db.matches(db_1)) {
			            	els.inputElasticsearch(jsonEls.toString(), obj.getString("database"));
			            }else if(db.matches(db_2)){
			            	els.inputElasticsearch(jsonEls.toString(), obj.getString("database"));
			            }
			            System.out.println(dateTimes.thaiDateTime() +" web scrapping ==> "+productUrl); 
					}
				}
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void bigc(String objStr) {
		String baseUrl = "https://www.bigc.co.th/";
		try {
			JSONObject jsonEls = new JSONObject();
			JSONObject obj = new JSONObject(objStr);
			String category = obj.getString("category");
			String cateId = obj.getString("cateId");
        	//call bigCapi
        	String elasValue = els.bigCApi(cateId, "1");
        	//get last page
            int lastPage = otherFunc.lastPage(elasValue);
            for(int j = 1; j < lastPage; j++) {
            	String bigCValue = els.bigCApi(cateId, Integer.toString(j));
            	
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

        				double discount = (((originalPrice - price) / originalPrice) * 100);
			            DecimalFormat df = new DecimalFormat("#"); // #.# แปลงทศนิยม 1 ตำแหน่ง
			            discount = Double.parseDouble(df.format(discount));
        			
	            		jsonEls.put("image",image);  
	            		jsonEls.put("name",name);  
	                    jsonEls.put("category",category);  
	                    jsonEls.put("productUrl",productUrl);  
	                    jsonEls.put("icon",obj.getString("icon_url"));
	                    jsonEls.put("price",price); 
	                    jsonEls.put("originalPrice",originalPrice);  
	                    jsonEls.put("discount",discount);  
	                    jsonEls.put("webName",obj.getString("web_name"));  
	                    jsonEls.put("review","-");
	                    jsonEls.put("ratingScore","-");
						
			            String db = obj.getString("database");
			            // ตรวจสอบ db แล้วทำการลง db นั้นๆ เช่น database1 database2
			            if(db.matches(db_1)) {
			            	els.inputElasticsearch(jsonEls.toString(), obj.getString("database"));
			            }else if(db.matches(db_2)){
			            	els.inputElasticsearch(jsonEls.toString(), obj.getString("database"));
			            }
			            System.out.println(dateTimes.thaiDateTime() +" web scrapping ==> "+productUrl); 
    				}
    			}
            }
	
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
