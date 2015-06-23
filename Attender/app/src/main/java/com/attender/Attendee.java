package com.attender;

/**
 * Created by Rita on 5/20/2015.
 */
public class Attendee
{
    private String _firstName;
    private String _lastName;
    private boolean _ff;
    private String _id;

    public Attendee(String firstName, String lastName ,boolean ff, String id)
    {
        _firstName = firstName;
        _lastName = lastName;
        _ff = ff;
        set_id(id);
    }

    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public boolean is_ff() {
        return _ff;
    }

    public void set_ff(boolean _ff) {
        this._ff = _ff;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
