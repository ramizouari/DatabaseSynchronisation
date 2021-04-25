package repository.HO;

import java.sql.SQLException;

public class SaleRepository extends repository.SaleRepository {
    public SaleRepository(String BOName) throws SQLException
    {
        super("HO."+BOName+"Sale");
    }
}
