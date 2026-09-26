import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * CampusUI.java
 * Window for the same student records and campus graph used by the console menu.
 * Run with: java -cp out CampusUI
 */
public class CampusUI extends JFrame {

    private static final Color HEADER = new Color(27, 58, 75);
    private static final Color PAGE = new Color(244, 246, 248);
    private static final Color INK = new Color(33, 37, 41);

    private final StudentLinkedList studentList = new StudentLinkedList();
    private final StudentBST studentTree = new StudentBST();
    private final StudentHashTable studentHash = new StudentHashTable();
    private final ActionStack actionHistory = new ActionStack();
    private final ServiceQueue serviceQueue = new ServiceQueue();
    private final CampusGraph campus = new CampusGraph();

    private final JTextField idField = new JTextField(16);
    private final JTextField nameField = new JTextField(16);
    private final JTextField programmeField = new JTextField(16);
    private final JTextField marksField = new JTextField(16);
    private final DefaultTableModel studentModel = new DefaultTableModel(
            new String[] {"Student ID", "Name", "Programme", "Marks"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTextArea bstArea = area();

    private final JTextField requestIdField = new JTextField(16);
    private final JTextField requestTypeField = new JTextField(16);
    private final JLabel queueCountLabel = new JLabel("Waiting: 0");
    private final JTextArea queueArea = area();

    private final JTextArea actionArea = area();

    private final JTextField locationField = new JTextField(14);
    private final JTextField fromField = new JTextField(14);
    private final JTextField toField = new JTextField(14);
    private final JTextField startField = new JTextField(14);
    private final JTextArea networkArea = area();
    private final JTextArea traversalArea = area();

    private final JLabel statusLabel = new JLabel("Ready");

    public CampusUI() {
        super("Student Record and Campus Route System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 680));
        getContentPane().setBackground(PAGE);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(tabs(), BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
        refreshAll();
        setSize(1180, 760);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // The system look is optional. The window still opens with the default look.
        }
        SwingUtilities.invokeLater(() -> new CampusUI().setVisible(true));
    }

    private JPanel header() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Student Record and Campus Route System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Linked list, stack, queue, BST, hashing, and campus graph");
        subtitle.setForeground(new Color(198, 216, 224));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(title);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitle);
        return panel;
    }

    private JTabbedPane tabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tabs.addTab("Students", studentsTab());
        tabs.addTab("Service Queue", queueTab());
        tabs.addTab("Recent Actions", actionsTab());
        tabs.addTab("Campus", campusTab());
        tabs.addChangeListener(event -> refreshAll());
        return tabs;
    }

