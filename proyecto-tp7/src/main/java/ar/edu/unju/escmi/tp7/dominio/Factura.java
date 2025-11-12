package ar.edu.unju.escmi.tp7.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {

    private static int contador = 0;
    private LocalDate fecha;
    private long nroFactura;
    private Cliente cliente;
    private List<Detalle> detalles = new ArrayList<Detalle>();

    public Factura() {
        contador++;
        this.nroFactura = contador;

    }

    public Factura(LocalDate fecha, long nroFactura, Cliente cliente, List<Detalle> detalles) {
        this.fecha = fecha;
        this.nroFactura = nroFactura;
        this.cliente = cliente;
        this.detalles = detalles;
        calcularTotal();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public long getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(long nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public double calcularTotal() {
        double total = 0;
        for (Detalle detalle : detalles) {
            total += detalle.getImporte();
        }
        return total;
    }

    /**
     * Calcula el total válido para Ahora30: sólo si la factura cumple las
     * condiciones
     * (productos permitidos y de fabricación nacional) devuelve el total, sino 0.
     */
    public double calcularTotalAhora30() {
        if (!esFacturaAhora30())
            return 0.0;
        return calcularTotal();
    }

    public boolean esFacturaAhora30() {
        if (detalles == null || detalles.isEmpty())
            return false;
        String[] permitidos = new String[] { "televisor", "aire", "heladera", "lavarropa", "lavarropas", "celular" };
        for (Detalle d : detalles) {
            Producto p = d.getProducto();
            if (p == null)
                return false;
            String desc = p.getDescripcion() == null ? "" : p.getDescripcion().toLowerCase();
            boolean esTipo = false;
            for (String key : permitidos) {
                if (desc.contains(key)) {
                    esTipo = true;
                    break;
                }
            }
            if (!esTipo)
                return false;
            if (p.getOrigenFabricacion() == null || !p.getOrigenFabricacion().equalsIgnoreCase("Argentina"))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\n\n******************** Factura ********************"
                + "\nFecha: " + fecha + " N° de Factura: " + nroFactura
                + "\nCliente: " + cliente.getNombre()
                + "\n************ Detalles de la Factura *************"
                + "\n" + detalles.toString().replaceAll("\\[|\\]", "").replaceAll(", ", "") + "\n";
    }
}
