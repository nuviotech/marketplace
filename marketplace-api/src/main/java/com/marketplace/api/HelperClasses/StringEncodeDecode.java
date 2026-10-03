package com.marketplace.api.HelperClasses;

import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StringEncodeDecode {
	//this method encode the String
	
    public String getEncodedString(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
    //this method decode the password
	
    public String getDecodeString(String encryptedStr){
        return new String(Base64.getMimeDecoder().decode(encryptedStr));
    }
}
