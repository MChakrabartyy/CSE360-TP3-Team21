package entityClasses;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*******
 * <p> Title: CoverageChecker Class. </p>
 *
 * <p> Description: This class is the TP3 production version (ported from HW3 prototype) for the Coverage Checker aspect of
 * TP3.  Its single responsibility is to compute, for every student in the system, the set of
 * distinct OTHER students whose questions that student has answered, and to label each
 * student PASS or FAIL against a configurable threshold.  The default threshold is 3, which
 * matches the HW3 rubric language: "each student has answered the questions from at least
 * three other students." </p>
 *
 * <p> The class is intentionally a pure-Java computation: it reads from {@code PostList} and
 * {@code ReplyList} but never writes anywhere, never opens a JavaFX window, never touches the
 * H2 database, and never makes a network call.  This makes it easy to unit-test in the same
 * StudentPostTests / UserTests / SingleRoleDispatchTests pattern used everywhere else in this
 * project (a {@code main} method and a {@code performTest} helper, no external test
 * framework). </p>
 *
 * <p><b>How the algorithm works:</b></p>
 * <ol>
 *   <li>Defensive null checks: if either input is {@code null}, return an empty result map.
 *       The grader UI must never get a NullPointerException out of this class.</li>
 *   <li>Walk every post once.  For each post, ensure the post-author has an entry in the
 *       result map (even if that author never replies to anyone).  This satisfies CC-R13:
 *       "a student who has only posted should appear in the result with 0 distinct askers."
 *       </li>
 *   <li>Walk every reply once.  For each reply, look up the post the reply belongs to using
 *       {@code PostList.getPostByID(int)}.  If the post does not exist (orphan reply), skip
 *       it silently.  Otherwise:
 *       <ul>
 *         <li>Get the asker (the post's author) and the answerer (the reply's author).</li>
 *         <li>If asker == answerer, skip (CC-R7: self-replies do not count).</li>
 *         <li>Add the asker username to the answerer's distinct-askers set.</li>
 *       </ul>
 *       Replies to soft-deleted posts are still credited (CC-R8) because the answerer did
 *       the work; we should not punish them for the original asker later removing the post.
 *       </li>
 *   <li>Build the final {@link Result} object: for each student in the working map, count
 *       their distinct askers and label PASS if {@code count &gt;= threshold}, else FAIL.</li>
 * </ol>
 *
 * <p> The output uses a {@link TreeMap} so iteration order is alphabetical by username, which
 * makes the result deterministic (CC-R12) regardless of the order posts and replies were
 * created in. </p>
 *
 * <p> Copyright: CSE 360 Team 21 &copy; 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-04-07  Initial TP3 production version (ported from HW3 prototype)
 */
public class CoverageChecker {

    /** Default threshold: a student must have answered at least this many distinct other
     *  students to be labeled PASS.  Matches the HW3 rubric language ("at least three"). */
    public static final int DEFAULT_THRESHOLD = 3;

    /** Label used in the result for a student that meets or exceeds the threshold. */
    public static final String LABEL_PASS = "PASS";

    /** Label used in the result for a student that does not meet the threshold. */
    public static final String LABEL_FAIL = "FAIL";


    /*-******************************************************************************************

    Inner Result Types

    A small immutable record-style class for one student's coverage entry, plus a Result
    wrapper that holds the per-student map and the threshold that was used.  These are
    inner classes so the prototype is one self-contained file.

    */

    /**********
     * <p> Class: StudentCoverage </p>
     *
     * <p> Description: One row of the result.  Holds the student's username, the set of
     * distinct askers they have answered, the count, and the PASS/FAIL label.  Immutable
     * once constructed. </p>
     */
    public static class StudentCoverage {
        private final String username;
        private final Set<String> distinctAskers;
        private final int count;
        private final String label;

        /**
         * Constructs a coverage row.
         *
         * @param username        the student's username
         * @param distinctAskers  the set of OTHER students whose questions they have answered
         * @param label           "PASS" or "FAIL"
         */
        public StudentCoverage(String username, Set<String> distinctAskers, String label) {
            this.username = username;
            // Defensive copy and unmodifiable wrapper so callers cannot mutate the result
            this.distinctAskers = Collections.unmodifiableSet(
                    new LinkedHashSet<>(distinctAskers));
            this.count = this.distinctAskers.size();
            this.label = label;
        }

        /** @return the student's username */
        public String getUsername() { return username; }

