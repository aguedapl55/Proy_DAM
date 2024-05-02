package fp.dam.proy.proy_dam.Cuentas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import fp.dam.proy.proy_dam.R;

public class CuentasFrag extends Fragment {

    private String email;
    private FirebaseFirestore db;
    private RecyclerView rv;
    private List<Cuentas> Cuentas;

    public CuentasFrag() {}

    public static CuentasFrag newInstance(String email) {
        CuentasFrag fragment = new CuentasFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cuentas = new ArrayList<>();
        //email = getArguments().getString("email");
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transacciones, container, false);
        /*
        rv = rootView.findViewById();
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rellenarRV(rv);
        rv.setAdapter(new CuentasAdapter(Cuentas));
         */
        return rootView;
    }

    private void rellenarRV(@NonNull RecyclerView rv) {

    }
}