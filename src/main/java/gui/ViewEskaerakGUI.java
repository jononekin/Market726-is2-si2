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

public class ViewEskaerakGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private String userEmail;
    private JTable table = new JTable();
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane = new JScrollPane();
    private JButton btnMakeOffer = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.btnMakeOffer"));

    private String[] columnNames = {
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColId"), 
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColBuyer"), 
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColTitle"), 
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColDesc"), 
        "EskaeraObj"
    };

    public ViewEskaerakGUI(String userEmail) {
        this.userEmail = userEmail;
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.Title"));
        this.setSize(new Dimension(600, 400));
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(null, columnNames);
        table.setModel(tableModel);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        scrollPane.setBounds(new Rectangle(20, 20, 540, 250));
        scrollPane.setViewportView(table);
        this.getContentPane().add(scrollPane);

        btnMakeOffer.setBounds(180, 290, 220, 30);
        this.getContentPane().add(btnMakeOffer);

        btnMakeOffer.addActionListener(e -> makeOffer());

        loadEskaerak();
    }

    private void loadEskaerak() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            List<Eskaera> openRequests = facade.getOpenEskaerak();
            tableModel.setRowCount(0);

            for (Eskaera e : openRequests) {
                Vector<Object> row = new Vector<>();
                row.add(e.getId());
                row.add(e.getBuyer().getEmail());
                row.add(e.getTitle());
                row.add(e.getDescription());
                row.add(e);
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeOffer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ErrorSelect"));
            return;
        }

        Eskaera selected = (Eskaera) tableModel.getValueAt(row, 4);

        if (selected.getBuyer().getEmail().equals(this.userEmail)) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ErrorSelf"));
            return;
        }

        try {
            String priceStr = JOptionPane.showInputDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.InputPrice"));
            if (priceStr == null || priceStr.isEmpty()) return;
            float price = Float.parseFloat(priceStr);

            String message = JOptionPane.showInputDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.InputMessage"));
            if (message == null) message = "";

            BLFacade facade = MainGUI.getBusinessLogic();
            boolean ok = facade.addEskaintza(selected.getId(), userEmail, price, message);

            if (ok) {
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.Success"));
            } else {
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.Error"));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ErrorFormat"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}