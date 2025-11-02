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
 * Clase que representa un osciloscopio para medición de señales eléctricas.
 * Implementa las interfaces Calibrable y Medible.
 * 
 * @author Sistema de Laboratorio de Física
 * @version 1.0
 */
public class Osciloscopio extends EquipoBase implements Calibrable, Medible {
    
    private int numeroCanales;
    private double frecuenciaMuestreoMax;
    private double voltajeMaximo;
    private String resolucion;
    private LocalDate ultimaCalibracion;
    private String estadoCalibracion;
    private double rangoMin;
    private double rangoMax;
    private Random random;
    
    /**
     * Constructor para Osciloscopio
     */
    public Osciloscopio(String id, String nombre, String fabricante, double consumoElectrico,
                       String resumenCaracteristicas, int numeroCanales, 
                       double frecuenciaMuestreoMax, double voltajeMaximo, String resolucion) {
        super(id, nombre, "Osciloscopio", fabricante, consumoElectrico, resumenCaracteristicas);
        this.numeroCanales = numeroCanales;
        this.frecuenciaMuestreoMax = frecuenciaMuestreoMax;
        this.voltajeMaximo = voltajeMaximo;
        this.resolucion = resolucion;
        this.ultimaCalibracion = LocalDate.now().minusDays(5);
        this.estadoCalibracion = "Calibrado";
        this.rangoMin = -voltajeMaximo;
        this.rangoMax = voltajeMaximo;
        this.random = new Random();
    }
    
    @Override
    public String mostrarDetalles() {
        return String.format(
            "=== DETALLES DEL OSCILOSCOPIO ===\n" +
            "ID: %s\n" +
            "Nombre: %s\n" +
            "Fabricante: %s\n" +
            "Consumo Eléctrico: %.2f W\n" +
            "Número de Canales: %d\n" +
            "Frecuencia de Muestreo Máxima: %.1f MHz\n" +
            "Voltaje Máximo: %.1f V\n" +
            "Resolución: %s\n" +
            "Estado de Calibración: %s\n" +
            "Última Calibración: %s\n" +
            "Rango de Medición: %.1f V - %.1f V\n" +
            "Características: %s\n" +
            "==================================",
            id, nombre, fabricante, consumoElectrico, numeroCanales, 
            frecuenciaMuestreoMax, voltajeMaximo, resolucion, estadoCalibracion, 
            ultimaCalibracion, rangoMin, rangoMax, resumenCaracteristicas
        );
    }
    
    @Override
    public void calibrar() {
        ultimaCalibracion = LocalDate.now();
        estadoCalibracion = "Calibrado";
        // Simular calibración de todos los canales
        try {
            Thread.sleep(200);
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
        // Simula medición de voltaje
        double voltaje = rangoMin + (rangoMax - rangoMin) * random.nextDouble();
        return Math.round(voltaje * 100.0) / 100.0; // Redondear a 2 decimales
    }
    
    @Override
    public void configurarRango(double min, double max) {
        if (min >= -voltajeMaximo && max <= voltajeMaximo && min < max) {
            this.rangoMin = min;
            this.rangoMax = max;
        } else {
            throw new IllegalArgumentException(
                String.format("Rango inválido para osciloscopio (%.1fV - %.1fV)", 
                             -voltajeMaximo, voltajeMaximo));
        }
    }
    
    @Override
    public String getRangos() {
        return String.format("Rango configurado: %.1f V - %.1f V | Rango máximo: %.1f V - %.1f V", 
                           rangoMin, rangoMax, -voltajeMaximo, voltajeMaximo);
    }
    
    // Getters específicos
    public int getNumeroCanales() { return numeroCanales; }
    public double getFrecuenciaMuestreoMax() { return frecuenciaMuestreoMax; }
    public double getVoltajeMaximo() { return voltajeMaximo; }
    public String getResolucion() { return resolucion; }
}
