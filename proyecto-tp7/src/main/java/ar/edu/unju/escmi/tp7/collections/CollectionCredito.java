package ar.edu.unju.escmi.tp7.collections;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unju.escmi.tp7.dominio.Credito;

public class CollectionCredito {

	public static List<Credito> creditos = new ArrayList<Credito>();

	public static void agregarCredito(Credito credito) {

		try {
			creditos.add(credito);
		} catch (Exception e) {
			System.out.println("\nNO SE PUEDE GUARDAR EL CREDITO");
		}

	}

	public static List<Credito> buscarCreditoPorDni(long dni) {
		List<Credito> encontrados = new ArrayList<>();
		try {
			if (creditos != null) {
				for (Credito c : creditos) {
					if (c.getTarjetaCredito() != null && c.getTarjetaCredito().getCliente() != null
							&& c.getTarjetaCredito().getCliente().getDni() == dni) {
						encontrados.add(c);
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return encontrados;
	}
}
