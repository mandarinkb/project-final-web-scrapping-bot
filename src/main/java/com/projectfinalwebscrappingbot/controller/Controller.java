package com.projectfinalwebscrappingbot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projectfinalwebscrappingbot.dao.Redis;
import com.projectfinalwebscrappingbot.function.DateTimes;
import com.projectfinalwebscrappingbot.service.ServiceWeb;

import redis.clients.jedis.Jedis;

@Component
public class Controller {

    @Autowired
    private DateTimes dateTimes;

    @Autowired
    private Redis rd;

    @Autowired
    private ServiceWeb serviceWeb;

    @Scheduled(cron = "0 0/1 * 1/1 * ?") // เรียกใช้งานทุกๆ 1 นาที
    public void runTask() {
        System.out.println(dateTimes.interDateTime() + " : web scrapping input database runTask start");
        Jedis redis = rd.connect();
        boolean checkDetailUrl = true;
        while (checkDetailUrl) {
        	String obj = redis.rpop("detailUrl");
        	if (obj != null) {
        		serviceWeb.tescolotus(obj);;
            } else {
            	checkDetailUrl = false;
            }
        }
        System.out.println(dateTimes.interDateTime() + " : web scrapping input database runTask stop");
    }
}
