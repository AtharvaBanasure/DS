import java.util.*;
import java.io.*;
class Bully {
		static int n;
		static int priorites[]=new int[100];
		static int status[]=new int[100];
		static int cord;
		
	public static void main(String args[])throws IOException{
		try{
			Scanner sc=new Scanner(System.in);
			
			System.out.print("Enter number of process: ");
			n=sc.nextInt();
			
			for(int i=0;i<n;i++){
				System.out.print("Enter priority of process:"+(i)+"->");
				priorites[i]=sc.nextInt();
				
				
				System.out.print("Enter status [0->Dead, 1->Alive] of process:"+(i)+"->");
				status[i]=sc.nextInt();
			}
			int ele;
			while(true){
				
				System.out.print("Enter process which will initiate a election: ");
				ele=sc.nextInt();
				
				if(status[ele]==1){
					break;
				}else{
				     	System.out.println("\nAlert!! Initiated Process is Dead, Please select live Node(status==1)");
				}				
				
			}
			
			election(ele);
            		System.out.println("After electing process the final coordinator is " + cord);
		}catch(Exception e){
			System.out.println("Error occured: "+ e);
		}
	}
	
	static void election(int ele){
		cord=ele;

		for(int i=0;i<n;i++){
			if(priorites[ele]<priorites[i]){
				System.out.println("Election message is sent from "+(ele)+" to "+(i));
				if(status[i]==1){
					election(i);
				}
			}
		}
	}
}
