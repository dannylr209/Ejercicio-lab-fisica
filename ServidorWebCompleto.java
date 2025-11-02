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

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.List;

/**
 * Servidor web completo que sirve HTML directamente sin JavaScript.
 * Cumple con SRP: Solo se encarga de manejar HTTP y generar HTML
 * Integra con el controlador para mostrar datos dinámicamente
 * 
 * @author Daniel López - 242159
 * @version 2.0 - Sin JavaScript
 */
public class ServidorWebCompleto {
    
    private final ControladorWeb controlador;
    private final int puerto;
    private boolean activo;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    
    /**
     * Constructor del servidor web completo
     * @param puerto puerto en el que escuchará el servidor
     * @param controlador controlador web a utilizar
     */
    public ServidorWebCompleto(int puerto, ControladorWeb controlador) {
        this.puerto = puerto;
        this.controlador = controlador;
        this.activo = false;
        this.threadPool = Executors.newFixedThreadPool(10);
    }
    
    /**
     * Inicia el servidor web
     */
    public void iniciar() {
        try {
            controlador.inicializarSistema();
            serverSocket = new ServerSocket(puerto);
            activo = true;
            
            System.out.println("🚀 Servidor Web iniciado en http://localhost:" + puerto);
            System.out.println("📊 Equipos cargados: " + CatalogoEquipos.getInstance().obtenerCantidad());
            System.out.println("🌐 Abra su navegador en: http://localhost:" + puerto);
            System.out.println();
            
            while (activo) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    threadPool.submit(() -> manejarCliente(clienteSocket));
                } catch (IOException e) {
                    if (activo) {
                        System.err.println("Error al aceptar conexión: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Detiene el servidor
     */
    public void detener() {
        activo = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
        } catch (IOException e) {
            System.err.println("Error al detener servidor: " + e.getMessage());
        }
    }
    
    /**
     * Maneja las solicitudes de un cliente
     * @param clienteSocket socket del cliente
     */
    private void manejarCliente(Socket clienteSocket) {
        try (BufferedReader entrada = new BufferedReader(
                new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter salida = new PrintWriter(
                clienteSocket.getOutputStream(), true)) {
            
            String linea = entrada.readLine();
            if (linea == null) return;
            
            String[] partesRequest = linea.split(" ");
            if (partesRequest.length < 2) return;
            
            String metodo = partesRequest[0];
            String ruta = partesRequest[1];
            
            // Leer headers
            while ((linea = entrada.readLine()) != null && !linea.isEmpty()) {
                // Procesar headers si es necesario
            }
            
            String respuesta = procesarSolicitud(metodo, ruta);
            salida.print(respuesta);
            salida.flush();
            
        } catch (IOException e) {
            System.err.println("Error al manejar cliente: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar socket: " + e.getMessage());
            }
        }
    }
    
    /**
     * Procesa las solicitudes HTTP
     * @param metodo método HTTP
     * @param ruta ruta solicitada
     * @return respuesta HTTP completa
     */
    private String procesarSolicitud(String metodo, String ruta) {
        try {
            if (!"GET".equals(metodo)) {
                return crearRespuestaError(405, "Método no permitido");
            }
            
            switch (ruta) {
                case "/":
                case "/index.html":
                    return servirPaginaPrincipal();
                case "/equipos":
                    return servirListadoEquipos();
                case "/ordenar":
                    return servirEquiposOrdenados();
                case "/styles.css":
                    return servirCSS();
                case "/favicon.ico":
                    return crearRespuesta404();
                default:
                    if (ruta.startsWith("/buscar/id/")) {
                        String id = ruta.substring("/buscar/id/".length());
                        return servirBusquedaPorId(id);
                    } else if (ruta.startsWith("/buscar/nombre/")) {
                        String nombre = ruta.substring("/buscar/nombre/".length());
                        nombre = URLDecoder.decode(nombre, "UTF-8");
                        return servirBusquedaPorNombre(nombre);
                    } else if (ruta.startsWith("/detalles/")) {
                        String id = ruta.substring("/detalles/".length());
                        return servirDetallesEquipo(id);
                    } else {
                        return crearRespuesta404();
                    }
            }
            
        } catch (Exception e) {
            return crearRespuesta500(e.getMessage());
        }
    }
    
    /**
     * Sirve la página principal con menú
     */
    private String servirPaginaPrincipal() {
        String html = generarPaginaPrincipal();
        return crearRespuestaHTML(html);
    }
    
    /**
     * Sirve el listado completo de equipos
     */
    private String servirListadoEquipos() {
        List<Equipo> equipos = CatalogoEquipos.getInstance().obtenerTodos();
        String html = generarPaginaEquipos(equipos, "Listado Completo de Equipos", "/");
        return crearRespuestaHTML(html);
    }
    
    /**
     * Sirve los equipos ordenados por consumo
     */
    private String servirEquiposOrdenados() {
        CatalogoEquipos.getInstance().ordenarPorConsumo();
        List<Equipo> equipos = CatalogoEquipos.getInstance().obtenerTodos();
        String html = generarPaginaEquipos(equipos, "Equipos Ordenados por Consumo Eléctrico", "/");
        return crearRespuestaHTML(html);
    }
    
    /**
     * Sirve búsqueda por ID
     */
    private String servirBusquedaPorId(String id) {
        Equipo equipo = CatalogoEquipos.getInstance().buscarPorId(id);
        if (equipo != null) {
            List<Equipo> equipos = List.of(equipo);
            String html = generarPaginaEquipos(equipos, "Resultado de Búsqueda por ID: " + id, "/");
            return crearRespuestaHTML(html);
        } else {
            String html = generarPaginaError("No se encontró equipo con ID: " + id, "/");
            return crearRespuestaHTML(html);
        }
    }
    
    /**
     * Sirve búsqueda por nombre
     */
    private String servirBusquedaPorNombre(String nombre) {
        List<Equipo> equipos = CatalogoEquipos.getInstance().buscarPorNombre(nombre);
        if (!equipos.isEmpty()) {
            String html = generarPaginaEquipos(equipos, "Resultado de Búsqueda por Nombre: " + nombre, "/");
            return crearRespuestaHTML(html);
        } else {
            String html = generarPaginaError("No se encontraron equipos con nombre: " + nombre, "/");
            return crearRespuestaHTML(html);
        }
    }
    
    /**
     * Sirve detalles de un equipo específico
     */
    private String servirDetallesEquipo(String id) {
        Equipo equipo = CatalogoEquipos.getInstance().buscarPorId(id);
        if (equipo != null) {
            String html = generarPaginaDetalles(equipo);
            return crearRespuestaHTML(html);
        } else {
            String html = generarPaginaError("No se encontró equipo con ID: " + id, "/");
            return crearRespuestaHTML(html);
        }
    }
    
    /**
     * Genera la página principal con menú
     */
    private String generarPaginaPrincipal() {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"es\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Sistema de Laboratorio de Física - UVG</title>\n" +
               "    <link rel=\"stylesheet\" href=\"/styles.css\">\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <header>\n" +
               "            <h1>🔬 Sistema de Laboratorio de Física</h1>\n" +
               "            <p>Universidad del Valle de Guatemala - Gestión de Equipos</p>\n" +
               "            <p><strong>Desarrollado por:</strong> Daniel López - 242159</p>\n" +
               "        </header>\n" +
               "\n" +
               "        <nav class=\"menu\">\n" +
               "            <a href=\"/equipos\" class=\"btn-primary\">📋 Ver Todos los Equipos</a>\n" +
               "            <a href=\"/ordenar\" class=\"btn-accent\">⚡ Ordenar por Consumo</a>\n" +
               "        </nav>\n" +
               "\n" +
               "        <section class=\"busqueda\">\n" +
               "            <h3>🔍 Buscar Equipos</h3>\n" +
               "            \n" +
               "            <div class=\"form-group\">\n" +
               "                <h4>Buscar por ID:</h4>\n" +
               "                <form method=\"get\" style=\"display: flex; gap: 1rem; align-items: center;\">\n" +
               "                    <input type=\"text\" name=\"id\" placeholder=\"Ejemplo: PEN001\" required style=\"flex: 1; padding: 10px; border: 2px solid #bdc3c7; border-radius: 8px;\">\n" +
               "                    <button type=\"button\" class=\"btn-secondary\" onclick=\"buscarPorId(this.form)\">Buscar</button>\n" +
               "                </form>\n" +
               "            </div>\n" +
               "            \n" +
               "            <div class=\"form-group\">\n" +
               "                <h4>Buscar por Nombre:</h4>\n" +
               "                <form method=\"get\" style=\"display: flex; gap: 1rem; align-items: center;\">\n" +
               "                    <input type=\"text\" name=\"nombre\" placeholder=\"Ejemplo: Péndulo\" required style=\"flex: 1; padding: 10px; border: 2px solid #bdc3c7; border-radius: 8px;\">\n" +
               "                    <button type=\"button\" class=\"btn-secondary\" onclick=\"buscarPorNombre(this.form)\">Buscar</button>\n" +
               "                </form>\n" +
               "            </div>\n" +
               "        </section>\n" +
               "\n" +
               "        <section class=\"info\">\n" +
               "            <h3>📊 Información del Sistema</h3>\n" +
               "            <div class=\"estadisticas\">\n" +
               "                <div class=\"stat-item\">\n" +
               "                    <strong>Total de Equipos:</strong> " + CatalogoEquipos.getInstance().obtenerCantidad() + "\n" +
               "                </div>\n" +
               "                <div class=\"stat-item\">\n" +
               "                    <strong>Tipos Disponibles:</strong> Péndulos, Fotopuertas, Osciloscopios, Generadores, Simuladores\n" +
               "                </div>\n" +
               "                <div class=\"stat-item\">\n" +
               "                    <strong>Principios Implementados:</strong> SOLID, MVC, Factory, Singleton\n" +
               "                </div>\n" +
               "            </div>\n" +
               "        </section>\n" +
               "\n" +
               "        <footer>\n" +
               "            <p>&copy; 2025 Universidad del Valle de Guatemala - CC2008 Programación Orientada a Objetos</p>\n" +
               "        </footer>\n" +
               "    </div>\n" +
               "\n" +
               "    <script>\n" +
               "        function buscarPorId(form) {\n" +
               "            const id = form.id.value.trim();\n" +
               "            if (id) {\n" +
               "                window.location.href = '/buscar/id/' + encodeURIComponent(id);\n" +
               "            }\n" +
               "        }\n" +
               "        \n" +
               "        function buscarPorNombre(form) {\n" +
               "            const nombre = form.nombre.value.trim();\n" +
               "            if (nombre) {\n" +
               "                window.location.href = '/buscar/nombre/' + encodeURIComponent(nombre);\n" +
               "            }\n" +
               "        }\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Genera página con listado de equipos
     */
    private String generarPaginaEquipos(List<Equipo> equipos, String titulo, String volverUrl) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"es\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>").append(titulo).append(" - Sistema de Laboratorio</title>\n")
            .append("    <link rel=\"stylesheet\" href=\"/styles.css\">\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <div class=\"container\">\n")
            .append("        <header>\n")
            .append("            <h1>🔬 ").append(titulo).append("</h1>\n")
            .append("            <p>Universidad del Valle de Guatemala</p>\n")
            .append("        </header>\n")
            .append("\n")
            .append("        <nav class=\"menu\">\n")
            .append("            <a href=\"").append(volverUrl).append("\" class=\"btn-secondary\">🏠 Volver al Menú Principal</a>\n")
            .append("            <a href=\"/equipos\" class=\"btn-primary\">📋 Ver Todos</a>\n")
            .append("            <a href=\"/ordenar\" class=\"btn-accent\">⚡ Ordenar por Consumo</a>\n")
            .append("        </nav>\n")
            .append("\n")
            .append("        <section class=\"resultados\">\n")
            .append("            <div class=\"mensaje exito\">\n")
            .append("                ✅ Se encontraron ").append(equipos.size()).append(" equipo(s)\n")
            .append("            </div>\n")
            .append("\n");
        
        for (Equipo equipo : equipos) {
            html.append("            <div class=\"equipo-card\">\n")
                .append("                <div class=\"equipo-header\">\n")
                .append("                    <span class=\"equipo-id\">").append(equipo.getId()).append("</span>\n")
                .append("                    <span class=\"equipo-consumo\">⚡ ").append(String.format("%.2f", equipo.getConsumoElectrico())).append("W</span>\n")
                .append("                </div>\n")
                .append("                <div class=\"equipo-nombre\">").append(escapeHtml(equipo.getNombre())).append("</div>\n")
                .append("                <div class=\"equipo-info\">\n")
                .append("                    <div class=\"equipo-detalle\">\n")
                .append("                        <strong>🏷️ Tipo:</strong>\n")
                .append("                        ").append(escapeHtml(equipo.getTipo())).append("\n")
                .append("                    </div>\n")
                .append("                    <div class=\"equipo-detalle\">\n")
                .append("                        <strong>🏭 Fabricante:</strong>\n")
                .append("                        ").append(escapeHtml(equipo.getFabricante())).append("\n")
                .append("                    </div>\n")
                .append("                    <div class=\"equipo-detalle\">\n")
                .append("                        <strong>📋 Características:</strong>\n")
                .append("                        ").append(escapeHtml(equipo.getResumenCaracteristicas())).append("\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("                <a href=\"/detalles/").append(equipo.getId()).append("\" class=\"btn-detalles\">\n")
                .append("                    🔍 Ver Detalles Completos\n")
                .append("                </a>\n")
                .append("            </div>\n");
        }
        
        html.append("        </section>\n")
            .append("        <footer>\n")
            .append("            <p>&copy; 2025 Universidad del Valle de Guatemala - Daniel López (242159)</p>\n")
            .append("        </footer>\n")
            .append("    </div>\n")
            .append("</body>\n")
            .append("</html>");
        
        return html.toString();
    }
    
    /**
     * Genera página de detalles de un equipo
     */
    private String generarPaginaDetalles(Equipo equipo) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"es\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Detalles: " + escapeHtml(equipo.getNombre()) + " - Sistema de Laboratorio</title>\n" +
               "    <link rel=\"stylesheet\" href=\"/styles.css\">\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <header>\n" +
               "            <h1>🔍 Detalles del Equipo</h1>\n" +
               "            <p>Universidad del Valle de Guatemala</p>\n" +
               "        </header>\n" +
               "\n" +
               "        <nav class=\"menu\">\n" +
               "            <a href=\"/\" class=\"btn-secondary\">🏠 Menú Principal</a>\n" +
               "            <a href=\"/equipos\" class=\"btn-primary\">📋 Ver Todos</a>\n" +
               "        </nav>\n" +
               "\n" +
               "        <section class=\"detalles-completos\">\n" +
               "            <div class=\"equipo-card-detalle\">\n" +
               "                <div class=\"equipo-header\">\n" +
               "                    <span class=\"equipo-id\">" + equipo.getId() + "</span>\n" +
               "                    <span class=\"equipo-consumo\">⚡ " + String.format("%.2f", equipo.getConsumoElectrico()) + "W</span>\n" +
               "                </div>\n" +
               "                <h2>" + escapeHtml(equipo.getNombre()) + "</h2>\n" +
               "                \n" +
               "                <div class=\"detalles-equipo\">\n" +
               "                    <pre>" + escapeHtml(equipo.mostrarDetalles()) + "</pre>\n" +
               "                </div>\n" +
               "                \n" +
               "                <div class=\"acciones\">\n" +
               "                    <a href=\"javascript:history.back()\" class=\"btn-secondary\">← Volver</a>\n" +
               "                    <a href=\"/equipos\" class=\"btn-primary\">Ver Todos los Equipos</a>\n" +
               "                </div>\n" +
               "            </div>\n" +
               "        </section>\n" +
               "\n" +
               "        <footer>\n" +
               "            <p>&copy; 2025 Universidad del Valle de Guatemala - Daniel López (242159)</p>\n" +
               "        </footer>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Genera página de error
     */
    private String generarPaginaError(String mensaje, String volverUrl) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"es\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Error - Sistema de Laboratorio</title>\n" +
               "    <link rel=\"stylesheet\" href=\"/styles.css\">\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <header>\n" +
               "            <h1>❌ Error</h1>\n" +
               "            <p>Universidad del Valle de Guatemala</p>\n" +
               "        </header>\n" +
               "\n" +
               "        <section class=\"error\">\n" +
               "            <div class=\"mensaje error\">\n" +
               "                " + escapeHtml(mensaje) + "\n" +
               "            </div>\n" +
               "            \n" +
               "            <div class=\"acciones\">\n" +
               "                <a href=\"" + volverUrl + "\" class=\"btn-primary\">🏠 Volver al Menú Principal</a>\n" +
               "                <a href=\"/equipos\" class=\"btn-secondary\">📋 Ver Todos los Equipos</a>\n" +
               "            </div>\n" +
               "        </section>\n" +
               "        \n" +
               "        <footer>\n" +
               "            <p>&copy; 2025 Universidad del Valle de Guatemala - Daniel López (242159)</p>\n" +
               "        </footer>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Sirve el archivo CSS
     */
    private String servirCSS() {
        String css = generarCSS();
        return String.format(
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/css; charset=UTF-8\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n%s",
            css.getBytes().length, css
        );
    }
    
    /**
     * Genera el CSS para la aplicación
     */
    private String generarCSS() {
        return "/* Estilos para el Sistema de Laboratorio de Física - Sin JavaScript */\n" +
               "* {\n" +
               "    margin: 0;\n" +
               "    padding: 0;\n" +
               "    box-sizing: border-box;\n" +
               "}\n" +
               "\n" +
               "body {\n" +
               "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
               "    line-height: 1.6;\n" +
               "    color: #333;\n" +
               "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "    min-height: 100vh;\n" +
               "}\n" +
               "\n" +
               ".container {\n" +
               "    max-width: 1200px;\n" +
               "    margin: 0 auto;\n" +
               "    padding: 20px;\n" +
               "}\n" +
               "\n" +
               "header {\n" +
               "    background: rgba(255, 255, 255, 0.95);\n" +
               "    padding: 2rem;\n" +
               "    border-radius: 15px;\n" +
               "    text-align: center;\n" +
               "    margin-bottom: 2rem;\n" +
               "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
               "    backdrop-filter: blur(10px);\n" +
               "}\n" +
               "\n" +
               "header h1 {\n" +
               "    font-size: 2.5rem;\n" +
               "    color: #2c3e50;\n" +
               "    margin-bottom: 0.5rem;\n" +
               "}\n" +
               "\n" +
               "header p {\n" +
               "    font-size: 1.1rem;\n" +
               "    color: #7f8c8d;\n" +
               "    margin-bottom: 0.5rem;\n" +
               "}\n" +
               "\n" +
               ".menu {\n" +
               "    display: flex;\n" +
               "    gap: 1rem;\n" +
               "    margin-bottom: 2rem;\n" +
               "    flex-wrap: wrap;\n" +
               "    justify-content: center;\n" +
               "}\n" +
               "\n" +
               ".btn-primary, .btn-secondary, .btn-accent, .btn-detalles {\n" +
               "    padding: 12px 24px;\n" +
               "    border: none;\n" +
               "    border-radius: 8px;\n" +
               "    font-size: 1rem;\n" +
               "    font-weight: 600;\n" +
               "    cursor: pointer;\n" +
               "    transition: all 0.3s ease;\n" +
               "    text-decoration: none;\n" +
               "    display: inline-block;\n" +
               "    text-align: center;\n" +
               "}\n" +
               "\n" +
               ".btn-primary {\n" +
               "    background: #3498db;\n" +
               "    color: white;\n" +
               "}\n" +
               "\n" +
               ".btn-primary:hover {\n" +
               "    background: #2980b9;\n" +
               "    transform: translateY(-2px);\n" +
               "}\n" +
               "\n" +
               ".btn-secondary {\n" +
               "    background: #95a5a6;\n" +
               "    color: white;\n" +
               "}\n" +
               "\n" +
               ".btn-secondary:hover {\n" +
               "    background: #7f8c8d;\n" +
               "    transform: translateY(-2px);\n" +
               "}\n" +
               "\n" +
               ".btn-accent {\n" +
               "    background: #e74c3c;\n" +
               "    color: white;\n" +
               "}\n" +
               "\n" +
               ".btn-accent:hover {\n" +
               "    background: #c0392b;\n" +
               "    transform: translateY(-2px);\n" +
               "}\n" +
               "\n" +
               ".btn-detalles {\n" +
               "    background: #17a2b8;\n" +
               "    color: white;\n" +
               "    padding: 8px 16px;\n" +
               "    font-size: 0.9rem;\n" +
               "}\n" +
               "\n" +
               ".btn-detalles:hover {\n" +
               "    background: #138496;\n" +
               "    transform: scale(1.05);\n" +
               "}\n" +
               "\n" +
               ".busqueda, .info, .resultados, .detalles-completos, .error {\n" +
               "    background: rgba(255, 255, 255, 0.95);\n" +
               "    padding: 2rem;\n" +
               "    border-radius: 15px;\n" +
               "    margin-bottom: 2rem;\n" +
               "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
               "}\n" +
               "\n" +
               ".busqueda h3, .info h3 {\n" +
               "    margin-bottom: 1.5rem;\n" +
               "    color: #2c3e50;\n" +
               "}\n" +
               "\n" +
               ".form-group {\n" +
               "    margin-bottom: 1.5rem;\n" +
               "}\n" +
               "\n" +
               ".form-group h4 {\n" +
               "    margin-bottom: 0.5rem;\n" +
               "    color: #34495e;\n" +
               "}\n" +
               "\n" +
               ".estadisticas {\n" +
               "    display: grid;\n" +
               "    gap: 1rem;\n" +
               "}\n" +
               "\n" +
               ".stat-item {\n" +
               "    background: #f8f9fa;\n" +
               "    padding: 1rem;\n" +
               "    border-radius: 8px;\n" +
               "    border-left: 4px solid #3498db;\n" +
               "}\n" +
               "\n" +
               ".mensaje {\n" +
               "    padding: 1rem;\n" +
               "    border-radius: 8px;\n" +
               "    margin-bottom: 1rem;\n" +
               "    font-weight: 600;\n" +
               "}\n" +
               "\n" +
               ".mensaje.exito {\n" +
               "    background: #d4edda;\n" +
               "    color: #155724;\n" +
               "    border: 1px solid #c3e6cb;\n" +
               "}\n" +
               "\n" +
               ".mensaje.error {\n" +
               "    background: #f8d7da;\n" +
               "    color: #721c24;\n" +
               "    border: 1px solid #f5c6cb;\n" +
               "}\n" +
               "\n" +
               ".equipo-card, .equipo-card-detalle {\n" +
               "    background: #fff;\n" +
               "    border: 1px solid #e9ecef;\n" +
               "    border-radius: 12px;\n" +
               "    padding: 1.5rem;\n" +
               "    margin-bottom: 1rem;\n" +
               "    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);\n" +
               "    transition: all 0.3s ease;\n" +
               "}\n" +
               "\n" +
               ".equipo-card:hover {\n" +
               "    transform: translateY(-4px);\n" +
               "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);\n" +
               "}\n" +
               "\n" +
               ".equipo-header {\n" +
               "    display: flex;\n" +
               "    justify-content: space-between;\n" +
               "    align-items: center;\n" +
               "    margin-bottom: 1rem;\n" +
               "    flex-wrap: wrap;\n" +
               "    gap: 1rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-id {\n" +
               "    background: #3498db;\n" +
               "    color: white;\n" +
               "    padding: 0.5rem 1rem;\n" +
               "    border-radius: 20px;\n" +
               "    font-weight: 600;\n" +
               "    font-size: 0.9rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-consumo {\n" +
               "    background: #e74c3c;\n" +
               "    color: white;\n" +
               "    padding: 0.5rem 1rem;\n" +
               "    border-radius: 20px;\n" +
               "    font-weight: 600;\n" +
               "    font-size: 0.9rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-nombre {\n" +
               "    font-size: 1.3rem;\n" +
               "    font-weight: 700;\n" +
               "    color: #2c3e50;\n" +
               "    margin-bottom: 0.5rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-card-detalle h2 {\n" +
               "    font-size: 1.5rem;\n" +
               "    font-weight: 700;\n" +
               "    color: #2c3e50;\n" +
               "    margin-bottom: 1rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-info {\n" +
               "    display: grid;\n" +
               "    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n" +
               "    gap: 1rem;\n" +
               "    margin-bottom: 1rem;\n" +
               "}\n" +
               "\n" +
               ".equipo-detalle {\n" +
               "    background: #f8f9fa;\n" +
               "    padding: 0.75rem;\n" +
               "    border-radius: 8px;\n" +
               "}\n" +
               "\n" +
               ".equipo-detalle strong {\n" +
               "    color: #495057;\n" +
               "    display: block;\n" +
               "    margin-bottom: 0.25rem;\n" +
               "}\n" +
               "\n" +
               ".detalles-equipo {\n" +
               "    background: #f8f9fa;\n" +
               "    padding: 1.5rem;\n" +
               "    border-radius: 8px;\n" +
               "    border-left: 4px solid #3498db;\n" +
               "    margin-bottom: 1rem;\n" +
               "}\n" +
               "\n" +
               ".detalles-equipo pre {\n" +
               "    font-family: 'Courier New', monospace;\n" +
               "    white-space: pre-wrap;\n" +
               "    line-height: 1.6;\n" +
               "    margin: 0;\n" +
               "}\n" +
               "\n" +
               ".acciones {\n" +
               "    display: flex;\n" +
               "    gap: 1rem;\n" +
               "    flex-wrap: wrap;\n" +
               "}\n" +
               "\n" +
               "footer {\n" +
               "    text-align: center;\n" +
               "    padding: 2rem;\n" +
               "    color: rgba(255, 255, 255, 0.8);\n" +
               "    font-size: 0.9rem;\n" +
               "}\n" +
               "\n" +
               "@media (max-width: 768px) {\n" +
               "    .container {\n" +
               "        padding: 10px;\n" +
               "    }\n" +
               "    \n" +
               "    header h1 {\n" +
               "        font-size: 2rem;\n" +
               "    }\n" +
               "    \n" +
               "    .menu {\n" +
               "        flex-direction: column;\n" +
               "    }\n" +
               "    \n" +
               "    .equipo-header {\n" +
               "        flex-direction: column;\n" +
               "        align-items: stretch;\n" +
               "    }\n" +
               "    \n" +
               "    .equipo-info {\n" +
               "        grid-template-columns: 1fr;\n" +
               "    }\n" +
               "    \n" +
               "    .acciones {\n" +
               "        flex-direction: column;\n" +
               "    }\n" +
               "}";
    }
    
    /**
     * Escapa caracteres HTML para prevenir XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
    
    // Métodos auxiliares para crear respuestas HTTP
    private String crearRespuestaHTML(String html) {
        return String.format(
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n%s",
            html.getBytes().length, html
        );
    }
    
    private String crearRespuesta404() {
        String html = "<html><body><h1>404 - Página no encontrada</h1><a href=\"/\">Volver al inicio</a></body></html>";
        return String.format(
            "HTTP/1.1 404 Not Found\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n%s",
            html.getBytes().length, html
        );
    }
    
    private String crearRespuestaError(int codigo, String mensaje) {
        String html = String.format("<html><body><h1>%d - %s</h1><a href=\"/\">Volver al inicio</a></body></html>", codigo, mensaje);
        return String.format(
            "HTTP/1.1 %d %s\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n%s",
            codigo, mensaje, html.getBytes().length, html
        );
    }
    
    private String crearRespuesta500(String error) {
        String html = String.format("<html><body><h1>500 - Error del servidor</h1><p>%s</p><a href=\"/\">Volver al inicio</a></body></html>", escapeHtml(error));
        return String.format(
            "HTTP/1.1 500 Internal Server Error\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: %d\r\n" +
            "\r\n%s",
            html.getBytes().length, html
        );
    }
}
