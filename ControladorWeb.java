import java.util.List;

/**
 * Controlador web que maneja las solicitudes HTTP y coordina con el modelo.
 * Cumple con SRP: Solo se encarga de la lógica de controlador
 * Cumple con DIP: Depende de abstracciones, no de implementaciones concretas
 * Cumple con OCP: Extensible sin modificar código existente
 * 
 * @author Sistema de Laboratorio de Física
 * @version 2.0 - Refactorizado para SOLID
 */
public class ControladorWeb {
    
    private final ServicioEquipos servicioEquipos;
    private final GeneradorRespuesta generadorRespuesta;
    
    /**
     * Constructor que inyecta dependencias (DIP)
     * @param servicioEquipos servicio para gestión de equipos
     * @param generadorRespuesta generador de respuestas
     */
    public ControladorWeb(ServicioEquipos servicioEquipos, GeneradorRespuesta generadorRespuesta) {
        this.servicioEquipos = servicioEquipos;
        this.generadorRespuesta = generadorRespuesta;
    }
    
    /**
     * Constructor por defecto para compatibilidad
     */
    public ControladorWeb() {
        this(CatalogoEquipos.getInstance(), new GeneradorRespuestaJSON());
    }
    
    /**
     * Inicializa el sistema cargando los equipos
     */
    public void inicializarSistema() {
        servicioEquipos.inicializar();
    }
    
    /**
     * Obtiene todos los equipos en formato JSON
     * @return String respuesta JSON con todos los equipos
     */
    public String obtenerTodosLosEquiposJSON() {
        try {
            List<Equipo> equipos = servicioEquipos.obtenerTodos();
            return generadorRespuesta.generarRespuestaLista("Equipos obtenidos correctamente", equipos);
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error al obtener equipos: " + e.getMessage());
        }
    }
    
    /**
     * Busca un equipo por ID y retorna JSON
     * @param id identificador del equipo
     * @return String respuesta JSON
     */
    public String buscarEquipoPorIdJSON(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return generadorRespuesta.generarRespuestaError("ID no puede estar vacío");
            }
            
            Equipo equipo = servicioEquipos.buscarPorId(id.trim());
            if (equipo != null) {
                return generadorRespuesta.generarRespuestaExito("Equipo encontrado", equipo.toJSON());
            } else {
                return generadorRespuesta.generarRespuestaError("No se encontró equipo con ID: " + id);
            }
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error en búsqueda por ID: " + e.getMessage());
        }
    }
    
    /**
     * Busca equipos por nombre y retorna JSON
     * @param nombre nombre o parte del nombre a buscar
     * @return String respuesta JSON
     */
    public String buscarEquiposPorNombreJSON(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return generadorRespuesta.generarRespuestaError("Nombre no puede estar vacío");
            }
            
            List<Equipo> equipos = servicioEquipos.buscarPorNombre(nombre.trim());
            
            if (!equipos.isEmpty()) {
                return generadorRespuesta.generarRespuestaLista("Se encontraron equipos", equipos);
            } else {
                return generadorRespuesta.generarRespuestaError("No se encontraron equipos con nombre: " + nombre);
            }
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error en búsqueda por nombre: " + e.getMessage());
        }
    }
    
    /**
     * Ordena los equipos por consumo eléctrico y retorna JSON
     * @return String respuesta JSON con equipos ordenados
     */
    public String ordenarPorConsumoJSON() {
        try {
            servicioEquipos.ordenarPorConsumo();
            List<Equipo> equipos = servicioEquipos.obtenerTodos();
            return generadorRespuesta.generarRespuestaLista("Equipos ordenados por consumo eléctrico", equipos);
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error al ordenar equipos: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene detalles completos de un equipo
     * @param id identificador del equipo
     * @return String respuesta JSON con detalles
     */
    public String obtenerDetallesEquipoJSON(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return generadorRespuesta.generarRespuestaError("ID no puede estar vacío");
            }
            
            Equipo equipo = servicioEquipos.buscarPorId(id.trim());
            if (equipo != null) {
                String detalles = equipo.mostrarDetalles().replace("\"", "\\\"").replace("\n", "\\n");
                return String.format(
                    "{\"success\":true,\"message\":\"Detalles del equipo\",\"data\":{\"detalles\":\"%s\",\"equipo\":%s}}",
                    detalles, equipo.toJSON()
                );
            } else {
                return generadorRespuesta.generarRespuestaError("No se encontró equipo con ID: " + id);
            }
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error al obtener detalles: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene estadísticas del catálogo
     * @return String respuesta JSON con estadísticas
     */
    public String obtenerEstadisticasJSON() {
        try {
            int totalEquipos = servicioEquipos.obtenerCantidad();
            List<Equipo> equipos = servicioEquipos.obtenerTodos();
            
            double consumoTotal = equipos.stream()
                .mapToDouble(Equipo::getConsumoElectrico)
                .sum();
            
            double consumoPromedio = equipos.isEmpty() ? 0 : consumoTotal / equipos.size();
            
            String estadisticas = String.format(
                "{\"totalEquipos\":%d,\"consumoTotal\":%.2f,\"consumoPromedio\":%.2f}",
                totalEquipos, consumoTotal, consumoPromedio
            );
            
            return generadorRespuesta.generarRespuestaExito("Estadísticas del catálogo", estadisticas);
        } catch (Exception e) {
            return generadorRespuesta.generarRespuestaError("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}
