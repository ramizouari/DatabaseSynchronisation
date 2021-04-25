package repository;

import dba.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

abstract public class TableRepository
{
    private Connection connection;
    private String tableName;
    private Set<String> columns;
    public TableRepository(String tName,Set<String> C) throws SQLException
    {
        connection= DBConnection.getInstance().getConnection();
        columns=C;
        tableName=tName;
    }
    public TableRepository(String tName,String ...columnName)
    {
        tableName=tName;
        columns=Set.of(columnName);
    }


    public Collection<Map<String,String>> fetchAll() throws SQLException
    {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM "+tableName);
        System.out.println(statement.toString());
        ResultSet set = statement.executeQuery();
        Vector<Map<String,String>> results=new Vector<Map<String,String>>();
        while(set.next())
        {
            Map map=new HashMap<String,String>();
            for(String col:columns)
                map.put(col,set.getString(col));
            results.add(map);
        }
        return results;
    }

    public Collection<Map<String,String>> findWhereEqual(String columnName, String value) throws SQLException
    {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM ? WHERE ?=?");
        statement.setString(1,tableName);
        statement.setString(2,columnName);
        statement.setString(3,value);
        ResultSet set = statement.executeQuery();
        Vector<Map<String,String>> results=new Vector<Map<String,String>>();
        while(set.next())
        {
            Map map=new HashMap<String,String>();
            for(String col:columns)
                map.put(col,set.getString(col));
            results.add(map);
        }
        return results;
    }

    public int insert(Map<String,String> colMap ) throws SQLException
    {
        StringBuilder builder = new StringBuilder("INSERT INTO %s".formatted(tableName));
        String columns=String.join
        (",", colMap
                        .keySet()
        );
        String values=String.join
        (",", colMap
                .values()
                .stream()
                .<String>map(s->"?")
                .collect(Collectors.toList())
        );        builder.append(columns);
        builder.append(") values(");
        builder.append(values);
        builder.append(")");
        PreparedStatement statement=connection.prepareStatement(builder.toString());
        int k=1;
        for(String col: colMap.keySet())
            statement.setString(k++,col);
        for(String col: colMap.values())
            statement.setString(k++,col);
        return statement.executeUpdate();
    }

    public Integer updateWhereEqual(Map<String,String> colMap,String colName,String value ) throws SQLException
    {
        StringBuilder builder = new StringBuilder("UPDATE %s set ".formatted(tableName));
        for(String column : colMap.keySet())
            builder.append("%s=?,".formatted(column));
        builder.setCharAt(builder.length()-1,' ');
        builder.append("WHERE %s = ?".formatted(colName));
        PreparedStatement statement=connection.prepareStatement(builder.toString());
        statement.setString(1,tableName);
        int k=2;
        for(String col: colMap.keySet())
            statement.setString(k++,colMap.get(col));
        statement.setString(k,value);
        return statement.executeUpdate();
    }

    public Integer dropWhereEqual(String colName,String value ) throws SQLException
    {
        PreparedStatement statement=connection.prepareStatement("DROP FROM %s WHERE ? = ? ".formatted(tableName));
        statement.setString(1,colName);
        statement.setString(2,value);
        return statement.executeUpdate();
    }
}
