package com.attender;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 *
 */
public class Event implements Serializable
{
    public Event(String id, String date, String name, String time, String city, String address, String description, String eventUrl, String host, String price, Date dateObject, String attendies)
    {
        setId(id);
        setDate(date);
        setName(name);
        setTime(time);
        setCity(city);
        setAddress(address);
        setDescription(description);
        setEventUrl(eventUrl);
        setHost(host);
        setDateObject(dateObject);
        setPrice(price);
        setAttendees(attendies);
    }
    private String _id;
    private String _date;
    private String _name;
    private String _time;
    private String _city;
    private String _address;
    private String _description;
    private String _eventUrl;
    private String _host;
    private String _price;
    private String _attendees;
    private Date   _dateObject;


    public String getId() {
        return _id;
    }

    public void setId(String id) { _id = id;  }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
        _date = date;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        _time = time;
    }

    public String getCity()
    {
        return _city;
    }

    public void setCity(String _city)
    {
        this._city = _city;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getHost() {
        return _host;
    }

    public void setHost(String host) {
        _host = host;
    }

    public Date getDateObject()
    {
        return _dateObject;
    }

    public void setDateObject(Date _dateObject)
    {
        _dateObject = (Date) _dateObject.clone();
    }

    public String getEventUrl() {
        return _eventUrl;
    }

    public void setEventUrl(String _eventUrl) {
        this._eventUrl = _eventUrl;
    }

    public String getPrice() {
        return _price;
    }

    public void setPrice(String _price) {
        this._price = _price;
    }

    public String getAttendees() { return _attendees; }

    public void setAttendees(String _attendees) { this._attendees = _attendees; }
}
