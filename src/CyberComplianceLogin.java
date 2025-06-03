import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CyberComplianceLogin {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "hindalco123";

    public void showLoginScreen() {
        JFrame loginFrame = new JFrame("🔐 Admin Login - Cyber Compliance");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(userLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginFrame.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                loginFrame.dispose();
                SwingUtilities.invokeLater(() -> new CyberComplianceApp().showLoginUI()); // Call your main UI
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
    }
}
