package controladores;

import java.util.ArrayList;

import modelos.Cliente;

public class ClienteControlador {

    private ArrayList<Cliente> clientes;

    public ClienteControlador() {
        this.clientes = new ArrayList<>();
    }

    public boolean crearCliente(String nombre, int identificacion, int numTelefono, String correoElectronico) {
        if (buscarClientePorIdentificacion(identificacion) == null) {
            Cliente nuevoCliente = new Cliente(nombre, identificacion, numTelefono, correoElectronico);
            clientes.add(nuevoCliente);
            return true;
        } else {
            return false; // Cliente ya existe
        }
    }

    public boolean actualizarTelefono(int identificacion, int nuevoTelefono) {
        Cliente cliente = buscarClientePorIdentificacion(identificacion);
        if (cliente != null) {
            cliente.setNumTelefono(nuevoTelefono);
            return true;
        } else {
            return false; // Cliente no encontrado
        }
    }

    public boolean actualizarCorreo(int identificacion, String nuevoCorreo) {
        Cliente cliente = buscarClientePorIdentificacion(identificacion);
        if (cliente != null) {
            cliente.setCorreoElectronico(nuevoCorreo);
            return true;
        } else {
            return false; // Cliente no encontrado
        }
    }

    private Cliente buscarClientePorIdentificacion(int identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion() == identificacion) {
                return cliente;
            }
        }
        return null;
    }

    public ArrayList<Cliente> obtenerClientes() {
        return clientes;
    }
}
