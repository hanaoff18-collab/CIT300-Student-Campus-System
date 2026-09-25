/**
 * StudentBST.java
 * MEMBER 3: Binary Search Tree that organizes students by Student ID.
 * Smaller IDs go to the left, bigger IDs go to the right.
 * Inorder traversal shows the students sorted by ID.
 */
public class StudentBST {

    private static class Node {
        Student data;
        Node left;
        Node right;

        Node(Student data) {
            this.data = data;
        }
    }

    private Node root;

    // Insert a student. Returns false if the ID already exists.
    public boolean insert(Student s) {
        if (search(s.getId()) != null) {
            return false;
        }
        root = insertRec(root, s);
        return true;
    }

    private Node insertRec(Node node, Student s) {
        if (node == null) {
            return new Node(s);
        }
        if (s.getId().compareToIgnoreCase(node.data.getId()) < 0) {
            node.left = insertRec(node.left, s);
        } else {
            node.right = insertRec(node.right, s);
        }
        return node;
    }

    // Search by ID. Returns null if not found.
    public Student search(String id) {
        Node current = root;
        while (current != null) {
            int result = id.compareToIgnoreCase(current.data.getId());
            if (result == 0) {
                return current.data;
            } else if (result < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    // Delete a student by ID
    public void delete(String id) {
        root = deleteRec(root, id);
    }

    private Node deleteRec(Node node, String id) {
        if (node == null) {
            return null;
        }
        int result = id.compareToIgnoreCase(node.data.getId());
        if (result < 0) {
            node.left = deleteRec(node.left, id);
        } else if (result > 0) {
            node.right = deleteRec(node.right, id);
        } else {
            // Found the node to delete
            if (node.left == null) {
                return node.right;          // 0 or 1 child
            }
            if (node.right == null) {
                return node.left;           // 1 child
            }
            // 2 children: replace with the smallest node of the right side
            Node smallest = node.right;
            while (smallest.left != null) {
                smallest = smallest.left;
            }
            node.data = smallest.data;
            node.right = deleteRec(node.right, smallest.data.getId());
        }
        return node;
    }

    // Show all students sorted by ID (left, node, right)
    public void displayInorder() {
        if (root == null) {
            System.out.println("No student records found.");
            return;
        }
        inorder(root);
    }

    private void inorder(Node node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.println(node.data);
        inorder(node.right);
    }
}
