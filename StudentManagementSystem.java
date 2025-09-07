import java.io.*;
import java.util.*;

/**
 * Student class represents a single student with all their details
 * This is our data model class (Entity class in OOP)
 */
class Student {
    // Private fields - encapsulation principle
    private int id;
    private String name;
    private int age;
    private String grade;
    private String email;
    
    /**
     * Constructor to create a new student object
     * @param id - unique identifier for student
     * @param name - student's full name
     * @param age - student's age
     * @param grade - student's grade/class
     * @param email - student's email address
     */
    public Student(int id, String name, int age, String grade, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.email = email;
    }
    
    // Getter methods - to access private fields from outside the class
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public String getEmail() {
        return email;
    }
    
    // Setter methods - to modify private fields (except ID which should be immutable)
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * toString method - converts student object to readable string format
     * This method is automatically called when we print the object
     */
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Grade: %s | Email: %s", 
                           id, name, age, grade, email);
    }
    
    /**
     * Converts student object to CSV format for file storage
     * @return CSV string representation of student
     */
    public String toCSV() {
        return id + "," + name + "," + age + "," + grade + "," + email;
    }
    
    /**
     * Creates a Student object from CSV string (static method)
     * @param csvLine - CSV format string
     * @return Student object created from the CSV data
     */
    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        return new Student(
            Integer.parseInt(parts[0]),  // id
            parts[1],                    // name
            Integer.parseInt(parts[2]),  // age
            parts[3],                    // grade
            parts[4]                     // email
        );
    }
}

/**
 * StudentManager class handles all business logic and operations
 * This is our service/manager class that manages the collection of students
 */
class StudentManager {
    // ArrayList to store all students in memory
    private ArrayList<Student> students;
    private String fileName = "students.csv";  // File name for data persistence
    
    /**
     * Constructor - initializes the student list and loads existing data
     */
    public StudentManager() {
        students = new ArrayList<>();
        loadFromFile();  // Load existing data when program starts
    }
    
    /**
     * Adds a new student to the system
     * @param student - Student object to be added
     */
    public void addStudent(Student student) {
        students.add(student);
        saveToFile();  // Save to file immediately after adding
        System.out.println("✅ Student added successfully!");
    }
    
    /**
     * Displays all students in a formatted table
     */
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("📝 No students found in the system.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                          📚 ALL STUDENTS");
        System.out.println("=".repeat(80));
        
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        System.out.println("=".repeat(80));
        System.out.println("Total students: " + students.size());
    }
    
    /**
     * Finds a student by their ID
     * @param id - student ID to search for
     * @return Student object if found, null otherwise
     */
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;  // Student not found
    }
    
    /**
     * Finds students by name (partial match, case-insensitive)
     * @param name - name to search for
     * @return List of matching students
     */
    public List<Student> findStudentsByName(String name) {
        List<Student> foundStudents = new ArrayList<>();
        String searchName = name.toLowerCase();
        
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(searchName)) {
                foundStudents.add(student);
            }
        }
        return foundStudents;
    }
    
    /**
     * Updates student details by ID
     * @param id - ID of student to update
     * @return true if student was found and updated, false otherwise
     */
    public boolean updateStudent(int id) {
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("❌ Student with ID " + id + " not found!");
            return false;
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n📝 Current details: " + student);
        System.out.println("\n🔄 Enter new details (press Enter to keep current value):");
        
        // Update name
        System.out.print("New name [" + student.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            student.setName(newName);
        }
        
        // Update age
        System.out.print("New age [" + student.getAge() + "]: ");
        String ageInput = scanner.nextLine().trim();
        if (!ageInput.isEmpty()) {
            try {
                int newAge = Integer.parseInt(ageInput);
                if (newAge > 0 && newAge < 150) {
                    student.setAge(newAge);
                } else {
                    System.out.println("⚠️  Invalid age. Keeping current age.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Invalid age format. Keeping current age.");
            }
        }
        
        // Update grade
        System.out.print("New grade [" + student.getGrade() + "]: ");
        String newGrade = scanner.nextLine().trim();
        if (!newGrade.isEmpty()) {
            student.setGrade(newGrade);
        }
        
        // Update email
        System.out.print("New email [" + student.getEmail() + "]: ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            student.setEmail(newEmail);
        }
        
        saveToFile();
        System.out.println("✅ Student updated successfully!");
        return true;
    }
    
    /**
     * Deletes a student by ID
     * @param id - ID of student to delete
     * @return true if student was found and deleted, false otherwise
     */
    public boolean deleteStudent(int id) {
        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            saveToFile();
            System.out.println("✅ Student deleted successfully!");
            return true;
        } else {
            System.out.println("❌ Student with ID " + id + " not found!");
            return false;
        }
    }
    
    /**
     * Saves all students to CSV file
     * This method handles file I/O and exception handling
     */
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("id,name,age,grade,email");
            
            // Write each student as CSV
            for (Student student : students) {
                writer.println(student.toCSV());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving to file: " + e.getMessage());
        }
    }
    
    /**
     * Loads students from CSV file
     * This method handles file I/O and exception handling
     */
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Student student = Student.fromCSV(line);
                    students.add(student);
                }
            }
            System.out.println("📁 Loaded " + students.size() + " students from file.");
        } catch (FileNotFoundException e) {
            System.out.println("📁 No existing data file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("❌ Error loading from file: " + e.getMessage());
        }
    }
    
    /**
     * Checks if a student ID already exists
     * @param id - ID to check
     * @return true if ID exists, false otherwise
     */
    public boolean isIdExists(int id) {
        return findStudentById(id) != null;
    }
    
    /**
     * Gets the current number of students
     * @return number of students in the system
     */
    public int getStudentCount() {
        return students.size();
    }
}

