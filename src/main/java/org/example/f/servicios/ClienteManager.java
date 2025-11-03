package org.example.f.servicios;

import org.example.f.modelos.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteManager {

    private final List<Cliente> catalogoClientes;
    private int nextId = 1;
    private static final String FILE_NAME = "clientes.dat";

    public ClienteManager() {
        this.catalogoClientes = new ArrayList<>();

        if (!cargarDatos()) {
            cargarDatosIniciales();
            guardarDatos();
        }
    }

    private void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales de prueba...");

        Cliente c1 = new Cliente();
        c1.setNombre("Juan Pérez");
        c1.setTelefono("5512345678");
        c1.setEmail("juan.perez@mail.com");
        c1.setDireccion("Calle 269");
        registrarClienteInterno(c1);

        Cliente c2 = new Cliente();
        c2.setNombre("Ana Gómez");
        c2.setTelefono("5598765432");
        c2.setEmail("ana.gomez@mail.com");
        c2.setDireccion("Avenida 345");
        registrarClienteInterno(c2);
    }

    private void registrarClienteInterno(Cliente cliente) {
        cliente.setIdCliente(nextId++);
        this.catalogoClientes.add(cliente);
    }

    public void guardarCliente(Cliente cliente) {
        cliente.setIdCliente(nextId++);
        this.catalogoClientes.add(cliente);
        System.out.println("Cliente CREADO y registrado: " + cliente.getNombre() + " (ID: " + cliente.getIdCliente() + ")");

        guardarDatos();
    }

    public void actualizarCliente(Cliente clienteActualizado) {
        int index = findClienteIndexById(clienteActualizado.getIdCliente());

        if (index != -1) {
            this.catalogoClientes.set(index, clienteActualizado);
            System.out.println("Cliente ACTUALIZADO: " + clienteActualizado.getNombre() + " (ID: " + clienteActualizado.getIdCliente() + ")");

            guardarDatos();
        } else {
            System.out.println("Error: No se puede actualizar. Cliente ID " + clienteActualizado.getIdCliente() + " no encontrado.");
        }
    }

    public void eliminarCliente(int idCliente) {
        boolean eliminado = this.catalogoClientes.removeIf(c -> c.getIdCliente() == idCliente);

        if (eliminado) {
            System.out.println("Cliente ID " + idCliente + " eliminado.");

            guardarDatos();
        } else {
            System.out.println("Cliente ID " + idCliente + " no encontrado para eliminar.");
        }
    }

    private void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this.catalogoClientes);
            oos.writeInt(this.nextId);
            System.out.println("✅ Clientes guardados en " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar clientes: " + e.getMessage());
        }
    }

    private boolean cargarDatos() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            @SuppressWarnings("unchecked")
            List<Cliente> loadedList = (List<Cliente>) ois.readObject();
            this.catalogoClientes.addAll(loadedList);
            this.nextId = ois.readInt();
            System.out.println("✅ Clientes cargados exitosamente desde " + FILE_NAME);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error al cargar clientes. Se usará nueva lista. " + e.getMessage());
            return false;
        }
    }


    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(catalogoClientes);
    }

    private int findClienteIndexById(int idCliente) {
        for (int i = 0; i < catalogoClientes.size(); i++) {
            if (catalogoClientes.get(i).getIdCliente() == idCliente) {
                return i;
            }
        }
        return -1;
    }

    public Optional<Cliente> buscarCliente(String busqueda) {
        try {
            int id = Integer.parseInt(busqueda.trim());
            return catalogoClientes.stream().filter(c -> c.getIdCliente() == id).findFirst();
        } catch (NumberFormatException e) {
            String nombreBusqueda = busqueda.trim().toLowerCase();
            return catalogoClientes.stream().filter(c -> c.getNombre().toLowerCase().contains(nombreBusqueda)).findFirst();
        }
    }
}