package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vehiculos implements IVehiculos {

    private static final String FICHERO_VEHICULOS = "datos/ficheros/json/vehiculos.json";
    private static Vehiculos instancia;
    private final List<Vehiculo> coleccionVehiculos;
    private final ObjectMapper mapper;

    private Vehiculos() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        coleccionVehiculos = new ArrayList<>();
        leer();
    }

    public static Vehiculos getInstancia() {
        if (instancia == null) {
            instancia = new Vehiculos();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        // Vacío ya que se cargan al instanciar
    }

    @Override
    public void terminar() {
        // Vacío ya que se escribe en cada modificación
    }

    @Override
    public List<Vehiculo> get() {
        return new ArrayList<>(coleccionVehiculos);
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede insertar un vehículo nulo.");
        if (coleccionVehiculos.contains(vehiculo)) {
            throw new TallerMecanicoExcepcion("Ya existe un vehículo con esa matrícula.");
        }
        coleccionVehiculos.add(vehiculo);
        escribir();
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "No se puede buscar un vehículo nulo.");
        int index = coleccionVehiculos.indexOf(vehiculo);
        return index != -1 ? coleccionVehiculos.get(index) : null;
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede borrar un vehículo nulo.");
        if (!coleccionVehiculos.remove(vehiculo)) {
            throw new TallerMecanicoExcepcion("No existe ningún vehículo con esa matrícula.");
        }
        escribir();
    }

    private void leer() {
        File fichero = new File(FICHERO_VEHICULOS);
        if (fichero.exists()) {
            try {
                List<Vehiculo> vehiculosLeidos = mapper.readValue(fichero, new TypeReference<List<Vehiculo>>() {});
                coleccionVehiculos.clear();
                coleccionVehiculos.addAll(vehiculosLeidos);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error al leer el fichero de vehículos: " + e.getMessage());
            }
        }
    }

    private void escribir() {
        File fichero = new File(FICHERO_VEHICULOS);
        File directorio = fichero.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fichero, coleccionVehiculos);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al escribir en el fichero de vehículos: " + e.getMessage());
        }
    }
}