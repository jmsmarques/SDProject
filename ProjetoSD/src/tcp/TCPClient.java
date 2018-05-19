package tcp;

        import java.io.*;
        import java.net.*;
        import java.util.*;

/**
 * This class establishes a TCP connection to a specified server, and loops
 * sending/receiving strings to/from the server.
 * <p>
 * The main() method receives two arguments specifying the server address and
 * the listening port.
 * <p>
 * The usage is similar to the 'telnet <address> <port>' command found in most
 * operating systems, to the 'netcat <host> <port>' command found in Linux,
 * and to the 'nc <hostname> <port>' found in macOS.
 *
 * @author Raul Barbosa
 * @author Alcides Fonseca
 * @version 1.1
 */
class TCPClient {
    private static String hostname = "localhost"; //variable for hostname of the host who hostes the server
    private static int serverPort = 6000; //variable for port number of tcp server with default value
    private static String fileName = "ClientConfig.txt"; //variable with the name of the file with the server configs

    public static void main(String[] args) {
        Socket socket;
        User user = new User();
        PrintWriter outToServer;
        BufferedReader inFromServer = null;

        fileConfig(); //Loads the servers configurations from config file

        try {
            // connect to the specified address:port (default is localhost:6000)

            socket = new Socket(hostname, serverPort);

            // create streams for writing to and reading from the socket
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new PrintWriter(socket.getOutputStream(), true);

            // create a thread for reading from the keyboard and writing to the server



            new Thread() {
                public void run() {
                    Scanner keyboardScanner = new Scanner(System.in);
                    while(!socket.isClosed()) {
                        String readKeyboard = keyboardScanner.nextLine();
                        outToServer.println(readKeyboard);
                    }
                    keyboardScanner.close();
                }
            }.start();


            // the main thread loops reading from the server and writing to System.out
            String messageFromServer = null;
            @SuppressWarnings("unused")
			String readKeyboard;
            String[] message, message1, message2;
            String[] premess;
            boolean logged = false;

            System.out.println("\t\tWELLCOME TO iVOTAS");
            boolean exitCondition = false;
            while (!exitCondition) {


                if ((messageFromServer = inFromServer.readLine()) != null) {

                    System.out.println("RECEBIDO: "+messageFromServer);
                    //System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    premess = messageFromServer.split(";");

                    message = premess[0].split("\\|");

                    switch (message[0]) {
                        case "msg":
                            System.out.println("\t\t--------------============--------------\n\t\t\t" + message[1] + "\n\t\t--------------============--------------");

                            break;
                        case "type":
                            switch (message[1]) {

                                case "login":
                                    //split
                                    message1 = premess[1].split("\\|");

                                    switch (message1[0]) {
                                        case "id":

                                            user.setId(message1[1]);



                                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\t\t--== L O G I N ==--\n\n\tUser ID: " + message1[1] );
                                            if (!socket.isClosed()) {

                                               // readKeyboard = keyboardScanner.nextLine();
                                               // outToServer.println("type|login;password|" + readKeyboard + ";");
                                               // System.out.println("ENVIADO: type|login;password|" + readKeyboard + ";");

                                            }

                                            break;
                                    }

                                    break;


                                case "status":
                                    //split
                                    message1 = premess[1].split("\\|");
                                    switch (message1[0]) {

                                        case "logged":
                                            System.out.println("\tLOGGED: " + message1[1]);
                                            if (message1[1].equals("on")) {
                                                logged = true;
                                            }

                                            break;
                                    }
                                    message2 = premess[2].split("\\|");
                                    switch (message2[0]) {
                                        case "msg":
                                            System.out.println("\t\t--------------============--------------\n\t\t\t" + message2[1] + "\n\t\t--------------============--------------");

                                            if (logged) {
                                                System.out.println("\n\t\tOperacao a realizar:" + "\n\t1-Votar\n\t0-Logout");

                                                //readKeyboard = keyboardScanner.nextLine();
                                               /* switch (readKeyboard) {
                                                    case "1":
                                                        //enviar pedido de votacoes ao servidor
                                                        //outToServer.println("type|eleicoes_list;request|elections;");
                                                        //System.out.println("ENVIADO: type|eleicoes_list;request|elections;");

                                                        break;
                                                    case "0":
                                                        //LOGOUT
                                                       // outToServer.println("type|logout");
                                                       // System.out.println("ENVIADO: type|logout");

                                                }*/


                                            }
                                            break;
                                    }

                                    break;

                                case "msg":
                                    //split
                                    message1 = premess[1].split("\\|");
                                    switch (message1[0]) {
                                        case "msg":
                                            System.out.println("\t\t--------------============--------------\n\t\t\t" + message1[1] + "\n\t\t--------------============--------------");

                                            break;
                                    }

                                    break;
                                case "eleicoes_list":
                                    message1 = premess[1].split("\\|");
                                    switch (message1[0]) {
                                        case "eleicoes_count":
                                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\t\t--------Eleicoes Disponiveis--------");

                                            int num;
                                            num = Integer.parseInt(message1[1]);
                                            for (int i = 2; i <= num + 1; i++) {
                                                message2 = premess[i].split("\\|");
                                                System.out.println("\t  - " + message2[1]);


                                            }
                                            System.out.println("\t\t-----------------------------------");
                                           // readKeyboard = keyboardScanner.nextLine();
                                           // outToServer.println("type|listas_list;eleicao|" + readKeyboard + ";");
                                           // System.out.println("ENVIADO: type|listas_list;eleicao|" + readKeyboard + ";");


                                            break;
                                    }
                                    break;
                                case "listas_list":
                                    @SuppressWarnings("unused") String message4=null;
                                    String[] message3 = premess[1].split("\\|");
                                    for (String aux : premess) {
                                        message3 = aux.split("\\|");
                                        if (message3[0].equals("eleicao")) {
                                            System.out.println("\t\t------------ " + message3[1] + " ----------");
                                            message4=message3[1];
                                        }
                                    }
                                    @SuppressWarnings("unused") int num;
                                    for (String aux : premess) {
                                        message1 = aux.split("\\|");
                                        if (message1[0].equals("list_count")) {
                                            num = Integer.parseInt(message1[1]);
                                        }
                                    }
                                    for (String aux : premess) {
                                        message1 = aux.split("\\|");
                                        if (message1[0].startsWith("lista") && message1[0].endsWith("nome")) {
                                            System.out.println("\t-" + message1[1]);
                                        }

                                    }
                                   // readKeyboard = keyboardScanner.nextLine();
                                   // outToServer.println("type|vote;eleicao|"+message4+";vote|"+readKeyboard+";");
                                   // System.out.println("ENVIADO: type|vote;eleicao|"+message4+";vote|"+readKeyboard+";");

                                    break;

                            }


                            break;

                    }


                }


            }








/*



						for (String stt : premess) {
							message = stt.split("\\|");
							switch (message[0]) {

								case "id":
									user.setId(message[1]);
									System.out.print("\t\t--== L O G I N ==--\n\n\tUser ID: " + message[1] + "\n\tPassword: ");
									if (!socket.isClosed()) {
										readKeyboard = keyboardScanner.nextLine();
										outToServer.println("type|login;password|" + readKeyboard + ";");
									}
									break;
								//keyboardScanner.close();


								case "logged":
									System.out.println("\tLOGGED: " + message[1]);
									if (message[1].equals("on")) {
										logged = true;
									}
									break;
								case "msg":
									System.out.println("\t\t--------------============--------------\n\t\t\t" + message[1] + "\n\t\t--------------============--------------");
									if (logged) {
										System.out.println("\n\t\tOperacao a realizar:" + "\n\t1-Votar\n\t9-Logout");
										readKeyboard = keyboardScanner.nextLine();
										switch (readKeyboard) {
											case "1":
												//enviar pedido de votacoes ao servidor
												outToServer.println("type|item_list;request|elections;");

												break;
											case "0":
												//LOGOUT
												outToServer.println("type|login;password| ");

										}


									}
								case "item_list":

									break;

							}

////////////////////

						}
						*/
        }


        catch(
                IOException e)

        {
            System.out.println(e.getMessage());
        } finally

        {
            try {
                inFromServer.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Loads the servers configurations from config file
     */
    private static void fileConfig() {
        //Start reading from config file
        try {
            BufferedReader fileRead = new BufferedReader(new FileReader(fileName));
            String parts[]; //variable used to split the string read from the config file
            String read; //variable to store the string read from the config file
            //read file and load values
            while ((read = fileRead.readLine()) != null) {
                parts = read.split("="); //split the string read on the = sign
                if (parts[0].equals("hostname")) {
                    hostname = parts[1];
                } else if (parts[0].equals("serverPort")) {
                    serverPort = Integer.parseInt(parts[1]);
                } else {
                    System.out.println("File is not correctly written.\nCreating a new file with default values");
                    fileRead.close();
                    throw new FileNotFoundException();
                }
            }
            fileRead.close();

        } catch (FileNotFoundException e) {//if file doesnt exist or cant be opened
            System.out.println("Configuration file non existant.\nNew one created with default values");
            try {
                //create new file and fill it with default values
                PrintWriter writer = new PrintWriter(fileName, "UTF-8");
                writer.println("serverPort=6000");
                writer.println("hostname=localhost");
                writer.close();
            } catch (Exception e1) {
                System.out.println("Could not create new file, default values still apply");
            }
        } catch (IOException e) {
            System.out.println("Error reading from file, setting values to default and continuing");
        }
        //End of reading from config file
    }
}


class User {
    private String id = "0";
    private String password=null;

    public synchronized String getId() {
        return this.id;
    }

    public synchronized void setId(String id) {
        this.id = id;
        System.out.println("ID FOUND!");
    }
    public synchronized String getPassword() {
        return this.password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
        System.out.println("Password saved!");
    }
}





