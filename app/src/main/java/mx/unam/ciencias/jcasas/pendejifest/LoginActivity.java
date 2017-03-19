package mx.unam.ciencias.jcasas.pendejifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "";
    private SharedPreferences preferences;

    private EditText editTextLogIn;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        buildUI();
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextLogIn.getText().toString();
                String pass = editTextPassword.getText().toString();
                if (login(email, pass)) {
                    toMain();
                    saveOnPreferences(email, pass);
                }
            }
        });
    }

    private void buildUI() {
        editTextLogIn = (EditText) findViewById(R.id.edit_text_mail);
        editTextPassword = (EditText) findViewById(R.id.edit_text_pass);
        buttonLogIn = (Button) findViewById(R.id.button_log_in);
        signupTextView = (TextView) findViewById(R.id.text_view_signup);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        SpannableString str =
                new SpannableString(getString(R.string.signup));
        str.setSpan(new BackgroundColorSpan(Color.BLUE), 27, str.length(), 0);
        signupTextView.setText(str);
    }

    /**
     * Validates the email and the password
     * @param email The email of the user.
     * @param pass The password of the user.
     * @return <code>true</code> if the email and pass are correct, false in other case.
     */
    private boolean login(String email, String pass) {
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

}
