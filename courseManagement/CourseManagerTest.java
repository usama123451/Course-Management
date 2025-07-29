package courseManagement;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;


public class CourseManagerTest {

    private CourseManager manager;

    /**
     * This ensures that tests are independent and do not interfere with each other.
     */
    @Before
    public void setUp() {
        manager = new CourseManager();
    }

    /**
     * R1 Test: Verifies that a course can be added successfully.
     */
    @Test
    public void testAddCourse() throws Exception {
        manager.addCourse("CS101", 30);
        // To verify, we can try to enroll a student, which would fail if the course wasn't added.
        manager.enrollStudent("S001", "CS101");
        assertNotNull(manager.getEnrolledStudents("CS101"));
    }

    /**
     * R1 Test: Ensures that adding a course with a duplicate ID throws an exception.
     */
    @Test
    public void testAddCourseDuplicate() throws Exception {
        manager.addCourse("CS101", 30);
        try {
            manager.addCourse("CS101", 30);
            fail("Expected an Exception to be thrown for duplicate course ID.");
        } catch (Exception e) {
            assertEquals("Course ID CS101 already exists.", e.getMessage());
        }
    }

    /**
     * R1 Test: Checks that adding a course with student capacity below the valid range throws an exception.
     */
    @Test
    public void testAddCourseInvalidCapacityLow() {
        try {
            manager.addCourse("CS102", 9);
            fail("Expected an Exception to be thrown for capacity < 10.");
        } catch (Exception e) {
            assertEquals("Maximum number of students must be between 10 and 200.", e.getMessage());
        }
    }

    /**
     * R1 Test: Checks that adding a course with student capacity above the valid range throws an exception.
     */
    @Test
    public void testAddCourseInvalidCapacityHigh() {
        try {
            manager.addCourse("CS103", 201);
            fail("Expected an Exception to be thrown for capacity > 200.");
        } catch (Exception e) {
            assertEquals("Maximum number of students must be between 10 and 200.", e.getMessage());
        }
    }

    /**
     * R1 Test: Verifies successful student enrollment and retrieval.
     */
    @Test
    public void testEnrollStudentAndGetEnrolledStudents() throws Exception {
        manager.addCourse("PHY101", 15);
        manager.addCourse("CHE101", 20);
        manager.enrollStudent("S001", "PHY101", "CHE101");
        manager.enrollStudent("S002", "PHY101");

        List<String> students = manager.getEnrolledStudents("PHY101");
        assertEquals(2, students.size());
        assertEquals("S001", students.get(0)); // List should be sorted
        assertEquals("S002", students.get(1));
    }

    /**
     * R1 Test: Checks that enrolling a student with an already registered ID throws an exception.
     */
    @Test
    public void testEnrollDuplicateStudent() throws Exception {
        manager.addCourse("BIO101", 25);
        manager.enrollStudent("S003", "BIO101");

        try {
            manager.enrollStudent("S003", "BIO101");
            fail("Expected an Exception for enrolling a duplicate student.");
        } catch (Exception e) {
            assertEquals("Student ID S003 is already registered.", e.getMessage());
        }
    }

    /**
     * R1 Test: Ensures enrolling a student into a non-existent course throws an exception.
     */
    @Test
    public void testEnrollStudentInInvalidCourse() {
         try {
            manager.enrollStudent("S004", "NOSUCHCOURSE");
            fail("Expected an Exception for enrolling in a non-existent course.");
        } catch (Exception e) {
            assertEquals("Course ID NOSUCHCOURSE does not exist.", e.getMessage());
        }
    }

    /**
     * R2 Test: Verifies that an exam can be added successfully to a course.
     */
    @Test
    public void testAddExam() throws Exception {
        manager.addCourse("MAT202", 50);
        manager.addExam("MAT202", "M202-FINAL", "Room 101");

        Map<String, List<String>> exams = manager.getExamsByCourse();
        assertTrue(exams.containsKey("MAT202"));
        assertEquals(1, exams.get("MAT202").size());
        assertEquals("M202-FINAL", exams.get("MAT202").get(0));
    }

