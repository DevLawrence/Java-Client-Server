 import java.io.EOFException;
 import java.io.IOException; 
 import java.io.ObjectInputStream; 
 import java.io.ObjectOutputStream;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.awt.BorderLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities; 
 
public class Server extends JFrame {
	 private JTextField enterField; // inputs message from user
	 private JTextArea displayArea; // display information to user 
	 private ObjectOutputStream output; // output stream to client 
	 private ObjectInputStream input; // input stream from client 
	 private ServerSocket server;
	 private Socket connection;
	 
	 public Server()
	 {
		 super ("SERVER");
		 enterField = new JTextField(); // create enterField 
		 enterField.setEditable( false ); 
		 enterField.addActionListener( 
				 new ActionListener()  { // send message to client 
			 public void actionPerformed( ActionEvent event ) 
			 {
				 sendData( event.getActionCommand() ); 
				 enterField.setText( "" ); 
				 } 
			 }
				 );
		 add(enterField, BorderLayout.NORTH);
		 displayArea = new JTextArea(); 
		 add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		 
		 setSize( 300, 150 ); 
		 setVisible( true ); 
	 }
	 public void runServer() 
	 { 
		 try 
		 {
			 server = new ServerSocket( 80,100 ); 
			 while ( true ) 
			 { 
				 try { 
					 waitForConnection();
					 getStreams();
					 processConnection();
				 }//end try
		
				 catch ( EOFException eofException )  
				 {  
					 displayMessage( "\nServer terminated connection" ); 
					 } // end catch 
				finally {
					closeConnection();
				}
			 }
		 }
		 catch ( IOException ioException ) 
		 {
			 ioException.printStackTrace(); 
		 }
	 }//end method runServer
	 
	 private void waitForConnection() throws IOException 
	 {
		 displayMessage( "Waiting for connection\n" );
		 connection = server.accept();
		 displayMessage( "Connection " + " received from: " + connection.getInetAddress().getHostName());
		 
	 }
	 private void getStreams() throws IOException
	 {
		 output=new ObjectOutputStream(connection.getOutputStream());
		 output.flush();
		 input=new ObjectInputStream(connection.getInputStream());
		 displayMessage( "\nGot I/O streams\n" ); 
	 }
	 
	 private void processConnection() throws IOException 
	 {
		 String message = "Connection successful"; 
		 sendData( message ); 
		 setTextFieldEditable( true );
		 
		 do{
			 try{
				 message = ( String ) input.readObject(); 
				 displayMessage( "\n" + message ); 
			 }
			 
			 catch ( ClassNotFoundException classNotFoundException ) 
			 {
				 displayMessage( "\nUnknown object type received" ); 
			 }
		 } while ( !message.equals( "CLIENT>>> TERMINATE" ) ); 
	 }
	 
	 private void closeConnection() 
	 {
		 displayMessage( "\nTerminating connection\n" ); 
		 setTextFieldEditable( false ); // disable enterField 
		 try
		 {
			 output.close(); 
			 input.close();
			 connection.close();
		 }//end try
		 
		 catch ( IOException ioException ) 
		 {
			 ioException.printStackTrace(); 
		 }
	 }
	 private void sendData( String message ) 
	 {
		 try
		 {
			 output.writeObject( "SERVER>>> " + message ); 
			 output.flush(); // flush output to client
			 displayMessage( "\nSERVER>>> " + message ); 
		 }
		 
		 catch ( IOException ioException ) 
		 {
			 displayArea.append( "\nError writing object" ); 
		 }
	 }
	 
	 private void displayMessage( final String messageToDisplay ) 
	 {
		 SwingUtilities.invokeLater( 
				 new Runnable()
				 {
					 public void run() 
					 {
						 displayArea.append( messageToDisplay ); 
					 }
				 }
				 );
	 }
	 private void setTextFieldEditable( final boolean editable ) 
	 {
		 SwingUtilities.invokeLater( 
				 new Runnable()
				 {
					 public void run() 
					 {
						 enterField.setEditable( editable ); 
						 //output.flush();
					 }
				 }
				 );
	 }
}
					 