import java.io.*; 
import java.net.*; 
import java.util.*;
 
final class EchoConnect  extends TCPServer implements Runnable {
  Socket socket;
  

  // Constructor
  public EchoConnect(Socket socket) throws Exception {
    this.socket = socket;
    // sockets.add(this.socket);
  }


  // Implement the run() method of the Runnable interface.
  public void run() {
    try {
      processRequest();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  private void processRequest() throws Exception {

    String clientSentence;
    PrintStream toLeClient = null;
    String capitalizedSentence;
    InputStream is = socket.getInputStream();
    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(is)); 
    int counter = 0;                                     //Troubleshooting
    int source_port = socket.getPort();


    System.out.println("Source address sending message to all users: " + socket.getInetAddress().getHostName() + " Port: "+ socket.getPort());
    int tokenCounter = 0;
    while ((clientSentence = inFromClient.readLine()) != null)                          //When client enters a sentence
    {
      
      List<String> tokenizedSentence = new ArrayList<String>();

      String newString = clientSentence.trim();
      System.out.println(clientSentence);
      String three = newString.substring(0, Math.min(newString.length(), 3));
      String four = newString.substring(0, Math.min(newString.length(), 4));

      if(three.equals("REG")){

        System.out.println("User input REG");
        StringTokenizer defaultTokenizer = new StringTokenizer(clientSentence);
        System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());
        

        

          for(int x = 0; x < defaultTokenizer.countTokens(); x++){
            tokenizedSentence.add(defaultTokenizer.nextToken());
            if(x == 1){
              String username = tokenizedSentence.get(1);
              System.out.println(username);
              registerSocket(socket, username);
            }
            // System.out.println(++tokenCounter);                                                                   
            // System.out.println("X: "+ x);
            // System.out.println(tokenizedSentence.get(x));
          }
        // registerSocket(socket, );
      }
      if(four.equals("MESG")){
       System.out.println("User input - MSG"); 
      }
      if(four.equals("PMSG")){
        System.out.println("User input - PMSG"); 
      }
      if(four.equals("EXIT")){
        System.out.println("User input - EXIT"); 
      }
      if(three.equals("ACK")){
        System.out.println("User input - ACK"); 
      }
      if(three.equals("MSG")){
        System.out.println("User input - MSG"); 
      }
      if(three.equals("ERR")){
        System.out.println("User input - ERR"); 
      }

        DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
        sendToAllTCP(clientSentence, source_port);
    }
    System.out.println("Client hung up. Host: " + socket.getInetAddress().getHostName() + ", Port: " + socket.getPort());
  }

    public void sendToAllTCP(String message, int s_port){

      PrintStream toLeClient = null;
      // System.out.println("THe FIRST SOCKET "+ socket);
      System.out.println("Socketss size: " + socketss.size());
    for (Map.Entry<Socket, String> entry : socketss.entrySet()) {
          // System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
          System.out.println(entry.getValue());
          socket = entry.getKey();
          if(socket.getPort() != s_port){
            try{
              toLeClient = new PrintStream(socket.getOutputStream());
              toLeClient.println(message);                              //Printing to all clients
            }catch(IOException e){
              System.out.println("Casught an IO exception trying to send to TCP connections");
              e.printStackTrace();
            }
          }
        }
  }
}


 
public class TCPServer{ 
  // static ArrayList<Socket> sockets = new ArrayList<Socket>();    Array List
  static Map <Socket, String> socketss = new HashMap<Socket, String>();
  

  public static void main(String args[]) throws Exception 
  {
    // EchoConnect sockets = new EchoConnect();
    int listenPort = 12346; 
    

    ServerSocket welcomeSocket = new ServerSocket(listenPort);
    TCPServer sok = new TCPServer();

    while(true) 
    { 

      Socket connectionSocket = welcomeSocket.accept();     
      sok.addSocket(connectionSocket);
      System.out.println("Client connect. Host: " + connectionSocket.getInetAddress().getHostName() + ", Port: " + connectionSocket.getPort());
      EchoConnect econnect = new EchoConnect(connectionSocket);     // Construct object to handle client
      Thread thread = new Thread(econnect);                         // Create new thread
      thread.start();                                               // Start thread
  
    } 
  } 

  public void addSocket(Socket socket){
    // this.sockets.add(socket);  Array List
    socketss.put(socket, null);
    // System.out.println("SOCKET IN addSocket FUNCTION " + socket);
  }
  public int getSocketSize(){
    // return this.sockets.size(); //ArrayList
    return socketss.size(); 
  }
  public void registerSocket(Socket register_socket , String username){
    System.out.println("Register Port: " + register_socket.getPort());
    System.out.println("Register Username with port: " + username);

       for (Map.Entry<Socket, String> entry : socketss.entrySet()) {
          // System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
          // System.out.println(entry.getValue());
          Socket socket = entry.getKey();
          // System.out.println("Key socket " + socket.getPort());
          if(socket.getPort() == register_socket.getPort()){
            System.out.println("Got the registered socket and the socket from the map");
        }
      }

  }
}  