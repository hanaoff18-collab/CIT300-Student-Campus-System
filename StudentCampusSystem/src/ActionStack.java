/**
 * ActionStack.java
 * MEMBER 2: Stack (Last In, First Out) that keeps the recent actions history.
 * The newest action is always on top.
 */
public class ActionStack {

    private static class Node {
        String action;
        Node next;

        Node(String action) {
            this.action = action;
        }
    }

    private Node top;
    private int size;

    public ActionStack() {
        top = null;
        size = 0;
    }

    // Put a new action on top
    public void push(String action) {
        Node newNode = new Node(action);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Take the top action off. Returns null if the stack is empty.
    public String pop() {
        if (top == null) {
            return null;
        }
        String action = top.action;
        top = top.next;
        size--;
        return action;
    }

    // Look at the top action without removing it
    public String peek() {
        if (top == null) {
            return null;
        }
        return top.action;
    }

    // Show actions from newest to oldest
    public void display() {
        if (top == null) {
            System.out.println("No recent actions.");
            return;
        }
        Node current = top;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.action);
            current = current.next;
            count++;
        }
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }
}
