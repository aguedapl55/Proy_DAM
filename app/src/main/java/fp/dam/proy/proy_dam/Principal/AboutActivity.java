package fp.dam.proy.proy_dam.Principal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import fp.dam.proy.proy_dam.R;

public class AboutActivity extends AppCompatActivity {

    String usuario, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        try {
            usuario = getIntent().getExtras().getString("usuario");
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
        } catch (NullPointerException e) {
            Toast.makeText(this, "Ha ocurrido un error al pasar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void goto_link(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/aguedapl55/Proy_DAM/"));
        startActivity(intent);
    }

    public void back(View v) {
        Intent i = new Intent(this, AjustesActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }
}