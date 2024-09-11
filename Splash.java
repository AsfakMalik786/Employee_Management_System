package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Splash extends JFrame {

    public Splash() {
        // Initialize JFrame settings
        setTitle("Splash Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set a fixed size for simplicity
        setLocationRelativeTo(null); // Center on the screen
        setLayout(new BorderLayout());

        // Load and display the image
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/front.gif"));
        if (icon != null) {
            JLabel imageLabel = new JLabel(icon);
            add(imageLabel, BorderLayout.CENTER);
        } else {
            System.err.println("Image not found!");
        }

        // Display the JFrame
        setVisible(true);

        // Set up a timer to transition to the Login screen
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Login(); // Open the next screen
            }
        });
        timer.setRepeats(false); // Timer should only execute once
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Splash(); // Create and display the splash screen
            }
        });
    }
}
