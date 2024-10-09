package controladores;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.PersistenciaDatos;
import servicios.TipoDeCambioBCCR;
import java.time.format.DateTimeFormatter;
import controladores.CuentaControlador;



public class TransaccionesControlador {

    private CuentaControlador cuentaControlador;
    private ClienteControlador clienteControlador;
    private List<Transaccion> transacciones;

    public TransaccionesControlador(CuentaControlador cuentaControlador) {
        this.cuentaControlador = cuentaControlador;
        this.transacciones = new ArrayList<>(); // Inicializa la lista de transacciones
        cargarTransacciones(); // Carga las transacciones al crear el controlador
        this.clienteControlador = new ClienteControlador();
    }

    public void cargarTransacciones() {
        List<Transaccion> transaccionesDesdeArchivo = PersistenciaDatos.cargarTransacciones();
        System.out.println("Cargando transacciones desde archivo, total: " + transaccionesDesdeArchivo.size());
    
        for (Transaccion transaccion : transaccionesDesdeArchivo) {
            System.out.println("Procesando transacción: " + transaccion);
    
            Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(transaccion.getCodigoCuenta());
            if (cuentaOpt.isPresent()) {
                transacciones.add(transaccion); // Agrega la transacción a la lista
                System.out.println("Transacción añadida para la cuenta: " + transaccion.getCodigoCuenta());
                System.out.println(transaccion); // También mostrará la transacción en formato legible
            } else {
                System.out.println("La cuenta " + transaccion.getCodigoCuenta() + " no se encuentra registrada.");
            }
        }
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        List<Transaccion> transaccionesPorCuenta = new ArrayList<>();
        for (Transaccion transaccion : transacciones) { // Asegúrate de usar la variable 'transacciones' aquí
            if (transaccion.getCodigoCuenta().equals(numeroCuenta)) {
                transaccionesPorCuenta.add(transaccion);
            }
        }
        return transaccionesPorCuenta;
    }


    // Verificar si la cuenta existe
    public boolean verificarCuenta(String numeroCuenta) {
        return cuentaControlador.getCuentas().stream()
                                .anyMatch(c -> c.getCodigo().equals(numeroCuenta));
    }

