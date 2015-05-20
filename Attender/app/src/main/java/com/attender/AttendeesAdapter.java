package com.attender;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendeesAdapter extends ArrayAdapter<String>
{
    private final Context context;
    LayoutInflater lInflater;
    private final ArrayList<Attendee> attendees;


    public AttendeesAdapter(Context context, ArrayList<Attendee> attendees)
    {
        super(context, R.layout.event);
        this.context = context;
        this.attendees = attendees;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.event, parent, false);
        }

        Attendee at = getAttendee(position);
        ((TextView) view.findViewById(R.id.tvName)).setText(at.get_firstName());
        ((TextView) view.findViewById(R.id.tvCity)).setText(at.get_lastName());


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