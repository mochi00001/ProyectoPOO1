package servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TipoDeCambioBCCR {

    // URL del Banco Central de Costa Rica
    private static final String BCCR_URL = "https://gee.bccr.fi.cr/indicadoreseconomicos/Cuadros/frmVerCatCuadro.aspx?idioma=1&CodCuadro=400";

    public static void main(String[] args) {
        double tipoCompra = obtenerTipoCambioCompra();
        double tipoVenta = obtenerTipoCambioVenta();

        if (tipoCompra != -1 && tipoVenta != -1) {
            System.out.println("Tipo de Cambio de Compra: " + tipoCompra);
            System.out.println("Tipo de Cambio de Venta: " + tipoVenta);
        } else {
            System.out.println("Hubo un error al obtener el tipo de cambio.");
        }
    }

    /**
     * Método para obtener el tipo de cambio de compra del día actual desde
     * la página del BCCR.
     * 
     * @return El tipo de cambio de compra como un double, o -1 en caso de error.
     */
    public static double obtenerTipoCambioCompra() {
        try {
            Document document = Jsoup.connect(BCCR_URL).get();
            Elements celdas = document.select("td.celda400");

            // Formatear la fecha actual
            LocalDate fechaHoy = LocalDate.now();
            String fechaHoyStr = fechaHoy.format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("es", "ES")))
                    .toLowerCase();

            int indexFecha = obtenerIndiceFecha(celdas, fechaHoyStr);
            int totalFechas = contarFechas(celdas);
            int indiceCompra = indexFecha + totalFechas;

            if (indiceCompra < celdas.size()) {
                String tipoCambioCompraStr = celdas.get(indiceCompra).text().replace(",", ".");
                return Double.parseDouble(tipoCambioCompraStr);
            } else {
                System.out.println("Error: No se encontró el índice adecuado para el tipo de cambio de compra.");
                return -1;
            }
        } catch (IOException e) {
            System.err.println("Error al conectarse a la página: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Método para obtener el tipo de cambio de venta del día actual desde
     * la página del BCCR.
     * 
     * @return El tipo de cambio de venta como un double, o -1 en caso de error.
     */
    public static double obtenerTipoCambioVenta() {
        try {
            Document document = Jsoup.connect(BCCR_URL).get();
            Elements celdas = document.select("td.celda400");

            // Formatear la fecha actual
            LocalDate fechaHoy = LocalDate.now();
            String fechaHoyStr = fechaHoy.format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("es", "ES")))
                    .toLowerCase();

            int indexFecha = obtenerIndiceFecha(celdas, fechaHoyStr);
            int totalFechas = contarFechas(celdas);
            int indiceVenta = indexFecha + (2 * totalFechas);

            if (indiceVenta < celdas.size()) {
                String tipoCambioVentaStr = celdas.get(indiceVenta).text().replace(",", ".");
                return Double.parseDouble(tipoCambioVentaStr);
            } else {
                System.out.println("Error: No se encontró el índice adecuado para el tipo de cambio de venta.");
                return -1;
            }
        } catch (IOException e) {
            System.err.println("Error al conectarse a la página: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Método para obtener el índice de la fecha actual en la tabla.
     * 
     * @param celdas      Las celdas de la tabla HTML.
     * @param fechaHoyStr La fecha actual como cadena de texto.
     * @return El índice de la fecha actual o -1 si no se encuentra.
     */
    private static int obtenerIndiceFecha(Elements celdas, String fechaHoyStr) {
        for (int i = 0; i < celdas.size(); i++) {
            String textoCelda = celdas.get(i).text().toLowerCase();
            if (textoCelda.equals(fechaHoyStr)) {
                return i;
            }
        }
        return -1; // No se encontró la fecha
    }

    /**
     * Método para contar cuántas fechas hay en la tabla.
     */
    private static int contarFechas(Elements celdas) {
        int contador = 0;
        Pattern patronFecha = Pattern.compile("^\\d{1,2} [a-z]{3} \\d{4}$"); // Regex para validar fechas con 1 o 2
                                                                             // dígitos para el día

        for (Element celda : celdas) {
            String texto = celda.text().toLowerCase();
            Matcher matcher = patronFecha.matcher(texto);
            if (matcher.matches()) {
                contador++;
            }
        }
        return contador;
    }
}
