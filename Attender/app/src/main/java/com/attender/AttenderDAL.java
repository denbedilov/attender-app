package com.attender;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;

        import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Shai Pe'er on 03/05/2015.
 */
public class AttenderDAL {
    final String API_URL = "http://attender-mobile.appspot.com/";     //api url
    private int responseCode;

    /* =============================================== BUILDER ============================================================================== */

    public AttenderDAL() {

    }


    /* =============================================== GET EVENTS ============================================================================== */

    public JSONArray getEvents(String eventType, String eventDate, String eventLocation) {
        JSONObject jsonObject;
        JSONArray jsonArray;
        String jsonData;

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

    //===================================Server Connection==============================================================================

    public String serverConnection(String query)
    {
        String jsonData = "";
        JSONObject jsonObject = null;

        try {
            URL url = new URL(API_URL + query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            responseCode = con.getResponseCode();
            jsonData += readJsonStream(con.getInputStream());

        } catch (Exception e) {
            return null;
        }
        return jsonData;
    }
//=================================================Facebook login======================================================================
    public String loginToServer(String id, String token)
    {
        String query="login?";
        query+="id="+id+"&token="+token;
        String jsonData="";
        jsonData=serverConnection(query);
        return jsonData;
    }
    //============================================Attend=============================================================================
    public String Attend(String token,String eventId, String isAttend)
    {
        String query="attend?";
        query+="token="+token+"&eventid="+eventId+"&isAttend="+isAttend;
        String jsonData="";
        jsonData=serverConnection(query);
        return jsonData;
    }
    //=========================================get Attendees===============================================================================
    public JSONArray getAttendees(String eventId, String token)
    {
        JSONObject jsonObject;
        JSONArray jsonArray;
        String query="attendees?";
        query+="eventid="+eventId+"&token="+token;
        String jsonData = "";
        try{
            jsonData = "{ Attendees:\n";
            jsonData += serverConnection(query);
            jsonData += "}";

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("Attendees");

        } catch (Exception e) {
            return null;
        }

    return jsonArray;
    }
  //  ===============================================get User Events============================================================================
    public JSONArray getUserEvents(String token)
    {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String jsonData = "";
        String query="calendar?token="+token;

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
    //====================================================Registration===================================================================
public  String userRegistration(String firstName,String lastName,String email,int password)
{
    String query="userlogin?email="+email+"&password="+password+"&firstname="+firstName+"&lastname="+lastName;
    String serverResponse=serverConnection(query);
    return responseCode + serverResponse;

}
//======================================================userLogin=======================================================================
public  String userLogin(String email,int password)
{
    String query="userlogin?email="+email+"&password="+password;
    String serverResponse=serverConnection(query);
    return responseCode + serverResponse;

}
//=====================================================google login======================================================================
    public String googleLogin()
    {
        String query="googlelogin";
        String serverResponse=serverConnection(query);
        return serverResponse;
    }
}
