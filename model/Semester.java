package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "SEMESTERS")
public class Semester implements Comparable<Semester>{

    @Column(length = 16, nullable = false)
    private String title;
    private java.time.LocalDate startDate;

    // Surrogate key
    @Id
    @Column(name = "SEMESTER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int semesterId;

    // Association fields.
    @OneToMany(mappedBy = "semester")
    private List<Section> sections;

    // Constructor
    public Semester() {
    }

    public Semester(String title, java.time.LocalDate startDate) {
        this.title = title;
        this.startDate = startDate;
        sections = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public int getSemesterId() {
        return semesterId;
    }

    /**
     * Helper function for bidirectional association for section
     * @param s section being added
     */
    public void addSection(Section s) {
        this.sections.add(s);
        s.setSemester(this);
    }

    @Override
    public String toString() {
        return title + " " + startDate;
    }

    @Override
    public int compareTo(Semester semester) {
        return this.getStartDate().compareTo(semester.getStartDate());
    }
}
