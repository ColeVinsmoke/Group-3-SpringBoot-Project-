package Entity;

import com.example.registration.entity.Course;
import com.example.registration.entity.Student;
import com.example.registration.entity.Registration;
import com.example.registration.repository.CourseRepository;
import com.example.registration.repository.StudentRepository;
import com.example.registration.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // enroll a student in a course
    public Registration enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        // Check if student already registered for this course
        if (registrationRepository.findByStudentAndCourse(student, course).isPresent()) {
            throw new RuntimeException("Student already enrolled in this course!");
        }

        // Check course capacity
        long enrolledCount = registrationRepository.countByCourse(course);
        if (enrolledCount >= course.getCapacity()) {
            throw new RuntimeException("Course capacity reached!");
        }

        // Check credit unit limit (example: max 18 units)
        int currentCredits = registrationRepository.findByStudent(student)
                .stream()
                .mapToInt(r -> r.getCourse().getCreditUnits())
                .sum();
        if (currentCredits + course.getCreditUnits() > 18) {
            throw new RuntimeException("Credit unit limit exceeded!");
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);

        return registrationRepository.save(registration);
    }

    // drop a course for a student
    public void dropCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Registration registration = registrationRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Registration not found!"));

        registrationRepository.delete(registration);
    }

    // view all courses a student is registered for
    public List<Registration> getStudentCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        return registrationRepository.findByStudent(student);
    }

    // view all students registered in a course
    public List<Registration> getCourseStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        return registrationRepository.findByCourse(course);
    }
}
