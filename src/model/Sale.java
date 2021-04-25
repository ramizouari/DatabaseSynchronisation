package model;

import java.io.*;

public class Sale implements java.io.Serializable{

    private Long id  ;
    private String date ;
    private String region  ;
    private String product ;
    private int quantity ;
    private double cost ;
    private double tax ;



    public Sale (String date , String region,String product, int quantity,double cost, double tax)
    {
        this.date = date ;
        this.region = region ;
        this.product = product ;
        this.quantity = quantity ;
        this.cost = cost ;
        this.tax = tax ;
    }
    public Sale (Long id , String date , String region,String product, int quantity,double cost, double tax)
    {
        this(date,region,product,quantity,cost,tax) ;
        this.id = id ;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    public byte[] getBytes() throws IOException{
        byte[]bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.flush();
        oos.reset();
        bytes = baos.toByteArray();
        return bytes;
    }

    public static Sale fromBytes(byte[] body)throws IOException,ClassNotFoundException {
        Sale obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream(bis);
        obj = (Sale) ois.readObject();
        return obj;
    }

}
