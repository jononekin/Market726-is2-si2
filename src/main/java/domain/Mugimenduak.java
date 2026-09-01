package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Mugimenduak implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    private String mota; 
    private Date data;
    
    @ManyToOne
    private Seller seller;
    
    @ManyToOne
    private Sale sale;
    
    @ManyToOne
    private Eskaera eskaera;
    
    @ManyToOne
    private Eskaintza eskaintza;

 
    public Mugimenduak() {
        super();
    }

  
    public Mugimenduak(String mota, Date data, Seller seller) {
        this.mota = mota;
        this.data = data;
        this.seller = seller;
    }

   
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public Eskaera getEskaera() { return eskaera; }
    public void setEskaera(Eskaera eskaera) { this.eskaera = eskaera; }

    public Eskaintza getEskaintza() { return eskaintza; }
    public void setEskaintza(Eskaintza eskaintza) { this.eskaintza = eskaintza; }
}