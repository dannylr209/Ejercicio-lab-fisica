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

import java.util.Objects;

/**
 * Clase base abstracta que implementa la funcionalidad común para todos los equipos.
 * Proporciona implementación básica de la interfaz Equipo y Comparable.
 * 
 * @author Daniel López - 242159
 * @version 2.0 - Refactorizado para SOLID
 */
public abstract class EquipoBase implements Equipo {
    
    protected String id;
    protected String nombre;
    protected String tipo;
    protected String fabricante;
    protected double consumoElectrico;
    protected String resumenCaracteristicas;
    
    /**
     * Constructor para la clase base
     * @param id identificador único del equipo
     * @param nombre nombre del equipo
     * @param tipo tipo de equipo
     * @param fabricante fabricante del equipo
     * @param consumoElectrico consumo eléctrico en watts
     * @param resumenCaracteristicas resumen de características
     */
    public EquipoBase(String id, String nombre, String tipo, String fabricante, 
                     double consumoElectrico, String resumenCaracteristicas) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fabricante = fabricante;
        this.consumoElectrico = consumoElectrico;
        this.resumenCaracteristicas = resumenCaracteristicas;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String getTipo() {
        return tipo;
    }
    
    @Override
    public String getFabricante() {
        return fabricante;
    }
    
    @Override
    public double getConsumoElectrico() {
        return consumoElectrico;
    }
    
    @Override
    public String getResumenCaracteristicas() {
        return resumenCaracteristicas;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %s - %.2fW", 
                           id, nombre, tipo, fabricante, consumoElectrico);
    }
    
    @Override
    public String toJSON() {
        return String.format(
            "{\"id\":\"%s\",\"nombre\":\"%s\",\"tipo\":\"%s\",\"fabricante\":\"%s\"," +
            "\"consumoElectrico\":%.2f,\"resumenCaracteristicas\":\"%s\",\"detalles\":\"%s\"}",
            id, nombre, tipo, fabricante, consumoElectrico, 
            resumenCaracteristicas.replace("\"", "\\\""),
            mostrarDetalles().replace("\"", "\\\"").replace("\n", "\\n")
        );
    }
    
    /**
     * Compara equipos por consumo eléctrico para ordenamiento
     * @param otro equipo a comparar
     * @return int resultado de la comparación
     */
    @Override
    public int compareTo(Equipo otro) {
        return Double.compare(this.consumoElectrico, otro.getConsumoElectrico());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EquipoBase that = (EquipoBase) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Método abstracto que debe ser implementado por las clases hijas
     * para mostrar detalles específicos de cada tipo de equipo
     */
    @Override
    public abstract String mostrarDetalles();
}
