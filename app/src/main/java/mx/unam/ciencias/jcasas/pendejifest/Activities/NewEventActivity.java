package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mx.unam.ciencias.jcasas.pendejifest.R;

public class NewEventActivity extends AppCompatActivity
        implements View.OnClickListener {

    private EditText editName;
    private Button editDate;
    private Button editTime;
    private Button editAddress;
    private EditText editDescription;
    private SwitchCompat switchGuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        setSupportActionBar(((Toolbar) findViewById(R.id.toolbarNewEvent)));
        getSupportActionBar().setTitle(getResources().getString(R.string.new_event_title));
        buildUi();
    }

    private void buildUi() {
        editName = (EditText) findViewById(R.id.editCreateEventName);
        editDate = (Button) findViewById(R.id.editNewEventDate);
        editTime = (Button) findViewById(R.id.editNewEventTime);
        editAddress = (Button) findViewById(R.id.editNewEventAddress);
        editDescription = (EditText) findViewById(R.id.editNewEventDescription);
        switchGuests = (SwitchCompat) findViewById(R.id.switchNewEvent);
        editDate.setOnClickListener(this);
        editTime.setOnClickListener(this);
        editAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.editNewEventDate:
                Toast.makeText(this, "FECHA", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editNewEventTime:
                Toast.makeText(this, "HORA", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editNewEventAddress:
                Toast.makeText(this, "DIRECCIÓN", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
