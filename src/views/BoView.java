package views;

import dba.DBConnection;
import model.BO.Sale;
import model.BO.SalesTableModel;
import network.BO.NetworkConfig;
import network.GlobalNetworkConfig;
import repository.BO.SaleRepository;
import utils.Serializer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class BoView extends MainWindow{


    SalesTableModel model;
    private NetworkConfig networkConfig ;
    private SaleRepository saleRepository;


    public BoView()
    {
        synchronizeMI.setText("Synchronise with Head Office");
        model = new SalesTableModel(new ArrayList<Sale>());
        table = new JTable(model)
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // network configuration
        // end network configuration

        this.setTitle("Branch Office 1 App");

        JScrollPane scrollpane = new JScrollPane(table) ;
        contentPane.add(scrollpane);


        //navbar configuration
        //end navbar configuration

        model.addRow(new Sale("01-04-2021","East","Paper",73,12.95,66.17));
        model.addRow(new Sale("01-04-2021", "West", "Paper", 33,12.95,29.91));
        model.addRow(new Sale("02-04-2021","East", "Pens", 14, 2.19,2.15));
        model.addRow(new Sale("02-04-2021", "West", "Pens", 40, 2.19, 6.13));
        model.addRow(new Sale("03-04-2021", "East", "Paper", 21, 12.95, 19.04));
        model.addRow(new Sale("03-04-2021", "West", "Paper", 10, 12.95,9.07));

        try
        {
            saleRepository=new SaleRepository("BO1");
            this.networkConfig = new NetworkConfig(GlobalNetworkConfig.UPWARD_QUEUE_NAME,DBConnection.getInstance());
            networkConfig.initConnection();

        }
        catch(SQLException|IOException|TimeoutException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,exc.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            saleRepository.findAll().forEach(S -> model.addRow((Sale)S));
        }
        catch(SQLException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
        }

        addMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        pullMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                try {
                    saleRepository.findAll().forEach(S->model.addRow((Sale)S));
                } catch (SQLException sqlExc) {
                    System.err.println(sqlExc.getMessage());
                    JOptionPane.showMessageDialog(BoView.this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        synchronizeMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

                try {
                    networkConfig.publishMessage
                            (Serializer
                                    .getBytesFromCollection(saleRepository.findAll().stream().collect(Collectors.toList())));
                } catch (IOException ioException)
                {
                    System.err.println(ioException.getMessage());
                    JOptionPane.showMessageDialog(BoView.this,"Unable to send data for Synchronization","Synchronization Error",JOptionPane.ERROR_MESSAGE);
                } catch (SQLException exc) {
                    System.err.println(exc.getMessage());
                    JOptionPane.showMessageDialog(BoView.this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }


    public static void main(String[] args)
    {
        if (args.length==0)
        {
            JOptionPane.showMessageDialog(null,
    """
No Branch Office is configured
For now, Branch configuration is not supported yet: defaulting to BO1
            """
                    ,"No Configured Branch Office", JOptionPane.WARNING_MESSAGE);
        }
        DBConnection.configure(GlobalNetworkConfig.DB_HOST, "BO1", "BO1", "bo10000");
        BoView app = new BoView();
        app.pack();
        app.setVisible(true);
    }
}
