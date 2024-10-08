package controladores;

import modelos.Cliente;
import modelos.Cuenta;
import servicios.PersistenciaDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ClienteControlador {

    private List<Cliente> clientes;

    public ClienteControlador() {
        // Cargar clientes existentes desde el archivo XML al iniciar
        this.clientes = PersistenciaDatos.cargarClientes();
        if (clientes == null) {
            clientes = new ArrayList<>(); // Inicializa la lista si no se cargaron clientes
        }

    }

    public boolean crearCliente(String nombre, int identificacion, String numTelefono, String correoElectronico) {
        if (buscarClientePorIdentificacion(identificacion).isEmpty()) {
            Cliente nuevoCliente = new Cliente(nombre, identificacion, numTelefono, correoElectronico);
            clientes.add(nuevoCliente);
            PersistenciaDatos.guardarClientes(clientes); // Asegúrate de guardar toda la lista, no solo el nuevo cliente
            return true;
        } else {
            return false; // Cliente ya existe
        }
    }
    

    public boolean actualizarTelefono(int identificacion, String nuevoTelefono) {
        Optional<Cliente> clienteOpt = buscarClientePorIdentificacion(identificacion);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get(); // Obtener el cliente del Optional
            cliente.setNumTelefono(nuevoTelefono);
            PersistenciaDatos.guardarClientes(clientes);
            return true;
        } else {
            return false; // Cliente no encontrado
        }
    }

    public boolean actualizarCorreo(int identificacion, String nuevoCorreo) {
        Optional<Cliente> clienteOpt = buscarClientePorIdentificacion(identificacion);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get(); // Obtener el cliente del Optional
            cliente.setCorreoElectronico(nuevoCorreo);
            PersistenciaDatos.guardarClientes(clientes);
            return true;
        } else {
            return false; // Cliente no encontrado
        }
    }

    public Optional<Cliente> buscarClientePorIdentificacion(int identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion() == identificacion) {
                return Optional.of(cliente); // Devuelve un Optional que contiene el cliente
            }
        }
        return Optional.empty(); // Devuelve un Optional vacío si no se encuentra el cliente
    }

    public Cliente indicarClientePorIdentificacion(int identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion() == identificacion) {
                return cliente;
            }
        }
        return null;
    }
    

    public List<Cliente> obtenerClientes() {
        return clientes;
    }

}