package com.attender;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AttendeesAdapter extends ArrayAdapter<String>
{
    private final Context context;
    LayoutInflater lInflater;
    private final ArrayList<Attendee> attendees;


    public AttendeesAdapter(Context context, ArrayList<Attendee> attendees)
    {
        super(context, R.layout.attendee_layout);
        this.context = context;
        this.attendees = attendees;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.attendee_layout, parent, false);
        }

        Attendee at = getAttendee(position);
        ((TextView) view.findViewById(R.id.attendee_name)).setText(at.get_firstName() + " " + at.get_lastName());
        if(at.is_ff())
        {
            ImageView iv = new ImageView(getApplicationContext());
            iv.setImageResource(R.drawable.facebook_friend);
            //((TextView) view.findViewById(R.id.attendee_is_friend)).setText("friend");
            //at.
            ((ImageView) view.findViewById(R.id.attendee_ff)).setImageResource(R.drawable.facebook_icon_ff);
        }
        else
            ((ImageView) view.findViewById(R.id.attendee_ff)).setImageResource(R.drawable.facebook_icon_not_ff);
            //((TextView) view.findViewById(R.id.attendee_is_friend)).setText("not friend");

        return view;
    }

    Attendee getAttendee(int position) {
        return attendees.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return attendees.size();
    }
}