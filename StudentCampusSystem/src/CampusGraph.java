import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * CampusGraph.java
 * MEMBER 4: Graph of campus locations (vertices) and roads (edges).
 * Represented with an ADJACENCY LIST: each location has a list of its neighbours.
 * Roads are two-way (undirected).
 */
public class CampusGraph {

    // key = location name, value = list of neighbouring locations
    private LinkedHashMap<String, ArrayList<String>> adjacencyList = new LinkedHashMap<>();

    // Find the stored name of a location (ignores upper/lower case). Returns null if missing.
    private String findName(String name) {
        for (String key : adjacencyList.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return key;
            }
        }
        return null;
    }

    public boolean hasLocation(String name) {
        return findName(name) != null;
    }

    public boolean hasConnection(String a, String b) {
        String nameA = findName(a);
        String nameB = findName(b);
        if (nameA == null || nameB == null) {
            return false;
        }
        return adjacencyList.get(nameA).contains(nameB);
    }

    // Add a location. Returns false if it already exists.
    public boolean addLocation(String name) {
        if (hasLocation(name)) {
            return false;
        }
        adjacencyList.put(name, new ArrayList<String>());
        return true;
    }

    // Remove a location and every road that touches it. Returns false if missing.
    public boolean removeLocation(String name) {
        String stored = findName(name);
        if (stored == null) {
            return false;
        }
        adjacencyList.remove(stored);
        for (ArrayList<String> neighbours : adjacencyList.values()) {
            neighbours.remove(stored);
        }
        return true;
    }

    // Add a two-way road. Returns false if a location is missing,
    // both names are the same, or the road already exists.
    public boolean addConnection(String a, String b) {
        String nameA = findName(a);
        String nameB = findName(b);
        if (nameA == null || nameB == null || nameA.equals(nameB)) {
            return false;
        }
        if (adjacencyList.get(nameA).contains(nameB)) {
            return false;
        }
        adjacencyList.get(nameA).add(nameB);
        adjacencyList.get(nameB).add(nameA);
        return true;
    }

    // Remove a two-way road. Returns false if it does not exist.
    public boolean removeConnection(String a, String b) {
        if (!hasConnection(a, b)) {
            return false;
        }
        String nameA = findName(a);
        String nameB = findName(b);
        adjacencyList.get(nameA).remove(nameB);
        adjacencyList.get(nameB).remove(nameA);
        return true;
    }

    // Show every location with its neighbours
    public void displayNetwork() {
        if (adjacencyList.isEmpty()) {
            System.out.println("No campus locations added yet.");
            return;
        }
        for (String location : adjacencyList.keySet()) {
            ArrayList<String> neighbours = adjacencyList.get(location);
            if (neighbours.isEmpty()) {
                System.out.println(location + " -> (no connections)");
            } else {
                System.out.println(location + " -> " + String.join(", ", neighbours));
            }
        }
    }

    // BFS: visit the closest locations first (uses a queue)
    public void bfs(String start) {
        String startName = findName(start);
        if (startName == null) {
            System.out.println("Location not found.");
            return;
        }
        ArrayList<String> visited = new ArrayList<>();
        ServiceQueue queue = new ServiceQueue();   // reuse our own queue
        visited.add(startName);
        queue.enqueue(startName);

        System.out.print("BFS order: ");
        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            System.out.print(current + "  ");
            for (String neighbour : adjacencyList.get(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.enqueue(neighbour);
                }
            }
        }
        System.out.println();
    }

    // DFS: go as deep as possible first (uses recursion)
    public void dfs(String start) {
        String startName = findName(start);
        if (startName == null) {
            System.out.println("Location not found.");
            return;
        }
        ArrayList<String> visited = new ArrayList<>();
        System.out.print("DFS order: ");
        dfsVisit(startName, visited);
        System.out.println();
    }

    private void dfsVisit(String current, ArrayList<String> visited) {
        visited.add(current);
        System.out.print(current + "  ");
        for (String neighbour : adjacencyList.get(current)) {
            if (!visited.contains(neighbour)) {
                dfsVisit(neighbour, visited);
            }
        }
    }
}