
import java.io.*;

import java.io.BufferedReader;
public class SPN_HW2 {

	
	public static String[] keyStr;
	public static String[] sBoxStr;
	public static String[] permStr;
	public static int m;
	public static String[] inputStr;
	public static int a;
	public static int Round=6;
	public static String[] outputStr;
	
	public static void main(String args[]) {
        readFile();
        
        
        if(a==0) {
        	encryption();
        }
        else if (a==1){ 
        	decryption();
        }  
        
        writeFile();
        
    }
	
	public static void readFile() {
        String pathname = "input_spn.txt"; 
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
        	keyStr = br.readLine().split(" ");    
        	sBoxStr =   br.readLine().split(" ");    
        	permStr = br.readLine().split(" ");            	
        	m = Integer.parseInt(br.readLine());
        	inputStr = br.readLine().split(" ");    
        	a = Integer.parseInt(br.readLine());          	
        	       
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void writeFile() {
		
		 try {
	            File writeName = new File("output_spn.txt"); 
	            writeName.createNewFile(); 
	            try (FileWriter writer = new FileWriter(writeName);
	                 BufferedWriter out = new BufferedWriter(writer)
	            ) {
	            	out.write(StringArrayToString(keyStr)+"\r\n");
	                out.write(StringArrayToString(sBoxStr)+"\r\n"); 
	                out.write(StringArrayToString(permStr)+"\r\n"); 
	                out.write(m+"\r\n");
	                out.write(StringArrayToString(outputStr)+"\r\n");
	                out.write(a+"\r\n");
	                out.flush(); 
	                out.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	

	
	}
	
	public static void encryption() {
		int size=m/8;
		String[] tempOutput=  new String[size]; 
		tempOutput = inputStr; 		
		
		for(int i=0;i<Round ;i++)//Round
		{
			String[] tempXorResult= new String[size]; 
			String tempSBoxResult="";
			for(int j=0;j< size;j++)
			{
				tempXorResult[j]=xorString(keyStr[i*size + j],tempOutput[j]);
				tempSBoxResult += sboxOutput(tempXorResult[j]);
				if(tempSBoxResult.length()==m) {tempOutput= execPermutation(tempSBoxResult).split(" ");
				}
				
			}
		
		}
		for(int k=0;k< size;k++)
		{
			tempOutput[k]= xorString(keyStr[Round*size + k],tempOutput[k]);			
		}
		outputStr=tempOutput;
		
	}
	
	public static void decryption() {
		int size=m/8;
		String[] tempOutput=  new String[size]; 
		String str="";
		for(int k=0;k< size;k++)
		{
			tempOutput[k]= xorString(keyStr[Round*size + k],inputStr[k]);	
			str+=	tempOutput[k]; 
			if (k!=size-1) str+= " ";
			
		}
		String[] tempPermResult= new String[size]; 
		tempPermResult =str.split(" ");
		for(int i=Round-1;i>=0 ;i--)
		{
			
			String[] tempSBoxResult= new String[size];
			String[] tempXorResult= new String[size];
			tempPermResult = execPermutation(StringArrayToString(tempPermResult)).split(" ");
			for(int j=0;j< size;j++)
			{
				
					
				tempSBoxResult[j] = sboxOutput(tempPermResult[j]);
				tempXorResult[j] = xorString(keyStr[size*i+ j],tempSBoxResult[j]);
				
			}
			
			System.out.println("tempPermResult" +" : i="+ i);
			System.out.println(tempPermResult[0]);System.out.println(tempPermResult[1]);System.out.println(tempPermResult[2]);System.out.println("");
			
			System.out.println("tempSBoxResult" +" : i="+ i);
			System.out.println(tempSBoxResult[0]);System.out.println(tempSBoxResult[1]);System.out.println(tempSBoxResult[2]);System.out.println("");
			
			System.out.println("tempXorResult" +" : i="+ i);
			System.out.println(tempXorResult[0]);System.out.println(tempXorResult[1]);System.out.println(tempXorResult[2]);System.out.println("");
			tempPermResult=tempXorResult;
		
		}
		
		
		outputStr=tempPermResult;
	}
	
	


	public static String xorString(String input, String key){  		
	   	String str= Integer.toBinaryString(Integer.valueOf(input,2)^Integer.valueOf(key,2));	   	
	   	while(str.length()!=8)	{	   		
	   			str="0"+str;	   		
	    }
   		return str;
   }
	
	
	public static String sboxOutput (String inputStr){
		if(a==0 && inputStr!= null) {
		
		String[] sboxSub=new String[sBoxStr.length];
		for(int i=0;i<sBoxStr.length;i++) {sboxSub[i]=sBoxStr[i].substring(4, 8);}
		
   		char[] buf=inputStr.toCharArray();
		int n1=0, n2=0;
		if(buf[0]=='1') {n2 += 8;}
		if(buf[1]=='1') {n2 += 4;}
		if(buf[2]=='1') {n2 += 2;}
		if(buf[3]=='1') {n2 += 1;}
		
   		if(buf[4]=='1') {n1 += 8;}
		if(buf[5]=='1') {n1 += 4;}
		if(buf[6]=='1') {n1 += 2;}
		if(buf[7]=='1') {n1 += 1;}
		
		
        return sboxSub[n2]+sboxSub[n1]; 
        }
		else if (a==1 && inputStr!= null) {
			
			int s1=Integer.valueOf(inputStr.substring(0,4),2);
			int s2=Integer.valueOf(inputStr.substring(4,8),2);

			int[] sboxSub=new int[sBoxStr.length];
			int i1=0,i2=0;
			for(int i=0;i<sBoxStr.length;i++) {
				sboxSub[i]=Integer.valueOf(sBoxStr[i].substring(4, 8),2);

				if (s1 == sboxSub[i]) i1=i ;
				if (s2 == sboxSub[i]) i2=i;
			}
			
			String out1="",out2="";
			out1=i1==0?"0000":Integer.toBinaryString(i1); 
			out2=i2==0?"0000":Integer.toBinaryString(i2); 
			
			
			while(out1.length()!=4 )	{	   		
				out1="0"+out1;	   		
			}while(out2.length()!=4)	{	   		
				out2="0"+out2;	   		
			}
			return out1+out2;
			
		}
		else
		return "";
   }
	
	
	public static String  execPermutation(String inputStr) {
		int[] permInt = GetPerm();		
   		char[] buf=inputStr.replace(" ", "").toCharArray();		
   		char[] after = new char[m];
   		if(a==0) {
   			for(int i=0; i<m; i++)
   			{
   				after[permInt[i]-1] = buf[i] ;	
   			}
   		} else if (a==1) {
   			for(int i=0; i<m; i++)
   			{
   				
   				after[i] = buf[permInt[i]-1] ;	
   			}
   			
   		}   			
   		
   		String tempOutput="";
   		for(int i=0; i<m; i++)
   		{
   			if(i%8==0 && i!=0 )tempOutput += " ";
   			tempOutput += after[i] ;
   			
	   	}
	   	return tempOutput;
   } 
	

	
	
	
	public static int[] GetPerm() {
		int[] permInt=new int[permStr.length];
		for(int i=0;i<permStr.length;i++) {
		permInt[i]=Integer.valueOf(permStr[i],2);     
		}
		return permInt;
	}
	
	public static String StringArrayToString(String[] strA) {
		String str="";
		for(int i=0;i<strA.length;i++)
		{
			str+=strA[i];
			if(i!=strA.length-1) str+=" ";
		}
		return str;
	}
	
	
}
