package model;

import jakarta.persistence.*;

@Entity
public class Transcript implements Comparable<Transcript>{

    @Id
    @JoinColumn(name = "SECTION_ID")
    @ManyToOne
    private Section section;

    @Id
    @JoinColumn(name = "STUDENT_ID")
    @ManyToOne
    private Student student;

    @Id
    @Column(length = 2, name = "GRADE_EARNED")
    private String gradeEarned;

    // Constructor
    public Transcript() {
    }

    public Transcript(Section section, Student student, String gradeEarned) {
        this.section = section;
        this.student = student;
        this.gradeEarned = gradeEarned;
    }

    public Section getSection() {
        return section;
    }

    public Student getStudent() {
        return student;
    }

    public String getGradeEarned() {
        return gradeEarned;
    }

    @Override
    public String toString() {
        return "Grade earned: " + gradeEarned;
    }

    @Override
    public int compareTo(Transcript transcript) {
        return this.getSection().getSemester().compareTo(transcript.getSection().getSemester());
    }
}
