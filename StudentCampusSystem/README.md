# University Student Record and Campus Route Management System

CIT300 Data Structures and Algorithms - Graded Practical Assignment 1

A Java console application that manages student records and a campus map.

## Group Members

| Name | Student ID | Responsibility | Individual Contribution |
|------|-----------|----------------|-------------------------|
| MJF.Hana | 23DA2-1134 | Linked list and student records | Implemented the singly linked list in StudentLinkedList.java to add, find, remove, and display student records, and blocked duplicate student IDs. |
| MMF.Mahdhiyya | 23da2-1153 | Stack and queue | Implemented ActionStack.java for recent actions and ServiceQueue.java so service requests are handled in arrival order. |
| ANF.Ifatha | 23DA2-1140 | BST and hashing | Implemented StudentBST.java to organise students by ID and StudentHashTable.java for fast student ID search using chaining. |
| MAF.Farhath | 23DA2-0710 | Graph, locations, BFS/DFS | Implemented CampusGraph.java with an adjacency list, add and remove operations for locations and roads, network display, and BFS and DFS traversal. |

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
