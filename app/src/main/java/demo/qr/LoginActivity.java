package demo.qr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView link_signup;
    private EditText user_email,user_password;
    private Button login_button;
    private TextInputLayout layout_email,layout_password;
    private AnimationDrawable animationDrawable;
    private ScrollView scrollView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        scrollView= (ScrollView) findViewById(R.id.scroll_view);
        user_email= (EditText) findViewById(R.id.input_email);
        user_password= (EditText) findViewById(R.id.input_password);
        login_button= (Button) findViewById(R.id.btn_login);
        link_signup= (TextView) findViewById(R.id.link_signup);
        layout_email = (TextInputLayout) findViewById(R.id.layout_email);
        layout_password = (TextInputLayout) findViewById(R.id.layout_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        link_signup.setOnClickListener(this);
        login_button.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login:
                login();
                break;

            case R.id.link_signup:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
        }
    }

    public void login()
    {
        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user_email.getText().toString(), user_password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                                          Snackbar snackbar;

                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              progressDialog.dismiss();
                                              if (e instanceof FirebaseAuthInvalidUserException) {
                                                  snackbar = Snackbar.make(link_signup, "ERROR USER NOT FOUND ", Snackbar.LENGTH_SHORT);
                                                  snackbar.show();
                                              } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                  snackbar = Snackbar.make(link_signup, "ERROR INVALID CREDENTIALS", Snackbar.LENGTH_SHORT);
                                                  snackbar.show();
                                              }
                                          }
                                      }
                );
    }
}