package model.BO;

import model.AbstractSale;
import model.AbstractSalesTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SalesTableModel extends AbstractSalesTableModel
{

    private Boolean editableColumns[];
    private List<Long> deletedIds;
    public SalesTableModel(List<Sale> sales)
    {
        super(new String[]{"Sale ID","Date", "Region", "Product", "Qty","Cost","Amt","Tax","Total"},
                sales.stream().map(S->(AbstractSale)S).collect(Collectors.toList()));
        editableColumns=new Boolean[] {false,true,true,true,true,true,false,true,false};
        deletedIds = new ArrayList<Long>();
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
    public boolean isCellEditable(int row, int column)
    {
        // custom isCellEditable function
        return editableColumns[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Sale S = (Sale)sales.get(rowIndex);
        try {


            switch (columnIndex) {
                case 1:
                {
                    String dateString=(String)aValue;
                    if(!dateString.isEmpty())
                    {
                        LocalDate date = (LocalDate.parse((String) aValue));
                        S.setDate(date.toString());
                    }
                    else S.setDate(null);
                    break;
                }
                case 2:
                    S.setRegion((String) aValue);
                    break;
                case 3:
                    S.setProduct((String) aValue);
                    break;
                case 4: {
                    int value = Integer.parseInt((String) aValue);
                    if (value < 0)
                        throw new InputMismatchException("Negative Value not Allowed");
                    S.setQuantity(value);
                    break;
                }
                case 5: {
                    double value = Double.parseDouble((String) aValue);
                    if (value < 0)
                        throw new InputMismatchException("Negative Value not Allowed");
                    S.setCost(value);
                    break;
                }
                case 7: {
                    double value = Double.parseDouble((String) aValue);
                    if (value < 0)
                        throw new InputMismatchException("Negative Value not Allowed");
                    S.setTax(value);
                    break;
                }

            }
            fireTableDataChanged();
        }
        catch(NumberFormatException exc)
        {
            System.err.println("Not a valid Number");
        }
        catch(InputMismatchException exc)
        {
            System.err.println(exc.getMessage());
        }
        catch(DateTimeParseException exc)
        {
            System.err.println("Not a valid Date");
        }

    }

    public void addEmptyRow()
    {
        addRow( new Sale(null,null,null,0,0,0));
    }
    public void registerDeleteId(long ID)
    {
        deletedIds.add(ID);
    }

    public List<Long> getAllDeletedIds()
    {
        return deletedIds;
    }


    public void clearDeletedIds()
    {
        deletedIds.clear();
    }

    @Override
    public void clear()
    {
        super.clear();
        clearDeletedIds();
    }
    public int remove(int row)
    {
        if(row == -1)
            return -1;
        registerDeleteId(sales.get(row).getId());
        sales.remove(sales.get(row));
        fireTableRowsDeleted(row,row);
        return Math.min(row,sales.size()-1);
    }
}
