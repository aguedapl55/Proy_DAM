package fp.dam.proy.proy_dam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    CuentasFrag cuentasFrag = new CuentasFrag();
    CategoriasFrag catFrag = new CategoriasFrag();
    TransaccionesFrag transFrag = new TransaccionesFrag();
    EstadisticasFrag estFrag = new EstadisticasFrag();
    AjustesFrag ajFrag = new AjustesFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationBarView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            return selectFragment(itemId);
        });
        nav.setOnItemReselectedListener(item -> {
            int itemId = item.getItemId();
            selectFragment(itemId);
        });

        ImageButton ajustesBtn = findViewById(R.id.settingsBut);
        ajustesBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AjustesScreen.class);
            startActivity(i);
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
        } else if (itemId == R.id.navAjustes) {
            loadFragment(ajFrag);
        } else return false;
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }


}