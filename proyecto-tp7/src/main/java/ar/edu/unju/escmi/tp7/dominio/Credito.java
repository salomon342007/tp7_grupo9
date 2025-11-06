package ar.edu.unju.escmi.tp7.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Credito {

	private TarjetaCredito tarjetaCredito;
	private Factura factura;
	private List<Cuota> cuotas = new ArrayList<Cuota>();

	public Credito() {
	}

	public Credito(TarjetaCredito tarjetaCredito, Factura factura, List<Cuota> cuotas) {
		this.tarjetaCredito = tarjetaCredito;
		this.factura = factura;
		this.cuotas = cuotas;
		// Constructor ya no genera cuotas ni cambia estado de la tarjeta.
		// La aprobación/ejecución del crédito debe hacerse explícitamente con
		// aprobarCredito().
	}

	public Credito(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	public TarjetaCredito getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(TarjetaCredito tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public List<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	public void generarCuotas() {
		// Genera 30 cuotas con el monto igual a total/30.
		double total = this.factura.calcularTotal();
		this.cuotas = new ArrayList<>();
		double montoCuota = total / 30.0;
		int nroCuota = 0;
		LocalDate fechaGeneracion = LocalDate.now();
		LocalDate fechaVencimiento = fechaGeneracion;

		for (int i = 0; i < 30; i++) {
			nroCuota++;
			Cuota cuota = new Cuota();
			cuota.setMonto(montoCuota);
			cuota.setNroCuota(nroCuota);
			cuota.setFechaGeneracion(fechaGeneracion);
			fechaVencimiento = fechaVencimiento.plusMonths(1);
			cuota.setFechaVencimiento(fechaVencimiento);
			cuotas.add(cuota);
		}

	}

	/**
	 * Aprueba y aplica el crédito: comprueba validez, genera las cuotas y disminuye
	 * el límite
	 * de la tarjeta. Retorna true si la aprobación y aplicación fue exitosa.
	 */
	public boolean aprobarCredito() {
		if (!esCreditoValido())
			return false;
		// Generar cuotas (no muta la tarjeta)
		generarCuotas();
		// Disminuir límite de la tarjeta por el total de la factura
		double total = this.factura.calcularTotal();
		if (this.tarjetaCredito != null) {
			this.tarjetaCredito.disminuirLimite(total);
		}
		return true;
	}

	/**
	 * Determina el tope permitido para la compra según tipos de productos en la
	 * factura.
	 * Si todos los productos son celulares, aplica tope de 800000, sino 1500000.
	 */
	private double determinarTopePorFactura() {
		if (this.factura == null || this.factura.getDetalles() == null || this.factura.getDetalles().isEmpty()) {
			return 1500000.0;
		}
		boolean todosCelulares = true;
		for (Detalle d : this.factura.getDetalles()) {
			Producto p = d.getProducto();
			if (p == null || p.getDescripcion() == null || !p.getDescripcion().toLowerCase().contains("celular")) {
				todosCelulares = false;
				break;
			}
		}
		return todosCelulares ? 800000.0 : 1500000.0;
	}

	/**
	 * Valida si el crédito cumple las reglas del programa Ahora 30:
	 * - La factura no es nula
	 * - El total no supera el tope (800k para celulares, sino 1.5M)
	 * - La tarjeta tiene límite suficiente
	 */
	public boolean esCreditoValido() {
		if (this.factura == null)
			return false;
		double total = this.factura.calcularTotal();
		double tope = determinarTopePorFactura();
		if (total > tope)
			return false;
		if (this.tarjetaCredito == null)
			return false;
		if (this.tarjetaCredito.getLimiteCompra() < total)
			return false;
		return true;
	}

	public void mostarCredito() {
		System.out.println("Tarjeta De Credito: " + tarjetaCredito + "\n" + factura + "\nCant. Cuotas:\n");
		for (Cuota cuota : cuotas) {
			System.out.println(cuota);
		}
	}
}
