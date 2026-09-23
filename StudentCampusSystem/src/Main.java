import java.util.Scanner;

/**
 * Main.java
 * Menu-driven console interface. Connects all the data structures together.
 * ALL MEMBERS: integration, validation and testing.
 */
public class Main {

    private static Scanner input = new Scanner(System.in);

    // One copy of each data structure for the whole program
    private static StudentLinkedList studentList = new StudentLinkedList();
    private static StudentBST studentTree = new StudentBST();
    private static StudentHashTable studentHash = new StudentHashTable();
    private static ActionStack actionHistory = new ActionStack();
    private static ServiceQueue serviceQueue = new ServiceQueue();
    private static CampusGraph campus = new CampusGraph();

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 16) {
            showMenu();
            choice = readMenuChoice();
            System.out.println();
            switch (choice) {
                case 1:  addStudent(); break;
                case 2:  updateStudent(); break;
                case 3:  deleteStudent(); break;
                case 4:  studentList.displayAll(); break;
                case 5:  addServiceRequest(); break;
                case 6:  processNextRequest(); break;
                case 7:  actionHistory.display(); break;
                case 8:  studentTree.displayInorder(); break;
                case 9:  searchStudentHashing(); break;
                case 10: addLocation(); break;
                case 11: removeLocation(); break;
                case 12: addConnection(); break;
                case 13: removeConnection(); break;
                case 14: campus.displayNetwork(); break;
                case 15: traverseCampus(); break;
                case 16: System.out.println("Goodbye!"); break;
                default: System.out.println("Please choose a number from 1 to 16.");
            }
        }
    }

    // ---------------------------------------------------------------
    // MENU
    // ---------------------------------------------------------------
    private static void showMenu() {
        System.out.println("\n===== Student Record and Campus Route System =====");
        System.out.println(" 1. Add Student Record");
        System.out.println(" 2. Update Student Record");
        System.out.println(" 3. Delete Student Record");
        System.out.println(" 4. Display All Records using Linked List");
        System.out.println(" 5. Add Service Request to Queue");
        System.out.println(" 6. Process Next Service Request");
        System.out.println(" 7. Display Recent Actions using Stack");
        System.out.println(" 8. Display Students using BST");
        System.out.println(" 9. Search Student using Hashing");
        System.out.println("10. Add Campus Location");
        System.out.println("11. Remove Campus Location");
        System.out.println("12. Add Campus Connection/Road");
        System.out.println("13. Remove Campus Connection/Road");
        System.out.println("14. Display Campus Connections");
        System.out.println("15. Traverse Campus Locations using BFS or DFS");
        System.out.println("16. Exit");
    }

    // ---------------------------------------------------------------
    // INPUT HELPERS (validation)
    // ---------------------------------------------------------------

    // Read a menu number. Returns -1 if the user did not type a number.
    private static int readMenuChoice() {
        System.out.print("Enter your choice: ");
        String text = input.nextLine().trim();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Keep asking until the user types something that is not empty
    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    // Keep asking until the user types a valid mark from 0 to 100
    private static double readMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            try {
                double marks = Double.parseDouble(text);
                if (marks >= 0 && marks <= 100) {
                    return marks;
                }
                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // ---------------------------------------------------------------
    // STUDENT OPERATIONS
    // ---------------------------------------------------------------
    private static void addStudent() {
        String id = readText("Student ID: ");
        if (studentHash.get(id) != null) {
            System.out.println("Error: a student with this ID already exists.");
            return;
        }
        String name = readText("Name: ");
        String programme = readText("Programme: ");
        double marks = readMarks("Marks (0-100): ");

        // The SAME Student object is stored in all three structures
        Student s = new Student(id, name, programme, marks);
        studentList.add(s);
        studentTree.insert(s);
        studentHash.put(s);

        actionHistory.push("Added student " + id);
        System.out.println("Student added successfully.");
    }

    private static void updateStudent() {
        if (studentList.isEmpty()) {
            System.out.println("No student records to update.");
            return;
        }
        String id = readText("Enter the Student ID to update: ");
        Student s = studentHash.get(id);
        if (s == null) {
            System.out.println("Error: student not found.");
            return;
        }
        System.out.println("Current record: " + s);
        s.setName(readText("New name: "));
        s.setProgramme(readText("New programme: "));
        s.setMarks(readMarks("New marks (0-100): "));
        // No need to update the tree or hash table: they hold the same object.

        actionHistory.push("Updated student " + s.getId());
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() {
        if (studentList.isEmpty()) {
            System.out.println("No student records to delete.");
            return;
        }
        String id = readText("Enter the Student ID to delete: ");
        Student s = studentHash.get(id);
        if (s == null) {
            System.out.println("Error: student not found.");
            return;
        }
        studentList.remove(id);
        studentTree.delete(id);
        studentHash.remove(id);

        actionHistory.push("Deleted student " + s.getId() + " (" + s.getName() + ")");
        System.out.println("Student deleted successfully.");
    }

    private static void searchStudentHashing() {
        String id = readText("Enter the Student ID to search: ");
        Student s = studentHash.get(id);
        System.out.println("Hash slot for this ID: " + studentHash.getSlot(id));
        if (s == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Found: " + s);
        }
    }

    // ---------------------------------------------------------------
    // QUEUE OPERATIONS
    // ---------------------------------------------------------------
    private static void addServiceRequest() {
        String id = readText("Student ID making the request: ");
        Student s = studentHash.get(id);
        if (s == null) {
            System.out.println("Error: student not found. Add the student first.");
            return;
        }
        String type = readText("Request type (e.g. Transcript, ID Card): ");
        serviceQueue.enqueue(s.getId() + " (" + s.getName() + ") - " + type);
        System.out.println("Request added. Requests waiting: " + serviceQueue.getSize());
    }

    private static void processNextRequest() {
        String request = serviceQueue.dequeue();
        if (request == null) {
            System.out.println("No pending service requests.");
            return;
        }
        System.out.println("Processing request: " + request);
        actionHistory.push("Processed request: " + request);
        System.out.println("Requests still waiting: " + serviceQueue.getSize());
    }

    // ---------------------------------------------------------------
    // GRAPH OPERATIONS
    // ---------------------------------------------------------------
    private static void addLocation() {
        String name = readText("Location name: ");
        if (campus.addLocation(name)) {
            actionHistory.push("Added location " + name);
            System.out.println("Location added.");
        } else {
            System.out.println("Error: this location already exists.");
        }
    }

    private static void removeLocation() {
        String name = readText("Location name to remove: ");
        if (campus.removeLocation(name)) {
            actionHistory.push("Removed location " + name);
            System.out.println("Location and its roads removed.");
        } else {
            System.out.println("Error: location not found.");
        }
    }

    private static void addConnection() {
        String a = readText("First location: ");
        String b = readText("Second location: ");
        if (!campus.hasLocation(a) || !campus.hasLocation(b)) {
            System.out.println("Error: both locations must exist first.");
        } else if (a.equalsIgnoreCase(b)) {
            System.out.println("Error: a road must connect two different locations.");
        } else if (campus.hasConnection(a, b)) {
            System.out.println("Error: this road already exists.");
        } else {
            campus.addConnection(a, b);
            actionHistory.push("Added road " + a + " <-> " + b);
            System.out.println("Road added.");
        }
    }

    private static void removeConnection() {
        String a = readText("First location: ");
        String b = readText("Second location: ");
        if (campus.removeConnection(a, b)) {
            actionHistory.push("Removed road " + a + " <-> " + b);
            System.out.println("Road removed.");
        } else {
            System.out.println("Error: this road does not exist.");
        }
    }

    private static void traverseCampus() {
        String start = readText("Start location: ");
        if (!campus.hasLocation(start)) {
            System.out.println("Error: location not found.");
            return;
        }
        String type = readText("Choose traversal - 1 for BFS, 2 for DFS: ");
        if (type.equals("1")) {
            campus.bfs(start);
        } else if (type.equals("2")) {
            campus.dfs(start);
        } else {
            System.out.println("Invalid choice. Enter 1 or 2.");
        }
    }
}
