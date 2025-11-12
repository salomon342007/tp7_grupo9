package ar.edu.unju.escmi.tp7.collections;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unju.escmi.tp7.dominio.Producto;
import ar.edu.unju.escmi.tp7.dominio.Stock;

public class CollectionStock {

	public static List<Stock> stocks = new ArrayList<Stock>();

	public static void precargarStocks() {
		if (stocks.isEmpty()) {
			stocks = new ArrayList<Stock>();
			List<Producto> productos = CollectionProducto.listarProductos();
			stocks.add(new Stock(12, productos.get(0)));
			stocks.add(new Stock(22, productos.get(1)));
			stocks.add(new Stock(13, productos.get(2)));
			stocks.add(new Stock(101, productos.get(3)));
			stocks.add(new Stock(87, productos.get(4)));
			stocks.add(new Stock(45, productos.get(5)));
			stocks.add(new Stock(16, productos.get(6)));
			stocks.add(new Stock(8, productos.get(7)));
			stocks.add(new Stock(5, productos.get(8)));
			stocks.add(new Stock(21, productos.get(9)));
			stocks.add(new Stock(17, productos.get(10)));
			stocks.add(new Stock(11, productos.get(11)));
			stocks.add(new Stock(8, productos.get(12)));
			stocks.add(new Stock(14, productos.get(13)));
			stocks.add(new Stock(4, productos.get(14)));
			stocks.add(new Stock(15, productos.get(15)));
			stocks.add(new Stock(28, productos.get(16)));
			stocks.add(new Stock(47, productos.get(17)));
			stocks.add(new Stock(33, productos.get(18)));
			stocks.add(new Stock(13, productos.get(19)));
		}
	}

	public static void agregarStock(Stock stock) {

		try {
			if (stocks.isEmpty()) {
				stocks.add(stock);
			} else {
				Producto controlProducto = stock.getProducto();
				boolean band = true;
				int i = 0;

				for (Stock sto : stocks) {
					if (band) {
						if (controlProducto == sto.getProducto()) {
							stocks.set(i, stock);
							band = false;
						}
					}
					i++;
				}
				if (band) {
					stocks.add(stock);
				}
			}
		} catch (Exception e) {
			System.out.println("\nNO SE PUEDE GUARDAR EL STOCK");
		}

	}

	public static void reducirStock(Stock stock, int cantidad) {
		int i = stocks.indexOf(stock);
		if (i >= 0) {
			if (stock.getCantidad() - cantidad >= 0) {
				stock.setCantidad(stock.getCantidad() - cantidad);
				stocks.set(i, stock);
			}
		} else {
			System.out.println("\nERROR");
		}
	}

	public static Stock buscarStock(Producto producto) {
		Stock stockTotal = null;

		try {
			if (stocks != null) {
				for (Stock sto : stocks) {
					if (sto.getProducto() == producto) {
						stockTotal = sto;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

		return stockTotal;
	}
}
