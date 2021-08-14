
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
public class milestone1 {
	static Object[] Memory = new Object[1024];
	static int currentMemoryLocation=0;
	static int processID=0;
	static int PCBPointer=900;
	static Queue<Object>readyQueue= new LinkedList<>();
	static HashMap<String,Object>h=new HashMap();
    static 	HashMap<Object,Object>quanta=new HashMap();
    static 	HashMap<Object,Object>quantaFinal=new HashMap();
	
	
	public static void createPCB(String input) throws IOException {
		BufferedReader Reader = new BufferedReader(new FileReader(input));
		ArrayList variables=new ArrayList();
		String instr="";

		ArrayList Instructions= new ArrayList();
		while ((instr = Reader.readLine()) != null) {
			String[] data = instr.split(" ");
			Instructions.add(instr);

			if(data[0].equals("assign")) {
				if(!(variables.contains(data[1]))){
					variables.add(data[1]);
				}
			}

		}
		processID++;

		String processState="ready";
		int MemoryBoundMin=currentMemoryLocation;
		int MemoryBoundMax=Instructions.size() + currentMemoryLocation-1;
		int programCounter=MemoryBoundMin;
		
		readyQueue.add(processID);
		quanta.put(processID, 0);
		quantaFinal.put(processID, "");

		for( int i =0; i<Instructions.size() ;i++) {
			Memory[currentMemoryLocation]=Instructions.get(i);
			System.out.println("Index: "+ (currentMemoryLocation) + ", Memory: "+ Memory[currentMemoryLocation]  );

			currentMemoryLocation++;
			
		}
		for( int i =0; i<variables.size() ;i++) {
			Object[] var = new Object[2];
			var[0]=variables.get(i);
			
			//n.put((String) variables.get(i), null);
			Memory[currentMemoryLocation]=var;
//			n.clear();
			System.out.println("Index: "+ (currentMemoryLocation) + ", Memory: "+ Arrays.toString((Object[] )Memory[currentMemoryLocation])  );
			currentMemoryLocation++;
			//currentMemoryLocation++;
		}
		int variableMin=MemoryBoundMax+1;
		int variableMax=currentMemoryLocation-1;
		System.out.println(" ");
		Memory[PCBPointer++]=processID;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		
		Memory[PCBPointer++]=MemoryBoundMin;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		Memory[PCBPointer++]=MemoryBoundMax;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		Memory[PCBPointer++]=variableMin;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		Memory[PCBPointer++]=variableMax;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		Memory[PCBPointer++]=programCounter;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
		Memory[PCBPointer++]=processState;
		System.out.println("Index: "+ (PCBPointer-1) + ", Memory: "+ Memory[PCBPointer-1]  );
System.out.println("----------------------------------------------------------");
		//System.out.println(Arrays.toString(Memory));

	}
	

public static void Scheduling() throws IOException {
	int current = (int) readyQueue.poll();
	int instMin=0;
	int instMax=0;
	int varMin=0;
	int varMax=0;
	int PC=0;
	
	String state="";
	System.out.println(" ");
	System.out.println(" ");
	System.out.println("             CURRENTLY EXECUTING PROGRAM: " + current);
	System.out.println(" ");
	 quanta.put(current, (int)quanta.get(current)+1);
	 for (int i=900;i<Memory.length;i++){
		 System.out.println("Index: "+ (i) + ", Memory: "+ Memory[i] + " , Action: Reading" );
			
		
		 if(current==(int)Memory[i]) {
			 instMin=(int)Memory[i+1];
			 System.out.println("Index: "+ (i+1) + ", Memory: "+ Memory[i+1] + " , Action: Reading");
			 instMax=(int)Memory[i+2];
			 System.out.println("Index: "+ (i+2) + ", Memory: "+ Memory[i+2] + " , Action: Reading" );
			 varMin=(int)Memory[i+3];
			 System.out.println("Index: "+ (i+3) + ", Memory: "+ Memory[i+3]  + " , Action: Reading");
			 varMax=(int)Memory[i+4];
			 System.out.println("Index: "+ (i+4) + ", Memory: "+ Memory[i+4] + " , Action: Reading" );
			 PC=(int)Memory[i+5];
			 System.out.println("Index: "+ (i+5) + ", Memory: "+ Memory[i+5] + " , Action: Reading" );
			 state=Memory[i+6].toString();
			 System.out.println("Index: "+ (i+6) + ", Memory: "+ Memory[i+6] + " , Action: Reading");
			 
				 h.clear();
			
				 for(int j= varMin;j<=varMax;j++) {
					 Object[] temp = (Object[]) Memory[j];
					 System.out.println("Index: "+ (j) + ", Memory: "+ Arrays.toString((Object[]) Memory[j]) + " , Action: Reading" );

					 if(!(temp[1]==null)) {
						 
						 h.put((String) temp[0], temp[1]);	 
					 }
				 }
				 state = "Running";
				 Memory[i+6]=state;
				 System.out.println("Index: "+ (i+6) + ", Memory: "+ Memory[i+6] + " , Action: Reading" );
				 System.out.println("Index: "+ (PC) + ", Memory: "+ Memory[PC] + " , Action: Reading" );
				 interpret(Memory[PC].toString());
				 boolean fullQuantum = false;
				 if(PC<=instMax) {
					 
					 if(PC+1<=instMax) {
					PC++;
					 System.out.println("Index: "+ (PC) + ", Memory: "+ Memory[PC] + " , Action: Reading");
					 interpret(Memory[PC].toString());
					 fullQuantum = true;
					 
					 }
					  if(!(PC==instMax)) {
						readyQueue.add(current);
						
						 state = "Not Running";
						 Memory[i+6]=state;
						 System.out.println("Index: "+ (i+6) + ", Memory: "+ Memory[i+6] + " , Action: Writting" );
						 Memory[i+5]=PC+1;
						 System.out.println("Index: "+ (i+5) + ", Memory: "+ Memory[i+5]  + " , Action: Writting");
						 
						 
					  }
					  else {
							 state = "finished";
							 
							 Memory[i+6]=state;
							 System.out.println("Index: "+ (i+6) + ", Memory: "+ Memory[i+6]  + " , Action: Writting");
							 Memory[i+5]=PC;
							
							 
							 System.out.println("Index: "+ (i+5) + ", Memory: "+ Memory[i+5]  + " , Action: Writting");
							 System.out.println();
							 System.out.println();
							

							
				 }
				 
					 
				 }
				 
				 
				 
				 System.out.println();
				 memoryVariables(h,varMin,varMax);
				 System.out.println();
				 int finalQuanta = (fullQuantum?2:1);
				 quantaFinal.put(current, (String)quantaFinal.get(current)+finalQuanta+", ");
				 
				 System.out.println("PROGRAM WITH ID " + current+" RAN FOR "+ finalQuanta + " QUANTA");
				 if(state.equals("finished")) {
					 String s =(String) quantaFinal.get(current);
					 System.out.println("PROGRAM WITH ID " + current+" FINISHED EXECUTING AFTER  "+ quanta.get(current)+ " TIME SLICES ");
				 	 System.out.println("SEQUENCE OF QUANTA FOR PROGRAM "+current+" IS: " +s.substring(0,s.length() -2) +"\n");
				 }
				 System.out.println("---------------------------------------------------------------");
			 
				 
			 break; 
			 }
		 
		 else {
			 i+=6; 
		 }
	 
	}
	// System.out.println(h + " ---------- eh");
	 
	
		 
//		 System.out.println("ba3daha");
		// System.out.println(h+"-------- didi");
		
//		 System.out.println(Arrays.toString(Memory)+"------------> Memory");
//		 System.out.println(state+"---------> akher State");
//		 System.out.println(readyQueue+"-------------> ready Queue");
//	 
//		 System.out.println("---------------------------------------------------------------");
//	 
}

//	 
	
//	 System.out.println(instMin);
//	 System.out.println(instMax);
//	 System.out.println(varMin);
//	 System.out.println(varMax);
//	 System.out.println(PC);
//	 System.out.println(state);
//	 System.out.println("-------------------------");

	
		

	
	
	
	
