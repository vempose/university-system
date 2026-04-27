## Final scope
The diagram should cover these modules first, in this order:
1. user/roles and authentication
2. course registration
3. marks and transcript-related data
4. research
5. news/messages/requests
6. tech support/admin/supporting utilities
The core of the project must stay centered on **course registration, putting marks, and research**. Everything else should support those flows, not dominate them.
## Task 1 — Core user hierarchy
Create the main inheritance structure:
* `User` — **abstract**
* `Employee` — **abstract**, extends `User`
* `Student` — extends `User`
* `GraduateStudent` — extends `Student`
* `Teacher` — extends `Employee`
* `Manager` — extends `Employee`
* `Admin` — extends `Employee`
* `TechSupportSpecialist` — extends `Employee`
Put only truly shared user data in `User`, such as:
* `id`
* `name`
* `email`
* `passwordHash`
* `language`
Put only shared user actions in `User`, such as:
* `login()`
* `logout()`
* `changeLanguage()`
This satisfies the required role set and keeps the inheritance tree clean.
## Task 2 — Add academic context classes
Add the small structural classes that make student/course rules realistic:
* `School`
* `Major`
Use them for:
* course targeting by school/major
* filtering users and courses
* top cited researcher **by school**
* cases like SITE student + Oil and Gas course classification
These are good-to-have extras and worth adding because they prevent awkward hardcoded strings later.
## Task 3 — Authentication and language support
Model authentication as a user-level responsibility, not a separate subsystem.
Required element:
* `Language` enum with `KZ`, `EN`, `RU`
Keep language as a field in `User`.
Do **not** add full role-based security or token/session classes for this project.
## Task 4 — Academic core classes
Create the main academic entities:
* `Course`
* `Lesson`
* `Enrollment` **(extra, must-have support class)**
* `Mark`
Why `Enrollment` is necessary:
* stores student-course registration
* supports approval workflow
* stores attempts / semester info
* links registration to its mark cleanly
Without `Enrollment`, both course registration and grading become messy.
## Task 5 — Course registration modeling
Keep `Course` lean. Suggested fields:
* `courseCode`
* `title`
* `credits`
Do **not** model major/minor/free-elective as a fixed property of `Course` alone, because the same course can count differently for different programs.
Add:
* `CourseRequirement` **(extra class)**
Suggested fields:
* `course`
* `major`
* `yearOfStudy`
* `category`
This supports:
* `MAJOR`
* `MINOR`
* `FREE_ELECTIVE`
and correctly handles cases like “for one student this course is major, for another it is free elective.”
## Task 6 — Lessons and instructors
Create `Lesson` to represent the teaching unit connected to a course.
Required support:
* lesson types `LECTURE`, `PRACTICE`
* different instructors for lecture and practice
Recommended relationship:
* `Course 1 --- 1..* Lesson`
* `Lesson 1 --- 1 Teacher`
This is the cleanest way to support multiple instructors per course without adding timetable complexity.
## Task 7 — Registration rules and student academic data
Add to `Student` the academic responsibilities:
* `viewCourses()`
* `registerForCourse()`
* `viewMarks()`
* `viewTranscript()`
* `getTranscript()`
Use UML notes for required constraints:
* max `21` credits
* cannot fail more than `3` times
Suggested `Enrollment` fields:
* `student`
* `course`
* `semesterLabel`
* `status`
* `attemptNo`
Keep transcript as derived from enrollments + marks. No separate heavy transcript subsystem is needed.
## Task 8 — Mark structure
Create `Mark` with the exact required parts:
* `firstAttestation`
* `secondAttestation`
* `finalExam`
Optional extra:
* `getTotal()`
* `isPassed()`
Do **not** split this into separate attestation classes; that would be unnecessary for this project.
## Task 9 — Teacher operations
Add the teacher responsibilities directly to `Teacher`:
* `viewCourses()`
* `manageCourse()`
* `putMark()`
* `viewStudents()`
* `sendMessage()`
* `sendComplaint()`
Add supporting class:
* `Complaint`
Suggested `Complaint` fields:
* `sender`
* `targetStudents`
* `urgency`
* `text`
* `receiver`
* `date`
Complaint receiver can be modeled as a `Manager` of dean type. That avoids creating a separate `Dean` hierarchy.
## Task 10 — Teacher rating
Add the missing student-to-teacher rating part:
* `TeacherRating` **(extra class)**
Suggested fields:
* `student`
* `teacher`
* `score`
* `comment`
Keep it minimal. This satisfies the checklist without introducing analytics logic.
## Task 11 — Student organizations
Add the required student organization module:
* `StudentOrganization`
* `OrganizationMembership` **(extra class)**
Why `OrganizationMembership` is useful:
* supports one student in multiple organizations
* stores role cleanly
Suggested membership fields:
* `student`
* `organization`
* `role`
Use enum values like `MEMBER`, `HEAD`.
## Task 12 — Manager responsibilities
Add to `Manager`:
* `assignTeacherToCourse()`
* `approveRegistration()`
* `addCourseForRegistration()`
* `createAcademicReport()`
* `manageNews()`
* `viewStudentsSorted()`
* `viewTeachersSorted()`
* `viewEmployeeRequests()`
Also add:
* `ManagerType` enum
Possible values:
* `OR`
* `DEPARTMENT`
* `DEAN`
This matches the checklist and keeps “dean” inside manager typing instead of making another top-level role.
## Task 13 — Researcher role
Model `Researcher` as an **interface**, not as a superclass.
Add:
* `Researcher` — interface
* `ResearchProfile` — concrete class implementing `Researcher`
Connect users to research by composition/association:
* `User 1 --- 0..1 ResearchProfile`
This is the most Java-friendly solution because:
* teachers can be researchers
* students can be researchers
* some employees can be researchers
* no multiple inheritance is needed
Required rule notes:
* `GraduateStudent` must have a research profile
* `Teacher` with `position = PROFESSOR` must have a research profile
* bachelor students and other teachers may optionally have one
* some non-teaching employees may also have one
## Task 14 — Research entities
Create:
* `ResearchPaper`
* `ResearchProject`
Suggested `ResearchPaper` fields:
* `title`
* `authors`
* `journalName`
* `pages`
* `publishDate`
* `doi`
* `citations`
Required methods:
* `getCitation(Format f)`
Suggested `ResearchProject` fields:
* `topic`
* `participants`
* `publishedPapers`
Required rule:
* only researchers may join a project
This is enough to satisfy the brief without modeling grant budgets, peer review, or external institutions.
## Task 15 — Graduate supervision and diploma papers
Add research-specific graduate student elements:
* `GraduateStudent` must reference a `Researcher` supervisor
* if supervisor `hIndex < 3`, throw a custom exception
* keep a list of diploma-related published papers
This can be modeled through:
* `GraduateStudent 1 --- 1 Researcher` as supervisor
* `GraduateStudent 1 --- 0..* ResearchPaper`
That directly satisfies the supervisor and diploma-paper requirements.
## Task 16 — Research service layer
Add a lightweight service/facade for university-wide research operations:
* `ResearchService` **(extra class)**
Responsibilities:
* `printAllPapers(Comparator<ResearchPaper> c)`
* `getTopCitedResearcherBySchool(School s)`
* `getTopCitedResearcherOfYear(int year)`
Keep `Researcher` responsible for:
* `calculateHIndex()`
* `printPapers(Comparator<ResearchPaper> c)`
This split avoids stuffing global logic into individual users.
## Task 17 — Journals and subscriptions
Add:
* `UniversityJournal`
* `JournalSubscription` **(good-to-have extra class)**
Why `JournalSubscription` helps:
* makes user-journal relation explicit
* can store subscribe date if needed later
* cleaner than a plain many-to-many line
Required behavior:
* all users can subscribe
* users are notified when a new paper is published
* new journals can appear over time
This is the cleanest place to use **Observer pattern**.
## Task 18 — News and comments
Create:
* `News`
* `NewsComment`
* `NewsService` **(extra class)**
Suggested `News` fields:
* `title`
* `content`
* `topic`
* `createdDate`
* `pinned`
Required behavior handled by `NewsService`:
* create announcement when researcher publishes paper
* automatically create news about top cited researcher
* pin news with topic `RESEARCH`
This keeps automation logic out of the `News` data class.
## Task 19 — Messaging and official requests
Create:
* `Message`
* `OfficialMessage` **(extra subclass)**
* `EmployeeRequest` **(extra class)**
Use them like this:
* any `Employee` can send `Message` to any `Employee`
* `OfficialMessage` covers working official messages
* `EmployeeRequest` covers requests viewed by managers and signed by dean/rector
Suggested `EmployeeRequest` fields:
* `sender`
* `description`
* `signedBy`
* `status`
This is clearer than trying to overload one generic message class for everything.
## Task 20 — Tech support workflow
Create:
* `TechSupportRequest`
Connect it to:
* requester (`User` or `Employee`, depending on your chosen restriction)
* `TechSupportSpecialist`
Suggested fields:
* `description`
* `status`
* `requester`
* `assignedSpecialist`
* `createdDate`
Required statuses:
* `NEW`
* `VIEWED`
* `ACCEPTED`
* `REJECTED`
* `DONE`
This is enough; do not build a full ticketing engine.
## Task 21 — Reports
Add:
* `AcademicReport` **(extra class)**
Responsibilities:
* simple mark reports
* simple statistics
Keep reports lightweight and manager-oriented.
No need for dashboards, charts, or a separate analytics module in UML.
## Task 22 — Admin and logging
Add:
* `LogEntry`
Add to `Admin`:
* `addUser()`
* `removeUser()`
* `updateUser()`
* `viewLogs()`
Suggested `LogEntry` fields:
* `actor`
* `action`
* `timestamp`
This satisfies the admin checklist and supports log viewing.
## Task 23 — Central system/storage class
Add one central system class:
* `UniversitySystem` **(good-to-have extra class)**
Use it for:
* storing collections
* serialization entry point
* high-level coordination
This is a good place for:
* singleton-style access
* repository-like storage
* save/load methods
This helps satisfy the serialization requirement without diagramming file/database details.
## Task 24 — Comparators and ordering support
Since the brief explicitly asks for `Comparable` and `Comparators`, add comparator support classes:
* `PaperByDateComparator`
* `PaperByCitationsComparator`
* `PaperByPagesComparator`
* `StudentByGpaComparator`
* `UserByNameComparator`
Also allow one or two natural-order `Comparable` implementations, for example:
* `User` by `id`
* `Course` by `courseCode`
These are small but useful good-to-have extras for the class diagram and later code.
## Task 25 — Enumerations
Add these enums:
* `Language { KZ, EN, RU }`
* `LessonType { LECTURE, PRACTICE }`
* `UrgencyLevel { LOW, MEDIUM, HIGH }`
* `TeacherPosition { TUTOR, LECTOR, SENIOR_LECTOR, PROFESSOR }`
* `ManagerType { OR, DEPARTMENT, DEAN }`
* `CourseCategory { MAJOR, MINOR, FREE_ELECTIVE }`
* `DegreeType { BACHELOR, MASTER, PHD }`
* `NewsTopic { RESEARCH, ACADEMIC, GENERAL }`
* `CitationFormat { PLAIN_TEXT, BIBTEX }`
* `RequestStatus { NEW, VIEWED, ACCEPTED, REJECTED, DONE }`
* `OrganizationRole { MEMBER, HEAD }`
* `EnrollmentStatus { PENDING, APPROVED, REJECTED, REGISTERED }`
These cover the requirement set without adding unnecessary enums.
## Task 26 — Custom exceptions
Add only the custom exceptions that are directly required or strongly justified:
* `InvalidSupervisorException`
* `NonResearcherJoinProjectException`
* `CreditLimitExceededException`
* `RetakeLimitExceededException`
Optional but still reasonable:
* `UnauthorizedRequestSigningException`
Keep exception count low.
## Task 27 — Design patterns to show in the UML
Use 4+ patterns, but keep them easy to defend in class:
* **Observer** — journal subscriptions and notifications
* **Singleton** — `UniversitySystem`
* **Strategy** — comparators for sorting papers/users/reports
* **Factory Method / Simple Factory** — creating user objects by role
* **Facade** — `ResearchService` / `NewsService` as simplified entry points
These are enough for the requirement and not hard to implement later.
## Task 28 — Final relationship pass
When drawing the final UML, make sure these important relations are visible:
* inheritance among user roles
* `User` optional link to `ResearchProfile`
* `Student` to `Enrollment`
* `Course` to `Enrollment`
* `Course` to `Lesson`
* `Lesson` to `Teacher`
* `Enrollment` to `Mark`
* `GraduateStudent` to supervisor `Researcher`
* `ResearchProfile` to `ResearchPaper`
* `ResearchProfile` to `ResearchProject`
* `UniversityJournal` to subscribers
* `News` to `NewsComment`
* `Employee` to `Message`
* `TechSupportRequest` to `TechSupportSpecialist`
* `Admin` to `LogEntry`
