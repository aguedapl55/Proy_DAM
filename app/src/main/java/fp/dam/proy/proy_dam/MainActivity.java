package fp.dam.proy.proy_dam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import fp.dam.proy.proy_dam.Categorias.AddCategoriaActivity;
import fp.dam.proy.proy_dam.Categorias.CategoriasFrag;
import fp.dam.proy.proy_dam.Cuentas.AddCuentaActivity;
import fp.dam.proy.proy_dam.Cuentas.CuentasFrag;
import fp.dam.proy.proy_dam.Transacciones.AddTransaccionActivity;
import fp.dam.proy.proy_dam.Transacciones.TransaccionesFrag;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    NavigationBarView nav;
    CuentasFrag cuentasFrag = new CuentasFrag();
    CategoriasFrag catFrag = new CategoriasFrag();
    TransaccionesFrag transFrag = new TransaccionesFrag();
    EstadisticasFrag estFrag = new EstadisticasFrag();
    //AjustesFrag ajFrag = new AjustesFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = getIntent().getExtras().getString("email");
        //db.collection(email);

        nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            return selectFragment(itemId);
        });
        nav.setOnItemReselectedListener(item -> {
            int itemId = item.getItemId();
            selectFragment(itemId);
        });

        ImageButton ajustesBtn = findViewById(R.id.addBut);
        ajustesBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AjustesScreen.class);
            startActivity(i);
        });

        TextView placeholder = findViewById(R.id.textView);
        DocumentReference docRef = db.collection("users").document(email);
        Source source = Source.DEFAULT;
        docRef.get(source).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Log.d("sos", "Cached document data: " + document.getData());
                placeholder.setText(email);
            } else {
                Log.d("sos", "Cached get failed: ", task.getException());
            }
        });
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
        //} else if (itemId == R.id.navAjustes) {
            //loadFragment(ajFrag);
        } else return false;
        return true;
    }

    public void loadFragment(Fragment fragment) {
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); //cambia al fragmento seleccionado
        transaction.setReorderingAllowed(true); // optimiza cambios estados fragmentos -> animaciones mejores -> TODO mirar animaciones
        transaction.addToBackStack(null); //atras tecla movil -> vuelve al fragmento anterior
        transaction.commit();
    }

    public void goto_AddTransaccion(View v) {
        Intent i = new Intent(this, AddTransaccionActivity.class);
        i.putExtra("email", email);
        startActivity(i);
    }

    public void goto_AddSmth(View v) {
        Activity clase = null;
        int itemId = nav.getSelectedItemId();
        if (itemId == R.id.navCuentas) {
            clase = new AddCuentaActivity();
        } else if (itemId == R.id.navCategorias) {
            clase = new AddCategoriaActivity();
        } else if (itemId == R.id.navTransacciones) {
            clase = new AddTransaccionActivity();
        } else if (itemId == R.id.navEstadisticas) {
            Toast.makeText(this, "no se puede añadir estadisticas aun", Toast.LENGTH_SHORT);
        }
        Intent i = new Intent(this, clase.getClass());
        startActivity(i);

    }

    public void goto_Settings(View v) {
        Intent i = new Intent(this, AjustesScreen.class);
        i.putExtra("email", email);
        startActivity(i);
    }

}