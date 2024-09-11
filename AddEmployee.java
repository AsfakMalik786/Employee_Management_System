package employee.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Random;

public class AddEmployee extends JFrame implements ActionListener {
    Random ran = new Random();
    int number = ran.nextInt(999999);
    JTextField tname, tfname, taddress, tphone, taadhar, temail, tsalary, tdesignation;
    JLabel tempid;
    JDateChooser tdob;
    JComboBox<String> Boxeducation;
    JButton add, back;

    AddEmployee() {
        setTitle("Add Employee Detail");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use GridBagLayout for the main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(163, 255, 188));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add components using GridBagLayout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel heading = new JLabel("Add Employee Detail", JLabel.CENTER);
        heading.setFont(new Font("serif", Font.BOLD, 25));
        contentPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel name = new JLabel("Name");
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(name, gbc);

        gbc.gridx++;
        tname = new JTextField();
        tname.setBackground(new Color(177, 252, 197));
        contentPanel.add(tname, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fname = new JLabel("Father's Name");
        fname.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(fname, gbc);

        gbc.gridx++;
        tfname = new JTextField();
        tfname.setBackground(new Color(177, 252, 197));
        contentPanel.add(tfname, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dob = new JLabel("Date Of Birth");
        dob.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(dob, gbc);

        gbc.gridx++;
        tdob = new JDateChooser();
        tdob.setBackground(new Color(177, 252, 197));
        contentPanel.add(tdob, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel salary = new JLabel("Salary");
        salary.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(salary, gbc);

        gbc.gridx++;
        tsalary = new JTextField();
        tsalary.setBackground(new Color(177, 252, 197));
        contentPanel.add(tsalary, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel address = new JLabel("Address");
        address.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(address, gbc);

        gbc.gridx++;
        taddress = new JTextField();
        taddress.setBackground(new Color(177, 252, 197));
        contentPanel.add(taddress, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phone = new JLabel("Phone");
        phone.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(phone, gbc);

        gbc.gridx++;
        tphone = new JTextField();
        tphone.setBackground(new Color(177, 252, 197));
        contentPanel.add(tphone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel email = new JLabel("Email");
        email.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(email, gbc);

        gbc.gridx++;
        temail = new JTextField();
        temail.setBackground(new Color(177, 252, 197));
        contentPanel.add(temail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel education = new JLabel("Highest Education");
        education.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(education, gbc);

        gbc.gridx++;
        String[] items = {"BBA", "BCA", "CSE", "B.TECH", "BSC", "B.COM", "MBA", "MCA", "MA", "MTECH", "MSC", "PHD"};
        Boxeducation = new JComboBox<>(items);
        Boxeducation.setBackground(new Color(177, 252, 197));
        contentPanel.add(Boxeducation, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel aadhar = new JLabel("Aadhar Number");
        aadhar.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(aadhar, gbc);

        gbc.gridx++;
        taadhar = new JTextField();
        taadhar.setBackground(new Color(177, 252, 197));
        contentPanel.add(taadhar, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel empid = new JLabel("Employee ID");
        empid.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(empid, gbc);

        gbc.gridx++;
        tempid = new JLabel("" + number);
        tempid.setFont(new Font("SAN_SARIF", Font.BOLD, 20));
        tempid.setForeground(Color.RED);
        contentPanel.add(tempid, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel designation = new JLabel("Designation");
        designation.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        contentPanel.add(designation, gbc);

        gbc.gridx++;
        tdesignation = new JTextField();
        tdesignation.setBackground(new Color(177, 252, 197));
        contentPanel.add(tdesignation, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add = new JButton("ADD");
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add.addActionListener(this);
        contentPanel.add(add, gbc);

        gbc.gridy++;
        back = new JButton("BACK");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        contentPanel.add(back, gbc);

        add(contentPanel);

        setSize(900, 700);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            String name = tname.getText();
            String fname = tfname.getText();
            String dob = ((JTextField) tdob.getDateEditor().getUiComponent()).getText();
            String salary = tsalary.getText();
            String address = taddress.getText();
            String phone = tphone.getText();
            String email = temail.getText();
            String education = (String) Boxeducation.getSelectedItem();
            String designation = tdesignation.getText();
            String aadhar = taadhar.getText();
            String empid = tempid.getText();

            try {
                conn c = new conn();
                String query = "INSERT INTO employee (name, fname, dob, salary, address, phone, email, designation, aadhar, empid, education) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = c.getConnection().prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setString(3, dob);
                pst.setString(4, salary);
                pst.setString(5, address);
                pst.setString(6, phone);
                pst.setString(7, email);
                pst.setString(8, designation);
                pst.setString(9, aadhar);
                pst.setString(10, empid);
                pst.setString(11, education);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Details added successfully");
                setVisible(false);
                new Main_class();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == back) {
            setVisible(false);
            new Main_class();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEmployee::new);
    }
}
