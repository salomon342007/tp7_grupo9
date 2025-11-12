package ar.edu.unju.escmi.tp7.collections;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unju.escmi.tp7.dominio.Factura;

public class CollectionFactura {

	public static List<Factura> facturas = new ArrayList<Factura>();

	public static void agregarFactura(Factura factura) {

		try {
			facturas.add(factura);
		} catch (Exception e) {
			System.out.println("\nNO SE PUEDE GUARDAR LA FACTURA");
		}

	}

	public static Factura buscarFactura(long nroFactura) {
		Factura facturaEncontrada = null;

		try {
			if (facturas != null) {
				for (Factura fac : facturas) {
					if (fac.getNroFactura() == nroFactura) {
						facturaEncontrada = fac;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

		return facturaEncontrada;
	}

	/**
	 * Busca todas las facturas asociadas a un cliente por su DNI.
	 * (Nota: esta implementación es parcial; idealmente las facturas tendrían
	 * una referencia directa al cliente.)
	 */
	public static List<Factura> buscarFacturasPorDni(long dni) {
		List<Factura> encontradas = new ArrayList<>();
		try {
			if (facturas != null && !facturas.isEmpty()) {
				// Por ahora devolvemos la lista vacía si no hay referencias directas
				// El usuario debería implementar una relación Cliente <-> Factura
			}
		} catch (Exception e) {
			return null;
		}
		return encontradas;
	}
}
