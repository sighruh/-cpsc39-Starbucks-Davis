# Caffeine Quest: Find your next coffee stop

**CPSC-39 · Advanced sorts · Individual project · 100 points**

Build and explain the sorting behind a Starbucks finder. Your starter project
already reads a real dataset, calculates distances, and displays results on a map.
You will complete the merge operation in merge sort, verify its results, investigate
one unfamiliar part of the application, and add a small customization.

## Learning goals

- Merge two sorted ranges and explain how recursion produces a fully sorted list.
- Use a comparator to order objects while keeping complete records together.
- Verify sorting correctness, stability, and preservation of input records.
- Trace unfamiliar code using documentation and concrete examples.

## 1. Explore the supplied application

Follow README.md and select **Demo mode**. Java's built-in sort makes the application
work before you implement merge sort. Use the default starting coordinates and
ten results. Run once with all stores and once with drive-through stores only.
View the Demo map using the preview server. Capture evidence before another
search overwrites the same file.

In your reflection, record the first three stores for both searches and describe
what changed. Explain why alphabetical sorting would not answer “Which stores
are nearest?” The map ranks by straight-line distance; it does not plan a route.

## 2. Plan with the five questions

Before coding, answer these in REFLECTION-TEMPLATE.md:

1. **Purpose:** What must the merge operation accomplish?
2. **Inputs/outputs:** What do items, buffer, order, start, middle, and end mean?
   What is changed in place, and what does the method return?
3. **Constraints:** What must already be true about the two halves? Which range
   endpoints are excluded? What sorting operations are prohibited?
4. **Edge cases:** What happens when one half runs out or keys are equal?
5. **Test oracle:** How will you determine whether the output is correct and
   contains exactly the original records?

## 3. Implement your merge

Open **MergeSort.java**. The recursive division, base case, temporary list,
method signatures, and comparator are supplied. Complete **TODO A-D** in merge():

- Compare the current elements while both halves have elements. Copy the earlier
  element into buffer and advance the relevant indices. Count comparator calls.
- Copy the remaining elements from the left half, then the right half.
- Copy the merged range back into items and return this merge's comparison count.

Replace the placeholder exception with your code. Use the supplied comparator;
do not compare store names or rounded display distances. Select the left element
on equal keys to preserve stability. Do not change the recursive scaffold.
The temporary buffer is already sized: use get(index) to read and set(index, value)
to replace an element, rather than inserting elements with add().

**Your MergeSort implementation must not call List.sort, Collections.sort,
Arrays.sort, stream sorted(), or another library sorting operation.** The built-in
sort in Demo mode and SortCheck is supplied infrastructure; leave it there.
Do not redirect Student mode to Demo mode or alter the checks to suppress failures.

Trace one small example by hand: merge **[2, 7]** and **[3, 5]**. Show each
comparison, the item copied, and the final copy of any remaining elements.

## 4. Check and demonstrate correctness

Run **SortCheck.java**. Initially, the empty and one-item cases pass while cases
requiring a merge fail. After your work, all **12 supplied checks** should pass.
These cover empty/single lists, duplicates, existing order, reverse order, odd
sizes, stability, comparisons, store ID ties, and the full/filtered dataset.

For your report:

- Include the final check output.
- Document one additional test of your choice, including input, expected output,
  actual output, and why it matters. You may put an extra test in a separate class;
  keep the supplied SortCheck unchanged.
- Run **Student mode** with the same two searches used in Part 1. Compare the
  first three store IDs/names and displayed distances with Demo results. Exact
  comparison counts and timings do not need to match across different algorithms.
- Include a Student map screenshot with its mode label and numbered results.

## 5. Investigate unfamiliar code

Choose **one** topic below. Write **150-250 words**, identify the class/method,
quote a short relevant excerpt from this project, and trace one concrete example.
Include at least one documentation or course source you actually consulted.

| Topic | Questions to answer |
|---|---|
| Comparator and lambda | What do negative, zero, and positive return values mean? How are distance ties handled? |
| Objects and ArrayList | Why do names, distances, and coordinates remain associated when their order changes? What do get() and set() do here? |
| CSV parsing | Why is splitting at every comma unreliable? Trace a field containing a quoted comma. |
| Distance helper | What do latitude/longitude mean? Where are degrees converted to radians, and why is the result not a driving distance? |
| Map export | How do sorted Java objects become data in the HTML page? Which part draws the map? |
| Local web server | What does localhost refer to? What does the server return, and why do we keep it running? |

A definition alone is insufficient. Explain how the selected feature operates
in this application. You do not need to explain every line of the project.

## 6. Add one small customization

After the original checks pass, choose **one**:

- **Farthest first:** Add a menu option and comparator for descending distance.
- **Alphabetical:** Add a menu option and comparator for store name, with ID ties.
- **Distance limit:** Let users choose a maximum straight-line distance.
- **City filter:** Let users choose a city, with a clear no-matches message.
- **Units:** Let users select miles or kilometers; keep calculations and labels consistent.

Keep the original nearest-first behavior available. Your customization must work
in Student mode and continue using your merge sort. Check default behavior again
and document one test of your new behavior. If it changes map data or labels,
update those too. Explain which file(s) you changed and why.

## 7. Explain the algorithm and show your work

Answer briefly:

1. What is merge sort's time complexity? Explain how the number of recursion
   levels and work per level lead to it.
2. What auxiliary space does this implementation use?
3. What does stable mean, and which choice in your merge preserves it?
4. Why might the Demo and Student comparison counts differ, even when rankings match?
5. Why is one short timing measurement insufficient to conclude which sort is faster?

Give a **two-minute in-class demonstration**: run Student mode, show the map,
explain one comparison in your merge, and demonstrate your customization.

## Submit

- Your complete Eclipse project folder as a ZIP, including src, resources, data,
  and Eclipse configuration. Omit bin and other compiled output.
- Your completed reflection with the planning, trace, investigation/source,
  analysis answers, customization explanation, and test evidence.
- Demo-versus-Student comparison evidence, final SortCheck output, and a Student
  map screenshot. Put these in an evidence folder in your submission.

Keep your name in your reflection. If your instructor assigns a GitHub repository,
commit your work there and follow its submission instructions.

## Rubric

| Component | Points | Evidence |
|---|---:|---|
| Merge implementation | 40 | Correct comparisons, indices, remainder handling, copy-back, stability, and count; no prohibited sort |
| Testing and validation | 20 | Supplied checks, additional test, and matching Demo/Student rankings |
| Investigation | 15 | Accurate project-specific explanation, concrete trace, and consulted source |
| Customization | 15 | Works in Student mode, preserves default behavior, documented test |
| Explanation and demonstration | 10 | Planning/trace/complexity answers and clear demonstration of readable code |
| **Total** | **100** | |
