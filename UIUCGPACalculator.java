import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UIUCGPACalculator {
    private JFrame frame;
    private JPanel inputPanel, resultPanel;
    private JTextField courseCountField, prevCgpaField, prevCreditField;
    private JButton addButton, calculateButton;
    private JTextArea resultArea;
    private JScrollPane scrollPane;
    private ArrayList<JTextField> courseNames = new ArrayList<>();
    private ArrayList<JTextField> courseCredits = new ArrayList<>();
    private ArrayList<JTextField> courseGPs = new ArrayList<>();

    public UIUCGPACalculator() {
        frame = new JFrame("UIU CGPA Calculator");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Dark mode colors
        Color bgColor = new Color(40, 44, 52);
        Color fgColor = new Color(187, 187, 187);
        Color btnColor = new Color(60, 63, 65);

        JLabel title = new JLabel("UIU CGPA Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(fgColor);
        title.setBackground(bgColor);
        title.setOpaque(true);
        frame.add(title, BorderLayout.NORTH);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(bgColor);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgColor);
        topPanel.setForeground(fgColor);
        JLabel courseLabel = new JLabel("Number of Courses:");
        courseLabel.setForeground(fgColor);
        topPanel.add(courseLabel);
        courseCountField = new JTextField(5);
        topPanel.add(courseCountField);
        addButton = new JButton("Add");
        addButton.setBackground(btnColor);
        addButton.setForeground(fgColor);
        topPanel.add(addButton);
        inputPanel.add(topPanel);

        JPanel prevPanel = new JPanel();
        prevPanel.setBackground(bgColor);
        prevPanel.setForeground(fgColor);
        JLabel prevCgpaLabel = new JLabel("Previous CGPA:");
        prevCgpaLabel.setForeground(fgColor);
        prevPanel.add(prevCgpaLabel);
        prevCgpaField = new JTextField(5);
        prevPanel.add(prevCgpaField);
        JLabel prevCreditLabel = new JLabel("Completed Credits:");
        prevCreditLabel.setForeground(fgColor);
        prevPanel.add(prevCreditLabel);
        prevCreditField = new JTextField(5);
        prevPanel.add(prevCreditField);
        inputPanel.add(prevPanel);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(bgColor);

        JScrollPane scroll = new JScrollPane(resultPanel);
        scroll.setPreferredSize(new Dimension(550, 250));
        inputPanel.add(scroll);

        calculateButton = new JButton("Calculate");
        calculateButton.setBackground(btnColor);
        calculateButton.setForeground(fgColor);
        inputPanel.add(calculateButton);

        resultArea = new JTextArea(12, 50);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(30, 30, 30));
        resultArea.setForeground(fgColor);
        // Make the text bigger
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        scrollPane = new JScrollPane(resultArea);
        inputPanel.add(scrollPane);

        frame.add(inputPanel, BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addCourseFields(bgColor, fgColor));
        calculateButton.addActionListener(e -> calculateCGPA());

        frame.getContentPane().setBackground(bgColor);
        frame.setVisible(true);
    }

    private void addCourseFields(Color bgColor, Color fgColor) {
        resultPanel.removeAll();
        courseNames.clear();
        courseCredits.clear();
        courseGPs.clear();

        int numCourses;
        try {
            numCourses = Integer.parseInt(courseCountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Enter a valid number of courses.");
            return;
        }

        for (int i = 0; i < numCourses; i++) {
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new FlowLayout());
            coursePanel.setBackground(bgColor);

            JLabel nameLabel = new JLabel("Course " + (i + 1) + " Name:");
            nameLabel.setForeground(fgColor);
            JTextField nameField = new JTextField(15);
            courseNames.add(nameField);

            JLabel creditLabel = new JLabel("Credit Hour:");
            creditLabel.setForeground(fgColor);
            JTextField creditField = new JTextField(5);
            courseCredits.add(creditField);

            JLabel gpLabel = new JLabel("Grade Point:");
            gpLabel.setForeground(fgColor);
            JTextField gpField = new JTextField(5);
            courseGPs.add(gpField);

            coursePanel.add(nameLabel);
            coursePanel.add(nameField);
            coursePanel.add(creditLabel);
            coursePanel.add(creditField);
            coursePanel.add(gpLabel);
            coursePanel.add(gpField);

            resultPanel.add(coursePanel);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void calculateCGPA() {
        double totalCurrentPoints = 0;
        double totalCurrentCredits = 0;

        for (int i = 0; i < courseNames.size(); i++) {
            try {
                double credit = Double.parseDouble(courseCredits.get(i).getText());
                double gp = Double.parseDouble(courseGPs.get(i).getText());
                totalCurrentCredits += credit;
                totalCurrentPoints += credit * gp;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Enter valid numbers for credits and grade points.");
                return;
            }
        }

        double currentGPA = totalCurrentPoints / totalCurrentCredits;

        double prevCgpa = 0, prevCredits = 0;
        try {
            prevCgpa = Double.parseDouble(prevCgpaField.getText());
            prevCredits = Double.parseDouble(prevCreditField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Enter valid previous CGPA and credits.");
            return;
        }

        double newCGPA = ((prevCgpa * prevCredits) + (currentGPA * totalCurrentCredits)) / (prevCredits + totalCurrentCredits);

        StringBuilder result = new StringBuilder();
        result.append("===============================================\n");
        result.append("              CGPA CALCULATION REPORT\n");
        result.append("===============================================\n\n");

        // Show previous academic record
        result.append("PREVIOUS ACADEMIC RECORD:\n");
        result.append("• Previous CGPA: ").append(String.format("%.2f", prevCgpa)).append("\n");
        result.append("• Completed Credits: ").append(String.format("%.0f", prevCredits)).append("\n\n");

        // Show current semester courses
        result.append("THIS SEMESTER COURSES:\n");
        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i).getText().trim();
            if (courseName.isEmpty()) courseName = "Course " + (i + 1);
            double credit = Double.parseDouble(courseCredits.get(i).getText());
            double gp = Double.parseDouble(courseGPs.get(i).getText());
            result.append("• ").append(courseName).append(" - Credit: ").append(String.format("%.0f", credit))
                    .append(", Grade: ").append(String.format("%.2f", gp)).append("\n");
        }

        result.append("\nTOTAL CURRENT CREDITS: ").append(String.format("%.0f", totalCurrentCredits)).append("\n\n");

        // Show calculated results
        result.append("CALCULATION RESULTS:\n");
        result.append("• Current Semester GPA: ").append(String.format("%.2f", currentGPA)).append("\n");
        result.append("• Updated CGPA: ").append(String.format("%.2f", newCGPA)).append("\n");
        result.append("• Total Credits Completed: ").append(String.format("%.0f", prevCredits + totalCurrentCredits)).append("\n\n");

        // Show performance message
        result.append("PERFORMANCE ANALYSIS:\n");
        if (newCGPA >= 3.50) {
            result.append("🎉 Congratulations! You're eligible for a waiver for the next semester.");
        } else if (newCGPA > prevCgpa) {
            result.append("👏 Congratulations you did it! You can make it happen.\n Let them see your best at the next sem... Go go go Champ!");
        } else {
            result.append("💪 You tried hard, but this time it's not happening.\n Don't worry — you will definitely do better in the next.\n Keep faith in yourself and Allah. Insha'Allah!");
        }

        resultArea.setText(result.toString());
    }
}