package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScrappingDemo {
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
	
    public static void main(String[] args) throws IOException, InterruptedException{
    	//https://www.lazada.co.th/shop-mobiles/?page=2
        String url = "https://www.lazada.co.th/shop-motorcycle-riding-gear-balaclavas/?page=2";  //https://www.lazada.co.th/#
        WebScrappingDemo w = new WebScrappingDemo();
        w.content(url);
    }	
	
}
