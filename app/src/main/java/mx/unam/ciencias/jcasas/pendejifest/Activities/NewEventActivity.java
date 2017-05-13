package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

import mx.unam.ciencias.jcasas.party.Date;
import mx.unam.ciencias.jcasas.pendejifest.R;

public class NewEventActivity extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText editName;
    private Button editDate;
    private Button editTime;
    private Button editAddress;
    private EditText editDescription;
    private SwitchCompat switchGuests;
    private double latitude;
    private double longitude;
    private Date date;
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;

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
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.editNewEventTime:
                Calendar t = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(this,
                        this,
                        t.get(Calendar.HOUR_OF_DAY),
                        t.get(Calendar.MINUTE),
                        true);
                dialog.show();
                break;
            case R.id.editNewEventAddress:
                startActivityForResult(new Intent(this, LocationSearchActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                latitude = data.getDoubleExtra("Latitude", 1);
                longitude = data.getDoubleExtra("Longitude", 1);
                editAddress.setText(data.getStringExtra("Address"));
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        Date d = new Date(year, month, dayOfMonth);
        int[] data = d.getDate();
        String date = String.format("%s %d, %s, %d",
                getStringRes(data[0]),
                data[1],
                getStringRes(data[2]),
                data[3]);
        editDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        String time;
        if (minute < 10)
            time = String.format("%d : 0%d", hour, minute);
        else time = String.format("%d : %d", hour, minute);
        editTime.setText(time);
    }

    private String getStringRes(int id) {
        return getResources().getString(id);
    }
}
