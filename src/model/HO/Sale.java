package model.HO;

import model.AbstractSale;

public class Sale extends AbstractSale
{
    private String office;

    public Sale(Long id,String office, String date, String region, String product, int quantity, double cost, double tax) {
        super(id, date, region, product, quantity, cost, tax);
        this.office=office;
    }
    public Sale(model.BO.Sale localSale,String office)
    {
        this(localSale.getId(),office, localSale.getDate(), localSale.getRegion(),localSale.getProduct(),
                localSale.getQuantity(), localSale.getCost(), localSale.getTax());
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Sale))
            return false;
        return super.equals(other) && ((Sale)other).office.equals(office);
    }
}