	public static void memoryVariables(HashMap<String, Object> h, int min , int max) {
		for(int i=min;i<=max;i++) {
			 Object[] temp = (Object[]) Memory[i];
			 System.out.println("Index: "+ (i) + ", Memory: "+Arrays.toString((Object[]) Memory[i] ) + " , Action: Reading");
			if (h.containsKey(temp[0].toString())) {
				
				temp[1]=h.get(temp[0]);
				Memory[i]=temp;
				System.out.println("Index: "+ (i) + ", Memory: "+ Arrays.toString((Object[]) Memory[i]) + " , Action: Writting" );
				
			}
		}
		
	}
		
	public static  void  add(String a , String b ) {
		int x=Integer.parseInt(readMemory(a).toString());
		int y=Integer.parseInt(readMemory(b).toString());
        int temp=x+y;
        writeMemory(a,temp);
     
	}
	
	public static void writeFile(String name, Object data) throws IOException {
		String fileName = "src/" + name+".txt" ;
		FileWriter myWriter = new FileWriter(fileName);
		myWriter.write( data+"");
        myWriter.close();
		
	}
	
	public static Object readMemory(String variableName) {
		return h.get(variableName);
		}
	public static void writeMemory(String variableName,Object value) {
		
		  h.put(variableName, value.toString());
		  
		}
	
	
	public static  String readFile(String name)  {
		String fileName = "src/" + name+".txt";
		try {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
		    stringBuilder.append(line);
		    stringBuilder.append(" ");
		}
	
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		reader.close();
		return stringBuilder.toString();
		}
		catch(IOException e) {
		     return"FILE NOT FOUND";
			}
}	
    public static void interpret( String x ) throws IOException{
	String[] code = x.split(" ");
	int i = 0;
	while (i<code.length){
		switch(code[i]){
			case ("print"):
				print(code[i+1]);
				i=i+2;
				break;
			case ("writeFile"):
				 writeFile( readMemory(code[i+1]).toString() , readMemory(code[i+2]).toString() );
				i=i+3;
				break;
			case("add"):
				add( code[i+1] , code[i+2] );
				i=i+3;
				break;
			case("assign"):
				if(code[i+2].equals("readFile")){
					String z = readFile( readMemory(code[i+3]).toString() );
					assign( code[i+1] , z);
					
					i=i+4;
				}
				else{	
					if(code[i+2].equals("input")){
						String y = input();
						assign( code[i+1] , y);
						
						i=i+3;
			    }else{
					assign( code[i+1] , code[i+2] );
					i=i+3;
				}}
				break;
			default:
				break;
			}	
		}
	}
		
    public static void print( String x ){
	if( (readMemory(x)==null)){
		System.out.println(x);
	}
	else {
	     String i = readMemory(x).toString();
		System.out.println(i);
	}
}

    public static String input() {
	Scanner sc= new Scanner(System.in);
	String str= sc.nextLine(); //reads string.
	return str;
}

    public static  void assign(String name, Object value) {
    	if (value.toString().equals("FILE NOT FOUND")) {
    		System.out.println("FILE NOT FOUND");
    		return;
    	}
    	
      String type= ( value.getClass()).toString();
      if (type.equals("class java.lang.String"))
    	  writeMemory(name,value);
      else
    	  writeMemory(name,value);

    	
      }


    public static void main(String[] args) throws IOException {
    	createPCB("src/Program 1.txt");
    	createPCB("src/Program 2.txt");
    	createPCB("src/Program 3.txt");

    while(!readyQueue.isEmpty()) {
   		Scheduling();
   	}
    	
    	
    	
    	//System.out.println(h+"--------->hash");
//	MileStone1 x = new MileStone1();
//    String p1 = x.readFile("Program 1");
//    x.interpret(p1);
//    String p2 = x.readFile("Program 2");
//    x.interpret(p2);
//    String p3 = x.readFile("Program 3");
//    x.interpret(p3);

//    	String x= "AND R1 2(R14)";
//    	String [] s=x.split(" ");
//    	String[] b=s[2].split("()");
//    	System.out.println(Arrays.toString(b));


}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	



