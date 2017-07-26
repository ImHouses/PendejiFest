package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import mx.unam.ciencias.jcasas.pendejifest.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputName;
    private EditText inputMail;
    private EditText inputPass;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private String email;
    private String pass;
    private String name;
    private String photoUrl;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        bindUi();
    }

    /**
     * Auxiliar method for building the UI.
     */
    private void bindUi() {
        inputName = (EditText) findViewById(R.id.editTextNameSignup);
        inputMail = (EditText) findViewById(R.id.editTextEmail);
        inputPass = (EditText) findViewById(R.id.editTextPass);
        buttonSignUp = (Button) findViewById(R.id.buttonSignup);
        buttonSignUp.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignUp) {
            register();
        }
    }

    /**
     * Method for registering the user.
     */
    public void register() {
        name = inputName.getText().toString();
        email = inputMail.getText().toString();
        pass = inputPass.getText().toString();
        if (validCredentials(email, pass)) {
            progressDialog.setMessage(ActivityUtils.getStringByRes(R.string.signup_progress, this));
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        //UPLOAD THE PHOTO.
                        userUid = firebaseAuth.getCurrentUser().getUid();
                        pushUser();
                        pushUserCode();
                        getPhotoUrl();
                        saveOnPreferences(name, email, pass, photoUrl);
                        updateProfile(firebaseAuth.getCurrentUser());
                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        String alertTitle = ActivityUtils
                                .getStringByRes(R.string.error_title, SignupActivity.this);
                        String alertMessage = task.getResult().toString();
                        ActivityUtils
                                .makeStdAlert(alertTitle, alertMessage, SignupActivity.this)
                                .show();
                    }
                }
            });
        }
    }
    /**
     * Generates and pushes the new user code for adding friends.
     */
    private void pushUserCode() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Random r = new Random();
        reference.getRoot().child("users_codes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                char[] a = firebaseAuth.getCurrentUser().getUid().toCharArray();
                while (true) {
                    String code = "";
                    for (int i = 0; i < 5; i++)
                        code += a[i];
                    code += new Integer(r.nextInt()).toString();
                    if (!dataSnapshot.hasChild(code)) {
                        reference.child("users_codes").child(code).setValue(userUid);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Pushes the user in the FirebaseDatabase
     */
    private void pushUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.getRoot().child("users").child(userUid).child("email").setValue(email);
        reference.getRoot().child("users").child(userUid).child("name").setValue(name);
        if (photoUrl != null)
            reference.getRoot().child("users").child(userUid).child("photo_url").setValue(photoUrl);
    }

    /**
     * Gets the photo URL of the current user, <code>null</code> will be the photo URL
     * in case the user does not have a photo.
     */
    private void getPhotoUrl() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.getRoot().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photoUrl = dataSnapshot
                        .child(userUid)
                        .child("photo_url").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    /**
     * Checks if the credentials are valid
     * @param email The email to check.
     * @param pass The password to check.
     * @return <code>true</code> if the email and the password are valid credentials.
     */
    public boolean validCredentials(String email, String pass) {
        return (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(email)
                && isValidEmail(email)
                && isValidPass(pass));
    }

    /**
     * Indicates if the password given is correct.
     * @param pass The string that contains the password.
     * @return <code>true</code> if the password is correct, <code>false</code> in other case.
     */
    public boolean isValidPass(String pass) {
        if (pass.length() < 6) {
            Toast.makeText(SignupActivity.this,
                        "The password length must be greater than 6 characters",
                        Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    /**
     * Checks whether the email is valid or not.
     * @param email
     * @return <code>true</code> if the email is correct, <code>false</code> in other case.
     */
    public boolean isValidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this,
                        "The email address is not valid",
                        Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }



    /**
     * Save the new user data into the {@link SharedPreferences}
     * @param name
     * @param email
     * @param pass
     * @param photoUrl
     */
    private void saveOnPreferences(String name, String email, String pass, String photoUrl) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.putString("name", name);
        if (photoUrl == null)
            editor.putString("photoUrl", "");
        else editor.putString("photoUrl", photoUrl);
        editor.apply();
    }

    /**
     * Update the profile name of the user.
     * @param user
     */
    private void updateProfile(FirebaseUser user) {
        if (user != null) {
            SharedPreferences sp = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            UserProfileChangeRequest profileChanges = new UserProfileChangeRequest.Builder()
                    .setDisplayName(sp.getString("name", "DEFAULT")).build();
            user.updateProfile(profileChanges);
        }
    }
}
