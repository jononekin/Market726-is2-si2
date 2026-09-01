package gui;

import javax.swing.*;

import businesslogic.BLFacade;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class CreateEskaeraGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private String userEmail;
    
    private JLabel lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.lblTitle"));
    private JTextField txtTitle = new JTextField();
    
    private JLabel lblDesc = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.lblDesc"));
    private JTextArea txtDesc = new JTextArea();
    
    private JButton btnCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.btnCreate"));
    private JLabel lblMsg = new JLabel("");

    public CreateEskaeraGUI(String userEmail) {
        this.userEmail = userEmail;
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.Title"));
        this.setSize(new Dimension(400, 350));
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);

        lblTitle.setBounds(30, 20, 300, 20);
        this.getContentPane().add(lblTitle);

        txtTitle.setBounds(30, 45, 320, 25);
        this.getContentPane().add(txtTitle);

        lblDesc.setBounds(30, 80, 300, 20);
        this.getContentPane().add(lblDesc);

        txtDesc.setBounds(30, 105, 320, 100);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.getContentPane().add(txtDesc);

        btnCreate.setBounds(90, 230, 200, 30);
        this.getContentPane().add(btnCreate);

        lblMsg.setBounds(30, 270, 320, 20);
        this.getContentPane().add(lblMsg);

        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = txtTitle.getText().trim();
                String desc = txtDesc.getText().trim();

                if (title.isEmpty() || desc.isEmpty()) {
                    lblMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.ErrorEmpty"));
                    lblMsg.setForeground(Color.RED);
                    return;
                }

                try {
                    BLFacade facade = MainGUI.getBusinessLogic();
                    boolean ok = facade.createEskaera(userEmail, title, desc);
                    if (ok) {
                        lblMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.Success"));
                        lblMsg.setForeground(Color.GREEN);
                        txtTitle.setText("");
                        txtDesc.setText("");
                    } else {
                        lblMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEskaeraGUI.Error"));
                        lblMsg.setForeground(Color.RED);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}