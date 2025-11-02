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

import java.time.LocalDate;

/**
 * Interfaz para equipos que requieren calibración periódica.
 * Define las operaciones básicas de calibración y estado.
 * 
 * @author Daniel López - 242159
 * @version 2.0
 */
public interface Calibrable {
    
    /**
     * Realiza la calibración del equipo
     */
    void calibrar();
    
    /**
     * Obtiene el estado actual de calibración
     * @return String con el estado (ej: "Calibrado", "Requiere Calibración", "En Mantenimiento")
     */
    String getEstadoCalibracion();
    
    /**
     * Obtiene la fecha de la última calibración
     * @return LocalDate con la fecha de última calibración
     */
    LocalDate getUltimaCalibracion();
}
