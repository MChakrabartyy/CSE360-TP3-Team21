# CSE 360 — Team Project Phase 3 (TP3)
## Student Discussion System - Instructional Team Grading Tools

**Course:** CSE 360 Software Engineering · Arizona State University · Spring 2026  
**Team:** Team 21  
**Phase:** TP3 (Final Submission)  
**Deadline:** April 21, 2025

---

### Team Members

| Name | Role | Primary Responsibilities |
|------|------|--------------------------|
| Manisha Chakrabarty | Lead Coder + PM | Coverage Checker, Grade Export, Grader Home Page, GUI Integration, Final Assembly |
| Ali Ahmed | Coder | Grader Stats Dashboard |
| Sahil Ejanthkar | Documentation | Team Norms, Requirements Screencast, Execution Screencast |
| Sarim Fazal | Testing Lead | Staff User Stories, Filtered Post Viewer, JUnit Tests, Manual Tests |
| Haseeb Baher | Design Lead (UML) | Answer Quality Review, Architecture.pdf, Detailed Design.pdf |

---

### Project Overview

This project extends the FoundationsSP26 JavaFX application — a student discussion forum built on an H2 in-memory database. TP3 adds **instructional team (grader) tools** that allow graders to:

- **Verify student participation** — check that each student answered questions from at least 3 different students
- **View discussion statistics** — per-student post counts, reply counts, and pass/fail badges
- **Filter and browse posts** — by student, thread, type, and answered/unanswered status
- **Review answer quality** — mark replies as reasonable or not reasonable
- **Export grades** — generate a CSV file with all grading data

These tools satisfy the **Staff Epics** defined by the course instructors.

---

### Architecture

```
FoundationsSP26/
├── src/
│   ├── applicationMain/        # App entry point (FoundationsMain.java)
│   ├── database/               # H2 database layer (Database.java)
│   ├── entityClasses/          # Data model
│   │   ├── User.java           # User entity with 4 roles (admin, role1, role2, grader)
│   │   ├── Post.java           # Discussion post entity (TP2)
│   │   ├── PostList.java       # Post CRUD + validation (TP2)
│   │   ├── Reply.java          # Reply entity (HW2)
│   │   ├── ReplyList.java      # Reply CRUD + validation (HW2)
│   │   ├── CoverageChecker.java    # ★ TP3: Coverage analysis engine
│   │   └── GradeExporter.java      # ★ TP3: CSV grade export
│   ├── guiGraderHome/          # ★ TP3: Grader landing page (MVC)
│   ├── guiGraderStats/         # ★ TP3: Stats Dashboard (MVC) — Ali
│   ├── guiFilteredPosts/       # ★ TP3: Filtered Post Viewer (MVC) — Sarim
│   ├── guiAnswerQuality/       # ★ TP3: Answer Quality Review (MVC) — Hasib
│   ├── guiStudentPosts/        # Student Discussion page (TP2)
│   ├── guiAdminHome/           # Admin Home page
│   ├── guiRole1/               # Student Home page
│   ├── guiRole2/               # Reviewer Home page
│   ├── guiUserLogin/           # Login page
│   ├── guiUserUpdate/          # Account settings page
│   ├── guiFirstAdmin/          # First-time admin setup
│   ├── guiNewAccount/          # New account via invitation
│   ├── guiAddRemoveRoles/      # Admin: manage user roles
│   ├── guiAdminResetPassword/  # Admin: reset passwords
│   ├── guiMultipleRoleDispatch/ # Multi-role user dispatch
│   ├── guiTools/               # Utility: single role dispatch
│   ├── Validators/             # Email + Username validators
│   ├── validator/              # Password validator (FSM-based)
│   ├── HW2/                    # HW2 test automation
│   └── HW3/                    # HW3 prototype + boundary tests
```

---

### TP3 New Features (Staff Epics)

