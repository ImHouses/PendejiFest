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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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
        TextView tvname = (TextView) findViewById(R.id.textViewEventName);
        TextView tvdate = (TextView) findViewById(R.id.textViewDate);
        TextView tvdescription = (TextView) findViewById(R.id.textViewDescription);
        TextView tvAddress = (TextView) findViewById(R.id.textViewAddress);
        TextView tvAddressHint = (TextView) findViewById(R.id.textViewHintAddress);
        tvname.setText(name);
        tvdate.setText(date);
        tvdescription.setText(getResources().getText(R.string.event_infoSign).toString() +
                "\n" + description);
        tvAddress.setText(getResources().getText(R.string.event_directionSign).toString() +"\n" +
                address);
        tvAddressHint.setText(getResources().getText(R.string.event_directionhowSign).toString() +
                "\n" + addressHint);
        //new FetchEventTask().execute("E");

    }

    private class FetchEventTask extends AsyncTask<String,Integer,Event> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(EventActivity.this);
            progress.setMessage("Loading Event...");
            progress.show();
        }

        @Override
        protected Event doInBackground(String... params) {
            Event ev = null;
            try {
                URL url = new URL("https://raw.githubusercontent.com/ImHouses/ImHouses.github.io/master/resources/PendejifestAPP/EVENTS/birthday18/event.xml");
                Event event = new Event(url.openStream());
                ev = event;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ev;
        }

        @Override
        protected void onPostExecute(Event event) {
            super.onPostExecute(event);
            if (event == null) {
                progress.dismiss();

            } else {
                TextView tvname = (TextView) findViewById(R.id.textViewEventName);
                TextView tvdate = (TextView) findViewById(R.id.textViewDate);
                TextView tvdescription = (TextView) findViewById(R.id.textViewDescription);
                tvname.setText(event.getName());
                tvdate.setText(event.getDate());
                tvdescription.setText(event.getDescription());
                toolbar.setTitle(event.getName());
                progress.dismiss();
            }
        }
    }

}
