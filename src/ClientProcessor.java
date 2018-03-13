import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by wautel_l on 14/06/2017.
 */
public class ClientProcessor extends Thread {
    private Socket socket;

    private PrintWriter writer = null;
    private BufferedReader reader = null;
    public String data = "";
    private String ip = "192.168.1.45";
    int test = 0;
    Item[] ex ;
    Mob mob[];
    JsonElement element;
    JsonObject jobject;
    JsonArray tmparr;
    Gson gson;
    private Server server;
    int tmp_inv;



    public ClientProcessor(Socket psock, Server server)
    {
        socket = psock;
        this.server = server;
    }


    public void run() {
        System.err.println("traitement de la connection cliente");
        init_items();
        boolean closeCo = false;
        tmp_inv = 0;
        while (!socket.isClosed()) {

            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

 //               System.err.println("test1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String response = reader.readLine();
//                System.err.println("test2");
                InetSocketAddress remote = (InetSocketAddress) socket.getRemoteSocketAddress();
 //               System.err.println("test2");



                if (response != null) {
                    String debug = "";
                    debug = "Thread : " + Thread.currentThread().getName() + ". ";
                    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() + ".";
                    debug += " Sur le port : " + remote.getPort() + ".\n";
                    debug += "\t -> Commande reçue : " + response + "\n";
                    //System.err.println("\n" + debug);
                    if (!(remote.getAddress().getHostAddress().equals(this.ip))) {
                        System.out.print("apppppppp");
                        this.data = response;
                        this.server.send_data();
                        System.out.print("app \n"+response);
                        //this.data = "";
/*                        if (response.toLowerCase().equals("inventory"))
                            send_inventory(response);
                        else if (response.toLowerCase().contains("item_"))
                            send_items(response);
                        else if (response.toLowerCase().contains("action_"))
                            execute_action(response);
                        else if (response.toLowerCase().contains("listmob"))
                            send_mob_list();
                        else if (response.toLowerCase().contains("mob_"))
                            receive_good();
                        else if (response.toLowerCase().contains("map"))
                            send_coord();
                        else
                            send_response_auto();*/
                    } else {
                        response = response.substring(5,response.length());
                        this.data = response;
                        System.out.print("\nplugin="+this.data);

                        this.server.send_data();
                        //this.data = "";
                        //System.out.print("plugin \n"+response);
                    }
                } else {
                    this.data = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receive_good()
    {
        this.data = "ok";
        writer.println("ok");
        writer.flush();
        System.out.println("ok");
    }

    public void send_response_auto()
    {
        writer.println("command not implemented yet");
        writer.flush();
    }


    public void send_mob_list()
    {
        init_list();
        gson = new Gson();

        init_list();
        tmparr = new JsonArray();
        for (int i = 0; i < mob.length; i += 1) {
            element = gson.toJsonTree(mob[i]);
            jobject = element.getAsJsonObject();
            tmparr.add(jobject);
        }
        // JsonObject fin = new JsonObject(tmparr);
        // String tmps = "{\"inventory\": [{\"id\": \"1\", \"name\": \"sword\", \"number\": \"1\"}, {\"id\": \"2\", \"name\": \"apple\", \"number\": \"2\"}]}";                        Gson gson_inventory = new GsonBuilder().create();
        writer.println(tmparr);
        writer.flush();
        System.out.print(tmparr);
    }


    public void init_list()
    {
         mob = new Mob[2];
        mob[0] = new Mob(0, "ROBOT TUEUR");
        mob[1] = new Mob(1, "MARIO");

    }

    public void execute_action(String strtmp)
    {
        String[] tmp = strtmp.split("_");
        if (tmp[1].equals("supprimer"))
        {
            System.out.println("delete");
            ex = null;
            ex = new Item[1];
            if (Integer.parseInt(tmp[2]) == 1)
            {
                ex[0] = new Item("apple", 2, 2, "ingredient rapportant de la vie", "utiliser", "supprimer");
            }
            else if (Integer.parseInt(tmp[2]) == 2)
            {
                ex[0] = new Item("sword", 1, 1, "arme", "equiper", "supprimer");
            }

        }
        System.out.println("succeed");
        writer.println("succeed");
        writer.flush();
    }

    public void send_items(String strtmp)
    {
        gson = new Gson();
        String[] tmp = strtmp.split("_");
        element = gson.toJsonTree(ex[Integer.parseInt(tmp[1]) - 1]);
        jobject = element.getAsJsonObject();
        writer.println(jobject);
        writer.flush();

    }

    public void init_items()
    {

        ex = new Item[2];
        ex[0] = new Item("sword", 1, 1, "arme", "equiper", "supprimer");
        ex[1] = new Item("apple", 2, 2, "ingredient rapportant de la vie", "utiliser", "supprimer");
    }

    public void send_inventory(String response)
    {
        this.data = response;
        server.send_datas_plugin(response);

        gson = new Gson();

        tmparr = new JsonArray();
        if (tmp_inv % 2 == 0) {
            for (int i = 0; i < ex.length; i += 1) {
                element = gson.toJsonTree(ex[i]);
                jobject = element.getAsJsonObject();
                tmparr.add(jobject);

            }
            tmp_inv += 1;
        }
        else{
                element = gson.toJsonTree(ex[0]);
                jobject = element.getAsJsonObject();
                tmparr.add(jobject);
                tmp_inv += 1;
        }
        // JsonObject fin = new JsonObject(tmparr);
        // String tmps = "{\"inventory\": [{\"id\": \"1\", \"name\": \"sword\", \"number\": \"1\"}, {\"id\": \"2\", \"name\": \"apple\", \"number\": \"2\"}]}";                        Gson gson_inventory = new GsonBuilder().create();
        //toAppWriter.println(tmparr);
        //toAppWriter.flush();
        writer.println(tmparr);
        writer.flush();
        System.out.print(tmparr);
    }

    public void save_coord() {
    }

    public void send_coord()
    {

        if (test == 0)
        {
            writer.println("1500;-490");
            writer.flush();
            test = 1;
        }
        else if (test == 1)
        {
            writer.println("1500;1500");
            writer.flush();
            test = 2;
        }
        else if (test == 2)
        {
            writer.println("-490;1500");
            writer.flush();
            test = 3;
        }
        else if (test == 3) {
            writer.println("-490;-490");
            writer.flush();
            test = 0;
        }
    }
     /*   private String read() throws IOException{
            String response = "";
            int stream;
            byte[] b = new byte[4096];
            stream = reader.read(b);
            response = new String(b, 0, stream);
            return response;
        }*/
     public PrintWriter getWriter() {
         return writer;
     }

     public void sendresponse(String answer)
     {
         writer.println(answer);
         writer.flush();
     }

}
