package ar.edu.unju.escmi.tp7.main;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import ar.edu.unju.escmi.tp7.collections.CollectionCliente;
import ar.edu.unju.escmi.tp7.collections.CollectionProducto;
import ar.edu.unju.escmi.tp7.collections.CollectionStock;
import ar.edu.unju.escmi.tp7.collections.CollectionTarjetaCredito;
import ar.edu.unju.escmi.tp7.collections.CollectionFactura;
import ar.edu.unju.escmi.tp7.collections.CollectionCredito;
import ar.edu.unju.escmi.tp7.dominio.Producto;
import ar.edu.unju.escmi.tp7.dominio.Stock;
import ar.edu.unju.escmi.tp7.dominio.Factura;
import ar.edu.unju.escmi.tp7.dominio.Credito;
import ar.edu.unju.escmi.tp7.dominio.Detalle;
import ar.edu.unju.escmi.tp7.dominio.Cliente;
import ar.edu.unju.escmi.tp7.dominio.TarjetaCredito;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        CollectionTarjetaCredito.precargarTarjetas();
        CollectionCliente.precargarClientes();
        CollectionProducto.precargarProductos();
        CollectionStock.precargarStocks();
        int opcion = 0;
        do {
            System.out.println("\n====== Menu Principal =====");
            System.out.println("1- Realizar una venta");
            System.out.println("2- Revisar compras realizadas por el cliente (debe ingresar el DNI del cliente)");
            System.out.println("3- Mostrar lista de los electrodomésticos");
            System.out.println("4- Consultar stock");
            System.out.println("5- Revisar creditos de un cliente (debe ingresar el DNI del cliente)");
            System.out.println("6- Salir");

            System.out.println("Ingrese su opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    realizarVenta();
                    break;
                case 2:
                    revisarCompras();
                    break;
                case 3:
                    mostrarProductos();
                    break;
                case 4:
                    consultarStock();
                    break;
                case 5:
                    revisarCreditos();
                    break;
                case 6:
                    System.out.println("Gracias por usar el sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }

        } while (opcion != 6);
        scanner.close();

    }

    private static void realizarVenta() {
        System.out.println("\n--- Realizar una Venta (Programa Ahora 30) ---");

        // 1. Seleccionar cliente por DNI
        System.out.print("Ingrese DNI del cliente: ");
        long dniCliente = scanner.nextLong();
        scanner.nextLine();

        Cliente cliente = CollectionCliente.buscarCliente(dniCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Cliente encontrado: " + cliente.getNombre());

        // 2. Seleccionar tarjeta de crédito del cliente
        System.out.print("Ingrese número de tarjeta del cliente: ");
        long nroTarjeta = scanner.nextLong();
        scanner.nextLine();

        TarjetaCredito tarjeta = CollectionTarjetaCredito.buscarTarjetaCredito(nroTarjeta);
        if (tarjeta == null || tarjeta.getCliente() == null || tarjeta.getCliente().getDni() != dniCliente) {
            System.out.println("Tarjeta no encontrada o no pertenece al cliente.");
            return;
        }
        System.out.println("Tarjeta encontrada. Límite disponible: $" + tarjeta.getLimiteCompra());

        // 3. Crear factura y agregar detalles
        Factura factura = new Factura();
        List<Detalle> detalles = new ArrayList<>();

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.print("\nIngrese código de producto (0 para terminar): ");
            long codigoProducto = scanner.nextLong();
            scanner.nextLine();

            if (codigoProducto == 0) {
                agregarMas = false;
                break;
            }

            Producto producto = CollectionProducto.buscarProducto(codigoProducto);
            if (producto == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            System.out.print("Ingrese cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            if (cantidad <= 0) {
                System.out.println("Cantidad inválida.");
                continue;
            }

            Stock stock = CollectionStock.buscarStock(producto);
            if (stock == null || stock.getCantidad() < cantidad) {
                System.out.println("Stock insuficiente. Disponible: " +
                        (stock != null ? stock.getCantidad() : 0));
                continue;
            }

            Detalle detalle = new Detalle(cantidad, 0.0, producto);
            detalles.add(detalle);
            System.out.println("Producto agregado: " + producto.getDescripcion() +
                    " (Cantidad: " + cantidad + ", Importe: $" + detalle.getImporte() + ")");
        }

        if (detalles.isEmpty()) {
            System.out.println("No se agregaron productos. Venta cancelada.");
            return;
        }

        factura.setDetalles(detalles);
        double totalFactura = factura.calcularTotal();
        System.out.println("\n--- Resumen de la Compra ---");
        System.out.println("Total de la compra: $" + totalFactura);

        // 4. Validar si es elegible para Ahora30
        if (!factura.esFacturaAhora30()) {
            System.out.println("Advertencia: Esta compra no es elegible para Ahora 30.");
            System.out.println("(Debe contener solo productos permitidos y ser origen Argentina)");
        }

        // 5. Crear crédito y validar
        Credito credito = new Credito();
        credito.setTarjetaCredito(tarjeta);
        credito.setFactura(factura);

        if (!credito.esCreditoValido()) {
            System.out.println("\nError: El crédito no cumple con los requisitos:");
            System.out.println("- Monto máximo permitido: $1.500.000");
            System.out.println("- Para celulares: $800.000 máximo");
            System.out.println("- Límite disponible en tarjeta: $" + tarjeta.getLimiteCompra());
            return;
        }

        // 6. Aprobar crédito
        if (credito.aprobarCredito()) {
            System.out.println("\n--- ¡Crédito Aprobado! ---");
            System.out.println("Número de factura: " + factura.getNroFactura());
            System.out.println("Total a pagar en 30 cuotas de: $" +
                    (totalFactura / 30.0));
            System.out.println("Nuevo límite disponible en tarjeta: $" + tarjeta.getLimiteCompra());

            // 7. Guardar factura y crédito
            CollectionFactura.agregarFactura(factura);
            CollectionCredito.agregarCredito(credito);

            // 8. Actualizar stock
            for (Detalle d : detalles) {
                Stock stock = CollectionStock.buscarStock(d.getProducto());
                if (stock != null) {
                    CollectionStock.reducirStock(stock, d.getCantidad());
                }
            }

            System.out.println("\nVenta registrada exitosamente.");
        } else {
            System.out.println("\nError: No se pudo aprobar el crédito.");
        }
    }

    private static void revisarCompras() {
        System.out.println("\n--- Revisar Compras del Cliente ---");
        System.out.print("Ingrese DNI del cliente: ");
        long dni = scanner.nextLong();
        scanner.nextLine();

        List<Factura> facturas = CollectionFactura.buscarFacturasPorDni(dni);
        if (facturas != null && !facturas.isEmpty()) {
            System.out.println("\nFacturas del cliente con DNI " + dni + ":");
            for (Factura f : facturas) {
                System.out.println(f);
            }
        } else {
            System.out.println("No se encontraron facturas para el cliente con DNI " + dni);
        }
    }

    private static void mostrarProductos() {
        System.out.println("\n--- Lista de Productos ---");
        List<Producto> productos = CollectionProducto.listarProductos();
        if (productos != null && !productos.isEmpty()) {
            for (Producto p : productos) {
                System.out.println(p);
            }
        } else {
            System.out.println("No hay productos disponibles.");
        }
    }

    private static void consultarStock() {
        System.out.println("\n--- Consultar Stock ---");
        System.out.print("Ingrese código de producto: ");
        long codigo = scanner.nextLong();
        scanner.nextLine();

        Producto producto = CollectionProducto.buscarProducto(codigo);
        if (producto != null) {
            Stock stock = CollectionStock.buscarStock(producto);
            if (stock != null) {
                System.out
                        .println("Stock para " + producto.getDescripcion() + ": " + stock.getCantidad() + " unidades");
            } else {
                System.out.println("No hay información de stock para este producto.");
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void revisarCreditos() {
        System.out.println("\n--- Revisar Créditos del Cliente ---");
        System.out.print("Ingrese DNI del cliente: ");
        long dni = scanner.nextLong();
        scanner.nextLine();

        List<Credito> creditos = CollectionCredito.buscarCreditoPorDni(dni);
        if (creditos != null && !creditos.isEmpty()) {
            System.out.println("\nCréditos del cliente con DNI " + dni + ":");
            for (Credito c : creditos) {
                System.out.println(c);
            }
        } else {
            System.out.println("No se encontraron créditos para el cliente con DNI " + dni);
        }
    }

}
