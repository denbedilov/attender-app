package com.attender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Shai Pe'er on 03/05/2015.
 */
public class AttenderBL
{
    private AttenderDAL dal;

    //=============================================== BUILDER ==============================================================================

    public AttenderBL()
    {
       dal = new AttenderDAL();
    }


    //=============================================== GET EVENTS ==============================================================================

    //The function return the relevant events by search values.
    //Return values: null meaning that there are no relevant events, else return the events
    public ArrayList<Event> getEvents(String eventType, String eventDate, String eventLocation)
    {
        Date date;
        Event ev;
        JSONArray jsonArr;
        ArrayList<Event> events = new ArrayList<Event>();
        DateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

        jsonArr = dal.getEvents(eventType, eventDate, eventLocation);
        if(jsonArr == null)
            return null;

        try
        {
           // JSONArray jEventArr = jo.getJSONArray("Events");
            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject childJSONObject = jsonArr.getJSONObject(i);
                date = convertMilliSecondsToDate(childJSONObject.getString("date"));

                // change city name NULL and Unknown
                String city = "";
                if(childJSONObject.getString("city").compareTo("null") == 0 || childJSONObject.getString("city").compareTo("Unknown") == 0)
                    city = "somewhere in Israel";
                else
                    city = childJSONObject.getString("city");

                if(date != null)
                {
                    ev = new Event(
                            childJSONObject.getString("id"),
                            dateFormatDate.format(date),
                            childJSONObject.getString("name"),
                            dateFormatTime.format(date),
                            // change city name
                            city,
                            childJSONObject.getString("address"),
                            childJSONObject.getString("description"),
                            childJSONObject.getString("event_url"),
                            childJSONObject.getString("host"),
                            childJSONObject.getString("price"),
                            date,
                            childJSONObject.getString("attendees")
                    );

                    events.add(ev);
                }
            }
        }
        catch(JSONException e)
        {
           events = null;
        }

        return events;
    }
    //==============================================Get user Events==========================================================================================
    public ArrayList<Event> getUserEvents(String token)
    {
        Date date;
        Event ev;
        JSONArray jsonArr;
        ArrayList<Event> userEvents = new ArrayList<Event>();
        DateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

        jsonArr = dal.getUserEvents(token);
        if(jsonArr == null)
            return null;

        try
        {
            // JSONArray jEventArr = jo.getJSONArray("Events");
            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject childJSONObject = jsonArr.getJSONObject(i);
                date = convertMilliSecondsToDate(childJSONObject.getString("date"));

                // change city name NULL and Unknown
                String city = "";
                if(childJSONObject.getString("city").compareTo("null") == 0 || childJSONObject.getString("city").compareTo("Unknown") == 0)
                    city = "somewhere in Israel";
                else
                    city = childJSONObject.getString("city");

                if(date != null)
                {
                    ev = new Event(
                            childJSONObject.getString("id"),
                            dateFormatDate.format(date),
                            childJSONObject.getString("name"),
                            dateFormatTime.format(date),
                            // change city name
                            city,
                            childJSONObject.getString("address"),
                            childJSONObject.getString("description"),
                            childJSONObject.getString("event_url"),
                            childJSONObject.getString("host"),
                            childJSONObject.getString("price"),
                            date,
                            childJSONObject.getString("attendees")
                    );

                    userEvents.add(ev);
                }
            }
        }
        catch(JSONException e)
        {
            userEvents = null;
        }

        return userEvents;
    }
    //=============================================== CONVERT MILI-SEC TO DATE ==============================================================================

    //Convert the mili-sec string to date object, in case of fail, return null
    private Date convertMilliSecondsToDate(String miliSecDateString)
    {
        Date date = new Date();
        try
        {
            long milliSeconds = Long.parseLong(miliSecDateString);
            date.setTime(milliSeconds);
        }
        catch(NumberFormatException e)
        {
             return null;
        }
        return date;
    }


    //============================================Facebook Login===========================================================

    public String loginToServer(String id, String token)
    {
        return dal.loginToServer(id, token);
    }
    //=======================================user Registration/login==========================================================

    public String userRegistration(String name,String lastName,String email,int password)
    {
        return dal.userRegistration(name,lastName,email,password);
    }
    public String userLogin(String email,int password)
    {
        return dal.userLogin(email,password);
    }
    //============================================googlelogin=======================================================
    public String googleLogin(String firstName, String lastName, String email)
    {
        return dal.googleLogin(firstName, lastName, email);
    }
    //=============================================Attend============================================================
    public String Attend(String token,String eventId, boolean isAttend)
    {
        if(isAttend)
            return dal.Attend(token,eventId, "true");
        else
            return dal.Attend(token,eventId, "false");
    }
    //=============================================getAttendees===================================================
    public ArrayList<Attendee> getAttendees(String eventId, String token,String fbToken )
    {
        Attendee at;
        ArrayList<Attendee> attendees=new ArrayList<Attendee>();
        JSONArray attendeesJson;
        attendeesJson = dal.getAttendees(eventId,token,fbToken);
        if(attendeesJson == null)
            return null;
        try
        {
            // JSONArray jEventArr = jo.getJSONArray("Events");
            for (int i = 0; i < attendeesJson.length(); i++)
            {
                JSONObject childJSONObject = attendeesJson.getJSONObject(i);
                boolean ff = false;
                if(childJSONObject.getString("fbf").equals("true"))
                    ff = true;
                at= new Attendee(
                        childJSONObject.getString("name"),
                        childJSONObject.getString("lastname"),
                        ff,
                        childJSONObject.getString("id")
                    );
                    attendees.add(at);
                }
        }
        catch(JSONException e)
        {
            attendees = null;
        }
        return attendees;
    }
    public String getUserDetails(String token)
    {
        String userDetails="";
        JSONArray userDetailsJson=dal.getUserDetails(token);
        if(userDetailsJson == null)
            return null;
        try {
            // JSONArray jEventArr = jo.getJSONArray("Events");
            for (int i = 0; i < userDetailsJson.length(); i++) {
                JSONObject childJSONObject = userDetailsJson.getJSONObject(i);
                userDetails =  childJSONObject.getString("name")+" "+ childJSONObject.getString("lastname");
            }
        }
            catch(JSONException e)
            {
                userDetails = "";
            }
        return userDetails;

    }
}
