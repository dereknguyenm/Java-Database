package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "COURSES")
@Table(uniqueConstraints= {
        @UniqueConstraint(columnNames = {"DEPARTMENT_ID", "NUMBER"})
})
public class Course {

    @Column(length = 8, nullable = false)
    private String number;
    @Column(length = 64, nullable = false)
    private String title;

    //Units have a max value of 127 which is less than the maximum held by byte (255)
    private byte units;

    // Surrogate key
    @Id
    @Column(name = "COURSE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;

    // Association fields
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @OneToMany(mappedBy = "preceedingCourse")
    private Set<Prerequisite> prerequisites;

    // Constructor
    public Course() {
    }

    public Course(String number, String title, byte units, Department department) {
        this.number = number;
        this.title = title;
        this.units = units;
        this.department = department;
        prerequisites = new HashSet<>();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte getUnits() {
        return units;
    }

    public void setUnits(byte units) {
        this.units = units;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getCourseId() {
        return courseId;
    }

    /**
     * Helper function for bidirectional association for department
     * @param d department being added
     */
    public void addDepartment(Department d) {
        this.department = d;
        List<Course> deps = d.getCourses();
        deps.add(this);
        d.setCourses(deps);
    }

    @Override
    public String toString() {
        return "Title: " + title + " " + number + " Units: " + units;
    }
}
