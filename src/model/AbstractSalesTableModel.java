package model;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSalesTableModel extends AbstractTableModel {

    protected List <AbstractSale> sales ;

    protected String[] columnNames;

    public AbstractSalesTableModel(String colNames[],List<AbstractSale> sales)
    {
        this.sales = sales ;
        columnNames=colNames;
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
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Adds Element to list and update the model
     * @param sale
     */
    public void addRow(AbstractSale sale)
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
    public List<AbstractSale> getAllValues()
    {
        return sales;
    }

}
