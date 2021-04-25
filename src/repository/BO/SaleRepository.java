package repository.BO;

import java.sql.SQLException;

public class SaleRepository extends repository.SaleRepository
{
    public SaleRepository(String BOName) throws SQLException
    {
        super(BOName+".Sale");
    }
}
