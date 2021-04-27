package network.HO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import model.HO.Sale;
import network.GlobalNetworkConfig;
import utils.Serializer;
import views.HoView;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import repository.HO.SaleRepository;

public class NetworkConfig
{

    private ConnectionFactory factory ;
    private Connection connection ;
    private Channel channel ;
    private String[] queues;
    private SaleRepository saleRepository;

    private HoView hoView;
    public void closeHo() throws IOException ,TimeoutException{
        System.out.println("Closing HO...");
        connection.close();
        channel.close();
    }


    public NetworkConfig(HoView hoView,SaleRepository repo) throws IOException,TimeoutException
    {
        queues =new String[]{"queue-bo1"};
        this.hoView=hoView ;
        this.initGlobalSettings() ;
        saleRepository=repo;
    }

    public void initGlobalSettings() throws IOException,TimeoutException
    {
        factory = new ConnectionFactory() ;
        factory.setHost(GlobalNetworkConfig.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel() ;
        channel.queueDeclare(GlobalNetworkConfig.UPWARD_QUEUE_NAME, true, false, false, null);
        initIncomingConnection(GlobalNetworkConfig.UPWARD_QUEUE_NAME);
    }


    private void initIncomingConnection(String queueName) {

        try {

            DeliverCallback callback = (consumerTag,delivery) -> {
                byte [] byteArray = delivery.getBody() ;
                String officeName=delivery.getProperties().getHeaders().get("office").toString();
                try {
                    List<Sale> saleDB= Serializer.<List<model.BO.Sale>>collectFromBytes(byteArray)
                            .stream()
                            .map(S->new Sale(S,officeName))
                            .collect(Collectors.toList());
                    saleRepository.removeByOffice(officeName);
                    for(Sale s:saleDB)
                        saleRepository.save(s);
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(hoView,"Corrupted Data","Data Received is corrupted",JOptionPane.ERROR_MESSAGE);
                    System.err.println(e.getMessage());
                }
                catch (SQLException e)
                {
                    JOptionPane.showMessageDialog(hoView,"Update Error","Unable to update with received content",JOptionPane.ERROR_MESSAGE);
                    System.err.println(e.getMessage());
                }
                //serverView.updateText(queueName.equals("incoming-text1")?1:2,byteArray );
                /** Here we need to update the HO based on the payload */
            };
            channel.basicConsume(GlobalNetworkConfig.UPWARD_QUEUE_NAME, true, callback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
