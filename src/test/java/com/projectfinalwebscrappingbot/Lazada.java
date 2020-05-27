package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Lazada {
    public static void content(String url) throws IOException, InterruptedException {
    	
		Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//
        Elements eles = doc.select("head");//lzd-site-nav-menu-dropdown
        String eles2 = eles.select("script").get(3).html();
        System.out.println(eles2);       
    	
    	
    	/*int i = 0;
        int j = 0;
    	while(true) {
    		j = ++i;
    		System.out.println(j);
    		System.out.println(url+Integer.toString(j));
    		Document doc = Jsoup.connect(url+Integer.toString(j)).timeout(60 * 1000).get();//
            Elements eles = doc.select("head");//lzd-site-nav-menu-dropdown
            String eles2 = eles.select("script").get(3).html();
            System.out.println(eles2);
            System.out.println();
            
    		TimeUnit.MINUTES.sleep(1);
    		eles2 = "";
    	}
    	
*/    	
    	
    	
/*        Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//
        Elements eles = doc.select("head");//lzd-site-nav-menu-dropdown
        String eles2 = eles.select("script").get(3).html();
        System.out.print(eles2);
*/        
        
/*        Elements eles = doc.select(".lzd-site-menu-root");
        Elements elesLi = eles.select(".lzd-site-menu-sub-item");
        for (Element ele : elesLi) {
            Element title = ele.select("a").first();
            String strUrl = title.attr("href");
            System.out.println("https:"+strUrl);
            Document newDoc = Jsoup.connect("https:"+strUrl).timeout(60 * 1000).get();//
            Elements elesHead = newDoc.select("head");
            String strScript = elesHead.select("script").get(3).html();
            String strScriptNew = strScript.replace("window.pageData=", "");
            System.out.println(strScriptNew);
            System.out.println();
            //System.out.println();
        }
*/      
        //System.out.print(eles);
    }
	
    public static void category(String url) {
    	try {
    		Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//
            Elements eles = doc.select(".lzd-site-menu-root");
            
            JSONObject objCategory = new JSONObject();
            List<JSONObject> list = new ArrayList<>(); 
            for(Element ele : eles.select(".lzd-site-menu-root-item")) {
            	objCategory = new JSONObject();
            	String id = ele.attr("id");
            	String name = ele.select("span").html();
            	objCategory.put("class","." + id);
            	objCategory.put("category", name);
            	list.add(objCategory);	// เก้บ class และ category ลง list
            } 
            //นำ list ที่เก็บไว้มาแสดง
            JSONArray arr = new JSONArray(list.toString());
            for (int i = 0; i < arr.length(); i++) {
            	JSONObject obj = arr.getJSONObject(i);
            	String cl = obj.getString("class");
            	String ca = obj.getString("category");

            	// กรณี บ้านและไลฟ์สไตล์ ให้จัดเก็บข้อมูลบางส่วน(sub)
            	if(ca.equals("บ้านและไลฟ์สไตล์")) {
            		// เก็บ Sub ลง list ก่อน
            		System.out.println(ca);
            		Elements elesSubCategory = eles.select(cl);
            		for(Element ele : elesSubCategory.select(".lzd-site-menu-sub-item")) {
                        String name = ele.select("span").first().html(); 
                        // เก็บเฉพาะ อุปกรณ์ทำความสะอาดและซักรีด(detail)
                        if(name.equals("อุปกรณ์ทำความสะอาดและซักรีด")) {
                    		for(Element e : ele.select(".lzd-site-menu-grand-item")) {
                                Element eleDetail = e.select("a").first();
                                String urlSubDetail = eleDetail.absUrl("href");
                                String nameSub = e.select("span").html();
                                
                                System.out.println(nameSub);     
                                System.out.println(urlSubDetail);  
                    		}
                    		System.out.println("=============================================================");
                        }
            		}
            	}else {
            		// กรณีอื่นๆ เก็บ detail ได้เลย
            		System.out.println(ca);
            		Elements elesSubCategory = eles.select(cl);
            		for(Element ele : elesSubCategory.select(".lzd-site-menu-grand-item")) {
                        Element eleUrl = ele.select("a").first();
                        String name = ele.select("span").html();
                        String urlDetail = eleUrl.absUrl("href");
                        
                        //System.out.println(name);
                        //System.out.println(urlDetail);
            		}
            		System.out.println("=============================================================");
            	}
            }      
            
    	} catch(Exception e) {
    		System.out.println("error => " + e.getMessage());
    	}	
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
    	//https://www.lazada.co.th/shop-mobiles/?page=2
        /*String url = "https://www.lazada.co.th/shop-motorcycle-riding-gear-balaclavas/?page=2";  //https://www.lazada.co.th/#
        Lazada w = new Lazada();
        w.content(url);
        */
    	

    	String url = "https://www.lazada.co.th/#";
    	Lazada l = new Lazada();
    	l.category(url);
    	
    	
    }	
	
}
