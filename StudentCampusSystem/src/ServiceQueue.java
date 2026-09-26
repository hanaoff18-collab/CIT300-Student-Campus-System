import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ServiceQueue.java
 * MEMBER 2: Queue (First In, First Out) for student service requests.
 * The request that arrived first is processed first.
 *
 * Advanced version:
 *  - Generic (works with any request type, not just String)
 *  - Implements Iterable so it can be used in a for-each loop
 *  - Adds peek(), contains(), clear(), and a proper toString()
 *  - Throws exceptions on invalid operations instead of silently
 *    returning null, so bugs surface immediately
 */
public class ServiceQueue<T> implements Iterable<T> {

    private static class Node<T> {
        T request;
        Node<T> next;

        Node(T request) {
            this.request = request;
        }
    }

    private Node<T> front;   // next request to process
    private Node<T> rear;    // last request that arrived
    private int size;

    public ServiceQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /** Add a request at the back of the queue. */
    public void enqueue(T request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        Node<T> newNode = new Node<>(request);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Remove and return the request at the front.
     * Throws NoSuchElementException if the queue is empty.
     */
    public T dequeue() {
        if (front == null) {
            throw new NoSuchElementException("Cannot dequeue: queue is empty.");
        }
        T request = front.request;
        front = front.next;
        if (front == null) {
            rear = null;   // the queue became empty
        }
        size--;
        return request;
    }

    /**
     * Return (without removing) the request at the front.
     * Throws NoSuchElementException if the queue is empty.
     */
    public T peek() {
        if (front == null) {
            throw new NoSuchElementException("Cannot peek: queue is empty.");
        }
        return front.request;
    }

    /** True if the given request is somewhere in the queue. */
    public boolean contains(T request) {
        Node<T> current = front;
        while (current != null) {
            if (current.request.equals(request)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Remove all pending requests. */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    /** Show all waiting requests, from front to back. */
    public void display() {
        if (front == null) {
            System.out.println("No pending service requests.");
            return;
        }
        Node<T> current = front;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.request);
            current = current.next;
            count++;
        }
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }

    /** Lets ServiceQueue be used directly in a for-each loop. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = front;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = current.request;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ServiceQueue [front -> rear]: ");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.request);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        return sb.toString();
    }

    // Simple demo / smoke test
    public static void main(String[] args) {
        ServiceQueue<String> queue = new ServiceQueue<>();
        queue.enqueue("Student A - ID card");
        queue.enqueue("Student B - transcript");
        queue.enqueue("Student C - fee payment");

        System.out.println("Current queue:");
        queue.display();

        System.out.println("\nPeek: " + queue.peek());
        System.out.println("Contains 'Student B - transcript'? " + queue.contains("Student B - transcript"));

        System.out.println("\nProcessing: " + queue.dequeue());
        System.out.println("Remaining size: " + queue.getSize());

        System.out.println("\nIterating with for-each:");
        for (String request : queue) {
            System.out.println(" - " + request);
        }

        System.out.println("\n" + queue);
    }
}
