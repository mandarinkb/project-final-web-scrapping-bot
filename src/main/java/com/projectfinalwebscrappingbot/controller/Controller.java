package com.projectfinalwebscrappingbot.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projectfinalwebscrappingbot.dao.Redis;
import com.projectfinalwebscrappingbot.function.DateTimes;
import com.projectfinalwebscrappingbot.function.Elasticsearch;
import com.projectfinalwebscrappingbot.function.Log;
import com.projectfinalwebscrappingbot.function.OtherFunc;
import com.projectfinalwebscrappingbot.service.ServiceWeb;

import redis.clients.jedis.Jedis;

@Component
public class Controller {
	@Autowired
	private Elasticsearch elas;
	
	@Autowired
	private Log log;

    @Autowired
    private DateTimes dateTimes;

    @Autowired
    private Redis rd;

    @Autowired
    private ServiceWeb serviceWeb;
    
    @Autowired
    private OtherFunc otherFunc;

    @Scheduled(cron = "0 0/1 * 1/1 * ?") // เรียกใช้งานทุกๆ 1 นาที
    public void runTask() {
    	try {
            System.out.println(dateTimes.interDateTime() + " : web scrapping input database start");
            Jedis redis = rd.connect();
            boolean checkDetailUrl = true;
            boolean isFinish = false;
            String index = "";
            while (checkDetailUrl) {
            	String obj = redis.rpop("detailUrl");
            	if (obj != null) {
            		isFinish = true;
                	JSONObject json = new JSONObject(obj);
                	String webName = json.getString("web_name");
                	index = json.getString("database");
                    switch(webName) 
                    { 
                        case "tescolotus": 
                        	serviceWeb.tescolotus(json.toString());
                            break; 
                        case "lazada": 
                        	serviceWeb.lazada(json.toString());
                            break; 
                        case "makroclick": 
                        	serviceWeb.makroclick(json.toString());
                            break;  
                        case "bigc": 
                        	serviceWeb.bigc(json.toString());;
                            break; 
                        default: 
                            System.out.println("no match"); 
                    } 
                } else {
                	checkDetailUrl = false;
                } 
            }
            if(isFinish) {
                String value = elas.findAll(index);
                log.createLog(dateTimes.datetime(),dateTimes.timestamp(), otherFunc.randomStr() + "_bot", "match all", index+" total "+value);
                System.out.println(dateTimes.interDateTime() + " "+index+" total "+value);
            	isFinish = false;
            }
            System.out.println(dateTimes.interDateTime() + " : web scrapping input database stop");
            //log.createLog(dateTimes.datetime(), dateTimes.timestamp(), "system", "stop web scrapping", "stop web scrapping input database");
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
}
