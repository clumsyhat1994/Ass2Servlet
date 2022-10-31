package Servlet;

import Shapes.LiftRide;
import Shapes.LiftRideEvent;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import rmqpool.RMQChannelFactory;
import rmqpool.RMQChannelPool;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "Servlet", value = "/Servlet")
public class Servlet extends HttpServlet {
    private String  requestQueueName = "rq_queue";
    // private String  replyQueueName = "rp_queue";
    private RMQChannelPool pool;
    @Override
    public void init() throws ServletException {
        super.init();
        ConnectionFactory factory = new ConnectionFactory();
        //factory.setHost("localhost");
        factory.setHost("35.87.183.246");
        try {
            pool = new RMQChannelPool(50,new RMQChannelFactory(factory.newConnection()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.getWriter().write("20:56");
        res.setContentType("application/json");
        String urlPath = req.getPathInfo();

        if(urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(("missing parameters"));
            return;
        }

        String[] urlParts = urlPath.split("/");



        if(isUrlValid(urlParts)){
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("okok");
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //res.setContentType("application/json");
        String urlPath = req.getPathInfo();
        if(urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(("missing parameters"));
            return;
        }
        String[] urlParts = urlPath.split("/");
        if(!isUrlValid(urlParts)){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("Invalid path.");
            return;
        }


        BufferedReader bufferedReader = req.getReader();

        Gson gson = new Gson();
        StringBuilder msg = new StringBuilder();
        String line;
        while( (line = bufferedReader.readLine()) != null) {
            msg.append(line);
        }

        LiftRide lr = (LiftRide)gson.fromJson(msg.toString(),LiftRide.class);
        if(lr.getLiftID()==null||lr.getTime()==null){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        LiftRideEvent lrEvent = new LiftRideEvent(Integer.valueOf(urlParts[1]),urlParts[3],urlParts[5],Integer.valueOf(urlParts[7]),lr);
        String result = produce(gson.toJson(lrEvent));
        if(Objects.equals(result, "200")) res.setStatus(HttpServletResponse.SC_OK);
        else res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private String produce(String msg){
        final String corrId = UUID.randomUUID().toString();
        try {
            Channel channel = pool.borrowObject();
//
//            String replyQueueName = channel.queueDeclare().getQueue();
//
//            AMQP.BasicProperties props = new AMQP.BasicProperties
//                    .Builder()
//                    .correlationId(corrId)
//                    .replyTo(replyQueueName)
//                    .build();

           // channel.basicPublish("", requestQueueName, props, msg.getBytes("UTF-8"));
            channel.basicPublish("", requestQueueName, null, msg.getBytes("UTF-8"));
//            final CompletableFuture<String> response = new CompletableFuture<>();
//            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
//                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                    System.out.println("Response: "+new String(delivery.getBody(), "UTF-8"));
//                    response.complete(new String(delivery.getBody(), "UTF-8"));
//                }
//            }, consumerTag -> {
//            });
//
//            String ret = response.get();
//
//            channel.basicCancel(ctag);
            pool.returnObject(channel);
            //return ret;
            return "200";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            pool.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isUrlValid(String[] urlParts){
        if(urlParts.length!=3 && urlParts.length!=8) return false;
        if(urlParts.length==3){
            if(!urlParts[2].equals("vertical")||!isNumeric(urlParts[1])) return false;
        } else if(urlParts.length==8){
            if(!isNumeric(urlParts[1])||Integer.valueOf(urlParts[1])>10||!urlParts[2].equals("seasons")||!isNumeric(urlParts[3])||!urlParts[4].equals("days")||!isNumeric(urlParts[5])||!urlParts[6].equals("skiers")||!isNumeric(urlParts[7])) return false;
        }
        return true;
    }
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}