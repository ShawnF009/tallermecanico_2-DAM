package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vehiculos implements IVehiculos {

    private static final File FICHERO_VEHICULOS = new File(String.format("%s%s%s%s%s%s%s", "datos", File.separator, "ficheros", File.separator, "json", File.separator, "vehiculos.json"));

    private static Vehiculos instancia;
    private final ObjectMapper mapper;

    private Vehiculos() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static Vehiculos getInstancia() {
        if (instancia == null) {
            instancia = new Vehiculos();
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

    private List<Vehiculo> leer() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        if (FICHERO_VEHICULOS.exists()) {
            try {
                vehiculos = mapper.readValue(FICHERO_VEHICULOS, new TypeReference<List<Vehiculo>>() {});
            } catch (IOException e) {
                System.out.printf("Error al leer el fichero %s. --> %s%n", FICHERO_VEHICULOS, e.getMessage());
            }
        } else {
            System.out.printf("No se encuentra el fichero %s (directorio de trabajo: %s).%n", FICHERO_VEHICULOS.getAbsolutePath(), System.getProperty("user.dir"));
        }
        return vehiculos;
    }

    private void escribir(List<Vehiculo> vehiculos) {
        try {
            File directorio = FICHERO_VEHICULOS.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            mapper.writeValue(FICHERO_VEHICULOS, vehiculos);
        } catch (IOException e) {
            System.out.printf("Error al escribir el fichero %s. --> %s%n", FICHERO_VEHICULOS, e.getMessage());
        }
    }

    @Override
    public List<Vehiculo> get() {
        return leer();
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede insertar un vehículo nulo.");
        List<Vehiculo> vehiculos = leer();
        if (vehiculos.contains(vehiculo)) {
            throw new TallerMecanicoExcepcion("Ya existe un vehículo con esa matrícula.");
        }
        vehiculos.add(vehiculo);
        escribir(vehiculos);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "No se puede buscar un vehículo nulo.");
        List<Vehiculo> vehiculos = leer();
        int indice = vehiculos.indexOf(vehiculo);
        return (indice == -1) ? null : vehiculos.get(indice);
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede borrar un vehículo nulo.");
        List<Vehiculo> vehiculos = leer();
        if (!vehiculos.remove(vehiculo)) {
            throw new TallerMecanicoExcepcion("No existe ningún vehículo con esa matrícula.");
        }
        escribir(vehiculos);
    }
}
