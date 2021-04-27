package utils;

import model.AbstractSale;

import java.io.*;
import java.util.Collection;

abstract public class Serializer
{
    public static <SaleType extends AbstractSale> SaleType fromBytes(byte[] body)throws IOException,ClassNotFoundException {
        SaleType obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream(bis);
        obj = (SaleType) ois.readObject();
        return obj;
    }

    public static <C extends Collection<? extends AbstractSale>> C collectFromBytes(byte[] body)throws IOException,ClassNotFoundException {
        C obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream(bis);
        obj = (C) ois.readObject();
        return obj;
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

    public static <C extends Collection<? extends AbstractSale>> byte[] getBytesFromCollection(C collection) throws IOException
    {
        byte[]bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(collection);
        oos.flush();
        oos.reset();
        bytes = baos.toByteArray();
        return bytes;
    }
}
