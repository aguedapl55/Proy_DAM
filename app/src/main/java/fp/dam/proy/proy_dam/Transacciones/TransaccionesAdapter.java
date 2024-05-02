package fp.dam.proy.proy_dam.Transacciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fp.dam.proy.proy_dam.R;

public class TransaccionesAdapter extends RecyclerView.Adapter<TransaccionesAdapter.ViewHolder> {

    List<Transacciones> modeList;

    public TransaccionesAdapter(List<Transacciones> modelist) {
        this.modeList = modelist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(fp.dam.proy.proy_dam.R.layout.rvrow_transaccion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(modeList.get(position));
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fecha, lugar, producto, precio;

        public ViewHolder(View v) {
            super (v);
            fecha = v.findViewById(R.id.transRV_TxtFecha);
            lugar = v.findViewById(R.id.transRV_TxtLugar);
            producto = v.findViewById(R.id.transRV_TxtProd);
            precio = v.findViewById(R.id.transRV_TxtDinero);
        }

        void bind(Transacciones transacciones) {
            precio.setText(transacciones.getDinero().toString());
            producto.setText(transacciones.getProducto());
            fecha.setText(transacciones.getFechaFormated());
            lugar.setText(transacciones.getLugar());
        }
    }
}
