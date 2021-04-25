package model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SalesTableModel extends AbstractTableModel {

    ArrayList <Sale> sales ;

    final String[] columnNames={"Product ID","Date", "Region", "Product", "Qty","Cost","Amt","Tax","Total"};

    public SalesTableModel(ArrayList<Sale> sales)
    {
        this.sales = sales ;
    }
    @Override
    public int getRowCount() {
        return sales.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Object result = null ;

        switch (columnIndex)
        {
            case 0 : result = sales.get(rowIndex).getId(); break;
            case 1 : result = sales.get(rowIndex).getDate(); break;
            case 2 : result = sales.get(rowIndex).getRegion(); break ;
            case 3 : result = sales.get(rowIndex).getProduct(); break ;
            case 4 : result = sales.get(rowIndex).getQuantity();break ;
            case 5 : result = sales.get(rowIndex).getCost(); break ;
            case 6 : result = sales.get(rowIndex).getCost()*sales.get(rowIndex).getQuantity(); break ;
            case 7 : result = sales.get(rowIndex).getTax();break ;
            case 8 : {double amount = sales.get(rowIndex).getCost()*sales.get(rowIndex).getQuantity();
                     result = amount + sales.get(rowIndex).getTax();}
        }
        return result;
    }


    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Adds Element to list and update the model
     * @param sale
     */
    public void addRow(Sale sale)
    {
        sales.add(sale) ;
        fireTableRowsInserted(sales.size() - 1, sales.size() - 1);
    }


    /**
     * Update the whole model
     */
    public void updateAll()
    {
        fireTableDataChanged();
    }

    /**
     * Clear All fields
     */
    public void clear()
    {
        sales.clear();
    }

}
