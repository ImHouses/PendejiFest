package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import mx.unam.ciencias.jcasas.party.Event;
import mx.unam.ciencias.jcasas.pendejifest.Adapters.EventsAdapter;
import mx.unam.ciencias.jcasas.pendejifest.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private FloatingActionButton addEventsButton;
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
        firebasedb = FirebaseDatabase.getInstance();
        events = new ArrayList<>();
        DatabaseReference reference = firebasedb.getReference("events");
        list = (ListView) findViewById(R.id.list_main);
        eventsAdapter = new EventsAdapter(getApplicationContext(),events);
        reference.getRoot().child("events");
        buildUI();
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
        TextView nameHeader = (TextView)headerView.findViewById(R.id.header_name);
        nameHeader.setText(getUserName());
        addEventsButton = (FloatingActionButton) findViewById(R.id.buttonAddEvent);
        addEventsButton.setOnClickListener(this);
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
                makeAboutDialog();
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
            case R.id.nav_add_friend:
                String t = getResources().getString(R.string.friend_code_dialog_title);
                String m = getResources().getString(R.string.friend_code_dialog_message);
                makeFriendCodeDialog(t, m).show();
                break;
            case R.id.nav_signout:
                signOut();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Register a new friend in the {@link FirebaseDatabase}.
     * @param code The code of the friend to register.
     */
    private void addFriend(String code) {
        final DatabaseReference reference = firebasedb.getReference().getRoot();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        reference.child("users_codes").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myUid = mAuth.getCurrentUser().getUid();
                String friendUid = (String) dataSnapshot.getValue();
                reference.getRoot().child("friends").child(friendUid).child(myUid).setValue(myUid);
                reference.getRoot().child("friends").child(myUid).child(friendUid).setValue(friendUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String title = getResources().getString(R.string.error_title);
                String message = getResources().getString(R.string.friend_register_error);
                makeAlertWithNeutralButton(title, message);
            }
        });
    }

    /**
     * Make an {@link android.support.v7.app.AlertDialog} with the input of the friend code and
     * returns it.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private AlertDialog makeFriendCodeDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        String positveText = getResources().getString(R.string.friend_code_dialog_positve);
        String negativeText = getResources().getString(R.string.dialog_negative_std);
        builder.setPositiveButton(positveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addFriend(input.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    /**
     * Make an {@link android.support.v7.app.AlertDialog} and returns it.
     * @param title The title of the alert.
     * @param message The message of the alert.
     * @return The AlertDialog created.
     */
    private AlertDialog makeAlertWithNeutralButton(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    /**
     * Auxiliar method for creating snackbars and showing them.
     * @param message The message that the snackbar will display.
     */
    private void makeSnackbar(String message) {
        Snackbar.make(this.drawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Auxiliar method for creating Alert Dialogs.
     * @param
     */
    private void makeAboutDialog() {

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

    private String getUserName() { return (user != null) ? user.getDisplayName() : null; }

    private String getUserEmail() {
        return user.getEmail() != null ? user.getEmail() : null;
    }

    /**
     * Auxiliar method for sign out the current user, it deletes the {@link SharedPreferences}
     * user preferences and signs out from FirebaseAuth.
     */
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor
                = getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        editor.remove("email");
        editor.remove("pass");
        editor.apply();
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddEvent:
                startActivity(new Intent(this, NewEventActivity.class));
                break;
        }
    }
}
