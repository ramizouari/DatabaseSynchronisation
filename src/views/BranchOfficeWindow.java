package views;

import dba.DBConnection;
import model.AbstractSale;
import model.BO.Sale;
import model.BO.SalesTableModel;
import network.BO.NetworkConfig;
import network.GlobalNetworkConfig;
import repository.BO.SaleRepository;
import utils.Serializer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.awt.event.ActionEvent.ACTION_PERFORMED;

public class BranchOfficeWindow extends MainWindow{


    SalesTableModel model;
    protected JMenu editMenu = new JMenu ("Edit") ;
        protected JMenuItem addMI = new JMenuItem ("Add Sale");
        protected JMenuItem removeMI= new JMenuItem("Remove Sale");
    protected JMenuItem synchronizeMI=new JMenuItem("Synchronize with Head Office") ;
    protected JMenuItem pushMI = new JMenuItem("Push to Database");
    private NetworkConfig networkConfig ;
    private SaleRepository saleRepository;

    public BranchOfficeWindow(String officeName)
    {
        synchronizeMI.setText("Synchronise with Head Office");
        model = new SalesTableModel(new ArrayList<Sale>());
        table = new JTable(model);

        // network configuration
        // end network configuration
        menuBar.add(editMenu,1) ;
        editMenu.add(addMI) ;
        editMenu.add(removeMI);
        operationsMenu.add(pushMI);
        operationsMenu.add(synchronizeMI);
        editMenu.setMnemonic(KeyEvent.VK_E);
        addMI.setMnemonic(KeyEvent.VK_A);
        addMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        removeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        removeMI.setMnemonic(KeyEvent.VK_R);
        pushMI.setMnemonic(KeyEvent.VK_S);
        synchronizeMI.setMnemonic(KeyEvent.VK_Y);
        pushMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        synchronizeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.ALT_MASK));

        this.setTitle("Branch Office %c App".formatted(officeName.charAt(officeName.length()-1)));

        JScrollPane scrollpane = new JScrollPane(table) ;
        contentPane.add(scrollpane);


        //navbar configuration
        //end navbar configuration

        try
        {
            saleRepository=new SaleRepository(officeName);
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

        addMI.addActionListener(listener->model.addEmptyRow());

        removeMI.addActionListener
            (
                listener->
                {
                    Integer row=table.getSelectedRow();
                    model.remove(row);
                }
            );
        pushMI.addActionListener
                (
                        listener->
                        {
                            try
                            {
                                for(Long id:model.getAllDeletedIds())
                                    saleRepository.removeById(id);
                                for(AbstractSale S : model.getAllValues())
                                    saleRepository.save((Sale)S);
                                for(ActionListener L:pullMI.getActionListeners())
                                    L.actionPerformed(new ActionEvent(pushMI,ACTION_PERFORMED,"Pull New Values"));
                            }
                            catch(SQLException exc)
                            {
                                System.err.println(exc.getMessage());
                                JOptionPane.showMessageDialog(BranchOfficeWindow.this,"Unable to push data to Database",
                                        "Write Error",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                );


        pullMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                try {
                    saleRepository.findAll().forEach(S->model.addRow((Sale)S));
                } catch (SQLException sqlExc) {
                    System.err.println(sqlExc.getMessage());
                    JOptionPane.showMessageDialog(BranchOfficeWindow.this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(BranchOfficeWindow.this,"Unable to send data for Synchronization","Synchronization Error",JOptionPane.ERROR_MESSAGE);
                } catch (SQLException exc) {
                    System.err.println(exc.getMessage());
                    JOptionPane.showMessageDialog(BranchOfficeWindow.this,"Unable to read data from Database","Database Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }


    public static void main(String[] args)
    {
        String officeName;
        String connectionPassword;
        if (args.length==0)
        {
            JOptionPane.showMessageDialog(null,
    """
No Branch Office is configured
For now, Branch configuration is not supported yet: defaulting to BO1
            """
                    ,"No Configured Branch Office", JOptionPane.WARNING_MESSAGE);
           officeName="BO1";
        }
        else officeName= args[0].toUpperCase();
        /*
        * ALL Branch Offices' passwords are generated with this rule
        * */
        connectionPassword=officeName.toLowerCase()+"0000";
        DBConnection.configure(GlobalNetworkConfig.DB_HOST, officeName, officeName, connectionPassword);
        BranchOfficeWindow app = new BranchOfficeWindow(officeName);
        app.pack();
        app.setVisible(true);
    }
}
