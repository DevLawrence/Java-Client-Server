import javax.swing.JFrame; 
public class RunClient {
	 public static void main( String[] args ) 
	 {
		 Client application; // declare client application 
		 application = new Client ("127.0.0.1");
		 application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		 application.runClient();
	 }

}
