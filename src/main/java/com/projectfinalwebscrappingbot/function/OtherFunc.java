package com.projectfinalwebscrappingbot.function;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class OtherFunc {

	// for makro
    public int totalPage(String elasValue) {
    	int total = 0;
    	try {
			JSONObject json = new JSONObject(elasValue);
			total = json.getInt("totalPages"); // แปลงเป็น String ในตัว
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return total;
    }  
}
