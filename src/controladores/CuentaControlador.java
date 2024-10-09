package controladores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.MensajeSMS;
import servicios.PersistenciaDatos;
import servicios.TipoDeCambioBCCR;
import validacion.Formato;

public class CuentaControlador {
    private List<Cuenta> cuentas; // Lista de cuentas
    private MensajeSMS mensajeSMS = new MensajeSMS(); // Para enviar el código de verificación
    private String codigoVerificacion; // Para almacenar el código generado

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
                    System.out.println("Cuenta añadida: " + cuenta.getCodigo() + " al cliente: "
                            + cuenta.getMiCliente().getIdentificacion());
                }
            } else {
                System.out.println("La cuenta " + cuenta.getCodigo() + " ya está asociada al cliente: "
                        + cuenta.getMiCliente().getIdentificacion());
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
            // System.out.println("Revisando cuenta: " + cuenta.getCodigo() + ", PIN
            // almacenado: " + cuenta.getPinEncriptado());
            if (cuenta.getCodigo().equals(numeroCuenta)) {
                System.out.println("PIN ingresado: " + pin);
                if (cuenta.validarIngreso(pin)) {
                    cuenta.eliminarCuenta();
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
            codigoYPin[1] = cuenta.getPin(); // Obtén el PIN (si es que está accesible)
            codigosYPins.add(codigoYPin);
        }

        return codigosYPins; // Retorna la lista de códigos y PINs
    }

    public double consultarSaldo(String numeroCuenta, String pin) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta); // Verificar si la cuenta existe
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            if (cuenta.validarIngreso(pin)) { // Verificar si el PIN es correcto
                return cuenta.getSaldo(); // Retornar el saldo si el PIN es correcto
            } else {
                System.out.println("PIN incorrecto.");
                return -1; // Retornar -1 si el PIN es incorrecto
            }
        } else {
            System.out.println("Cuenta no encontrada.");
            return -1; // Retornar -1 si la cuenta no existe
        }
    }

    public String obtenerNombreCliente(String numeroCuenta) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            Cliente cliente = cuenta.getMiCliente();
            if (cliente != null) {
                return cliente.getNombre(); // Asume que Cliente tiene un método getNombre()
            } else {
                System.out.println("Cliente no encontrado para la cuenta: " + numeroCuenta);
                return "Cliente desconocido";
            }
        } else {
            System.out.println("Cuenta no encontrada: " + numeroCuenta);
            return "Cuenta no encontrada";
        }
    }

    // Método para verificar si la cuenta existe
    public boolean verificarCuenta(String numeroCuenta) {
        return obtenerCuentaPorNumero(numeroCuenta).isPresent();
    }

    // Método para enviar el código de verificación
    public String enviarCodigoVerificacion(String numeroCuenta) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            String codigo = mensajeSMS.generarPalabraVerificacion(); // Genera el código
            boolean enviado = mensajeSMS.enviarMensajeVerificacion(cuenta.getMiCliente().getNumTelefono(), codigo); // Enviar
                                                                                                                    // SMS
            if (enviado) {
                this.codigoVerificacion = codigo; // Guarda el código generado para validación posterior
                return codigo;
            }
        }
        return null; // Retorna null si no se pudo enviar el código
    }

    // Método para validar el código de verificación
    public boolean validarCodigoVerificacion(String numeroCuenta, String codigoIngresado) {
        return codigoVerificacion != null && codigoVerificacion.equals(codigoIngresado);
    }

    // Método para verificar si la cuenta tiene suficientes fondos
    public boolean verificarFondos(String numeroCuenta, double monto) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            return cuenta.getSaldo() >= monto; // Verifica si hay saldo suficiente
        }
        return false; // Retorna false si la cuenta no existe o no tiene fondos suficientes
    }

    // Método para realizar el retiro
    public boolean realizarRetiro(String numeroCuenta, double montoRetiro, boolean esDolar) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            double saldoActual = cuenta.getSaldo();
            double comision = 0;

            // Si se realiza en dólares, aplicar conversión
            if (esDolar) {
                double tipoCambioVenta = TipoDeCambioBCCR.obtenerTipoCambioVenta(); // Obtener tipo de cambio
                montoRetiro = montoRetiro * tipoCambioVenta; // Convertir a colones
            }

            // Calcular si se aplica comisión
            if (cuenta.getTransacciones().size() > 5) {
                comision = montoRetiro * 0.02; // Aplicar 2% de comisión si hay más de 5 transacciones
            }

            double montoTotal = montoRetiro + comision;

            if (saldoActual >= montoTotal) {
                cuenta.setSaldo(saldoActual - montoTotal); // Descontar el saldo

                // Registrar la transacción de retiro
                Transaccion nuevaTransaccion = new Transaccion("Retiro", montoRetiro, numeroCuenta);
                nuevaTransaccion.setMontoComision(comision);
                cuenta.getTransacciones().add(nuevaTransaccion);

                // Guardar la cuenta actualizada
                PersistenciaDatos.guardarCuentas(cuentas);
                PersistenciaDatos.guardarTransacciones(cuenta.getTransacciones());

                return true; // Retiro exitoso
            } else {
                System.out.println("Fondos insuficientes.");
                return false; // Fondos insuficientes
            }
        }
        return false; // Cuenta no encontrada
    }

    // Verificar que ambas cuentas sean del mismo dueño
    public boolean verificarCuentaMismoDueno(String numeroCuentaOrigen, String numeroCuentaDestino) {
        Optional<Cuenta> cuentaOrigenOpt = obtenerCuentaPorNumero(numeroCuentaOrigen);
        Optional<Cuenta> cuentaDestinoOpt = obtenerCuentaPorNumero(numeroCuentaDestino);

        if (cuentaOrigenOpt.isPresent() && cuentaDestinoOpt.isPresent()) {
            Cuenta cuentaOrigen = cuentaOrigenOpt.get();
            Cuenta cuentaDestino = cuentaDestinoOpt.get();
            return cuentaOrigen.getMiCliente().getIdentificacion() == cuentaDestino.getMiCliente().getIdentificacion();
        }
        return false;
    }

    // Realizar la transferencia entre dos cuentas
    public boolean realizarTransferencia(String numeroCuentaOrigen, String pin, String codigoVerificacion,
            String numeroCuentaDestino, double montoTransferencia) {
        // Obtener la cuenta de origen
        Optional<Cuenta> cuentaOrigenOpt = obtenerCuentaPorNumero(numeroCuentaOrigen);
        Optional<Cuenta> cuentaDestinoOpt = obtenerCuentaPorNumero(numeroCuentaDestino);

        // Validar que ambas cuentas existan
        if (cuentaOrigenOpt.isPresent() && cuentaDestinoOpt.isPresent()) {
            Cuenta cuentaOrigen = cuentaOrigenOpt.get();
            Cuenta cuentaDestino = cuentaDestinoOpt.get();

            // Verificar si el PIN es correcto
            if (!autenticarCuenta(numeroCuentaOrigen, pin)) {
                System.out.println("Error: El PIN es incorrecto.");
                return false;
            }

            // Verificar si las cuentas pertenecen al mismo dueño
            // Verificar si las cuentas pertenecen al mismo dueño
            if (cuentaOrigen.getMiCliente().getIdentificacion() != cuentaDestino.getMiCliente().getIdentificacion()) {
                System.out.println("Error: Las cuentas no pertenecen al mismo dueño.");
                return false;
            }

            // Verificar si hay saldo suficiente en la cuenta origen
            if (!verificarFondos(numeroCuentaOrigen, montoTransferencia)) {
                System.out.println("Error: Fondos insuficientes para realizar la transferencia.");
                return false;
            }

            // Realizar la transacción de cuenta a cuenta
            cuentaOrigen.realizarTransaccionEntreCuentas(montoTransferencia, cuentaDestino, codigoVerificacion, null);

            System.out.println("Transferencia realizada con éxito.");
            return true;
        } else {
            System.out.println("Error: Una o ambas cuentas no están registradas.");
            return false;
        }
    }

}