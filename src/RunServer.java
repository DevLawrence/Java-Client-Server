import javax.swing.JFrame; 
public class RunServer {
	public static void main( String[] args ) 
	{
		 Server application = new Server(); // create server 
		 application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); 
		 application.runServer(); // run server application 
	}

}
