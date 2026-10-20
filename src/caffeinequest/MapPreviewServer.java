package caffeinequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Run AFTER CaffeineQuest, then open http://localhost:8080/demo-map.html in a browser.
 * Serving the page over HTTP lets the browser send its normal Referer header
 * when requesting map tiles. Only the generated map is served, on loopback.
 * Keep this application running while viewing the map; Eclipse's red square stops it.
 * Requires a full JDK 11 or newer. No additional libraries; module-info.java declares jdk.httpserver.
 */
public final class MapPreviewServer {
    private MapPreviewServer() { }

    public static void main(String[] args) throws IOException {
        Path mapFile = Paths.get("output", "demo-map.html").toAbsolutePath();
        if (!Files.isDirectory(mapFile.getParent())) {
            System.err.println("Map not found: " + mapFile);
            System.err.println("Run CaffeineQuest first. Set this application's working directory to the project folder.");
            return;
        }
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        } catch (IOException exception) {
            System.err.println("Could not start the preview server on port 8080: " + exception.getMessage());
            System.err.println("If another MapPreviewServer is running, stop it with Eclipse's red square first.");
            return;
        }
        server.createContext("/", exchange -> serve(exchange, mapFile.getParent()));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        server.start();
        System.out.println("Caffeine Quest map preview is running.");
        System.out.println("Open this address in Chrome, Safari, Firefox, or Edge:");
        System.out.println("Demo: http://localhost:8080/demo-map.html");
        System.out.println("Student: http://localhost:8080/student-map.html");
        System.out.println("Keep this Java application running while using the map.");
        System.out.println("Stop it using the red square in Eclipse's Console toolbar.");
        System.out.println("Run CaffeineQuest again to change the results, then refresh the browser page.");
    }

    private static void serve(HttpExchange exchange, Path outputFolder) throws IOException {
        try {
            if (!exchange.getRequestMethod().equals("GET") && !exchange.getRequestMethod().equals("HEAD")) {
                exchange.getResponseHeaders().set("Allow", "GET, HEAD");
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            String path = exchange.getRequestURI().getPath();
            if (!path.equals("/") && !path.equals("/demo-map.html") && !path.equals("/student-map.html")) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            Path mapFile = outputFolder.resolve(path.equals("/student-map.html") ? "student-map.html" : "demo-map.html");
            byte[] content;
            int status;
            if (Files.isRegularFile(mapFile)) {
                content = Files.readAllBytes(mapFile);
                status = 200;
            } else {
                content = "Run CaffeineQuest in the corresponding mode to generate this map, then refresh.".getBytes(StandardCharsets.UTF_8);
                status = 404;
            }
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            // An accurate browser Referer identifies this local web page to the tile service.
            exchange.getResponseHeaders().set("Referrer-Policy", "strict-origin-when-cross-origin");
            // Applies only to this HTML, not the tile requests or tile cache.
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
            exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
            if (exchange.getRequestMethod().equals("HEAD")) {
                exchange.sendResponseHeaders(status, -1);
            } else {
                exchange.sendResponseHeaders(status, content.length);
                exchange.getResponseBody().write(content);
            }
        } finally {
            exchange.close();
        }
    }
}
