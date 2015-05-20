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
public class AttenderDAL {
    final String API_URL = "http://attender-mobile.appspot.com/";     //api url


    //=============================================== BUILDER ==============================================================================

    public AttenderDAL() {

    }


    //=============================================== GET EVENTS ==============================================================================

    public JSONArray getEvents(String eventType, String eventDate, String eventLocation) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String jsonData = "";

        // Query format: ?[category=***]&[time=***]&[city=***]";
        String query = "api?";
        if (!eventType.contains("Type")) query += "category=" + eventType + "&";
        if (!eventDate.contains("Date")) query += "time=" + eventDate + "&";
        if (!eventLocation.contains("City")) query += "city=" + eventLocation;
        if (query.endsWith("&") || query.endsWith("?"))
            query = query.substring(0, query.length() - 1); //delete the last char if it '&'


        try {
            jsonData = "{ Events:\n";
            jsonData+=serverConnection(query);
            jsonData += "}";

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("Events");

        } catch (Exception e) {
            return null;
        }

        return jsonArray;
    }


    //=========================================== READ STREAM ==================================================================================

    private String readJsonStream(InputStream in) {
        BufferedReader reader = null;
        String jsonStreamString = "";
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                jsonStreamString += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return "";
                }
            }
        }
        return jsonStreamString;
    }

    //===================================Server Connection==============================================================

    public String serverConnection(String query)
    {
        String jsonData = "";
        JSONObject jsonObject = null;

        try {
            URL url = new URL(API_URL + query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            jsonData += readJsonStream(con.getInputStream());


        } catch (Exception e) {
            return null;
        }
        return jsonData;
    }
//=================================================login======================================================================
    public String loginToServer(String id, String token)
    {
        String query="login?";
        query+="id="+id+"&token="+token;
        String jsonData="";
        jsonData=serverConnection(query);
        return jsonData;
    }
    public String Attend(String token,String eventId)
    {
        String query="attend?";
        query+="token="+token+"&eventid="+eventId;
        String jsonData="";
        jsonData=serverConnection(query);
        return jsonData;
    }
    public JSONArray getAttendees(String eventId, String token)
    {
        JSONObject jsonObject;
        JSONArray jsonArray;
        String query="attendees?";
        query+="eventid="+eventId+"&token="+token;
        String jsonData="";
        jsonData=serverConnection(query);
        try{
        jsonObject = new JSONObject(jsonData);
        jsonArray = jsonObject.getJSONArray("Events");

    } catch (Exception e) {
        return null;
    }

    return jsonArray;
    }

}
