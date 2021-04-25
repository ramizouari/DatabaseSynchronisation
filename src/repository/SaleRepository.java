package repository;

import dba.DBConnection;
import model.Sale;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SaleRepository extends IndexedTableRepository<Long>
{
    private static Map<String,String>convert(Sale S)
    {
        Map<String,String> saleMapper= new HashMap<String,String>();
        saleMapper.put("ID",String.valueOf(S.getId()));
        saleMapper.put("COST",String.valueOf(S.getCost()));
        saleMapper.put("QUANTITY",String.valueOf(S.getQuantity()));
        saleMapper.put("TAX",String.valueOf(S.getTax()));
        saleMapper.put("DATE",S.getDate());
        saleMapper.put("PRODUCT",S.getProduct());
        saleMapper.put("REGION",S.getRegion());
        return saleMapper;
    }
    private static Sale convert(Map<String,String> S)
    {
        Long id=Long.valueOf(S.get("ID"));
        Double cost = Double.valueOf(S.get("COST"));
        Integer quantity = Integer.valueOf(S.get("QUANTITY"));
        Double tax = Double.valueOf(S.get("TAX"));
        String date = S.get("DATE");
        String region = S.get("REGION");
        String product=S.get("PRODUCT");
        return new Sale(id,date,region,product,quantity,cost,tax);
    }

    public SaleRepository() throws SQLException {
        super(DBConnection.getSchema()+".Sale","ID","QUANTITY","COST","TAX","DATE","REGION");
    }

    protected SaleRepository(String dbName) throws  SQLException
    {
        super(dbName,".Sale","ID","QUANTITY","COST","TAX","DATE","REGION");
    }

    private Integer create(Sale S) throws SQLException
    {
        return insert(convert(S));
    }

    private Integer update(Sale S) throws SQLException
    {
        return updateById(S.getId(),convert(S));
    }

    public Integer save(Sale S )throws SQLException
    {
        if(findByIdMap(S.getId()).isEmpty())
            return create(S);
        else return update(S);
    }
    public  Integer remove(Sale S) throws SQLException
    {
        return removeById(S.getId());
    }
    public Set<Sale> findAll() throws SQLException
    {
        return fetchAll().stream().map(S->convert(S)).collect(Collectors.toSet());
    }

    public Optional<Sale> findById(Long Id) throws  SQLException
    {
        return findByIdMap(Id).map(S->convert(S));
    }
    private <ValType> Set<Sale>  findBy(String colName,ValType val ) throws SQLException
    {
        return findWhereEqual(colName,val.toString()).stream().map(S->convert(S)).collect(Collectors.toSet());
    }

    public Set<Sale> findByCost(Double cost) throws  SQLException
    {
        return findBy("COST",cost);
    }

    public Set<Sale> findByTax(Double tax) throws  SQLException
    {
        return findBy("TAX",tax);
    }

    public Set<Sale> findByQuantity(Double quantity) throws  SQLException
    {
        return findBy("QUANTITY",quantity);
    }

    public Set<Sale> findByProduct(Double product) throws  SQLException
    {
        return findBy("PRODUCT",product);
    }
    public Set<Sale> findByDate(String date) throws  SQLException
    {
        return findBy("DATE",date);
    }
    public Set<Sale> findByRegion(String region) throws  SQLException
    {
        return findBy("REGION",region);
    }

    public Optional<Sale> find(Sale S) throws SQLException
    {
        return findById(S.getId()).filter(H->H==S);
    }

}
