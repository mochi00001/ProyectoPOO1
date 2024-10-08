package servicios;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.XMLUtils;
import java.util.List;
import java.io.File;
import java.util.ArrayList;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controladores.ClienteControlador;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PersistenciaDatos {
    private static final String RUTA_CUENTAS_XML = "src/data/cuentas.xml";
    private static final String RUTA_CLIENTES_XML = "src/data/clientes.xml"; // Ruta para clientes
    private static final String RUTA_TRANSACCIONES_XML = "src/data/transacciones.xml"; // Ruta para transacciones

    // Métodos para manejar cuentas
    // Método para guardar cuentas
    public static void guardarCuentas(List<Cuenta> cuentas) {
        try {
            // Crear el archivo si no existe
            File archivoCuentas = new File(RUTA_CUENTAS_XML);
            if (!archivoCuentas.getParentFile().exists()) {
                archivoCuentas.getParentFile().mkdirs();
            }

            // Usar el nuevo método para escribir cuentas a archivo XML
            XMLUtils.escribirCuentasAArchivoXML(cuentas, RUTA_CUENTAS_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Método para cargar cuentas
    public static List<Cuenta> cargarCuentas(ClienteControlador clienteControlador) {
        try {
            // Intentar leer las cuentas desde el archivo XML, proporcionando la ruta y el ClienteControlador
            List<Cuenta> cuentas = XMLUtils.leerCuentasDesdeArchivoXML(RUTA_CUENTAS_XML, clienteControlador);
            return cuentas != null ? cuentas : new ArrayList<>(); // Crear una nueva lista si el archivo está vacío o no existe
        } catch (Exception e) {
            System.err.println("Error al cargar las cuentas desde el archivo XML: " + e.getMessage());
            return new ArrayList<>(); // Devuelve una lista vacía en caso de error
        }
    }
    
    
    


    // Métodos para manejar clientes
    // Método para guardar clientes
    public static void guardarClientes(List<Cliente> clientes) {
        try {
            // Crear el archivo si no existe
            File archivoClientes = new File(RUTA_CLIENTES_XML);
            if (!archivoClientes.getParentFile().exists()) {
                archivoClientes.getParentFile().mkdirs();
            }

            // Usar el nuevo método para escribir clientes a archivo XML
            XMLUtils.escribirClientesAArchivoXML(clientes, RUTA_CLIENTES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Método para cargar clientes
    public static List<Cliente> cargarClientes() {
        try {
            // Intentar leer los clientes desde el archivo XML
            List<Cliente> clientes = XMLUtils.leerClientesDesdeArchivoXML(RUTA_CLIENTES_XML);
            if (clientes == null || clientes.isEmpty()) {
                System.out.println("No se encontraron clientes en el archivo XML.");
            } else {
                System.out.println("Clientes cargados correctamente: " + clientes.size() + " clientes encontrados.");
            }
            return clientes;
        } catch (Exception e) {
            System.err.println("Error al cargar los clientes desde el archivo XML: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve una lista vacía en caso de error
        }
    }
    

    // Métodos para manejar transacciones
    // Método para guardar transacciones
    public static void guardarTransacciones(List<Transaccion> transacciones) {
        try {
            // Crear el archivo si no existe
            File archivoTransacciones = new File(RUTA_TRANSACCIONES_XML);
            if (!archivoTransacciones.getParentFile().exists()) {
                archivoTransacciones.getParentFile().mkdirs();
            }
    
            // Usar el método utilitario para escribir transacciones en el archivo XML
            XMLUtils.escribirTransaccionesAArchivoXML(transacciones, RUTA_TRANSACCIONES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Transaccion> cargarTransacciones() {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            // Verificar si el archivo existe antes de intentar cargar
            File archivoTransacciones = new File(RUTA_TRANSACCIONES_XML);
            if (!archivoTransacciones.exists()) {
                return transacciones; // Retorna una lista vacía si no existe el archivo
            }
    
            // Usar el método utilitario para leer transacciones desde el archivo XML
            transacciones = XMLUtils.leerTransaccionesDesdeArchivoXML(RUTA_TRANSACCIONES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transacciones;
    }
}    
    





    
    