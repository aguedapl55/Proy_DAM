package fp.dam.proy.proy_dam.Cuentas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import fp.dam.proy.proy_dam.CategoriasCuentas.CatCtaAdapter;
import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.R;

public class CuentasFrag extends Fragment {

    private String email, usuario;
    private FirebaseFirestore db;
    private RecyclerView rv;
    private List<CategoriasCuentas> cuentas;

    public CuentasFrag() {}

    public static CuentasFrag newInstance(String email, String usuario) {
        CuentasFrag fragment = new CuentasFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cuentas = new ArrayList<>();
        email = getArguments().getString("email");
        usuario = getArguments().getString("usuario");
        db = FirebaseFirestore.getInstance();
        try {
            rv.getAdapter().notifyDataSetChanged();
        } catch (NullPointerException e) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cuentas, container, false);
        rv = rootView.findViewById(R.id.ctaRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rellenarRV(rv);
        CatCtaAdapter adapter = new CatCtaAdapter(cuentas);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        return rootView;
    }

    private void rellenarRV(@NonNull RecyclerView rv) {
        cuentas = new ArrayList<>();
        db.collection("users").document(email).collection("cuentas").orderBy("nombre", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.wtf("APL TAMAÑO TASK", "" + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains("nombre") && document.contains("icon")) {
                                CategoriasCuentas cta = new CategoriasCuentas(
                                        document.getString("nombre"),
                                        document.getString("icon"),
                                        document.getDouble("gastos"),
                                        document.getDouble("budget"));
                                cuentas.add(cta);
                                Log.wtf("APL AÑADIDO", document.getId() + " => " + document.getData());
                            } else
                                Log.wtf("APL SALTADO", document.getId());
                        }
                        rv.getAdapter().notifyDataSetChanged();
                    } else {
                        Log.wtf("APL TASK FALLADO", "Error getting documents.", task.getException());
                    }
                });
    }
}