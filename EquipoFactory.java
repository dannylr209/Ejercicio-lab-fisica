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
 * Factory para la creación de diferentes tipos de equipos.
 * Implementa el patrón Factory Method para centralizar la creación de objetos.
 * 
 * @author Sistema de Laboratorio de Física
 * @version 1.0
 */
public class EquipoFactory {
    
    /**
     * Crea un péndulo con encoder
     */
    public static PenduloEncoder crearPendulo(String id, String nombre, String fabricante, 
                                            double consumoElectrico, String resumenCaracteristicas,
                                            double longitudBrazo, int resolucionEncoder, 
                                            double frecuenciaMuestreo) {
        return new PenduloEncoder(id, nombre, fabricante, consumoElectrico, 
                                resumenCaracteristicas, longitudBrazo, resolucionEncoder, frecuenciaMuestreo);
    }
    
    /**
     * Crea una fotopuerta
     */
    public static Fotopuerta crearFotopuerta(String id, String nombre, String fabricante,
                                           double consumoElectrico, String resumenCaracteristicas,
                                           double tiempoRespuesta, String tipoSensor, 
                                           double rangoDeteccion) {
        return new Fotopuerta(id, nombre, fabricante, consumoElectrico, 
                            resumenCaracteristicas, tiempoRespuesta, tipoSensor, rangoDeteccion);
    }
    
    /**
     * Crea un osciloscopio
     */
    public static Osciloscopio crearOsciloscopio(String id, String nombre, String fabricante,
                                               double consumoElectrico, String resumenCaracteristicas,
                                               int numeroCanales, double frecuenciaMuestreoMax,
                                               double voltajeMaximo, String resolucion) {
        return new Osciloscopio(id, nombre, fabricante, consumoElectrico, 
                              resumenCaracteristicas, numeroCanales, frecuenciaMuestreoMax,
                              voltajeMaximo, resolucion);
    }
    
    /**
     * Crea un generador de señales
     */
    public static Generador crearGenerador(String id, String nombre, String fabricante,
                                         double consumoElectrico, String resumenCaracteristicas,
                                         String tipoOndas, double frecuenciaMaxima,
                                         double amplitudMaxima, String modoOperacion) {
        return new Generador(id, nombre, fabricante, consumoElectrico, 
                           resumenCaracteristicas, tipoOndas, frecuenciaMaxima,
                           amplitudMaxima, modoOperacion);
    }
    
    /**
     * Crea un simulador de física
     */
    public static SimuladorFisica crearSimulador(String id, String nombre, String fabricante,
                                               double consumoElectrico, String resumenCaracteristicas,
                                               String tipoSimulacion, String algoritmoSimulacion) {
        return new SimuladorFisica(id, nombre, fabricante, consumoElectrico, 
                                 resumenCaracteristicas, tipoSimulacion, algoritmoSimulacion);
    }
}
