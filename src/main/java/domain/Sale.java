package domain;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Sale implements Serializable {
	
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer saleNumber;
	private String title;
	private String description;
	private int status;
	private float price;
	private Date pubDate;
	private String fileName;
	
	// Mantenemos la carga EAGER para las denuncias
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private ArrayList<Salaketa> salaketak = new ArrayList<>();
	
	@OneToOne(cascade=CascadeType.PERSIST)
    private Erreklamazioa erreklamazioa;
	
	@OneToOne(cascade=CascadeType.ALL)
    private Bidalketa bidalketa;
	
	// --- CAMBIO CLAVE: Eliminado @XmlIDREF y añadido EAGER ---
	@ManyToOne(fetch=FetchType.EAGER) 
	private Seller seller;
	
	@ManyToOne(fetch=FetchType.EAGER) 
	private Seller buyer;
	
	public Sale(){
		super();
	}
		
	public Sale(String title, String description, int status, float price, Date pubDate, File file, Seller seller) {
		super();
		this.title = title;
		this.description = description;
		this.status = status;
		this.price = price;
		this.pubDate = pubDate;
		
		if (file != null) {
		    this.fileName = file.getName();
			try {
				BufferedImage img1 = ImageIO.read(file);
				String path = "src/main/resources/images/";
				File outputfile = new File(path + file.getName());
			    ImageIO.write(img1, "png", outputfile);
			} catch(IOException ex) {
				System.out.println("Error guardando imagen: " + ex.getMessage());
			}
		}
		this.seller = seller;
	}
	
	// Getters y Setters
	public Seller getBuyer() { return buyer; }
	public void setBuyer(Seller buyer) { this.buyer = buyer; }
	
	public Integer getSaleNumber() { return saleNumber; }
	public void setSaleNumber(Integer saleNumber) { this.saleNumber = saleNumber; }
	
	public Bidalketa getBidalketa() { return bidalketa; }
    public void setBidalketa(Bidalketa bidalketa) { this.bidalketa = bidalketa; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	
	public float getPrice() { return price; }
	public void setPrice(float price) { this.price = price; }
	
	public Date getPubDate() { return pubDate; }
	public void setPubDate(Date pubDate) { this.pubDate = pubDate; }
	
	public Seller getSeller() { return seller; }
	public void setSeller(Seller seller) { this.seller = seller; }
	
	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }
	
	@Override
	public String toString(){
		return saleNumber + ";" + title + ";" + price;  
	}

	public void addSalaketa(String reason, String userEmail) {
	    salaketak.add(new Salaketa(reason, userEmail));
	}
	
	public ArrayList<Salaketa> getSalaketak() { return salaketak; }
	public void setSalaketak(ArrayList<Salaketa> salaketak) { this.salaketak = salaketak; }
	
	public void removeSalaketa(Salaketa s) { salaketak.remove(s); }
	
	public Erreklamazioa getErreklamazioa() { return erreklamazioa; }
    public void setErreklamazioa(Erreklamazioa erreklamazioa) { this.erreklamazioa = erreklamazioa; }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return saleNumber != null && saleNumber.equals(sale.saleNumber);
    }
    
    @Override
    public int hashCode() {
        return saleNumber != null ? saleNumber.hashCode() : 0;
    }
}