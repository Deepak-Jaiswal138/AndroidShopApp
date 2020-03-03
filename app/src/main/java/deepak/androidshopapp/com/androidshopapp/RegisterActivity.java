package deepak.androidshopapp.com.androidshopapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText ufirstname,ulastname,uemail,upassword,uconfpassword,ucontactno;
    Button btnRegister;
    TextInputLayout userFirstNameWrapper,userLastNameWrapper,userEmailWrapper,userPassswordWrapper,userConfPasswordWrapper,userContactWrapper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        ufirstname=findViewById(R.id.userFirstName);
        ulastname=findViewById(R.id.userLastName);
        uemail=findViewById(R.id.userEmailAddress);
        upassword=findViewById(R.id.userPassword);
        uconfpassword=findViewById(R.id.userConfPassword);
        ucontactno=findViewById(R.id.userContactNumber);

        userFirstNameWrapper=findViewById(R.id.userFirstNameWrapper);
        userLastNameWrapper=findViewById(R.id.userLastNameWrapper);
        userEmailWrapper=findViewById(R.id.userEmailWrapper);
        userPassswordWrapper=findViewById(R.id.userPasswordWrapper);
        userConfPasswordWrapper=findViewById(R.id.userConfPasswordWrapper);
        userContactWrapper=findViewById(R.id.userContactNoWrapper);

        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser()!=null){
                    //user is logged in and can redirect to another actiity
                }else {
                    final String firstname = ufirstname.getText().toString().trim();
                    final String lastname = ulastname.getText().toString().trim();
                    final String email = uemail.getText().toString().trim();
                    String password = upassword.getText().toString().trim();
                    String confpassword = uconfpassword.getText().toString().trim();
                    final String contactno = ucontactno.getText().toString().trim();

                    if (firstname.isEmpty()) {
                        userFirstNameWrapper.setError("Enter Your First Name");
                        userFirstNameWrapper.requestFocus();
                        return;
                    }
                    if (lastname.isEmpty()) {
                        userLastNameWrapper.setError("Enter Your Last Name");
                        userLastNameWrapper.requestFocus();
                        return;
                    }
                    if (email.isEmpty()) {
                        userEmailWrapper.setError("Enter Your Email");
                        userEmailWrapper.requestFocus();
                        return;
                    }
                    if (password.isEmpty()) {
                        userPassswordWrapper.setError("Enter Your Password");
                        userPassswordWrapper.requestFocus();
                        return;
                    }
                    if (confpassword.isEmpty()) {
                        userConfPasswordWrapper.setError("Enter Your Confirm Password");
                        userConfPasswordWrapper.requestFocus();
                        return;
                    }
                    if (!password.equals(confpassword)) {
                        userConfPasswordWrapper.setError("Password didn't match");
                        userConfPasswordWrapper.requestFocus();
                        return;
                    }
                    if (contactno.isEmpty()) {
                        userContactWrapper.setError("Enter Your Contact Number");
                        userContactWrapper.requestFocus();
                        return;
                    }
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                User user=new User(firstname,lastname,email,contactno);
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
