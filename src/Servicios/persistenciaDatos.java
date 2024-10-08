package servicios;

import modelos.Cuenta;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class persistenciaDatos {

    public static List<Cuenta> cargarCuentasDesdeXML(String rutaArchivo) {
        List<Cuenta> cuentas = new ArrayList<>();

        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                System.out.println("Archivo XML no encontrado, iniciando con base de datos vacía.");
                return cuentas;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);

            doc.getDocumentElement().normalize();
            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Node nodoCuenta = listaCuentas.item(i);

                if (nodoCuenta.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoCuenta = (Element) nodoCuenta;

                    String codigo = elementoCuenta.getElementsByTagName("codigo").item(0).getTextContent();
                    Date fechaCreacion = Date.valueOf(elementoCuenta.getElementsByTagName("fechaCreacion").item(0).getTextContent());
                    int saldo = Integer.parseInt(elementoCuenta.getElementsByTagName("saldo").item(0).getTextContent());
                    String pin = elementoCuenta.getElementsByTagName("pin").item(0).getTextContent();
                    String estatus = elementoCuenta.getElementsByTagName("estatus").item(0).getTextContent();

                    Cuenta cuenta = new Cuenta(codigo, fechaCreacion, saldo, pin, estatus);
                    cuentas.add(cuenta);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cuentas;
    }

    public static void guardarCuentasEnXML(List<Cuenta> cuentas, String rutaArchivo) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("cuentas");
            doc.appendChild(rootElement);

            for (Cuenta cuenta : cuentas) {
                Element cuentaElement = doc.createElement("cuenta");

                Element codigo = doc.createElement("codigo");
                codigo.appendChild(doc.createTextNode(cuenta.getCodigo()));
                cuentaElement.appendChild(codigo);

                Element fechaCreacion = doc.createElement("fechaCreacion");
                fechaCreacion.appendChild(doc.createTextNode(cuenta.getFechaCreacion().toString()));
                cuentaElement.appendChild(fechaCreacion);

                Element saldo = doc.createElement("saldo");
                saldo.appendChild(doc.createTextNode(Integer.toString(cuenta.getSaldo())));
                cuentaElement.appendChild(saldo);

                Element pin = doc.createElement("pin");
                pin.appendChild(doc.createTextNode(cuenta.getPin()));
                cuentaElement.appendChild(pin);

                Element estatus = doc.createElement("estatus");
                estatus.appendChild(doc.createTextNode(cuenta.getEstatus()));
                cuentaElement.appendChild(estatus);

                rootElement.appendChild(cuentaElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaArchivo));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Crear un cliente
        Cliente cliente1 = new Cliente("Fernanda", "12d5689", "3234567890", "fernanda@example.com");
        
        // Crear una cuenta asociada a ese cliente
        Cuenta cuenta1 = new Cuenta("00112223344", cliente1, "1274", 5000.00);
     
        // Crear instancia del controlador
        BancoControlador bancoControlador = new BancoControlador();
    
        // Crear una lista de cuentas y agregar la cuenta
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta1);
    
        // Escribir la cuenta en el archivo XML
        bancoControlador.escribirCuentasXML(cuentas);
    
        // Mostrar la información en consola
        System.out.println("Número de cuenta: " + cuenta1.getNumeroCuenta());
        System.out.println("Dueño: " + cuenta1.getDueño().getNombreCompleto());
        System.out.println("Saldo: " + cuenta1.getSaldo());
    
        // Leer y mostrar las cuentas almacenadas en el archivo XML
        List<Cuenta> cuentasLeidas = bancoControlador.leerCuentasXML();
        for (Cuenta c : cuentasLeidas) {
            System.out.println("Número de cuenta: " + c.getNumeroCuenta());
            System.out.println("Saldo: " + c.getSaldo());
            System.out.println("Nombre del dueño: " + c.getDueño().getNombreCompleto());
            System.out.println("-----------------------------------");
        }
    }

}
