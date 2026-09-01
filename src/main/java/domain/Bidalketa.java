package domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Bidalketa implements Serializable {
    
    @Id 
    @GeneratedValue
    private Integer id;
    
    private String trackingNumber;
    private String egoera; 
  
    @XmlTransient 
    private Sale sale;

    
    public Bidalketa() {
        super();
    }

    public Bidalketa(Sale sale) {
        this.sale = sale;
        this.egoera = "PRESTATZEN"; 
        this.trackingNumber = "Zehaztu gabe"; 
       
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getEgoera() { return egoera; }
    public void setEgoera(String egoera) { this.egoera = egoera; }
 

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }
}