import jakarta.persistence.*;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class  App {

    /**
     * Initializes the database schema with mock data
     * @param em entity manager to connect to database
     */
    private static void initializeSchema(EntityManager em) {

        //Creates objects and adds to database
        //Semesters
        em.getTransaction().begin();

        //Semester 1 Spring 2021
        Semester sem1 = new Semester("Spring 2021", LocalDate.of(2021, Month.JANUARY, 19));
        em.persist(sem1);

        //Semester 2 Fall 2021
        Semester sem2 = new Semester("Fall 2021", LocalDate.of(2021, Month.AUGUST, 17));
        em.persist(sem2);

        //Semester 3 Spring 2022
        Semester sem3 = new Semester("Spring 2022", LocalDate.of(2022, Month.JANUARY, 20));
        em.persist(sem3);
        em.getTransaction().commit();

        //Departments
        em.getTransaction().begin();

        //Department 1
        Department dep1 = new Department("Computer Engineering and Computer Science", "CECS");
        em.persist(dep1);

        //Department 2
        Department dep2 = new Department("Italian Studies", "ITAL");
        em.persist(dep2);
        em.getTransaction().commit();

        //Courses - Begins a new transaction before each course and commits. Should eliminate PK error
        //Course 1
        em.getTransaction().begin();
        Course course1 = new Course("174", "Introduction to Programming and Problem Solving", (byte) 3, dep1);
        em.persist(course1);
        em.getTransaction().commit();

        //Course 2
        em.getTransaction().begin();
        Course course2 = new Course("274", "Data Structures", (byte) 3, dep1);
        Set<Prerequisite> course2Prereq = course2.getPrerequisites();
        Prerequisite prereq1 = new Prerequisite(course2, course1, 'C');
        em.persist(prereq1);
        course2Prereq.add(prereq1);
        course2.setPrerequisites(course2Prereq);
        em.persist(course2);
        em.getTransaction().commit();

        //Course 3
        em.getTransaction().begin();
        Course course3 = new Course("277", "Object Oriented Application Programming", (byte) 3, dep1);
        Set<Prerequisite> course3Prereq = course3.getPrerequisites();
        course3Prereq.add(prereq1);
        course3.setPrerequisites(course3Prereq);
        em.persist(course3);
        em.getTransaction().commit();

        //Course 4
        em.getTransaction().begin();
        Course course4 = new Course("282", "Advanced C++", (byte) 3, dep1);
        Set<Prerequisite> course4Prereq = course4.getPrerequisites();
        Prerequisite prereq2 = new Prerequisite(course4, course2, 'C');
        Prerequisite prereq3 = new Prerequisite(course4, course3, 'C');
        em.persist(prereq2);
        em.persist(prereq3);
        course4Prereq.add(prereq2);
        course4Prereq.add(prereq3);
        course4.setPrerequisites(course4Prereq);
        em.persist(course4);
        em.getTransaction().commit();

        //Course 5
        em.getTransaction().begin();
        Course course5 = new Course("101A", "Fundamentals of Italian", (byte) 4, dep2);
        em.persist(course5);
        em.getTransaction().commit();

        //Course 6
        em.getTransaction().begin();
        Course course6 = new Course("101B", "Fundamentals of Italian", (byte) 4, dep2);
        Set<Prerequisite> course6Prereq = course6.getPrerequisites();
        Prerequisite prereq4 = new Prerequisite(course6, course5, 'D');
        em.persist(prereq4);
        course6Prereq.add(prereq4);
        course6.setPrerequisites(course6Prereq);
        em.persist(course6);
        em.getTransaction().commit();

        //Timeslots
        em.getTransaction().begin();

        //Timeslot 1 - MW 1230-1345
        TimeSlot ts1 = new TimeSlot(Byte.parseByte("0101000", 2), LocalTime.of(12, 30), LocalTime.of(13, 45));
        em.persist(ts1);

        //Timeslot 2 - TuTh 1400-1515
        TimeSlot ts2 = new TimeSlot(Byte.parseByte("0010100", 2), LocalTime.of(14, 0), LocalTime.of(15, 15));
        em.persist(ts2);

        //Timeslot 3 - MWF 1200-1250
        TimeSlot ts3 = new TimeSlot(Byte.parseByte("0101010", 2), LocalTime.of(12, 0), LocalTime.of(12, 50));
        em.persist(ts3);

        //Timeslot 4 - F 0800-1045
        TimeSlot ts4 = new TimeSlot(Byte.parseByte("0000010", 2), LocalTime.of(8, 0), LocalTime.of(10, 45));
        em.persist(ts4);
        em.getTransaction().commit();

        //Sections
        em.getTransaction().begin();

        //Section 1 - CECS 174 section 1, Spring 2021, MW 12:30 - 1:45, capacity 105
        Section sec1 = new Section((byte) 1, (short) 105, course1, sem1, ts1);
        em.persist(sec1);

        //Section 2 - CECS 274 section 1, Fall 2021, TuTh 2:00 - 3:15, capacity 140
        Section sec2 = new Section((byte) 1, (short) 140, course2, sem2, ts2);
        em.persist(sec2);

        //Section 3 - CECS 277 section 3, Fall 2021, F 8:00 - 10:45, capacity 35
        Section sec3 = new Section((byte) 3, (short) 35, course3, sem2, ts4);
        em.persist(sec3);

        //Section 4 - CECS 282 section 5, Spring 2022, TuTh 2:00 - 3:15, capacity 35
        Section sec4 = new Section((byte) 5, (short) 35, course4, sem3, ts2);
        em.persist(sec4);

        //Section 5 - CECS 277 section 1, Spring 2022, MW 12:30 - 1:45, capacity 35
        Section sec5 = new Section((byte) 1, (short) 35, course3, sem3, ts1);
        em.persist(sec5);

        //Section 6 - CECS 282 section 7, Spring 2022, MW 12:30 - 1:45, capacity 35
        Section sec6 = new Section((byte) 7, (short) 35, course4, sem3, ts1);
        em.persist(sec6);

        //Section 7 - ITAL 101A section 1, Spring 2022, MWF 12:00 - 12:50, capacity 25
        Section sec7 = new Section((byte) 1, (short) 25, course5, sem3, ts3);
        em.persist(sec7);
        em.getTransaction().commit();

        //Students - Begins a new transaction before each course and commits. Should eliminate PK error
        //Student 1
        em.getTransaction().begin();
        Student s1 = new Student(123456789, "Naomi Nagata");
        //Student 1 - Transcript
        Set<Transcript> s1tran = s1.getTranscripts();
        Transcript s1t1 = new Transcript(sec1, s1, "A");
        Transcript s1t2 = new Transcript(sec2, s1, "A");
        Transcript s1t3 = new Transcript(sec3, s1, "A");
        em.persist(s1t1);
        em.persist(s1t2);
        em.persist(s1t3);
        s1tran.add(s1t1);
        s1tran.add(s1t2);
        s1tran.add(s1t3);
        s1.setTranscripts(s1tran);
        //Student 1 - Enrolled Courses
        sec4.addStudent(s1);
        s1.addSection(sec4);
        em.persist(sec4);
        em.persist(s1);
        em.getTransaction().commit();

        //Student 2
        em.getTransaction().begin();
        Student s2 = new Student(987654321, "James Holden");
        //Student 2 - Transcript
        Set<Transcript> s2tran = s2.getTranscripts();
        Transcript s2t1 = new Transcript(sec1, s2, "C");
        Transcript s2t2 = new Transcript(sec2, s2, "C");
        Transcript s2t3 = new Transcript(sec3, s2, "C");
        em.persist(s2t1);
        em.persist(s2t2);
        em.persist(s2t3);
        s2tran.add(s2t1);
        s2tran.add(s2t2);
        s2tran.add(s2t3);
        s2.setTranscripts(s2tran);
        em.persist(s2);
        em.getTransaction().commit();

        //Student 3
        em.getTransaction().begin();
        Student s3 = new Student(555555555, "Amos Burton");
        //Student 3 - Transcript
        Set<Transcript> s3tran = s3.getTranscripts();
        Transcript s3t1 = new Transcript(sec1, s3, "C");
        Transcript s3t2 = new Transcript(sec2, s3, "B");
        Transcript s3t3 = new Transcript(sec3, s3, "D");
        em.persist(s3t1);
        em.persist(s3t2);
        em.persist(s3t3);
        s3tran.add(s3t1);
        s3tran.add(s3t2);
        s3tran.add(s3t3);
        s3.setTranscripts(s3tran);
        em.persist(s3);
        em.getTransaction().commit();

        System.out.println("Model initialization completed...");
    }

    /**
     * Presents the user with menu options and prompts for a menu selection. Performs the actions related to each menu selection
     * 1: initialize model
     * 2: student lookup
     * 3: class registration
     * @param em entity manager to connect to database
     */
    public static void menu(EntityManager em) {
        boolean repeatMenu = true;
        while (repeatMenu) {
            menuOptions();
            int userSelection = intInput(4);
            switch (userSelection) {
                case (1) -> initializeSchema(em);
                case (2) -> studentLookup(em);
                case (3) -> courseRegistration(em);
                case (4) -> {
                    return;
                }
                default -> {
                }
            }
            System.out.println("Would you like to go to the Main Menu?");
            repeatMenu = repeatMenuPrompt();
        }
    }

    /**
     * Prints menu options to the user
     */
    private static void menuOptions() {
        System.out.println("Welcome to the Course tool!");
        System.out.println("---------------------------");
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1. Instantiate model");
        System.out.println("2. Student lookup");
        System.out.println("3. Register for a course");
        System.out.println("4. Quit!");
    }

    /**
     * Prompts the user to go to main menu. y or Y = yes. n or N = no
     * Checks for invalid inputs
     * @return The boolean value to repeat or not
     */
    private static boolean repeatMenuPrompt() {
        System.out.println("yes [y | Y]. Press anything else for 'no'");
        String input = stringInput();
        return Objects.equals(input, "y") || Objects.equals(input, "Y");
    }

    /**
     * Prompts the user for an input between [1:limit]
     * Checks for invalid inputs
     * @return the user input
     */
    private static int intInput(int limit) {
        System.out.println("Please enter a valid input [1-" + limit + "]:");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        while(true) {
            try {
                if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > limit) {
                    System.out.println("Please enter a valid input [1-" + limit + "]:");
                    input = in.nextLine();
                } else {
                    return Integer.parseInt(input);
                }
            }
            catch(NumberFormatException nfe) {
                System.out.println("Please enter a valid input [1-" + limit + "]:");
                input = in.nextLine();
            }
        }
    }

    /**
     * The string input from console
     * @return The string input from console
     */
    private static String stringInput() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    /**
     * Looks up a student based on user input
     * Displays the courses in the student's transcript, sorted by semester
     * Displays the student's GPA
     * @param em entity manager to connect to DB
     */
    private static void studentLookup(EntityManager em) {

        boolean repeatMenu = true;
        while(repeatMenu) {
            System.out.println("Please enter a student name: ");
            String studentName = stringInput();

            //JDBC query
            TypedQuery<Student> namedStudent = em.createQuery("SELECT s FROM STUDENTS s WHERE s.name = ?1", Student.class);
            namedStudent.setParameter(1, studentName);
            try {
                Student studentLookup = namedStudent.getSingleResult();
                System.out.println("Student found: " + studentLookup);

                //Prints courses in transcript
                Set<Transcript> transcript = studentLookup.getTranscripts();

                //Sorts the transcript set by semester
                List<Transcript> sortedTranscripts = new ArrayList<>(transcript);
                Collections.sort(sortedTranscripts);

                //Prints each section in the transcript
                for (Transcript t: sortedTranscripts) {
                    Section sect = t.getSection();
                    Department dep = sect.getCourse().getDepartment();
                    Course course = sect.getCourse();
                    System.out.println(dep.getAbbreviation() + " " + course.getNumber() + ", " + sect.getSemester().getTitle() + ". Grade earned: " + t.getGradeEarned());
                }

                //GPA
                System.out.println("Total GPA: " + studentLookup.getGPA());

            } catch (NoResultException nre) {
                System.out.println("Student with name '" + studentName + "' not found!");
            }
            System.out.println("Would you like to try another name?");
            repeatMenu = repeatMenuPrompt();
        }
    }

    /**
     * Registers the student to a course
     * Checks parameters around the courses to make sure that it meets qualifications to be added
     * @param em Entity manager to connect to DB
     */
    private static void courseRegistration(EntityManager em) {
        //Semester selection
        int semester = semesterSelection();

        Student currentStudent = new Student();
        //Repeat student searches
        boolean repeatQuery = true;
        while (repeatQuery) {
            //Student name input
            System.out.println("Please enter a student name: ");
            String studentName = stringInput();

            //Student name search
            TypedQuery<Student> searchStudent = em.createQuery("SELECT s FROM STUDENTS s WHERE s.name = ?1", Student.class);
            searchStudent.setParameter(1, studentName);
            try {
                currentStudent = searchStudent.getSingleResult();
                System.out.println("Student found: " + currentStudent);
                repeatQuery = false;

            } catch (NoResultException nre) {
                System.out.println("Student with name '" + studentName + "' not found!");
            }
        }

        //Repeat for repeated course searches
        repeatQuery = true;
        while (repeatQuery) {
            //Course name input
            String[] courseComponents = courseInput();

            //Separated course details
            String depAbbr = courseComponents[0];
            String courseNumber = courseComponents[1];
            byte secNumber = Byte.parseByte(courseComponents[2]);

            //JPQL search query
            TypedQuery<Section> searchSection = em.createQuery("SELECT s FROM SECTIONS s INNER JOIN COURSES c on c.courseId = s.course.courseId " +
                    "WHERE c.department.abbreviation = ?1 AND c.number = ?2 AND s.sectionNumber = ?3", Section.class);
            searchSection.setParameter(1, depAbbr);
            searchSection.setParameter(2, courseNumber);
            searchSection.setParameter(3, secNumber);

            try {
                Section requestedSection = searchSection.getSingleResult();
                System.out.println("Searched course: " + requestedSection);

                //Register for the section upon successful registration status. Saves transaction to DB
                Student.RegistrationResult registrationStatus = currentStudent.registerForSection(requestedSection);
                System.out.println(registrationStatus);
                if(registrationStatus == Student.RegistrationResult.SUCCESS) {
                    currentStudent.addSection(requestedSection);
                    em.getTransaction().begin();
                    em.persist(currentStudent);
                    em.getTransaction().commit();
                }

            } catch (NoResultException nre) {
                System.out.println("No course found..");
            }
            System.out.println("Would you like to search for a different course?");
            repeatQuery = repeatMenuPrompt();
        }
    }

    /**
     * Prompts the user to select which semester they intend to register for
     * @return The int value corresponding to the semester
     */
    private static int semesterSelection() {
        System.out.println("What semester are you registering for? ");
        System.out.println("1. Spring 2021");
        System.out.println("2. Fall 2021");
        System.out.println("3. Spring 2022");
        return(intInput(3));
    }

    /**
     * Prompts the user to enter the course details for the class
     * Performs a rudimentary check on the input by checking the size of the split string array (3)
     * @return The course details in string array form
     */
    private static String[] courseInput() {
        while(true) {
            System.out.println("Please enter the name of the course section [ie: CECS 277-05]");
            String course = stringInput();
            String[] courseComp = course.split("[ -]");
            if(courseComp.length != 3) {
                System.out.println("Incorrect course input. Please try again!");
            }
            else {
                return courseComp;
            }
        }
    }

    //Main driver
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("demoDb");
        EntityManager em = factory.createEntityManager();
        menu(em);
    }
}
