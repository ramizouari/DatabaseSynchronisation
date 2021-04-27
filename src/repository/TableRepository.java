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
    public TableRepository(String tName,String ...columnName) throws SQLException
    {
        connection= DBConnection.getInstance().getConnection();
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

    /*
    * This method will return a list of values of a given tuple
    * TODO:
    *  Add support for escaped ','
    * */
    static private List<String> getTupleValues(String S)
    {
        String V[]=S.split(",");
        V[0]=V[0].replaceAll("\\(","");
        V[V.length-1]=V[V.length-1].replaceAll("\\)","");
        return Arrays.stream(V).collect(Collectors.toList());
    }
    public Collection<Map<String,String>> findWhereEqual(String columnName, String value) throws SQLException
    {
        List<String> V=getTupleValues(value);
        PreparedStatement statement;
        if(V.size()==1) {
             statement= connection.prepareStatement("SELECT * FROM %s WHERE %s=?".formatted(tableName, columnName));
            statement.setString(1, value);
        }
        else
        {
            StringBuilder tupleBuilder=new StringBuilder("(");
            tupleBuilder.append(String.join(",", V.stream().<String>map(S->"?").collect(Collectors.toList())));
            tupleBuilder.append(")");
            statement=connection.prepareStatement("SELECT * FROM %s WHERE %s=".formatted(tableName,columnName)
                    +tupleBuilder.toString());
            int k=1;
            for(String v:V)
                statement.setString(k++,v);
        }
        System.err.println(statement.toString());
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
        StringBuilder builder = new StringBuilder("INSERT INTO %s(".formatted(tableName));
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
        for(String value: colMap.values())
            statement.setString(k++,value);
        System.err.println(statement.toString());
        return statement.executeUpdate();
    }

    public Integer updateWhereEqual(Map<String,String> colMap,String colName,String value ) throws SQLException
    {
        List<String>V=getTupleValues(value);
        String whereClause;
        int tupleSize=V.size();
        if(V.size()==1)
            whereClause="WHERE %s = ?".formatted(colName);
        else
        {
            StringBuilder tupleBuilder=new StringBuilder("(");
            tupleBuilder.append(String.join(",", V.stream().<String>map(S->"?").collect(Collectors.toList())));
            tupleBuilder.append(")");
            whereClause="WHERE %s=%s".formatted(colName,tupleBuilder.toString());
            int k=1;
        }

        StringBuilder builder = new StringBuilder("UPDATE %s set ".formatted(tableName));
        for(String column : colMap.keySet())
            builder.append("%s=?,".formatted(column));
        builder.setCharAt(builder.length()-1,' ');
        builder.append(whereClause);
        PreparedStatement statement=connection.prepareStatement(builder.toString());
        statement.setString(1,tableName);
        int k=2;
        for(String col: colMap.keySet())
            statement.setString(k++,colMap.get(col));
        for(String v:V)
            statement.setString(k++,v);
        return statement.executeUpdate();
    }

    public Integer deleteWhereEqual(String colName,String value ) throws SQLException
    {
        List<String>V=getTupleValues(value);
        PreparedStatement statement;
        if(V.size()==1) {
            statement= connection.prepareStatement("DELETE FROM %s WHERE %s=?".formatted(tableName, colName));
            statement.setString(1, value);
        }
        else
        {
            StringBuilder tupleBuilder=new StringBuilder("(");
            tupleBuilder.append(String.join(",", V.stream().<String>map(S->"?").collect(Collectors.toList())));
            tupleBuilder.append(")");
            statement=connection.prepareStatement("DELETE FROM %s WHERE %s=".formatted(tableName,colName)+
                    tupleBuilder.toString());
            int k=1;
            for(String v:V)
                statement.setString(k++,v);
        }
        return statement.executeUpdate();
    }
    public Integer deleteAll() throws SQLException
    {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM %s".formatted(tableName));
        return statement.executeUpdate();
    }
}
