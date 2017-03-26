package mx.unam.ciencias.jcasas.pendejifest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mx.unam.ciencias.jcasas.party.Event;
import mx.unam.ciencias.jcasas.party.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ListView list;
    private ProgressDialog progress;
    private ArrayList<Event> events;
    private static final String STRING_TAG = "LOG: ";
    private static String FIREBASE_URL="https://happening-93473.firebaseio.com/";
    private FirebaseDatabase firebasedb;
    private ArrayAdapter<Event> eventsAdapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
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
        View headerView = navDrawer.getHeaderView(0);
        TextView emailHeader = (TextView)headerView.findViewById(R.id.header_email);
        emailHeader.setText(getUserEmail());
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.nav_about:
                makeSnackbar("Powered by Firebase. <3");
                break;
            case R.id.nav_user:
                makeSnackbar("There's no user!");
                break;
            case R.id.nav_settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_events:
                makeSnackbar("Soon my son!");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public String getUserName() {
        if (user != null) {
            return user.getDisplayName();
        } else {
            return "";
        }
    }

    public String getUserEmail() {
        if (user != null) {
            return user.getEmail();
        } else {
            return "";
        }
    }

}
