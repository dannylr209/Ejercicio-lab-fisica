//======================================================================================================
//  __   __ ____        ____   ________
// |  | |  |\   \      /   /  /   __   \
// |  | |  | \   \    /   /  /   /  |__|
// |  | |  |  \   \  /   /   |   |  ___
// |  | |  |   \   \/   /    |   | |_  |
// |  |_|  |    \      /     |   |__|  |
// \_______/`    \____/      \_________/
//Universidad   del Valle    de Guatemala
//Programación Orientada a Objetos
//Sección 50
//
//Daniel López
// ______     ______   ____    ___ ____    ___ ___     __
//|   __  \ /  ____  \|    \  |   |    \  |   |   |   |  |
//|  |  \  |  |____|  |     \ |   |     \ |   |   \__/   |
//|  |   | |   ____   |      \|   |      \|   |\__    __/
//|  |___| |  |    |  |   |\      |   |\      |   |  |
//|________|__|    |__|___| \_____|___| \_____|   |__|
//242159
//
//Laboratorio de Física
//02/11/2025
//======================================================================================================

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de equipos usando patrón Singleton.
 * Cumple con SRP: Solo gestiona la colección de equipos
 * Cumple con OCP: Extensible sin modificar código existente
 * Cumple con DIP: Implementa abstracción ServicioEquipos
 * 
 * @author Daniel López - 242159
 * @version 2.0 - Refactorizado para SOLID
 */
public class CatalogoEquipos implements ServicioEquipos {
    
    private static CatalogoEquipos instancia;
    private final List<Equipo> equipos;
    private final EquipoFactory factory;
    
    /**
     * Constructor privado para implementar Singleton
     */
    private CatalogoEquipos() {
        this.equipos = new ArrayList<>();
        this.factory = new EquipoFactory();
    }
    
    /**
     * Obtiene la instancia única del catálogo
     * @return CatalogoEquipos instancia singleton
     */
    public static CatalogoEquipos getInstance() {
        if (instancia == null) {
            instancia = new CatalogoEquipos();
        }
        return instancia;
    }
    
    @Override
    public void agregarEquipo(Equipo equipo) {
        if (equipo != null && !equipos.contains(equipo)) {
            equipos.add(equipo);
        }
    }
    
    @Override
    public List<Equipo> obtenerTodos() {
        return new ArrayList<>(equipos);
    }
    
    @Override
    public Equipo buscarPorId(String id) {
        return equipos.stream()
                     .filter(equipo -> equipo.getId().equalsIgnoreCase(id))
                     .findFirst()
                     .orElse(null);
    }
    
    @Override
    public List<Equipo> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return equipos.stream()
                     .filter(equipo -> equipo.getNombre().toLowerCase()
                                            .contains(nombre.toLowerCase().trim()))
                     .collect(Collectors.toList());
    }
    
    @Override
    public int obtenerCantidad() {
        return equipos.size();
    }
    
    @Override
    public void ordenarPorConsumo() {
        Collections.sort(equipos);
    }
    
    @Override
    public void ordenar(Comparator<? super Equipo> comparador) {
        equipos.sort(comparador);
    }
    
    @Override
    public void inicializar() {
        cargarEquiposIniciales();
    }
    
    @Override
    public String exportarJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\"equipos\":[");
        
        for (int i = 0; i < equipos.size(); i++) {
            json.append(equipos.get(i).toJSON());
            if (i < equipos.size() - 1) {
                json.append(",");
            }
        }
        
        json.append("]}");
        return json.toString();
    }
    
    /**
     * Carga los equipos iniciales usando el factory
     * Método privado que cumple con SRP
     */
    private void cargarEquiposIniciales() {
        // Limpiar catálogo existente
        equipos.clear();
        
        // Usar factory para crear equipos (cumple con DIP)
        cargarPendulos();
        cargarFotopuertas();
        cargarOsciloscopios();
        cargarGeneradores();
        cargarSimuladores();
    }
    
    private void cargarPendulos() {
        agregarEquipo(factory.crearPendulo(
            "PEN001", "Péndulo Simple Digital", "PASCO Scientific", 25.5,
            "Péndulo con encoder rotatorio de alta precisión para estudios de movimiento armónico simple y amortiguado",
            0.75, 3600, 1000.0
        ));
        
        agregarEquipo(factory.crearPendulo(
            "PEN002", "Péndulo Físico Avanzado", "Vernier Software", 32.0,
            "Sistema de péndulo físico con encoder de alta resolución y soporte para diferentes configuraciones",
            1.2, 7200, 2000.0
        ));
    }
    
    private void cargarFotopuertas() {
        agregarEquipo(factory.crearFotopuerta(
            "FPU001", "Fotopuerta Dual Infrarroja", "PASCO Scientific", 15.0,
            "Sistema de doble fotopuerta para medición precisa de velocidad y aceleración",
            0.1, "Infrarrojo", 0.5
        ));
        
        agregarEquipo(factory.crearFotopuerta(
            "FPU002", "Fotopuerta Láser Precisión", "Vernier Software", 22.0,
            "Fotopuerta láser de alta precisión para mediciones de tiempo y velocidad en experimentos de cinemática",
            0.05, "Láser", 1.0
        ));
    }
    
    private void cargarOsciloscopios() {
        agregarEquipo(factory.crearOsciloscopio(
            "OSC001", "Osciloscopio Digital 4CH", "Tektronix", 85.0,
            "Osciloscopio digital de 4 canales con pantalla táctil y capacidades de análisis avanzado",
            4, 100.0, 10.0, "12 bits"
        ));
        
        agregarEquipo(factory.crearOsciloscopio(
            "OSC002", "Osciloscopio Portátil", "Keysight", 45.0,
            "Osciloscopio portátil de 2 canales ideal para mediciones de campo y laboratorio básico",
            2, 50.0, 5.0, "8 bits"
        ));
    }
    
    private void cargarGeneradores() {
        agregarEquipo(factory.crearGenerador(
            "GEN001", "Generador de Funciones DDS", "Rigol Technologies", 40.0,
            "Generador de funciones DDS con formas de onda arbitrarias y modulación avanzada",
            "Senoidal, Cuadrada, Triangular, Ruido, Arbitraria", 25.0, 10.0, "Continuo/Burst"
        ));
        
        agregarEquipo(factory.crearGenerador(
            "GEN002", "Generador RF de Precisión", "Agilent", 120.0,
            "Generador de señales RF de alta precisión para experimentos de ondas electromagnéticas",
            "Senoidal, FM, AM, PM", 1000.0, 1.0, "CW/Modulado"
        ));
    }
    
    private void cargarSimuladores() {
        agregarEquipo(factory.crearSimulador(
            "SIM001", "Simulador de Mecánica Clásica", "PhET Interactive", 150.0,
            "Software de simulación interactiva para experimentos de mecánica clásica y ondas",
            "Mecánica, Ondas, Termodinámica", "Runge-Kutta 4"
        ));
        
        agregarEquipo(factory.crearSimulador(
            "SIM002", "Simulador de Circuitos Eléctricos", "NI Multisim", 200.0,
            "Entorno de simulación completo para diseño y análisis de circuitos eléctricos y electrónicos",
            "Circuitos DC/AC, Electrónica Digital, Análisis de Fourier", "SPICE"
        ));
    }
}
