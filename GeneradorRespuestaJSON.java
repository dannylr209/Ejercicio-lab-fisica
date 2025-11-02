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

import java.util.List;

/**
 * Implementación de generador de respuestas JSON.
 * Cumple con SRP: Solo se encarga de generar respuestas JSON
 * Cumple con OCP: Extensible para nuevos tipos de respuesta
 * 
 * @author Sistema de Laboratorio de Física
 * @version 2.0 - Refactorizado para SOLID
 */
public class GeneradorRespuestaJSON implements GeneradorRespuesta {
    
    @Override
    public String generarRespuestaExito(String mensaje, Object data) {
        if (data == null) {
            return String.format(
                "{\"success\":true,\"message\":\"%s\",\"data\":null}",
                escaparJSON(mensaje)
            );
        }
        
        if (data instanceof String) {
            return String.format(
                "{\"success\":true,\"message\":\"%s\",\"data\":%s}",
                escaparJSON(mensaje), data
            );
        }
        
        return String.format(
            "{\"success\":true,\"message\":\"%s\",\"data\":\"%s\"}",
            escaparJSON(mensaje), escaparJSON(data.toString())
        );
    }
    
    @Override
    public String generarRespuestaError(String mensaje) {
        return String.format(
            "{\"success\":false,\"message\":\"%s\",\"data\":null}",
            escaparJSON(mensaje)
        );
    }
    
    @Override
    public String generarRespuestaLista(String mensaje, List<?> items) {
        if (items == null || items.isEmpty()) {
            return generarRespuestaError("No se encontraron elementos");
        }
        
        StringBuilder json = new StringBuilder();
        json.append(String.format(
            "{\"success\":true,\"message\":\"%s (%d elemento(s))\",\"data\":[",
            escaparJSON(mensaje), items.size()
        ));
        
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof Representable) {
                json.append(((Representable) item).toJSON());
            } else {
                json.append("\"").append(escaparJSON(item.toString())).append("\"");
            }
            
            if (i < items.size() - 1) {
                json.append(",");
            }
        }
        
        json.append("]}");
        return json.toString();
    }
    
    /**
     * Escapa caracteres especiales para JSON
     * @param texto texto a escapar
     * @return texto escapado
     */
    private String escaparJSON(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
