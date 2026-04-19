package entityClasses;

import entityClasses.CoverageChecker.Result;
import entityClasses.CoverageChecker.StudentCoverage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*******
 * <p> Title: GradeExporter Class (TP3). </p>
 *
 * <p> Description: This class exports the {@link CoverageChecker.Result} to a CSV or
 * plain-text file that the grader can save outside the application.  The exported file
 * contains one row per student with the following columns: username, total posts, total
 * replies given, unique askers replied to, and pass/fail label. </p>
 *
 * <p> The class is intentionally a pure-Java utility with no JavaFX dependencies, no
 * database access, and no side effects beyond writing the output file.  This makes it
 * easy to test in the same semi-automated pattern used throughout the project. </p>
 *
 * <p><b>Staff User Stories satisfied:</b></p>
 * <ul>
 *   <li><b>US-G4</b> — Export coverage results to a CSV/text file.</li>
 *   <li><b>US-G5</b> — Include quality marks in the export (column reserved; "Not reviewed"
 *       shown when no marks exist).</li>
 * </ul>
 *
 * <p><b>Tested by:</b> {@code GradeExporterTests} (TP3 test suite). </p>
 *
 * <p> Copyright: CSE 360 Team 21 &copy; 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-04-10  Initial TP3 version
 */
public class GradeExporter {

    /** CSV column separator. Tab-separated is easier to read in Notepad than comma. */
    private static final String SEP = ",";

    /** Header row for the CSV file. */
    private static final String HEADER =
            "Username" + SEP + "Total Posts" + SEP + "Total Replies Given" + SEP +
            "Unique Askers Replied To" + SEP + "Pass/Fail" + SEP + "Quality Marks";


    /**********
     * <p> Method: export(Result, PostList, ReplyList, File) </p>
     *
     * <p> Description: Writes the coverage results to the specified file in CSV format.
     * For each student in the result, counts their total posts (from PostList) and total
     * replies (from ReplyList) to provide a complete row.  The "Quality Marks" column is
     * reserved for the Answer Quality Review tool — if no marks exist for a student, the
     * column shows "Not reviewed". </p>
     *
     * <p> Returns an empty string on success or an error message on failure, following the
     * same convention used by {@code PostList} and {@code ReplyList}. </p>
     *
     * @param result     the CoverageChecker result to export
     * @param postList   the PostList used to count total posts per student
     * @param replyList  the ReplyList used to count total replies per student
     * @param outputFile the File to write the CSV to
     * @return           empty string on success, error message on failure
     *
     * @see CoverageChecker
     * @see CoverageChecker.Result
     */
    public String export(Result result, PostList postList, ReplyList replyList, File outputFile) {

        // Defensive null checks — same pattern as CoverageChecker
        if (result == null) {
            return "No coverage results to export.";
        }
        if (outputFile == null) {
            return "Output file path is required.";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            // Write header
            writer.write(HEADER);
            writer.newLine();

            // Write one row per student
            for (StudentCoverage row : result.getCoverageByUser().values()) {
                String username = row.getUsername();

                // Count total posts by this student
                int totalPosts = 0;
                if (postList != null) {
                    List<Post> studentPosts = postList.getMyPosts(username);
                    totalPosts = studentPosts.size();
                }

                // Count total replies given by this student
                int totalReplies = 0;
                if (replyList != null) {
                    for (Reply r : replyList.getAllReplies()) {
                        if (username.equals(r.getAuthor())) {
                            totalReplies++;
                        }
                    }
                }

                // US-G5: quality marks column — reserved for Answer Quality Review tool
                // For now, show "Not reviewed" since Hasib's tool stores marks on Reply objects
                // When Hasib adds a getQualityMark() method to Reply, this line will be updated
                String qualityMarks = "Not reviewed";

                // Build and write the row
                StringBuilder line = new StringBuilder();
                line.append(username).append(SEP);
                line.append(totalPosts).append(SEP);
                line.append(totalReplies).append(SEP);
                line.append(row.getCount()).append(SEP);
                line.append(row.getLabel()).append(SEP);
                line.append(qualityMarks);

                writer.write(line.toString());
                writer.newLine();
            }

            writer.flush();
            System.out.println("** Grade export written to: " + outputFile.getAbsolutePath());
            return "";

        } catch (IOException e) {
            return "Failed to write export file: " + e.getMessage();
        }
    }


    /**********
     * <p> Method: exportToString(Result, PostList, ReplyList) </p>
     *
     * <p> Description: Same as {@link #export(Result, PostList, ReplyList, File)} but returns
     * the CSV content as a String instead of writing to a file.  Useful for displaying a
     * preview in the GUI before the grader decides to save, and for testing without creating
     * actual files on disk. </p>
     *
     * @param result     the CoverageChecker result to export
     * @param postList   the PostList used to count total posts per student
     * @param replyList  the ReplyList used to count total replies per student
     * @return           the CSV content as a String, or an error message if result is null
     *
     * @see #export(Result, PostList, ReplyList, File)
     */
    public String exportToString(Result result, PostList postList, ReplyList replyList) {

        if (result == null) {
            return "No coverage results to export.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(HEADER).append("\n");

        for (StudentCoverage row : result.getCoverageByUser().values()) {
            String username = row.getUsername();

            int totalPosts = 0;
            if (postList != null) {
                totalPosts = postList.getMyPosts(username).size();
            }

            int totalReplies = 0;
            if (replyList != null) {
                for (Reply r : replyList.getAllReplies()) {
                    if (username.equals(r.getAuthor())) {
                        totalReplies++;
                    }
                }
            }

            String qualityMarks = "Not reviewed";

            sb.append(username).append(SEP);
            sb.append(totalPosts).append(SEP);
            sb.append(totalReplies).append(SEP);
            sb.append(row.getCount()).append(SEP);
            sb.append(row.getLabel()).append(SEP);
            sb.append(qualityMarks).append("\n");
        }

        return sb.toString();
    }
}
