package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


import businesslogic.*;
import domain.*;

public class NireEskaerakGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private String userEmail;
    
    private JTable tableRequests = new JTable();
    private DefaultTableModel modelRequests;
    private JScrollPane scrollRequests = new JScrollPane();
    
    private JTable tableOffers = new JTable();
    private DefaultTableModel modelOffers;
    private JScrollPane scrollOffers = new JScrollPane();

    private JButton btnAccept = new JButton(ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.btnAccept"));

    private String[] reqCols = {
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColId"), 
        ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ColTitle"), 
        ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ColStatus"), 
        "EskaeraObj"
    };
    
    private String[] offCols = {
        ResourceBundle.getBundle("Etiquetas").getString("ViewEskaerakGUI.ColId"), 
        ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ColSeller"), 
        ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ColPrice"), 
        ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ColMessage"), 
        "EskaintzaObj"
    };

    public NireEskaerakGUI(String userEmail) {
        this.userEmail = userEmail;
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.Title"));
        this.setSize(new Dimension(600, 500));
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);

        JLabel lblTop = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.lblTop"));
        lblTop.setBounds(20, 10, 200, 20);
        this.getContentPane().add(lblTop);
        
        modelRequests = new DefaultTableModel(null, reqCols);
        tableRequests.setModel(modelRequests);
        tableRequests.getColumnModel().getColumn(3).setMinWidth(0);
        tableRequests.getColumnModel().getColumn(3).setMaxWidth(0);

        scrollRequests.setBounds(new Rectangle(20, 30, 540, 150));
        scrollRequests.setViewportView(tableRequests);
        this.getContentPane().add(scrollRequests);

        JLabel lblBot = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.lblBot"));
        lblBot.setBounds(20, 200, 300, 20);
        this.getContentPane().add(lblBot);

        modelOffers = new DefaultTableModel(null, offCols);
        tableOffers.setModel(modelOffers);
        tableOffers.getColumnModel().getColumn(4).setMinWidth(0);
        tableOffers.getColumnModel().getColumn(4).setMaxWidth(0);

        scrollOffers.setBounds(new Rectangle(20, 220, 540, 150));
        scrollOffers.setViewportView(tableOffers);
        this.getContentPane().add(scrollOffers);

        btnAccept.setBounds(180, 400, 220, 30);
        this.getContentPane().add(btnAccept);

        tableRequests.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    loadOffersForSelectedRequest();
                }
            }
        });

        btnAccept.addActionListener(e -> acceptOffer());

        loadMyRequests();
    }

    private void loadMyRequests() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            List<Eskaera> allOpen = facade.getOpenEskaerak();
            modelRequests.setRowCount(0);

            for (Eskaera e : allOpen) {
                if (e.getBuyer().getEmail().equals(userEmail)) {
                    Vector<Object> row = new Vector<>();
                    row.add(e.getId());
                    row.add(e.getTitle());
                    row.add(e.isClosed() ? ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.StatusClosed") : ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.StatusOpen"));
                    row.add(e);
                    modelRequests.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOffersForSelectedRequest() {
        int row = tableRequests.getSelectedRow();
        modelOffers.setRowCount(0); 
        if (row == -1) return;

        Eskaera selected = (Eskaera) modelRequests.getValueAt(row, 3);
        for (Eskaintza off : selected.getEskaintzak()) {
            Vector<Object> r = new Vector<>();
            r.add(off.getId());
            r.add(off.getSeller().getEmail());
            r.add(off.getPrice());
            r.add(off.getMessage());
            r.add(off);
            modelOffers.addRow(r);
        }
    }

    private void acceptOffer() {
        int reqRow = tableRequests.getSelectedRow();
        int offRow = tableOffers.getSelectedRow();

        if (reqRow == -1 || offRow == -1) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ErrorSelect"));
            return;
        }

        Eskaera req = (Eskaera) modelRequests.getValueAt(reqRow, 3);
        Eskaintza off = (Eskaintza) modelOffers.getValueAt(offRow, 4);

        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            boolean ok = facade.acceptEskaintza(req.getId(), off.getId());

            if (ok) {
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.Success"));
                modelOffers.setRowCount(0);
                loadMyRequests();
            } else {
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("NireEskaerakGUI.ErrorBuy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}