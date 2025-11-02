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
 * Clase que representa un péndulo con encoder para mediciones de movimiento pendular.
 * Implementa las interfaces Calibrable y Medible.
 * 
 * @author Daniel López - 242159
 * @version 2.0
 */
public class PenduloEncoder extends EquipoBase implements Calibrable, Medible {
    
    private double longitudBrazo;
    private int resolucionEncoder;
    private double frecuenciaMuestreo;
    private LocalDate ultimaCalibracion;
    private String estadoCalibracion;
    private double rangoMin;
    private double rangoMax;
    private Random random;
    
    /**
     * Constructor para PenduloEncoder
     */
    public PenduloEncoder(String id, String nombre, String fabricante, double consumoElectrico,
                         String resumenCaracteristicas, double longitudBrazo, 
                         int resolucionEncoder, double frecuenciaMuestreo) {
        super(id, nombre, "Péndulo con Encoder", fabricante, consumoElectrico, resumenCaracteristicas);
        this.longitudBrazo = longitudBrazo;
        this.resolucionEncoder = resolucionEncoder;
        this.frecuenciaMuestreo = frecuenciaMuestreo;
        this.ultimaCalibracion = LocalDate.now().minusDays(15);
        this.estadoCalibracion = "Calibrado";
        this.rangoMin = 0.0;
        this.rangoMax = 360.0;
        this.random = new Random();
    }
    
    @Override
    public String mostrarDetalles() {
        return String.format(
            "=== DETALLES DEL PÉNDULO CON ENCODER ===\n" +
            "ID: %s\n" +
            "Nombre: %s\n" +
            "Fabricante: %s\n" +
            "Consumo Eléctrico: %.2f W\n" +
            "Longitud del Brazo: %.2f m\n" +
            "Resolución del Encoder: %d pulsos/revolución\n" +
            "Frecuencia de Muestreo: %.1f Hz\n" +
            "Estado de Calibración: %s\n" +
            "Última Calibración: %s\n" +
            "Rango de Medición: %.1f° - %.1f°\n" +
            "Características: %s\n" +
            "=========================================",
            id, nombre, fabricante, consumoElectrico, longitudBrazo, 
            resolucionEncoder, frecuenciaMuestreo, estadoCalibracion, 
            ultimaCalibracion, rangoMin, rangoMax, resumenCaracteristicas
        );
    }
    
    @Override
    public void calibrar() {
        ultimaCalibracion = LocalDate.now();
        estadoCalibracion = "Calibrado";
        // Simulación del proceso de calibración
        try {
            Thread.sleep(100); // Simula tiempo de calibración
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getEstadoCalibracion() {
        // Verificar si necesita calibración (más de 30 días)
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
        // Simula una medición de ángulo del péndulo
        double angulo = rangoMin + (rangoMax - rangoMin) * random.nextDouble();
        return Math.round(angulo * 100.0) / 100.0; // Redondear a 2 decimales
    }
    
    @Override
    public void configurarRango(double min, double max) {
        if (min >= 0 && max <= 360 && min < max) {
            this.rangoMin = min;
            this.rangoMax = max;
        } else {
            throw new IllegalArgumentException("Rango inválido para péndulo (0° - 360°)");
        }
    }
    
    @Override
    public String getRangos() {
        return String.format("Rango configurado: %.1f° - %.1f° | Rango máximo: 0° - 360°", 
                           rangoMin, rangoMax);
    }
    
    // Getters específicos
    public double getLongitudBrazo() { return longitudBrazo; }
    public int getResolucionEncoder() { return resolucionEncoder; }
    public double getFrecuenciaMuestreo() { return frecuenciaMuestreo; }
}
