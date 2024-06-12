package fp.dam.proy.proy_dam.Transacciones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fp.dam.proy.proy_dam.R;

public class TransaccionesFrag extends Fragment {
    private String email, usuario;
    private FirebaseFirestore db;
    private RecyclerView rv;
    TextView taskSize;
    private List<Transacciones> transacciones;

    public TransaccionesFrag() {}

    public static TransaccionesFrag newInstance(String email, String usuario) {
        TransaccionesFrag fragment = new TransaccionesFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transacciones = new ArrayList<>();
        try {
            email = getArguments().getString("email");
            usuario = getArguments().getString("usuario");
        } catch (NullPointerException e) {}
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transacciones, container, false);
        try {
            rv = rootView.findViewById(R.id.transRecyclerView);
            rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
            checkRellenar();
            TransaccionesAdapter adapter = new TransaccionesAdapter(transacciones);
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);
        } catch (NullPointerException e) {}
        taskSize = rootView.findViewById(R.id.taskSize);
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
                        boolean valor = mapa.get("transacciones");
                        Log.wtf("APL mapa.get(transacciones)", "" + mapa.get("transacciones") + "; valor = " + valor);
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
        transacciones = new ArrayList<>();
        db.collection("users").document(email.toString()).collection("transacciones").orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.wtf("APL TAMAÑO TASK", "" + task.getResult().size());
                            if (task.getResult().size() == 0)
                                taskSize.setText("Aún no has añadido ninguna transacción");
                            else
                                taskSize.setText("");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.contains("dinero")) {
                                    Transacciones trans = new Transacciones(
                                            document.getDouble("dinero"),
                                            document.getTimestamp("fecha"),
                                            document.getString("categoria"),
                                            document.getString("cuenta"),
                                            document.getString("comentario"));
                                    transacciones.add(trans);
                                    Log.wtf("APL AÑADIDO", document.getId() + " => " + document.getData());
                                } else
                                    Log.wtf("APL SALTADO", document.getId());
                            }
                            rv.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        //Timestamp now = new Timestamp(new Date(124, 0, 27)); //meses empiezan en 0, años empiezan en 1900
    }

}