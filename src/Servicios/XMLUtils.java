package servicios;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controladores.ClienteControlador;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class XMLUtils {

    // Método para escribir clientes a un archivo XML
    public static void escribirClientesAArchivoXML(List<Cliente> listaClientes, String rutaArchivo) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("clientes");
            doc.appendChild(rootElement);

            for (Cliente cliente : listaClientes) {
                Element clienteElement = doc.createElement("cliente");
                rootElement.appendChild(clienteElement);

                Element nombre = doc.createElement("nombre");
                nombre.appendChild(doc.createTextNode(cliente.getNombre()));
                clienteElement.appendChild(nombre);

                Element identificacion = doc.createElement("identificacion");
                identificacion.appendChild(doc.createTextNode(String.valueOf(cliente.getIdentificacion())));
                clienteElement.appendChild(identificacion);

                Element numTelefono = doc.createElement("numTelefono");
                numTelefono.appendChild(doc.createTextNode(String.valueOf(cliente.getNumTelefono())));
                clienteElement.appendChild(numTelefono);

                Element correoElectronico = doc.createElement("correoElectronico");
                correoElectronico.appendChild(doc.createTextNode(cliente.getCorreoElectronico()));
                clienteElement.appendChild(correoElectronico);
            }

            // Escribir el contenido en el archivo XML
            try (FileOutputStream fos = new FileOutputStream(new File(rutaArchivo))) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    // Método para leer clientes a un archivo XML
    public static List<Cliente> leerClientesDesdeArchivoXML(String rutaArchivo) {
        List<Cliente> listaClientes = new ArrayList<>();

        try {
            File inputFile = new File(rutaArchivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cliente");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nodo = nList.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoCliente = (Element) nodo;
                    String nombre = elementoCliente.getElementsByTagName("nombre").item(0).getTextContent();
                    int identificacion = Integer.parseInt(elementoCliente.getElementsByTagName("identificacion").item(0).getTextContent());
                    String numTelefono = elementoCliente.getElementsByTagName("numTelefono").item(0).getTextContent();
                    String correoElectronico = elementoCliente.getElementsByTagName("correoElectronico").item(0).getTextContent();

                    // Crear el objeto Cliente con los datos obtenidos del XML
                    Cliente cliente = new Cliente(nombre, identificacion, numTelefono, correoElectronico);
                    listaClientes.add(cliente);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaClientes;
    }


    // Método para escribir cuentas a un archivo XML
    // Método para escribir cuentas a un archivo XML
    public static void escribirCuentasAArchivoXML(List<Cuenta> listaCuentas, String rutaArchivo) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("cuentas");
            doc.appendChild(rootElement);

            for (Cuenta cuenta : listaCuentas) {
                Element cuentaElement = doc.createElement("cuenta");
                rootElement.appendChild(cuentaElement);

                Element numeroCuenta = doc.createElement("numeroCuenta");
                numeroCuenta.appendChild(doc.createTextNode(cuenta.getCodigo()));
                cuentaElement.appendChild(numeroCuenta);

                Element estatus = doc.createElement("estatus");
                estatus.appendChild(doc.createTextNode(cuenta.getEstatus()));
                cuentaElement.appendChild(estatus);

                Element saldo = doc.createElement("saldo");
                saldo.appendChild(doc.createTextNode(cuenta.getSaldoFormateado()));
                cuentaElement.appendChild(saldo);

                Element identificacion = doc.createElement("identificacion");
                identificacion.appendChild(doc.createTextNode(String.valueOf(cuenta.getMiCliente().getIdentificacion())));
                cuentaElement.appendChild(identificacion);
                
                // Agregar el PIN de la cuenta
                Element pin = doc.createElement("pin");
                pin.appendChild(doc.createTextNode(cuenta.getPin())); // Agregar el PIN
                cuentaElement.appendChild(pin);
            }

            // Escribir el contenido en el archivo XML
            try (FileOutputStream fos = new FileOutputStream(new File(rutaArchivo))) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }



    // Método para leer cuentas a un archivo XML
    // Método para leer cuentas desde un archivo XML
    public static List<Cuenta> leerCuentasDesdeArchivoXML(String rutaArchivo, ClienteControlador clienteControlador) {
        List<Cuenta> listaCuentas = new ArrayList<>();
    
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(rutaArchivo));
    
            NodeList listaNodosCuenta = doc.getElementsByTagName("cuenta");
    
            for (int i = 0; i < listaNodosCuenta.getLength(); i++) {
                Node nodoCuenta = listaNodosCuenta.item(i);
    
                if (nodoCuenta.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoCuenta = (Element) nodoCuenta;
    
                    String numeroCuenta = elementoCuenta.getElementsByTagName("numeroCuenta").item(0).getTextContent();
                    String estatus = elementoCuenta.getElementsByTagName("estatus").item(0).getTextContent();
                    String saldoFormateado = elementoCuenta.getElementsByTagName("saldo").item(0).getTextContent();
                    double saldo = Double.parseDouble(saldoFormateado);

                    String pin = elementoCuenta.getElementsByTagName("pin").item(0).getTextContent();
    
                    int identificacion = Integer.parseInt(elementoCuenta.getElementsByTagName("identificacion").item(0).getTextContent());
    
                    // Buscar el cliente en el ClienteControlador utilizando la identificación
                    Optional<Cliente> optionalCliente = clienteControlador.buscarClientePorIdentificacion(identificacion);
    
                    // Obtener el cliente del Optional, o null si no existe
                    Cliente cliente = optionalCliente.orElse(null);
    
                    if (cliente == null) {
                        System.err.println("Advertencia: Cliente con identificación " + identificacion + " no encontrado. La cuenta no se asociará a ningún cliente.");
                    }
    
                    // Crear el objeto Cuenta con la información leída del archivo XML
                    Cuenta cuenta = new Cuenta(saldo, numeroCuenta, pin, cliente);
                    listaCuentas.add(cuenta);
    
                    // Si el cliente existe, agregar la cuenta a la lista de cuentas del cliente
                    if (cliente != null) {
                        cliente.getMisCuentas().add(cuenta);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo XML: " + e.getMessage());
            e.printStackTrace();
        }
    
        return listaCuentas;
    }
    
    


    // Método para escribir transacciones a un archivo XML
    public static void escribirTransaccionesAArchivoXML(List<Transaccion> transacciones, String rutaArchivo) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("transacciones");
            doc.appendChild(rootElement);

            for (Transaccion transaccion : transacciones) {
                Element transaccionElement = doc.createElement("transaccion");

                Element fechaElement = doc.createElement("fecha");
                fechaElement.appendChild(doc.createTextNode(transaccion.getFecha().toString()));
                transaccionElement.appendChild(fechaElement);

                Element tipoElement = doc.createElement("tipo");
                tipoElement.appendChild(doc.createTextNode(transaccion.getTipo()));
                transaccionElement.appendChild(tipoElement);

                Element montoElement = doc.createElement("monto");
                montoElement.appendChild(doc.createTextNode(String.valueOf(transaccion.getMonto())));
                transaccionElement.appendChild(montoElement);

                Element comisionElement = doc.createElement("comision");
                comisionElement.appendChild(doc.createTextNode(String.valueOf(transaccion.isComision())));
                transaccionElement.appendChild(comisionElement);

                Element montoRetiroComisionElement = doc.createElement("montoRetiroComision");
                montoRetiroComisionElement.appendChild(doc.createTextNode(String.valueOf(transaccion.getMontoRetiroComision())));
                transaccionElement.appendChild(montoRetiroComisionElement);

                Element montoDepositoComisionElement = doc.createElement("montoDepositoComision");
                montoDepositoComisionElement.appendChild(doc.createTextNode(String.valueOf(transaccion.getMontoDepositoComision())));
                transaccionElement.appendChild(montoDepositoComisionElement);

                Element numeroCuentaElement = doc.createElement("numeroCuenta");
                numeroCuentaElement.appendChild(doc.createTextNode(transaccion.getCodigoCuenta()));
                transaccionElement.appendChild(numeroCuentaElement);

                rootElement.appendChild(transaccionElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaArchivo));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para leer transacciones a un archivo XML
    public static List<Transaccion> leerTransaccionesDesdeArchivoXML(String rutaArchivo) {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            File archivoXML = new File(rutaArchivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            NodeList transaccionNodes = doc.getElementsByTagName("transaccion");

            for (int i = 0; i < transaccionNodes.getLength(); i++) {
                Node transaccionNode = transaccionNodes.item(i);

                if (transaccionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transaccionElement = (Element) transaccionNode;

                    double monto = Double.parseDouble(transaccionElement.getElementsByTagName("monto").item(0).getTextContent());
                    String fecha = transaccionElement.getElementsByTagName("fecha").item(0).getTextContent();
                    String tipo = transaccionElement.getElementsByTagName("tipo").item(0).getTextContent();
                    boolean comision = Boolean.parseBoolean(transaccionElement.getElementsByTagName("comision").item(0).getTextContent());
                    double montoRetiroComision = Double.parseDouble(transaccionElement.getElementsByTagName("montoRetiroComision").item(0).getTextContent());
                    double montoDepositoComision = Double.parseDouble(transaccionElement.getElementsByTagName("montoDepositoComision").item(0).getTextContent());
                    String numeroCuenta = transaccionElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();

                    Transaccion transaccion = new Transaccion(tipo, monto, numeroCuenta);
                    transacciones.add(transaccion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transacciones;
    }
}