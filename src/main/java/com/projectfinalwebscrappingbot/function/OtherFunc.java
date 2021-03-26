package com.projectfinalwebscrappingbot.function;

import java.util.Random;

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
    
    // for bigC
    public int lastPage(String elasValue) {
    	int last = 0;
    	try {
			JSONObject json = new JSONObject(elasValue);
			JSONObject result = json.getJSONObject("result");
			last = result.getInt("lastPage");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

    	return last;
    }
    
    public String randomStr() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
    
        return generatedString;
    }
}
