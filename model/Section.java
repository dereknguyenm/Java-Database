package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "SECTIONS")
@Table(uniqueConstraints= {
        @UniqueConstraint(columnNames = {"SEMESTER_ID", "COURSE_ID", "SECTIONNUMBER"})
})
public class Section {

    // There are a very small number of sections for each class. Byte is ideal here since it holds the smallest numerical values
    @Column(nullable = false)
    private byte sectionNumber;

    // Some classes can hold over 255 students. Short is the next smallest numerical data type
    private short maxCapacity;

    // Surrogate key
    @Id
    @Column(name = "SECTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sectionId;

    // Association fields.
    @ManyToOne
    private TimeSlot timeslot;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "SEMESTER_ID")
    private Semester semester;

    @ManyToMany
    @JoinTable(
            name = "ENROLLMENT",
            joinColumns = @JoinColumn(name = "SECTION_ID"),
            inverseJoinColumns = @JoinColumn(name = "STUDENT_ID")
    )
    private Set<Student> enrollment;

    @OneToMany(mappedBy = "section")
    private Set<Transcript> transcripts;

    // Constructor
    public Section() {
    }

    public Section(byte sectionNumber, short maxCapacity, Course course, Semester semester, TimeSlot timeslot) {
        this.sectionNumber = sectionNumber;
        this.maxCapacity = maxCapacity;
        this.course = course;
        this.semester = semester;
        this.timeslot = timeslot;
        enrollment = new HashSet<>();
        transcripts = new HashSet<>();
    }

    public short getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(byte sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public short getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(short maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public TimeSlot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(TimeSlot timeslot) {
        this.timeslot = timeslot;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Set<Student> getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Set<Student> enrollment) {
        this.enrollment = enrollment;
    }

    public Set<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(Set<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    public int getSectionId() {
        return sectionId;
    }

    /**
     * Helper function for bidirectional association for semester
     * @param s semester being added/set
     */
    public void addSemester(Semester s) {
        this.semester = s;
        List<Section> secs = s.getSections();
        secs.add(this);
        s.setSections(secs);
    }

    /**
     * Bidirectional association for Student
     * @param s student being added/set
     */
    public void addStudent(Student s) {
        this.enrollment.add(s);
        Set<Section> secs = s.getSections();
        secs.add(this);
        s.setSections(secs);
    }

    @Override
    public String toString() {
        return "Course: " + course.getDepartment().getAbbreviation() + " " + course.getNumber() + ". Section number: " + sectionNumber + ". Max capacity: " + maxCapacity;
    }
}
