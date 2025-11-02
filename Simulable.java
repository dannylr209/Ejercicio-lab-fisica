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

import java.util.Map;

/**
 * Interfaz para equipos que pueden realizar simulaciones.
 * Define las operaciones básicas de control de simulación.
 * 
 * @author Daniel López - 242159
 * @version 2.0
 */
public interface Simulable {
    
    /**
     * Inicia la simulación del equipo
     */
    void iniciarSimulacion();
    
    /**
     * Detiene la simulación en curso
     */
    void detenerSimulacion();
    
    /**
     * Configura los parámetros de la simulación
     * @param parametros Map con los parámetros y sus valores
     */
    void configurarParametros(Map<String, Object> parametros);
    
    /**
     * Obtiene el estado actual de la simulación
     * @return String con el estado (ej: "Iniciada", "Detenida", "Pausada", "Error")
     */
    String getEstadoSimulacion();
}
