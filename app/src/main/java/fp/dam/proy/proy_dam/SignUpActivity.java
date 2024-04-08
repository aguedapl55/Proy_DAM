package fp.dam.proy.proy_dam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, passConfInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailInput = findViewById(R.id.supEdtUsuario);
        passwordInput = findViewById(R.id.supEdtContrasena);
        passConfInput = findViewById(R.id.supEdtContrasena2);
    }

    public void signup(View v) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (password.equals(passConfInput.getText().toString())) {
            FirebaseAuth fbAuth = FirebaseAuth.getInstance();
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                    Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(this, "Las contraseñas introducidas no son iguales", Toast.LENGTH_SHORT).show();
    }

}