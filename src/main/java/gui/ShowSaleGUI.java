package gui;

import java.util.*;
import java.util.List;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;


import businesslogic.*;
import domain.*;

public class ShowSaleGUI extends JFrame {
	
    File targetFile;
    BufferedImage targetImg;
    public JPanel panel_1;
    private static final int baseSize = 160;
    private static final String basePath="src/main/resources/images/";
	
    private static final long serialVersionUID = 1L;

    private JTextField fieldTitle=new JTextField();
    private JTextField fieldDescription=new JTextField();
	
    JLabel labelStatus = new JLabel(); 

    private JLabel jLabelTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Title"));
    private JLabel jLabelDescription = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Description")); 
    private JLabel jLabelProductStatus = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Status"));
    private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"));
    private JTextField fieldPrice = new JTextField();
    private File selectedFile;
    private String irudia;
    private String userEmail;

    private JScrollPane scrollPaneEvents = new JScrollPane();
    DefaultComboBoxModel<String> statusOptions = new DefaultComboBoxModel<String>();
    private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private JLabel jLabelMsg = new JLabel();
    private JLabel jLabelError = new JLabel();
    private JLabel statusField=new JLabel();
    private JFrame thisFrame;
    
    
    private JLabel jLabelSeller = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Seller"));
    private JLabel sellerField = new JLabel();
	
    public ShowSaleGUI(Sale sale, String userEmail) {
        this.userEmail = userEmail;
        thisFrame=this; 
        this.setVisible(true);
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(604, 370));

        fieldTitle.setText(sale.getTitle());
        fieldDescription.setText(sale.getDescription());
        fieldPrice.setText(Float.toString(sale.getPrice()));	
		
        jLabelSeller.setBounds(215, 214, 100, 20);
        getContentPane().add(jLabelSeller);

        sellerField.setBounds(310, 214, 188, 20);
        getContentPane().add(sellerField);
		
        sellerField.setText(sale.getSeller().getName());
		
        if (sale != null && sale.getPubDate() != null) {
            labelStatus.setText(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPubDate()));
        } else {
            labelStatus.setText("Fecha no disponible");
        }
		
        jLabelTitle.setBounds(new Rectangle(6, 56, 92, 20));
        jLabelPrice.setBounds(new Rectangle(6, 166, 101, 20));
        fieldPrice.setEditable(false);
        fieldPrice.setBounds(new Rectangle(137, 166, 60, 20));

        scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));
        jButtonClose.setBounds(new Rectangle(16, 272, 114, 30));
        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thisFrame.setVisible(false);			
            }
        });

        jLabelError.setBounds(new Rectangle(19, 245, 289, 20));
        jLabelError.setForeground(Color.red);
        this.getContentPane().add(jLabelError, null);

        this.getContentPane().add(jButtonClose, null);
        this.getContentPane().add(jLabelTitle, null);
		
        this.getContentPane().add(jLabelPrice, null);
        this.getContentPane().add(fieldPrice, null);
		
        jLabelProductStatus.setBounds(new Rectangle(40, 15, 140, 25));
        jLabelProductStatus.setBounds(6, 187, 140, 25);
        getContentPane().add(jLabelProductStatus);
		
        jLabelDescription.setBounds(6, 81, 109, 16);
        getContentPane().add(jLabelDescription);
        fieldTitle.setEditable(false);
		
        fieldTitle.setBounds(128, 53, 370, 26);
        getContentPane().add(fieldTitle);
        fieldTitle.setColumns(10);
        fieldDescription.setEditable(false);
		
        fieldDescription.setBounds(127, 81, 371, 73);
        getContentPane().add(fieldDescription);
        fieldDescription.setColumns(10);
		
        panel_1 = new JPanel();
        panel_1.setBounds(422, 160, 109, 105);
        getContentPane().add(panel_1);
        panel_1.add(jLabelMsg);
        jLabelMsg.setForeground(Color.red);
		
        labelStatus.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelStatus.setBounds(38, 214, 289, 16);
        getContentPane().add(labelStatus);
		
        BLFacade facade = MainGUI.getBusinessLogic();
        String file=sale.getFileName();
        if (file!=null) {
        	Image img = null;
        	/*byte[] imageBytes = facade.downloadImage(file);
        	
        	if (imageBytes != null) {
        	    try {
        	        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(imageBytes);
        	        img = javax.imageio.ImageIO.read(bis);
        	    } catch (Exception ex) {
        	        System.out.println("Error al reconstruir la imagen: " + ex.getMessage());
        	    }
        	}
            targetImg = rescale((BufferedImage)img);
            panel_1.setLayout(new BorderLayout(0, 0));
            panel_1.add(new JLabel(new ImageIcon(targetImg))); */
        }
        System.out.println("status: "+sale.getStatus());
        statusField = new JLabel(Utils.getStatus(sale.getStatus())); 
        statusField.setBounds(137, 191, 92, 16);
        getContentPane().add(statusField);
		
      
        JButton ErosiButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Buy")); 
        ErosiButton.setBounds(192, 276, 89, 23);
	
        ErosiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                jLabelError.setText("");
                jLabelMsg.setText("");
                
                if (userEmail == null || userEmail.isEmpty()) {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorMustLogin"));
                    return;
                }
                
                if (userEmail.equals(sale.getSeller().getEmail())) {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorBuyOwnProduct"));
                    return;
                }
                
                BLFacade facade = MainGUI.getBusinessLogic();
                boolean success = facade.buyProduct(userEmail, sale.getSaleNumber());
                
                if (success) {
                    jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Bought"));
                    ErosiButton.setEnabled(false); 
                } else {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorBuying"));
                }
            }
        });
        getContentPane().add(ErosiButton);
        this.repaint();
        setVisible(true);
		
        
        JButton btnSalatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Salatu"));
        btnSalatu.setBounds(400, 276, 100, 23);
        getContentPane().add(btnSalatu);
		
        btnSalatu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SalaketaGUI(userEmail, sale).setVisible(true);
            }
        });
		
       
        JButton btnAddBasket = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.AddToBasket"));
        btnAddBasket.setBounds(300, 276, 100, 23);
        getContentPane().add(btnAddBasket);
		
        btnAddBasket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                jLabelError.setText("");
                jLabelMsg.setText("");

                if (userEmail == null || userEmail.isEmpty()) {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorMustLogin"));
                    return;
                }
                if (userEmail.equals(sale.getSeller().getEmail())) {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorBuyOwnProduct"));
                    return;
                }

                BLFacade facade = MainGUI.getBusinessLogic();
                
                List<Sale> currentBasket = facade.getBasket(userEmail);
                boolean alreadyInBasket = false;
                
                if (currentBasket != null) {
                    for (Sale s : currentBasket) {
                        if (s.getSaleNumber() != null && s.getSaleNumber().equals(sale.getSaleNumber())) {
                            alreadyInBasket = true;
                            break;
                        }
                    }
                }

                if (alreadyInBasket) {
                    jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorAlreadyInBasket"));
                } else {
                	boolean ok = facade.addToBasket(userEmail, sale.getSaleNumber());

                    if (ok) {
                        jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.AddedToBasket"));
                    } else {
                        jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ErrorDifferentSeller"));
                    }
                }
            }
        });
    }   
    
    public BufferedImage rescale(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
        g.dispose();
        return resizedImage;
    }
}