package network.HO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import model.Sale;
import views.HoView;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NetworkConfig {

    private String host = "51.144.40.231";
    private ConnectionFactory factory ;
    private Connection connection ;
    private Channel channel ;
    private String[] queues;

    private HoView hoView;
    public void closeHo() throws IOException ,TimeoutException{
        System.out.println("Closing HO...");
        connection.close();
        channel.close();
    }


    public NetworkConfig(HoView hoView) throws IOException,TimeoutException
    {
        queues =new String[]{"queue-bo1"};
        this.hoView=hoView ;
        this.initGlobalSettings() ;
    }

    public void initGlobalSettings() throws IOException,TimeoutException
    {
        factory = new ConnectionFactory() ;
        factory.setHost(this.host);
        connection = factory.newConnection();
        channel = connection.createChannel() ;

        for (String queue : queues) {
            initIncomingConnection(factory,queue);
        }
    }


    private void initIncomingConnection(ConnectionFactory factory, String queueName) {

        try {

            DeliverCallback callback = (consumerTag,delivery) -> {
                byte [] byteArray = delivery.getBody() ;
                System.out.println("Sender:" +delivery.getProperties().getHeaders().get("schema"));
                try {
                    hoView.addRow(Sale.fromBytes(byteArray));
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(hoView,"Corrupted Data","Data Received is corrupted",JOptionPane.ERROR);
                }
                //serverView.updateText(queueName.equals("incoming-text1")?1:2,byteArray );
                /** Here we need to update the HO based on the payload */
            };
            channel.basicConsume(queueName, true, callback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
