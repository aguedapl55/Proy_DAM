package fp.dam.proy.proy_dam.Transacciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import fp.dam.proy.proy_dam.R;

public class TransaccionesFrag extends Fragment {
    private String email;
    private FirebaseFirestore db;
    private RecyclerView rv;
    private List<Transacciones> transacciones;

    public TransaccionesFrag() {}

    public static TransaccionesFrag newInstance(String email) {
        TransaccionesFrag fragment = new TransaccionesFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transacciones = new ArrayList<>();
        email = getArguments().getString("email");
        db = FirebaseFirestore.getInstance();
        try {
            rv.getAdapter().notifyDataSetChanged();
        } catch (NullPointerException e) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transacciones, container, false);
        rv = rootView.findViewById(R.id.transRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rellenarRV(rv);
        TransaccionesAdapter adapter = new TransaccionesAdapter(transacciones);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        return rootView;
    }

    private void rellenarRV(@NonNull RecyclerView rv) {
        transacciones = new ArrayList<>();
        db.collection("users").document(email.toString()).collection("transacciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.wtf("TAMAÑO TASK", "" + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.contains("dinero")) {
                                    Double dinero = document.getDouble("dinero");
                                    String categoria = document.getString("categoria");
                                    String cuenta = document.getString("cuenta");
                                    Timestamp fecha = document.getTimestamp("fecha");
                                    String comentario = document.getString("comentario");
                                    Transacciones trans = new Transacciones(dinero, fecha, categoria, cuenta, comentario); //dinero, fecha, categoria, cuenta
                                    /*
                                    Transacciones trans = new Transacciones(
                                            document.getDouble("dinero"),
                                            document.getString("producto"),
                                            document.getString("cuenta"),
                                            document.getTimestamp("fecha"));
                                     */
                                    transacciones.add(trans);
                                    Log.wtf("AÑADIDO", document.getId() + " => " + document.getData());
                                } else
                                    Log.wtf("SALTADO", document.getId());
                            }
                            rv.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        Timestamp now = new Timestamp(new Date(124, 0, 27)); //meses empiezan en 0, años empiezan en 1900
        //Transacciones prueba = new Transacciones(0.0, "NADA", "aqui", now);
        //transacciones.add(prueba);
    }

    public void goto_AddTransaccion(View view) {
        Intent i = new Intent(getActivity(), AddTransaccionActivity.class);
        startActivity(i);
    }

}