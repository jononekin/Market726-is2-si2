package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import businesslogic.*;
import domain.*;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldErabiltzaile;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	
	private JFrame nirePantaila;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterGUI frame = new RegisterGUI();
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
	public RegisterGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.User"));
		lblNewLabel.setBounds(40, 63, 101, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblEmaila = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Gmail"));
		lblEmaila.setBounds(40, 96, 136, 14);
		contentPane.add(lblEmaila);
		
		JLabel lblNewLabel_1_1 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.pass"));
		lblNewLabel_1_1.setBounds(40, 128, 136, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.confirmPass"));
		lblNewLabel_1_1_1.setBounds(40, 162, 121, 14);
		contentPane.add(lblNewLabel_1_1_1);
		
		textFieldErabiltzaile = new JTextField();
		textFieldErabiltzaile.setBounds(186, 60, 86, 20);
		contentPane.add(textFieldErabiltzaile);
		textFieldErabiltzaile.setColumns(10);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(186, 93, 86, 20);
		contentPane.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(186, 125, 86, 20);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(186, 159, 86, 20);
		contentPane.add(textField_2);
		
		JLabel lblNewLabel_1 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		lblNewLabel_1.setBounds(183, 11, 101, 20);
		contentPane.add(lblNewLabel_1);
		
		nirePantaila = this;
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BLFacade facade = MainGUI.getBusinessLogic();
				Seller s = facade.isRegister(textFieldErabiltzaile.getText(), textField.getText(), textField_1.getText(), textField_2.getText());
				if (s!=null) {
					new MainGUIErregistratua(s.getEmail()).setVisible(true);
				    nirePantaila.setVisible(false);
				}else {
					System.out.println("Errorea erregistratzen");
				}
			}
		});
		btnNewButton.setBounds(154, 206, 130, 23);
		contentPane.add(btnNewButton);
		
		

	}
}
