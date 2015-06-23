package com.attender;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {
    Map<String, Integer> map = new HashMap<String, Integer>();
    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {

        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author + ": ");

        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername))
        {
            authorText.setTextColor(Color.RED);
            ((RelativeLayout) view).setGravity(Gravity.RIGHT);
            //((RelativeLayout) view).setBackground();

        }
        else
        {
            int color;
            if (map.containsKey(author))
                color = map.get(author);
            else
            {
                Random rnd = new Random();
                color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                while(map.containsValue(color)&&color==Color.RED)
                {
                    color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                }
                map.put(author, color);
            }
            authorText.setTextColor(color);
            ((RelativeLayout) view).setGravity(Gravity.LEFT);
        }

        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());

    }
}
