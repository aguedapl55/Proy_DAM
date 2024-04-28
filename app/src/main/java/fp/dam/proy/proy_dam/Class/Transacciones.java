package fp.dam.proy.proy_dam.Class;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Transacciones {
    Double dinero;
    String producto, lugar;
    Timestamp fecha;
    //Date fecha;

    public Transacciones(Double dinero, String producto, String lugar, Timestamp fecha) {
        this.dinero = dinero;
        this.producto = producto;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Double getDinero() {
        return dinero;
    }

    public String getProducto() {
        return producto;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(fecha.toDate());
    }
}
