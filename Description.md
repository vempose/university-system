# Introduction

The current project is a **research-oriented university system**.

You should include:
- classes (**superclasses, subclasses, abstract classes**),
- interfaces,
- enumerations,
- your own exceptions,
- patterns (**to be studied next week**),
- etc.

---

# Requirements

We have some general requirements for this project. You should use and follow the following requirements:

- **Object-oriented style**:
  - low coupling,
  - high cohesion,
  - usage of `Comparable`,
  - `Comparators`,
  - `equals`,
  - `hashCode`,
  - `toString`,
  - etc.
- Usage of **Java API** (standard classes)
- Consistency with UML and intuitive usage
- Any user should access the system via **authentication**
- Properly working **serialization** (think about Data Storage and some pattern)
- Proper usage of **enumerations**
  Example: teachers’ positions such as **tutor, lector, senior lector, professor**, etc.
- Proper and logically consistent usage of **Collections**
- **Documentation**

There won’t be a detailed description of the project, because it is not only a programming task, but also a **DESIGN** task. So, it is up to you which fields and methods will be in your classes.

Even though, it is required that we will be able to have the following classes:

- `User`
- `Employee`
- `Teacher`
- `Manager`
- `Student`
- `GraduateStudent` (can be Master or PhD student)
- `Admin`
- `Course`
- `Mark`
- `Lesson`
- `TechSupportSpecialist`
- `Researcher`
- `ResearchPaper`
- `ResearchProject`
- `News`
- `Message`

**Note:** You can also add other classes.

---

# System Specifications

## Most Important Functionality
- **Course registration**
- **Putting marks**
- **Research**

Try to finish these first.

In general, your system has to support the following processes and types:

- Lesson types:
  - lecture
  - practice

- Switching between languages:
  - KZ
  - EN
  - RU

- The class of general and graduated students

- A teacher must have a method to send a **complaint about student(s)** to a dean with urgency levels:
  - `LOW`
  - `MEDIUM`
  - `HIGH`

- Major, minor, free elective courses.
  Please note that for a **SITE** student, some major course from **Oil and Gas school** can be a free elective.

- News with comments.
  News with a topic **"Research"** must be prioritized in order (**pinned**).

- When some `Researcher` publishes a paper, there must be an announcement.

- Also, don’t forget to automatically generate news about the **top cited Researcher in the university**.

- The `Researcher` class must have a method to calculate **h-index**.

- All graduated students have their **research supervisor**, who is a `Researcher`.
  If a person whose **h-index < 3** is assigned as a supervisor, a **custom exception** must be thrown.

- There can be more than one instructor per course, i.e. for **lecture** and **practice** times separately.

- In the system, **teachers and students CAN be researchers**.
  - Those teachers who are **Professors** are always researchers.
  - **PhD** and **Master** students are always researchers.
  - However, **bachelor students** and other teachers (e.g. tutors, senior lecturers, etc.) can also be researchers.
  - Moreover, there can be some employees who are neither a teacher nor a student, but are researchers.
  - A `Researcher` has research project(s), research papers (also an Object!), etc.

- The fields for a `ResearchPaper` can be chosen from **LMS Logs and Student Performance: The Influence of Retaking a Course**.
  You can take **5–10 important ones**, such as:
  - citations,
  - name,
  - authors,
  - journal,
  - pages,
  - date,
  - doi,
  - etc.

- `Researcher` must have a method:

  `printPapers(Comparator c)`

  This method prints the researcher’s papers in sorted order dictated by the comparator:
  - by date published,
  - by citations,
  - by article length (use pages).

  Moreover, the system must support printing research papers of **all researchers in the university**, also sorted by:
  - date published,
  - citations,
  - article length.

  Additionally, there should be a method that supports printing the **top cited researcher of the school**, and **of the year** (among all schools).

- `ResearchPaper` must have a method:

  `String getCitation(Format f)`

  where format can be either:
  - `"Plain Text"`
  - `"Bibtex"`

  To see the text of formats, you can follow the link above and click the **"Cite this"** button. Then you can find both format texts.

- `ResearchProject` has:
  - a topic,
  - published papers,
  - project participant(s).

  If someone who is **not a Researcher** tries to join the `ResearchProject`, a **custom exception** must be thrown.

- Report generation:
  - about marks,
  - simple statistics.

- Additionally, there should be **"Working official messages"** about events inside the university
  (for example, booking a room when an exam is planned, and so on).

- Tech support specialists need to be able to see new requests, and they **CAN accept/reject** them.

  After getting and seeing the request, its status should be:
  - `VIEWED`

  Other possible statuses:
  - `ACCEPTED`
  - `REJECTED`
  - `DONE`

  Example requests:
  - fix a projector
  - fix a printer

- Graduated students must have a list of **published research paper(s) as diploma projects**.

- As it is a research university, it has its own **university research journals**.
  - All users in the university (**not only researchers**) can subscribe to some university journals.
  - The system must notify readers when a new paper is published in a journal they are subscribed to.
  - From time to time, new journals appear.
  - **Which pattern is this?**

- Use **4 or more design patterns**.

---

# General Checklist

## 1. Admin
- Manage users:
  - add,
  - remove,
  - update
- See log files about user actions

## 2. Teacher
- View courses
- Manage course
- Put marks
- View students, info about students
- Send messages to other employees
  (actually, any employee can send a message to any employee)
- Send complaints

## 3. Student
- View courses
- Register for courses
- View info about teacher of a specific course
- View marks
- View transcript
- Rate teachers
- Get transcript
- Student organizations:
  - student can be a member
  - student can be a head

## 4. Manager
- Assign courses to teachers
- Approve students registration
- Add courses for registration
  (specify for which major/year of study the course is intended)
- Manager types:
  - OR
  - Departments
  - etc.
  (**use enumeration**)
- Create statistical reports on academic performance
- Manage news
- View info about students and teachers in different ways, e.g.:
  - sorted by GPA,
  - alphabetically,
  - etc.
- View requests from employees
  (they have to be signed by dean/rector)

## 5. Researcher
- You should think about the `Researcher` class:
  - Is it an interface?
  - Abstract class?
  - Created using Decorator pattern?
  - Just employee?

Figure it out. There is no single answer.

---

# Important Notes

- Students can’t have more than **21 credits**
- Students can’t fail more than **3 times**
- `Mark` consists of:
  - 1st attestation,
  - 2nd attestation,
  - final
