package fp.dam.proy.proy_dam.AdapterClass;

import com.google.firebase.Timestamp;

public class Transacciones {
    Integer dinero;
    String producto, lugar;
    Timestamp fecha;
    //Date fecha;

    public Transacciones(Integer dinero, String producto, String lugar, Timestamp fecha) {
        this.dinero = dinero;
        this.producto = producto;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Integer getDinero() {
        return dinero;
    }

    public String getProducto() {
        return producto;
    }

    public String getLugar() {
        return lugar;
    }

    public Timestamp getFecha() {
        return fecha;
    }
}
