package validacion;

import java.util.regex.Pattern;

public class Formato {

    /**
     * Validar el formato del PIN según la especificación.
     *
     * @param pin el PIN a validar
     * @return true si el PIN es válido, false en caso contrario
     */
    public static boolean validarFormatoPin(String pin) {
        // Verificar que el PIN no sea nulo y tenga longitud 6
        if (pin == null || pin.length() != 6) {
            return false;
        }

        // Expresión regular para validar el PIN
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(pin).matches();
    }
    
    // Método para validar el número telefónico
    public static boolean validarTelefono(String telefono) {
        // Validar que el número de teléfono tenga el formato "+506" seguido de 8 dígitos
        return telefono != null && telefono.matches("\\+506\\d{8}");
    }    

    // Método para validar el correo electrónico
    public static boolean validarCorreo(String correo) {
        // Expresión regular para validar el formato del correo electrónico
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return correo != null && Pattern.matches(regex, correo);
    }
}