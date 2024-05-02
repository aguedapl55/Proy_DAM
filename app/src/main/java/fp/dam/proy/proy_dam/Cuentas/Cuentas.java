package fp.dam.proy.proy_dam.Cuentas;

public class Cuentas {
    String nombre;
    int icon;
    double dinero;

    public Cuentas(String nombre, int icon, double gastos) {
        this.nombre = nombre;
        this.icon = icon;
        this.dinero = gastos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIcon() {
        return icon;
    }

    public double getDinero() {
        return dinero;
    }
}