    /**
     * R2 Test: Ensures adding an exam for a non-existent course throws an exception.
     */
    @Test
    public void testAddExamForInvalidCourse() {
        try {
            manager.addExam("NOSUCHCOURSE", "EXAM01", "Room 102");
            fail("Expected an Exception for adding an exam to a non-existent course.");
        } catch (Exception e) {
            assertEquals("Course ID NOSUCHCOURSE does not exist.", e.getMessage());
        }
    }

    /**
     * R2 Test: Verifies that adding an exam with a duplicate ID throws an exception.
     */
    @Test
    public void testAddDuplicateExam() throws Exception {
        manager.addCourse("ENG101", 40);
        manager.addExam("ENG101", "E101-MID", "Hall A");

        try {
            manager.addExam("ENG101", "E101-MID", "Hall B");
            fail("Expected an Exception for adding an exam with a duplicate ID.");
        } catch (Exception e) {
            assertEquals("Exam ID E101-MID has already been used.", e.getMessage());
        }
    }

    /**
     * R2 Test: Checks if getExamsByCourse returns a correctly structured and sorted map.
     */
    @Test
    public void testGetExamsByCourse() throws Exception {
        manager.addCourse("C1", 20);
        manager.addCourse("C2", 20);
        manager.addExam("C1", "E2", "loc1");
        manager.addExam("C1", "E1", "loc2");
        manager.addExam("C2", "E3", "loc3");

        Map<String, List<String>> exams = manager.getExamsByCourse();
        assertEquals(2, exams.size());
        assertTrue(exams.containsKey("C1") && exams.containsKey("C2"));
        // Check if exam IDs are sorted alphabetically
        assertEquals("E1", exams.get("C1").get(0));
        assertEquals("E2", exams.get("C1").get(1));
    }

    /**
     * R3 Test: Verifies the full booking and confirmation process.
     */
    @Test
    public void testBookAndConfirmExam() throws Exception {
        manager.addCourse("HIS201", 30);
        manager.enrollStudent("S100", "HIS201");
        manager.addExam("HIS201", "H201-EXAM", "Library");

        int bookingNumber = manager.bookExam("S100", "H201-EXAM");
        assertEquals(1, bookingNumber); // First booking should have number 1

        // Booking should be PENDING, so confirmed list is empty
        assertTrue(manager.getConfirmedBookings().isEmpty());

        manager.confirmBooking(bookingNumber);

        List<Integer> confirmed = manager.getConfirmedBookings();
        assertEquals(1, confirmed.size());
        assertEquals(Integer.valueOf(bookingNumber), confirmed.get(0));
    }

    /**
     * R3 Test: Ensures booking an exam for a student not enrolled in the course throws an exception.
     */
    @Test
    public void testBookExamStudentNotEnrolled() throws Exception {
        manager.addCourse("C1", 20);
        manager.addCourse("C2", 20);
        manager.enrollStudent("S1", "C2");
        manager.addExam("C1", "E1", "loc1");

        try {
            manager.bookExam("S1", "E1");
            fail("Expected an Exception for booking exam when not enrolled.");
        } catch (Exception e) {
            assertEquals("Student S1 is not enrolled in the course for this exam.", e.getMessage());
        }
    }

    /**
     * R3 Test: Checks that confirming a non-existent booking throws an exception.
     */
    @Test
    public void testConfirmInvalidBooking() {
        try {
            manager.confirmBooking(999); // Invalid booking number
            fail("Expected an Exception for confirming an invalid booking.");
        } catch (Exception e) {
            assertEquals("Booking number 999 does not exist.", e.getMessage());
        }
    }

