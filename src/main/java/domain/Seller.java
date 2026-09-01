package domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient; 

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Seller implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@XmlID
	@Id 
	private String email;
	private String name; 
	private String pass;
	
	@XmlTransient 
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Sale> sales = new ArrayList<Sale>();
	
	@XmlTransient 
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Sale> purchasedSales = new ArrayList<Sale>();
	
	@XmlTransient 
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Sale> basket = new ArrayList<>();

	// --- AQUI ESTABA EL ERROR: Faltaba el @XmlTransient para evitar el bucle ---
	@XmlTransient
	@OneToMany(mappedBy="seller", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Mugimenduak> movements = new ArrayList<>();

	private float money = 0;

	public Seller() {
		super();
	}
	
	public Seller(String email, String name, String pass) {
		this.email = email;
		this.name = name;
		this.pass = pass;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return email+";"+name;
	}
	
	/**
	 * This method creates/adds a sale to a seller
	 */
	public Sale addSale(String title, String description, int status, float price,  Date pubDate, File file)  {
		Sale sale=new Sale(title, description, status, price,  pubDate, file, this);
        sales.add(sale);
        return sale;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 */
	public boolean doesSaleExist(String title)  {	
		for (Sale s:sales)
			if ( s.getTitle().compareTo(title)==0 )
			 return true;
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		if (email != other.email)
			return false;
		return true;
	}

	public List<Sale> getPurchasedSales() {
	    return purchasedSales;
	}

	public void setPurchasedSales(List<Sale> purchasedSales) {
	    this.purchasedSales = purchasedSales;
	}

	public void addPurchasedSale(Sale sale) {
	    this.purchasedSales.add(sale);
	}

	public void removeSale(Sale sale) {
	    this.sales.remove(sale);
	}

	public float getMoney() {
	    return money;
	}

	public void setMoney(float money) {
	    this.money = money;
	}

	public void addMoney(float money) {
	    this.money += money;
	}

	public boolean subtractMoney(float money) {
	    if (this.money >= money) {
	        this.money -= money;
	        return true;
	    }
	    return false;
	}
	
	public List<Sale> getSales() {
	    return sales;
	}
	
	public List<Sale> getBasket() {
	    return basket;
	}

	public void setBasket(List<Sale> basket) {
	    this.basket = basket;
	}
	
	public boolean addToBasket(Sale sale) {
	    if (!basket.contains(sale)) {
	        basket.add(sale);
	        return true;
	    }
	    return false;
	}

	public void removeFromBasket(Sale sale) {
	    basket.remove(sale);
	}

	public void clearBasket() {
	    basket.clear();
	}

    public List<Mugimenduak> getMovements() {
        return movements;
    }

    public void setMovements(List<Mugimenduak> movements) {
        this.movements = movements;
    }

    public void addMovement(Mugimenduak m) {
        this.movements.add(m);
        m.setSeller(this); 
    }
}