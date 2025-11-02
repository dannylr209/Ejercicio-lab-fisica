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
 * Clase principal que inicia el sistema de laboratorio de física con API separada.
 * Cumple con SRP: Solo se encarga de inicializar y coordinar la aplicación
 * Diseñado para trabajar con frontend separado (Live Server/Netlify)
 * 
 * @author Daniel López - 242159
 * @version 2.0 - Refactorizado para SOLID y arquitectura separada
 */
public class Principal {
    
    private static final int PUERTO_API_DEFAULT = 8080;
    private static final String URL_API = "http://localhost:" + PUERTO_API_DEFAULT;
    
    /**
     * Método principal que inicia la aplicación
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        mostrarBanner();
        
        try {
            // Crear dependencias siguiendo DIP
            ServicioEquipos servicioEquipos = CatalogoEquipos.getInstance();
            GeneradorRespuesta generadorRespuesta = new GeneradorRespuestaJSON();
            ControladorWeb controlador = new ControladorWeb(servicioEquipos, generadorRespuesta);
            
            // Crear servidor API
            ServidorAPI servidorAPI = new ServidorAPI(PUERTO_API_DEFAULT, controlador);
            
            // Configurar shutdown hook para cerrar el servidor correctamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n🔄 Cerrando servidor API...");
                servidorAPI.detener();
                System.out.println("✅ Servidor API cerrado correctamente.");
            }));
            
            mostrarInstruccionesUso();
            
            // Iniciar servidor API (este método es bloqueante)
            servidorAPI.iniciar();
            
        } catch (Exception e) {
            System.err.println("❌ Error fatal al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Muestra el banner de bienvenida de la aplicación
     */
    private static void mostrarBanner() {
        System.out.println("\n" +
            "═══════════════════════════════════════════════════════════════════\n" +
            "  🔬 SISTEMA DE LABORATORIO DE FÍSICA - UNIVERSIDAD DEL VALLE  🔬  \n" +
            "═══════════════════════════════════════════════════════════════════\n" +
            "  📚 CC2008 - Programación Orientada a Objetos - Sección 50       \n" +
            "  🎯 Ejercicio 6: Interfaces y Polimorfismo + Principios SOLID     \n" +
            "  🏗️  Arquitectura: API REST + Frontend Separado                   \n" +
            "  👨‍💻 Desarrollado por: Daniel López - 242159                      \n" +
            "═══════════════════════════════════════════════════════════════════\n");
        
        System.out.println("🚀 Iniciando sistema...");
        System.out.println("📋 Principios SOLID implementados:");
        System.out.println("   ✅ SRP: Cada clase tiene una responsabilidad específica");
        System.out.println("   ✅ OCP: Extensible sin modificar código existente");
        System.out.println("   ✅ LSP: Sustitución de Liskov en jerarquía de equipos");
        System.out.println("   ✅ ISP: Interfaces segregadas (EquipoInfo, Representable, etc.)");
        System.out.println("   ✅ DIP: Dependencias sobre abstracciones");
        System.out.println();
        System.out.println("🏛️ Patrones de diseño aplicados:");
        System.out.println("   ✅ Singleton: CatalogoEquipos");
        System.out.println("   ✅ Factory: EquipoFactory");
        System.out.println("   ✅ MVC: Separación de responsabilidades");
        System.out.println("   ✅ Strategy: GeneradorRespuesta");
        System.out.println();
        System.out.println("🔧 Características técnicas:");
        System.out.println("   ✅ Lista polimórfica única");
        System.out.println("   ✅ Interfaz Comparable para ordenamiento");
        System.out.println("   ✅ API REST con CORS habilitado");
        System.out.println("   ✅ Frontend separado (Live Server/Netlify)");
        System.out.println("   ✅ Manejo robusto de errores");
        System.out.println();
    }
    
    /**
     * Muestra instrucciones de uso para el desarrollador
     */
    private static void mostrarInstruccionesUso() {
        System.out.println("📖 INSTRUCCIONES DE USO:");
        System.out.println("\n🖥️  OPCIÓN 1 - Live Server (Recomendado para desarrollo):");
        System.out.println("   1. Instale la extensión 'Live Server' en VS Code");
        System.out.println("   2. Abra los archivos HTML/CSS/JS en VS Code");
        System.out.println("   3. Haga clic derecho en index.html → 'Open with Live Server'");
        System.out.println("   4. El frontend se abrirá en http://localhost:5500 (o puerto similar)");
        System.out.println("   5. La API estará en " + URL_API);
        System.out.println();
        
        System.out.println("🌐 OPCIÓN 2 - Netlify (Para despliegue):");
        System.out.println("   1. Suba los archivos HTML/CSS/JS a un repositorio Git");
        System.out.println("   2. Conecte el repositorio con Netlify");
        System.out.println("   3. Configure la URL de producción en script.js");
        System.out.println("   4. Despliegue su API en Heroku, Railway o similar");
        System.out.println();
        
        System.out.println("🔧 CONFIGURACIÓN:");
        System.out.println("   • API Backend: " + URL_API);
        System.out.println("   • Edite script.js para cambiar la URL de producción");
        System.out.println("   • El frontend detecta automáticamente el entorno");
        System.out.println();
        
        System.out.println("⚠️  IMPORTANTE:");
        System.out.println("   • Mantenga este servidor API ejecutándose");
        System.out.println("   • Use Live Server para el frontend");
        System.out.println("   • CORS está habilitado para desarrollo");
        System.out.println("\n🎯 Para cerrar: Presione Ctrl+C en esta consola");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }
}
