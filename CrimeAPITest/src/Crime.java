public class Crime 
{
	String date;
	String time;
	String location;
	String description;
	String longitude; // x
	String latitude;  // y
	
	public Crime(String date, String time, String location, String description, String longitude, String latitude)
	{
		this.date = date;
		this.time = time;
		this.location = location;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	// accessor methods
    public String getDate()
    {
        return date;
    }
    
    public String getTime()
    {
        return time;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public String getLongitude()
    {
        return longitude;
    }
    
    public String getLatitude()
    {
        return latitude;
    }
    
    public String getDescription()
    {
        return description;
    }
    
 // toString
    public String toString()
    {
        return "Crime Info:\n"
        		+"- Date: " + date + "\n"
        			+"- Time: " + time + "\n"
        				+"- Description: " + description + "\n"
        					+"- Location: " + location + "\n";
    }
    
    public String toCSVString()
    {
    	return date + "," 
    			+ time + "," 
    			+ description + "," 
    			+ location + "," 
    			+ longitude + "," 
                + latitude + "\n";
    } // end toString
}