        /** @return the unmodifiable set of distinct askers this student has answered */
        public Set<String> getDistinctAskers() { return distinctAskers; }

        /** @return the integer count of distinct askers (== distinctAskers.size()) */
        public int getCount() { return count; }

        /** @return "PASS" or "FAIL" */
        public String getLabel() { return label; }

        /**
         * Equality is based on username, count, label, and the set of askers.  Used by
         * the determinism test (CC-T5) to compare two result objects from two runs on
         * the same input.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StudentCoverage)) return false;
            StudentCoverage other = (StudentCoverage) o;
            return count == other.count
                && username.equals(other.username)
                && label.equals(other.label)
                && distinctAskers.equals(other.distinctAskers);
        }

        @Override
        public int hashCode() {
            return username.hashCode() * 31 + count;
        }
    }

    /**********
     * <p> Class: Result </p>
     *
     * <p> Description: The full result of one Coverage Checker run.  Holds a map from
     * username to StudentCoverage and the threshold that was used to label everyone.
     * The map is unmodifiable so the caller cannot accidentally mutate the result. </p>
     */
    public static class Result {
        private final Map<String, StudentCoverage> coverageByUser;
        private final int threshold;

        /**
         * Constructs a Result wrapping the given map and threshold.  The given map is
         * wrapped in an unmodifiable view so the caller cannot mutate it.
         *
         * @param coverageByUser  per-username coverage rows
         * @param threshold       the threshold used to label PASS/FAIL
         */
        public Result(Map<String, StudentCoverage> coverageByUser, int threshold) {
            this.coverageByUser =
                    Collections.unmodifiableMap(new TreeMap<>(coverageByUser));
            this.threshold = threshold;
        }

        /** @return the unmodifiable per-user coverage map (sorted alphabetically) */
        public Map<String, StudentCoverage> getCoverageByUser() { return coverageByUser; }

        /** @return the threshold value used to label PASS/FAIL */
        public int getThreshold() { return threshold; }

        /** @return number of students in this result */
        public int size() { return coverageByUser.size(); }

        /** @return true if no students are in this result */
        public boolean isEmpty() { return coverageByUser.isEmpty(); }

        /**
         * Convenience accessor.
         *
         * @param username the student to look up
         * @return that student's coverage row, or {@code null} if no such student
         */
        public StudentCoverage get(String username) {
            return coverageByUser.get(username);
        }

        /**
         * Equality is based on the threshold and the per-user map.  Used by the
         * determinism test (CC-T5).
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Result)) return false;
            Result other = (Result) o;
            return threshold == other.threshold
                && coverageByUser.equals(other.coverageByUser);
        }

        @Override
        public int hashCode() {
            return coverageByUser.hashCode() * 31 + threshold;
        }
    }


    /*-******************************************************************************************

    Public API

    */

    /**********
     * <p> Method: check(PostList, ReplyList) </p>
     *
     * <p> Description: Convenience overload that runs the checker with the default
     * threshold ({@link #DEFAULT_THRESHOLD}, which is 3). </p>
     *
     * @param postList   the PostList containing all posts in the system; may be {@code null}
     * @param replyList  the ReplyList containing all replies in the system; may be {@code null}
     * @return           a {@link Result} mapping each student to their coverage row
     */
    public Result check(PostList postList, ReplyList replyList) {
        return check(postList, replyList, DEFAULT_THRESHOLD);
    }

