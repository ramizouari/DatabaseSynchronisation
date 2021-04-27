package model.BO;

import model.AbstractSale;

public class Sale extends AbstractSale {
    public Sale(String date, String region, String product, int quantity, double cost, double tax) {
        super(0L,date, region, product, quantity, cost, tax);
    }

    public Sale(Long id, String date, String region, String product, int quantity, double cost, double tax) {
        super(id, date, region, product, quantity, cost, tax);
    }
}
