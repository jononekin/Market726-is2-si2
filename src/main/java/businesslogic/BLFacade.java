package businesslogic;

import java.io.File;
import java.util.Date;
import java.util.List;

import domain.Admin;
import domain.Eskaera;
import domain.Salaketa;
import domain.Sale;
import domain.Seller;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.awt.Image;


/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  

	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Sale
	 */
   @WebMethod
	public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
	
	
	/**
	 * This method retrieves the products that contain desc
	 * 
	 * @param desc the text to search
	 * @return collection of sales that contain desc 
	 */
	@WebMethod public List<Sale> getSales(String desc);
	
	/**
	 * 	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @param pubDate the date  of the publication date
	 * @return collection of sales that contain desc and published before pubDate
	 */
	@WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);

	
	/**
	 * This method calls the data access to initialize the database with some sellers and products.
	 * It is only invoked  when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
		
	@WebMethod public Image downloadImage(String imageName);
	
	@WebMethod public Seller isLogged(String log, String pass);
	
	@WebMethod public Seller isRegister(String erabiltzaile, String helbidea, String pass, String confirmPass );
	@WebMethod public boolean buyProduct(String buyerEmail, Integer saleNumber);
	@WebMethod public List<Sale> getPurchasedItems(String email);
	@WebMethod public float getMoney(String email);
	@WebMethod public boolean addMoney(String email, float zenbat);
	@WebMethod public List<Sale> getSoldItems(String email);
	@WebMethod public boolean reportSale(String userEmail, Integer saleNumber, String reason);
	@WebMethod public List<Sale> getReportedSales();
	@WebMethod public boolean resolveReport(Integer saleNumber, Salaketa sal,boolean aceptar);
	@WebMethod public boolean addErreklamazioa(Integer saleNumber, String reason, String buyerEmail);
    @WebMethod public List<Sale> getClaimedSales();
    @WebMethod public boolean resolveErreklamazioa(Integer saleNumber, boolean accept);
    @WebMethod public Admin isAdmin(String log, String pass);
    
    @WebMethod public boolean addToBasket(String email, Integer saleNumber);
    @WebMethod public boolean removeFromBasket(String email, Integer saleNumber);
    @WebMethod public List<Sale> getBasket(String email);
    @WebMethod public boolean buyBasket(String email);
    @WebMethod public boolean createEskaera(String buyerEmail, String title, String description);
    @WebMethod public List<Eskaera> getOpenEskaerak();
    @WebMethod public boolean addEskaintza(Integer eskaeraId, String sellerEmail, float price, String message);
    @WebMethod public boolean acceptEskaintza(Integer eskaeraId, Integer eskaintzaId);
    @WebMethod public boolean confirmArrival(Integer saleNumber);
}
