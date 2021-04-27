package model.BO;

import model.AbstractSale;
import model.AbstractSalesTableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SalesTableModel extends AbstractTableModel
{
    ArrayList<Sale> sales ;

    String[] columnNames={"Sale ID","Date", "Region", "Product", "Qty","Cost","Amt","Tax","Total"};

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
        AbstractSale sale = sales.get(rowIndex);
        switch (columnIndex)
        {
            case 0 : result = sale.getId(); break;
            case 1 : result = sale.getDate(); break;
            case 2 : result = sale.getRegion(); break ;
            case 3 : result = sale.getProduct(); break ;
            case 4 : result = sale.getQuantity();break ;
            case 5 : result = sale.getCost(); break ;
            case 6 : result = sale.getCost()*sale.getQuantity(); break ;
            case 7 : result = sale.getTax();break ;
            case 8 : {double amount = sale.getCost()*sale.getQuantity();
                result = amount + sale.getTax();}
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
