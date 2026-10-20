package caffeinequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Writes an HTML file with embedded results; no server or API key required. */
public final class MapExporter {
    private MapExporter() { }

    public static void write(Path template, Path destination, List<StoreDistance> results,
            double latitude, double longitude, boolean driveOnly, long comparisons, String modeName) throws IOException {
        StringBuilder json = new StringBuilder("[");
        for (int index = 0; index < results.size(); index++) {
            StoreDistance result = results.get(index);
            StarbucksStore store = result.store;
            if (index > 0) {
                json.append(',');
            }
            json.append("{\"rank\":").append(index + 1)
                .append(",\"id\":").append(store.id)
                .append(",\"name\":").append(jsonString(store.name))
                .append(",\"city\":").append(jsonString(store.city))
                .append(",\"address\":").append(jsonString(store.address))
                .append(",\"latitude\":").append(store.latitude)
                .append(",\"longitude\":").append(store.longitude)
                .append(",\"miles\":").append(result.miles)
                .append(",\"driveThrough\":").append(store.driveThrough).append('}');
        }
        json.append(']');
        String html = Files.readString(template, StandardCharsets.UTF_8)
                .replace("__ORIGIN_LAT__", Double.toString(latitude))
                .replace("__ORIGIN_LON__", Double.toString(longitude))
                .replace("__SORT_MODE__", modeName)
                .replace("__COMPARISONS__", Long.toString(comparisons))
                .replace("__FILTER__", driveOnly ? "Drive-through locations" : "All locations")
                .replace("__RESULTS_JSON__", json.toString());
        Files.createDirectories(destination.toAbsolutePath().getParent());
        Files.writeString(destination, html, StandardCharsets.UTF_8);
    }

    // Escape HTML-sensitive characters too: data cannot terminate the script tag.
    static String jsonString(String value) {
        StringBuilder escaped = new StringBuilder("\"");
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                default:
                    if (ch < 32 || ch == '<' || ch == '>' || ch == '&' || ch == '\u2028' || ch == '\u2029') {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
            }
        }
        return escaped.append('"').toString();
    }
}
