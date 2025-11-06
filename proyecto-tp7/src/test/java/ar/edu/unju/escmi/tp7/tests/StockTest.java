package ar.edu.unju.escmi.tp7.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp7.dominio.Producto;
import ar.edu.unju.escmi.tp7.dominio.Stock;
import ar.edu.unju.escmi.tp7.collections.CollectionStock;

class StockTest {

    @Test
    void testReducirStockDecrementaCantidad() {
        Producto p = new Producto(7777, "Lavarropas Test", 1000.0, "Argentina");
        Stock s = new Stock(10, p);
        // Agregar al repositorio de stocks
        CollectionStock.stocks.clear();
        CollectionStock.agregarStock(s);

        CollectionStock.reducirStock(s, 3);

        Stock encontrado = CollectionStock.buscarStock(p);
        assertNotNull(encontrado, "El stock debe existir en el repositorio");
        assertEquals(7, encontrado.getCantidad(), "La cantidad de stock debe haberse decrementado en 3");
    }
}
