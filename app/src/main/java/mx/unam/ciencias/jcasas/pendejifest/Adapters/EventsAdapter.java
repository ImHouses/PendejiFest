package mx.unam.ciencias.jcasas.pendejifest.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.unam.ciencias.jcasas.party.Event;
import mx.unam.ciencias.jcasas.pendejifest.Activities.EventActivity;
import mx.unam.ciencias.jcasas.pendejifest.R;

/**Protected class EventsAdapter {
 * Private class EventsAdapter for creating a custom adapter for objects of type {@link Event}
 * and use them in the NavigationDrawer of this activity.
 * Provides one method for inflating the view of each event.
 */
public class EventsAdapter extends ArrayAdapter<Event> {

    private Context mContext;

    public EventsAdapter(Context context, ArrayList<Event> events) {
        super(context,0,events);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Event event = getItem(position);
        LayoutInflater inflater = (LayoutInflater) mContext.
                getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_event, null);
        TextView eventName = (TextView) view.findViewById(R.id.tvNameEventItem);
        TextView eventDate = (TextView) view.findViewById(R.id.tvDateEventItem);
        eventName.setText(event.getName());
        eventDate.setText(event.getDate().toString());
        TextView eventInfo = (TextView) view.findViewById(R.id.tvEventinfo);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEventinfo(event);
            }
        });
        return view;
    }

    public void toEventinfo(Event event) {
        Intent i = new Intent(mContext,EventActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("event_name",event.getName());
        i.putExtra("event_description",event.getDescription());
        i.putExtra("event_address",event.getAddress());
        mContext.startActivity(i);
    }
}
