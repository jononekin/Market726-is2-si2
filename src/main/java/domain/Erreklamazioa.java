package domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Erreklamazioa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String reason;
    private String buyerEmail;
    private boolean tratatuta = false;

    public Erreklamazioa() {}

    public Erreklamazioa(String reason, String buyerEmail) {
        this.reason = reason;
        this.buyerEmail = buyerEmail;
    }

    public Integer getId() { return id; }
    public String getReason() { return reason; }
    public String getBuyerEmail() { return buyerEmail; }
    
    public boolean isTratatuta() {
        return tratatuta;
    }

    public void setTratatuta(boolean tratatuta) {
        this.tratatuta = tratatuta;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}