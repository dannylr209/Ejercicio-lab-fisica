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

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa un simulador de experimentos de física.
 * Implementa la interfaz Simulable.
 * 
 * @author Sistema de Laboratorio de Física
 * @version 1.0
 */
public class SimuladorFisica extends EquipoBase implements Simulable {
    
    private String tipoSimulacion;
    private Map<String, Object> parametrosConfiguracion;
    private String estadoSimulacion;
    private String algoritmoSimulacion;
    
    /**
     * Constructor para SimuladorFisica
     */
    public SimuladorFisica(String id, String nombre, String fabricante, double consumoElectrico,
                          String resumenCaracteristicas, String tipoSimulacion, String algoritmoSimulacion) {
        super(id, nombre, "Simulador de Física", fabricante, consumoElectrico, resumenCaracteristicas);
        this.tipoSimulacion = tipoSimulacion;
        this.algoritmoSimulacion = algoritmoSimulacion;
        this.parametrosConfiguracion = new HashMap<>();
        this.estadoSimulacion = "Detenida";
        
        // Configurar parámetros por defecto
        parametrosConfiguracion.put("tiempo_simulacion", 10.0);
        parametrosConfiguracion.put("paso_tiempo", 0.01);
        parametrosConfiguracion.put("precision", "alta");
    }
    
    @Override
    public String mostrarDetalles() {
        StringBuilder parametros = new StringBuilder();
        for (Map.Entry<String, Object> entry : parametrosConfiguracion.entrySet()) {
            parametros.append(String.format("  • %s: %s\n", entry.getKey(), entry.getValue()));
        }
        
        return String.format(
            "=== DETALLES DEL SIMULADOR DE FÍSICA ===\n" +
            "ID: %s\n" +
            "Nombre: %s\n" +
            "Fabricante: %s\n" +
            "Consumo Eléctrico: %.2f W\n" +
            "Tipo de Simulación: %s\n" +
            "Algoritmo de Simulación: %s\n" +
            "Estado de Simulación: %s\n" +
            "Parámetros de Configuración:\n%s" +
            "Características: %s\n" +
            "=========================================",
            id, nombre, fabricante, consumoElectrico, tipoSimulacion, 
            algoritmoSimulacion, estadoSimulacion, parametros.toString(), resumenCaracteristicas
        );
    }
    
    @Override
    public void iniciarSimulacion() {
        if ("Detenida".equals(estadoSimulacion)) {
            estadoSimulacion = "Iniciada";
            // Simular inicio de simulación
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                estadoSimulacion = "Error";
            }
        }
    }
    
    @Override
    public void detenerSimulacion() {
        if ("Iniciada".equals(estadoSimulacion) || "Pausada".equals(estadoSimulacion)) {
            estadoSimulacion = "Detenida";
        }
    }
    
    @Override
    public void configurarParametros(Map<String, Object> parametros) {
        if (parametros != null) {
            this.parametrosConfiguracion.putAll(parametros);
        }
    }
    
    @Override
    public String getEstadoSimulacion() {
        return estadoSimulacion;
    }
    
    /**
     * Pausa la simulación en curso
     */
    public void pausarSimulacion() {
        if ("Iniciada".equals(estadoSimulacion)) {
            estadoSimulacion = "Pausada";
        }
    }
    
    /**
     * Reanuda una simulación pausada
     */
    public void reanudarSimulacion() {
        if ("Pausada".equals(estadoSimulacion)) {
            estadoSimulacion = "Iniciada";
        }
    }
    
    /**
     * Obtiene los resultados de la simulación
     * @return String con los resultados simulados
     */
    public String obtenerResultados() {
        if ("Detenida".equals(estadoSimulacion)) {
            return "Simulación completada. Resultados disponibles para análisis.";
        } else {
            return "Simulación en curso. Resultados no disponibles aún.";
        }
    }
    
    // Getters específicos
    public String getTipoSimulacion() { return tipoSimulacion; }
    public String getAlgoritmoSimulacion() { return algoritmoSimulacion; }
    public Map<String, Object> getParametrosConfiguracion() { 
        return new HashMap<>(parametrosConfiguracion); 
    }
}
