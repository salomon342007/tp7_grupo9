package ar.edu.unju.escmi.tp7.dominio;

public class Stock {
    private int cantidad;
    private Producto producto;

    public Stock() {
    }

    public Stock(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public boolean validarStockDisponible(int cantidadSolicitada) {
        return this.cantidad >= cantidadSolicitada;
    }

    public void actualizarStock(int cantidadReducir) {
        if (cantidadReducir <= this.cantidad) {
            this.cantidad -= cantidadReducir;
        }
    }
}
