package controladores;

import java.util.ArrayList;
import java.util.List; 
import java.util.Iterator;
import java.util.Optional;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion; 
import servicios.PersistenciaDatos;
import validacion.Formato;

public class CuentaControlador {
    private List<Cuenta> cuentas; // Lista de cuentas

    public CuentaControlador(ClienteControlador clienteControlador) {
        this.cuentas = new ArrayList<>(); // Inicializa como ArrayList
        cargarCuentas(clienteControlador);
    }

    public void cargarCuentas(ClienteControlador clienteControlador) {
        List<Cuenta> cuentasDesdeArchivo = PersistenciaDatos.cargarCuentas(clienteControlador);
        System.out.println("Cargando cuentas desde archivo, total: " + cuentasDesdeArchivo.size());
        for (Cuenta cuenta : cuentasDesdeArchivo) {
            System.out.println("Procesando cuenta: " + cuenta.getCodigo());
            if (!cuentas.contains(cuenta)) {
                cuentas.add(cuenta);
                if (cuenta.getMiCliente() != null && !cuenta.getMiCliente().getMisCuentas().contains(cuenta)) {
                    cuenta.getMiCliente().getMisCuentas().add(cuenta);
                    System.out.println("Cuenta añadida: " + cuenta.getCodigo() + " al cliente: " + cuenta.getMiCliente().getIdentificacion());
                }
            } else {
                System.out.println("La cuenta " + cuenta.getCodigo() + " ya está asociada al cliente: " + cuenta.getMiCliente().getIdentificacion());
            }
        }
    }


    public List<Cuenta> getCuentas() {
        return cuentas;
    }
    
    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getCodigo().equals(numeroCuenta)) {
                return Optional.of(cuenta); // Retorna la cuenta si se encuentra
            }
        }
        return Optional.empty(); // Retorna un Optional vacío si no se encuentra
    }
    

    
    // Método para autenticar cuenta con número de cuenta y PIN
    public boolean autenticarCuenta(String numeroCuenta, String pin) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getCodigo().equals(numeroCuenta)) { // Asumiendo que getCodigo() devuelve el número de cuenta
                return cuenta.validarIngreso(pin);
            }
        }
        return false; // Cuenta no encontrada
    }
    

    // Método para crear una cuenta y añadirla al sistema
    public String crearCuenta(double saldoInicial, String pin, Cliente cliente) {
        try {
            if (!Formato.validarFormatoPin(pin)) {
                throw new IllegalArgumentException("El PIN no cumple con el formato requerido.");
            }
            String numeroCuenta = "cta-" + cuentas.size(); // Generar número de cuenta único
            Cuenta nuevaCuenta = new Cuenta(saldoInicial, numeroCuenta, pin, cliente);
            nuevaCuenta.setPinEncriptado(nuevaCuenta.encriptarPin(pin));
            cliente.getMisCuentas().add(nuevaCuenta);
            cuentas.add(nuevaCuenta); // Añadir la nueva cuenta a la lista
            PersistenciaDatos.guardarCuentas(cuentas); // Guarda todas las cuentas
            return numeroCuenta; // Devolver el número de cuenta
        } catch (Exception e) {
            System.err.println("Error al crear la cuenta: " + e.getMessage());
            return null; // Devolver null en caso de error
        }
    }
    
    
    // Método para cambiar el PIN de una cuenta
    public boolean cambiarPinCuenta(Cuenta cuenta, String nuevoPin) {
        try {
            cuenta.setPin(nuevoPin);
            PersistenciaDatos.guardarCuentas(cuentas); // Guardar la lista actualizada
            return true; // Indicar que el cambio fue exitoso
        } catch (Exception e) {
            System.err.println("Error al cambiar el PIN: " + e.getMessage());
            return false; // Indicar que hubo un error
        }
    }
    
    

    // Método para eliminar una cuenta
    public boolean eliminarCuenta(String numeroCuenta, String pin) {
        Iterator<Cuenta> iterator = cuentas.iterator();
        while (iterator.hasNext()) {
            Cuenta cuenta = iterator.next();
            System.out.println("Revisando cuenta: " + cuenta.getCodigo() + ", PIN almacenado: " + cuenta.getPinEncriptado());
            if (cuenta.getCodigo().equals(numeroCuenta)) {
                System.out.println("PIN ingresado: " + pin);
                if (cuenta.validarIngreso(pin)) {
                    iterator.remove();
                    PersistenciaDatos.guardarCuentas(cuentas);
                    return true;
                } else {
                    System.out.println("PIN incorrecto para la cuenta: " + cuenta.getCodigo());
                }
            }
        }
        return false;
    }

    public boolean cambiarPIN(Cuenta cuenta, String nuevoPin) {
        try {
            String pinEncriptado = cuenta.encriptarPin(nuevoPin); // Encriptar el nuevo PIN
            cuenta.setPin(pinEncriptado); // Asumiendo que tienes un método para establecer el PIN en la cuenta
            PersistenciaDatos.guardarCuentas(cuentas); // Guardar la lista actualizada
            return true; // Indicar que el cambio fue exitoso
        } catch (Exception e) {
            System.err.println("Error al cambiar el PIN: " + e.getMessage());
            return false; // Indicar que hubo un error
        }
    }

    // Método para obtener códigos y PINs de todas las cuentas
    public List<String[]> obtenerCodigosYPins() {
        List<String[]> codigosYPins = new ArrayList<>();
        
        for (Cuenta cuenta : cuentas) {
            String[] codigoYPin = new String[2];
            codigoYPin[0] = cuenta.getCodigo(); // Obtén el código de la cuenta
            codigoYPin[1] = cuenta.getPin();    // Obtén el PIN (si es que está accesible)
            codigosYPins.add(codigoYPin);
        }
        
        return codigosYPins; // Retorna la lista de códigos y PINs
    }
    
}