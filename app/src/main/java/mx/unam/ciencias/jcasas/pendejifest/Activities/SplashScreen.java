package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import mx.unam.ciencias.jcasas.pendejifest.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(900);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null
                            && getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("email")
                            && getSharedPreferences("Preferences", Context.MODE_APPEND).contains("pass"))  {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

            }
        };
        t.start();
    }
}


