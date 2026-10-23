# Caffeine Quest reflection

Name: Saira Davis

## Five questions before coding

1. Purpose: The purpose of the merge is to take the sorted ranges and then combine them into one sorted range. 
2. Inputs/Outputs:
   Parameters:
     items - The list of records is going to be merged 
     buffer - This is a temporary list where the merged results go
     order - Comparator that will see how two records are ordered
     start - 1st index of the left half
     middle - 1st index of the right half 
     end - Last index of the right half
   What changes? - The buffer is filled and then the items[start-end] is replaced with the sorted items
   What is returned? - A long is returned
4. Constraints: The two halves are sorted by the comparator. The ending index is excluded. Built-in sorting are prohibited in the merge sort.
5. Edge cases: If the left side runs out the first loop stops and then the right side's half would be copied into the buffer. They are inputed without comparison. If the right side runs out the first loop stops and then the left side's half would be copied into the buffer. If the keys are equal then the compare will return a 0.
6. Test oracle: We would need to verify the correctness by using multiple checks. These should include hand-traced cases, sorted check, stability, etc. Stability uses equal keys then confirms that the relative order from the output matches their relative order from the original input.

## Explore Demo mode

Starting latitude/longitude: 37.3022/-120.4830

| Filter | Rank | Store ID / name | Distance in miles |
|---|---:|---|---:|
| All stores | 1 | 15622 | 0.08 |
| All stores | 2 | 11150 | 1.20 |
| All stores | 3 | 17430 | 1.84 |
| Drive-through only | 1 | 17430 | 1.84 |
| Drive-through only | 2 | 17356 | 7.32 |
| Drive-through only | 3 | 6633 | 7.83 |

What changed, and why would alphabetical order not answer this search?
The results were filtered to include only stores with drive-throughs. Alphabetical order would not answer this search because it would only sort the store's name and not the distance from the starting point.

## Hand trace: merge [2, 7] and [3, 5]

| Step | Compared values (if any) | Item copied | Buffer so far |
|---|---|---|---|
| 1 | 2 & 3 | 2 (from the left) | [2] |
| 2 | 7 & 3 | 3 (from the right) | [2, 3] |
| 3 | 7 & 5 | 5 (from the right) | [2, 3, 5] |
| 4 | none | 7 (leftover from the left) | [2, 3, 5, 7] |

## Validation

Final SortCheck output or evidence filename:
12 passed; 0 failed. All supplied checks passed.

Additional test: input / expected output / actual output / reason:
Input: [2, 5, 4, 6)]

Expected output: [2, 4, 5, 6]

Actual output: [2, 4, 5, 6]

Reason: This confirms that the merge sorts a partially unsorted list from smallest to largest preserving all the original elements with no disruption to any values. 

Student-mode first three results for the two original searches:

Starting latitude/longitude: 37.3022/-120.4830

| Filter | Rank | Store ID / name | Distance in miles |
|---|---:|---|---:|
| All stores | 1 | 15622 | 0.08 |
| All stores | 2 | 11150 | 1.20 |
| All stores | 3 | 17430 | 1.84 |
| Drive-through only | 1 | 17430 | 1.84 |
| Drive-through only | 2 | 17356 | 7.32 |
| Drive-through only | 3 | 6633 | 7.83 |

How do they compare with Demo mode? Student map evidence filename:
They match the Demo mode therefore, the results are consistent.
StudentDrive-Through.png
StudentNoDrive-Through.png

## Investigate one feature (150-250 words)

Topic and class/method:
Local Web Server and MapPreviewServer
Short relevant code excerpt:
server = HttpServer.create(
    new InetSocketAddress("127.0.0.1", 8080), 0);
server.createContext("/", exchange -> serve(exchange, mapFile.getParent()));
server.start();
Explanation and concrete example:
The localhost refers to my computer. The MapPreviewScanner class runs a local web server that allows me to view the maps in a browser. The server responds to the loopback address 127.0.0.1 using a port 8080. This allows me to access the maps using http://localhost:8080/demo-map.html and http://localhost:8080/student-map.html. The server must remain running because it handles browser requests and provides the map files. If I stop running the Java application, the browser can no longer retrieve the maps from the local server giving me an error. 
Source consulted (title and URL or course reference):
https://www.cloudns.net/blog/understanding-localhost-the-basics-of-127-0-0-1-explained/
## Customization

Feature:

Files changed and explanation:

New-feature test and result:

Default-behavior regression check:

## Algorithm explanation

1. Time complexity and reasoning: The merge sort's time complexity is O(nlogn). Basically it repeatedly divides the list in half until each of the sublist contain 1 or 0 items. The recursion levels of a merge sort is log2n. 
2. Auxiliary space: The merge sort uses O(n) auxiliary space. It needs more memory as the list increases and as the buffer is used. 
3. Stability and how my code preserves it: A sort is considered stable when the elements with equal keys are in the original relative order of records. In the code if the comparator returns a 0 it means the keys are equal and copies the equal elements from the left before the equals from the right. 
4. Why Demo/Student comparison counts may differ: The Demo/Student comparison might use different information in which may affect the the comparisons during merging. The matching ranks may have the same order but the number of comparisons may not be the same. 
5. Why a single short timing is insufficient: There are several reasons as to why a single short measurement isn't reliable. That includes JVM warm-ups, small n hides growth rate, background activity, etc. A better way to do this is to run many tests and report the average. The code does something like this. 
long startTime = System.nanoTime();
sort(list);
long endTime = System.nanoTime();
System.out.println(endTime - startTime);

## Demonstration checklist

- [ ] Student search and map
- [ ] Explain one merge comparison
- [ ] Show customization
