package fp.dam.proy.proy_dam.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fp.dam.proy.proy_dam.R;

public class SharedDataActivity extends AppCompatActivity {

    String email, usuario, password;
    MaterialSwitch ctasSW, catsSW, transSW, statsSW;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_data);
        db = FirebaseFirestore.getInstance();

        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
        }

        ctasSW = findViewById(R.id.shared_cuentasSW);
        catsSW = findViewById(R.id.shared_categoriasSW);
        transSW = findViewById(R.id.shared_transSW);
        statsSW = findViewById(R.id.shared_statsSW);

        db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
           Map<String, Boolean> valores = (Map<String, Boolean>) task.getResult().get("visibilidad");
           try {
               ctasSW.setChecked(valores.get("cuentas"));
               catsSW.setChecked(valores.get("categorias"));
               transSW.setChecked(valores.get("transacciones"));
               statsSW.setChecked(valores.get("estadisticas"));
           } catch (NullPointerException e) {
               Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
           }
        });
    }

    public void confirm(View v) {
        db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Boolean> visibilidad = new HashMap<>();
                visibilidad.put("cuentas", ctasSW.isChecked());
                visibilidad.put("categorias", catsSW.isChecked());
                visibilidad.put("transacciones", transSW.isChecked());
                visibilidad.put("estadisticas", statsSW.isChecked());
                db.collection("users").document(usuario).update("visibilidad", visibilidad).addOnCompleteListener(complete -> {
                    if (complete.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Se ha cambiado la visibilidad", Toast.LENGTH_SHORT).show();
                });
                exit(v);
            }
        });
    }

    public void exit(View v) {
        Intent i = new Intent(this, AjustesActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }
}