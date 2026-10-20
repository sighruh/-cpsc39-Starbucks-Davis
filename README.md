# Caffeine Quest: Getting Started

## 1. Create your own GitHub repository

1. Sign in to GitHub.
2. Open the [Caffeine Quest template](https://github.com/Merced-College/cpsc39-advSorting-StarbucksData).
3. Click **Use this template → Create a new repository**.
4. Select your own GitHub account as the owner.
5. Name your repository **cpsc39-Starbucks-YourLastName**.
6. Click **Create repository**.
7. In **your new repository**, click **Code → HTTPS** and copy the URL.

Make sure you copy the URL for your own repository. Your changes will be committed and pushed there.

## 2. Clone your repository into Eclipse

1. In Eclipse, choose **File → Import**.
2. Expand **Git**, select **Projects from Git**, and click **Next**.
3. Select **Clone URI → Next**.
4. Paste your repository’s URL into **URI**. Eclipse should fill in the host and repository path.
5. Complete authentication if prompted, then click **Next**.
6. Select the **main** branch and click **Next**.
7. Keep the suggested local directory, or choose a folder outside your Eclipse workspace. Leave the remote name as **origin**.
8. Click **Next**.
9. Select **Import existing Eclipse projects**, then click **Next**.
10. Select **CaffeineQuestStudent** and click **Finish**.

The project should appear in Package Explorer. Use **JDK 11 or newer**.

## 3. Run Demo mode

1. Expand **CaffeineQuestStudent → src → caffeinequest**.
2. Right-click **CaffeineQuest.java → Run As → Java Application**.
3. Click inside the Console.
4. Press **Enter** to select **1 — Demo mode**.
5. Press **Enter** at the remaining prompts to use the default search and finish.

Demo mode uses Java’s built-in sort, so it works before you complete the assignment.

## 4. View the Demo map

1. Right-click **MapPreviewServer.java → Run As → Java Application**.
2. Keep this application running.
3. Open **http://localhost:8080/demo-map.html** in your browser.

If port 8080 is already in use, stop your previous preview server before starting another.

## 5. Complete the assignment

Read **ASSIGNMENT.md** and complete your responses in **REFLECTION-TEMPLATE.md**.

Complete **TODO A–D in MergeSort.java**, then run **SortCheck.java**.

After completing your merge sort:

1. Run **CaffeineQuest.java** again.
2. Choose **2 — Student mode**.
3. Enter your search settings.
4. Open **http://localhost:8080/student-map.html**.

Keep the preview server running while viewing either map. Refresh the browser after generating new results.

## 6. Save your progress to GitHub

1. Save your files.
2. Right-click the project → **Team → Commit**.
3. Stage your changed source files and reflection.
4. Enter a meaningful commit message describing your changes.
5. Click **Commit and Push**.
6. Check your GitHub repository to confirm your changes appear.

Commit as you make progress. Follow the Canvas assignment instructions for your final submission.
