package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "STUDENTS")
public class Student {

    //Most student IDs are 8 digits. The largest 8 digit number is 99999999 which is 26 bits. Int is used since it can hold up to 32 bits
    @Column(nullable = false, unique = true)
    private int studentID;

    @Column(length = 128, nullable = false)
    private String name;

    // Surrogate Key
    @Id
    @Column(name = "STUDENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentIdSurrogate;

    // Association fields
    @ManyToMany(mappedBy = "enrollment")
    private Set<Section> sections;

    @OneToMany(mappedBy = "student")
    private Set<Transcript> transcripts;

    // Constructor
    public Student() {
    }

    public Student(int studentID, String name) {
        this.studentID = studentID;
        this.name = name;
        sections = new HashSet<>();
        transcripts = new HashSet<>();
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public Set<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(Set<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    public int getStudentIdSurrogate() {
        return studentIdSurrogate;
    }

    /**
     * Helper function for bidirectional association for section
     * @param s section being added
     */
    public void addSection(Section s) {
        this.sections.add(s);
        Set<Student> enr = s.getEnrollment();
        enr.add(this);
        s.setEnrollment(enr);
    }

    /**
     * Calculates the GPA by multiplying each course's letter grade with its units, adding them all together,
     * and dividing the total points by the total units
     * @return student's GPA
     */
    public double getGPA() {
        int totalPoints = 0;
        int totalUnits = 0;
        for (Transcript t:
             transcripts) {
            totalPoints += letterGrade(t.getGradeEarned()) * t.getSection().getCourse().getUnits();
            totalUnits += t.getSection().getCourse().getUnits();
        }
        return (double)totalPoints / totalUnits;
    }

    /**
     * Helper method for getGPA to determine the points based on letter grade
     * @param letter String letter grade
     * @return int equivalent for letter grade
     */
    private int letterGrade(String letter) {
        return switch (letter) {
            case ("A") -> 4;
            case ("B") -> 3;
            case ("C") -> 2;
            case ("D") -> 1;
            default -> 0;
        };
    }

    /**
     * Attempts to register the current student to the section and returns a status message
     * @param s Section being added
     * @return The registration result status
     */
    public RegistrationResult registerForSection(Section s) {
        Set<Course> transcriptCourses = new HashSet<>();

        // Enrolled in the same section
        for(Section enrolledSections : sections) {
            if(s.getSectionId() == enrolledSections.getSectionId()) {
                return RegistrationResult.ENROLLED_IN_SECTION;
            }

            // Enrolled in another section of the same course
            if(s.getCourse().getCourseId() == enrolledSections.getCourse().getCourseId()) {
                return RegistrationResult.ENROLLED_IN_ANOTHER;
            }
        }

        for (Transcript t: this.transcripts) {
            //Adds the courses to the list of transcript courses for use in determining prerequisites
            transcriptCourses.add(t.getSection().getCourse());

            //Compares user course to the parameter course
            if(t.getSection().getCourse().getCourseId() == s.getCourse().getCourseId()) {
                // Passed the class with a C or greater
                if(letterGrade(t.getGradeEarned()) >= 2) {
                    return RegistrationResult.ALREADY_PASSED;
                }
            }
        }

        // Prerequisites
        for (Prerequisite p: s.getCourse().getPrerequisites()) {
            if(!transcriptCourses.contains(p.getCourse())) {
                return RegistrationResult.NO_PREREQUISITES;
            }
        }

        // Time conflict
        for (Section section: this.sections) {
            //Compare start times and end time
            java.time.LocalTime currentSectionStartTime = section.getTimeslot().getStartTime();
            java.time.LocalTime currentSectionEndTime = section.getTimeslot().getEndTime();
            java.time.LocalTime sStartTime = s.getTimeslot().getStartTime();
            byte enrolledDays = section.getTimeslot().getDaysOfWeek();
            byte sDays = s.getTimeslot().getDaysOfWeek();
            byte bitwiseANDDays = (byte) (enrolledDays | sDays);

            if(sStartTime.isAfter(currentSectionStartTime) && sStartTime.isBefore(currentSectionEndTime) && bitwiseANDDays > 0) {
                return RegistrationResult.TIME_CONFLICT;
            }
        }

        // Register success
        s.addStudent(this);
        this.addSection(s);
        return RegistrationResult.SUCCESS;
    }

    // Enum used for registration to give registration status
    public enum RegistrationResult {
        SUCCESS,
        ALREADY_PASSED,
        ENROLLED_IN_SECTION,
        NO_PREREQUISITES,
        ENROLLED_IN_ANOTHER,
        TIME_CONFLICT
    }

    @Override
    public String toString() {
        return "Student ID: " + studentID + " Name: " + name;
    }
}
