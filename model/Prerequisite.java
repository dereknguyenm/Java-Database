package model;

import jakarta.persistence.*;

@Entity
public class Prerequisite {

    //Prerequisite course
    @Id
    @JoinColumn(name = "PRECEEDING_COURSE_ID", referencedColumnName = "COURSE_ID")
    @ManyToOne
    private Course preceedingCourse;

    //Current course
    @Id
    @JoinColumn(name = "COURSE_ID", referencedColumnName = "COURSE_ID")
    @OneToOne
    private Course course;

    @Column(name = "MINIMUM_GRADE")
    private char minimumGrade;

    // Constructor
    public Prerequisite() {
    }

    public Prerequisite(Course course, Course preceedingCourse, char minimumGrade) {
        this.preceedingCourse = preceedingCourse;
        this.course = course;
        this.minimumGrade = minimumGrade;
    }

    public Course getPreceedingCourse() {
        return preceedingCourse;
    }

    public Course getCourse() {
        return course;
    }

    public char getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(char minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    @Override
    public String toString() {
        return "Minimum grade: " + minimumGrade;
    }
}
