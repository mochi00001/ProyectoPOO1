package validacion;

public class Formato {

    /**
     * Validar el formato del PIN según la especificación.
     *
     * @param pin el PIN a validar
     * @return true si el PIN es válido, false en caso contrario
     */
    public static boolean validarFormatoPin(String pin) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6}$";
        return pin.matches(regex);
    }

}