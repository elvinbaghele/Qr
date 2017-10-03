package demo.qr;

import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText user_name,user_email,collid,user_password,confirm_password,deptid;
    private Button signup_button;
    private AnimationDrawable animationDrawable;
    private ScrollView scrollView;
    private int coin;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        user_name= (EditText) findViewById(R.id.input_name);
        user_email= (EditText) findViewById(R.id.input_email);
        user_password= (EditText) findViewById(R.id.input_password);
        confirm_password= (EditText) findViewById(R.id.input_reEnterPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        signup_button= (Button) findViewById(R.id.btn_signup);
        signup_button.setOnClickListener(this);

    }



    private void signup()
    {
        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user_email.getText().toString(),user_password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        String uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = (FirebaseDatabase.getInstance().getReference().child("users").child(uid));
                        userRef.child("name").setValue(user_name.getText().toString());
                        userRef.child("email").setValue(user_email.getText().toString());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {

                        progressDialog.dismiss();

                        if (e instanceof FirebaseAuthUserCollisionException)
                        {
                            //The email address is already in use by another account.
                        }
                        else if (e instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            //The email address is badly formatted.
                        }
                        else if (e instanceof FirebaseException)
                        {
                            //An internal error has occurred. [ WEAK_PASSWORD  ]
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        //new Register().execute();
        signup();
    }
}

