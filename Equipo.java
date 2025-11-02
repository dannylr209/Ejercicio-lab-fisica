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
 * Interfaz principal que define el contrato común para todos los equipos del laboratorio.
 * Cumple con ISP: Extiende interfaces específicas en lugar de ser monolítica.
 * 
 * @author Daniel López - 242159
 * @version 2.0 - Refactorizado para SOLID
 */
public interface Equipo extends EquipoInfo, Representable, Comparable<Equipo> {
    // Esta interfaz combina las capacidades necesarias sin duplicar métodos
}
