package edu.rutgers.ece453.rupool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private static final int REQUESTCODE_SIGNUPACTIVITY = 508;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private TextView mTextViewResetPassword;

    private Button mButtonLogin;
    private Button mButtonSignUp;

    private FirebaseAuth mFirebaseAuth;


    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Views
        mEditTextEmail = findViewById(R.id.EditText_Email_LoginActivity);
        mEditTextPassword = findViewById(R.id.EditText_Password_LoginActivity);
        mTextViewResetPassword = findViewById(R.id.TextView_ResetPassword_LoginActivity);
        mButtonLogin = findViewById(R.id.Button_Login_LoginActivity);
        mButtonSignUp = findViewById(R.id.Button_SignUp_LoginActivity);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
            }
        });
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, SignUpActivity.class), REQUESTCODE_SIGNUPACTIVITY);
            }
        });

        mTextViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        // 已有用户登陆， 刷新用户信息
        if (mFirebaseAuth.getCurrentUser() != null)
            mFirebaseAuth.getCurrentUser().reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // 通过邮件验证的用户
                            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                finish();
                            } else {
                                // 未通过邮件验证
                                startActivity(new Intent(LoginActivity.this, WaitingEmailVerifyActivity.class));
                            }
                        }
                    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUESTCODE_SIGNUPACTIVITY: {
                switch (resultCode) {
                    case SignUpActivity.SIGNUP_SUCCESS: {
                        Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show();
//                        finish();
                        break;
                    }

                    case SignUpActivity.SIGNUP_FAILURE: {
                        Log.w(TAG, "onActivityResult: failure");
                        Toast.makeText(this, "Sign Up Failure", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case SignUpActivity.SIGNUP_CANCEL: {
                        Toast.makeText(this, "Sign Up Cancel", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    default: {
                        Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    boolean validate() {
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        mEditTextEmail.setError(null);
        mEditTextPassword.setError(null);

        if (email.isEmpty()) {
            mEditTextEmail.setError("This field is required");
            hideProgressDialog();
            return false;
        }

        if (password.isEmpty()) {
            mEditTextPassword.setError("This field is required");
            hideProgressDialog();
            return false;
        }


        return true;
    }

    private void login(String email, String password) {
        Log.d(TAG, "login: login " + email + "\t" + password);

        showProgressDialog();

        if (!validate()) return;

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
                            if (mFirebaseAuth.getCurrentUser() != null)
                                if (mFirebaseAuth.getCurrentUser().isEmailVerified())
                                    finish();
                                else
                                    startActivity(new Intent(LoginActivity.this, WaitingEmailVerifyActivity.class));
                        } else {
                            Log.w(TAG, "onComplete: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean validateForm() {
        return true;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
