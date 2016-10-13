package mx.unam.ciencias.jcasas.pendejifest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        TextView tvA = (TextView)findViewById(R.id.textViewAbout);
        String aboutText = "Pendeji Fest App In Developing\n Release:" +
                getResources().getString(R.string.date_dev).toString() +
                " Juan Casas";
        tvA.setText(aboutText);
        toolbar.setNavigationOnClickListener((v) -> {
            onBackPressed();
        });
    }
}
