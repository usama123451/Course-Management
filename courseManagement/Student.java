package courseManagement;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student in the system.
 * It stores the student's ID and the courses they are enrolled in.
 */
public class Student {
    private String studentId;
    private Set<String> courseIds = new HashSet<>();

    /**
     * Constructor for the Student class.
     * @param studentId The unique identifier for the student.
     */
    public Student(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    /**
     * Adds a course to the student's set of enrolled courses.
     * @param courseId The ID of the course to add.
     */
    public void addCourse(String courseId) {
        courseIds.add(courseId);
    }

    /**
     * Checks if the student is enrolled in a specific course.
     * @param courseId The ID of the course to check.
     * @return true if the student is enrolled, false otherwise.
     */
    public boolean isEnrolledIn(String courseId) {
        return courseIds.contains(courseId);
    }
}