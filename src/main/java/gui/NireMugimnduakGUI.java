package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import businesslogic.*;
import domain.*;

public class NireMugimnduakGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel jLabelProducts = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Title"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Close"));
	private JScrollPane scrollPanelProducts = new JScrollPane();
	private JTable tableProducts = new JTable();
	private DefaultTableModel tableModelProducts;
	private JFrame thisFrame;
	

	private JButton btnJasota = new JButton(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.btnJasota"));
	
	private String[] columnNamesProducts = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Mota"), 
			ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ProduktuIzena"),
			ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ProduktuPrezioa"), 
			ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ProduktuArgitaratzeData"),
			"SaleObj"
	};

	public NireMugimnduakGUI(String loggedUserEmail) {
		
		thisFrame = this;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(650, 350));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Title")+ " - " +  loggedUserEmail);

		jLabelProducts.setBounds(30, 20, 427, 16);
		this.getContentPane().add(jLabelProducts);

		scrollPanelProducts.setBounds(new Rectangle(30, 50, 570, 180));
		scrollPanelProducts.setViewportView(tableProducts);
		tableModelProducts = new DefaultTableModel(null, columnNamesProducts);
		tableProducts.setModel(tableModelProducts);
		this.getContentPane().add(scrollPanelProducts, null);

		
		
		JButton btnErreklamatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Erreklamatu"));
		btnErreklamatu.setBounds(new Rectangle(30, 260, 150, 30));
		btnErreklamatu.addActionListener(e -> {
			int row = tableProducts.getSelectedRow();
			if (row != -1) {
				String mota = (String) tableModelProducts.getValueAt(row, 0);
				
				if (mota.equals(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Compra"))) {
					Sale sale = (Sale) tableModelProducts.getValueAt(row, 4); 
					new ErreklamatuGUI(loggedUserEmail, sale).setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Bakarrik erosketak erreklamatu ditzakezu!");
				}
			}
		});
		this.getContentPane().add(btnErreklamatu);

		jButtonClose.setBounds(new Rectangle(190, 260, 130, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
			}
		});
		this.getContentPane().add(jButtonClose, null);

		
		btnJasota.setBounds(new Rectangle(330, 260, 270, 30));
		btnJasota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableProducts.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ErrorSelectPurchase"));
					return;
				}
				
				String mota = (String) tableModelProducts.getValueAt(row, 0);
				if (!mota.equals(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Compra"))) {
					JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ErrorOnlyPurchases"));
					return;
				}

				Sale selectedSale = (Sale) tableModelProducts.getValueAt(row, 4); 

				if (selectedSale.getBidalketa() == null) {
					JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ErrorNoShipping"));
					return;
				}
				
				if (selectedSale.getBidalketa().getEgoera().equals("JASOTA")) {
					JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ErrorAlreadyReceived"));
					return;
				}

				try {
					BLFacade facade = MainGUI.getBusinessLogic();
					boolean ok = facade.confirmArrival(selectedSale.getSaleNumber());
					
					if (ok) {
						JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.SuccessDelivery"));
						ErosketakKargatu(loggedUserEmail); 
					} else {
						JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.ErrorConfirm"));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		this.getContentPane().add(btnJasota);

		
		ErosketakKargatu(loggedUserEmail);
	}

	private void ErosketakKargatu (String email) {
		try {
			tableModelProducts.setDataVector(null, columnNamesProducts);
			tableModelProducts.setColumnCount(5);

			BLFacade facade = MainGUI.getBusinessLogic();
			List<Sale> misCompras = facade.getPurchasedItems(email);

			if (misCompras.isEmpty()) {
				jLabelProducts.setText("Oraindik ez dituzu produkturik erosi");
			} else {
				jLabelProducts.setText("Erositako produktuen lista (" + misCompras.size() + "):");
			}

			// BUCLE 1: COMPRAS
			for (Sale sale : misCompras) {
				Vector<Object> row = new Vector<Object>();
				row.add(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Compra"));
				
				String titulo = sale.getTitle();
				if(sale.getBidalketa() != null) {
					String estadoReal = sale.getBidalketa().getEgoera(); // "JASOTA", "PRESTATZEN"...
					String estadoTraducido = ResourceBundle.getBundle("Etiquetas").getString("Status." + estadoReal);
					titulo = titulo + " [" + estadoTraducido + "]";
				}
				row.add(titulo);
				row.add(sale.getPrice() + "");
				
				
				if (sale.getPubDate() != null) {
					row.add(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPubDate()));
				} else {
					row.add("---");
				}
				
				row.add(sale);
				tableModelProducts.addRow(row);
			}
			
		
			List<Sale> ventas = facade.getSoldItems(email);

			for (Sale sale : ventas) {
				Vector<Object> row = new Vector<Object>();
				row.add(ResourceBundle.getBundle("Etiquetas").getString("NireErosketakGUI.Venta"));
				
				String titulo = sale.getTitle();
				if(sale.getBidalketa() != null) {
					String estadoReal = sale.getBidalketa().getEgoera(); // "JASOTA", "PRESTATZEN"...
					String estadoTraducido = ResourceBundle.getBundle("Etiquetas").getString("Status." + estadoReal);
					titulo = titulo + " [" + estadoTraducido + "]";
				}
				row.add(titulo);
				row.add("+" + sale.getPrice());
				
				if (sale.getPubDate() != null) {
					row.add(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPubDate()));
				} else {
					row.add("---");
				}
				
				row.add(sale);
				tableModelProducts.addRow(row);
			}

			tableProducts.getColumnModel().getColumn(0).setPreferredWidth(80);
			tableProducts.getColumnModel().getColumn(1).setPreferredWidth(220);
			tableProducts.getColumnModel().getColumn(2).setPreferredWidth(80);
			tableProducts.getColumnModel().getColumn(3).setPreferredWidth(120);
			tableProducts.getColumnModel().getColumn(4).setMinWidth(0);
			tableProducts.getColumnModel().getColumn(4).setMaxWidth(0);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}