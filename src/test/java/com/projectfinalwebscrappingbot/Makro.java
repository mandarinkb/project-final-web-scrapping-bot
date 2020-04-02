package com.projectfinalwebscrappingbot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Makro {
    public static void content(String url) throws IOException, InterruptedException {   	
		Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//
        Elements eles = doc.select(".background-grey");//.container.py-lg-3.py-2
        Elements eles2 = eles.select(".cardBorderShadow.px-2.pb-2");
        for (Element ele : eles2) {
        	String title = ele.select(".pt-2.ProductCard__TitleMaxLines-sc-1lb6pe3-2.kAKAhd").html();
        	System.out.println(title); 
        } 
        Elements eles3 = eles.select(".mt-3");
        for (Element ele : eles3) {
        	String title = ele.select(".pt-2.ProductCard__TitleMaxLines-sc-1lb6pe3-2.kAKAhd").html();
        	System.out.println(title); 
        }

    }	
	
	
    public static void main(String[] args) throws IOException, InterruptedException{
        String url = "https://www.makroclick.com/th";
        Makro w = new Makro();
        w.content(url);
    }
}
