package courseManagement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles all the main logic of the system.
 */
public class CourseManager {

    private Map<String, Course> courses = new HashMap<>();
    private Map<String, Student> students = new HashMap<>();
    private Map<String, Exam> exams = new HashMap<>();
    private Map<Integer, Booking> bookings = new HashMap<>();
    private int nextBookingNumber = 1;

    /**
     * R1: Registers a new course.
     * @param courseId The unique ID for the course.
     * @param maxStudents Maximum number of students (10-200).
     * @throws Exception if courseId exists or maxStudents is out of range.
     */
    public void addCourse(String courseId, int maxStudents) throws Exception {
        if (courses.containsKey(courseId)) {
            throw new Exception("Course ID " + courseId + " already exists.");
        }
        if (maxStudents < 10 || maxStudents > 200) {
            throw new Exception("Maximum number of students must be between 10 and 200.");
        }
        courses.put(courseId, new Course(courseId, maxStudents));
    }

    /**
     * R1: Enrolls a student into one or more courses.
     * @param studentId The unique ID for the student.
     * @param courseIds A variable number of course IDs to enroll in.
     * @throws Exception if studentId exists, courseId is invalid, course is full, or student is already enrolled.
     */
    public void enrollStudent(String studentId, String... courseIds) throws Exception {
        if (students.containsKey(studentId)) {
            throw new Exception("Student ID " + studentId + " is already registered.");
        }

        Student student = new Student(studentId);
        for (String courseId : courseIds) {
            Course course = courses.get(courseId);
            if (course == null) {
                throw new Exception("Course ID " + courseId + " does not exist.");
            }
            course.addStudent(studentId); // This method throws exceptions for capacity and duplicate enrollment
            student.addCourse(courseId);
        }
        students.put(studentId, student);
    }

    /**
     * R1: Gets a sorted list of student IDs for a course.
     * @param courseId The ID of the course.
     * @return A sorted list of student IDs.
     */
    public List<String> getEnrolledStudents(String courseId) {
        Course course = courses.get(courseId);
        return course != null ? course.getEnrolledStudents() : new ArrayList<>();
    }

    /**
     * R2: Adds an exam for a specific course.
     * @param courseId The ID of the course.
     * @param examId The unique ID for the exam.
     * @param location The location of the exam.
     * @throws Exception if courseId does not exist or examId is already used.
     */
    public void addExam(String courseId, String examId, String location) throws Exception {
        if (!courses.containsKey(courseId)) {
            throw new Exception("Course ID " + courseId + " does not exist.");
        }
        if (exams.containsKey(examId)) {
            throw new Exception("Exam ID " + examId + " has already been used.");
        }
        exams.put(examId, new Exam(examId, courseId, location));
    }

    /**
     * R2: Gets a map of courses and their exams.
     * @return A map with courseId as key and a sorted list of examIds as value.
     */
    public Map<String, List<String>> getExamsByCourse() {
        Map<String, List<String>> examsByCourse = new TreeMap<>(); // TreeMap to sort by courseId
        for (Exam exam : exams.values()) {
            examsByCourse.computeIfAbsent(exam.getCourseId(), k -> new ArrayList<>()).add(exam.getExamId());
        }
        // Sort the list of examIds for each course alphabetically
        examsByCourse.values().forEach(Collections::sort);
        return examsByCourse;
    }

    /**
     * R3: Books a student for an exam.
     * @param studentId The ID of the student.
     * @param examId The ID of the exam.
     * @return A unique, sequential booking number.
     * @throws Exception if student or exam is invalid, or if the student cannot book the exam.
     */
    public int bookExam(String studentId, String examId) throws Exception {
        Student student = students.get(studentId);
        Exam exam = exams.get(examId);

        if (student == null) {
            throw new Exception("Student ID " + studentId + " is not valid.");
        }
        if (exam == null) {
            throw new Exception("Exam ID " + examId + " is not valid.");
        }
        if (!student.isEnrolledIn(exam.getCourseId())) {
            throw new Exception("Student " + studentId + " is not enrolled in the course for this exam.");
        }
        // Check if student already booked this exam
        for (Booking b : bookings.values()) {
            if (b.getStudentId().equals(studentId) && b.getExamId().equals(examId)) {
                throw new Exception("Student " + studentId + " has already booked exam " + examId + ".");
            }
        }

        int bookingNumber = nextBookingNumber++;
        bookings.put(bookingNumber, new Booking(bookingNumber, studentId, examId));
        return bookingNumber;
    }

    /**
     * R3: Confirms a PENDING booking.
     * @param bookingNumber The booking number to confirm.
     * @throws Exception if the booking number is invalid or not PENDING.
     */
    public void confirmBooking(int bookingNumber) throws Exception {
        Booking booking = bookings.get(bookingNumber);
        if (booking == null) {
            throw new Exception("Booking number " + bookingNumber + " does not exist.");
        }
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new Exception("Booking " + bookingNumber + " is not in PENDING state.");
        }
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
    }

    /**
     * R3: Retrieves a sorted list of all CONFIRMED booking numbers.
     * @return A sorted list of integers.
     */
    public List<Integer> getConfirmedBookings() {
        return bookings.values().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
                .map(Booking::getBookingNumber)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * R4: Sets a grade for a CONFIRMED booking.
     * @param bookingNumber The booking number.
     * @param grade The grade (0-100).
     * @throws Exception if booking is invalid, not CONFIRMED, or grade is out of range.
     */
    public void setGrade(int bookingNumber, int grade) throws Exception {
        Booking booking = bookings.get(bookingNumber);
        if (booking == null) {
            throw new Exception("Booking number " + bookingNumber + " is not valid.");
        }
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new Exception("Booking " + bookingNumber + " is not in CONFIRMED state.");
        }
        if (grade < 0 || grade > 100) {
            throw new Exception("Grade must be between 0 and 100.");
        }
        booking.setGrade(grade);
        booking.setStatus(Booking.BookingStatus.COMPLETED);
    }

    /**
     * R4: Retrieves a sorted list of all COMPLETED booking numbers.
     * @return A sorted list of integers.
     */
    public List<Integer> getCompletedBookings() {
        return bookings.values().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .map(Booking::getBookingNumber)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * R5: Gets all grades for a specific student.
     * @param studentId The ID of the student.
     * @return A map with examId as key and grade as value.
     */
    public Map<String, Integer> getStudentGrades(String studentId) {
        return bookings.values().stream()
                .filter(b -> b.getStudentId().equals(studentId) && b.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toMap(Booking::getExamId, Booking::getGrade));
    }

    /**
     * R5: Calculates the average grade for each course.
     * @return A map with courseId as key and the average grade as value.
     */
    public Map<String, Double> getCourseAverage() {
        // Group completed bookings by courseId
        Map<String, List<Integer>> gradesByCourse = bookings.values().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        b -> exams.get(b.getExamId()).getCourseId(),
                        Collectors.mapping(Booking::getGrade, Collectors.toList())
                ));

        // Calculate the average for each course
        return gradesByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0)
                ));
    }
}