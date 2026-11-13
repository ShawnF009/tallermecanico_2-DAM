package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clientes implements IClientes {

    private static final File FICHERO_CLIENTES = new File(String.format("%s%s%s%s%s%s%s", "datos", File.separator, "ficheros", File.separator, "json", File.separator, "clientes.json"));

    private static Clientes instancia;
    private final ObjectMapper mapper;

    private Clientes() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static Clientes getInstancia() {
        if (instancia == null) {
            instancia = new Clientes();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        // Los datos se leen cuando los solicita el usuario.
    }

    @Override
    public void terminar() {
        // Los datos se escriben cada vez que hay alguna modificación.
    }

    private List<Cliente> leer() {
        List<Cliente> clientes = new ArrayList<>();
        if (FICHERO_CLIENTES.exists()) {
            try {
                clientes = mapper.readValue(FICHERO_CLIENTES, new TypeReference<List<Cliente>>() {});
            } catch (IOException e) {
                System.out.printf("Error al leer el fichero %s. --> %s%n", FICHERO_CLIENTES, e.getMessage());
            }
        } else {
            System.out.printf("No se encuentra el fichero %s (directorio de trabajo: %s).%n", FICHERO_CLIENTES.getAbsolutePath(), System.getProperty("user.dir"));
        }
        return clientes;
    }

    private void escribir(List<Cliente> clientes) {
        try {
            File directorio = FICHERO_CLIENTES.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            mapper.writeValue(FICHERO_CLIENTES, clientes);
        } catch (IOException e) {
            System.out.printf("Error al escribir el fichero %s. --> %s%n", FICHERO_CLIENTES, e.getMessage());
        }
    }

    @Override
    public List<Cliente> get() {
        return leer();
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede insertar un cliente nulo.");
        List<Cliente> clientes = leer();
        if (clientes.contains(cliente)) {
            throw new TallerMecanicoExcepcion("Ya existe un cliente con ese DNI.");
        }
        clientes.add(cliente);
        escribir(clientes);
    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede modificar un cliente nulo.");
        List<Cliente> clientes = leer();
        int indice = clientes.indexOf(cliente);
        if (indice == -1) {
            throw new TallerMecanicoExcepcion("No existe ningún cliente con ese DNI.");
        }
        Cliente clienteEncontrado = clientes.get(indice);
        if (nombre != null && !nombre.isBlank()) {
            clienteEncontrado.setNombre(nombre);
        }
        if (telefono != null && !telefono.isBlank()) {
            clienteEncontrado.setTelefono(telefono);
        }
        escribir(clientes);
        return clienteEncontrado;
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Objects.requireNonNull(cliente, "No se puede buscar un cliente nulo.");
        List<Cliente> clientes = leer();
        int indice = clientes.indexOf(cliente);
        return (indice == -1) ? null : clientes.get(indice);
    }

    @Override
    public void borrar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede borrar un cliente nulo.");
        List<Cliente> clientes = leer();
        if (!clientes.remove(cliente)) {
            throw new TallerMecanicoExcepcion("No existe ningún cliente con ese DNI.");
        }
        escribir(clientes);
    }
}
