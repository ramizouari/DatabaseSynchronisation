package repository.HO;

import model.AbstractSale;
import model.HO.Sale;
import repository.IndexedTableRepository;
import utils.Couple;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

public class SaleRepository extends IndexedTableRepository<Couple<Long,String>> {
    public SaleRepository() throws SQLException
    {
        super("HO.Sale", "(ID,OFFICE)","ID","OFFICE","QUANTITY","COST","TAX","DATE","REGION","PRODUCT");
    }

    private Integer create(AbstractSale S) throws SQLException
    {
        return insert(convert(S));
    }

    private Integer update(Sale S) throws SQLException
    {
        return updateById(new Couple<Long,String>(S.getId(),S.getOffice()),convert(S));
    }

    public Integer save(Sale S )throws SQLException
    {
        if(findByIdMap(new Couple<Long,String>(S.getId(),S.getOffice())).isEmpty())
            return create(S);
        else return update(S);
    }
    public  Integer remove(Sale S) throws SQLException
    {
        return removeById(new Couple<Long,String>(S.getId(),S.getOffice()));
    }
    public List<AbstractSale> findAll() throws SQLException
    {
        return fetchAll().stream().map(S->convert(S)).collect(Collectors.toList());
    }

    public Optional<AbstractSale> findById(Couple<Long,String>Id) throws  SQLException
    {
        return findByIdMap(Id).map(S->convert(S));
    }
    private <ValType> List<AbstractSale>  findBy(String colName, ValType val ) throws SQLException
    {
        return findWhereEqual(colName,val.toString()).stream().map(S->convert(S)).collect(Collectors.toList());
    }

    public Optional<AbstractSale> find(Sale S) throws SQLException
    {
        return findById(new Couple<Long,String>(S.getId(),S.getOffice())).filter(H->H.equals(S));
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

    public List<AbstractSale> findByProductId(Long id) throws  SQLException
    {
        return findBy("ID",id);
    }

    public List<AbstractSale> findByDate(String date) throws  SQLException
    {
        return findBy("DATE",date);
    }
    public List<AbstractSale> findByRegion(String region) throws  SQLException
    {
        return findBy("REGION",region);
    }

    private <ValType> Integer  removeBy(String colName, ValType val ) throws SQLException
    {
        return deleteWhereEqual(colName,val.toString());
    }

    public Integer removeByOffice(String office) throws SQLException
    {
        return removeBy("OFFICE",office);
    }

    public Integer removeByProductId(Long id) throws SQLException
    {
        return removeBy("ID",id);
    }

    protected Map<String,String> convert(AbstractSale abstractS)
    {
        Sale S=(Sale)abstractS;
        Map<String,String> saleMapper= new HashMap<String,String>();
        saleMapper.put("ID",String.valueOf(S.getId()));
        saleMapper.put("COST",String.valueOf(S.getCost()));
        saleMapper.put("QUANTITY",String.valueOf(S.getQuantity()));
        saleMapper.put("TAX",String.valueOf(S.getTax()));
        saleMapper.put("DATE",S.getDate());
        saleMapper.put("PRODUCT",S.getProduct());
        saleMapper.put("REGION",S.getRegion());
        saleMapper.put("OFFICE",S.getOffice());
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
        String office=S.get("OFFICE");
        return new Sale(id,office,date,region,product,quantity,cost,tax);
    }


}
