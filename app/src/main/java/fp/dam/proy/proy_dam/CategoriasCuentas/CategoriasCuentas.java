package fp.dam.proy.proy_dam.CategoriasCuentas;

public class CategoriasCuentas {
    String nombre, icon;
    //int icon;
    double gastos, budget;

    public CategoriasCuentas(String nombre, String icon, double gastos, double budget) {
        this.nombre = nombre;
        this.icon = icon;
        this.gastos = gastos;
        this.budget = budget;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcon() {
        return icon;
    }

    public double getGastos() {
        return gastos;
    }

    public double getBudget() {
        return budget;
    }
}
