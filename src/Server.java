import com.google.gson.*;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ThreadFactory;

/**
 * Created by wautel_l on 10/04/2017.
 */
public class Server {

    private ServerSocket server;
    private boolean isRunning = true;
    private String invent_json;
    private ClientProcessor ClPlugin = null;
    public Server server_ref;
    private ClientProcessor ClApp = null;
    public static void main(String[] args)
    {
        new Server();
    }

    public Server()
    {
        try {
            server_ref = this;
            server = new ServerSocket(9999);
            System.out.println("Server started at: " + new Date());

          //  Socket socket = sSocket.accept();


        }
        catch (IOException exception)
        {
            System.out.println("Error: " + exception);
        }

        Thread t = new Thread(new Runnable() {
            public void run() {
                while (isRunning == true) try {
                    Socket connection = server.accept();
//                    ClientProcessor cl = new ClientProcessor(connection);
                    if ((connection.getInetAddress().getHostAddress().equals("192.168.1.45"))) {
                        System.out.println("hello plugin");
                        ClPlugin = new ClientProcessor(connection, server_ref);
//                        Thread t = new Thread(ClPlugin);
                        ClPlugin.start();
                    } else {
                        System.out.println("hello app");
                        ClApp = new ClientProcessor(connection, server_ref);
                        //Thread t = new Thread(ClApp);
                        ClApp.start();
                    }

//                    Thread t = new Thread(new ClientProcessor(connection));
//                    t.start();
                    //send_data();
                    System.out.println("connexion client");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    server = null;
                }
            }

        } );
        t.start();
        if (ClApp != null)
            System.out.print(ClApp.data+"efdkjfhsiuduhfierz");
    }

    public void send_datas_app(String data)
    {
        if (ClApp != null)
        {
            System.out.println("data plugin:" + ClPlugin.data);
            ClApp.sendresponse(data);
        }
    }

    public void send_data() {
        if (ClApp != null && ClPlugin != null) {
            System.out.println("allo");
            String msg = "";
            if (ClApp.data.contains("mob_")) {
                System.out.println("YOUPOIIIIIIIIIIIIIIIIIIIIIIIII");
                ClApp.sendresponse("ok");
//                ClApp.data = "";
            }
            if (ClApp.data.contains("map")) {
                if (ClPlugin.data.contains("coordinates")) {
                    Gson gson = new Gson();
                    JsonElement element = gson.fromJson(ClPlugin.data, JsonElement.class);
                    JsonObject jsonObject = element.getAsJsonObject();
                    msg = jsonObject.get("coordinates").getAsString();
                    msg = msg.replaceAll("\\s+","");
                    System.out.println("MESAAGGGEE" + msg + "\n\n\n\n");

                    ClApp.sendresponse(msg);
                }
            }
            //ClApp.sendresponse(ClPlugin.data);
        }
    }

    public void send_datas_plugin(String data)
    {
        if (ClPlugin != null)
        {
            System.out.println("data plugin:" + ClPlugin.data);
            ClPlugin.sendresponse(data);
        }
    }

    public void close()
    {
        isRunning =false;
    }
}



