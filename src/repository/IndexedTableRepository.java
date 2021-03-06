package repository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class IndexedTableRepository<IDType> extends TableRepository
{
    protected String idName;
    static private Set<String>appendID(String ID,Set<String> S)
    {
        S.add(ID);
        return S;
    }
    public IndexedTableRepository(String tName,String IDName, Set<String> C) throws SQLException
    {
        super(tName,C);
        idName=IDName;
    }

    public IndexedTableRepository(String tName,String IDName, String ...cols) throws SQLException
    {
        super(tName,cols);
        idName=IDName;
    }

    public Optional<Map<String,String>> findByIdMap(String id) throws SQLException
    {
        return findWhereEqual(idName,id).stream().findAny();
    }

    public Optional<Map<String,String>> findByIdMap(IDType id) throws SQLException
    {
        return findByIdMap(id.toString());
    }

    public Integer removeById(String id) throws SQLException
    {
        return deleteWhereEqual(idName,id);
    }

    public Integer removeById(IDType id) throws SQLException
    {
        return deleteWhereEqual(idName,id.toString());
    }

    public Integer updateById(IDType id,Map<String,String> updater) throws SQLException {
        return updateWhereEqual(updater,idName,String.valueOf(id));
    }

    public Integer updateById(String id,Map<String,String> updater) throws SQLException {
        return updateWhereEqual(updater,idName,id);
    }
}
