package businesslogic;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.DataAccess;
import domain.Admin;
import domain.Eskaera;
import domain.Salaketa;
import domain.Sale;
import domain.Seller;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;


/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	 private static final int baseSize = 160;

		private static final String basePath="src/main/resources/images/";
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager=new DataAccess();		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager=da;		
	}
    

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
	public Sale createSale(String title, String description,int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		dbManager.open();
		Sale product=dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file);		
		dbManager.close();
		return product;
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Sale> getSales(String desc){
		dbManager.open();
		List<Sale>  rides=dbManager.getSales(desc);
		dbManager.close();
		return rides;
	}
	
	/**
	    * {@inheritDoc}
	    */
		@WebMethod 
		public List<Sale> getPublishedSales(String desc, Date pubDate) {
			dbManager.open();
			List<Sale>  rides=dbManager.getPublishedSales(desc,pubDate);
			dbManager.close();
			return rides;
		}
	/**
	    * {@inheritDoc}
	    */
	@WebMethod public BufferedImage getFile(String fileName) {
		return dbManager.getFile(fileName);
	}

    
	public void close() {
		DataAccess dB4oManager=new DataAccess();
		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
    /**
	 * {@inheritDoc}
	 */
    @WebMethod public Image downloadImage(String imageName) {
        File image = new File(basePath+imageName);
        try {
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @WebMethod public Seller isLogged(String log, String pass){
    	dbManager.open();
		Seller s =dbManager.isLogged(log,pass);
		dbManager.close();
		return s;
    }
    
    @WebMethod public Seller isRegister(String erabiltzaile, String helbidea, String pass, String confirmPass){
    	dbManager.open();
		Seller s =dbManager.isRegister(erabiltzaile, helbidea, pass, confirmPass);
		dbManager.close();
		return s;
    }
    @WebMethod 
    public boolean buyProduct(String buyerEmail, Integer saleNumber) {
        dbManager.open();
        boolean success = dbManager.buyProduct(buyerEmail, saleNumber);
        dbManager.close();
        return success;
    }
    @WebMethod 
	public List<Sale> getPurchasedItems(String email) {
		dbManager.open();
		List<Sale> items = dbManager.getPurchasedItems(email);
		dbManager.close();
		return items;
	}
    
    @WebMethod
    public boolean addMoney(String email, float zenbat) {
        dbManager.open();
        boolean res = dbManager.addMoney(email, zenbat);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public float getMoney(String email) {
        dbManager.open();
        float money = dbManager.getMoney(email);
        dbManager.close();
        return money;
    }
    
    @WebMethod 
   	public List<Sale> getSoldItems(String email) {
   		dbManager.open();
   		List<Sale> items = dbManager.getSoldItems(email);
   		dbManager.close();
   		return items;
   	}
    
    @WebMethod
    public boolean reportSale(String userEmail,Integer saleNumber, String reason) {
        dbManager.open();
        boolean res = dbManager.reportSale(userEmail,saleNumber,reason);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public List<Sale> getReportedSales(){
    	dbManager.open();
        List<Sale> rep = dbManager.getReportedSales();
        dbManager.close();
        return rep;
    }
    
    @WebMethod 
    public boolean resolveReport(Integer saleNumber, Salaketa sal, boolean aceptar) {
    	dbManager.open();
        boolean rep = dbManager.resolveReport(saleNumber,sal,aceptar);
        dbManager.close();
        return rep;
    }
    
    @WebMethod public boolean addErreklamazioa(Integer saleNumber, String reason, String buyerEmail) {
        dbManager.open();
        boolean res = dbManager.addErreklamazioa(saleNumber, reason, buyerEmail);
        dbManager.close();
        return res;
    }
    @WebMethod public List<Sale> getClaimedSales() {
        dbManager.open();
        List<Sale> res = dbManager.getClaimedSales();
        dbManager.close();
        return res;
    }
    @WebMethod public boolean resolveErreklamazioa(Integer saleNumber, boolean accept) {
        dbManager.open();
        boolean res = dbManager.resolveErreklamazioa(saleNumber, accept);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public Admin isAdmin(String log, String pass) {
        dbManager.open();
        Admin a = dbManager.isAdmin(log, pass);
        dbManager.close();
        return a;
    }
    
    @WebMethod
    public boolean addToBasket(String buyerEmail, Integer saleNumber) {
        dbManager.open();
        boolean a = dbManager.addToBasket(buyerEmail, saleNumber);
        dbManager.close();
        return a;
    }

    @WebMethod
    public boolean removeFromBasket(String buyerEmail, Integer saleNumber) {
        dbManager.open();
        boolean a = dbManager.removeFromBasket(buyerEmail, saleNumber);
        dbManager.close();
        return a;
    }

    @WebMethod
    public List<Sale> getBasket(String buyerEmail) {
        dbManager.open();
        List<Sale> a = dbManager.getBasket(buyerEmail);
        dbManager.close();
        return a;
    }

    @WebMethod
    public boolean buyBasket(String buyerEmail) {
        dbManager.open();
        boolean a = dbManager.buyBasket(buyerEmail);
        dbManager.close();
        return a;
    }
    @WebMethod
    public boolean createEskaera(String buyerEmail, String title, String description) {
        dbManager.open();
        boolean success = dbManager.createEskaera(buyerEmail, title, description);
        dbManager.close();
        return success;
    }

    @WebMethod
    public List<Eskaera> getOpenEskaerak() {
        dbManager.open();
        List<Eskaera> lista = dbManager.getOpenEskaerak();
        dbManager.close();
        return lista;
    }

    @WebMethod
    public boolean addEskaintza(Integer eskaeraId, String sellerEmail, float price, String message) {
        dbManager.open();
        boolean success = dbManager.addEskaintza(eskaeraId, sellerEmail, price, message);
        dbManager.close();
        return success;
    }

    @WebMethod
    public boolean acceptEskaintza(Integer eskaeraId, Integer eskaintzaId) {
        dbManager.open();
        boolean success = dbManager.acceptEskaintza(eskaeraId, eskaintzaId);
        dbManager.close();
        return success;
    }
    @WebMethod
    public boolean confirmArrival(Integer saleNumber) {
        dbManager.open();
        boolean ok = dbManager.confirmArrival(saleNumber);
        dbManager.close();
        return ok;
    }
    
}

