package courseManagement;

/**
 * Represents an exam for a specific course.
 */
public class Exam {
    private String examId;
    private String courseId;
    private String location;

    /**
     * Constructor for the Exam class.
     * @param examId The unique identifier for the exam.
     * @param courseId The ID of the course this exam belongs to.
     * @param location The location where the exam will be held.
     */
    public Exam(String examId, String courseId, String location) {
        this.examId = examId;
        this.courseId = courseId;
        this.location = location;
    }

    public String getExamId() {
        return examId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getLocation() {
        return location;
    }
}