#### 1. Coverage Checker (`entityClasses/CoverageChecker.java`)
Pure-Java computation engine that analyzes student discussion participation:
- Counts how many **distinct** students each student has replied to
- Labels each student PASS or FAIL against a configurable threshold (default: 3)
- Handles edge cases: self-replies excluded, soft-deleted posts still credited, deterministic output
- 39 automated tests passing (ported from HW3 prototype)

#### 2. Grade Export (`entityClasses/GradeExporter.java`)
Exports grading data to CSV format:
- Columns: Username, Total Posts, Total Replies, Unique Askers, Pass/Fail, Quality Marks
- Two methods: `export()` writes to file, `exportToString()` returns preview
- Quality Marks column integrates with Answer Quality Review tool

#### 3. Grader Home Page (`guiGraderHome/`)
New "Grader" role added to the system with its own:
- Login flow (graderRole boolean in User + Database)
- Dispatch logic (single-role + multi-role)
- Landing page with buttons for all 5 grader tools
- Admin can invite users with "Grader" role

#### 4. Grader Stats Dashboard (`guiGraderStats/`) — Ali
Per-student table view showing post counts, reply counts, unique askers, and pass/fail badges.

#### 5. Filtered Post Viewer (`guiFilteredPosts/`) — Sarim
Filter posts by student name, thread, post type, and answered/unanswered status.

#### 6. Answer Quality Review (`guiAnswerQuality/`) — Hasib
Side-by-side view of reply + original question with reasonable/not-reasonable marking.

---

### Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17+ |
| GUI Framework | JavaFX |
| Database | H2 (in-memory) |
| IDE | Eclipse |
| UML Diagrams | Astah |
| Version Control | Git / GitHub |
| Build | Eclipse Project (no Maven/Gradle) |

---

### How to Run

1. Clone this repository
2. Open Eclipse → File → Import → Existing Projects into Workspace
3. Select the `FoundationsSP26` folder
4. Ensure JavaFX and H2 libraries are on the build path
5. Run `applicationMain/FoundationsMain.java` as Java Application
6. First run: set up admin account → invite a user with "Grader" role → log in as grader

### How to Run Tests

| Test Suite | Package | How to Run | Expected |
|-----------|---------|-----------|----------|
| CoverageCheckerTests | HW3 | Run As → Java Application | 39 passed, 0 failed |
| StudentPostTests | guiStudentPostTests | Run As → Java Application | 35 passed, 0 failed |
| PostReplyTestingAutomation | HW2 | Run As → Java Application | 37 passed, 0 failed |
| SingleRoleDispatchTests | HW3 | Run As → Java Application | 9 passed, 0 failed |
| UserTests | HW3 | Run As → Java Application | All passed |

---

### Design Patterns Used

- **Singleton** — All View classes (ViewGraderHome, ViewAdminHome, etc.) are instantiated once and reused
- **MVC** — Every GUI page follows Model-View-Controller separation
- **Strategy** — CoverageChecker accepts configurable threshold parameter
- **Defensive Programming** — Null checks, input validation, soft-delete preservation throughout

---

### Previous Phases

| Phase | Description | Key Deliverables |
|-------|-------------|-----------------|
| TP1 | Login, account management, role system | User entity, Database, Admin/Student/Reviewer roles |
| HW2 | Post & Reply CRUD with validation | Post.java, Reply.java, PostList.java, ReplyList.java |
| TP2 | Student Discussion System | guiStudentPosts MVC, soft-delete, thread filtering, search |
| HW3 | Security testing + TP3 prototyping | CWE/OWASP boundary tests, CoverageChecker prototype |
| **TP3** | **Grader tools (Staff Epics)** | **Coverage Checker, Grade Export, Stats Dashboard, Filtered Posts, Answer Quality** |

---

### License

This project was developed for CSE 360 at Arizona State University. Foundation code © Lynn Robert Carter 2025. Team additions © Team 21, 2026.
