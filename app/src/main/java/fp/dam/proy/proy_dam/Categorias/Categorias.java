package fp.dam.proy.proy_dam.Categorias;

public class Categorias {
    String nombre, icon;
    //int icon;
    double dinero, budget;

    public Categorias(String nombre, String icon, double gastos, double budget) {
        this.nombre = nombre;
        this.icon = icon;
        this.dinero = gastos;
        this.budget = budget;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcon() {
        return icon;
    }

    public double getDinero() {
        return dinero;
    }

    public double getBudget() {
        return budget;
    }
}
