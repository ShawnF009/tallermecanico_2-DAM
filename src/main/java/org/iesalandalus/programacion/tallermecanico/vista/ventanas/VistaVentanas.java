package org.iesalandalus.programacion.tallermecanico.vista.ventanas;

import javafx.collections.ObservableList;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.TipoTrabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.vista.Vista;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.GestorEventos;
import org.iesalandalus.programacion.tallermecanico.vista.ventanas.controladores.*;
import org.iesalandalus.programacion.tallermecanico.vista.ventanas.utilidades.Controladores;
import org.iesalandalus.programacion.tallermecanico.vista.ventanas.utilidades.Dialogos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VistaVentanas implements Vista {

    private final GestorEventos gestorEventos = new GestorEventos(Evento.values());
    private VentanaPrincipal ventanaPrincipal;
    private static VistaVentanas instancia;

    private boolean clientesLeidos = false;
    private boolean vehiculosLeidos = false;
    private boolean trabajosLeidos = false;

    private MostrarCliente mostrarCliente;
    private MostrarVehiculo mostrarVehiculo;
    private MostrarTrabajo mostrarTrabajo;
    private MostrarClientes mostrarClientes;
    private MostrarVehiculos mostrarVehiculos;
    private MostrarTrabajos mostrarTrabajos;

    private VistaVentanas() {
    }

    public static VistaVentanas getInstancia() {
        if (instancia == null) {
            instancia = new VistaVentanas();
        }
        return instancia;
    }

    public void inicializar() {
        ventanaPrincipal = (VentanaPrincipal) Controladores.get("/vistas/VentanaPrincipal.fxml", "Taller Mecánico", null);
        mostrarCliente = (MostrarCliente) Controladores.get("/vistas/MostrarCliente.fxml", "Mostrar cliente", ventanaPrincipal.getEscenario());
        mostrarVehiculo = (MostrarVehiculo) Controladores.get("/vistas/MostrarVehiculo.fxml", "Mostrar vehiculo", ventanaPrincipal.getEscenario());
        mostrarTrabajo = (MostrarTrabajo) Controladores.get("/vistas/MostrarTrabajo.fxml", "Mostrar trabajo", ventanaPrincipal.getEscenario());
        mostrarClientes = (MostrarClientes) Controladores.get("/vistas/MostrarClientes.fxml", "Mostrar Clientes", ventanaPrincipal.getEscenario());
        mostrarVehiculos = (MostrarVehiculos) Controladores.get("/vistas/MostrarVehiculos.fxml", "Mostrar Vehículos", ventanaPrincipal.getEscenario());
        mostrarTrabajos = (MostrarTrabajos) Controladores.get("/vistas/MostrarTrabajos.fxml", "Mostrar trabajos", ventanaPrincipal.getEscenario());
    }

    public List<Cliente> getClientes() {
        return mostrarClientes.getClientes();
    }

    public List<Vehiculo> getVehiculos() {
        return mostrarVehiculos.getVehiculos();
    }

    @Override
    public GestorEventos getGestorEventos() {
        return gestorEventos;
    }

    @Override
    public void comenzar() {
        LanzadoraVentanaPrincipal.comenzar();
    }

    @Override
    public void terminar() {
        System.out.println("Hasta luego Lucasss!!!");
    }

    @Override
    public Cliente leerCliente() {
        LeerCliente leerCliente = (LeerCliente) Controladores.get("/vistas/LeerCliente.fxml", "Leer Cliente", ventanaPrincipal.getEscenario());
        return leerCliente.getCliente();
    }

    @Override
    public Cliente leerClienteDni() {
        String dni = null;
        if (!mostrarCliente.getEscenario().isShowing()) {
            dni = Dialogos.mostrarDialogoTexto("Leer dni", "Introduce el DNI:", ventanaPrincipal.getEscenario(), Cliente.ER_DNI);
        }
        return mostrarCliente.getEscenario().isShowing() ? mostrarCliente.getCliente() : Cliente.get(dni);
    }

    @Override
    public String leerNuevoNombre() {
        return mostrarCliente.getNombre();
    }

    @Override
    public String leerNuevoTelefono() {
        return mostrarCliente.getTelefono();
    }

    @Override
    public Vehiculo leerVehiculo() {
        LeerVehiculo leerVehiculo = (LeerVehiculo) Controladores.get("/vistas/LeerVehiculo.fxml", "Leer Vehículo", ventanaPrincipal.getEscenario());
        return leerVehiculo.getVehiculo();
    }

    @Override
    public Vehiculo leerVehiculoMatricula() {
        String matricula = null;
        if (!mostrarVehiculo.getEscenario().isShowing()) {
            matricula = Dialogos.mostrarDialogoTexto("Leer matrícula", "Introduce la matrícula", ventanaPrincipal.getEscenario(), Vehiculo.ER_MATRICULA);
        }
        return mostrarVehiculo.getEscenario().isShowing() ? mostrarVehiculo.getVehiculo() : Vehiculo.get(matricula);
    }

    @Override
    public Trabajo leerRevision() {
        LeerTrabajo leerTrabajo = (LeerTrabajo) Controladores.get("/vistas/LeerTrabajo.fxml", "Leer Trabajo", ventanaPrincipal.getEscenario());
        LeerTrabajoBuscar leerTrabajoBuscar = (LeerTrabajoBuscar) Controladores.get("/vistas/LeerTrabajoBuscar.fxml", "Buscar Trabajo", ventanaPrincipal.getEscenario());
        //System.out.println(leerTrabajo.getEscenario().isShowing());
        Trabajo trabajo =  null;
        if (mostrarTrabajos.getEscenario().isShowing()) {
            trabajo = mostrarTrabajos.getTrabajo();
        } else if (leerTrabajo.getEscenario().isShowing()) {
            trabajo = leerTrabajo.getTrabajo();
        } else if (leerTrabajoBuscar.getEscenario().isShowing()) {
            trabajo = leerTrabajoBuscar.getTrabajo();
        }
        return trabajo;
    }

    @Override
    public Trabajo leerMecanico() {
        return leerRevision();
    }

    @Override
    public Trabajo leerTrabajoVehiculo() {
        return leerRevision();
    }

    @Override
    public int leerHoras() {
        return mostrarTrabajos.getEscenario().isShowing() ? mostrarTrabajos.getHoras() : mostrarTrabajo.getHoras();
    }

    @Override
    public float leerPrecioMaterial() {
        return mostrarTrabajos.getEscenario().isShowing() ? mostrarTrabajos.getPrecioMaterial() :  mostrarTrabajo.getPrecioMaterial();
    }

    @Override
    public LocalDate leerFechaCierre() {
        LeerFechaFin leerFechaFin = (LeerFechaFin) Controladores.get("/vistas/LeerFechaFin.fxml", "Leer fecha fin", ventanaPrincipal.getEscenario());
        return leerFechaFin.getFechaFin();
    }

    @Override
    public LocalDate leerMes() {
        MostrarEstadisticas mostrarEstadisticas = (MostrarEstadisticas) Controladores.get("/vistas/MostrarEstadisticas.fxml", "Mostrar estadísticas",  ventanaPrincipal.getEscenario());
        return mostrarEstadisticas.getMes();
    }

    @Override
    public void mostrarCliente(Cliente cliente) {
        mostrarCliente.actualizar(cliente);
        mostrarCliente.getEscenario().showAndWait();
    }

    @Override
    public void mostrarVehiculo(Vehiculo vehiculo) {
        mostrarVehiculo.actualizar(vehiculo);
        mostrarVehiculo.getEscenario().showAndWait();
    }

    @Override
    public void mostrarTrabajo(Trabajo trabajo) {
        mostrarTrabajo.actualizar(trabajo);
        mostrarTrabajo.getEscenario().showAndWait();
    }

    @Override
    public void mostrarClientes(List<Cliente> clientes) {
        if (!clientesLeidos) {
            clientesLeidos = true;
            mostrarClientes.actualizar(clientes);
        } else {
            mostrarClientes.actualizar(clientes);
            mostrarClientes.getEscenario().show();
        }
    }

    @Override
    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        if (!vehiculosLeidos) {
            vehiculosLeidos = true;
            mostrarVehiculos.actualizar(vehiculos);
        } else {
            mostrarVehiculos.actualizar(vehiculos);
            mostrarVehiculos.getEscenario().show();
        }
    }

    @Override
    public void mostrarTrabajos(List<Trabajo> trabajos) {
        if (!trabajosLeidos) {
            trabajosLeidos = true;
            mostrarTrabajos.actualizar(trabajos);
        } else {
            mostrarTrabajos.actualizar(trabajos);
            mostrarTrabajos.getEscenario().show();
        }
    }

    @Override
    public void mostrarEstadisticasMensuales(Map<TipoTrabajo, Integer> estadisticas) {
        MostrarEstadisticas mostrarEstadisticas = (MostrarEstadisticas) Controladores.get("/vistas/MostrarEstadisticas.fxml", "Mostrar estadísticas",  ventanaPrincipal.getEscenario());
        mostrarEstadisticas.setEstadisticas(estadisticas);
    }


    @Override
    public void notificarResultado(Evento evento, String texto, boolean exito) {
        if (exito) {
            Dialogos.mostrarDialogoInformacion(evento.toString(), texto, ventanaPrincipal.getEscenario());
        } else {
            Dialogos.mostrarDialogoError(evento.toString(), texto, ventanaPrincipal.getEscenario());
        }
    }

    @Override
    public void notificarResultado(Evento evento, String texto, boolean exito, Cliente cliente) {
        switch (evento) {
            case INSERTAR_CLIENTE -> mostrarClientes.getClientes().add(cliente);
            case MODIFICAR_CLIENTE -> modificarCliente(cliente);
            case BORRAR_CLIENTE -> mostrarClientes.getClientes().remove(cliente);
            default -> throw new IllegalArgumentException("El evento debe estar referido a un cliente.");
        }
        notificarResultado(evento, texto, exito);
    }

    private void modificarCliente(Cliente cliente) {
        ObservableList<Cliente> clientes = mostrarClientes.getClientes();
        int indice = clientes.indexOf(cliente);
        if (indice != -1) {
            clientes.set(indice, cliente);
        }
    }

    @Override
    public void notificarResultado(Evento evento, String texto, boolean exito, Vehiculo vehiculo) {
        switch (evento) {
            case INSERTAR_VEHICULO -> mostrarVehiculos.getVehiculos().add(vehiculo);
            case BORRAR_VEHICULO -> mostrarVehiculos.getVehiculos().remove(vehiculo);
            default -> throw new IllegalArgumentException("El evento debe estar referido a un vehículo.");
        }
        notificarResultado(evento, texto, exito);
    }

    @Override
    public void notificarResultado(Evento evento, String texto, boolean exito, Trabajo trabajo) {
        switch (evento) {
            case INSERTAR_MECANICO, INSERTAR_REVISION -> mostrarTrabajos.getTrabajos().add(trabajo);
            case ANADIR_HORAS_TRABAJO, ANADIR_PRECIO_MATERIAL_TRABAJO, CERRAR_TRABAJO -> { mostrarTrabajo.actualizar(trabajo); mostrarTrabajos.actualizar(trabajo); }
            case BORRAR_TRABAJO -> mostrarTrabajos.getTrabajos().remove(trabajo);
            default -> throw new IllegalArgumentException("El evento debe estar referido a un trabajo.");
        }
        notificarResultado(evento, texto, exito);
    }

}
