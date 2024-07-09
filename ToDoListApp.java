import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskField;
    private JTextField dueDateField;
    private JTextField priorityField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton sortByDateButton;
    private JButton sortByPriorityButton;
    private DataManager dataManager;
    private User currentUser;

    public ToDoListApp() {
        dataManager = new DataManager();
        registerOrLogin();

        frame = new JFrame("To-Do List Application");
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskField = new JTextField(20);
        dueDateField = new JTextField(10);
        priorityField = new JTextField(5);
        addButton = new JButton("Add Task");
        editButton = new JButton("Edit Task");
        deleteButton = new JButton("Delete Task");
        sortByDateButton = new JButton("Sort by Date");
        sortByPriorityButton = new JButton("Sort by Priority");

        // Set up the layout
        frame.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Due Date (yyyy-MM-dd):"));
        inputPanel.add(dueDateField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityField);
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(sortByDateButton);
        inputPanel.add(sortByPriorityButton);

        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Add listeners to buttons
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        sortByDateButton.addActionListener(new SortByDateButtonListener());
        sortByPriorityButton.addActionListener(new SortByPriorityButtonListener());

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        loadTasks();
    }

    private void registerOrLogin() {
        while (currentUser == null) {
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);
            panel.add(new JLabel("Confirm Password:"));
            panel.add(confirmPasswordField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Register or Login", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    try {
                        if (!dataManager.getUsers().containsKey(username)) {
                            currentUser = new User(username, password);
                            dataManager.getUsers().put(username, currentUser);
                            dataManager.saveUsers();
                        } else if (dataManager.getUsers().get(username).authenticate(password)) {
                            currentUser = dataManager.getUsers().get(username);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.exit(0);
            }
        }
    }
    private void loadTasks() {
        if (currentUser != null) {
            for (Task task : currentUser.getTasks()) {
                taskListModel.addElement(task);
            }
        }
    }

    // Action listener for the add button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String taskDescription = taskField.getText();
                Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateField.getText());
                int priority = Integer.parseInt(priorityField.getText());

                if (!taskDescription.isEmpty()) {
                    Task task = new Task(taskDescription, dueDate, priority);
                    taskListModel.addElement(task);
                    currentUser.addTask(task);
                    taskField.setText("");
                    dueDateField.setText("");
                    priorityField.setText("");
                    dataManager.saveUsers();
                } else {
                    JOptionPane.showMessageDialog(frame, "Task description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check the date format and priority.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Action listener for the edit button
    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                try {
                    String taskDescription = taskField.getText();
                    Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateField.getText());
                    int priority = Integer.parseInt(priorityField.getText());

                    if (!taskDescription.isEmpty()) {
                        Task task = taskListModel.get

ElementAt(selectedIndex);
                        task.setDescription(taskDescription);
                        task.setDueDate(dueDate);
                        task.setPriority(priority);
                        taskList.repaint();
                        taskField.setText("");
                        dueDateField.setText("");
                        priorityField.setText("");
                        dataManager.saveUsers();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Task description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please check the date format and priority.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Action listener for the delete button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task task = taskListModel.getElementAt(selectedIndex);
                taskListModel.remove(selectedIndex);
                currentUser.removeTask(task);
                dataManager.saveUsers();
            } else {
                JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Action listener for the sort by date button
private class SortByDateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Collections.sort(currentUser.getTasks(), (t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
        reloadTaskList();
    }
}

// Action listener for the sort by priority button
private class SortByPriorityButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Collections.sort(currentUser.getTasks(), (t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()));
        reloadTaskList();
    }
}

private void reloadTaskList() {
    taskListModel.clear();
    for (Task task : currentUser.getTasks()) {
        taskListModel.addElement(task);
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new ToDoListApp());
}
}


