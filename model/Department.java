package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "DEPARTMENTS")
public class Department {

    @Column(length = 128, nullable = false)
    private String name;
    @Column(length = 8)
    private String abbreviation;

    // Surrogate Key
    @Id
    @Column(name = "DEPARTMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;

    // Association fields
    @OneToMany(mappedBy = "department")
    private List<Course> courses;

    // Constructor
    public Department() {
    }

    public Department(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
        courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * Helper function for bidirectional association for course
     * @param c Course being added
     */
    public void addCourse(Course c) {
        this.courses.add(c);
        c.setDepartment(this);
    }

    @Override
    public String toString() {
        return "Name: " + name + " Abbreviation: " + abbreviation;
    }
}
