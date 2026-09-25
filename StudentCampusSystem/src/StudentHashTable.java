/**
 * StudentHashTable.java
 * MEMBER 3: Hash table for fast student ID searching.
 * Uses chaining: if two IDs land in the same slot, they are linked together.
 */
public class StudentHashTable {

    private static class Node {
        Student data;
        Node next;

        Node(Student data) {
            this.data = data;
        }
    }

    private static final int TABLE_SIZE = 11;
    private Node[] table = new Node[TABLE_SIZE];

    // Hash function: turns an ID into a slot number (0 to TABLE_SIZE - 1)
    private int hash(String id) {
        int sum = 0;
        String key = id.toUpperCase();
        for (int i = 0; i < key.length(); i++) {
            sum = sum * 31 + key.charAt(i);
            sum = sum % TABLE_SIZE;
        }
        return sum;
    }

    // Add a student. Returns false if the ID already exists.
    public boolean put(Student s) {
        if (get(s.getId()) != null) {
            return false;
        }
        int index = hash(s.getId());
        Node newNode = new Node(s);
        newNode.next = table[index];   // put at the front of the chain
        table[index] = newNode;
        return true;
    }

    // Find a student by ID. Returns null if not found.
    public Student get(String id) {
        int index = hash(id);
        Node current = table[index];
        while (current != null) {
            if (current.data.getId().equalsIgnoreCase(id)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    // Remove a student by ID
    public void remove(String id) {
        int index = hash(id);
        Node current = table[index];
        Node previous = null;
        while (current != null) {
            if (current.data.getId().equalsIgnoreCase(id)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
    }

    // Tells which slot an ID goes to (used in the demo to explain hashing)
    public int getSlot(String id) {
        return hash(id);
    }
}
