import java.rmi.*;

public class Server{
	public static void main(String args[]){
		try{
			ServerImple si=new ServerImple();	
			Naming.rebind("Server", si);
			System.out.println("Server Started");		
			
		}catch(Exception e){
			System.out.println("Error occured"+ e);
		}
	}
}
