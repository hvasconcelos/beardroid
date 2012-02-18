package com.bearstouch.android.core;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.content.Context;

@Singleton
public class CriptoUtil
{
	
	Context mContext;

	 
	@Inject
	public CriptoUtil(Context context){		
		mContext=context;
	}
	
	
	public String md5Digest(String val) {
	    String result = null;

	    if ((val != null) && (val.length() > 0)) {
	      try {
	        MessageDigest md5 = MessageDigest.getInstance("MD5");	       
	        md5.update(val.getBytes());
	        byte[] md5byte=md5.digest();
	        BigInteger bigInt = new BigInteger(1,md5byte);
	        String hashtext = bigInt.toString(16);	        
	        // Now we need to zero pad it if you actually want the full 32 chars.
	        while(hashtext.length() < 32 ){
	          hashtext = "0"+hashtext;
	        }
	        result=hashtext;
	      } catch (NoSuchAlgorithmException nsae) {
	        result = val.substring(0, 32);
	      }
	    }
	    return result;
	  }
	
	public String sha1Digest(String val) {
	    String result = null;

	    if ((val != null) && (val.length() > 0)) {
	      try {	      	        
	        MessageDigest sha1digest = MessageDigest.getInstance("SHA-1");	       
	        sha1digest.update(val.getBytes(Charset.forName("UTF8")));
	        byte[] sha1byte=sha1digest.digest();
	        BigInteger bigInt = new BigInteger(1,sha1byte);
	        String hashtext = bigInt.toString(16);
	        // Now we need to zero pad it if you actually want the full 32 chars.
	        while(hashtext.length() < 32 ){
	          hashtext = "0"+hashtext;
	        }
	      } catch (NoSuchAlgorithmException nsae) {
	        result = val.substring(0, 32);
	      }
	    }
	    return result;
	  }
}
