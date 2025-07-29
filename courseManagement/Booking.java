package courseManagement;

/**
 * Represents a student's booking for an exam.
 * It holds booking details like status, grade, and associated IDs.
 */
public class Booking {
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        COMPLETED
    }

    private int bookingNumber;
    private String studentId;
    private String examId;
    private BookingStatus status;
    private int grade = -1; // -1 indicates no grade has been set

    /**
     * Constructor for the Booking class.
     * @param bookingNumber A unique, sequential number for the booking.
     * @param studentId The ID of the student making the booking.
     * @param examId The ID of the exam being booked.
     */
    public Booking(int bookingNumber, String studentId, String examId) {
        this.bookingNumber = bookingNumber;
        this.studentId = studentId;
        this.examId = examId;
        this.status = BookingStatus.PENDING; // Initial state is PENDING
    }

    public int getBookingNumber() {
        return bookingNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExamId() {
        return examId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}