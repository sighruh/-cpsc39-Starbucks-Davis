package caffeinequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** CSV support, including quoted commas, escaped quotes, and quoted newlines. */
public final class CsvLoader {
    private static final Pattern COORDINATES = Pattern.compile(
            "^([+-]?\\d+(?:\\.\\d+)?)([+-]\\d+(?:\\.\\d+)?)$");

    private CsvLoader() { }

    public static List<StarbucksStore> load(Path path) throws IOException {
        List<List<String>> rows;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            rows = readRows(reader);
        }
        if (rows.isEmpty()) {
            throw new IOException("The CSV is empty: " + path);
        }
        Map<String, Integer> columns = new HashMap<>();
        for (int index = 0; index < rows.get(0).size(); index++) {
            columns.put(rows.get(0).get(index).replace("\uFEFF", "").trim(), index);
        }
        for (String required : new String[] {"ID", "Name", "City", "Address", "Coordinates", "Drive-Thru"}) {
            if (!columns.containsKey(required)) {
                throw new IOException("Missing CSV column: " + required);
            }
        }
        List<StarbucksStore> stores = new ArrayList<>();
        for (int index = 1; index < rows.size(); index++) {
            List<String> row = rows.get(index);
            if (row.size() == 1 && row.get(0).trim().isEmpty()) {
                continue;
            }
            try {
                Matcher match = COORDINATES.matcher(field(row, columns, "Coordinates"));
                if (!match.matches()) {
                    throw new IllegalArgumentException("Expected coordinates such as 37.319163-120.478087");
                }
                double latitude = Double.parseDouble(match.group(1));
                double longitude = Double.parseDouble(match.group(2));
                if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
                    throw new IllegalArgumentException("Coordinates outside valid latitude/longitude ranges");
                }
                String drive = field(row, columns, "Drive-Thru");
                if (!drive.equalsIgnoreCase("Yes") && !drive.equalsIgnoreCase("No")) {
                    throw new IllegalArgumentException("Drive-Thru must be Yes or No");
                }
                stores.add(new StarbucksStore(Integer.parseInt(field(row, columns, "ID")),
                        field(row, columns, "Name"), field(row, columns, "City"),
                        field(row, columns, "Address"), latitude, longitude,
                        drive.equalsIgnoreCase("Yes")));
            } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
                throw new IOException("Invalid CSV record " + (index + 1) + ": " + exception.getMessage(), exception);
            }
        }
        return stores;
    }

    private static String field(List<String> row, Map<String, Integer> columns, String name) {
        return row.get(columns.get(name)).trim();
    }

    static List<List<String>> readRows(Reader source) throws IOException {
        PushbackReader reader = new PushbackReader(source, 1);
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        int character;
        while ((character = reader.read()) != -1) {
            char ch = (char) character;
            if (ch == '"') {
                if (quoted) {
                    int next = reader.read();
                    if (next == '"') {
                        field.append('"');
                    } else {
                        quoted = false;
                        if (next != -1) {
                            reader.unread(next);
                        }
                    }
                } else if (field.length() == 0) {
                    quoted = true;
                } else {
                    throw new IOException("Unexpected quote in an unquoted CSV field");
                }
            } else if (ch == ',' && !quoted) {
                row.add(field.toString());
                field.setLength(0);
            } else if ((ch == '\n' || ch == '\r') && !quoted) {
                if (ch == '\r') {
                    int next = reader.read();
                    if (next != '\n' && next != -1) {
                        reader.unread(next);
                    }
                }
                row.add(field.toString());
                rows.add(row);
                row = new ArrayList<>();
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        if (quoted) {
            throw new IOException("Unclosed quoted CSV field");
        }
        if (field.length() > 0 || !row.isEmpty()) {
            row.add(field.toString());
            rows.add(row);
        }
        return rows;
    }
}
