package repository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class IndexedTableRepository<IDType> extends TableRepository
{
    private String idName;
    static private Set<String>appendID(String ID,Set<String> S)
    {
        S.add(ID);
        return S;
    }
    public IndexedTableRepository(String tName,String IDName, Set<String> C) throws SQLException
    {
        super(tName,appendID(IDName,C));
        idName=IDName;
    }

    public IndexedTableRepository(String tName,String IDName, String ...cols) throws SQLException
    {
        super(tName,appendID(IDName, Arrays.stream(cols).collect(Collectors.toSet())));
        idName=IDName;
    }

    public Optional<Map<String,String>> findById(String id) throws SQLException
    {
        return findWhereEqual(idName,id).stream().findAny();
    }

    public Optional<Map<String,String>> findByIdMap(IDType id) throws SQLException
    {
        return findById(String.valueOf(id));
    }

    public Integer removeById(String id) throws SQLException
    {
        return dropWhereEqual(idName,id);
    }

    public Integer removeById(IDType id) throws SQLException
    {
        return dropWhereEqual(idName,id.toString());
    }

    public Integer updateById(IDType id,Map<String,String> updater) throws SQLException {
        return updateWhereEqual(updater,idName,String.valueOf(id));
    }

    public Integer updateById(String id,Map<String,String> updater) throws SQLException {
        return updateWhereEqual(updater,idName,id);
    }
}
