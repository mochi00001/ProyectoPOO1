package servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TipoDeCambioBCCR {

    // URL del Banco Central de Costa Rica
    private static final String BCCR_URL = "https://gee.bccr.fi.cr/indicadoreseconomicos/Cuadros/frmVerCatCuadro.aspx?idioma=1&CodCuadro=400";

    /**
     * Método para obtener el tipo de cambio de compra y venta del día actual desde
     * la página del BCCR.
     */
    public static void obtenerTipoCambioHoy() {
        try {
            // Conectar y obtener el documento HTML de la URL
            Document document = Jsoup.connect(BCCR_URL).get();

            // Formatear la fecha actual en el formato adecuado (ej. "3 Oct 2024")
            LocalDate fechaHoy = LocalDate.now();
            String fechaHoyStr = fechaHoy.format(DateTimeFormatter.ofPattern("d MMM yyyy")).replace(".", "")
                    .toLowerCase();

            // Buscar todas las celdas con clase "celda400" (que contienen fechas, compra y
            // venta)
            Elements celdas = document.select("td.celda400");

            // Variables para realizar el seguimiento de los índices y valores
            int indexFecha = -1;
            String tipoCambioCompra = "";
            String tipoCambioVenta = "";

            // Recorrer todas las celdas para encontrar la fecha actual
            for (int i = 0; i < celdas.size(); i++) {
                String textoCelda = celdas.get(i).text().toLowerCase();

                if (textoCelda.equals(fechaHoyStr)) {
                    indexFecha = i;
                    break; // Se ha encontrado la fecha, guardamos el índice y terminamos la búsqueda
                }
            }

            // Si se encuentra la fecha, buscar los valores correspondientes de compra y
            // venta
            if (indexFecha != -1) {
                // La columna de compra está después de la fecha
                int indexCompra = indexFecha + indexFecha + 1;

                if (indexCompra < celdas.size()) {
                    tipoCambioCompra = celdas.get(indexCompra).text();
                }

                // La columna de venta está después del valor de compra
                int indexVenta = indexFecha + indexFecha + indexFecha + 2;

                if (indexVenta < celdas.size()) {
                    tipoCambioVenta = celdas.get(indexVenta).text();
                }

                // Imprimir los resultados
                if (!tipoCambioCompra.isEmpty() && !tipoCambioVenta.isEmpty()) {
                    System.out.println("Fecha: " + fechaHoyStr);
                    System.out.println("Tipo de Cambio de Compra: " + tipoCambioCompra);
                    System.out.println("Tipo de Cambio de Venta: " + tipoCambioVenta);
                } else {
                    System.out
                            .println("No se encontró el tipo de cambio completo para la fecha de hoy: " + fechaHoyStr);
                }
            } else {
                System.out.println("No se encontró el tipo de cambio para la fecha de hoy: " + fechaHoyStr);
            }

        } catch (IOException e) {
            System.err.println("Error al conectarse a la página: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error al obtener el tipo de cambio: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        obtenerTipoCambioHoy();
    }
}
