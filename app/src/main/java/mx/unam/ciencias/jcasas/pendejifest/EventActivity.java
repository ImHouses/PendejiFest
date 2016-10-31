package mx.unam.ciencias.jcasas.pendejifest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.net.URL;
import mx.unam.ciencias.jcasas.party.Event;

public class EventActivity extends AppCompatActivity {

    ProgressDialog progress;
    Toolbar toolbar;
    TextView tvName;
    TextView tvDate;
    TextView tvDescription;
    TextView tvAddress;
    TextView tvAddressHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading event.");
        progress.show();
        buildUI();
        progress.dismiss();
    }

    /**
     * Build all the activity's elements.
     */
    private void buildUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_event);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("event_name");
        String date = intent.getStringExtra("event_date");
        String description = intent.getStringExtra("event_description");
        String address = intent.getStringExtra("event_address");
        String addressHint = intent.getStringExtra("event_addresshint");
        toolbar.setTitle(name);
        tvName = (TextView) findViewById(R.id.textViewEventName);
        tvDate = (TextView) findViewById(R.id.textViewDate);
        tvDescription = (TextView) findViewById(R.id.textViewDescription);
        tvAddress = (TextView) findViewById(R.id.textViewAddress);
        tvAddressHint = (TextView) findViewById(R.id.textViewHintAddress);
        tvName.setText(name);
        tvDate.setText(date);
        tvDescription.setText(getResources().getText(R.string.event_infoSign).toString() +
                "\n" + description);
        tvAddress.setText(getResources().getText(R.string.event_directionSign).toString() +"\n" +
                address);
        tvAddressHint.setText(getResources().getText(R.string.event_directionhowSign).toString() +
                "\n" + addressHint);
    }
}