/**
 * Main class that handles user interaction and menu system
 * This is our presentation layer / user interface
 */
public class StudentManagementSystem {
    private static StudentManager manager = new StudentManager();
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main method - entry point of the program
     */
    public static void main(String[] args) {
        System.out.println("🎓 Welcome to Student Management System!");
        System.out.println("==========================================");
        
        while (true) {
            displayMenu();
            int choice = getMenuChoice();
            
            switch (choice) {
                case 1:
                    addStudentFromInput();
                    break;
                case 2:
                    manager.viewAllStudents();
                    break;
                case 3:
                    updateStudentFromInput();
                    break;
                case 4:
                    deleteStudentFromInput();
                    break;
                case 5:
                    searchStudentFromInput();
                    break;
                case 6:
                    System.out.println("👋 Thank you for using Student Management System!");
                    System.out.println("💾 All data has been saved automatically.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please select 1-6.");
            }
            
            // Pause before showing menu again
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * Displays the main menu options
     */
    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           📚 MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. ➕ Add New Student");
        System.out.println("2. 📋 View All Students");
        System.out.println("3. ✏️  Update Student");
        System.out.println("4. 🗑️  Delete Student");
        System.out.println("5. 🔍 Search Student");
        System.out.println("6. 🚪 Exit");
        System.out.println("=".repeat(40));
        System.out.print("Choose an option (1-6): ");
    }
    
    /**
     * Gets and validates menu choice from user
     * @return valid menu choice (1-6)
     */
    public static int getMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return 0; // Invalid choice
        }
    }
    
    /**
     * Handles adding a new student from user input
     */
    public static void addStudentFromInput() {
        System.out.println("\n➕ Adding New Student");
        System.out.println("=".repeat(30));
        
        try {
            // Get student ID
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            if (manager.isIdExists(id)) {
                System.out.println("❌ Student ID " + id + " already exists!");
                return;
            }
            
            // Get student name
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("❌ Name cannot be empty!");
                return;
            }
            
            // Get student age
            System.out.print("Enter Student Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            if (age < 1 || age > 150) {
                System.out.println("❌ Please enter a valid age (1-150)!");
                return;
            }
            
            // Get student grade
            System.out.print("Enter Student Grade: ");
            String grade = scanner.nextLine().trim();
            if (grade.isEmpty()) {
                System.out.println("❌ Grade cannot be empty!");
                return;
            }
            
            // Get student email
            System.out.print("Enter Student Email: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty() || !email.contains("@")) {
                System.out.println("❌ Please enter a valid email address!");
                return;
            }
            
            // Create and add the student
            Student student = new Student(id, name, age, grade, email);
            manager.addStudent(student);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter valid numbers for ID and age!");
        }
    }
    
    /**
     * Handles updating a student from user input
     */
    public static void updateStudentFromInput() {
        System.out.println("\n✏️  Updating Student");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Enter Student ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            manager.updateStudent(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid student ID!");
        }
    }
    
    /**
     * Handles deleting a student from user input
     */
    public static void deleteStudentFromInput() {
        System.out.println("\n🗑️  Deleting Student");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Enter Student ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Student student = manager.findStudentById(id);
            if (student != null) {
                System.out.println("Student to delete: " + student);
                System.out.print("Are you sure? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("yes") || confirm.equals("y")) {
                    manager.deleteStudent(id);
                } else {
                    System.out.println("❌ Deletion cancelled.");
                }
            } else {
                System.out.println("❌ Student with ID " + id + " not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid student ID!");
        }
    }
    
    /**
     * Handles searching for students from user input
     */
    public static void searchStudentFromInput() {
        System.out.println("\n🔍 Search Student");
        System.out.println("=".repeat(30));
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Choose search type (1 or 2): ");
        
        try {
            int searchType = Integer.parseInt(scanner.nextLine().trim());
            
            if (searchType == 1) {
                // Search by ID
                System.out.print("Enter Student ID: ");
                int id = Integer.parseInt(scanner.nextLine().trim());
                
                Student student = manager.findStudentById(id);
                if (student != null) {
                    System.out.println("\n✅ Student Found:");
                    System.out.println(student);
                } else {
                    System.out.println("❌ No student found with ID: " + id);
                }
                
            } else if (searchType == 2) {
                // Search by name
                System.out.print("Enter Student Name (or part of name): ");
                String name = scanner.nextLine().trim();
                
                List<Student> foundStudents = manager.findStudentsByName(name);
                if (!foundStudents.isEmpty()) {
                    System.out.println("\n✅ Found " + foundStudents.size() + " student(s):");
                    for (int i = 0; i < foundStudents.size(); i++) {
                        System.out.println((i + 1) + ". " + foundStudents.get(i));
                    }
                } else {
                    System.out.println("❌ No students found with name containing: " + name);
                }
            } else {
                System.out.println("❌ Invalid search type!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter valid input!");
        }
    }
}