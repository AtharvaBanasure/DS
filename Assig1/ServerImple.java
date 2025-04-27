 import java.rmi.*;
 import java.rmi.server.*;
 
 public class ServerImple extends UnicastRemoteObject implements ServerInterface{
 	
 	public ServerImple()throws RemoteException {
 	
 	}
 	
 	public double Addition(double num1,double num2)throws RemoteException{
 		return num1+num2;
 	}
	public double Substraction(double num1,double num2)throws RemoteException{
		return num1-num2;
	}
	public double Multiplication(double num1,double num2)throws RemoteException{
		return num1*num2;
	}
	public double Division(double num1,double num2)throws RemoteException{
		if(num2==0){
			System.out.println("Cant divide by zero");
		}else{
			return num1/num2;
		}
		return num1/num2;
	}
	
 }
