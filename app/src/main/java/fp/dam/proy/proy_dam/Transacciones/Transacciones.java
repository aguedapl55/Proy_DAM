package fp.dam.proy.proy_dam.Transacciones;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Transacciones {
    Double dinero;
    String producto, lugar, categoria, cuenta;
    Timestamp fecha;

    public Transacciones(Double dinero, String producto, String lugar, Timestamp fecha) {
        this.dinero = dinero;
        this.producto = producto;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Transacciones(Double dinero, String producto, String lugar, Timestamp fecha, String categoria, String cuenta) {
        this.dinero = dinero;
        this.producto = producto;
        this.lugar = lugar;
        this.fecha = fecha;
        this.categoria = categoria;
        this.cuenta = cuenta;
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

    public String getFechaFormated() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(fecha.toDate());
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCuenta() {
        return cuenta;
    }
}
