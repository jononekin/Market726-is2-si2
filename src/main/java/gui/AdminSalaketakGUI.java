package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import businesslogic.*;
import domain.*;

public class AdminSalaketakGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable tableProducts = new JTable();
	private DefaultTableModel tableModelProducts;
	private JScrollPane scrollPanelProducts = new JScrollPane();
	private JButton btnKudeatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminSalaketakGUI.Ezabatu"));
	private JButton btnEzeztatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminErreklamazioakGUI.Ezeztatu"));
	
	private String[] columnNamesProducts = new String[] {
		    ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ProduktuIzena"),
		    ResourceBundle.getBundle("Etiquetas").getString("Reason"),
		    ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.User"),
		    ResourceBundle.getBundle("Etiquetas").getString("Egoera"),
		    "SaleObj", "SalaketaObj"
		};

	public AdminSalaketakGUI() {
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminSalaketakGUI.Title"));
		this.setSize(new Dimension(600, 350));
		this.getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);

		scrollPanelProducts.setBounds(new Rectangle(30, 30, 520, 200));
		tableModelProducts = new DefaultTableModel(null, columnNamesProducts);
		tableProducts.setModel(tableModelProducts);
		scrollPanelProducts.setViewportView(tableProducts);
		this.getContentPane().add(scrollPanelProducts);

		
		tableProducts.getColumnModel().getColumn(4).setMinWidth(0);
		tableProducts.getColumnModel().getColumn(4).setMaxWidth(0);

		tableProducts.getColumnModel().getColumn(5).setMinWidth(0);
		tableProducts.getColumnModel().getColumn(5).setMaxWidth(0);

		
		btnKudeatu.setBounds(new Rectangle(65, 250, 206, 30));
		btnKudeatu.addActionListener(e -> eliminar(true));
		this.getContentPane().add(btnKudeatu);
		
		btnEzeztatu.setBounds(new Rectangle(334, 250, 150, 30));
		btnEzeztatu.addActionListener(e -> eliminar(false));

		this.getContentPane().add(btnEzeztatu);

		cargar();
	}

	private void cargar() {
		BLFacade facade = MainGUI.getBusinessLogic();
		tableModelProducts.setRowCount(0);

		List<Sale> reported = facade.getReportedSales();

		for (Sale sale : reported) {
			for (Salaketa sal : sale.getSalaketak()) {
				Vector<Object> row = new Vector<>();
				row.add(sale.getTitle());
				row.add(sal.getReason());
				row.add(sal.getUserEmail());
				if (sal.isTratatuta()) {
				    row.add("Tratatuta");
				} else {
				    row.add("Tratatu gabe");
				}
				row.add(sale);  
				row.add(sal);   
				tableModelProducts.addRow(row);
			}
		}
	}

	private void eliminar(boolean aceptar) {
		int row = tableProducts.getSelectedRow();
		if (row == -1) return;

		Sale sale = (Sale) tableModelProducts.getValueAt(row, 4);
		Salaketa sal = (Salaketa) tableModelProducts.getValueAt(row, 5);
		if (sal.isTratatuta()) {
		    JOptionPane.showMessageDialog(this,
		        "Salaketa hau jada tratatuta dago");
		    return;
		}

		BLFacade facade = MainGUI.getBusinessLogic();
		boolean eliminado = facade.resolveReport(sale.getSaleNumber(), sal, aceptar);

		if (eliminado) {
			 JOptionPane.showMessageDialog(this, "Salaketa eguneratua");
		     cargar();
		}
	}
}