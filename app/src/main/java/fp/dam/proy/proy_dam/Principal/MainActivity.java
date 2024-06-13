package fp.dam.proy.proy_dam.Principal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import fp.dam.proy.proy_dam.Categorias.AddCategoriaActivity;
import fp.dam.proy.proy_dam.Categorias.CategoriasFrag;
import fp.dam.proy.proy_dam.Cuentas.AddCuentaActivity;
import fp.dam.proy.proy_dam.Cuentas.CuentasFrag;
import fp.dam.proy.proy_dam.EstadisticasFrag;
import fp.dam.proy.proy_dam.R;
import fp.dam.proy.proy_dam.Transacciones.AddTransaccionActivity;
import fp.dam.proy.proy_dam.Transacciones.TransaccionesFrag;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email, password, usuario;
    NavigationBarView nav;
    CuentasFrag cuentasFrag = new CuentasFrag();
    CategoriasFrag catFrag = new CategoriasFrag();
    TransaccionesFrag transFrag = new TransaccionesFrag();
    EstadisticasFrag estFrag = new EstadisticasFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
        }
        Log.wtf("APL USUARIO MAIN", usuario);
        Log.wtf("APL EMAIL MAIN", email);

        nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            return selectFragment(itemId);
        });
        nav.setOnItemReselectedListener(item -> {
            int itemId = item.getItemId();
            selectFragment(itemId);
        });
        nav.setSelectedItemId(R.id.navTransacciones);

        TextView placeholder = findViewById(R.id.addcat_textview);
        placeholder.setText(email);

    }

    public boolean selectFragment(int itemId) {
        if (itemId == R.id.navCuentas) {
            loadFragment(cuentasFrag);
        } else if (itemId == R.id.navCategorias) {
            loadFragment(catFrag);
        } else if (itemId == R.id.navTransacciones) {
            loadFragment(transFrag);
        } else if (itemId == R.id.navEstadisticas) {
            loadFragment(estFrag);
        } else return false;
        return true;
    }

    public void loadFragment(Fragment fragment) {
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("password", password);
        args.putString("usuario", usuario);
        fragment.setArguments(args); //pasa argumentos (email)

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); //cambia al fragmento seleccionado
        transaction.setReorderingAllowed(true); // optimiza cambios estados fragmentos
        transaction.addToBackStack(null); //atras tecla movil -> vuelve al fragmento anterior
        transaction.commit();
    }

    public void check_AddSmth(View v) {
        if (email.equals(usuario)) {
            Log.wtf("APL email == usuario", "true");
            goto_AddSmth(v);
        } else {
            try {
                Log.wtf("APL email == usuario", "false");
                db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String[] hijos = task.getResult().get("hijos").toString()
                                .replace("[", "")
                                .replace("]", "")
                                .split(", ");
                        boolean auxToast = true;
                        for (String s : hijos) {
                            Log.wtf("APL email", email);
                            Log.wtf("APL s hijos", s);
                            Log.wtf("APL s equals email", s.equals(email) ? "true" : "false");
                            if (s.equals(email)) {
                                auxToast = false;
                                goto_AddSmth(v);
                                break;
                            }
                        }
                        if (auxToast)
                            Toast.makeText(this, "No tienes permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.wtf("APL TASK FAIL ADDSTH", task.getException());
                        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goto_AddSmth(View v) {
        Activity clase = new Activity();
        boolean existeClase = true;
        int itemId = nav.getSelectedItemId();
        if (itemId == R.id.navCuentas) {
            clase = new AddCuentaActivity();
        } else if (itemId == R.id.navCategorias) {
            clase = new AddCategoriaActivity();
        } else if (itemId == R.id.navTransacciones) {
            clase = new AddTransaccionActivity();
        } else if (itemId == R.id.navEstadisticas) {
            Toast.makeText(this, "no se puede añadir estadisticas aun", Toast.LENGTH_SHORT).show();
            existeClase = false;
        }
        if (existeClase) {
            Intent i = new Intent(this, clase.getClass());
            i.putExtra("email", email);
            i.putExtra("usuario", usuario);
            i.putExtra("password", password );
            startActivity(i);
            finish();
        }
    }

    public void goto_Settings(View v) {
        Intent i = new Intent(this, AjustesActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }

}