package com.projectfinalwebscrappingbot;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BigC {
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
            	String category = ele.text();
            	System.out.println(category);
            }
            //System.out.println(eles);
    	}catch(Exception e) {
    		System.out.println("error => "+e.getMessage());
    	}
    	
    }
    public static void main(String[] args) {
    	String url = "https://www.bigc.co.th/";
    	BigC b = new BigC();
    	b.getCategory(url);
    	
    }
    

}
