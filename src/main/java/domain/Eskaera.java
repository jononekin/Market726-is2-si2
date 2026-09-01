package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Eskaera implements Serializable {
    @Id 
    @GeneratedValue
    private Integer id;
    private String title;
    private String description;
    private boolean isClosed; 

    @ManyToOne 
    private Seller buyer;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Eskaintza> eskaintzak;

    public Eskaera(String title, String description, Seller buyer) {
        this.title = title;
        this.description = description;
        this.buyer = buyer;
        this.isClosed = false;
        this.eskaintzak = new ArrayList<>();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean isClosed) { this.isClosed = isClosed; }
    public Seller getBuyer() { return buyer; }
    public void setBuyer(Seller buyer) { this.buyer = buyer; }
    public List<Eskaintza> getEskaintzak() { return eskaintzak; }

    public void addEskaintza(Eskaintza e) {
        this.eskaintzak.add(e);
    }

    public Eskaera() {
    }

    
    public void setEskaintzak(List<Eskaintza> eskaintzak) {
        this.eskaintzak = eskaintzak;
    }
    

    public void setId(Integer id) {
        this.id = id;
    }
}