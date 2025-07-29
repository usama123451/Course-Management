package courseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a course in the system.
 * It stores course details and a list of enrolled students.
 */
public class Course {
    private String courseId;
    private int maxStudents;
    private List<String> enrolledStudents = new ArrayList<>();

    /**
     * Constructor for the Course class.
     * @param courseId The unique identifier for the course.
     * @param maxStudents The maximum number of students that can enroll.
     */
    public Course(String courseId, int maxStudents) {
        this.courseId = courseId;
        this.maxStudents = maxStudents;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    /**
     * Returns a sorted list of enrolled student IDs.
     * @return A sorted list of strings.
     */
    public List<String> getEnrolledStudents() {
        Collections.sort(enrolledStudents);
        return enrolledStudents;
    }

    /**
     * Adds a student to the course if not already full or enrolled.
     * @param studentId The ID of the student to enroll.
     * @throws Exception if the course is full or the student is already enrolled.
     */
    public void addStudent(String studentId) throws Exception {
        if (enrolledStudents.size() >= maxStudents) {
            throw new Exception("Course " + courseId + " is already at maximum capacity.");
        }
        if (enrolledStudents.contains(studentId)) {
            throw new Exception("Student " + studentId + " is already enrolled in course " + courseId + ".");
        }
        enrolledStudents.add(studentId);
    }

    /**
     * Checks if a specific student is enrolled in this course.
     * @param studentId The ID of the student to check.
     * @return true if the student is enrolled, false otherwise.
     */
    public boolean isStudentEnrolled(String studentId) {
        return enrolledStudents.contains(studentId);
    }
}