package caffeinequest;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** Supplied correctness oracle. Do not edit it to make failures disappear. */
public final class SortCheck {
    private static int passed;
    private static int failed;
    private SortCheck() { }

    public static void main(String[] args) throws Exception {
        checkCase("Empty list", () -> integers(new ArrayList<>()));
        checkCase("One item", () -> integers(new ArrayList<>(Arrays.asList(7))));
        checkCase("Two reversed items", () -> integers(new ArrayList<>(Arrays.asList(9, 2))));
        checkCase("Duplicates and negatives", () -> integers(new ArrayList<>(Arrays.asList(3, -2, 3, 0, -2))));
        checkCase("Already sorted", () -> integers(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5))));
        checkCase("Reverse sorted", () -> integers(new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1))));
        checkCase("Odd-sized random list", () -> {
            Random random = new Random(39);
            List<Integer> items = new ArrayList<>();
            for (int index = 0; index < 101; index++) items.add(random.nextInt(20));
            integers(items);
        });
        checkCase("Stable equal-key order", () -> {
            List<String> items = new ArrayList<>(Arrays.asList("b1", "a1", "b2", "a2", "b3"));
            MergeSort.sort(items, Comparator.comparing(value -> value.substring(0, 1)));
            require(items.equals(Arrays.asList("a1", "a2", "b1", "b2", "b3")), "Equal-key elements changed order");
        });
        checkCase("Counts comparisons", () -> {
            List<Integer> items = new ArrayList<>(Arrays.asList(2, 1));
            require(MergeSort.sort(items, Comparator.naturalOrder()) == 1, "Two items require exactly one comparison");
            require(items.equals(Arrays.asList(1, 2)), "Pair is not sorted");
        });
        List<StarbucksStore> stores = CsvLoader.load(Paths.get("data/starbucks-california.csv"));
        checkCase("All Starbucks records", () -> storeCase(stores, false));
        checkCase("Drive-through Starbucks records", () -> storeCase(stores, true));
        checkCase("Distance ties use store ID", () -> {
            StarbucksStore first = new StarbucksStore(9, "Nine", "City", "Address", 0, 0, false);
            StarbucksStore second = new StarbucksStore(2, "Two", "City", "Address", 0, 0, false);
            List<StoreDistance> items = new ArrayList<>(Arrays.asList(new StoreDistance(first, 1), new StoreDistance(second, 1)));
            MergeSort.sort(items, StoreDistance.NEAREST_FIRST);
            require(items.get(0).store.id == 2 && items.get(1).store.id == 9, "Wrong ID order for equal distances");
        });
        System.out.println("\n" + passed + " passed; " + failed + " failed.");
        if (failed > 0) {
            System.out.println("Expected before you complete the TODOs. Use the failures to guide your work.");
        } else {
            System.out.println("All supplied checks passed. Add your own documented example in your reflection.");
        }
    }

    private static void integers(List<Integer> actual) {
        List<Integer> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder()); // Oracle only; prohibited in your merge sort.
        MergeSort.sort(actual, Comparator.naturalOrder());
        require(actual.equals(expected), "Order, values, or duplicate counts differ from the oracle");
    }

    private static void storeCase(List<StarbucksStore> stores, boolean driveOnly) {
        List<StoreDistance> actual = CaffeineQuest.rank(stores, 37.3022, -120.4830, driveOnly);
        List<StoreDistance> expected = new ArrayList<>(actual);
        expected.sort(StoreDistance.NEAREST_FIRST);
        MergeSort.sort(actual, StoreDistance.NEAREST_FIRST);
        // StoreDistance does not override equals: matching object references and order
        // verifies that this exact input's records were retained, including duplicates.
        require(actual.equals(expected), "Ranking or record preservation differs from the oracle");
    }

    private static void checkCase(String label, Runnable task) {
        try {
            task.run();
            passed++;
            System.out.println("PASS: " + label);
        } catch (RuntimeException | AssertionError problem) {
            failed++;
            System.out.println("FAIL: " + label + " -- " + problem.getMessage());
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
