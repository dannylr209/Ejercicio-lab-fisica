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
import java.util.Random;

/**
 * Clase que representa una fotopuerta para medición de velocidad y tiempo.
 * Implementa las interfaces Calibrable y Medible.
 * 
 * @author Daniel López - 242159
 * @version 2.0
 */
public class Fotopuerta extends EquipoBase implements Calibrable, Medible {
    
    private double tiempoRespuesta;
    private String tipoSensor;
    private double rangoDeteccion;
    private LocalDate ultimaCalibracion;
    private String estadoCalibracion;
    private double rangoMin;
    private double rangoMax;
    private Random random;
    
    /**
     * Constructor para Fotopuerta
     */
    public Fotopuerta(String id, String nombre, String fabricante, double consumoElectrico,
                     String resumenCaracteristicas, double tiempoRespuesta, 
                     String tipoSensor, double rangoDeteccion) {
        super(id, nombre, "Fotopuerta", fabricante, consumoElectrico, resumenCaracteristicas);
        this.tiempoRespuesta = tiempoRespuesta;
        this.tipoSensor = tipoSensor;
        this.rangoDeteccion = rangoDeteccion;
        this.ultimaCalibracion = LocalDate.now().minusDays(10);
        this.estadoCalibracion = "Calibrado";
        this.rangoMin = 0.001; // 1ms mínimo
        this.rangoMax = 10.0;  // 10s máximo
        this.random = new Random();
    }
    
    @Override
    public String mostrarDetalles() {
        return String.format(
            "=== DETALLES DE LA FOTOPUERTA ===\n" +
            "ID: %s\n" +
            "Nombre: %s\n" +
            "Fabricante: %s\n" +
            "Consumo Eléctrico: %.2f W\n" +
            "Tiempo de Respuesta: %.3f ms\n" +
            "Tipo de Sensor: %s\n" +
            "Rango de Detección: %.2f m\n" +
            "Estado de Calibración: %s\n" +
            "Última Calibración: %s\n" +
            "Rango de Medición: %.3f s - %.1f s\n" +
            "Características: %s\n" +
            "==================================",
            id, nombre, fabricante, consumoElectrico, tiempoRespuesta, 
            tipoSensor, rangoDeteccion, estadoCalibracion, 
            ultimaCalibracion, rangoMin, rangoMax, resumenCaracteristicas
        );
    }
    
    @Override
    public void calibrar() {
        ultimaCalibracion = LocalDate.now();
        estadoCalibracion = "Calibrado";
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getEstadoCalibracion() {
        if (LocalDate.now().minusDays(30).isAfter(ultimaCalibracion)) {
            estadoCalibracion = "Requiere Calibración";
        }
        return estadoCalibracion;
    }
    
    @Override
    public LocalDate getUltimaCalibracion() {
        return ultimaCalibracion;
    }
    
    @Override
    public double realizarMedicion() {
        double tiempo = rangoMin + (rangoMax - rangoMin) * random.nextDouble();
        return Math.round(tiempo * 1000.0) / 1000.0;
    }
    
    @Override
    public void configurarRango(double min, double max) {
        if (min >= 0.001 && max <= 10.0 && min < max) {
            this.rangoMin = min;
            this.rangoMax = max;
        } else {
            throw new IllegalArgumentException("Rango inválido para fotopuerta (0.001s - 10s)");
        }
    }
    
    @Override
    public String getRangos() {
        return String.format("Rango configurado: %.3f s - %.1f s | Rango máximo: 0.001s - 10s", 
                           rangoMin, rangoMax);
    }
    
    // Getters específicos
    public double getTiempoRespuesta() { return tiempoRespuesta; }
    public String getTipoSensor() { return tipoSensor; }
    public double getRangoDeteccion() { return rangoDeteccion; }
}
