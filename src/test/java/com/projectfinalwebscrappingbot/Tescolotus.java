package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Tescolotus {
	public static final String HTML = "input.html"; 
	public static List<String>list = new ArrayList<>();
	public static List<String>listUrlDetail = new ArrayList<>();
	//public static String baseUrl = "https://shoponline.tescolotus.com";
    public  void page(String url){ 
    	try {
    		Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) snap Chromium/83.0.4103.61 Chrome/83.0.4103.61 Safari/537.36")
                    .timeout(600000)
                    .maxBodySize(0)
                    .get();//
            Elements eles = doc.select(".list-item.list-item-large");
            for (Element ele : eles) {
            	String category = ele.select(".name").html();
            	// ตัดหมวดหมู่ดังกล่าวออก
            	if(!category.matches("ดูทั้งหมด") && !category.matches("แผนกเสื้อผ้า") && !category.matches("สินค้าอื่นๆ")) {
                    Element eleTitle = ele.select("a").first();
                    //String strUrl = eleTitle.attr("href");                    
                    String strUrl = eleTitle.absUrl("href");
                    //String categoryUrl = strUrl;
                    
                    category = category.replace(",", "");
                    category = category.replace("&amp; ", "");
                    
                    //String newCategory = els.getCategory(category); // แปลง category ใหม่
                    System.out.println(category); 

            }
            }
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }	
    public void content(String url){  
    	try {
        	System.out.println(url);       	
        	boolean checkNextPage = true;       	
        	while(checkNextPage) {       		
        	Document doc = Jsoup.connect(url).timeout(60 * 1000).get();
        	//urlDetail
        	Elements elesUrlDetail = doc.select(".tile-content");
            for (Element ele : elesUrlDetail) {
                Element eleUrl = ele.select("a").first();
                String urlDetail = eleUrl.absUrl("href");
                //System.out.println(urlDetail); 
                listUrlDetail.add(urlDetail);
        	}  		
        	//nextpage
        	Elements elesNextPage = doc.select(".pagination--page-selector-wrapper");
    		Element eleNextPage = elesNextPage.select(".pagination-btn-holder").last();
            Element eleA = eleNextPage.select("a").first();
            //String urlNextPage = eleA.attr("href");
            String urlNextPage = eleA.absUrl("href");    
            if(urlNextPage == "") {
            	checkNextPage = false;
            }
            url = urlNextPage;
            System.out.println(url);    
    		System.out.println();
        	}
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    public void contentDetail(String url){
    	try {
        	Document doc = Jsoup.connect(url).timeout(60 * 1000).get();
        	Elements elesUrlDetail = doc.select(".product-image__container");
            for (Element ele : elesUrlDetail) {
                Element eleUrl = ele.select("img").first();
                String image = eleUrl.attr("src");
                System.out.println(image);       
        	}
            String name = doc.select(".product-details-tile__title").html();
            System.out.println(name); 
            
            String priceAll = doc.select(".offer-text").first().html();
            System.out.println(priceAll); 
            String[] parts = priceAll.split("บาท");
            String part1 = parts[0].replace("ราคาพิเศษ ", "");
            String part2 = parts[1].replace(" จากราคาปกติ ", "");
            String part3 = parts[2].replace(" ประหยัด ", "");
            
            System.out.println(part1); 
            System.out.println(part2); 
            System.out.println(part3); 
            
            
            String icon = "https://www.tescolotus.com/assets/theme2018/tl-theme/img/logo.png";
            System.out.println(icon); 
            
            System.out.println(url); 
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}

    }
	
    public static void main(String[] args){
        
        Tescolotus t = new Tescolotus();
        
        String url = "https://shoponline.tescolotus.com/groceries/th-TH/promotions/";
        t.page(url);
        
/*        list.remove( list.size() - 1 );  // del last element
        System.out.println(list);
      
        for(String listUrl : list) {
        	t.content(listUrl);
        }
*/        
/*        int i = 0;
        for(String urlDetail : listUrlDetail) {
        	t.contentDetail(urlDetail);
        	System.out.println(++i);        	
        }
*/       
 
    }
}
