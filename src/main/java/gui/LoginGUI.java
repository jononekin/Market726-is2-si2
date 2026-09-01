package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import businesslogic.*;
import domain.*;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	
	private JFrame nirePantaila;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI frame = new LoginGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(136, 38, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(136, 96, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.User"));
		lblNewLabel.setBounds(21, 41, 105, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.pass"));
		lblPass.setBounds(21, 99, 105, 14);
		contentPane.add(lblPass);
		
		nirePantaila = this;
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BLFacade facade = MainGUI.getBusinessLogic();

				Admin a = facade.isAdmin(
				    textField.getText(),
				    textField_1.getText()
				);

				if (a != null) {

				    new AdminGUI().setVisible(true);

				} else {

				    Seller s = facade.isLogged(
				        textField.getText(),
				        textField_1.getText()
				    );

				    if (s != null) {
				        new MainGUIErregistratua(s.getEmail()).setVisible(true);
				    }
				}
		   }
		
		});
		btnNewButton.setBounds(136, 183, 157, 23);
		contentPane.add(btnNewButton);

		}
}
