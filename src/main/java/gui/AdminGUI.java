package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AdminGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public AdminGUI() {
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Title"));
		this.setSize(new Dimension(400, 250));
		this.getContentPane().setLayout(null);
		this.setLocationRelativeTo(null); 

		
		JButton btnSalaketak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.SalektakKudeatu"));
		btnSalaketak.setBounds(new Rectangle(90, 60, 200, 40));
		btnSalaketak.addActionListener(e -> {
			new AdminSalaketakGUI().setVisible(true); 
		});
		this.getContentPane().add(btnSalaketak);

		
		JButton btnVerErreklamazioak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.ErreklamazioakIkusi"));
		btnVerErreklamazioak.setBounds(new Rectangle(90, 120, 200, 40));
		btnVerErreklamazioak.addActionListener(e -> {
			new AdminErreklamazioakGUI().setVisible(true); 
		});
		this.getContentPane().add(btnVerErreklamazioak);
	}
}
