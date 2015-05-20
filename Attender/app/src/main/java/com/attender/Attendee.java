package com.attender;

/**
 * Created by Rita on 5/20/2015.
 */
public class Attendee
{
    private String _firstName;
    private String _lastName;
    private boolean _ff;

    public Attendee(String firstName,String lastName,boolean ff)
    {
        _firstName = firstName;
        _lastName = lastName;
        _ff=ff;
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
}
