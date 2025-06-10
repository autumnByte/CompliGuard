import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.sql.*;
public class CyberComplianceApp {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DefaultListModel<String> reportModel = new DefaultListModel<>();
    private JFrame mainFrame;
    private JTabbedPane tabs;
    private JDialog chatDialog; // The floating chatbot popup

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CyberComplianceApp().showLoginUI());
    }

    public void showLoginUI() {
        loginFrame = createFrame("🔐 Admin Login", 400, 240);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 18, 28)); // Dark navy
        loginFrame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);

        JLabel userLabel = createLabel("👤 Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = createTextField(15);
        panel.add(usernameField, gbc);

        JLabel passLabel = createLabel("🔑 Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        styleTextField(passwordField);
        panel.add(passwordField, gbc);

        JButton loginButton = createButton("🚀 Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> handleLogin());

        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> loginButton.doClick());

        loginFrame.setVisible(true);
    }

    private JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        return frame;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 200, 210)); // soft light gray
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        styleTextField(field);
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn);
        return btn;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(30, 32, 45)); // dark but not black
        field.setForeground(new Color(220, 220, 230)); // gentle off-white
        field.setCaretColor(new Color(0, 255, 255)); // bright cyan caret
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        // Remove focus border highlight
        field.setFocusTraversalKeysEnabled(true);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 128, 148));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 215), 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        // Add hover effect using mouse listener
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 180, 215));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 128, 148));
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            MySQLHelper.saveUserLogin(username);
            loginFrame.dispose();
            showMainUI();
        } else {
            JOptionPane.showMessageDialog(loginFrame, "❌ Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMainUI() {
        mainFrame = createFrame("🛡️ Cyber Hygiene Compliance Checker", 800, 600);
        mainFrame.getContentPane().setBackground(new Color(18, 18, 28));
        tabs = new JTabbedPane();
        tabs.setBackground(new Color(25, 25, 38));
        tabs.setForeground(new Color(200, 200, 210));
        tabs.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        tabs.addTab("💻 Device Info", new DeviceInfoPanel());
        tabs.addTab("✅ Check Compliance", new ComplianceCheckPanel());
        tabs.addTab("📄 Reports", new ReportPanel());

        mainFrame.add(tabs, BorderLayout.CENTER);

        JButton chatButton = createChatButton();
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        southPanel.setBackground(new Color(18, 18, 28));
        southPanel.add(chatButton);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        chatButton.addActionListener(e -> openChatBotWindow());

        // Reposition chat dialog if main window moves or resizes
        mainFrame.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) { repositionChatDialog(); }
            public void componentResized(ComponentEvent e) { repositionChatDialog(); }
        });

        mainFrame.setVisible(true);
    }

    private JButton createChatButton() {
        ImageIcon icon = null;
        try {
            // Use getResource to load image from classpath
            java.net.URL imgURL = getClass().getResource("/images/Hello World_.jpg");
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(40, 35, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            } else {
                System.err.println("Couldn't find file: /image/Hello World_.jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton chatButton;
        if (icon != null) {
            chatButton = new JButton(icon);
        } else {
            chatButton = new JButton("🤖");
        }
        chatButton.setPreferredSize(new Dimension(48, 48));
        chatButton.setBackground(new Color(90, 90, 90));
        chatButton.setForeground(Color.WHITE);
        chatButton.setFocusPainted(false);
        chatButton.setBorderPainted(false);
        chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return chatButton;
    }



    // ---- Panels ----

    class DeviceInfoPanel extends JPanel {
        public DeviceInfoPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(25, 25, 38));

            JTextArea infoArea = new JTextArea();
            infoArea.setEditable(false);
            infoArea.setFont(new Font("Seoge UI Emoji", Font.PLAIN, 15));
            infoArea.setBackground(new Color(20, 20, 30));
            infoArea.setForeground(new Color(190, 190, 200));
            infoArea.setMargin(new Insets(15, 15, 15, 15));
            add(new JScrollPane(infoArea), BorderLayout.CENTER);

            try {
                String os = System.getProperty("os.name");
                String user = System.getProperty("user.name");
                InetAddress inet = InetAddress.getLocalHost();
                infoArea.setText("🖥️ OS: " + os + "\n👤 User: " + user +
                        "\n🌐 IP: " + inet.getHostAddress() + "\n🏷️ Host: " + inet.getHostName());
            } catch (Exception e) {
                infoArea.setText("⚠️ Error fetching system info.");
            }
        }
    }

    class ComplianceCheckPanel extends JPanel {
        JTextArea resultArea = new JTextArea();
        JProgressBar progressBar = new JProgressBar();
        JButton scanBtn;
        JButton scheduleBtn;
        public ComplianceCheckPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.DARK_GRAY);

            resultArea.setEditable(false);
            resultArea.setFont(new Font("Seoge UI Emoji", Font.PLAIN, 14));
            resultArea.setBackground(new Color(20, 20, 30));
            resultArea.setForeground(new Color(200, 200, 210));
            resultArea.setMargin(new Insets(12, 12, 12, 12));
            add(new JScrollPane(resultArea), BorderLayout.CENTER);

            progressBar.setVisible(false);
            progressBar.setIndeterminate(false);
            progressBar.setStringPainted(true);
            progressBar.setString("");
            add(progressBar, BorderLayout.NORTH);

            scanBtn = createButton("# Run Compliance Scan");
            scheduleBtn = createButton("⏱️ Auto Scan (3x every 5s)");

            scanBtn.addActionListener(e -> runComplianceScan());
            scheduleBtn.addActionListener(e -> scheduleScan());

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.DARK_GRAY);
            btnPanel.add(scanBtn);
            btnPanel.add(scheduleBtn);
            add(btnPanel, BorderLayout.SOUTH);
        }

        private void runComplianceScan() {
            resultArea.setText("🔍 Scanning...\n");
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setString("Scanning...");
            scanBtn.setEnabled(false);
            scheduleBtn.setEnabled(false);
            new Thread(() -> {
                StringBuilder sb = new StringBuilder();
                int totalScore = 0;
                int maxScore = 8; // Total checks

                String antivirus = checkAntivirus();
                sb.append("🛡️ Antivirus: ").append(antivirus).append("\n");
                if (antivirus.contains("Enabled") || antivirus.contains("✅")) totalScore++;

                String firewall = checkFirewall();
                sb.append("🔥 Firewall: ").append(firewall).append("\n");
                if (firewall.contains("Enabled") || firewall.contains("✅")) totalScore++;

                String encryption = checkDiskEncryption();
                sb.append("🔐 Disk Encryption: ").append(encryption).append("\n");
                if (encryption.contains("Enabled") || encryption.contains("✅")) totalScore++;

                String password = checkPasswordPolicy();
                sb.append("\u2696\ufe0f Password Policy: ").append(password).append("\n");
                if (password.contains("Strong") || password.contains("✅")) totalScore++;

                String access = checkUserAccessControls();
                sb.append("\ud83d\udeaa User Access Controls: ").append(access).append("\n");
                if (access.contains("Restricted") || access.contains("✅")) totalScore++;

                String devices = checkExternalDevices();
                sb.append("🛌 External Devices: ").append(devices).append("\n");
                if (devices.contains("Blocked") || devices.contains("✅")) totalScore++;

                String startup = checkStartupApps();
                sb.append("\ud83d\ude84 Startup Apps: ").append(startup).append("\n");
                if (startup.contains("Clean") || startup.contains("✅")) totalScore++;

                String updates = checkUpdates();
                sb.append("🔄 Updates: ").append(updates).append("\n");
                if (updates.contains("Up-to-date") || updates.contains("✅")) totalScore++;

                int compliancePercent = (int) ((totalScore / (double) maxScore) * 100);
                sb.append("\n📊 Compliance Score: ").append(compliancePercent).append("%\n");

                if (compliancePercent < 70) {
                    sb.append("⚠️ Risk Detected: Review security settings.\n");
                } else if (compliancePercent < 90) {
                    sb.append("✅ Moderate Compliance: Room for improvement.\n");
                } else {
                    sb.append("🎉 Excellent Compliance! Your system is secure.\n");
                }

                String finalReport = sb.toString();

                SwingUtilities.invokeLater(() -> {
                    resultArea.setText(finalReport);
                    reportModel.addElement(new Date() + ":\n" + finalReport);
                    MySQLHelper.saveComplianceReport(finalReport);
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                    progressBar.setString("");
                    scanBtn.setEnabled(true);
                    scheduleBtn.setEnabled(true);

                    if (finalReport.contains("DISABLED") || finalReport.contains("Not Found") || finalReport.contains("Not Enabled")) {
                        JOptionPane.showMessageDialog(mainFrame, "🚨 Non-compliance detected!", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                });
            }).start();
        }

        private void scheduleScan() {
            new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(5000);
                        runComplianceScan();
                    } catch (InterruptedException ignored) {
                    }
                }
            }).start();
        }

        private String checkAntivirus() {
            String result = runCommand("wmic /namespace:\\\\root\\SecurityCenter2 path AntiVirusProduct get displayName /format:list");
            return result.isEmpty() ? "❌ Not Found" : "✅ Detected";
        }

        private String checkFirewall() {
            String result = runCommand("netsh advfirewall show allprofiles");
            return result.toLowerCase().contains("on") ? "✅ ENABLED" : "❌ DISABLED";
        }

        private String checkUpdates() {
            String result = runCommand("powershell Get-HotFix | Sort-Object InstalledOn -Descending | Select -First 1");
            return result.isEmpty() ? "❓ Unknown" : "✅ Latest patch:\n" + result;
        }
        private String checkDiskEncryption() {
            String result = runCommand("manage-bde -status C:");
            if (result.toLowerCase().contains("percentage encrypted") && result.toLowerCase().contains("100")) {
                return "✅ Enabled (BitLocker)";
            } else if (result.toLowerCase().contains("protection off")) {
                return "❌ Not Enabled";
            } else if (result.toLowerCase().contains("access is denied")) {
                return "⚠ Access denied (run as admin)";
            } else {
                return "❓ Unknown/Not Supported";
            }
        }

        private String checkPasswordPolicy() {
            String result = runCommand("net accounts");
            if (result.contains("Minimum password length") && result.contains("Password required")) {
                return "✅ Enforced";
            } else {
                return "❌ Not Enforced";
            }
        }
        private String checkUserAccessControls() {
            String result = runCommand("whoami /groups");
            return result.toLowerCase().contains("administrators") ? "⚠ Admin Rights Granted" : "✅ Standard User";
        }

        private String checkExternalDevices() {
            String result = runCommand("powershell Get-PnpDevice -Class USB | Where-Object {$_.Status -eq 'OK'} | Select-Object -ExpandProperty FriendlyName");
            if (result.isEmpty()) {
                return "✅ No external devices connected";
            } else {
                // Format the output to match your example
                String[] devices = result.split("\n");
                StringBuilder sb = new StringBuilder("⚠ Multiple USB devices detected:\n");
                for (String device : devices) {
                    sb.append("    • ").append(device.trim()).append("\n");
                }
                return sb.toString();
            }
        }

        private String checkStartupApps() {
            String result = runCommand("powershell Get-CimInstance -ClassName Win32_StartupCommand | Select-Object -ExpandProperty Name");
            if (result.isEmpty()) {
                return "✅ Clean startup (No apps configured to run at startup)";
            } else {
                // Format the output to match your example
                String[] apps = result.split("\n");
                StringBuilder sb = new StringBuilder("⚠ " + apps.length + " Apps Found on Startup (May slow boot & pose risk)\n");
                for (String app : apps) {
                    sb.append("    • ").append(app.trim()).append("\n");
                }
                return sb.toString();
            }
        }


        private String runCommand(String cmd) {
            StringBuilder output = new StringBuilder();
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null)
                    output.append(line).append("\n");
                reader.close();
                p.waitFor();
            } catch (Exception e) {
                return "⚠️ Error: " + e.getMessage();
            }
            return output.toString().trim();
        }

        // Password complexity check (Windows Local Policy)
        private String checkPasswordComplexity() {
            String result = runCommand("net accounts");
            if (result.toLowerCase().contains("password complexity") && result.toLowerCase().contains("enabled")) {
                return "✅ Enforced";
            } else if (result.toLowerCase().contains("password complexity") && result.toLowerCase().contains("disabled")) {
                return "❌ Not Enforced";
            } else {
                return "❓ Unknown/Not Supported";
            }
        }

        // Software whitelisting check (AppLocker for Windows)
        private String checkSoftwareWhitelisting() {
            String result = runCommand("powershell Get-AppLockerPolicy -Effective | Select-String RuleCollection");
            if (result.toLowerCase().contains("rulecollection")) {
                return "✅ AppLocker Policy Present";
            } else {
                return "❌ Not Configured";
            }
        }

        // IDS check (simulate, or check for Windows Defender ATP service)
        private String checkIDS() {
            String result = runCommand("sc query Sense"); // 'Sense' is Windows Defender ATP service
            if (result.toLowerCase().contains("running")) {
                return "✅ Windows Defender ATP Running";
            } else {
                return "❌ Not Running/Not Installed";
            }
        }

    }

    class ReportPanel extends JPanel {
        public ReportPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(25, 25, 38));

            JList<String> reportList = new JList<>(reportModel);
            reportList.setBackground(new Color(20, 20, 30));
            reportList.setForeground(new Color(190, 190, 200));
            reportList.setFont(new Font("Consolas", Font.PLAIN, 14));
            add(new JScrollPane(reportList) {{
                getVerticalScrollBar().setUnitIncrement(14);
                setBorder(BorderFactory.createLineBorder(new Color(40, 50, 70), 1));
            }}, BorderLayout.CENTER);

            JButton exportBtn = createButton("📤 Export to File");
            exportBtn.addActionListener(e -> {
                try (PrintWriter pw = new PrintWriter("report.txt")) {
                    for (int i = 0; i < reportModel.size(); i++)
                        pw.println(reportModel.get(i));
                    JOptionPane.showMessageDialog(this, "✅ Saved to report.txt");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Failed: " + ex.getMessage());
                }
            });

            add(exportBtn, BorderLayout.SOUTH);
        }
    }

    // ---- Chatbot Popup ----

    private void openChatBotWindow() {
        if (chatDialog != null && chatDialog.isVisible()) {
            chatDialog.toFront();
            chatDialog.requestFocus();
            return;
        }

        chatDialog = new JDialog(mainFrame, "🤖 HelpBot", false); // false = not modal
        chatDialog.setSize(350, 400);
        chatDialog.setUndecorated(false); // Show title bar with close button
        chatDialog.setResizable(true);
        chatDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE); // Just hide, don't dispose
        chatDialog.setLayout(new BorderLayout(10, 10));
        chatDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        chatDialog.getContentPane().setBackground(Color.DARK_GRAY);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(40, 40, 40));
        chatArea.setForeground(Color.LIGHT_GRAY);
        chatArea.setFont(new Font("Seoge UI Emoji", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatArea.setText("Bot: Hello! \nHow can I assist you with cyber hygiene compliance today?\n\n");

        JTextField inputField = new JTextField();
        styleTextField(inputField);
        JButton sendButton = createButton("📨 Send");

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(Color.DARK_GRAY);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            if (input.isEmpty()) return;
            chatArea.append("You: " + input + "\n");

            String response;
            String inputLower = input.toLowerCase();
            if (inputLower.contains("antivirus"))
                response = "🛡️ Antivirus should be up-to-date and running.";
            else if (inputLower.contains("firewall"))
                response = "🔥 Firewall should be enabled.";
            else if (inputLower.contains("report"))
                response = "📄 You can find reports in the Reports tab.";
            else if (inputLower.contains("update") || inputLower.contains("patch"))
                response = "🔄 Updates status is available in Check Compliance.";
            else
                response = "🤖 I can help with antivirus, firewall, update, and report queries.";

            chatArea.append("Bot: " + response + "\n");
            inputField.setText("");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });

        inputField.addActionListener(e -> sendButton.doClick());

        chatDialog.add(scrollPane, BorderLayout.CENTER);
        chatDialog.add(inputPanel, BorderLayout.SOUTH);

        repositionChatDialog();

        chatDialog.setVisible(true);
    }

    // Reposition chat dialog in the lower right of mainFrame
    private void repositionChatDialog() {
        if (chatDialog != null && mainFrame != null) {
            Point mainLoc = mainFrame.getLocationOnScreen();
            Dimension mainSize = mainFrame.getSize();
            Dimension chatSize = chatDialog.getSize();

            int x = mainLoc.x + mainSize.width - chatSize.width - 20;
            int y = mainLoc.y + mainSize.height - chatSize.height - 40;

            chatDialog.setLocation(x, y);
        }
    }
    public class MySQLHelper {
        private static final String DB_URL = System.getenv("DB_URL");
        private static final String DB_USER = System.getenv("DB_USER");      // <--- change this
        private static final String DB_PASS = System.getenv("DB_PASS");

        static {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public static void saveUserLogin(String username) {
            String sql = "INSERT INTO user_logins (username) VALUES (?)";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void saveComplianceReport(String reportText) {
            String sql = "INSERT INTO compliance_reports (report_text) VALUES (?)";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, reportText);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static String getAllComplianceReports() {
            StringBuilder sb = new StringBuilder();
            String sql = "SELECT report_time, report_text FROM compliance_reports ORDER BY id DESC";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    sb.append(rs.getString("report_time"))
                            .append(":\n")
                            .append(rs.getString("report_text"))
                            .append("\n\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

}