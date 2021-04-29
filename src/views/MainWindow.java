package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame
{
    protected JPanel contentPane;
    protected JTable table;

    protected JMenuBar menuBar = new JMenuBar()  ;
        protected JMenu fileMenu = new JMenu("File");
            protected JMenuItem openMI = new JMenuItem("Open") ;
            protected JMenuItem exitMI = new JMenuItem("Exit") ;
        protected JMenu operationsMenu = new JMenu("Operations") ;
            protected JMenuItem pullMI =new JMenuItem("Pull from Database") ;
        protected JMenu aboutMenu = new JMenu("About");
            protected JMenuItem aboutUs=new JMenuItem("About Us...");

    public MainWindow()
    {

        this.setTitle("HO App");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(7, 7, 7, 7));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(operationsMenu) ;
        menuBar.add(aboutMenu);
        fileMenu.add(openMI);
        fileMenu.addSeparator();
        fileMenu.add(exitMI);
        openMI.setEnabled(false);
        operationsMenu.add(pullMI)  ;
        aboutMenu.add(aboutUs);
        aboutUs.addActionListener(event->JOptionPane.showMessageDialog
                (MainWindow.this,
                        """
This application is made by:
- Saief Zneyti
- Rami Zouari
                                """,
                        "About Us...", JOptionPane.INFORMATION_MESSAGE));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        operationsMenu.setMnemonic(KeyEvent.VK_O);
        pullMI.setMnemonic(KeyEvent.VK_L);
        pullMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        aboutMenu.setMnemonic(KeyEvent.VK_A);
        exitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        aboutUs.setMnemonic(KeyEvent.VK_U);
        exitMI.setMnemonic(KeyEvent.VK_E);
        exitMI.addActionListener(event-> System.exit(0));
        //BAR FINISH
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


}
