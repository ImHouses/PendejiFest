package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.unam.ciencias.jcasas.pendejifest.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";
    private SharedPreferences preferences;

    private EditText editTextLogIn;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private TextView signupTextView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        buildUI();
    }

    private void buildUI() {
        editTextLogIn = (EditText) findViewById(R.id.edit_text_mail);
        editTextPassword = (EditText) findViewById(R.id.edit_text_pass);
        buttonLogIn = (Button) findViewById(R.id.button_log_in);
        signupTextView = (TextView) findViewById(R.id.text_view_signup);
        signupTextView.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);
    }

    /**
     * Validates the email and the password
     * @param email The email of the user.
     * @param pass The password of the user.
     * @return <code>true</code> if the email and pass are correct, false in other case.
     */
    private boolean validCredentials(String email, String pass) {
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isValidPass(pass)) {
            Toast.makeText(this, "Password must have at least 7 characters.", Toast.LENGTH_LONG)
                    .show();
            return false;
        } else return true;
    }

    /**
     * Intent to the MainActivity.
     */
    private void toMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    /**
     * Returns <code>true</code> if the given email is a correct Email Address.
     * @param email
     * @return <code>true</code> if the given email is in effect, an email.
     */
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Returns <code>true</code> if the given pass has length greater than 6 chars.
     * @param pass The password.
     * @return <code>true</code> if the length is at least 6.
     */
    private boolean isValidPass(String pass) {
        return pass.length() >= 6;
    }

    /**
     * Save preferences.
     */
    private void saveOnPreferences(String email, String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.apply();
    }

    private boolean isLogged() {
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.contains("email") && preferences.contains("pass");
    }

    private void loginFirebase(String email, String pass) {
        final String em = email;
        final String ps = pass;
        if (validCredentials(email, pass)) {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                toMain();
                                saveOnPreferences(em, ps);
                            }
                            else {
                                makeLocalAlert("There is a problem", "There has been a problem, try again").show();
                            }
                        }
                    });
        } else {
            makeLocalAlert("Your credentials are not valid", "Your email or password are not valid, verify them.").show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_log_in:
                String email = editTextLogIn.getText().toString();
                String pass = editTextPassword.getText().toString();
                loginFirebase(email, pass);
                break;
            case R.id.text_view_signup:
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                break;
        }
    }

    public AlertDialog makeLocalAlert(String title, String message) {
        AlertDialog.Builder alb = new AlertDialog.Builder(LoginActivity.this);
        AlertDialog ad = alb.create();
        ad.setTitle(title);
        ad.setMessage(message);
        return ad;
    }
}
