package com.attender;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shai Pe'er on 03/05/2015.
 */
public class AttenderDAL
{
    final String API_URL = "http://attender-mobile.appspot.com/api";     //api url


    //=============================================== BUILDER ==============================================================================

    public AttenderDAL()
    {

    }


    //=============================================== GET EVENTS ==============================================================================

    public JSONArray getEvents(String eventType, String eventDate, String eventLocation)
    {
        JSONObject jsonObject = null;
        JSONArray jsonArray   = null;
        String jsonData = "";

       // Query format: ?[category=***]&[time=***]&[city=***]";
        String query = "?";
        if(eventType.compareTo("Type") != 0)             query += "category="     + eventType + "&";
        if(eventDate.compareTo("Date") != 0)             query += "time="     + eventDate + "&";
        if(eventLocation.compareTo("City") != 0)         query += "city="     + eventLocation;
        if (query.endsWith("&") || query.endsWith("?"))  query = query.substring(0, query.length() - 1); //delete the last char if it '&'


        try
        {
            jsonData = "{ Events:\n";
                //URL url = new URL("http://attender-mobile.appspot.com/api?city=Jerusalem");   //for TESTING!!!
                URL url = new URL(API_URL + query);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                jsonData += readJsonStream(con.getInputStream());
            jsonData += "}";

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("Events");

        }
        catch (Exception e)
        {
            return null;
        }

        return jsonArray;
    }


    //=========================================== READ STREAM ==================================================================================

    private String readJsonStream(InputStream in)
    {
        BufferedReader reader = null;
        String jsonStreamString = "";
        try
        {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                jsonStreamString += line;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    return "";
                }
            }
        }
        return jsonStreamString;
    }

    //=============================================================================================================================




}
