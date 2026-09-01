package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


import businesslogic.*;
import domain.*;

public class ErreklamatuGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JButton btnEnviar;
	private JButton btnCerrar;
	private Sale sale;
	private String userEmail;

	public ErreklamatuGUI(String userEmail, Sale sale) {
		this.sale = sale;
		this.userEmail = userEmail;

		setTitle(ResourceBundle.getBundle("Etiquetas").getString("ErreklamatuGUI.Title"));
		setSize(400, 300);
		setLayout(null);
		setLocationRelativeTo(null);

		JLabel label = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErreklamatuGUI.Arrazoia"));
		label.setBounds(30, 20, 150, 20);
		add(label);

		textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(30, 50, 320, 120);
		add(scroll);

		btnEnviar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamatuGUI.Bidali"));
		btnEnviar.setBounds(60, 200, 120, 30);
		add(btnEnviar);

		btnCerrar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
		btnCerrar.setBounds(200, 200, 120, 30);
		add(btnCerrar);

		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reason = textArea.getText();

				if (reason.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Idatzi arrazoia mesedez.");
					return;
				}

				BLFacade facade = MainGUI.getBusinessLogic();
				
				boolean ok = facade.addErreklamazioa(sale.getSaleNumber(), reason, userEmail);

				if (ok) {
					JOptionPane.showMessageDialog(null, "Erreklamazioa bidalita! (Enviado)");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Errorea: Ezin izan da erreklamatu (Quizás ya está reclamado).");
				}
			}
		});

		btnCerrar.addActionListener(e -> dispose());
	}
}