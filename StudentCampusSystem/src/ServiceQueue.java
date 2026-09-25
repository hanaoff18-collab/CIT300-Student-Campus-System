/**
 * ServiceQueue.java
 * MEMBER 2: Queue (First In, First Out) for student service requests.
 * The request that arrived first is processed first.
 */
public class ServiceQueue {

    private static class Node {
        String request;
        Node next;

        Node(String request) {
            this.request = request;
        }
    }

    private Node front;   // next request to process
    private Node rear;    // last request that arrived
    private int size;

    public ServiceQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    // Add a request at the back of the queue
    public void enqueue(String request) {
        Node newNode = new Node(request);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Remove and return the request at the front. Returns null if empty.
    public String dequeue() {
        if (front == null) {
            return null;
        }
        String request = front.request;
        front = front.next;
        if (front == null) {
            rear = null;   // the queue became empty
        }
        size--;
        return request;
    }

    // Show all waiting requests, from front to back
    public void display() {
        if (front == null) {
            System.out.println("No pending service requests.");
            return;
        }
        Node current = front;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.request);
            current = current.next;
            count++;
        }
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }
}
