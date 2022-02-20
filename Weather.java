package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;


public class Weather {
	
	
	private static final int DEFAULT_RETRY_TIME = 1;
    private int retryTime = DEFAULT_RETRY_TIME;
	static HashMap<String,String> Provinces = null;
	
    // Retry sleepTime
    private int sleepTime = 0;

    public int getSleepTime() {
        return sleepTime;
    }

    // Test
    public Object execute() throws InterruptedException {
        for (int i = 0; i < retryTime; i++) {
            try {
      		  String province = "宁夏";
    		  String cities = "银川";
    		  String country = "永宁";
    		  
    		  return getTemperature(province,cities,country);
            } catch (Exception e) {
            	System.out.println(""); // print log
                Thread.sleep(sleepTime);
            }
        }

        return null;
    }
    
    public Weather setSleepTime(int sleepTime) {
        if(sleepTime < 0) {
            throw new IllegalArgumentException("sleepTime should equal or bigger than 0");
        }

        this.sleepTime = sleepTime;
        return this;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public Weather setRetryTime(int retryTime) {
        if (retryTime <= 0) {
            throw new IllegalArgumentException("retryTime should bigger than 0");
        }

        this.retryTime = retryTime;
        return this;
    }

	private static String getTemperature(String province, String city, String country) throws Exception
	{
		String provinceCode = null;
		//Get all provinces
		if(Provinces == null)
		{
			Provinces = getCodeInfo("china");
			provinceCode = Provinces.get(province);
		}
		if(provinceCode == null || provinceCode.isEmpty())
		{
			return "Wrong input province";
		}
		//Get all Cities By Province Code
		HashMap<String, String> cities = getCodeInfo("provshi/" + provinceCode);
		String cityCode = cities.get(city);
		if(cityCode == null || cityCode.isEmpty())
		{
			return "Wrong input city";
		}
		//Get the Countries by City Code
 		HashMap<String, String> countries = getCodeInfo("station/" + provinceCode +cityCode);
 		String countryCode = countries.get(country);
 		if(countryCode == null || countryCode.isEmpty())
		{
			return "Wrong input country";
		}
 		String temp = getTempByCode(provinceCode +cityCode +countryCode);
 		
 		return temp;
	}
	
	//Return the code based on the input url
	private static HashMap<String, String> getCodeInfo(String subUrl)
	{
		HashMap<String, String> Provinces = new HashMap();
		String allProvinceCodeURL = "http://www.weather.com.cn/data/city3jdata/"+subUrl+".html"; 
		
		String urlResult = getURLContent(allProvinceCodeURL);
		//remove the first'{' and last '}'
		urlResult = urlResult.substring(1,urlResult.length()-1);
		String[] results = urlResult.split(",");
		for(int i =0;i < results.length;i++)
		{
			String[] tempval = results[i].split(":");
			//remove the ""
			Provinces.put(tempval[1].substring(1,tempval[1].length()-1), tempval[0].substring(1,tempval[0].length()-1));
		}
		
		return Provinces;
	}
	
	
	// Get temperature by "provinceCode + cityCode + suburbs"
	private static String getTempByCode(String code)
	{
		String allProvinceCodeURL = "http://www.weather.com.cn/data/sk/"+ code +".html"; 
		String urlResult = getURLContent(allProvinceCodeURL);
		String results[] = urlResult.split(":");
		return results[4].substring(1,5);
		
	}

	
	//get URL Content
	private static String getURLContent(String urlStr) {               
		URL url = null;      
	    BufferedReader in = null;   
	    StringBuffer sb = new StringBuffer(); 
		try{
			url = new URL(urlStr);     
			in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8") ); 
			String str = null;  
			//read line
			while((str = in.readLine()) != null) {
				sb.append( str );     
			}     
		} catch (Exception ex) {   
			            
		} finally{    
			try{
				if(in!=null) {
					in.close();   
			    }     
		    }catch(IOException ex) {      
			        
		    }     
		}     
	   String result =sb.toString();     
	   return result;    
	}
	
//	  public static void main(String[] args) throws Exception {
//		  String province = "宁夏";
//		  String cities = "银川";
//		  String country = "永宁";
//		  
//		 
//		  System.out.print(getTemperature(province,cities,country));
//	  }
	
	
}
