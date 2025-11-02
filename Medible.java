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

/**
 * Interfaz para equipos que pueden realizar mediciones.
 * Define las operaciones básicas de medición y configuración de rangos.
 * 
 * @author Daniel López - 242159
 * @version 2.0
 */
public interface Medible {
    
    /**
     * Realiza una medición con el equipo
     * @return double con el valor medido
     */
    double realizarMedicion();
    
    /**
     * Configura el rango de medición del equipo
     * @param min valor mínimo del rango
     * @param max valor máximo del rango
     */
    void configurarRango(double min, double max);
    
    /**
     * Obtiene información sobre los rangos de operación
     * @return String con los rangos disponibles
     */
    String getRangos();
}
