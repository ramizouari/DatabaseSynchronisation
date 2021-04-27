package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame
{
    protected JPanel contentPane;
    protected JTable table;

    protected JMenuBar menuBar = new JMenuBar()  ;
    protected JMenu addMenu = new JMenu ("Add") ;
    protected JMenuItem addMI = new JMenuItem ("Add Sale");
    protected JMenuItem openMI = new JMenuItem("Open") ;
    protected JMenuItem exitMI = new JMenuItem("Exit") ;
    protected JMenuItem synchronizeMI=new JMenuItem("Synchronize") ;
    protected JMenuItem pullMI =new JMenuItem("Pull from Database") ;
    protected JMenu networkMenu = new JMenu("Network") ;

    public MainWindow()
    {

        this.setTitle("HO App");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(7, 7, 7, 7));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);


        networkMenu.add(pullMI)  ;
        networkMenu.add(synchronizeMI);

        addMenu.add(addMI) ;

        menuBar.add(addMenu) ;
        menuBar.add(networkMenu) ;

        this.setJMenuBar(menuBar);
        //BAR FINISH



        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


}
