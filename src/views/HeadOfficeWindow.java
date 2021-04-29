package views;

import dba.DBConnection;
import model.HO.Sale;
import model.HO.SalesTableModel;
import network.GlobalNetworkConfig;
import network.HO.NetworkConfig;
import repository.HO.SaleRepository;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class HeadOfficeWindow extends MainWindow {

    SalesTableModel model;
    private SaleRepository saleRepository;
    private NetworkConfig networkConfig;
    public HeadOfficeWindow()
    {
        model = new SalesTableModel(new ArrayList<Sale>());
        table = new JTable(model);

        this.setTitle("Head Office App");

        JScrollPane scrollpane = new JScrollPane(table) ;
        contentPane.add(scrollpane);
        //BAR START

        try
        {
            saleRepository=new repository.HO.SaleRepository();
        }
        catch(SQLException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,exc.getMessage(),"Error",JOptionPane.ERROR);
        }
        try {
            saleRepository.findAll().forEach(S -> model.addRow((Sale)S));
        }
        catch(SQLException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
        }

        try {
            networkConfig=new NetworkConfig(this,saleRepository);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        pullMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    model.clear();
                    saleRepository.findAll().forEach(S->model.addRow((Sale)S));
                }
                catch (SQLException exc)
                {
                    System.err.println(exc.getMessage());
                    JOptionPane.showMessageDialog(HeadOfficeWindow.this,"Synchronization Error","Unable to receive Synchronized Data",JOptionPane.ERROR);
                }
            }
        });
    }


    public static void main(String[] args)
    {
        DBConnection.configure(GlobalNetworkConfig.DB_HOST,"HO","HO","ho0000");
        HeadOfficeWindow app= new HeadOfficeWindow() ;

        app.pack();
        app.setVisible(true);
    }
    public void addRow(Sale sale)
    {
        model.addRow(sale);
    }

}
