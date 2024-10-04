package controladores;

public class CuentaControlador {
    public boolean autenticarCuenta(String numeroCuenta, String pin) {
        if (numeroCuenta.equals("123456") && pin.equals("1234")) {
            return true;
        }
        return false;
    }
}
