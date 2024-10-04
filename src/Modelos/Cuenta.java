package modelos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cuenta {
    private String codigo;
    private static int cantidadCuentas;
    private Date fechaCreacion;
    private String estatus;
    private int saldo;
    private String pin;
    private double sumaComisiones;
    private double sumaComisionesRetiros;
    private double sumaComisionesDepositos;
    private int sumaRetiros;
    private int sumaDepositos;
    private String monedaOficial;
    private String monedaExtrangera;
    private String pinEncriptado;
    private int usosPin;
    private Cliente miCliente;
    private ArrayList<Transaccion> transacciones;

    public Cuenta(int saldo, String pin, Cliente cliente) {
        codigo = "cta-" + cantidadCuentas;
        cantidadCuentas++;
        this.saldo = saldo;
        this.pin = pin;
        miCliente = cliente;
        transacciones = new ArrayList<>();
        estatus = "Activa";
        usosPin = 0;
    }

    public void agregarTransaccion(String tipo, int monto) {
        if (validarEstatus()) {
            Transaccion nuevaTransaccion = new Transaccion(tipo, monto);
            transacciones.add(nuevaTransaccion);
            if (tipo.equals("Retiro")) {
                nuevaTransaccion.realizarRetiro(monto, this.saldo);
            } else {
                if (tipo.equals("Depósito")) {
                    nuevaTransaccion.realizarDeposito(monto, this.saldo);
                }
            }
        }
    }

    private boolean validarCambioPin(String pinActual, String pinNuevo) {
        if (!pinActual.equals(pinNuevo)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarPin(String pin) {
        return this.pin.equals(pin);
    }

    private void bloquearCuenta() {
        if (estatus.equals("Activa")) {
            estatus = "Inactiva";
        }
    }

    private boolean validarEstatus() {
        if (this.estatus.equals("Activa")) {
            return true;
        }
        return false;
    }

    private boolean validarIngreso(String pinIngresado) {
        while (usosPin <= 3) {
            if (pinIngresado.compareTo(pin) == 0) {
                usosPin = 0;
                return true;
            }
            usosPin++;
        }
        bloquearCuenta();
        return false;
    }

    public String getPinEncriptado() {
        return pinEncriptado;
    }

    public void setPinEncriptado(String pinEncriptado) {
        this.pinEncriptado = pinEncriptado;
    }

    // Llave de cifrado (debe ser de 16 bytes para AES-128)
    private static final String CLAVE_SECRETA = "clave123456789012";

    public String encriptarPin(String pin) throws Exception {
        // Validar el formato del PIN antes de encriptarlo
        if (!validarFormatoPin(pin)) {
            throw new IllegalArgumentException("El PIN no cumple con el formato requerido.");
        }

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE_SECRETA.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encriptado = cipher.doFinal(pin.getBytes());
        return Base64.getEncoder().encodeToString(encriptado);
    }

    public String desencriptarPin(String pinEncriptado) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE_SECRETA.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodificado = Base64.getDecoder().decode(pinEncriptado);
        byte[] desencriptado = cipher.doFinal(decodificado);
        return new String(desencriptado);
    }

    private boolean validarFormatoPin(String pin) {
        // Expresión regular para validar el PIN
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(pin).matches();
    }

    // Ejemplo de cómo usar el método de encriptación cuando se establece un nuevo
    // PIN
    public void cambiarPin(String pinNuevo) {
        try {
            if (validarCambioPin(this.pin, pinNuevo)) {
                this.pinEncriptado = encriptarPin(pinNuevo);
                System.out.println("PIN cambiado y encriptado correctamente.");
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar el PIN: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Cuenta cuenta = new Cuenta(1000, "Ab1234", new Cliente("Luis", 208190240, 89655235, "luis@gmail.com")); // Debes
                                                                                                                    // definir
                                                                                                                    // la
                                                                                                                    // clase
                                                                                                                    // Cliente
            cuenta.cambiarPin("Ab5678");
            System.out.println("PIN encriptado: " + cuenta.pinEncriptado);
            String pinDesencriptado = cuenta.desencriptarPin(cuenta.pinEncriptado);
            System.out.println("PIN desencriptado: " + pinDesencriptado);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}