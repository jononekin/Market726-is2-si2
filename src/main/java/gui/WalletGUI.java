package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import businesslogic.BLFacade;

public class WalletGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel labelSaldo;
    private JTextField textCantidad;
    private JButton btnAddMoney;
    private JButton btnClose;

    private String userEmail;

    public WalletGUI(String email) {
        this.userEmail = email;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MyMoney"));
        setSize(350, 220);
        setLayout(null);
        setLocationRelativeTo(null);

        //  Label saldo
        labelSaldo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("WalletGUI.DiruaOrain"));
        labelSaldo.setFont(new Font("Tahoma", Font.BOLD, 16));
        labelSaldo.setBounds(100, 20, 200, 30);
        add(labelSaldo);

        //  meter dinero
        JLabel labelCantidad = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("WalletGUI.Kantitatea"));
        labelCantidad.setBounds(40, 70, 80, 25);
        add(labelCantidad);

        textCantidad = new JTextField();
        textCantidad.setBounds(120, 70, 100, 25);
        add(textCantidad);

        //  Botï¿½n aï¿½adir dinero
        btnAddMoney = new JButton(ResourceBundle.getBundle("Etiquetas").getString("WalletGUI.DiruaGehitu"));
        btnAddMoney.setBounds(80, 110, 180, 30);
        add(btnAddMoney);

        
        btnClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnClose.setBounds(110, 150, 120, 25);
        add(btnClose);

      
        actualizarSaldo();

        
        btnAddMoney.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    float cantidad = Float.parseFloat(textCantidad.getText());

                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(null, "Cantidad debe ser mayor que 0");
                        return;
                    }

                    BLFacade facade = MainGUI.getBusinessLogic();
                    facade.addMoney(userEmail, cantidad);

                    actualizarSaldo();
                    textCantidad.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Introduce un nï¿½mero vï¿½lido");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

      
        btnClose.addActionListener(e -> setVisible(false));
    }

   
    private void actualizarSaldo() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            float money = facade.getMoney(userEmail);
            labelSaldo.setText(ResourceBundle.getBundle("Etiquetas").getString("WalletGUI.Dirua") + money + "€");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}