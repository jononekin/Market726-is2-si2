package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


import businesslogic.*;
import domain.*;

public class SalaketaGUI extends JFrame {

    
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
    private JButton btnEnviar;
    private JButton btnCerrar;
	private Sale sale;
	private String userEmail;


    public SalaketaGUI(String userEmail,Sale sale) {
        this.sale = sale;
        this.userEmail = userEmail;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"));
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel label = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Reason"));
        label.setBounds(30, 20, 100, 20);
        add(label);

        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(30, 50, 320, 120);
        add(scroll);

        btnEnviar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SalaketaGUI.Bidali"));
        btnEnviar.setBounds(60, 200, 100, 30);
        add(btnEnviar);

        btnCerrar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnCerrar.setBounds(200, 200, 100, 30);
        add(btnCerrar);

        
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String reason = textArea.getText();

                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Idatzi arrazoia");
                    return;
                }

                BLFacade facade = MainGUI.getBusinessLogic();
                boolean ok = facade.reportSale(userEmail, sale.getSaleNumber(), reason);

                if (ok) {
                    JOptionPane.showMessageDialog(null, "Salaketa bidalita!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Errorea");
                }
            }
        });

        btnCerrar.addActionListener(e -> dispose());
    }
}