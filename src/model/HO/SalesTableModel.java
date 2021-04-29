package model.HO;

import model.AbstractSale;
import model.AbstractSalesTableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SalesTableModel extends AbstractSalesTableModel {

    public SalesTableModel(List<Sale> sales)
    {
        super(new String[]{"Office Name","Sale ID","Date", "Region", "Product", "Qty","Cost","Amt","Tax","Total"},
                sales.stream().map(S->(AbstractSale)S).collect(Collectors.toList()));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Sale sale=((Sale)sales.get(rowIndex));
        Object result = null ;

        switch (columnIndex)
        {

            case 0 : result = sale.getOffice(); break;
            case 1 : result = sale.getId(); break;
            case 2 : result = sale.getDate(); break;
            case 3 : result = sale.getRegion(); break ;
            case 4 : result = sale.getProduct(); break ;
            case 5 : result = sale.getQuantity();break ;
            case 6 : result = sale.getCost(); break ;
            case 7 : result = sale.getCost()*sales.get(rowIndex).getQuantity(); break ;
            case 8 : result = sale.getTax();break ;
            case 9 : {double amount = sale.getCost()*sale.getQuantity();
                result = amount + sale.getTax();}
        }
        return result;
    }

}
