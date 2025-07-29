# Course-Management

This system manages course enrollments and exam bookings for students. It allows for adding courses, enrolling students, scheduling exams, booking students for exams, and recording grades.
The main class for this system is CourseManager, and all related classes are located within the courseManagement package. The Example class provides demonstrations of how to use the primary methods.
R1: Courses and Students
These methods handle the registration of courses and the enrollment of students.
addCourse(String courseId, int maxStudents): Registers a new course with a unique ID and a maximum number of students. It will throw an exception if the courseId already exists or if maxStudents is not between 10 and 200 (inclusive).
enrollStudent(String studentId, String... courseIds): Enrolls a student, identified by a unique studentId, into one or more courses. It throws an exception if the studentId is already registered, if a courseId does not exist, if a course is already at maximum capacity, or if the student is already enrolled in a given course.
getEnrolledStudents(String courseId): Returns a sorted list of student IDs for a specified course.
R2: Exams
This section covers the creation and retrieval of exams associated with courses.
addExam(String courseId, String examId, String location): Adds an exam for a specific course. The examId must be unique. An exception is thrown if the courseId does not exist or if the examId has already been used.
getExamsByCourse(): Returns a map where each key is a courseId and the corresponding value is a alphabetically sorted list of examIds for that course.
R3: Exam Booking
These methods manage the process of students booking their exams.
bookExam(String studentId, String examId): Allows a student to book a place in an exam. A unique, sequential booking number (starting from 1) is returned. The booking is initially in a PENDING state. An exception is thrown if the studentId or examId is not valid, if the student is not enrolled in the course corresponding to the exam, or if they have already booked this exam.
confirmBooking(int bookingNumber): Changes the status of a booking from PENDING to CONFIRMED. An exception is thrown if the booking number does not exist or if the booking is not in the PENDING state.
getConfirmedBookings(): Retrieves a list of all booking numbers that are in the CONFIRMED state, sorted in ascending order.
R4: Grading
This section deals with recording the outcomes of completed exams.
setGrade(int bookingNumber, int grade): Assigns a grade to a booking and changes its status to COMPLETED. An exception is thrown if the booking number is not valid, if the booking is not in the CONFIRMED state, or if the grade is not between 0 and 100.
getCompletedBookings(): Returns a list of all booking numbers that are in the COMPLETED state, sorted ascendingly.
R5: Statistics
These methods provide summary data about grades and course performance.
getStudentGrades(String studentId): Returns a map containing all of a student's grades, with the examId as the key and the integer grade as the value.
getCourseAverage(): Returns a map where each key is a courseId and the value is the average grade for all completed exams in that course.
