package gui;

/**
 * @author Software Engineering teachers
 */

import javax.swing.*;

import businesslogic.BLFacade;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUIErregistratua extends JFrame {
    
    private String sellerMail;
    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;
    private JButton jButtonCreateQuery = null;
    private JButton jButtonQueryQueries = null;
    private JButton jButtonNireErosketak = null;
    private JButton jButtonWallet = null;
    
    private JButton btnCreateEskaera = null;
    private JButton btnViewEskaerak = null;
    private JButton btnNireEskaerak = null;

    private JButton btnBasket = null;

    private static BLFacade appFacadeInterface;
    
    public static BLFacade getBusinessLogic(){
        return appFacadeInterface;
    }
     
    public static void setBussinessLogic (BLFacade facade){
        appFacadeInterface=facade;
    }
    protected JLabel jLabelSelectOption;
    private JRadioButton rdbtnNewRadioButton;
    private JRadioButton rdbtnNewRadioButton_1;
    private JRadioButton rdbtnNewRadioButton_2;
    private JPanel panel;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    
    /**
     * This is the default constructor
     */
    public MainGUIErregistratua( String mail) {
        super();

        this.sellerMail=mail;
        
        this.setSize(495, 450);
        jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
        jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
        jLabelSelectOption.setForeground(Color.BLACK);
        jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
        
        rdbtnNewRadioButton = new JRadioButton("English");
        rdbtnNewRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Locale.setDefault(new Locale("en"));
                paintAgain();               
            }
        });
        buttonGroup.add(rdbtnNewRadioButton);
        
        rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
        rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Locale.setDefault(new Locale("eus"));
                paintAgain();               
            }
        });
        buttonGroup.add(rdbtnNewRadioButton_1);
        
        rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
        rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Locale.setDefault(new Locale("es"));
                paintAgain();
            }
        });
        buttonGroup.add(rdbtnNewRadioButton_2);
    
        panel = new JPanel();
        panel.add(rdbtnNewRadioButton_1);
        panel.add(rdbtnNewRadioButton_2);
        panel.add(rdbtnNewRadioButton);
        
        jButtonCreateQuery = new JButton();
        jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
        jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame a = new CreateSaleGUI(sellerMail);
                a.setVisible(true);
            }
        });
        
        jButtonQueryQueries = new JButton();
        jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
        jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame a = new QuerySalesGUI(sellerMail);
                a.setVisible(true);
            }
        });
        
        jButtonNireErosketak = new JButton();
        jButtonNireErosketak.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.NireErosketak"));
        jButtonNireErosketak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame a = new NireMugimnduakGUI(sellerMail);
                a.setVisible(true);
            }
        });
        
        jButtonWallet = new JButton();
        jButtonWallet.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MyMoney")); 
        jButtonWallet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame a = new WalletGUI(sellerMail);
                a.setVisible(true);
            }
        });
        

        btnBasket = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ViewBasket"));
        btnBasket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BasketGUI(sellerMail).setVisible(true);
            }
        });
        
  
        
        btnCreateEskaera = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateEskaera"));
        btnCreateEskaera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame a = new CreateEskaeraGUI(sellerMail);
                a.setVisible(true);
            }
        });

        btnViewEskaerak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ViewEskaerak"));
        btnViewEskaerak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame a = new ViewEskaerakGUI(sellerMail);
                a.setVisible(true);
            }
        });

        btnNireEskaerak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.NireEskaerak"));
        btnNireEskaerak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame a = new NireEskaerakGUI(sellerMail);
                a.setVisible(true);
            }
        });

        jContentPane = new JPanel();
        jContentPane.setLayout(new GridLayout(10, 1, 0, 0));
        
        jContentPane.add(jLabelSelectOption);
        jContentPane.add(jButtonCreateQuery);
        jContentPane.add(jButtonQueryQueries);
        jContentPane.add(jButtonNireErosketak);
        jContentPane.add(jButtonWallet);
        jContentPane.add(btnBasket);
        
        jContentPane.add(btnCreateEskaera);
        jContentPane.add(btnViewEskaerak);
        jContentPane.add(btnNireEskaerak);
        
        jContentPane.add(panel);
        
        setContentPane(jContentPane);
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") +": "+sellerMail);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
    }
    
    private void paintAgain() {
        jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
        jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
        jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
        jButtonNireErosketak.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.NireErosketak"));
        jButtonWallet.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MyMoney"));
        
        btnBasket.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ViewBasket"));
        
   
        btnCreateEskaera.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateEskaera"));
        btnViewEskaerak.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ViewEskaerak"));
        btnNireEskaerak.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.NireEskaerak"));
        
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ ": "+sellerMail);
    }
}