import java.util.*;
import java.rmi.*;

public class Client{
	public static void main(String args[]){
		Scanner sc=new Scanner(System.in);
		
		try{
			String url="rmi://localhost/Server";
			
			ServerInterface si= (ServerInterface) Naming.lookup(url);	
			
			System.out.println("Eneter num1");
			double a=sc.nextDouble();
			
			System.out.println("Eneter num2");
			double b=sc.nextDouble();
			
			System.out.println("Addition: "+si.Addition(a,b));
			System.out.println("Substraction: "+si.Substraction(a,b));
			System.out.println("Multiplication: "+si.Multiplication(a,b));
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}
	}
}
