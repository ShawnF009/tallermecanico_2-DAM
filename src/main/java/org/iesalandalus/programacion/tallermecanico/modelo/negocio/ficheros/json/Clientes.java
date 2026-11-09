package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clientes implements IClientes {

    private static final String FICHERO_CLIENTES = "datos/ficheros/json/clientes.json";
    private static Clientes instancia;
    private final List<Cliente> coleccionClientes;
    private final ObjectMapper mapper;

    private Clientes() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        coleccionClientes = new ArrayList<>();
        leer();
    }

    public static Clientes getInstancia() {
        if (instancia == null) {
            instancia = new Clientes();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        // Vacío
    }

    @Override
    public void terminar() {
        // Vacío
    }

    @Override
    public List<Cliente> get() {
        return new ArrayList<>(coleccionClientes);
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede insertar un cliente nulo.");
        if (coleccionClientes.contains(cliente)) {
            throw new TallerMecanicoExcepcion("Ya existe un cliente con ese DNI.");
        }
        coleccionClientes.add(cliente);
        escribir();
    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede modificar un cliente nulo.");
        Cliente clienteEncontrado = buscar(cliente);
        if (clienteEncontrado == null) {
            throw new TallerMecanicoExcepcion("No existe ningún cliente con ese DNI.");
        }
        if (nombre != null && !nombre.isBlank()) {
            clienteEncontrado.setNombre(nombre);
        }
        if (telefono != null && !telefono.isBlank()) {
            clienteEncontrado.setTelefono(telefono);
        }
        escribir();
        return clienteEncontrado;
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Objects.requireNonNull(cliente, "No se puede buscar un cliente nulo.");
        int index = coleccionClientes.indexOf(cliente);
        return index != -1 ? coleccionClientes.get(index) : null;
    }

    @Override
    public void borrar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede borrar un cliente nulo.");
        if (!coleccionClientes.remove(cliente)) {
            throw new TallerMecanicoExcepcion("No existe ningún cliente con ese DNI.");
        }
        escribir();
    }

    private void leer() {
        File fichero = new File(FICHERO_CLIENTES);
        if (fichero.exists()) {
            try {
                List<Cliente> clientesLeidos = mapper.readValue(fichero, new TypeReference<List<Cliente>>() {});
                coleccionClientes.clear();
                coleccionClientes.addAll(clientesLeidos);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error al leer el fichero de clientes: " + e.getMessage());
            }
        }
    }

    private void escribir() {
        File fichero = new File(FICHERO_CLIENTES);
        File directorio = fichero.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fichero, coleccionClientes);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al escribir en el fichero de clientes: " + e.getMessage());
        }
    }
}