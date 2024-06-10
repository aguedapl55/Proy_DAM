package fp.dam.proy.proy_dam.Categorias;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fp.dam.proy.proy_dam.CategoriasCuentas.CatCtaAdapter;
import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.R;

public class CategoriasFrag extends Fragment {

    private String email, usuario;
    private FirebaseFirestore db;
    private RecyclerView rv;
    private List<CategoriasCuentas> categorias;

    public CategoriasFrag() {}

    public static CategoriasFrag newInstance(String email, String usuario) {
        CategoriasFrag fragment = new CategoriasFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorias = new ArrayList<>();
        email = getArguments().getString("email");
        usuario = getArguments().getString("usuario");
        db = FirebaseFirestore.getInstance();
        try {
            rv.getAdapter().notifyDataSetChanged();
        } catch (NullPointerException e) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categorias, container, false);
        rv = rootView.findViewById(R.id.catRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        checkRellenar();
        CatCtaAdapter adapter = new CatCtaAdapter(categorias);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        return rootView;
    }

    private void checkRellenar() {
        Log.wtf("APL usuario.equals(email)", "" + usuario.equals(email));
        if (usuario.equals(email)) {
            rellenarRV(rv);
        }
        else {
            db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                List<String> usersAccesibles = new ArrayList<>();
                DocumentSnapshot doc = task.getResult();
                usersAccesibles.addAll(Arrays.asList(
                        doc.get("hijos").toString()
                                .replace("[", "")
                                .replace("]", "")
                                .split(", ")));
                usersAccesibles.removeIf(d -> d.equals(""));
                Log.wtf("APL usersAccesibles.contains(email)", "" + usersAccesibles.contains(email));
                if (usersAccesibles.contains(email))
                    rellenarRV(rv);
                else {
                    db.collection("users").document(email).get().addOnCompleteListener(task2 -> {
                        Map<String, Boolean> mapa = (Map<String, Boolean>) task2.getResult().get("visibilidad");
                        boolean valor = mapa.get("categorias");
                        Log.wtf("APL mapa.get(categorias)", "" + mapa.get("categorias") + "; valor = " + valor);
                        if (valor)
                            rellenarRV(rv);
                        else
                            Toast.makeText(getContext(), "No tienes acceso a los datos", Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    private void rellenarRV(@NonNull RecyclerView rv) {
        categorias = new ArrayList<>();
        db.collection("users").document(email.toString()).collection("categorias").orderBy("nombre", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.wtf("APL TAMAÑO TASK", "" + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.contains("nombre") && document.contains("icon")) {
                                    CategoriasCuentas cat = new CategoriasCuentas(
                                            document.getString("nombre"),
                                            document.getString("icon"),
                                            document.getDouble("gastos"),
                                            document.getDouble("budget"));
                                    categorias.add(cat);
                                    Log.wtf("APL AÑADIDO", document.getId() + " => " + document.getData());
                                } else
                                    Log.wtf("APL SALTADO", document.getId());
                            }
                            rv.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.w("APL TASK FALLADO", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}