package repository.BO;

import model.AbstractSale;
import model.BO.Sale;
import repository.IndexedTableRepository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SaleRepository extends IndexedTableRepository<Long>
{
    public SaleRepository(String BOName) throws SQLException
    {
        super(BOName+".Sale", "ID","ID","QUANTITY","COST","TAX","DATE","REGION","PRODUCT");
    }

    private Integer create(AbstractSale S) throws SQLException
    {
        return insert(convert(S));
    }

    private Integer update(AbstractSale S) throws SQLException
    {
        return updateById(S.getId(),convert(S));
    }

    public Integer save(AbstractSale S )throws SQLException
    {
        if(findByIdMap(S.getId()).isEmpty())
            return create(S);
        else return update(S);
    }
    public  Integer remove(AbstractSale S) throws SQLException
    {
        return removeById(S.getId());
    }
    public List<AbstractSale> findAll() throws SQLException
    {
        return fetchAll().stream().map(S->convert(S)).collect(Collectors.toList());
    }

    public Optional<AbstractSale> findById(Long Id) throws  SQLException
    {
        return findByIdMap(Id).map(S->convert(S));
    }
    private <ValType> List<AbstractSale>  findBy(String colName, ValType val ) throws SQLException
    {
        return findWhereEqual(colName,val.toString()).stream().map(S->convert(S)).collect(Collectors.toList());
    }

    public List<AbstractSale> findByCost(Double cost) throws  SQLException
    {
        return findBy("COST",cost);
    }

    public List<AbstractSale> findByTax(Double tax) throws  SQLException
    {
        return findBy("TAX",tax);
    }

    public List<AbstractSale> findByQuantity(Double quantity) throws  SQLException
    {
        return findBy("QUANTITY",quantity);
    }

    public List<AbstractSale> findByProduct(Double product) throws  SQLException
    {
        return findBy("PRODUCT",product);
    }
    public List<AbstractSale> findByDate(String date) throws  SQLException
    {
        return findBy("DATE",date);
    }
    public List<AbstractSale> findByRegion(String region) throws  SQLException
    {
        return findBy("REGION",region);
    }

    public Optional<AbstractSale> find(AbstractSale S) throws SQLException
    {
        return findById(S.getId()).filter(H->H.equals(S));
    }


    protected Map<String,String> convert(AbstractSale S)
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
    protected AbstractSale convert(Map<String,String> S)
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

}
