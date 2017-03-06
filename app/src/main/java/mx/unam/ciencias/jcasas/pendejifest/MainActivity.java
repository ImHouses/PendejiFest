package mx.unam.ciencias.jcasas.pendejifest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import mx.unam.ciencias.jcasas.party.Event;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private ListView list;
    private ProgressDialog progress;
    private ArrayList<Event> events;
    private static final String STRING_TAG = "LOG: ";
    private static String FIREBASE_URL="https://happening-93473.firebaseio.com/";
    FirebaseDatabase firebasedb;
    ArrayAdapter<Event> eventsAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Loading your events.");
        progress.show();
        events = new ArrayList<>();
        firebasedb = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebasedb.getReference("events");
        list = (ListView) findViewById(R.id.list_main);
        eventsAdapter = new EventsAdapter(getApplicationContext(),events);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name;
                String date;
                String description;
                String address;
                String addressinfo;
                events.clear();
                Iterable<DataSnapshot> eventsData = dataSnapshot.getChildren();
                for (DataSnapshot ds : eventsData) {
                    Log.i(STRING_TAG,ds.getValue().toString());
                    name = ds.child("name").getValue(String.class);
                    date = ds.child("date").getValue(String.class);
                    address = ds.child("address").getValue(String.class);
                    description = ds.child("info").getValue(String.class);
                    addressinfo = ds.child("addressinfo").getValue(String.class);
                    Log.i(STRING_TAG,name + date + address + description + addressinfo);
                    events.add(new Event(name,date,description,addressinfo,address));
                }
                ArrayAdapter<Event> eventsAdapter = new EventsAdapter(getApplicationContext(),
                                                                                        events);
                list.setAdapter(eventsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buildUI();
        progress.dismiss();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Auxiliar method for building the UI.
     */
    private void buildUI() {
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.open_navigation,R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navDrawer = (NavigationView) findViewById(R.id.nvView);
        navDrawer.setNavigationItemSelectedListener(this);
    }

    /**
     * Method for set a custom action for the button back.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    /**
     * Navigation Bar menu drawer.
     * @param menu The menu resource.
     * @return <code>true</code> if the
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menu) {
        int id = menu.getItemId();
        if (id == R.id.nav_about) {
            makeSnackbar("Powered by Firebase. <3");
        } else if (id == R.id.nav_user) {
            makeSnackbar("There's no user!");
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_events) {
            makeSnackbar("Soon my son!");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    /**
     * Auxiliar method for creating snackbars and showing them.
     * @param message The message that the snackbar will display.
     */
    private void makeSnackbar(String message) {
        Snackbar.make(this.drawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * Private class EventsAdapter for creating a custom adapter for objects of type {@link Event}
     * and use them in the NavigationDrawer of this activity.
     * Provides one method for inflating the view of each event.
     */
    private class EventsAdapter extends ArrayAdapter<Event> {

        private Context mContext;

        public EventsAdapter(Context context, ArrayList<Event> events) {
            super(context,0,events);
            mContext = context;
            Event e;
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
            eventDate.setText(event.getDate());
            Log.i(STRING_TAG,"This is the event in the adapter: " + event.getName() +
                                                                    event.getDate() +
                                                                    event.getAddress());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,EventActivity.class);
                    i.putExtra("event_name",event.getName());
                    i.putExtra("event_date", event.getDate());
                    i.putExtra("event_description",event.getDescription());
                    i.putExtra("event_address",event.getAddress());
                    i.putExtra("event_addresshint",event.getAddressInfo());
                    startActivity(i);
                }
            });
            return view;
        }
    }
}
