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
 * Clase que representa un generador de señales eléctricas.
 * Implementa la interfaz Calibrable.
 * 
 * @author Sistema de Laboratorio de Física
 * @version 1.0
 */
public class Generador extends EquipoBase implements Calibrable {
    
    private String tipoOndas;
    private double frecuenciaMaxima;
    private double amplitudMaxima;
    private String modoOperacion;
    private LocalDate ultimaCalibracion;
    private String estadoCalibracion;
    
    /**
     * Constructor para Generador
     */
    public Generador(String id, String nombre, String fabricante, double consumoElectrico,
                    String resumenCaracteristicas, String tipoOndas, 
                    double frecuenciaMaxima, double amplitudMaxima, String modoOperacion) {
        super(id, nombre, "Generador de Señales", fabricante, consumoElectrico, resumenCaracteristicas);
        this.tipoOndas = tipoOndas;
        this.frecuenciaMaxima = frecuenciaMaxima;
        this.amplitudMaxima = amplitudMaxima;
        this.modoOperacion = modoOperacion;
        this.ultimaCalibracion = LocalDate.now().minusDays(20);
        this.estadoCalibracion = "Calibrado";
    }
    
    @Override
    public String mostrarDetalles() {
        return String.format(
            "=== DETALLES DEL GENERADOR DE SEÑALES ===\n" +
            "ID: %s\n" +
            "Nombre: %s\n" +
            "Fabricante: %s\n" +
            "Consumo Eléctrico: %.2f W\n" +
            "Tipos de Ondas: %s\n" +
            "Frecuencia Máxima: %.1f MHz\n" +
            "Amplitud Máxima: %.1f V\n" +
            "Modo de Operación: %s\n" +
            "Estado de Calibración: %s\n" +
            "Última Calibración: %s\n" +
            "Características: %s\n" +
            "==========================================",
            id, nombre, fabricante, consumoElectrico, tipoOndas, 
            frecuenciaMaxima, amplitudMaxima, modoOperacion, estadoCalibracion, 
            ultimaCalibracion, resumenCaracteristicas
        );
    }
    
    @Override
    public void calibrar() {
        ultimaCalibracion = LocalDate.now();
        estadoCalibracion = "Calibrado";
        // Simular calibración de frecuencia y amplitud
        try {
            Thread.sleep(180);
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
    
    /**
     * Genera una señal con parámetros específicos
     * @param frecuencia frecuencia deseada en Hz
     * @param amplitud amplitud deseada en V
     * @param tipoOnda tipo de onda (senoidal, cuadrada, triangular)
     * @return String con la confirmación de generación
     */
    public String generarSenal(double frecuencia, double amplitud, String tipoOnda) {
        if (frecuencia > frecuenciaMaxima || amplitud > amplitudMaxima) {
            return "Error: Parámetros fuera del rango permitido";
        }
        return String.format("Generando señal %s de %.2f Hz con amplitud %.2f V", 
                           tipoOnda, frecuencia, amplitud);
    }
    
    // Getters específicos
    public String getTipoOndas() { return tipoOndas; }
    public double getFrecuenciaMaxima() { return frecuenciaMaxima; }
    public double getAmplitudMaxima() { return amplitudMaxima; }
    public String getModoOperacion() { return modoOperacion; }
}