    private JPanel studentsTab() {
        JPanel form = column(
                label("Student ID"), idField,
                label("Name"), nameField,
                label("Programme"), programmeField,
                label("Marks (0 to 100)"), marksField,
                Box.createVerticalStrut(8),
                button("Add student", this::addStudent),
                button("Search by ID", this::searchStudent),
                button("Update student", this::updateStudent),
                button("Delete student", this::deleteStudent),
                button("Clear form", this::clearStudentForm)
        );

        JTable table = new JTable(studentModel);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFormFromTable(table);
            }
        });

        JPanel records = new JPanel(new GridLayout(2, 1, 0, 8));
        records.setOpaque(false);
        records.add(titled("All records (linked list)", new JScrollPane(table)));
        records.add(titled("Students sorted by ID (BST)", new JScrollPane(bstArea)));

        JPanel page = new JPanel(new BorderLayout(12, 0));
        page.setBackground(PAGE);
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        page.add(side(form), BorderLayout.WEST);
        page.add(records, BorderLayout.CENTER);
        return page;
    }

    private JPanel queueTab() {
        queueCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel form = column(
                label("Student ID"), requestIdField,
                label("Request type"), requestTypeField,
                Box.createVerticalStrut(8),
                button("Add request", this::addRequest),
                button("Process next request", this::processRequest),
                queueCountLabel
        );

        JPanel page = new JPanel(new BorderLayout(12, 0));
        page.setBackground(PAGE);
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        page.add(side(form), BorderLayout.WEST);
        page.add(titled("Waiting requests (first come, first served)", new JScrollPane(queueArea)), BorderLayout.CENTER);
        return page;
    }

    private JPanel actionsTab() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(PAGE);
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        page.add(titled("Newest action first (stack)", new JScrollPane(actionArea)), BorderLayout.CENTER);
        return page;
    }

    private JPanel campusTab() {
        JPanel locations = section("Locations",
                label("Location name"), locationField,
                button("Add location", this::addLocation),
                button("Remove location", this::removeLocation));

        JPanel roads = section("Roads",
                label("First location"), fromField,
                label("Second location"), toField,
                button("Add road", this::addRoad),
                button("Remove road", this::removeRoad));

        JPanel travelButtons = new JPanel(new GridLayout(1, 2, 8, 0));
        travelButtons.setOpaque(false);
        travelButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        travelButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        travelButtons.add(button("Run BFS", this::runBfs));
        travelButtons.add(button("Run DFS", this::runDfs));

        JPanel travel = section("Traversal",
                label("Start location"), startField, travelButtons);

        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(locations);
        controls.add(Box.createVerticalStrut(10));
        controls.add(roads);
        controls.add(Box.createVerticalStrut(10));
        controls.add(travel);

        JPanel results = new JPanel(new GridLayout(2, 1, 0, 8));
        results.setOpaque(false);
        results.add(titled("Campus connections", new JScrollPane(networkArea)));
        results.add(titled("Traversal result", new JScrollPane(traversalArea)));

        JPanel page = new JPanel(new BorderLayout(12, 0));
        page.setBackground(PAGE);
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        page.add(side(controls), BorderLayout.WEST);
        page.add(results, BorderLayout.CENTER);
        return page;
    }

    private JPanel statusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 228)));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(INK);
        bar.add(statusLabel);
        return bar;
    }

    private void addStudent() {
        String id = text(idField);
        String name = text(nameField);
        String programme = text(programmeField);
        if (id.isEmpty() || name.isEmpty() || programme.isEmpty()) {
            warn("Student ID, name, and programme are required.");
            return;
        }
        Double marks = readMarks();
        if (marks == null) {
            return;
        }
        if (studentHash.get(id) != null) {
            warn("A student with this ID already exists.");
            return;
        }
        Student student = new Student(id, name, programme, marks);
        studentList.add(student);
        studentTree.insert(student);
        studentHash.put(student);
        actionHistory.push("Added student " + id);
        clearStudentForm();
        refreshAll();
        status("Student " + id + " added.");
    }

    private void searchStudent() {
        String id = text(idField);
        if (id.isEmpty()) {
            warn("Enter a Student ID to search.");
            return;
        }
        Student student = studentHash.get(id);
        int slot = studentHash.getSlot(id);
        if (student == null) {
            status("Student not found. Hash slot " + slot + ".");
            warn("Student not found. Hash slot for this ID: " + slot + ".");
            return;
        }
        showStudent(student);
        status("Found " + student.getId() + " in hash slot " + slot + ".");
    }

    private void updateStudent() {
        String id = text(idField);
        if (id.isEmpty()) {
            warn("Enter the Student ID to update.");
            return;
        }
        Student student = studentHash.get(id);
        if (student == null) {
            warn("Student not found.");
            return;
        }
        String name = text(nameField);
        String programme = text(programmeField);
        if (name.isEmpty() || programme.isEmpty()) {
            warn("Name and programme are required.");
            return;
        }
        Double marks = readMarks();
        if (marks == null) {
            return;
        }
        student.setName(name);
        student.setProgramme(programme);
        student.setMarks(marks);
        actionHistory.push("Updated student " + student.getId());
        refreshAll();
        status("Student " + student.getId() + " updated.");
    }

    private void deleteStudent() {
        String id = text(idField);
        if (id.isEmpty()) {
            warn("Enter the Student ID to delete.");
            return;
        }
        Student student = studentHash.get(id);
        if (student == null) {
            warn("Student not found.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete " + student.getName() + " (" + student.getId() + ")?",
                "Delete student", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        studentList.remove(id);
        studentTree.delete(id);
        studentHash.remove(id);
        actionHistory.push("Deleted student " + student.getId() + " (" + student.getName() + ")");
        clearStudentForm();
        refreshAll();
        status("Student " + student.getId() + " deleted.");
    }

    private void addRequest() {
        String id = text(requestIdField);
        String type = text(requestTypeField);
        if (id.isEmpty() || type.isEmpty()) {
            warn("Student ID and request type are required.");
            return;
        }
        Student student = studentHash.get(id);
        if (student == null) {
            warn("Student not found. Add the student first.");
            return;
        }
        serviceQueue.enqueue(student.getId() + " (" + student.getName() + ") - " + type);
        requestTypeField.setText("");
        refreshAll();
        status("Request added. Waiting: " + serviceQueue.getSize() + ".");
    }

    private void processRequest() {
        String request = serviceQueue.dequeue();
        if (request == null) {
            warn("No pending service requests.");
            return;
        }
        actionHistory.push("Processed request: " + request);
        refreshAll();
        status("Processed: " + request);
    }

    private void addLocation() {
        String name = text(locationField);
        if (name.isEmpty()) {
            warn("Enter a location name.");
            return;
        }
        if (campus.addLocation(name)) {
            actionHistory.push("Added location " + name);
            locationField.setText("");
            refreshAll();
            status("Location added.");
        } else {
            warn("This location already exists.");
        }
    }

    private void removeLocation() {
        String name = text(locationField);
        if (name.isEmpty()) {
            warn("Enter the location name to remove.");
            return;
        }
        if (campus.removeLocation(name)) {
            actionHistory.push("Removed location " + name);
            locationField.setText("");
            refreshAll();
            status("Location and its roads removed.");
        } else {
            warn("Location not found.");
        }
    }

    private void addRoad() {
        String from = text(fromField);
        String to = text(toField);
        if (from.isEmpty() || to.isEmpty()) {
            warn("Enter both locations for the road.");
            return;
        }
        if (!campus.hasLocation(from) || !campus.hasLocation(to)) {
            warn("Both locations must exist first.");
        } else if (from.equalsIgnoreCase(to)) {
            warn("A road must connect two different locations.");
        } else if (campus.hasConnection(from, to)) {
            warn("This road already exists.");
        } else {
            campus.addConnection(from, to);
            actionHistory.push("Added road " + from + " <-> " + to);
            refreshAll();
            status("Road added.");
        }
    }

    private void removeRoad() {
        String from = text(fromField);
        String to = text(toField);
        if (from.isEmpty() || to.isEmpty()) {
            warn("Enter both locations for the road.");
            return;
        }
        if (campus.removeConnection(from, to)) {
            actionHistory.push("Removed road " + from + " <-> " + to);
            refreshAll();
            status("Road removed.");
        } else {
            warn("This road does not exist.");
        }
    }

    private void runBfs() {
        runTraversal(true);
    }

    private void runDfs() {
        runTraversal(false);
    }

    private void runTraversal(boolean breadthFirst) {
        String start = text(startField);
        if (start.isEmpty()) {
            warn("Enter a start location.");
            return;
        }
        if (!campus.hasLocation(start)) {
            warn("Location not found.");
            return;
        }
        traversalArea.setText(capture(() -> {
            if (breadthFirst) {
                campus.bfs(start);
            } else {
                campus.dfs(start);
            }
        }));
        status(breadthFirst ? "BFS completed." : "DFS completed.");
    }

    private void refreshAll() {
        studentModel.setRowCount(0);
        studentList.forEach(student -> studentModel.addRow(new Object[] {
                student.getId(),
                student.getName(),
                student.getProgramme(),
                String.format("%.1f", student.getMarks())
        }));
        bstArea.setText(capture(studentTree::displayInorder));
        queueArea.setText(capture(serviceQueue::display));
        queueCountLabel.setText("Waiting: " + serviceQueue.getSize());
        actionArea.setText(capture(actionHistory::display));
        networkArea.setText(capture(campus::displayNetwork));
    }

    private void fillFormFromTable(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        idField.setText(String.valueOf(studentModel.getValueAt(row, 0)));
        nameField.setText(String.valueOf(studentModel.getValueAt(row, 1)));
        programmeField.setText(String.valueOf(studentModel.getValueAt(row, 2)));
        marksField.setText(String.valueOf(studentModel.getValueAt(row, 3)));
    }

    private void showStudent(Student student) {
        idField.setText(student.getId());
        nameField.setText(student.getName());
        programmeField.setText(student.getProgramme());
        marksField.setText(String.format("%.1f", student.getMarks()));
    }

    private void clearStudentForm() {
        idField.setText("");
        nameField.setText("");
        programmeField.setText("");
        marksField.setText("");
    }

    private Double readMarks() {
        String text = text(marksField);
        if (text.isEmpty()) {
            warn("Enter marks from 0 to 100.");
            return null;
        }
        try {
            double marks = Double.parseDouble(text);
            if (marks < 0 || marks > 100) {
                warn("Marks must be between 0 and 100.");
                return null;
            }
            return marks;
        } catch (NumberFormatException ex) {
            warn("Marks must be a number from 0 to 100.");
            return null;
        }
    }

    private String capture(Runnable action) {
        PrintStream previous = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.out.flush();
            System.setOut(previous);
        }
        return buffer.toString(StandardCharsets.UTF_8).trim();
    }

    private void status(String message) {
        statusLabel.setText(message);
    }

    private void warn(String message) {
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Check the input", JOptionPane.WARNING_MESSAGE);
    }

    private static String text(JTextField field) {
        return field.getText().trim();
    }

    private static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    private static JPanel column(Component... parts) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 8));
        for (Component part : parts) {
            stretch(part);
            panel.add(part);
            panel.add(Box.createVerticalStrut(6));
        }
        return panel;
    }

    private static JPanel section(String title, Component... parts) {
        JPanel panel = column(parts);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height + 24));
        return panel;
    }

    private static JScrollPane side(JPanel content) {
        JPanel holder = new JPanel(new BorderLayout());
        holder.setOpaque(false);
        holder.add(content, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(holder);
        scroll.setPreferredSize(new Dimension(320, 400));
        scroll.setMinimumSize(new Dimension(280, 200));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private static void stretch(Component part) {
        if (part instanceof JComponent) {
            ((JComponent) part).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        Dimension preferred = part.getPreferredSize();
        int height = Math.max(preferred.height, 28);
        part.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

    private static JButton button(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setMargin(new java.awt.Insets(6, 10, 6, 10));
        button.addActionListener(event -> action.run());
        return button;
    }

    private static JTextArea area() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setMargin(new java.awt.Insets(8, 8, 8, 8));
        return area;
    }

    private static JPanel titled(String title, JScrollPane pane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(pane, BorderLayout.CENTER);
        return panel;
    }
}
