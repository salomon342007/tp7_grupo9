package ar.edu.unju.escmi.tp7.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp7.dominio.Credito;
import ar.edu.unju.escmi.tp7.dominio.Factura;
import ar.edu.unju.escmi.tp7.dominio.Producto;
import ar.edu.unju.escmi.tp7.dominio.Detalle;
import ar.edu.unju.escmi.tp7.dominio.TarjetaCredito;

class CuotaTest {

    @Test
    void testListaCuotasNoEsNullYTiene30() {
        Producto p = new Producto(9999, "Televisor Test", 100000, "Argentina");
        Detalle d = new Detalle(1, 0.0, p); // calcularImporte en constructor
        List<Detalle> detalles = new ArrayList<>();
        detalles.add(d);

        Factura factura = new Factura();
        factura.setDetalles(detalles);

        TarjetaCredito tarjeta = new TarjetaCredito(11112222L, LocalDate.now().plusYears(1), null, 2000000.0);

        Credito credito = new Credito();
        credito.setTarjetaCredito(tarjeta);
        credito.setFactura(factura);

        boolean aprobada = credito.aprobarCredito();
        assertTrue(aprobada, "El credito debe aprobarse correctamente");
        assertNotNull(credito.getCuotas(), "La lista de cuotas no debe ser null");
        assertEquals(30, credito.getCuotas().size(), "La lista de cuotas debe tener 30 elementos");
        assertTrue(credito.getCuotas().size() <= 30, "La cantidad de cuotas no debe superar las 30");
    }
}
