package com.projectfinalwebscrappingbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Tescolotus {
	public static List<String>list = new ArrayList<>();
	public static String baseUrl = "https://shoponline.tescolotus.com";
    public static void page(String url) throws IOException, InterruptedException {   	
		Document doc = Jsoup.connect(url).timeout(60 * 1000).get();//.promotions-by-department--facets.promotions--department.by-department
        Elements eles = doc.select(".list-item.list-item-large");//.container.py-lg-3.py-2
        for (Element ele : eles) {
        	String title = ele.select(".name").html();
        	System.out.println(title); 
        	
            Element eleTitle = ele.select("a").first();
            String strUrl = eleTitle.attr("href");
            String page = baseUrl+strUrl;
            list.add(page);
            System.out.println(page);
        } 
        //product-list grid
        //product-list--list-item
        

    }	
    public static void content(String url) throws IOException, InterruptedException {  
    	System.out.println(url);
    	boolean checkNextPage = true;
    	
    	while(checkNextPage) {
    	Document doc = Jsoup.connect(url).timeout(60 * 1000).get();
    	Elements eles = doc.select(".product-details--wrapper");
    	for (Element ele : eles) {//product-details--wrapper
    		String detail = ele.select("a").first().html();
    		System.out.println(detail);
    	}
    	//nextpage
    	Elements elesNextPage = doc.select(".pagination--page-selector-wrapper");
		Element eleNextPage = elesNextPage.select(".pagination-btn-holder").last();
        Element eleA = eleNextPage.select("a").first();
        String urlNextPage = eleA.attr("href");
            
        if(urlNextPage == "") {
        	checkNextPage = false;
        }
        
        url = baseUrl+urlNextPage;
        System.out.println(url);    
		System.out.println();
    	}
    	
        
    }
	
	
    public static void main(String[] args) throws IOException, InterruptedException{
        
    	//https://shoponline.tescolotus.com/groceries/th-TH/promotions/all?superdepartment=12617&page=42
        //String url = "https://shoponline.tescolotus.com/groceries/th-TH/promotions/all?superdepartment=12617";
        Tescolotus t = new Tescolotus();
        
        String url = "https://shoponline.tescolotus.com/groceries/th-TH/promotions/";
        t.page(url);
        
        System.out.println(list);
        
        for(String listUrl : list) {
        	t.content(listUrl);
        }
        //String url = "https://shoponline.tescolotus.com/groceries/th-TH/promotions/all?superdepartment=12617";
        //t.content(url);
    }
}
