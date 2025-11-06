package ar.edu.unju.escmi.tp7.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp7.dominio.Detalle;
import ar.edu.unju.escmi.tp7.dominio.Factura;
import ar.edu.unju.escmi.tp7.dominio.Producto;
import ar.edu.unju.escmi.tp7.dominio.TarjetaCredito;

class CreditoTest {

	public static final int MONTO_1 = 500000;
	public static final int MONTO_2 = 200000;
	public static final int MONTO_3 = 1000;

	@Test
	void testMontoCreditoValido() {
		double montoObtenido = crearFactura().calcularTotal();
		double montoPermitido = 1500000;
		assertTrue(montoObtenido <= montoPermitido, "El monto total no debería superar al monto permitido");
	}

	@Test
	void testSumaImportesIgualTotalFactura() {
		Factura factura = crearFactura();
		double suma = 0.0;
		for (Detalle d : factura.getDetalles()) {
			suma += d.getImporte();
		}
		assertEquals(suma, factura.calcularTotal(), 0.0001,
				"La suma de importes debe ser igual al total de la factura");
	}

	@Test
	void testMontoNoSupereTopeYLimiteTarjeta() {
		// Crear factura con monto dentro del tope general (1.5M)
		Producto p = new Producto(8888, "Heladera Test", 500000, "Argentina");
		Detalle d1 = new Detalle(2, 0.0, p); // 2 * 500000 = 1_000_000
		List<Detalle> detalles = new ArrayList<>();
		detalles.add(d1);
		Factura factura = new Factura();
		factura.setDetalles(detalles);

		// Tarjeta con limite justo superior al total
		TarjetaCredito tarjeta = new TarjetaCredito(22223333L, java.time.LocalDate.now().plusYears(2), null, 1500000.0);

		// Crear Credito sin generar cuotas para validar condiciones iniciales
		ar.edu.unju.escmi.tp7.dominio.Credito credito = new ar.edu.unju.escmi.tp7.dominio.Credito();
		credito.setTarjetaCredito(tarjeta);
		credito.setFactura(factura);

		// Debe ser valido inicialmente y luego al generar cuotas la tarjeta debe
		// disminuir su limite
		assertTrue(credito.esCreditoValido(), "El credito debe ser valido dentro del tope y con limite suficiente");

		// Aprobar crédito (genera cuotas y decrementa el límite)
		boolean aprobo = credito.aprobarCredito();
		assertTrue(aprobo, "La aprobacion del credito debe ser exitosa");
		double expectedRemaining = 1500000.0 - factura.calcularTotal();
		assertEquals(expectedRemaining, tarjeta.getLimiteCompra(), 0.001,
				"El limite de la tarjeta debe disminuir en el monto de la compra");
	}

	private Factura crearFactura() {
		Factura factura = new Factura();
		factura.setDetalles(crearListaDetalles());
		return factura;
	}

	private List<Detalle> crearListaDetalles() {
		List<Detalle> listaDetalles = new ArrayList<Detalle>();
		Detalle detalle1 = new Detalle();
		detalle1.setImporte(MONTO_1);
		Detalle detalle2 = new Detalle();
		detalle2.setImporte(MONTO_2);
		Detalle detalle3 = new Detalle();
		detalle3.setImporte(MONTO_3);
		listaDetalles.add(detalle1);
		listaDetalles.add(detalle2);
		listaDetalles.add(detalle3);
		return listaDetalles;

	}

}
