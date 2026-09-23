# University Student Record and Campus Route Management System

CIT300 Data Structures and Algorithms - Graded Practical Assignment 1

A Java console application that manages student records and a campus map.

## Group Members

| Name | Student ID | Responsibility | Individual Contribution |
|------|-----------|----------------|-------------------------|
| (Member 1 name) | (ID) | Linked list and student records | (write what you did) |
| (Member 2 name) | (ID) | Stack and queue | (write what you did) |
| (Member 3 name) | (ID) | BST and hashing | (write what you did) |
| (Member 4 name) | (ID) | Graph, locations, BFS/DFS | (write what you did) |

All members worked together on: menu, validation, testing, debugging, documentation.

## Data Structures Used

| Data structure | File | Used for |
|----------------|------|----------|
| Singly linked list | StudentLinkedList.java | Store and manage student records |
| Stack | ActionStack.java | Recent actions history |
| Queue | ServiceQueue.java | Student service requests (first come, first served) |
| Binary Search Tree | StudentBST.java | Show students sorted by Student ID |
| Hash table (chaining) | StudentHashTable.java | Fast search by Student ID |
| Graph (adjacency list) | CampusGraph.java | Campus locations, roads, BFS and DFS |

## How to Run

1. Install JDK 17 or newer.
2. Open a terminal in the project folder.
3. Compile: `javac -d out src/*.java`
4. Run: `java -cp out Main`

## Input Validation

- Empty inputs are rejected.
- Duplicate student IDs and duplicate locations are rejected.
- Marks must be a number from 0 to 100.
- Missing students, missing locations and missing roads show an error message.
- Menu input that is not a number from 1 to 16 is rejected.

## Demo Video

(Add the link here)