    // Método para realizar un depósito en colones
    public boolean realizarDepositoColones(String numeroCuenta, double cantidad) {
        Cuenta cuenta = obtenerCuentaPorCodigo(numeroCuenta);
    
        if (cuenta != null) {
            cantidad = Math.round(cantidad * 100.0) / 100.0; // Redondear a dos decimales
    
            double comision = calcularComision(cuenta, cantidad); // Calcular la comisión
            double montoRealDeposito = cantidad - comision; // Monto real que se depositará en la cuenta
    
            // Verifica que el monto real a depositar sea positivo
            if (montoRealDeposito > 0) {
                cuenta.realizarDeposito(montoRealDeposito); // Depositar el monto real
            } else {
                System.out.println("El monto real a depositar es cero o negativo.");
                return false; // Manejo de caso negativo
            }
    
            // Crear la transacción
            Transaccion transaccion = new Transaccion("Depósito en Colones", cantidad, numeroCuenta);
            transaccion.setMontoComision(comision);
            transaccion.setFecha(LocalDate.now());
    
            // Guardar la transacción en el archivo XML
            List<Transaccion> transacciones = PersistenciaDatos.cargarTransacciones();
            transacciones.add(transaccion);
            PersistenciaDatos.guardarTransacciones(transacciones);

            PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());
    
            // Mensaje de confirmación
            System.out.println("Estimado usuario: " + cuenta.getMiCliente().getIdentificacion() + 
                            ", se han depositado correctamente " + cantidad + 
                            " colones. El monto real depositado es de " + 
                            montoRealDeposito + " colones. " +
                            "El monto cobrado por concepto de comisión fue de " + 
                            comision + " colones.");
            return true;
        } else {
            System.out.println("Cuenta no encontrada: " + numeroCuenta);
            return false;
        }
    }
    
    // Método para realizar un depósito en dólares, convierte a colones usando el tipo de cambio
    public boolean realizarDepositoDolares(String numeroCuenta, double montoDolares, double tipoCambioCompra) {
        System.out.println("Valor de tipoCambioCompra: " + tipoCambioCompra);
    
        if (montoDolares <= 0) {
            throw new IllegalArgumentException("El monto en dólares debe ser positivo.");
        }
    
        Cuenta cuenta = obtenerCuentaPorCodigo(numeroCuenta);
    
        if (cuenta != null) {
            double montoColones = montoDolares * tipoCambioCompra; // Usar tipoCambioCompra como argumento
            double comision = calcularComision(cuenta, montoColones);
            double montoRealDeposito = montoColones - comision;
    
            // Verifica que el monto real a depositar sea positivo
            if (montoRealDeposito > 0) {
                cuenta.realizarDeposito(montoRealDeposito); // Depositar el monto real
            } else {
                System.out.println("El monto real a depositar es cero o negativo.");
                return false; // Manejo de caso negativo
            }
    
            // Crear y registrar la transacción
            Transaccion transaccion = new Transaccion("Depósito en Dólares", montoColones, numeroCuenta);
            transaccion.setMontoComision(comision);
            transaccion.setFecha(LocalDate.now());
    
            // Guardar la transacción en el archivo XML
            List<Transaccion> transacciones = PersistenciaDatos.cargarTransacciones();
            transacciones.add(transaccion);
            PersistenciaDatos.guardarTransacciones(transacciones);

            PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());
    
            // Mensaje detallado al usuario
            String mensaje = String.format("Estimado usuario: %s, se han recibido correctamente %.0f dólares.\n" +
                            "[Según el BCCR, el tipo de cambio de compra del dólar de hoy %s es: %.2f]\n" +
                            "[El monto equivalente en colones es %.2f]\n" +
                            "[El monto real depositado a su cuenta %s es de %.2f colones]\n" +
                            "[El monto cobrado por concepto de comisión fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual].",
                    cuenta.getMiCliente().getIdentificacion(),
                    montoDolares,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    tipoCambioCompra,
                    montoColones,
                    numeroCuenta,
                    montoRealDeposito,
                    comision);
            System.out.println(mensaje);
    
            return true;
        } else {
            System.out.println("Cuenta no encontrada: " + numeroCuenta);
            return false;
        }
    }
    
    

    public double calcularComision(Cuenta cuenta, double montoTransaccion) {
        List<Transaccion> transacciones = cuenta.getTransacciones();
        
        // Contar transacciones para determinar si se cobrará comisión
        int totalTransacciones = transacciones.size();
        double comision = 0;

        // Si son más de 5 transacciones, aplica comisión del 2%
        if (totalTransacciones >= 5) {
            comision = 0.02 * montoTransaccion; // Aplicar 2% sobre el monto de la transacción
        }

        return comision; // Retorna la comisión calculada
    }


    // Método para realizar un retiro
    public boolean realizarRetiro(String numeroCuenta, String pin, double montoRetiro, boolean esDolar) {
        // Verificar que el monto de retiro sea válido
        if (montoRetiro <= 0) {
            System.out.println("Error: El monto de retiro debe ser mayor a cero.");
            return false;
        }
    
        // Obtener la cuenta por el número de cuenta
        Cuenta cuenta = obtenerCuentaPorCodigo(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: La cuenta no está registrada en el sistema.");
            return false;
        }
    
        // Validar el PIN ingresado
        if (!validarPinCuenta(numeroCuenta, pin)) {
            System.out.println("Error: Código de cuenta o PIN incorrecto.");
            return false;
        }
    
        // Convertir el monto a colones si es en dólares
        double montoRetiroColones = montoRetiro;
        if (esDolar) {
            double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();
            montoRetiroColones *= tipoCambioCompra; // Convertir a colones
        }
    
        // Calcular comisión para el retiro
        double comision = calcularComision(cuenta, montoRetiroColones);
    
        // Verificar si hay saldo suficiente
        if (montoRetiroColones + comision > cuenta.getSaldo()) {
            System.out.println("Error: Saldo insuficiente para realizar el retiro.");
            return false;
        }
    
        // Crear una transacción de retiro
        Transaccion transaccion = new Transaccion("Retiro", montoRetiroColones, numeroCuenta);
        double nuevoSaldo = cuenta.getSaldo() - montoRetiroColones - comision;
        cuenta.setSaldo(nuevoSaldo);
        
        // Establecer la fecha y la comisión de la transacción
        transaccion.setFecha(LocalDate.now());
        transaccion.setMontoComision(comision);
    
        // Guardar la transacción en el archivo XML
        List<Transaccion> transacciones = PersistenciaDatos.cargarTransacciones(); // Cargar transacciones existentes
        transacciones.add(transaccion); // Agregar la nueva transacción
        PersistenciaDatos.guardarTransacciones(transacciones); // Guardar la lista actualizada de transacciones

        PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());

        System.out.println("Retiro realizado con éxito.");
        return true;
    }
    
    // Método para validar el PIN ingresado
    private boolean validarPinCuenta(String numeroCuenta, String pinIngresado) {
        List<String[]> codigosYPins = cuentaControlador.obtenerCodigosYPins(); // Asume que tienes la instancia de CuentaControlador
        for (String[] codigoYPin : codigosYPins) {
            if (codigoYPin[0].equals(numeroCuenta) && codigoYPin[1].equals(pinIngresado)) {
                return true; // El código de cuenta y el PIN coinciden
            }
        }
        return false; // No coinciden
    }
    
    

    public double consultarSaldo(String numeroCuenta, String pin) {
        Cuenta cuenta = obtenerCuentaPorCodigo(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: La cuenta no está registrada en el sistema.");
            return -1; // Indicar que la cuenta no fue encontrada
        }
    
        // Mensaje de depuración
        System.out.println("PIN ingresado: " + pin);
        System.out.println("PIN almacenado: " + cuenta.getPin()); // Asegúrate de tener un método getPin()
    
        if (!cuenta.validarIngreso(pin)) {
            System.out.println("Error: PIN incorrecto.");
            return -1; // Indicar que el PIN es incorrecto
        }
    
        return cuenta.getSaldo(); // Retornar el saldo
    }
    
    


    public Cuenta obtenerCuentaPorCodigo(String numeroCuenta) {
        // Iterar sobre la lista de cuentas
        for (Cuenta cuenta : cuentaControlador.getCuentas()) {
            // Comparar el código de la cuenta con el número de cuenta proporcionado
            if (cuenta.getCodigo().equals(numeroCuenta)) {
                return cuenta; // Retorna la cuenta si se encuentra
            }
        }
        return null; // Retorna null si no se encuentra la cuenta
    }

    public void realizarTransferencia(String numeroCuentaOrigen, String pin, String palabraRecibida,
                                       String numeroCuentaDestino, double montoTransferencia) {
        // Validar que el monto de transferencia sea válido
        if (montoTransferencia <= 0) {
            System.out.println("Error: El monto de transferencia debe ser mayor a cero.");
            return;
        }

        // Obtener la cuenta de origen
        Cuenta cuentaOrigen = obtenerCuentaPorCodigo(numeroCuentaOrigen);
        if (cuentaOrigen == null) {
            System.out.println("Error: La cuenta de origen no está registrada en el sistema.");
            return;
        }

        // Validar el PIN ingresado
        if (!validarPinCuenta(numeroCuentaOrigen, pin)) {
            System.out.println("Error: Código de cuenta o PIN incorrecto.");
            return;
        }

        // Obtener la cuenta de destino
        Cuenta cuentaDestino = obtenerCuentaPorCodigo(numeroCuentaDestino);
        if (cuentaDestino == null) {
            System.out.println("Error: La cuenta destino no existe.");
            return;
        }

        // Realizar la transferencia utilizando el método de Cuenta
        cuentaOrigen.realizarTransaccionEntreCuentas(montoTransferencia, cuentaDestino, palabraRecibida, clienteControlador);

    }
    

}
