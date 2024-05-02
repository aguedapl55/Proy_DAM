package fp.dam.proy.proy_dam.Categorias;

public class Categorias {
    String nombre;
    int icon;
    double dinero;

    public Categorias(String nombre, int icon, double gastos) {
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
