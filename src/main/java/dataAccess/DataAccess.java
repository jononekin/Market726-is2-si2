package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Seller;
import domain.Admin;
import domain.Bidalketa;
import domain.Erreklamazioa;
import domain.Eskaera;
import domain.Eskaintza;
import domain.Mugimenduak;
import domain.Salaketa;
import domain.Sale;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
    private EntityManager db;
    private EntityManagerFactory emf;
    private static final int baseSize = 160;

    private static final String basePath="src/main/resources/images/";

    ConfigXML c=ConfigXML.getInstance();

    public DataAccess() {
        if (c.isDatabaseInitialized()) {
            String fileName=c.getDbFilename();

            File fileToDelete= new File(fileName);
            if(fileToDelete.delete()){
                File fileToDeleteTemp= new File(fileName+"$");
                fileToDeleteTemp.delete();
                System.out.println("File deleted");
             } else {
                 System.out.println("Operation failed");
                }
        }
        open();
        if (c.isDatabaseInitialized()) 
            initializeDB();
        System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

        close();
    }
     
    public DataAccess(EntityManager db) {
        this.db=db;
    }
    
    /**
     * This method initializes the database with some products and sellers.
     * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
     */ 
    public void initializeDB(){
        db.getTransaction().begin();
        try {        
            //Create sellers 
            Seller seller1=new Seller("seller1@gmail.com","Aitor Fernandez","aurrera");
            Seller seller2=new Seller("seller22@gmail.com","Ane Gaztañaga","aurrera");
            Seller seller3=new Seller("seller3@gmail.com","Test Seller","aurrera");

            Admin admin = new Admin("admin@gmail.com","Admin","admin123");
            db.persist(admin);
            
            //Create products
            Date today = UtilDate.trim(new Date());
        
            seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 2, 10, today, null);
            seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi",2,  20, today, null);
            seller1.addSale("samsung 42\" telebista", "berria, erabili gabe", 1, 175, today, null);

            seller2.addSale("imac 27", "7 urte, dena ondo dabil", 1, 200,today, null);
            seller2.addSale("iphone 17", "oso gutxi erabilita", 2, 400, today, null);
            seller2.addSale("orbea mendiko bizikleta", "29\" 10 urte, mantenua behar du", 3,225, today, null);
            seller2.addSale("polar kilor erlojua", "Vantage M, ondo dago", 3, 30, today, null);

            seller3.addSale("sukaldeko mahaia", "1.8*0.8, 4 aulkiekin. Prezio finkoa", 3,45, today, null);

            db.persist(seller1);
            db.persist(seller2);
            db.persist(seller3);
    
            db.getTransaction().commit();
            System.out.println("Db initialized");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
        System.out.println(">> DataAccess: createProduct=> title= "+title+" seller="+sellerEmail);
        try {
            if(pubDate.before(UtilDate.trim(new Date()))) {
                throw new MustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
            }
            if (file==null)
                throw new FileNotUploadedException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));

            db.getTransaction().begin();
            
            Seller seller = db.find(Seller.class, sellerEmail);
            if (seller.doesSaleExist(title)) {
                db.getTransaction().commit();
                throw new SaleAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
            }

            Sale sale = seller.addSale(title, description, status, price, pubDate, file);
            
            db.persist(seller); 
            db.getTransaction().commit();
            System.out.println("sale stored "+sale+ " "+seller);

            System.out.println("hasta aqui");
            return sale;
        } catch (NullPointerException e) {
            e.printStackTrace();
            db.getTransaction().commit();
            return null;
        }
    }
    
    public List<Sale> getSales(String desc) {
        System.out.println(">> DataAccess: getProducts=> from= "+desc);
        List<Sale> res = new ArrayList<Sale>(); 
        TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.buyer IS NULL", Sale.class);
        query.setParameter(1, "%"+desc+"%");
        
        List<Sale> sales = query.getResultList();
        for (Sale sale:sales){
            res.add(sale);
        }
        return res;
    }
    
    public List<Sale> getPublishedSales(String desc, Date pubDate) {
        System.out.println(">> DataAccess: getProducts=> from= "+desc);
        List<Sale> res = new ArrayList<Sale>(); 
        TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.pubDate <=?2 AND s.buyer IS NULL",Sale.class);
        query.setParameter(1, "%"+desc+"%");
        query.setParameter(2,pubDate);
        
        List<Sale> sales = query.getResultList();
        for (Sale sale:sales){
            res.add(sale);
        }
        return res;
    }

    public void open(){
        String fileName=c.getDbFilename();
        if (c.isDatabaseLocal()) {
            emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
            db = emf.createEntityManager();
        } else {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", c.getUser());
            properties.put("javax.persistence.jdbc.password", c.getPassword());

            emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
            db = emf.createEntityManager();
        }
        System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());
    }

    public BufferedImage getFile(String fileName) {
        File file=new File(basePath+fileName);
        BufferedImage targetImg=null;
        try {
             targetImg = rescale(ImageIO.read(file));
        } catch (IOException ex) {
            //Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return targetImg;
    }
    
    public BufferedImage rescale(BufferedImage originalImage) {
        System.out.println("rescale "+originalImage);
        BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
        g.dispose();
        return resizedImage;
    }
    
    @WebMethod public Seller isLogged(String log, String pass) {
         TypedQuery<Seller> query = db.createQuery("SELECT s FROM Seller s WHERE s.name=?1 AND s.pass=?2",Seller.class);   
         query.setParameter(1, log);
         query.setParameter(2, pass);
         if(!query.getResultList().isEmpty()) //User Existitzen bada
             return query.getResultList().get(0);
         else return null;
     }
    
    @WebMethod public Seller isRegister(String erabiltzaile, String helbidea, String pass, String confirmPass) {
         if(!pass.equals(confirmPass)) {
             return null;
         }else {
             TypedQuery<Seller> query = db.createQuery("SELECT s FROM Seller s WHERE s.email=?1 ",Seller.class);
             query.setParameter(1, helbidea);
            
             if(!query.getResultList().isEmpty()) {//Erabiltzailea existitzen bada
                 return null;
             }else {
                 db.getTransaction().begin();
                 Seller s = new Seller(helbidea, erabiltzaile, pass);
                 db.persist(s);
                 db.getTransaction().commit();
                 return s;
             }
         }
     }

    public boolean buyProduct(String buyerEmail, Integer saleNumber) {
        try {
            db.getTransaction().begin();
            Seller buyer = db.find(Seller.class, buyerEmail);
            Sale sale = db.find(Sale.class, saleNumber);
            Seller seller = sale.getSeller();
            
            if (buyer == null || sale == null || sale.getBuyer() != null) {
                db.getTransaction().rollback();
                return false; 
            }
            
            float price = sale.getPrice();
            if (buyer.getMoney() < price) {
                db.getTransaction().rollback();
                return false; 
            }
            
            buyer.addMoney(-price);
            sale.setBuyer(buyer); 
            buyer.addPurchasedSale(sale); 
            
            Bidalketa bidalketa = new Bidalketa(sale);
            sale.setBidalketa(bidalketa);
            
            db.persist(bidalketa);

            // --- NUEVO BLOQUE MUGIMENDUAK ---
            Mugimenduak mugimendu = new Mugimenduak("EROSKETA", new java.util.Date(), buyer);
            mugimendu.setSale(sale);
            db.persist(mugimendu);
            // --------------------------------

            db.merge(sale);
            db.merge(buyer);
            
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }
    
    public List<Sale> getPurchasedItems(String email) {
        System.out.println(">> DataAccess: getPurchasedItems=> user= " + email);
        db.getTransaction().begin();
        Seller user = db.find(Seller.class, email);
        
        List<Sale> compras = new ArrayList<Sale>();
        if (user != null && user.getPurchasedSales() != null) {
            for (Sale s : user.getPurchasedSales()) {
                compras.add(s);
            }
        }
        db.getTransaction().commit();
        return compras;
    }
    
    public boolean addMoney(String email, float zenbat) {
        try {
            db.getTransaction().begin();
            Seller s = db.find(Seller.class, email);
            if (s == null || zenbat <= 0) {
                db.getTransaction().rollback();
                return false;
            }

            s.addMoney(zenbat);

            // --- NUEVO BLOQUE MUGIMENDUAK (CORREGIDO) ---
            Mugimenduak mugimendu = new Mugimenduak("DIRU_SARRERA", new java.util.Date(), s);
            db.persist(mugimendu);
            // --------------------------------------------

            db.merge(s);
            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }
    
    public float getMoney(String email) {
        Seller s = db.find(Seller.class, email);
        if (s != null) {
            return s.getMoney();
        }
        return 0;
    }
    
    public List<Sale> getSoldItems(String email) {
        Seller s = db.find(Seller.class, email);
        List<Sale> sold = new ArrayList<>();
        for (Sale sale : s.getSales()) {
            if (sale.getBuyer() != null) {
                sold.add(sale);
            }
        }
        return sold;
    }
    
    public boolean reportSale(String userEmail,Integer saleNumber, String reason) {
        try {
            db.getTransaction().begin();
            Sale sale = db.find(Sale.class, saleNumber);

            if (sale == null || reason == null || reason.isEmpty()) {
                db.getTransaction().rollback();
                return false;
            }

            sale.addSalaketa(reason,userEmail);
            db.merge(sale);

            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }
    
    @WebMethod
    public List<Sale> getReportedSales() {
        try {
            TypedQuery<Sale> query = db.createQuery(
                "SELECT s FROM Sale s WHERE s.salaketak IS NOT EMPTY",
                Sale.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Sale>();
        }
    }
    
    public boolean resolveReport(Integer saleNumber, Salaketa salaketa,boolean aceptar) {
        try {
            db.getTransaction().begin();
            Sale sale = db.find(Sale.class, saleNumber);

            if (sale == null || salaketa == null) {
                db.getTransaction().rollback();
                return false;
            }
            
            sale.getSalaketak().removeIf(s -> s.getId() == salaketa.getId());

            Salaketa s = db.find(Salaketa.class, salaketa.getId());
            if (s != null) {
                s.setTratatuta(true);
                db.merge(s);
            }
           
            if (aceptar) {
                Seller owner = sale.getSeller();
                if (owner != null) {
                    owner.removeSale(sale);
                    db.merge(owner);
                }
                db.remove(sale);
            } else {
                db.merge(sale);
            }

            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    public boolean addErreklamazioa(Integer saleNumber, String reason, String buyerEmail) {
        try {
            db.getTransaction().begin();
            Sale sale = db.find(Sale.class, saleNumber);
            if (sale == null || sale.getErreklamazioa() != null) {
                db.getTransaction().rollback();
                return false;
            }
            Erreklamazioa err = new Erreklamazioa(reason, buyerEmail);
            sale.setErreklamazioa(err);
            db.merge(sale);
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    @WebMethod
    public List<Sale> getClaimedSales() {
        try {
            TypedQuery<Sale> query = db.createQuery(
                "SELECT s FROM Sale s WHERE s.erreklamazioa IS NOT NULL", Sale.class);
            return query.getResultList();
        } catch (Exception e) { return new ArrayList<Sale>(); }
    }

    public boolean resolveErreklamazioa(Integer saleNumber, boolean accept) {
        try {
            db.getTransaction().begin();
            Sale sale = db.find(Sale.class, saleNumber);
            if (sale == null || sale.getErreklamazioa() == null) {
                db.getTransaction().rollback(); return false;
            }

            if (accept) {
                Seller buyer = sale.getBuyer();
                Seller seller = sale.getSeller();

                if (buyer != null && seller != null) {
                    buyer.addMoney(sale.getPrice()); 
                    seller.addMoney(-sale.getPrice()); 

                   
                    Mugimenduak mBuyer = new Mugimenduak("ITZULKETA", new java.util.Date(), buyer);
                    mBuyer.setSale(sale);
                    db.persist(mBuyer);
                    
                    Mugimenduak mSeller = new Mugimenduak("ITZULKETA_KENTZEA", new java.util.Date(), seller);
                    mSeller.setSale(sale);
                    db.persist(mSeller);
                    
                 
                    buyer.getPurchasedSales().remove(sale);
                    sale.setBuyer(null); 

                    db.merge(buyer);
                    db.merge(seller);
                }
            }
            
            Erreklamazioa err = sale.getErreklamazioa();
            sale.setErreklamazioa(null);
            db.merge(sale);
            
            if (err != null) {
                Erreklamazioa dbErr = db.find(Erreklamazioa.class, err.getId());
                if (dbErr != null) err.setTratatuta(true);
                db.merge(err);
            }

            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }
    
    public Admin isAdmin(String log, String pass) {
        TypedQuery<Admin> query = db.createQuery(
            "SELECT a FROM Admin a WHERE a.name=?1 AND a.pass=?2",
            Admin.class
        );
        query.setParameter(1, log);
        query.setParameter(2, pass);

        if (!query.getResultList().isEmpty())
            return query.getResultList().get(0);
        else
            return null;
    }
    
    public boolean addToBasket(String buyerEmail, Integer saleNumber) {
        try {
            db.getTransaction().begin();
            Seller buyer = db.find(Seller.class, buyerEmail);
            Sale sale = db.find(Sale.class, saleNumber);

            if (buyer == null || sale == null || sale.getBuyer() != null) {
                db.getTransaction().rollback();
                return false;
            }
            
            List<Sale> basket = buyer.getBasket();
            
            for (Sale s : basket) {
                if (s.getSaleNumber().equals(saleNumber)) {
                    db.getTransaction().rollback();
                    return false; 
                }
            }
            
            if (basket.isEmpty()) {
                basket.add(sale);
                db.merge(buyer);
                db.getTransaction().commit();
                return true;
            }
            
            Seller firstSeller = buyer.getBasket().get(0).getSeller();

            if (!sale.getSeller().equals(firstSeller)) {
                return false; 
            } 

            buyer.addToBasket(sale);
            db.merge(buyer);

            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }
    
    public boolean removeFromBasket(String email, Integer saleNumber) {
        try {
            db.getTransaction().begin();
            Seller user = db.find(Seller.class, email);
            Sale sale = db.find(Sale.class, saleNumber);

            if (user == null || sale == null) {
                db.getTransaction().rollback();
                return false;
            }

            user.getBasket().remove(sale);

            db.merge(user);
            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            db.getTransaction().rollback();
            return false;
        }
    }
    
    public List<Sale> getBasket(String email) {
        Seller user = db.find(Seller.class, email);
        if (user == null) return new ArrayList<>();
        return new ArrayList<>(user.getBasket());
    }
    
    
    public boolean buyBasket(String buyerEmail) {
        try {
            db.getTransaction().begin();
            Seller buyer = db.find(Seller.class, buyerEmail);
            if (buyer == null) {
                db.getTransaction().rollback();
                return false;
            }

            List<Sale> basket = buyer.getBasket();

            if (basket == null || basket.isEmpty()) {
                db.getTransaction().rollback();
                return false;
            }

            Seller seller = basket.get(0).getSeller(); 

            float total = 0;

          
            for (Sale s : basket) {
                if (!s.getSeller().equals(seller)) {
                    db.getTransaction().rollback();
                    return false;
                }
                total += s.getPrice();
            }

           
            if (buyer.getMoney() < total) {
                db.getTransaction().rollback();
                return false;
            }

          
            buyer.addMoney(-total);
       
            for (Sale s : basket) {
                s.setBuyer(buyer);
                buyer.addPurchasedSale(s);
                
                Bidalketa bidalketa = new Bidalketa(s);
                s.setBidalketa(bidalketa);
                db.persist(bidalketa);

               
                Mugimenduak mugimendu = new Mugimenduak("EROSKETA", new java.util.Date(), buyer);
                mugimendu.setSale(s);
                db.persist(mugimendu);
                
                db.merge(s);
            }

            basket.clear();

            db.merge(buyer);
            db.merge(seller);

            db.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            return false;
        }
    }

    public boolean createEskaera(String buyerEmail, String title, String description) {
        try {
            db.getTransaction().begin();
            Seller buyer = db.find(Seller.class, buyerEmail);
            if (buyer == null) {
                db.getTransaction().rollback();
                return false;
            }
            
            Eskaera eskaera = new Eskaera(title, description, buyer);
            db.persist(eskaera);
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    public List<Eskaera> getOpenEskaerak() {
        TypedQuery<Eskaera> query = db.createQuery("SELECT e FROM Eskaera e WHERE e.isClosed = false", Eskaera.class);
        return query.getResultList();
    }

    public boolean addEskaintza(Integer eskaeraId, String sellerEmail, float price, String message) {
        try {
            db.getTransaction().begin();
            
            Eskaera eskaera = db.find(Eskaera.class, eskaeraId);
            Seller seller = db.find(Seller.class, sellerEmail);
            
            if (eskaera == null || seller == null || eskaera.isClosed() || eskaera.getBuyer().getEmail().equals(sellerEmail)) {
                db.getTransaction().rollback();
                return false;
            }

            Eskaintza eskaintza = new Eskaintza(price, message, seller, eskaera);
            eskaera.addEskaintza(eskaintza);
            
            db.persist(eskaintza);
            db.merge(eskaera);
            
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    public boolean acceptEskaintza(Integer eskaeraId, Integer eskaintzaId) {
        try {
            db.getTransaction().begin();
            
            Eskaera eskaera = db.find(Eskaera.class, eskaeraId);
            Eskaintza eskaintza = db.find(Eskaintza.class, eskaintzaId);
            
            if (eskaera == null || eskaintza == null || eskaera.isClosed()) {
                db.getTransaction().rollback();
                return false;
            }

            Seller buyer = eskaera.getBuyer();
            Seller seller = eskaintza.getSeller();
            float price = eskaintza.getPrice();

            if (buyer.getMoney() < price) {
                db.getTransaction().rollback();
                return false; 
            }

            buyer.addMoney(-price);
       
            eskaera.setClosed(true);

            String tituloSale = "[Eskaera] " + eskaera.getTitle();
            String descSale = eskaintza.getMessage();
            
            Sale transaccion = seller.addSale(tituloSale, descSale, 1, price, new java.util.Date(), null);
            transaccion.setBuyer(buyer);
            buyer.addPurchasedSale(transaccion);
            
            Bidalketa bidalketa = new Bidalketa(transaccion);
            transaccion.setBidalketa(bidalketa);
            
            db.persist(bidalketa);
            db.persist(transaccion);

         
            Mugimenduak mugimendu = new Mugimenduak("ESKAINTZA_ORDAINKETA", new java.util.Date(), buyer);
            mugimendu.setEskaera(eskaera);
            mugimendu.setEskaintza(eskaintza);
            db.persist(mugimendu);
            // --------------------------------------------

            db.merge(buyer);
            db.merge(seller);
            db.merge(eskaera);
            
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    public boolean confirmArrival(Integer saleNumber) {
        try {
            db.getTransaction().begin();
            Sale sale = db.find(Sale.class, saleNumber);
            
            if (sale == null || sale.getBidalketa() == null || sale.getBidalketa().getEgoera().equals("JASOTA")) {
                db.getTransaction().rollback();
                return false;
            }

            Bidalketa b = sale.getBidalketa();
            b.setEgoera("JASOTA");

            Seller seller = sale.getSeller();
            seller.addMoney(sale.getPrice());

           
            Mugimenduak mugimendu = new Mugimenduak("KOBRANTZA", new java.util.Date(), seller);
            mugimendu.setSale(sale);
            db.persist(mugimendu);
            

            db.merge(b);
            db.merge(seller);
            
            db.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.getTransaction().rollback();
            return false;
        }
    }

    public void close(){
        db.close();
        System.out.println("DataAcess closed");
    }
}