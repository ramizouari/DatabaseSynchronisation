package dba;

import network.GlobalNetworkConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private Connection connection;
    static private DBConnection dbConnection;
    static private String username;
    static private String schema;
    static private String password;
    static private String host;
    private DBConnection(String host,String u,String p) throws SQLException
    {
        this(host,"",u,p);
    }
    private DBConnection(String host,String sch,String u,String p) throws SQLException
    {
        connection=DriverManager.getConnection( GlobalNetworkConfig.DB_DRIVER+host+'/'+sch,u,p);
        p=null;
    }

    public static DBConnection getInstance() throws SQLException
    {
        if(dbConnection==null)
            dbConnection=new DBConnection(host,username,password);
        return dbConnection;
    }
    public static void configure(String h,String sch,String name,String pass)
    {
        username = name;
        password=pass;
        host=h;
        schema=sch;
    }
    public static void configure(String h,String name,String pass)
    {
        configure(h,"",name,pass);
    }
    public Connection getConnection()
    {
        return connection;
    }
    static public String getSchema()
    {
        return schema;
    }
}
