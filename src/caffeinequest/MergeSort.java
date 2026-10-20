package caffeinequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Stable, recursive merge sort: O(n log n) time and O(n) auxiliary space. */
public final class MergeSort {
    private MergeSort() { }

    /** Sort in place and return the number of comparator calls. No library sort. */
    public static <T> long sort(List<T> items, Comparator<? super T> order) {
        List<T> buffer = new ArrayList<>(items);
        return sortRange(items, buffer, order, 0, items.size());
    }

    // Each range is half-open: [start, end).
    private static <T> long sortRange(List<T> items, List<T> buffer,
            Comparator<? super T> order, int start, int end) {
        if (end - start < 2) {
            return 0;
        }
        int middle = start + (end - start) / 2;
        long comparisons = sortRange(items, buffer, order, start, middle)
                + sortRange(items, buffer, order, middle, end);

        return comparisons + merge(items, buffer, order, start, middle, end);
    }

    /** Both halves are already sorted: [start, middle) and [middle, end). */
    private static <T> long merge(List<T> items, List<T> buffer,
            Comparator<? super T> order, int start, int middle, int end) {
        int left = start;
        int right = middle;
        int destination = start;
        long comparisons = 0;

        // TODO A: While BOTH halves have items, compare their current elements.
        // Count each comparator call. Copy the earlier item into buffer.
        // On equal keys, take the left item to preserve stability.
        // Advance the selected source index and the destination index.

        // TODO B: Copy any remaining elements from the left half into buffer.

        // TODO C: Copy any remaining elements from the right half into buffer.

        // TODO D: Copy buffer[start..end) back into items[start..end).
        // Return the number of comparisons performed by THIS merge.
        // Replace the exception below with your implementation and return.
        throw new UnsupportedOperationException("Complete TODO A-D in MergeSort.merge().");
    }
}
