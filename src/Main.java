import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;


public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException{

		DBApp o = new DBApp();
//		System.out.println(o.search(pagerecords, 10099, "ID", 30));
		
		
		
		
		
//		Table t = null;
//		FileInputStream fileIn = new FileInputStream("Students.ser");
//		ObjectInputStream in = new ObjectInputStream(fileIn);
//		t = (Table)in.readObject();
//		in.close();
//		fileIn.close();
//		System.out.println(t.pageNo);
//		
				Object x = 0;
		        // Creating an empty Vector 
		        if(x.getClass().toString().substring(6).equals("java.lang.Integer")){
		        	System.out.println("works");
		        }
//		        Page p = null;
//				FileInputStream fileIn1 = new FileInputStream("Students1.ser");
//			    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
//			    p = (Page)in1.readObject();
//			    in1.close();
//			    fileIn1.close();
//			    for (int i = 0; i<=p.records.size()-1;i++){
//			    	System.out.println(p.records.get(i));
//			    	Vector<Hashtable<String, Object>> asdf = new Vector();
//			    	Hashtable<String,Object> p1 = new Hashtable<String, Object>();
//			    	asdf.add(p1);
//			    	if(p.records.isEmpty())
//			    		System.out.println("this");
//			    }
//			    System.out.println(p.records.size());
////			    p.records.remove(0);
//
//			    System.out.println(t.pageNo);
			    
			    Vector<Integer> ok = new Vector<Integer>();
			    ok.add(3);
			    ok.add(5);
			    ok.add(6);
			    System.out.println(ok);

			    ok.insertElementAt(4, 0);
			    System.out.println(ok);
			    
			    
//			    FileWriter csvWriter = new FileWriter("metadata.csv");
//			    csvWriter.append("Table Name,Column Name,Column Type,Clustering Key,Max Rows, Indexed");
//			    csvWriter.flush();
//			    csvWriter.close();
			    
			    System.out.println("kkkk,False".substring(0,4));
			    File tmp = new File("Student.ser");
			    System.out.println(tmp.exists());
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
//		         Use add() method to add elements into the Vector 
			    Vector<String> vec_tor = new Vector<String>();
		        vec_tor.add("Welcome"); 
		        vec_tor.add("To"); 
		        vec_tor.add("Geeks"); 
		        vec_tor.add("4"); 
		        vec_tor.add("Geeks"); 
		  
		        // Displaying the Vector 
//		        System.out.println("Vector: " + vec_tor); 
		  
		        // Inseting element at 3rd position 
		        vec_tor.insertElementAt("Hello", 2); 
		  
		        // Inseting element at last position 
		        vec_tor.insertElementAt("World", 6); 
//		        vec_tor.insertElementAt("bye", 0); 
//
//		  
		        // Displaying the final Vector 
//		        System.out.println("The final vector is " + vec_tor); 
		        
		
//		        while(decount > 0){
//			    	System.out.println("enters decount insert");
//			    	decount--;
//			    	Page p = null;
//					FileInputStream fileIn1 = new FileInputStream(strTableName+ pagecounter +".ser");
//				    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
//				    p = (Page)in1.readObject();
//				    in1.close();
//				    fileIn1.close();
//				    
//				    int cckvalue = 0;    //value of biggest record in page
//				    String cckvalue1 = "";
//				    double cckvalue2 = 0;
////				    boolean cckvalue3 = false;
//				    Date cckvalue4 = new Date();
//				    Polygon cckvalue5 = new Polygon();
//				    Enumeration <String> thekeyss = p.records.get(p.records.size()-1).keys(); 
//					while(thekeyss.hasMoreElements()){
//						String key = thekeyss.nextElement();
//						if(key.equals(ClusterKey)){
//							if(datatype.equals("java.lang.Integer")){
//								cckvalue = (Integer)p.records.get(p.records.size()-1).get(key);
//								System.out.println("enters biggest ck value in table integer " + cckvalue);
//							}
//							if(datatype.equals("java.lang.String")){
//								cckvalue1 = (String)p.records.get(p.records.size()-1).get(key);
//							}
//							if(datatype.equals("java.lang.Double")){
//								cckvalue2 = (Double)p.records.get(p.records.size()-1).get(key);
//							}
////							if(datatype.equals("java.lang.Boolean")){
////								cckvalue3 = (Boolean)p.records.get(p.N-1).get(key);
////							}
//							if(datatype.equals("java.util.Date")){
//								cckvalue4 = (Date)p.records.get(p.records.size()-1).get(key);
//							}
//							if(datatype.equals("java.awt.Polygon")){
//								cckvalue5 = (Polygon)p.records.get(p.records.size()-1).get(key);
//							}
//						}
//						}
//					
//					int ccckvalue = 0;    //value of biggest record in page
//				    String ccckvalue1 = "";
//				    double ccckvalue2 = 0;
////				    boolean ccckvalue3 = false;
//				    Date ccckvalue4 = new Date();
//				    Polygon ccckvalue5 = new Polygon();
//				    Enumeration <String> thekey = p.records.get(0).keys(); 
//					while(thekey.hasMoreElements()){
//						String key = thekey.nextElement();
//						if(key.equals(ClusterKey)){
//							if(datatype.equals("java.lang.Integer")){
//								ccckvalue = (Integer)p.records.get(p.records.size()-1).get(key);
//								System.out.println("enters biggest ck value in table integer " + ccckvalue);
//							}
//							if(datatype.equals("java.lang.String")){
//								ccckvalue1 = (String)p.records.get(0).get(key);
//							}
//							if(datatype.equals("java.lang.Double")){
//								ccckvalue2 = (Double)p.records.get(0).get(key);
//							}
////							if(datatype.equals("java.lang.Boolean")){
////								cckvalue3 = (Boolean)p.records.get(p.N-1).get(key);
////							}
//							if(datatype.equals("java.util.Date")){
//								ccckvalue4 = (Date)p.records.get(0).get(key);
//							}
//							if(datatype.equals("java.awt.Polygon")){
//								ccckvalue5 = (Polygon)p.records.get(0).get(key);
//							}
//						}
//						}
//					
//					System.out.println("deciding the page");
//					if(ckvalue < ccckvalue || ckvalue1.compareTo(ccckvalue1) < 0|| ckvalue2 < ccckvalue2 || ckvalue4.compareTo(ccckvalue4)<0 || comparePP(ckvalue5, ccckvalue5)==-1){
//						p.records.insertElementAt(htblColNameValue, 0);
//						if(p.records.size()>maxN){
//							Hashtable<String, Object> temp = p.records.get(p.records.size()-1);
//							p.records.remove(p.records.size()-1);
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(p.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(p);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+p.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							if(t.pageNo == pagecounter){//new page error counter
//							
//							Page newp = new Page(strTableName+Integer.toString(pagecounter+1));
//							newp.records.add(temp);
//							t.pageNo++;
//
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(strTableName+Integer.toString(t.pageNo)+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(newp);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+newp.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							t.pageNo++;
//
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(t.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(t);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+t.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							return;
//						}else{
//							theShifter(++pagecounter,strTableName,temp, maxN);
//							return;
//						}
//							
//						}
//					}
//					if (ckvalue > cckvalue || ckvalue1.compareTo(cckvalue1) > 0|| ckvalue2 > cckvalue2 || ckvalue4.compareTo(cckvalue4)>0 || comparePP(ckvalue5, cckvalue5)==1) {
//						System.out.println("ck of entry is biggest but this is the last page");
//
//						if(p.records.size()<maxN){//there still is space in the page
//							System.out.println("ck of entry is biggest but this is the last page and it is empty");
//							p.records.add(htblColNameValue);
//							System.out.println("The record has been inserted in the last page.");
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(p.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(p);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+p.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							return;
//						}
//					}
//					if(!(ckvalue > cckvalue || ckvalue1.compareTo(cckvalue1) > 0 || ckvalue2 > cckvalue2 || ckvalue4.compareTo(cckvalue4)>0 || comparePP(ckvalue5, cckvalue5)==1) && p.records.size()<maxN){
//						System.out.println("enters current page");
//						int replaceIndex = 0;
//						if(ckvalue != 0){// polygon for ms2
//							replaceIndex = search(p.records, ckvalue, ClusterKey);
////							System.out.println(replaceIndex);
//						}
//						else if(!ckvalue1.equals("")){
//							replaceIndex = search(p.records, ckvalue1, ClusterKey);
//						}
//						else if(ckvalue2 != 0){
//							replaceIndex = search(p.records, ckvalue2, ClusterKey);
//						}
//						else if(ckvalue5 != null){
//							replaceIndex = search(p.records, ckvalue5, ClusterKey);
//						}
//						else{
//							replaceIndex = search(p.records, ckvalue4, ClusterKey);
//
//						}
//						p.records.insertElementAt(htblColNameValue, replaceIndex);
//						if(p.records.size()>maxN){
//							Hashtable<String, Object> temp = p.records.get(p.records.size()-1);
//							p.records.remove(p.records.size());
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(p.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(p);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+p.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							if(t.pageNo == pagecounter){//new page error counter
//							
//							Page newp = new Page(strTableName+Integer.toString(pagecounter+1));
//							newp.records.add(temp);
//							t.pageNo++;
//
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(strTableName+Integer.toString(t.pageNo)+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(newp);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+newp.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							t.pageNo++;
//
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(t.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(t);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+t.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							return;
//						}else{
//							theShifter(++pagecounter,strTableName,temp, maxN);
//							return;
//						}
//							
//						
						Vector<Hashtable<String, Integer>> V = new Vector<Hashtable<String, Integer>>();
						Hashtable<String, Integer> H = new Hashtable();
						H.put("Hi", 6);
						V.add(H);
						V.add(H);
						V.add(H);
						V.add(H);
						V.add(H);
						V.add(H);
						System.out.println(V);
						V.remove(4);
						System.out.println(V);
						Hashtable<String, Integer> H1 = new Hashtable();
						H1.put("Mario", 1);
						V.add(4, H1);
						System.out.println(V);

//						
//
//					}
//						else{
//							try {
//						        FileOutputStream fileOut =
//						        new FileOutputStream(p.name+".ser");
//						        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//						        out.writeObject(p);
//						        out.close();
//						        fileOut.close();
//						        System.out.println("Serialized data is saved in "+p.name+".ser");
//						     } catch (IOException i) {
//						        i.printStackTrace();
//						     }
//							return;
//						}
//						
//				    
//				    
//				    
//				    
//				    
//			    }
//					pagecounter++;
//		 }
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//	 }
//}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
		
		
		

	
}
