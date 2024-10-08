package servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TipoDeCambioBCCR {
    private static final String BCCR_URL = "https://gee.bccr.fi.cr/indicadoreseconomicos/Cuadros/frmVerCatCuadro.aspx?idioma=1&CodCuadro=400";
    
    private static String tipoCambioCompra;
    private static String tipoCambioVenta;

    public static void obtenerTipoCambioHoy() {
        try {
            Document document = Jsoup.connect(BCCR_URL).get();
            LocalDate fechaHoy = LocalDate.now();
            String fechaHoyStr = fechaHoy.format(DateTimeFormatter.ofPattern("d MMM yyyy")).replace(".", "").toLowerCase();
            Elements celdas = document.select("td.celda400");

            int indexFecha = -1;

            for (int i = 0; i < celdas.size(); i++) {
                String textoCelda = celdas.get(i).text().toLowerCase();
                if (textoCelda.equals(fechaHoyStr)) {
                    indexFecha = i;
                    break;
                }
            }

            if (indexFecha != -1) {
                int indexCompra = indexFecha + indexFecha + 1;
                if (indexCompra < celdas.size()) {
                    tipoCambioCompra = celdas.get(indexCompra).text();
                }
                int indexVenta = indexFecha + indexFecha + indexFecha + 2;
                if (indexVenta < celdas.size()) {
                    tipoCambioVenta = celdas.get(indexVenta).text();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al conectarse a la página: " + e.getMessage());
        }
    }

    public static double obtenerTipoCambioCompra() {
        if (tipoCambioCompra != null) {
            try {
                // Reemplazar la coma con un punto para asegurar que el formato sea compatible
                String tipoCambioFormateado = tipoCambioCompra.replace(",", ".");
                return Double.parseDouble(tipoCambioFormateado);
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir el tipo de cambio de compra a número: " + e.getMessage());
            }
        } else {
            System.err.println("El tipo de cambio de compra es nulo.");
        }
        return 0; // O lanza una excepción si prefieres manejar el error de otra forma
    }

    public static double obtenerTipoCambioVenta() {
        if (tipoCambioVenta != null) {
            try {
                // Reemplazar la coma con un punto para asegurar que el formato sea compatible
                String tipoCambioFormateado = tipoCambioVenta.replace(",", ".");
                return Double.parseDouble(tipoCambioFormateado);
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir el tipo de cambio de venta a número: " + e.getMessage());
            }
        }
        return 0; // O lanza una excepción si prefieres manejar el error de otra forma
    }
}
