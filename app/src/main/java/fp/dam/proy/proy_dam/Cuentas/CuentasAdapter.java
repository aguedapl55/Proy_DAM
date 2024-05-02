package fp.dam.proy.proy_dam.Cuentas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fp.dam.proy.proy_dam.R;

public class CuentasAdapter extends RecyclerView.Adapter<CuentasAdapter.ViewHolder> {

    List<Cuentas> modeList;

    public CuentasAdapter(List<Cuentas> modeList) {
        this.modeList = modeList;
    }

    @NonNull
    @Override
    public CuentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvrow_cuentas, parent, false);
        return new CuentasAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentasAdapter.ViewHolder holder, int position) {
        holder.bind(modeList.get(position));
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombre, dinero;
        private final ImageView icon;

        public ViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.ctasRV_nombre);
            icon = v.findViewById(R.id.ctasRV_icon);
            dinero = v.findViewById(R.id.ctasRV_dinero);
        }

        void bind(Cuentas Cuentas) {
            nombre.setText(Cuentas.getNombre());
            icon.setImageResource(Cuentas.getIcon());
            dinero.setText(String.valueOf(Cuentas.getDinero()));
        }
    }
}
