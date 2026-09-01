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

public class AdminErreklamazioakGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable tableProducts = new JTable();
	private DefaultTableModel tableModelProducts;
	private JScrollPane scrollPanelProducts = new JScrollPane();
	
	private JButton btnOnartu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminErreklamazioakGUI.Onartu"));
	private JButton btnEzeztatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminErreklamazioakGUI.Ezeztatu"));

	private String[] columnNamesProducts = new String[] {
		    ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ProduktuIzena"),
		    ResourceBundle.getBundle("Etiquetas").getString("Reason"),
		    ResourceBundle.getBundle("Etiquetas").getString("AdminErreklamazioakGUI.Comprador"),
		    ResourceBundle.getBundle("Etiquetas").getString("Egoera"),
		    "SaleObj"
		};

	public AdminErreklamazioakGUI() {
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminErreklamazioakGUI.Title"));
		this.setSize(new Dimension(650, 350));
		this.getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);

		scrollPanelProducts.setBounds(new Rectangle(30, 30, 570, 200));
		tableModelProducts = new DefaultTableModel(null, columnNamesProducts);
		tableProducts.setModel(tableModelProducts);
		scrollPanelProducts.setViewportView(tableProducts);
		this.getContentPane().add(scrollPanelProducts);

		
		tableProducts.getColumnModel().getColumn(4).setMinWidth(0);
		tableProducts.getColumnModel().getColumn(4).setMaxWidth(0);

		btnOnartu.setBounds(new Rectangle(100, 250, 200, 30));
		btnOnartu.addActionListener(e -> resolver(true));
		this.getContentPane().add(btnOnartu);

		btnEzeztatu.setBounds(new Rectangle(320, 250, 200, 30));
		btnEzeztatu.addActionListener(e -> resolver(false));
		this.getContentPane().add(btnEzeztatu);

		cargar();
	}

	private void cargar() {
		BLFacade facade = MainGUI.getBusinessLogic();
		tableModelProducts.setRowCount(0); 

		List<Sale> claimed = facade.getClaimedSales();

		for (Sale sale : claimed) {
			Vector<Object> row = new Vector<>();
			row.add(sale.getTitle());
			row.add(sale.getErreklamazioa().getReason());
			row.add(sale.getErreklamazioa().getBuyerEmail());
			row.add(sale.getErreklamazioa().isTratatuta());
			row.add(sale);
			tableModelProducts.addRow(row);
		}
	}

	private void resolver(boolean aceptar) {
		int row = tableProducts.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Aukeratu erreklamazio bat! (Selecciona una reclamaci�n)");
			return;
		}

		Sale sale = (Sale) tableModelProducts.getValueAt(row, 4);
		BLFacade facade = MainGUI.getBusinessLogic();
		
		boolean ok = facade.resolveErreklamazioa(sale.getSaleNumber(), aceptar);

		if (ok) {
			if (aceptar) {
				JOptionPane.showMessageDialog(this, "Erreklamazioa ONARTU da. Dirua itzuli da.");
			} else {
				JOptionPane.showMessageDialog(this, "Erreklamazioa EZEZTATU da.");
			}
			cargar(); 
		} else {
			JOptionPane.showMessageDialog(this, "Errorea gertatu da.");
		}
	}
}