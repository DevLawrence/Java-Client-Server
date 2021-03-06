 import java.io.EOFException; 
 import java.io.IOException; 
 import java.io.ObjectInputStream; 
 import java.io.ObjectOutputStream; 
 import java.net.Socket;
 import java.awt.BorderLayout; 
 import java.awt.event.ActionEvent; 
 import java.awt.event.ActionListener; 
 import javax.swing.JFrame; 
 import javax.swing.JScrollPane; 
 import javax.swing.JTextArea; 
 import javax.swing.JTextField; 
 import javax.swing.SwingUtilities; 
 
public class Client extends JFrame {
	
	 private JTextField enterField;
	 private JTextArea displayArea;
	 private ObjectOutputStream output;
	 private ObjectInputStream input;
	 private String message = "";
	
	 private Socket client;

public Client( String host ) 
{
	super("CLIENT");
	
	 enterField = new JTextField(); 
	 enterField.setEditable( false ); 
	 enterField.addActionListener( 
			 new ActionListener() 
			 {
				 public void actionPerformed( ActionEvent event ) 
				 {
					 sendData( event.getActionCommand() ); 
					 enterField.setText( "" ); 
				 }
			 }
			 );
	 add( enterField, BorderLayout.NORTH ); 
	 displayArea = new JTextArea(); 
	 add( new JScrollPane( displayArea ), BorderLayout.CENTER ); 
	 setSize( 300, 150 ); 
	 setVisible( true ); 
}

public void runClient()
{
	try
	{
		connectToServer(); // create a Socket to make connection
		getStreams(); // get the input and output streams 
		processConnection(); 
	}
	 catch ( EOFException eofException ) 
	{
		 displayMessage( "\nClient terminated connection" ); 
	}
	 catch ( IOException ioException ) 
	{
		 ioException.printStackTrace(); 
	}
	finally 
	{
		 closeConnection(); 
	}
}
private void connectToServer() throws IOException 
{
	displayMessage( "Attempting connection\n" );
	client = new Socket("127.0.0.1",8080);//(address,port) // updated port from 80 to 8080
	 displayMessage( "Connected to: " + client.getInetAddress().getHostName());
}

private void getStreams() throws IOException 
{
	output=new ObjectOutputStream(client.getOutputStream());
	output.flush();
	input = new ObjectInputStream( client.getInputStream() ); 
	displayMessage( "\n Got I/O streams \n" ); 
}


private void processConnection() throws IOException 
{
	 setTextFieldEditable( true ); 
	 do
	 {
		 try
		 {
			 message = ( String ) input.readObject(); // read new message
			 displayMessage( "\n" + message ); 
		 }
		 
		 catch ( ClassNotFoundException classNotFoundException ) 
		 {
			 displayMessage( "\n Unknown object type received" ); 
		 }
	 } while ( !message.equals( "SERVER >>> TERMINATE" ) ); 
}

private void closeConnection() 
{
	 displayMessage( "\n Closing connection" ); 
	 setTextFieldEditable( false ); // disable enterField
try
{
	output.close();
	input.close();
	client.close();
}
catch ( IOException ioException ) 
{
	ioException.printStackTrace(); 
}
}
private void sendData( String message ) 
{
	try
	{
		output.writeObject( "CLIENT >>> " + message ); 
		output.flush(); 
		displayMessage( "\n CLIENT >>> " + message ); 
	}
	
	 catch ( IOException ioException ) 
	{
		 displayArea.append( "\n Error writing object" ); 
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
				 }
			 }
			 
			 );
}
}

	 

				 
	

