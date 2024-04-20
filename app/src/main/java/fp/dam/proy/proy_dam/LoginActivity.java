package fp.dam.proy.proy_dam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.logEdtUsuario);
        passwordInput = findViewById(R.id.logEdtContrasena);
    }

    public void login(View v) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Toast.makeText(this, "No se ha podido iniciar sesion", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("email", email);
                startActivity(i);
                finish();
            }
        });

    }

    public void goto_signup(View v) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}