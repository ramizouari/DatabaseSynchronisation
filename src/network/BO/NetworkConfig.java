package network.BO;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dba.DBConnection;
import network.GlobalNetworkConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class NetworkConfig
{
    private ConnectionFactory factory;
    private Connection connection ;
    private Channel channel  ;
    private String outgoingQueue ;
    private DBConnection dbConnection;

    public void closeBo() throws IOException,TimeoutException
    {
        channel.close();
        connection.close();
    }

    public NetworkConfig(String queueName, DBConnection dbCon) throws IOException,TimeoutException
    {
        this.outgoingQueue = queueName ;
        dbConnection=dbCon;
        initConnection();
    }

    public void initConnection () throws IOException,TimeoutException
    {
        factory = new ConnectionFactory() ;
        factory.setHost(GlobalNetworkConfig.RABBITMQ_HOST);
        initOutgoingConnection(factory);
    }


    public void initOutgoingConnection(ConnectionFactory factory)throws IOException, TimeoutException
    {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(GlobalNetworkConfig.UPWARD_QUEUE_NAME, true, false, false, null);
        System.out.println("Ready to send messages from "+ outgoingQueue);
    }
    public void publishMessage(byte [] byteArray) throws IOException
    {
            AMQP.BasicProperties.Builder builder=new AMQP.BasicProperties.Builder();
            Map<String,Object> header=new HashMap<String,Object>();
            header.put("office",dbConnection.getSchema());
            builder.headers(header);
            this.channel.basicPublish("", GlobalNetworkConfig.UPWARD_QUEUE_NAME, builder.build(),byteArray);
    }
}
