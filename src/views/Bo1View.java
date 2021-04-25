package views;

import dba.DBConnection;
import model.Sale;
import model.SalesTableModel;
import network.BO.NetworkConfig;
import repository.SaleRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Bo1View extends JFrame{
    private JPanel contentPane;
    private SalesTableModel model = new SalesTableModel(new ArrayList<Sale>()) ;
    private JTable table = new JTable(model)
    {
        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    };

    private JMenuBar menuBar = new JMenuBar()  ;
    private JMenu addMenu = new JMenu ("Add") ;
    private JMenuItem addMI = new JMenuItem ("Add Sale");
    private JMenuItem openMI = new JMenuItem("Open") ;
    private JMenuItem exitMI = new JMenuItem("Exit") ;
    private JMenuItem synchronizeMI=new JMenuItem("Synchronize") ;
    private JMenuItem pullMI=new JMenuItem("Pull from Database") ;
    private JMenu networkMenu = new JMenu("Network") ;

    private NetworkConfig networkConfig ;
    private SaleRepository saleRepository;


    public Bo1View()
    {

        // network configuration
        try
        {
            saleRepository=new SaleRepository();
            this.networkConfig = new NetworkConfig("queue-bo1",DBConnection.getInstance());
            networkConfig.initConnection();

        }
        catch(SQLException|IOException|TimeoutException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,exc.getMessage(),"Error",JOptionPane.ERROR);
        }
        // end network configuration

        this.setTitle("Branch Office 1 App");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(7, 7, 7, 7));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        //navbar configuration
        synchronizeMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

                try {
                    networkConfig.publishMessage(saleRepository.findAll().stream().findAny().get().getBytes());
                } catch (IOException ioException)
                {
                    System.err.println(ioException.getMessage());
                    JOptionPane.showMessageDialog(Bo1View.this,"Synchronization Error","Unable to send data for Synchronization",JOptionPane.ERROR);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        networkMenu.add(synchronizeMI)  ;
        networkMenu.add(pullMI);

        addMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        pullMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                try {
                    saleRepository.findAll().forEach(S->model.addRow(S));
                } catch (SQLException sqlExc) {
                    System.err.println(sqlExc.getMessage());
                    JOptionPane.showMessageDialog(Bo1View.this,"Unable to connect to Database","Connection Error",JDialog.ERROR);
                }
            }
        });
        addMenu.add(addMI) ;

        menuBar.add(addMenu) ;
        menuBar.add(networkMenu) ;

        this.setJMenuBar(menuBar);
        //end navbar configuration

        model.addRow(new Sale("01-04-2021","East","Paper",73,12.95,66.17));
        model.addRow(new Sale("01-04-2021", "West", "Paper", 33,12.95,29.91));
        model.addRow(new Sale("02-04-2021","East", "Pens", 14, 2.19,2.15));
        model.addRow(new Sale("02-04-2021", "West", "Pens", 40, 2.19, 6.13));
        model.addRow(new Sale("03-04-2021", "East", "Paper", 21, 12.95, 19.04));
        model.addRow(new Sale("03-04-2021", "West", "Paper", 10, 12.95,9.07));
        try {
            saleRepository.findAll().forEach(S -> model.addRow(S));
        }
        catch(SQLException exc)
        {
            System.err.println(exc.getMessage());
            JOptionPane.showMessageDialog(this,"Unable to fetch data from server","Connection Error",JOptionPane.ERROR);
        }

        JScrollPane scrollpane = new JScrollPane(table) ;
        contentPane.add(scrollpane);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


    public static void main(String[] args)
    {
        DBConnection.configure("51.144.40.231:3306", "BO1", "BO1", "bo10000");
        Bo1View app = new Bo1View();

        app.pack();
        app.setVisible(true);
    }
}
