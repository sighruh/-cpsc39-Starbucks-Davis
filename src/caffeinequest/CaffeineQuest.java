package caffeinequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Run this class in Eclipse. Requires Java 11 or newer; no external Java libraries. */
public final class CaffeineQuest {
    private CaffeineQuest() { }

    public static void main(String[] args) {
        Path root = Paths.get("").toAbsolutePath();
        Path data = args.length > 0 ? Paths.get(args[0]) : root.resolve("data/starbucks-california.csv");
        Path template = root.resolve("resources/map-template.html");
        try (Scanner input = new Scanner(System.in)) {
            List<StarbucksStore> stores = CsvLoader.load(data);
            System.out.println("CAFFEINE QUEST | Advanced Sorting with Starbucks");
            System.out.println("Loaded " + stores.size() + " stores from " + data);
            System.out.println("Dataset snapshot: store details may have changed.");
            if (!Files.isRegularFile(template)) {
                throw new IOException("Map template missing: " + template
                        + "\nSet the Eclipse working directory to the CaffeineQuestStudent project folder.");
            }
            boolean again = true;
            while (again) {
                System.out.println("\nPress Enter for each default to try a Merced search.");
                System.out.println("1. Demo mode (Java built-in sort)");
                System.out.println("2. Student mode (your merge sort)");
                int mode = readInteger(input, "Sorting mode", 1, 1, 2);
                String modeName = mode == 1 ? "DEMO: Java built-in sort" : "STUDENT: your merge sort";
                double latitude = readNumber(input, "Starting latitude", 37.3022, -90, 90);
                double longitude = readNumber(input, "Starting longitude", -120.4830, -180, 180);
                boolean driveOnly = readYesNo(input, "Drive-through stores only?", false);
                int limit = readInteger(input, "Number of results", 10, 1, stores.size());

                List<StoreDistance> ranked = rank(stores, latitude, longitude, driveOnly);
                long start = System.nanoTime();
                long comparisons;
                try {
                    if (mode == 1) {
                        // Supplied demo infrastructure. Do not call this from MergeSort.
                        long[] counter = {0};
                        ranked.sort((a, b) -> {
                            counter[0]++;
                            return StoreDistance.NEAREST_FIRST.compare(a, b);
                        });
                        comparisons = counter[0];
                    } else {
                        comparisons = MergeSort.sort(ranked, StoreDistance.NEAREST_FIRST);
                    }
                } catch (UnsupportedOperationException unfinished) {
                    System.out.println("Student merge sort is not finished: " + unfinished.getMessage());
                    System.out.println("No Student map was written. Complete the TODOs or choose Demo mode.");
                    again = readYesNo(input, "Run another search?", false);
                    continue;
                }
                double elapsedMs = (System.nanoTime() - start) / 1_000_000.0;
                List<StoreDistance> top = new ArrayList<>(ranked.subList(0, Math.min(limit, ranked.size())));

                System.out.printf(Locale.US, "%n%s: %,d eligible stores | %,d comparisons | %.3f ms%n",
                        modeName, ranked.size(), comparisons, elapsedMs);
                System.out.println("Timing includes sort work only; small timings vary between runs.");
                System.out.printf("%n%-5s %-9s %-11s %s%n", "Rank", "Miles", "Drive-thru", "Store / City");
                for (int index = 0; index < top.size(); index++) {
                    StoreDistance result = top.get(index);
                    System.out.printf(Locale.US, "%-5d %-9.2f %-11s %s / %s%n", index + 1,
                            result.miles, result.store.driveThrough ? "Yes" : "No",
                            result.store.name, result.store.city);
                }
                if (top.isEmpty()) {
                    System.out.println("No stores match the filter.");
                }
                System.out.println("Distances are straight-line miles, not driving routes.");
                Path output = root.resolve(mode == 1 ? "output/demo-map.html" : "output/student-map.html");
                MapExporter.write(template, output, top, latitude, longitude, driveOnly, comparisons, modeName);
                System.out.println("\nOpen this file in your web browser:\n" + output);
                System.out.println("Run MapPreviewServer.java, then open:");
                System.out.println(mode == 1 ? "http://localhost:8080/demo-map.html" : "http://localhost:8080/student-map.html");
                System.out.println("Keep the preview server running. Each search replaces its mode's map file.");
                again = readYesNo(input, "Run another search?", false);
            }
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Unable to complete search: " + exception.getMessage());
            System.err.println("In Eclipse: Run Configurations > Arguments > Working directory > select this project.");
        }
    }

    /** Builds an unsorted list; the caller must explicitly invoke the advanced sort. */
    static List<StoreDistance> rank(List<StarbucksStore> stores, double latitude,
            double longitude, boolean driveOnly) {
        List<StoreDistance> results = new ArrayList<>();
        for (StarbucksStore store : stores) {
            if (!driveOnly || store.driveThrough) {
                results.add(new StoreDistance(store, StoreDistance.milesBetween(
                        latitude, longitude, store.latitude, store.longitude)));
            }
        }
        return results;
    }

    private static double readNumber(Scanner input, String prompt, double fallback, double min, double max) {
        while (true) {
            System.out.print(prompt + " [" + fallback + "]: ");
            String text = nextLine(input);
            if (text.isEmpty()) {
                return fallback;
            }
            try {
                double value = Double.parseDouble(text);
                if (Double.isFinite(value) && value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a number from " + min + " to " + max + ".");
        }
    }

    private static int readInteger(Scanner input, String prompt, int fallback, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + fallback + "]: ");
            String text = nextLine(input);
            if (text.isEmpty()) {
                return Math.min(fallback, max);
            }
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a whole number from " + min + " to " + max + ".");
        }
    }

    private static boolean readYesNo(Scanner input, String prompt, boolean fallback) {
        while (true) {
            System.out.print(prompt + (fallback ? " [Y/n]: " : " [y/N]: "));
            String text = nextLine(input);
            if (text.isEmpty()) {
                return fallback;
            }
            if (text.equalsIgnoreCase("y") || text.equalsIgnoreCase("yes")) {
                return true;
            }
            if (text.equalsIgnoreCase("n") || text.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Enter y or n.");
        }
    }

    private static String nextLine(Scanner input) {
        // End-of-input accepts defaults, so piped demonstrations also finish cleanly.
        return input.hasNextLine() ? input.nextLine().trim() : "";
    }
}
