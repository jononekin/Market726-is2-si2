package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import businesslogic.*;
import domain.*;

public class BasketGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tableBasket = new JTable();
    private DefaultTableModel tableModelBasket;
    private JScrollPane scrollPane = new JScrollPane();


    private JButton btnAdd = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.Add"));
    private JButton btnRemove = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.Remove"));
    private JButton btnBuy = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.BuyAll"));

    private String userEmail;


    private String[] columnNames = new String[] {
        ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ColTitle"),
        ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ColPrice"),
        ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ColSeller"),
        "SaleObj"
    };

    public BasketGUI(String userEmail) {
        this.userEmail = userEmail;

     
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.Title") + " - " + userEmail);
        this.setSize(new Dimension(600, 400));
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);

    
        tableModelBasket = new DefaultTableModel(null, columnNames);
        tableBasket.setModel(tableModelBasket);
        tableBasket.getColumnModel().getColumn(3).setMinWidth(0);
        tableBasket.getColumnModel().getColumn(3).setMaxWidth(0);
        tableBasket.getColumnModel().getColumn(3).setWidth(0);
        tableBasket.getColumnModel().getColumn(3).setPreferredWidth(0);

        scrollPane.setBounds(new Rectangle(30, 30, 520, 200));
        scrollPane.setViewportView(tableBasket);
        this.getContentPane().add(scrollPane);

       
        btnAdd.setBounds(30, 260, 120, 30);
        btnRemove.setBounds(170, 260, 120, 30);
        btnBuy.setBounds(310, 260, 120, 30);

        this.getContentPane().add(btnAdd);
        this.getContentPane().add(btnRemove);
        this.getContentPane().add(btnBuy);

     
        btnAdd.addActionListener(e -> addToBasket());
        btnRemove.addActionListener(e -> removeFromBasket());
        btnBuy.addActionListener(e -> buyBasket());

        loadBasket();
    }

   
    private void loadBasket() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            tableModelBasket.setRowCount(0);
            List<Sale> basket = facade.getBasket(userEmail);

            for (Sale s : basket) {
                Vector<Object> row = new Vector<>();
                row.add(s.getTitle());
                row.add(s.getPrice());
                row.add(s.getSeller().getEmail());
                row.add(s); 
                tableModelBasket.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    private void addToBasket() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();

            Sale selected = getSelectedSale();
            if (selected == null) {
            
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.SelectProduct"));
                return;
            }

            List<Sale> basket = facade.getBasket(userEmail);

            if (!basket.isEmpty()) {
                String sellerBasket = basket.get(0).getSeller().getEmail();
                String sellerNew = selected.getSeller().getEmail();

                if (!sellerBasket.equals(sellerNew)) {
                  
                    JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ErrorSameSeller"));
                    return;
                }
            }

            boolean ok = facade.addToBasket(userEmail, selected.getSaleNumber());
            if (!ok) {
                
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ErrorAdd"));
                return;
            }

            loadBasket();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ExceptionAdd"));
        }
    }

  
    private void removeFromBasket() {
        try {
            int row = tableBasket.getSelectedRow();
            if (row == -1) return;

            BLFacade facade = MainGUI.getBusinessLogic();
            Sale sale = (Sale) tableModelBasket.getValueAt(row, 3);
            facade.removeFromBasket(userEmail, sale.getSaleNumber());

            loadBasket();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    private void buyBasket() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            boolean ok = facade.buyBasket(userEmail);

            if (ok) {
               
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.PurchaseSuccess"));
            } else {
                
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BasketGUI.ErrorBuy"));
            }

            loadBasket();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    private Sale getSelectedSale() {
        int row = tableBasket.getSelectedRow();
        if (row == -1) return null;
        return (Sale) tableModelBasket.getValueAt(row, 3);
    }
}