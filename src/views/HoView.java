package views;

import model.Sale;
import model.SalesTableModel;
import network.HO.NetworkConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;


/**
 *
 * TODO  :
 *  Create Sql operations class with different operations (getAll,insert...) + import data to View on startup
 *  Create a JSON based Serializer
 *  Create the Update View function (Both for HO and BO)
 *  We will probably create a two way queue for each BO , since we need to change both HO and BO on each synchronization
 *
 */
public class HoView extends JFrame {

    private JPanel contentPane;
    private SalesTableModel model = new SalesTableModel(new ArrayList<Sale>()) ;
    private JTable table = new JTable(model);


    private JMenuBar menuBar = new JMenuBar()  ;
    private JMenu addMenu = new JMenu ("Add") ;
    private JMenuItem addMI = new JMenuItem ("Add Sale");
    private JMenuItem openMI = new JMenuItem("Open") ;
    private JMenuItem exitMI = new JMenuItem("Exit") ;

    private JMenuItem changesMI=new JMenuItem("get Latest changes") ;
    private JMenu networkMenu = new JMenu("Network") ;
    private
    NetworkConfig networkConfig;

    public HoView()
    {
        this.setTitle("HO App");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(7, 7, 7, 7));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        try {
            networkConfig=new NetworkConfig(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //BAR START
        changesMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        networkMenu.add(changesMI)  ;

        addMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        addMenu.add(addMI) ;

        menuBar.add(addMenu) ;
        menuBar.add(networkMenu) ;

        this.setJMenuBar(menuBar);
        //BAR FINISH

        model.addRow(new Sale("01-04-2021","East","Paper",73,12.95,66.17));
        model.addRow(new Sale("01-04-2021", "West", "Paper", 33,12.95,29.91));
        model.addRow(new Sale("02-04-2021","East", "Pens", 14, 2.19,2.15));
        model.addRow(new Sale("02-04-2021", "West", "Pens", 40, 2.19, 6.13));
        model.addRow(new Sale("03-04-2021", "East", "Paper", 21, 12.95, 19.04));
        model.addRow(new Sale("03-04-2021", "West", "Paper", 10, 12.95,9.07));

        JScrollPane scrollpane = new JScrollPane(table) ;
        contentPane.add(scrollpane);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


    public static void main(String[] args)
    {
        HoView app= new HoView() ;

        app.pack();
        app.setVisible(true);
    }
    public void addRow(Sale sale)
    {
        model.addRow(sale);
    }

}
