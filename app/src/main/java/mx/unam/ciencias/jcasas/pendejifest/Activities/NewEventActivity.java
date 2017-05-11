package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mx.unam.ciencias.jcasas.pendejifest.R;

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbarNewEvent)));
        getSupportActionBar().setTitle(getResources().getString(R.string.new_event_title));
    }
}
