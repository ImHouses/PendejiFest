package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.unam.ciencias.jcasas.pendejifest.Adapters.LocationSearchAdapter;
import mx.unam.ciencias.jcasas.pendejifest.R;

public class LocationSearchActivity extends AppCompatActivity {

    private EditText editQuery;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        buildUi();
    }

    private void buildUi() {
        editQuery = (EditText) findViewById(R.id.editSearchLocation);
        listView = (ListView) findViewById(R.id.listSearchResults);
        editQuery.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new AsyncGetter(s.toString()).execute();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address a = (Address) listView.getAdapter().getItem(position);
                Intent intent = new Intent();
                int i = 0;
                String s = "";
                while (a.getAddressLine(i) != null) {
                    if (i == 0)
                        s += a.getAddressLine(i) + ", ";
                    else s += a.getAddressLine(i);
                    i++;
                }
                intent.putExtra("Address", s);
                intent.putExtra("Latitude", a.getLatitude());
                intent.putExtra("Longitude", a.getLongitude());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private class AsyncGetter extends AsyncTask<Void, Void, List<Address>> {

        private Geocoder geocoder;
        private List<Address> a;
        private String s;

        public AsyncGetter(String s) {
            this.geocoder = new Geocoder(LocationSearchActivity.this);
            this.s = s;
        }

        @Override
        protected List<Address> doInBackground(Void... params) {
                    try {
                        a = geocoder.getFromLocationName(s, 10);
                    } catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                    return a;
        }

        @Override
        protected void onPostExecute(List<Address> a) {
            LocationSearchAdapter adapter = new LocationSearchAdapter(LocationSearchActivity.this, a);
            listView.setAdapter(adapter);
            listView.invalidateViews();
        }
    }

}
