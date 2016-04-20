import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.*;


public class CrimeAPITest 
{
	public static void main(String args[]) throws Exception
	{
		String crimesURL;		// hold URL for API call
		//String[] JsonResponse = new String[7];    // hold Json responses from API calls 
		ArrayList<Crime> crimesInPhilly = new ArrayList<Crime>();
		
		int UCR = 100;
		//int i = 0;
		for(UCR = 100; UCR < 800; UCR = UCR + 100)
		{
			crimesURL = "http://gis.phila.gov/ArcGIS/rest/services/PhilaGov/Police_Incidents_Last30/MapServer/0/query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&where=SECTOR%3D%273%27+AND+UCR_GENERAL%3D%27" + UCR + "%27&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry=true&maxAllowableOffset=&outSR=&outFields=*&f=pjson";
			//JsonResponse[i] = getJson(crimesURL);
			parseCrimes(getJson(crimesURL), crimesInPhilly);
			//i++;
		}
		
		writeCSV(crimesInPhilly);
		System.out.println(crimesInPhilly.size());
	}
	
	public static String getJson(String url) 
	{
		String jsonLine = "";
		String l;
		
		URL wuurl = null;
		try 
		{
			wuurl = new URL(url);
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		URLConnection connection = null;
		try 
		{
			connection = wuurl.openConnection();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		InputStream response = null;
		try 
		{
			response = connection.getInputStream();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// read the response into a String 
		InputStreamReader isr = new InputStreamReader(response);
		BufferedReader br = new BufferedReader(isr);
		
		try 
		{
			while((l = br.readLine()) != null)
			{
				jsonLine += l;
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return jsonLine;
	}
	
	public static void parseCrimes(String jsonLine, ArrayList<Crime> crimes) 
	{
		String date, time, location, description;
		String longitude, latitude;
		
		// initialize JSON parser to parse the response String
		JsonParser jp = new JsonParser();
	    JsonElement root = jp.parse(jsonLine);
	    
	    JsonArray crimesList = root.getAsJsonObject().get("features").getAsJsonArray();

		for(int i = 0; i < crimesList.size(); i++)
		{
			JsonObject current = crimesList.get(i).getAsJsonObject().get("attributes").getAsJsonObject();
			
			date = current.get("DISPATCH_DATE").getAsString();
			time = current.get("DISPATCH_TIME").getAsString();
			location = formatLocation(current.get("LOCATION_BLOCK").getAsString());
			description = current.get("TEXT_GENERAL_CODE").getAsString();
			longitude = current.get("POINT_X").getAsString();
			latitude = current.get("POINT_Y").getAsString();
			
			crimes.add(new Crime(date, time, location, description, longitude, latitude));
		}
	}
	
	public static void writeCSV(ArrayList<Crime> crimes) throws Exception 
    {
        File ObjectTxt = new File("crimes.csv"); 
        // create a PrintWriter text output stream and link it to the File objectTxt 
        PrintWriter outfile = new PrintWriter(ObjectTxt);

        for(int i = 0; i < crimes.size(); i++)
        {
            outfile.print(crimes.get(i).toCSVString());         
        } // end for
        
        outfile.close();
    } // end writeTxt
	
	public static String formatLocation(String location) 
    {
		String formattedLocation = "";
		
		if(location.contains("BLOCK"))
		{
			String[] tokens = location.split(" BLOCK");
			for(int i = 0; i < tokens.length; i++)
				formattedLocation += tokens[i];
		}
		else
			formattedLocation = location.trim();
		
		System.out.println(formattedLocation);
		
		return formattedLocation;
    }
}
