package fp.dam.proy.proy_dam;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fp.dam.proy.proy_dam.AdapterClass.Transacciones;
import fp.dam.proy.proy_dam.AdapterClass.TransaccionesAdapter;

public class TransaccionesFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TransaccionesFrag() {}

    public static TransaccionesFrag newInstance(String param1, String param2) {
        TransaccionesFrag fragment = new TransaccionesFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        RecyclerView rv = (RecyclerView) this.getActivity().findViewById(R.id.transRecyclerView);
        //crearJSON(rv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.fragment_transacciones, container, false);
        View rootView = inflater.inflate(R.layout.fragment_transacciones, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.transRecyclerView);
        //crearJSON(rv);

        return rootView;
    }

    String string =
            /*
            "[{" +
            "\"_id\": {" +
                "\"$oid\": \"65e1a39b5ad73a04228a28cc\"" +
            "}," +
            "\"FECHA\": \"14/9/23\"," +
            "\"LUGAR\": \"Alimerka\"," +
            "\"PRODUCTO\": \"Conos Ali 80g\"," +
            "\"PRECIO\": \"0.60 €\"" +
        "}," +
        "{" +
            "\"_id\": {" +
                "\"$oid\": \"65e1a39b5ad73a04228a28cd\"" +
            "}," +
            "\"FECHA\": \"15/9/23\"," +
            "\"LUGAR\": \"Alimerka\"," +
            "\"PRODUCTO\": \"Crois Fr. Manteq\"," +
            "\"PRECIO\": \"0.65 €\"" +
        "}," +
        "{" +
            "\"_id\": {" +
            "\"$oid\": \"65e1a39b5ad73a04228a28ce\"" +
            "}," +

            "\"FECHA\": \"16/9/23\"," +
            "\"LUGAR\": \"Nova Hogar\"," +
            "\"PRODUCTO\": \"Lana\"," +
            "\"PRECIO\": \"1.95 €\""
            +"}"
             */
            //+ "}]"
            ""
            ;
    public JSONObject jsonObj;
    public JSONArray jsonArr;

    private void crearJSON(@NonNull RecyclerView rv) {
        try {
            //jsonObj = new JSONObject(string);
            jsonObj = new JSONObject();
            jsonObj.put("_id", "id_transaccion");
            jsonObj.put("FECHA", "16/9/23");
            jsonObj.put("LUGAR", "Nova Hogar");
            jsonObj.put("PRODUCTO", "Lana");
            jsonObj.put("PRECIO", "1.95€");

            jsonArr = new JSONArray();
            jsonArr.put(jsonObj);

            //RecyclerView rv = this.getView().findViewById(R.id.transRecyclerView);
            // rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
            List<Transacciones> listJSON = new ArrayList<>();
            listJSON.add(new Transacciones(jsonObj));
            /*
            for (int i = 0; i<jsonArr.length(); i++) {
                JSONObject aux = (JSONObject) jsonArr.get(i);
                listJSON.add(new Transacciones(aux));
            }
             */
            rv.setAdapter(new TransaccionesAdapter(listJSON));

        } catch (JSONException e) {
            Log.wtf("pito", "crearJSON: " + jsonObj.names());
            //throw new RuntimeException(e);

        }
    }

    //FIXME: por alguna razon solo funciona si esta en main activity???
    public void goto_AddTransaccion(View v) {
        Intent i = new Intent(getActivity(), AddTransaccionActivity.class);
        startActivity(i);
    }
}