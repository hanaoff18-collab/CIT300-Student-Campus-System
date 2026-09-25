/**
 * StudentLinkedList.java
 * Member 1: Fathima Hana
 *
 * This is a singly linked list. It is the place where every student record is kept.
 * The list starts at head. Each node stores one Student and a link called next.
 *
 * My own change: add() checks the list first. If that Student ID is already stored,
 * it does not attach another node. That stops duplicate IDs inside the linked list.
 */
public class StudentLinkedList {

    /**
     * One node in the chain.
     * data is the student record.
     * next points at the following node, or null when this is the last student.
     */
    private static class Node {
        Student data;
        Node next;

        Node(Student data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;   // the first student, or null when the list is empty
    private int size;

    public StudentLinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Attach a new student at the end of the list.
     * Returns false when the ID is already in the list, so we do not store it twice.
     */
    public boolean add(Student s) {
        if (find(s.getId()) != null) {
            return false;
        }

        Node newNode = new Node(s);
        if (head == null) {
            // empty list: the new node becomes the first one
            head = newNode;
        } else {
            // walk from head until we reach the last node, then attach the new one
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        return true;
    }

    /**
     * Look at each node from the head.
     * Compare the student's ID until one matches, then return that student.
     * Returns null when the ID is not in the list.
     */
    public Student find(String id) {
        Node current = head;
        while (current != null) {
            if (current.data.getId().equalsIgnoreCase(id)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Take a student out of the list by ID.
     * The previous node's next is moved so it skips the removed node.
     * Returns the removed student, or null if that ID is not in the list.
     */
    public Student remove(String id) {
        if (head == null) {
            return null;
        }

        // the student we want is the first node, so head moves forward
        if (head.data.getId().equalsIgnoreCase(id)) {
            Student removed = head.data;
            head = head.next;
            size--;
            return removed;
        }

        // look at the node in front of the one we might remove
        Node current = head;
        while (current.next != null) {
            if (current.next.data.getId().equalsIgnoreCase(id)) {
                Student removed = current.next.data;
                current.next = current.next.next;
                size--;
                return removed;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Print every student, starting at head and following next until the list ends.
     */
    public void displayAll() {
        if (head == null) {
            System.out.println("No student records found.");
            return;
        }
        Node current = head;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.data);
            current = current.next;
            count++;
        }
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }
}
