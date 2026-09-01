package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlID;

@Entity
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlID
    @Id
    private String email;
    private String name;
    private String pass;

    public Admin() {}

    public Admin(String email, String name, String pass) {
        this.email = email;
        this.name = name;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {
        return name;
    }
}