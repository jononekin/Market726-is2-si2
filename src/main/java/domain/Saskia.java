package domain;

import java.util.ArrayList;
import java.util.List;

public class Saskia {

    private Seller seller; //Saltzaile bakarra
    private List<Sale> products = new ArrayList<>();

    public Saskia(Seller seller) {
        this.seller = seller;
    }

    public Saskia() {
    }
    public void setSeller(Seller seller) {
        this.seller = seller;
    }


    public void setProducts(List<Sale> products) {
        this.products = products;
    }

    public Seller getSeller() {
        return seller;
    }

    public List<Sale> getProducts() {
        return products;
    }

    public void addProduct(Sale sale) {
        products.add(sale);
    }

    public void removeProduct(Sale sale) {
        products.remove(sale);
    }

    public float getTotalPrice() {
        float total = 0;
        for (Sale s : products) {
            total += s.getPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void clear() {
        products.clear();
    }
}