    /**********
     * <p> Method: check(PostList, ReplyList, int) </p>
     *
     * <p> Description: The main computation method.  See the class-level Javadoc for the
     * full algorithm.  Returns an empty {@link Result} (not {@code null}) if either input
     * is {@code null}, so the grader UI never gets a NullPointerException. </p>
     *
     * @param postList   the PostList containing all posts in the system; may be {@code null}
     * @param replyList  the ReplyList containing all replies in the system; may be {@code null}
     * @param threshold  the minimum number of distinct askers required for PASS; the rubric
     *                   default is 3 but the value is configurable so an instructor can
     *                   change it next semester (CC-R9)
     * @return           a non-null {@link Result} mapping every student to their coverage row
     */
    public Result check(PostList postList, ReplyList replyList, int threshold) {

        // Working map keyed by student username.  We use a TreeMap so the final result is
        // sorted alphabetically and therefore deterministic (CC-R12).  The value is a
        // LinkedHashSet so the askers within one row also keep insertion order, which makes
        // debugging easier.
        Map<String, Set<String>> workingAskers = new TreeMap<>();

        // Defensive null check: per CC-R11, the checker must never throw on null input.
        // We return an empty result rather than crashing because an empty system is a
        // normal state, not an error.
        if (postList == null || replyList == null) {
            return new Result(new TreeMap<>(), threshold);
        }

        // Step 1: walk every post.  For each post, make sure the author has an entry in
        // the working map, even if their entry stays empty.  This satisfies CC-R13: a
        // student who has only posted (never replied) must still appear in the result so
        // the grader can see them in the list.
        List<Post> allPosts = postList.getAllPosts();
        if (allPosts != null) {
            for (Post post : allPosts) {
                String author = post.getAuthor();
                if (author == null) continue;          // skip malformed posts defensively
                ensureEntry(workingAskers, author);
            }
        }

        // Step 2: walk every reply.  For each reply, find the post it belongs to via
        // PostList.getPostByID.  Skip orphan replies (post not found).  For valid replies,
        // skip self-replies (CC-R7), and otherwise add the asker username to the
        // answerer's distinct-askers set.  Replies to soft-deleted posts are still
        // credited (CC-R8) because we do not look at post.isDeleted() here at all.
        List<Reply> allReplies = replyList.getAllReplies();
        if (allReplies != null) {
            for (Reply reply : allReplies) {
                String answerer = reply.getAuthor();
                if (answerer == null) continue;        // skip malformed replies defensively

                Post parent = postList.getPostByID(reply.getPostID());
                if (parent == null) continue;          // orphan reply, no asker known

                String asker = parent.getAuthor();
                if (asker == null) continue;           // malformed parent

                // CC-R7: self-replies do not count toward your own coverage
                if (asker.equals(answerer)) continue;

                // Make sure the answerer has an entry too -- they may have replied without
                // ever having posted anything themselves
                ensureEntry(workingAskers, answerer);

                // The actual data move: record that 'answerer' has answered 'asker'
                workingAskers.get(answerer).add(asker);
            }
        }

        // Step 3: build the final per-user result map by counting and labeling.
        Map<String, StudentCoverage> finalMap = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : workingAskers.entrySet()) {
            String username = entry.getKey();
            Set<String> askers = entry.getValue();
            String label = (askers.size() >= threshold) ? LABEL_PASS : LABEL_FAIL;
            finalMap.put(username, new StudentCoverage(username, askers, label));
        }

        return new Result(finalMap, threshold);
    }

    /**********
     * <p> Method: toReport(Result) </p>
     *
     * <p> Description: Renders a Result object as a multi-line human-readable string for
     * display on the grader screen or for printing during a screencast demo.  This is the
     * helper required by CC-R14: the grader UI should not have to re-implement the
     * formatting itself. </p>
     *
     * <p> The output format is one line per student, of the form: </p>
     * <pre>
     *   alice    count=2  label=FAIL  askers=[bob, carol]
     *   bob      count=3  label=PASS  askers=[alice, carol, dave]
     * </pre>
     *
     * @param result the Result object to render; if {@code null} or empty, returns a
     *               clear "no data" placeholder string
     * @return       a multi-line string describing the result
     */
    public String toReport(Result result) {
        if (result == null || result.isEmpty()) {
            return "Coverage Checker: no data.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Coverage Checker (threshold=").append(result.getThreshold()).append(")\n");
        for (StudentCoverage row : result.getCoverageByUser().values()) {
            sb.append(row.getUsername())
              .append("  count=").append(row.getCount())
              .append("  label=").append(row.getLabel())
              .append("  askers=").append(new ArrayList<>(row.getDistinctAskers()))
              .append("\n");
        }
        return sb.toString();
    }


    /*-******************************************************************************************

    Private helpers

    */

    /**********
     * <p> Method: ensureEntry(Map, String) </p>
     *
     * <p> Description: Helper that guarantees the working map has an entry for the given
     * username.  If no entry exists yet, an empty LinkedHashSet is created.  If one already
     * exists, this is a no-op. </p>
     *
     * <p> Why a helper: the same insert-if-absent pattern is used in both Step 1 (every
     * post-author must appear) and Step 2 (every replier must appear).  Putting the
     * pattern in one method removes duplication and makes the algorithm steps easier to
     * read in the main method. </p>
     *
     * @param map       the working map being built
     * @param username  the user to ensure has an entry
     */
    private void ensureEntry(Map<String, Set<String>> map, String username) {
        if (!map.containsKey(username)) {
            map.put(username, new LinkedHashSet<>());
        }
    }
}
