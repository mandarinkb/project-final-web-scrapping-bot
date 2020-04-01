package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScrappingDemo {
    public static void content(String url) throws IOException, InterruptedException {
    	List<String> list= new ArrayList<>();
        Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//
        //Elements eles = doc.select("head");//lzd-site-nav-menu-dropdown
        //String eles2 = eles.select("script").get(3).html();
        //System.out.print(eles2);
        Elements eles = doc.select(".lzd-site-menu-root");
        Elements elesLi = eles.select(".lzd-site-menu-sub-item");
        for (Element ele : elesLi) {
            Element title = ele.select("a").first();
            String strUrl = title.attr("href");
            System.out.println("https:"+strUrl);
            /*String scriptStr = ele.select("script").html();
            System.out.println(scriptStr);
            System.out.println();
            */
            Document newDoc = Jsoup.connect("https:"+strUrl).timeout(60 * 1000).get();//
            Elements elesHead = newDoc.select("head");
            String strScript = elesHead.select("script").get(3).html();
            String strScriptNew = strScript.replace("window.pageData=", "");
            System.out.println(strScriptNew);
            System.out.println();
            //System.out.println();
        }
      
        //System.out.print(eles);
    }
	
    public static void main(String[] args) throws IOException, InterruptedException{
    	//https://www.lazada.co.th/shop-mobiles/
        String url = "https://www.lazada.co.th/#";
        WebScrappingDemo w = new WebScrappingDemo();
        w.content(url);
    }	
	
}