    /**
     * R4 Test: Verifies setting a grade for a confirmed booking and checks completed bookings.
     */
    @Test
    public void testSetGradeAndGetCompletedBookings() throws Exception {
        manager.addCourse("ART101", 20);
        manager.enrollStudent("S200", "ART101");
        manager.addExam("ART101", "A101-FINAL", "Studio 1");
        int bookingNumber = manager.bookExam("S200", "A101-FINAL");
        manager.confirmBooking(bookingNumber);

        // Before setting grade, completed list should be empty
        assertTrue(manager.getCompletedBookings().isEmpty());

        manager.setGrade(bookingNumber, 88);

        List<Integer> completed = manager.getCompletedBookings();
        assertEquals(1, completed.size());
        assertEquals(Integer.valueOf(bookingNumber), completed.get(0));
    }

    /**
     * R4 Test: Ensures setting a grade for a booking that is not confirmed throws an exception.
     */
    @Test
    public void testSetGradeForUnconfirmedBooking() throws Exception {
        manager.addCourse("C1", 20);
        manager.enrollStudent("S1", "C1");
        manager.addExam("C1", "E1", "loc1");
        int bookingNum = manager.bookExam("S1", "E1"); // Booking is PENDING

        try {
            manager.setGrade(bookingNum, 90);
            fail("Expected an Exception for setting a grade on an unconfirmed booking.");
        } catch (Exception e) {
            assertEquals("Booking " + bookingNum + " is not in CONFIRMED state.", e.getMessage());
        }
    }

    /**
     * R4 Test: Checks that setting an invalid grade (outside 0-100) throws an exception.
     */
    @Test
    public void testSetInvalidGrade() throws Exception {
        manager.addCourse("C1", 20);
        manager.enrollStudent("S1", "C1");
        manager.addExam("C1", "E1", "loc1");
        int bookingNum = manager.bookExam("S1", "E1");
        manager.confirmBooking(bookingNum);

        try {
            manager.setGrade(bookingNum, 101);
            fail("Expected an Exception for setting an invalid grade.");
        } catch (Exception e) {
            assertEquals("Grade must be between 0 and 100.", e.getMessage());
        }
    }

    /**
     * R5 Test: Verifies retrieval of all grades for a specific student.
     */
    @Test
    public void testGetStudentGrades() throws Exception {
        manager.addCourse("C1", 20);
        manager.addCourse("C2", 20);
        manager.enrollStudent("S1", "C1", "C2");
        manager.addExam("C1", "E1", "loc1");
        manager.addExam("C2", "E2", "loc2");

        int booking1 = manager.bookExam("S1", "E1");
        manager.confirmBooking(booking1);
        manager.setGrade(booking1, 95);

        int booking2 = manager.bookExam("S1", "E2");
        manager.confirmBooking(booking2);
        manager.setGrade(booking2, 85);

        Map<String, Integer> grades = manager.getStudentGrades("S1");
        assertEquals(2, grades.size());
        assertEquals(Integer.valueOf(95), grades.get("E1"));
        assertEquals(Integer.valueOf(85), grades.get("E2"));
    }

    /**
     * R5 Test: Calculates the average grade for courses.
     */
    @Test
    public void testGetCourseAverage() throws Exception {
        manager.addCourse("C1", 20);
        manager.addCourse("C2", 20);
        manager.enrollStudent("S1", "C1", "C2");
        manager.enrollStudent("S2", "C1");

        manager.addExam("C1", "E1", "loc1");
        manager.addExam("C2", "E2", "loc2");

        // Student 1, Course 1
        int b1 = manager.bookExam("S1", "E1");
        manager.confirmBooking(b1);
        manager.setGrade(b1, 80);

        // Student 2, Course 1
        int b2 = manager.bookExam("S2", "E1");
        manager.confirmBooking(b2);
        manager.setGrade(b2, 90);

        // Student 1, Course 2
        int b3 = manager.bookExam("S1", "E2");
        manager.confirmBooking(b3);
        manager.setGrade(b3, 75);

        Map<String, Double> averages = manager.getCourseAverage();
        assertEquals(2, averages.size());
        assertEquals(85.0, averages.get("C1"), 0.001);
        assertEquals(75.0, averages.get("C2"), 0.001);
    }
}
