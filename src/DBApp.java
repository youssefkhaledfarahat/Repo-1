import java.awt.Dimension;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.lang.Object;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;

//import DB.Tuple;


public class DBApp implements java.io.Serializable {
	Vector<String> TableNames;
	static Vector<String> tabels = new Vector<String>();	
	public DBApp(){
//		TableNames.add();
	}
	public void init() {
	}
//Check if table exists 
	public boolean checkTable(String strTableName){
		
		File Tmp = new File(strTableName+".ser");
		return Tmp.exists();
	}

//write and read objects online Check
	public static void writeObject(Object x,String path) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
		oos.writeObject(x);
		oos.close();
	}
	public static Object readObject(String path) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
	


// begin of Create Index
	
	public void createBIndex(String strTableName,String strColName) throws DBAppException, IOException, ClassNotFoundException 
	{
		boolean check=checkTable(strTableName);
		boolean checkcol=checkColName(strTableName,strColName);
		boolean checkindex=checkColIndexed(strTableName,strColName);
		if(check){
			if(checkcol){
				if(checkindex){
					throw new DBAppException("column already indexed");
				}
				else{
					String row;
					ArrayList rowDataCellTname = new ArrayList();
					ArrayList rowDataCellCname = new ArrayList();
					ArrayList rowData = new ArrayList();
					BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
					while ((row = csvReader.readLine()) != null) {
					    String[] data = row.split("\n");
					    rowData.add(data[0]);
					    String[] data2 = row.split(",");	
					    rowDataCellTname.add(data2[0]);
					    rowDataCellCname.add(data2[1]);
					    }
					csvReader.close();
					int PC = 0;
					for(PC = 0; PC < rowDataCellTname.size();PC++){
						if(rowDataCellTname.get(PC).equals(strTableName) && rowDataCellCname.get(PC).equals(strColName)){
							break;
						}
					}
					String k =(String) rowData.get(PC);
					k = k.substring(0, (k.length()-6))+",True";
					rowData.remove(PC);
					rowData.add(PC, k);
					FileWriter csvWriter = new FileWriter("metadata.csv");
					for(int j = 0; j<(rowData.size()); j++){
						csvWriter.append(rowData.get(j)+"\n");
						
					}
					csvWriter.flush();
					csvWriter.close();
					
					BPTree b = new BPTree(15, strTableName, strColName);
					Table t = null;
					FileInputStream fileIn = new FileInputStream(strTableName +".ser");
				    ObjectInputStream in = new ObjectInputStream(fileIn);
				    t = (Table)in.readObject();
				    in.close();
				    fileIn.close();
				for(int i=1;i<=t.pageNo;i++){
					Page p = null;
					FileInputStream fileInP = new FileInputStream(strTableName+i +".ser");
				    ObjectInputStream inP = new ObjectInputStream(fileInP);
				    p = (Page)inP.readObject();
				    inP.close();
				    fileInP.close();
				    for(int j=0;j<p.records.size();j++){
				    	b.insert((Comparable)p.records.get(j).get(strColName), strTableName+Integer.toString(i) , i);
				    }
				}
				}
			}
			else{
				throw new DBAppException("column does not exist try again");
			}
			
		}
		else{
			throw new DBAppException("Table does not exist try again");
		}
	}
	public void createRTreeIndex(String strTableName,String strColName) throws DBAppException, IOException, ClassNotFoundException 
		
		{
			boolean check=checkTable(strTableName);
			boolean checkcol=checkColName(strTableName,strColName);
			boolean checkindex=checkColIndexed(strTableName,strColName);
			if(check){
				if(checkcol){
					if(checkindex){
						throw new DBAppException("column already indexed");
					}
					else{
						String row;
						ArrayList rowDataCellTname = new ArrayList();
						ArrayList rowDataCellCname = new ArrayList();
						ArrayList rowData = new ArrayList();
						BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
						while ((row = csvReader.readLine()) != null) {
						    String[] data = row.split("\n");
						    rowData.add(data[0]);
						    String[] data2 = row.split(",");	
						    rowDataCellTname.add(data2[0]);
						    rowDataCellCname.add(data2[1]);
						    }
						csvReader.close();
						int PC = 0;
						for(PC = 0; PC < rowDataCellTname.size();PC++){
							if(rowDataCellTname.get(PC).equals(strTableName) && rowDataCellCname.get(PC).equals(strColName)){
								break;
							}
						}
						String k =(String) rowData.get(PC);
						k = k.substring(0, (k.length()-6))+"True";
						rowData.remove(PC);
						rowData.add(PC, k);
						FileWriter csvWriter = new FileWriter("metadata.csv");
						for(int j = 0; j<(rowData.size()); j++){
							csvWriter.append(rowData.get(j)+"\n");
							
						}
						csvWriter.flush();
						csvWriter.close();
						Rtree r = new Rtree(15, strTableName, strColName);
						Table t = null;
						FileInputStream fileIn = new FileInputStream(strTableName +".ser");
					    ObjectInputStream in = new ObjectInputStream(fileIn);
					    t = (Table)in.readObject();
					    in.close();
					    fileIn.close();
					for(int i=1;i<=t.pageNo;i++){
						Page p = null;
						FileInputStream fileInP = new FileInputStream(strTableName+i +".ser");
					    ObjectInputStream inP = new ObjectInputStream(fileInP);
					    p = (Page)inP.readObject();
					    inP.close();
					    fileInP.close();
					    for(int j=0;j<p.records.size();j++){
					    	r.insert((Comparable)p.records.get(j).get(strColName), strTableName+Integer.toString(i) , i);
					    }
					}
					}
				}
				else{
					throw new DBAppException("column does not exist try again");
				}
				
			}
			else{
				throw new DBAppException("Table does not exist try again");
			}
		}
		
	
	
	
	
	
	public static void createTable(String strTableName, String strClusteringKeyColumn, Hashtable<String, String> htblColNameType, int maxRowNo) throws IOException{
		////////////Table creation + insertion of table info into metadata.csv file
		//Vector<String> tabels = new Vector<String>();
		tabels.add(strTableName);
	Table t = new Table(strTableName, strClusteringKeyColumn, htblColNameType);
	String row;
	ArrayList rowData = new ArrayList();
	BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
	while ((row = csvReader.readLine()) != null) {
	    String[] data = row.split("\n");
	    rowData.add(data[0]);
	    }
	csvReader.close();
	FileWriter csvWriter = new FileWriter("metadata.csv");
	for(int j = 0; j<(rowData.size()); j++){
		csvWriter.append(rowData.get(j)+"\n");
		
	}
	Enumeration <String> keys = htblColNameType.keys();
	while(keys.hasMoreElements()){
		String key = keys.nextElement();
		if(key==(strClusteringKeyColumn)){
			csvWriter.append(strTableName+ "," + key + "," + htblColNameType.get(key)+ "," + "True" +","+ maxRowNo+","+"False"+ "\n");
		}
		else{
			csvWriter.append(strTableName);
			csvWriter.append(",");
			csvWriter.append(key);
			csvWriter.append( ",");
			csvWriter.append(htblColNameType.get(key));
			csvWriter.append(",");
			csvWriter.append( "False"+","+ maxRowNo+","+"False"+"\n");

		}
		
	}
//	csvWriter.append(strTableName);
//	csvWriter.append(",");
//	csvWriter.append("TouchDate");
//	csvWriter.append( ",");
//	csvWriter.append("java.util.Date");
//	csvWriter.append(",");
//	csvWriter.append( "False\n");
	csvWriter.flush();
	csvWriter.close();
	try {
        FileOutputStream fileOut =
        new FileOutputStream(strTableName+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(t);
        out.close();
        fileOut.close();
        System.out.printf("Serialized data is saved in "+strTableName+".ser");
     } catch (IOException i) {
        i.printStackTrace();
     }
	////////////////////////////////Page creation + serialization into .ser file
	Page p = new Page(strTableName+"1");
	try {
        FileOutputStream fileOut =
        new FileOutputStream(strTableName+"1.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(p);
        out.close();
        fileOut.close();
        System.out.printf("Serialized data is saved in "+strTableName+"1.ser");
     } catch (IOException i) {
        i.printStackTrace();
     }
	
}
	
	
	public static boolean checkColName(String strTableName, String strColName) throws IOException{
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellType = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellCname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		ArrayList rowDataCellCK = new ArrayList();
		Hashtable<String, Object> empty = new Hashtable();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
		    rowDataCellCname.add(data2[1]);
		    rowDataCellType.add(data2[2]);
		    rowDataCellCK.add(data2[3]);
			rowDataCellmaxN.add(data2[4]);


		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}
		Vector<String> typeData = new Vector<String>();
		Vector<String> nameData = new Vector<String>();
		Vector<String> CKData = new Vector<String>();
		Vector<String> indexData = new Vector<String>();
		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				typeData.add((String)rowDataCellType.get(j));
				nameData.add((String)rowDataCellCname.get(j));
				CKData.add((String)rowDataCellCK.get(j));
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

			}
		}
		for(int k = 0; k < nameData.size(); k++){
			if(nameData.get(k).equals(strColName)){
				return true;
			}
		}
		return false;
	}
	public static int getmaxN(String strTableName) throws IOException{
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		Hashtable<String, Object> empty = new Hashtable();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
			rowDataCellmaxN.add(data2[4]);


		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}

		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

			}
		}
		return maxN;
	}
	
	public static boolean checkColIndexed(String strTableName, String strColName) throws IOException{
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellType = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellCname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		ArrayList rowDataCellCK = new ArrayList();
		ArrayList rowDataIndex = new ArrayList();
		Hashtable<String, Object> empty = new Hashtable();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
		    rowDataCellCname.add(data2[1]);
		    rowDataCellType.add(data2[2]);
		    rowDataCellCK.add(data2[3]);
			rowDataCellmaxN.add(data2[4]);
			rowDataIndex.add(data2[5]);

		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}
		Vector<String> typeData = new Vector<String>();
		Vector<String> nameData = new Vector<String>();
		Vector<String> CKData = new Vector<String>();
		Vector<String> indexData = new Vector<String>();
		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				typeData.add((String)rowDataCellType.get(j));
				nameData.add((String)rowDataCellCname.get(j));
				CKData.add((String)rowDataCellCK.get(j));
				indexData.add((String)rowDataIndex.get(j));
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

			}
		}
		int indexLoc = 0;
		for(int k = 0; k < nameData.size(); k++){
			if(nameData.get(k) == strColName){
				indexLoc = k;
			}
		}
		if(indexData.get(indexLoc).equals("True")){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
public static void insertIntoTable(String strTableName,Hashtable<String,Object> htblColNameValue)throws DBAppException, IOException, ClassNotFoundException {
	if(!htblColNameValue.isEmpty()){
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellType = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellCname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		ArrayList rowDataCellCK = new ArrayList();
		ArrayList rowDataIndex = new ArrayList();
		Hashtable<String, Object> empty = new Hashtable();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
		    rowDataCellCname.add(data2[1]);
		    rowDataCellType.add(data2[2]);
		    rowDataCellCK.add(data2[3]);
			rowDataCellmaxN.add(data2[4]);
			rowDataIndex.add(data2[5]);

		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}
		Vector<String> typeData = new Vector<String>();
		Vector<String> nameData = new Vector<String>();
		Vector<String> CKData = new Vector<String>();
		Vector<String> indexData = new Vector<String>();
		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				typeData.add((String)rowDataCellType.get(j));
				nameData.add((String)rowDataCellCname.get(j));
				CKData.add((String)rowDataCellCK.get(j));
				indexData.add((String)rowDataIndex.get(j));
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

			}
		}
		int vectorCounter = 0;
		String ClusterKey = "none";
		String datatype = "none";
		 Enumeration <String> keys = htblColNameValue.keys();
		 while(keys.hasMoreElements()){
			 String key = keys.nextElement();
			 String dataType = (htblColNameValue.get(key).getClass().toString().substring(6));
//			 System.out.println(dataType);
//			 System.out.println(nameData.get(vectorCounter));
//			 System.out.println(typeData.get(vectorCounter));
//			 System.out.println(key);
			 if(CKData.get(vectorCounter).equals("True")){
				 ClusterKey = nameData.get(vectorCounter);
				 datatype = typeData.get(vectorCounter);
			 }
			 System.out.println(ClusterKey + "  " + maxN);
			 if(!key.equals(nameData.get(vectorCounter)) || !(typeData.get(vectorCounter)).equals(dataType)){
				 correctFormat = false;
				 break;
			 }
			 else{
			 vectorCounter++;
			 System.out.println("good so far");
			 }
			 
		 }
		 
		 if(!correctFormat){
			 System.out.println("wrong insertion format");
			 return;
		 }
		 else{
			 if(vectorCounter < nameData.size()){
				 System.out.println("record entered is incomplete");
				 return;
			 }
			 
			 else{
			//PHASE 2 CORRECT FORMAT
				 System.out.println("right format good job");
				 	if(getIndexedCol(strTableName).equals("Error") || ((!getIndexedCol(strTableName).equals("Error")) && !(getIndexedCol(strTableName).equals(ClusterKey)))){
				    Date date= new Date();
				    htblColNameValue.put("TouchDate", date);
			 if(insertCheckerEmpty(strTableName)){ //the table is empty
				 	Page p = null;
					FileInputStream fileIn1 = new FileInputStream(strTableName +"1.ser");
				    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
				    p = (Page)in1.readObject();
				    in1.close();
				    fileIn1.close();
			         
				    p.records.add(htblColNameValue);
				    FileOutputStream fileOut =
				            new FileOutputStream(strTableName+"1.ser");
				            ObjectOutputStream out = new ObjectOutputStream(fileOut);
				            out.writeObject(p);
				            out.close();
				            fileOut.close();
				            System.out.println("The record has been inserted in page 1 of table " + strTableName);
				            if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
				            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
								tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), strTableName+"1", 1);
				            }
				            else{
				            BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
							tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), strTableName+"1", 1);
				            }
							return;
				    
			 }
			 
			 else if (!insertCheckerEmpty(strTableName)){ // the table is
			    	System.out.println("enters the not empty page checker");

				 Table t = null;
					
					FileInputStream fileIn = new FileInputStream(strTableName +".ser");
				    ObjectInputStream in = new ObjectInputStream(fileIn);
				    t = (Table)in.readObject();
				    in.close();
				    fileIn.close();
				    int decount = t.pageNo;
				    int pagecounter = 1;
				    
				    int ckvalue = 0;    //value of clusterkey from inserted record
				    String ckvalue1 = "";
				    double ckvalue2 = 0;
				    String pattern = "yyyy-MM-dd";
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				    String ckvalue3 = simpleDateFormat.format(new Date());
				    ckvalue3 = "1999-10-10";
				    Polygon ckvalue4 = new Polygon();
				    Enumeration <String> thekey = htblColNameValue.keys(); 
					while(thekey.hasMoreElements()){
						String key = thekey.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								ckvalue = (Integer)htblColNameValue.get(key);
								System.out.println("integer ckvalue " + ckvalue);
							}
							if(datatype.equals("java.lang.String")){
								ckvalue1 = (String)htblColNameValue.get(key);
							}
							if(datatype.equals("java.lang.Double")){
								ckvalue2 = (Double)htblColNameValue.get(key);
							}
							if(datatype.equals("java.util.Date")){
								ckvalue3 = (String)htblColNameValue.get(key);
							}
							if(datatype.equals("java.awt.Polygon")){
								ckvalue4 = (Polygon)htblColNameValue.get(key);
							}
						}
						}//now i have the ckvalue of the entered htblColNameValue stored in whichever range from 0 to 5 not including 3
				for (int i = decount; i>0; i--){
					 Page p = null;
						
						FileInputStream fileInP = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
					    ObjectInputStream inP = new ObjectInputStream(fileInP);
					    p = (Page)inP.readObject();
					    inP.close();
					    fileInP.close();
				
					int ckvalueS = 0;    //value of clusterkey of smallest in the page
				    String ckvalue1S = "";
				    double ckvalue2S = 0;
				    String ckvalue3S = simpleDateFormat.format(new Date());
				    ckvalue3S = "1999-10-10";				    
				    Polygon ckvalue4S = new Polygon();
				    Enumeration <String> thekeyS = p.records.get(0).keys(); 
					while(thekeyS.hasMoreElements()){
						String key = thekeyS.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								ckvalueS = (Integer)p.records.get(0).get(key);
								System.out.println("integer ckvalue smallest in the page " + ckvalueS);
							}
							if(datatype.equals("java.lang.String")){
								ckvalue1S = (String)p.records.get(0).get(key);
							}
							if(datatype.equals("java.lang.Double")){
								ckvalue2S = (Double)p.records.get(0).get(key);
							}
							if(datatype.equals("java.util.Date")){
								ckvalue3S = (String)p.records.get(0).get(key);
							}
							if(datatype.equals("java.awt.Polygon")){
								ckvalue4S = (Polygon)p.records.get(0).get(key);
							}
						}
				}
					
					
					int ckvalueL = 0;    //value of clusterkey from inserted record
				    String ckvalue1L = "";
				    double ckvalue2L = 0;
				    String ckvalue3L = simpleDateFormat.format(new Date());
				    ckvalue3L = "1999-10-10";				    
				    Polygon ckvalue4L = new Polygon();
				    Enumeration <String> thekeyL = p.records.get(0).keys(); 
					while(thekeyL.hasMoreElements()){
						String key = thekeyL.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								ckvalueL = (Integer)p.records.get(p.records.size()-1).get(key);
								System.out.println("integer ckvalue largest in the page " + ckvalueL);
							}
							if(datatype.equals("java.lang.String")){
								ckvalue1L = (String)p.records.get(p.records.size()-1).get(key);
							}
							if(datatype.equals("java.lang.Double")){
								ckvalue2L = (Double)p.records.get(p.records.size()-1).get(key);
							}
							if(datatype.equals("java.util.Date")){
								ckvalue3L = (String)p.records.get(p.records.size()-1).get(key);
							}
							if(datatype.equals("java.awt.Polygon")){
								ckvalue4L = (Polygon)p.records.get(p.records.size()-1).get(key);
							}
						}
					}
					
					//checking to see if this will be the page
					if(ckvalue > ckvalueL || ckvalue1.compareTo(ckvalue1L)>0 || ckvalue2 > ckvalue2L || ckvalue3.compareTo(ckvalue3L)>0 || comparePP(ckvalue4, ckvalue4L) == 1 ){
						pagecounter++;
						System.out.println(pagecounter);
						System.out.println(t.pageNo);
						if(p.records.size()<maxN){
							p.records.add(htblColNameValue);
							FileOutputStream fileOut =
							        new FileOutputStream(p.name+".ser");
							        ObjectOutputStream out = new ObjectOutputStream(fileOut);
							        out.writeObject(p);
							        out.close();
							        fileOut.close();
									System.out.println("Record has been stored in "+p.name+".ser");
									return;
						}
						if(p.records.size()==maxN && pagecounter-1 == t.pageNo){
							Page newp = new Page(strTableName + (Integer.toString(pagecounter)));
							newp.records.add(htblColNameValue);
							
							FileOutputStream fileOut =
							        new FileOutputStream(newp.name+".ser");
							        ObjectOutputStream out = new ObjectOutputStream(fileOut);
							        out.writeObject(newp);
							        out.close();
							        fileOut.close();
									System.out.println("Record has been stored in "+newp.name+".ser");
									t.pageNo++;
									FileOutputStream fileOut1 =
									        new FileOutputStream(t.name+".ser");
									        ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
									        out1.writeObject(t);
									        out1.close();
									        fileOut1.close();
											System.out.println("Record has been stored in "+t.name+".ser");
											 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
									            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
													tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), newp.name, pagecounter);
									            }
											 else{
											 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
												tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), newp.name, pagecounter);
											 }
									return;
						}
					}
					else if((ckvalue < ckvalueS || ckvalue1.compareTo(ckvalue1S)<0 || ckvalue2 < ckvalue2S || ckvalue3.compareTo(ckvalue3S)<0 || comparePP(ckvalue4, ckvalue4S) == -1)){
						p.records.insertElementAt(htblColNameValue, 0);
						if(p.records.get(1) == empty){
							p.records.remove(1);
						}
						if(p.records.size() > maxN){
							System.out.println("enters size of vector bigger than max");
							Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
							p.records.remove(p.records.size()-1);
							FileOutputStream fileOut =
							        new FileOutputStream(p.name+".ser");
							        ObjectOutputStream out = new ObjectOutputStream(fileOut);
							        out.writeObject(p);
							        out.close();
							        fileOut.close();
									System.out.println("Record has been stored in "+p.name+".ser");
									 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
							            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
											tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
							            }
									 else{
									 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
										tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
									 }
							theShifter(pagecounter, strTableName,shifted,maxN);
							return;
						}
						else{
							FileOutputStream fileOut =
							        new FileOutputStream(p.name+".ser");
							        ObjectOutputStream out = new ObjectOutputStream(fileOut);
							        out.writeObject(p);
							        out.close();
							        fileOut.close();
									System.out.println("Record has been stored in "+p.name+".ser");
									return;
						}
						
					}
					
						else if(ckvalue < ckvalueL || ckvalue1.compareTo(ckvalue1L)<0 || ckvalue2 < ckvalue2L || ckvalue3.compareTo(ckvalue3L)<0 || comparePP(ckvalue4, ckvalue4L) == -1){
							int replaceIndex = search(p.records,ckvalue,ClusterKey);
							System.out.println("this is the replace index" + replaceIndex);
							p.records.insertElementAt(htblColNameValue, replaceIndex);
							if(p.records.get(replaceIndex+1) == empty){
								p.records.remove(replaceIndex+1);
							}
							if(p.records.size() > maxN){
								Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
								p.records.remove(p.records.size()-1);
								FileOutputStream fileOut =
								        new FileOutputStream(strTableName+Integer.toString(pagecounter)+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+strTableName + Integer.toString(pagecounter)+".ser");
										 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
								            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
												tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
								            }
										 else{
										 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
											tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
										 }
								theShifter(pagecounter, strTableName,shifted,maxN);
								return;
							}
							else{
								
								FileOutputStream fileOut =
								        new FileOutputStream(strTableName+Integer.toString(pagecounter)+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+strTableName + Integer.toString(pagecounter)+".ser");
										 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
								            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
												tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
								            }
										 else{
										 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
											tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
										 }
							return;
							}
						}
						else if((ckvalue == ckvalueL && ClusterKey.getClass().toString().substring(6).equals("java.lang.Integer") ) || (ckvalue1.compareTo(ckvalue1L)==0 && ClusterKey.getClass().toString().substring(6).equals("java.lang.String") ) || (ckvalue2 < ckvalue2L && ClusterKey.getClass().toString().substring(6).equals("java.lang.Double") ) || ((ckvalue3.compareTo(ckvalue3L)==0) && ClusterKey.getClass().toString().substring(6).equals("java.util.Date") ) || (comparePP(ckvalue4, ckvalue4L) == 0 && ClusterKey.getClass().toString().substring(6).equals("java.awt.Polygon") )){
							p.records.insertElementAt(htblColNameValue, p.records.size()-1);
							System.out.println("enters here");
							if(p.records.get(p.records.size()-1) == empty){
								p.records.remove(p.records.size()-1);
							}
							if(p.records.size() > maxN){
								Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
								p.records.remove(p.records.size()-1);
								FileOutputStream fileOut =
								        new FileOutputStream(strTableName+Integer.toString(pagecounter)+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+strTableName + Integer.toString(pagecounter)+".ser");
										 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
								            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
												tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
								            }
										 else{
										 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
											tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
										 }
								theShifter(pagecounter+1, strTableName,shifted,maxN);
								return;
							}
							else{
								
								FileOutputStream fileOut =
								        new FileOutputStream(strTableName+Integer.toString(pagecounter)+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+strTableName + Integer.toString(pagecounter)+".ser");
										 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
								            	Rtree tree = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
												tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
								            }
										 else{
										 BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
											tree.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pagecounter);
										 }
							return;
							}
						}
					}	
				pagecounter++;

			 }
			 }
				 	if(getIndexedCol(strTableName).equals(ClusterKey)){
				 		 Table t = null;
							
							FileInputStream fileIn = new FileInputStream(strTableName +".ser");
						    ObjectInputStream in = new ObjectInputStream(fileIn);
						    t = (Table)in.readObject();
						    in.close();
						    fileIn.close();
				 		BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+ClusterKey+"/Btree.class");
				 		int pno = tree.find((Comparable)htblColNameValue).getIdx();
				 		int ckvalue = 0;    //value of clusterkey from inserted record
					    String ckvalue1 = "";
					    double ckvalue2 = 0;
					    String pattern = "yyyy-MM-dd";
					    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					    String ckvalue3 = simpleDateFormat.format(new Date());
					    ckvalue3 = "1999-10-10";
					    Polygon ckvalue4 = new Polygon();
					    Enumeration <String> thekey = htblColNameValue.keys(); 
						while(thekey.hasMoreElements()){
							String key = thekey.nextElement();
							if(key.equals(ClusterKey)){
								if(datatype.equals("java.lang.Integer")){
									ckvalue = (Integer)htblColNameValue.get(key);
									System.out.println("integer ckvalue " + ckvalue);
								}
								if(datatype.equals("java.lang.String")){
									ckvalue1 = (String)htblColNameValue.get(key);
								}
								if(datatype.equals("java.lang.Double")){
									ckvalue2 = (Double)htblColNameValue.get(key);
								}
								if(datatype.equals("java.util.Date")){
									ckvalue3 = (String)htblColNameValue.get(key);
								}
								if(datatype.equals("java.awt.Polygon")){
									ckvalue4 = (Polygon)htblColNameValue.get(key);
								}
							}
							}
						 Page p = null;
							
							FileInputStream fileInP = new FileInputStream(strTableName+Integer.toString(pno) +".ser");
						    ObjectInputStream inP = new ObjectInputStream(fileInP);
						    p = (Page)inP.readObject();
						    inP.close();
						    fileInP.close();
					
						int ckvalueS = 0;    //value of clusterkey of smallest in the page
					    String ckvalue1S = "";
					    double ckvalue2S = 0;
					    String ckvalue3S = simpleDateFormat.format(new Date());
					    ckvalue3S = "1999-10-10";				    
					    Polygon ckvalue4S = new Polygon();
					    Enumeration <String> thekeyS = p.records.get(0).keys(); 
						while(thekeyS.hasMoreElements()){
							String key = thekeyS.nextElement();
							if(key.equals(ClusterKey)){
								if(datatype.equals("java.lang.Integer")){
									ckvalueS = (Integer)p.records.get(0).get(key);
									System.out.println("integer ckvalue smallest in the page " + ckvalueS);
								}
								if(datatype.equals("java.lang.String")){
									ckvalue1S = (String)p.records.get(0).get(key);
								}
								if(datatype.equals("java.lang.Double")){
									ckvalue2S = (Double)p.records.get(0).get(key);
								}
								if(datatype.equals("java.util.Date")){
									ckvalue3S = (String)p.records.get(0).get(key);
								}
								if(datatype.equals("java.awt.Polygon")){
									ckvalue4S = (Polygon)p.records.get(0).get(key);
								}
							}
					}
						
						
						int ckvalueL = 0;    //value of clusterkey from inserted record
					    String ckvalue1L = "";
					    double ckvalue2L = 0;
					    String ckvalue3L = simpleDateFormat.format(new Date());
					    ckvalue3L = "1999-10-10";				    
					    Polygon ckvalue4L = new Polygon();
					    Enumeration <String> thekeyL = p.records.get(0).keys(); 
						while(thekeyL.hasMoreElements()){
							String key = thekeyL.nextElement();
							if(key.equals(ClusterKey)){
								if(datatype.equals("java.lang.Integer")){
									ckvalueL = (Integer)p.records.get(p.records.size()-1).get(key);
									System.out.println("integer ckvalue largest in the page " + ckvalueL);
								}
								if(datatype.equals("java.lang.String")){
									ckvalue1L = (String)p.records.get(p.records.size()-1).get(key);
								}
								if(datatype.equals("java.lang.Double")){
									ckvalue2L = (Double)p.records.get(p.records.size()-1).get(key);
								}
								if(datatype.equals("java.util.Date")){
									ckvalue3L = (String)p.records.get(p.records.size()-1).get(key);
								}
								if(datatype.equals("java.awt.Polygon")){
									ckvalue4L = (Polygon)p.records.get(p.records.size()-1).get(key);
								}
							}
						}
						
						//checking to see if this will be the page
						if(ckvalue > ckvalueL || ckvalue1.compareTo(ckvalue1L)>0 || ckvalue2 > ckvalue2L || ckvalue3.compareTo(ckvalue3L)>0 || comparePP(ckvalue4, ckvalue4L) == 1 ){
							pno++;
							System.out.println(pno);
							if(p.records.size()<maxN){
								p.records.add(htblColNameValue);
								FileOutputStream fileOut =
								        new FileOutputStream(p.name+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+p.name+".ser");
										return;
							}
							if(p.records.size()==maxN && pno-1 == t.pageNo){
								Page newp = new Page(strTableName + (Integer.toString(pno)));
								newp.records.add(htblColNameValue);
								
								FileOutputStream fileOut =
								        new FileOutputStream(newp.name+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(newp);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+newp.name+".ser");
										t.pageNo++;
										FileOutputStream fileOut1 =
										        new FileOutputStream(t.name+".ser");
										        ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
										        out1.writeObject(t);
										        out1.close();
										        fileOut1.close();
												System.out.println("Record has been stored in "+t.name+".ser");
												 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
										            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
														tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), newp.name, pno);
										            }
												 else{
												 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
													tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), newp.name, pno);
												 }
										return;
							}
						}
						else if((ckvalue < ckvalueS || ckvalue1.compareTo(ckvalue1S)<0 || ckvalue2 < ckvalue2S || ckvalue3.compareTo(ckvalue3S)<0 || comparePP(ckvalue4, ckvalue4S) == -1)){
							p.records.insertElementAt(htblColNameValue, 0);
							if(p.records.get(1) == empty){
								p.records.remove(1);
							}
							if(p.records.size() > maxN){
								System.out.println("enters size of vector bigger than max");
								Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
								p.records.remove(p.records.size()-1);
								FileOutputStream fileOut =
								        new FileOutputStream(p.name+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+p.name+".ser");
										 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
								            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
												tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
								            }
										 else{
										 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
											tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
										 }
								theShifter(pno, strTableName,shifted,maxN);
								return;
							}
							else{
								FileOutputStream fileOut =
								        new FileOutputStream(p.name+".ser");
								        ObjectOutputStream out = new ObjectOutputStream(fileOut);
								        out.writeObject(p);
								        out.close();
								        fileOut.close();
										System.out.println("Record has been stored in "+p.name+".ser");
										return;
							}
							
						}
						
							else if(ckvalue < ckvalueL || ckvalue1.compareTo(ckvalue1L)<0 || ckvalue2 < ckvalue2L || ckvalue3.compareTo(ckvalue3L)<0 || comparePP(ckvalue4, ckvalue4L) == -1){
								int replaceIndex = search(p.records,ckvalue,ClusterKey);
								System.out.println("this is the replace index" + replaceIndex);
								p.records.insertElementAt(htblColNameValue, replaceIndex);
								if(p.records.get(replaceIndex+1) == empty){
									p.records.remove(replaceIndex+1);
								}
								if(p.records.size() > maxN){
									Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
									p.records.remove(p.records.size()-1);
									FileOutputStream fileOut =
									        new FileOutputStream(strTableName+Integer.toString(pno)+".ser");
									        ObjectOutputStream out = new ObjectOutputStream(fileOut);
									        out.writeObject(p);
									        out.close();
									        fileOut.close();
											System.out.println("Record has been stored in "+strTableName + Integer.toString(pno)+".ser");
											 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
									            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
													tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
									            }
											 else{
											 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
												tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
											 }
									theShifter(pno, strTableName,shifted,maxN);
									return;
								}
								else{
									
									FileOutputStream fileOut =
									        new FileOutputStream(strTableName+Integer.toString(pno)+".ser");
									        ObjectOutputStream out = new ObjectOutputStream(fileOut);
									        out.writeObject(p);
									        out.close();
									        fileOut.close();
											System.out.println("Record has been stored in "+strTableName + Integer.toString(pno)+".ser");
											 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
									            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
													tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
									            }
											 else{
											 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
												tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
											 }
								return;
								}
							}
							else if((ckvalue == ckvalueL && ClusterKey.getClass().toString().substring(6).equals("java.lang.Integer") ) || (ckvalue1.compareTo(ckvalue1L)==0 && ClusterKey.getClass().toString().substring(6).equals("java.lang.String") ) || (ckvalue2 < ckvalue2L && ClusterKey.getClass().toString().substring(6).equals("java.lang.Double") ) || ((ckvalue3.compareTo(ckvalue3L)==0) && ClusterKey.getClass().toString().substring(6).equals("java.util.Date") ) || (comparePP(ckvalue4, ckvalue4L) == 0 && ClusterKey.getClass().toString().substring(6).equals("java.awt.Polygon") )){
								p.records.insertElementAt(htblColNameValue, p.records.size()-1);
								System.out.println("enters here");
								if(p.records.get(p.records.size()-1) == empty){
									p.records.remove(p.records.size()-1);
								}
								if(p.records.size() > maxN){
									Hashtable<String, Object> shifted = p.records.get(p.records.size()-1);
									p.records.remove(p.records.size()-1);
									FileOutputStream fileOut =
									        new FileOutputStream(strTableName+Integer.toString(pno)+".ser");
									        ObjectOutputStream out = new ObjectOutputStream(fileOut);
									        out.writeObject(p);
									        out.close();
									        fileOut.close();
											System.out.println("Record has been stored in "+strTableName + Integer.toString(pno)+".ser");
											 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
									            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
													tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
									            }
											 else{
											 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
												tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
											 }
									theShifter(pno+1, strTableName,shifted,maxN);
									return;
								}
								else{
									
									FileOutputStream fileOut =
									        new FileOutputStream(strTableName+Integer.toString(pno)+".ser");
									        ObjectOutputStream out = new ObjectOutputStream(fileOut);
									        out.writeObject(p);
									        out.close();
									        fileOut.close();
											System.out.println("Record has been stored in "+strTableName + Integer.toString(pno)+".ser");
											 if(htblColNameValue.get(getIndexedCol(strTableName)).getClass().getSimpleName().toString().equals("Polygon")){
									            	Rtree tree1 = (Rtree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Rtree.class");
													tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
									            }
											 else{
											 BPTree tree1 = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
												tree1.insert((Comparable)htblColNameValue.get(getIndexedCol(strTableName)), p.name, pno);
											 }
								return;
								}
							}
						}	
				 	}
			 }
	
				    
				    //done
	}
	
	else{
		System.out.println("Record is empty, please enter a record");
	}
	
}
public static void theShifter(int pagecounter, String Tname, Hashtable<String, Object> theshifted, int maxN ) throws ClassNotFoundException, IOException{
	
	Table t = null;
	FileInputStream fileIn = new FileInputStream(Tname+".ser");
	ObjectInputStream in = new ObjectInputStream(fileIn);
	t = (Table)in.readObject();
	in.close();
	fileIn.close();
	System.out.println("enters 1");
//	if(pagecounter == t.pageNo){
//		System.out.println("enters first if in theshifter");
//
//				Page newp = new Page(Tname+Integer.toString(pagecounter+1));
//				newp.records.add(theshifted);
//				
//				try {
//					System.out.println("enters 5");
//			        FileOutputStream fileOut =
//			        new FileOutputStream(newp.name+".ser");
//			        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			        out.writeObject(newp);
//			        out.close();
//			        fileOut.close();
//			        System.out.println("Serialized data is saved in "+newp.name+".ser");
//			     } catch (IOException i) {
//			        i.printStackTrace();
//			     }
//				t.pageNo++;
//
//				try {
//					System.out.println("enters 6");
//			        FileOutputStream fileOut =
//			        new FileOutputStream(Tname+".ser");
//			        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			        out.writeObject(t);
//			        out.close();
//			        fileOut.close();
//			        System.out.println("Serialized data is saved in "+Tname+".ser");
//			     } catch (IOException i) {
//			        i.printStackTrace();
//			     }
//				return;
//			
//	}
//	if(pagecounter < t.pageNo) {
//		System.out.println("enters second if");
	System.out.println(pagecounter);
	if(pagecounter == t.pageNo+1){

		Page newp = new Page(Tname+Integer.toString(pagecounter));
		newp.records.add(theshifted);
		
		try {
			System.out.println("enters 5");
	        FileOutputStream fileOut =
	        new FileOutputStream(newp.name+".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(newp);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in "+Tname+Integer.toString(pagecounter)+".ser");
	     } catch (IOException i1) {
	        i1.printStackTrace();
	     }
		t.pageNo++;

		try {
			System.out.println("enters 6");
	        FileOutputStream fileOut =
	        new FileOutputStream(t.name+".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(t);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in "+t.name+".ser");
	     } catch (IOException i1) {
	        i1.printStackTrace();
	     }
		return;
	
}

	
	else{
	for(int i = pagecounter; i<=t.pageNo; i++){
		
		System.out.println(i);
//			System.out.println("this is sparta");
	Page p = null;
	FileInputStream fileIn1 = new FileInputStream(Tname+Integer.toString(i)+".ser");
	ObjectInputStream in1 = new ObjectInputStream(fileIn1);
	p = (Page)in1.readObject();
	in1.close();
	fileIn1.close();
	p.records.insertElementAt(theshifted, 0);
	if(p.records.size()>maxN){
	theshifted = p.records.get(p.records.size()-1);
	p.records.remove(p.records.size()-1);
	if(i == t.pageNo){

		Page newp = new Page(Tname+Integer.toString(i+1));
		newp.records.add(theshifted);
		
		try {
			System.out.println("enters 5");
	        FileOutputStream fileOut =
	        new FileOutputStream(newp.name+".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(newp);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in "+Tname+Integer.toString(i+1)+".ser");
	     } catch (IOException i1) {
	        i1.printStackTrace();
	     }
		t.pageNo++;

		try {
			System.out.println("enters 6");
	        FileOutputStream fileOut =
	        new FileOutputStream(t.name+".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(t);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in "+t.name+".ser");
	     } catch (IOException i1) {
	        i1.printStackTrace();
	     }
		return;
	
}
	System.out.println("enters 2");
	try {
		System.out.println("enters 3");
        FileOutputStream fileOut =
        new FileOutputStream(Tname+Integer.toString(i)+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(p);
        out.close();
        fileOut.close();
        System.out.println("Serialized data is saved in "+Tname+Integer.toString(i)+".ser");
     } catch (IOException i1) {
        i1.printStackTrace();
     }
	}
	else{
	try {
		System.out.println("enters 3");
        FileOutputStream fileOut =
        new FileOutputStream(Tname+Integer.toString(i)+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(p);
        out.close();
        fileOut.close();
        System.out.println("Serialized data is saved in "+Tname+Integer.toString(i)+".ser");
     } catch (IOException i1) {
        i1.printStackTrace();
     }
	
	}
		}
	
	}
	}


public static int comparePP(Polygon x, Polygon y){
	Dimension dimx =  x.getBounds().getSize();
	Dimension dimy =  y.getBounds().getSize();
	int Areax = dimx.width * dimx.height;
	int Areay = dimy.width * dimy.height;
	
	if(Areax > Areay){
		return 1;}
		else if(Areax < Areay){
			return -1;
		}
		else
			return 0;
	
}
public static int search(Vector<Hashtable<String, Object>> pageRecords, Object x, String CK) 
{ 	
	int x1 = 0;
	double x2 = 0;
	String x3 = "";
	Polygon x4 = null;
	Date x5 = new Date();
	Hashtable<String, Object> empty = new Hashtable<String, Object>();
	int m1 = 0;
	if(x.getClass().toString().substring(6).equals("java.lang.Integer")){
		 x1 = (Integer)x;
	}
	if(x.getClass().toString().substring(6).equals("java.lang.Double")){
		 x2 = (Double)x;
	}
	if(x.getClass().toString().substring(6).equals("java.lang.String")){
		 x3 = (String)x;
	}
	if(x.getClass().toString().substring(6).equals("java.awt.Polygon")){
		 x4 = (Polygon)x;
	}
	if(x.getClass().toString().substring(6).equals("java.util.Date")){
		 x5 = (Date)x;
	}
    for(int m = 0; m<pageRecords.size();m++)
    { 

        Enumeration <String> keys = pageRecords.get(m).keys();
		 while(keys.hasMoreElements()){
			 String key = keys.nextElement();
			 
			 if(key.equals(CK)){
				 if(x.getClass().toString().substring(6).equals("java.lang.Integer")){
					 int j = (Integer)pageRecords.get(m).get(key);
					 if ((x1 > j && x1 < (Integer)pageRecords.get(m+1).get(key))|| pageRecords.get(m+1) == empty) {
						 return m+1;
						 }
					 else if(x1 == j){
							return m+1;
						}
				        // If x is smaller, ignore upper half 
				       

					}
				 else if(x.getClass().toString().substring(6).equals("java.lang.Double")){
						double j = (Double)pageRecords.get(m).get(key);
						if ((x2 > j && x2 < (Double)pageRecords.get(m+1).get(key))|| pageRecords.get(m+1) == empty) {
						return m+1;
						}
						else if(x2 == j){
							return m+1;
						}
					}
				 else if(x.getClass().toString().substring(6).equals("java.lang.String")){
						String j = (String)pageRecords.get(m).get(key);
						if ((x3.compareTo(j)>0 && x3.compareTo((String)pageRecords.get(m).get(key))<0)|| pageRecords.get(m+1) == empty) {
						return m+1;
						}
						else if(x3.compareTo(j) == 0){
							return m+1;
						}

					}
				 else if(x.getClass().toString().substring(6).equals("java.awt.Polygon")){
						Polygon j = (Polygon)pageRecords.get(m).get(key);
						if ((comparePP(x4, j)==1 && comparePP(x4, (Polygon)pageRecords.get(m+1).get(key))==-1)|| pageRecords.get(m+1) == empty) {
						return m+1;
						}
						else if(comparePP(x4, j) == 0){
							return m+1;
						}
					}
				 else if(x.getClass().toString().substring(6).equals("java.util.Date")){
						Date j = (Date)pageRecords.get(m).get(key);
						if ((x5.compareTo(j)>0 && x5.compareTo((Date)pageRecords.get(m+1).get(key))<0)|| pageRecords.get(m+1) == empty) {
				            return m+1;
						 }
						else if(x5.compareTo(j) == 0){
							return m+1;
						}

					}
        
        // If x is smaller, ignore upper half 
        
    }
		 
}
		 m1 = m+1;
}
    return m1;
}
public static boolean insertCheckerEmpty(String Tname) throws ClassNotFoundException, IOException{
	Table t = null;
	
	FileInputStream fileIn = new FileInputStream(Tname +".ser");
    ObjectInputStream in = new ObjectInputStream(fileIn);
    t = (Table)in.readObject();
    in.close();
    fileIn.close();
 
	if(t.pageNo == 1){
		Page p = null;

		FileInputStream fileIn1 = new FileInputStream(Tname +"1.ser");
	    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
	    p = (Page)in1.readObject();
	    in1.close();
	    fileIn1.close();
	 
		if(p.records.size() == 0){
//			System.out.println("does");

			return true;
		}
		else{
//			System.out.println("doesn");
			return false;
		}
	}
	else{
//		System.out.println("doesnt");
		return false;
	}
}

//public static Vector<Hashtable<String, Object>> sortHash(String tName){
//		
//	
//}
public void updateTable(String strTableName,
		 String strClusteringKey,
		 Hashtable<String,Object> htblColNameValue ) throws IOException, ClassNotFoundException{
	if(!getIndexedCol(strTableName).equals("Error")){
	
	    
	    BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
		TuplePointer tp = tree.find((Comparable)getIndexedCol(strTableName));
		
		Page p = null;	
		FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(tp.getIdx()) +".ser");
	    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
	    p = (Page)inP1.readObject();
	    inP1.close();
	    fileInP1.close();
	    
	    for(int i = 0; i<p.records.size(); i++){
	    	if(p.records.get(i).get(getIndexedCol(strTableName)).equals(htblColNameValue.get(getIndexedCol(strTableName)))){
	    	p.records.remove(i);
	    	p.records.add(i, htblColNameValue);
	    	FileOutputStream fileOut =
		            new FileOutputStream(strTableName+Integer.toString(i)+".ser");
		            ObjectOutputStream out = new ObjectOutputStream(fileOut);
		            out.writeObject(p);
		            out.close();
		            fileOut.close();
	    	}
	    }
	}
	else{
	if(!htblColNameValue.isEmpty()){
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellType = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellCname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		ArrayList rowDataCellCK = new ArrayList();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
		    rowDataCellCname.add(data2[1]);
		    rowDataCellType.add(data2[2]);
		    rowDataCellCK.add(data2[3]);
			rowDataCellmaxN.add(data2[4]);

		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}
		Vector<String> typeData = new Vector<String>();
		Vector<String> nameData = new Vector<String>();
		Vector<String> CKData = new Vector<String>();
		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				typeData.add((String)rowDataCellType.get(j));
				nameData.add((String)rowDataCellCname.get(j));
				CKData.add((String)rowDataCellCK.get(j));
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));
			}
		}
		String CKname = "";
		String CKtype = "";		
		for(int i = 0; i<typeData.size()-1;i++){
			if(CKData.get(i).equals("True")){
				CKname = nameData.get(i);
				CKtype = typeData.get(i);
			}
		}
		if(CKtype.equals("java.lang.Integer")){
			int ckvalue = Integer.parseInt(strClusteringKey);
			
			Table t = null;	
			FileInputStream fileInP = new FileInputStream(strTableName +".ser");
		    ObjectInputStream inP = new ObjectInputStream(fileInP);
		    t = (Table)inP.readObject();
		    inP.close();
		    fileInP.close();
				for(int j = 1; j <= t.pageNo; j++){
				Page p = null;	
				FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(j) +".ser");
			    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
			    p = (Page)inP1.readObject();
			    inP1.close();
			    fileInP1.close();
				for(int k = 0; k <= p.records.size()-1; k++){

			    Enumeration <String> thekeyS = p.records.get(0).keys(); 
				while(thekeyS.hasMoreElements()){
					String key = thekeyS.nextElement();
					if(key == CKname){
						if(ckvalue==(Integer)p.records.get(k).get(key)){
							p.records.remove(k);
							htblColNameValue.put(CKname, ckvalue);
							p.records.insertElementAt(htblColNameValue, k);
							FileOutputStream fileOut =
						            new FileOutputStream(strTableName+Integer.toString(j)+".ser");
						            ObjectOutputStream out = new ObjectOutputStream(fileOut);
						            out.writeObject(p);
						            out.close();
						            fileOut.close();
						            System.out.println("The record has been inserted in" +strTableName+Integer.toString(j)+".ser");
						            return;
						}
				}
				
				}
				}
				}
			
			
			
		}
		if(CKtype.equals("java.lang.String")){
			String ckvalue = (strClusteringKey);
			Table t = null;	
			FileInputStream fileInP = new FileInputStream(strTableName +".ser");
		    ObjectInputStream inP = new ObjectInputStream(fileInP);
		    t = (Table)inP.readObject();
		    inP.close();
		    fileInP.close();
				for(int j = 1; j <= t.pageNo; j++){
				Page p = null;	
				FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(j) +".ser");
			    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
			    p = (Page)inP1.readObject();
			    inP1.close();
			    fileInP1.close();
				for(int k = 0; k <= p.records.size()-1; k++){

			    Enumeration <String> thekeyS = p.records.get(0).keys(); 
				while(thekeyS.hasMoreElements()){
					String key = thekeyS.nextElement();
					if(key == CKname){
						if(ckvalue.equals(p.records.get(k).get(key))){
							p.records.remove(k);
							htblColNameValue.put(CKname, ckvalue);
							p.records.insertElementAt(htblColNameValue, k);
							FileOutputStream fileOut =
						            new FileOutputStream(strTableName+Integer.toString(j)+".ser");
						            ObjectOutputStream out = new ObjectOutputStream(fileOut);
						            out.writeObject(p);
						            out.close();
						            fileOut.close();
						            System.out.println("The record has been inserted in" +strTableName+Integer.toString(j)+".ser");
						            return;
						}
				}
				
				}
				}
				}
			
			
		}
		if(CKtype.equals("java.lang.Double")){
			double ckvalue = Double.parseDouble(strClusteringKey);
			Table t = null;	
			FileInputStream fileInP = new FileInputStream(strTableName +".ser");
		    ObjectInputStream inP = new ObjectInputStream(fileInP);
		    t = (Table)inP.readObject();
		    inP.close();
		    fileInP.close();
				for(int j = 1; j <= t.pageNo; j++){
				Page p = null;	
				FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(j) +".ser");
			    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
			    p = (Page)inP1.readObject();
			    inP1.close();
			    fileInP1.close();
				for(int k = 0; k <= p.records.size()-1; k++){

			    Enumeration <String> thekeyS = p.records.get(0).keys(); 
				while(thekeyS.hasMoreElements()){
					String key = thekeyS.nextElement();
					if(key == CKname){
						if(ckvalue==(Integer)p.records.get(k).get(key)){
							p.records.remove(k);
							htblColNameValue.put(CKname, ckvalue);
							p.records.insertElementAt(htblColNameValue, k);
							FileOutputStream fileOut =
						            new FileOutputStream(strTableName+Integer.toString(j)+".ser");
						            ObjectOutputStream out = new ObjectOutputStream(fileOut);
						            out.writeObject(p);
						            out.close();
						            fileOut.close();
						            System.out.println("The record has been inserted in" +strTableName+Integer.toString(j)+".ser");
						            return;
						}
				}
				
				}
				}
				}
			
			
		}
		if(CKtype.equals("java.util.Date")){
			String ckvalue = (strClusteringKey);
			Table t = null;	
			FileInputStream fileInP = new FileInputStream(strTableName +".ser");
		    ObjectInputStream inP = new ObjectInputStream(fileInP);
		    t = (Table)inP.readObject();
		    inP.close();
		    fileInP.close();
				for(int j = 1; j <= t.pageNo; j++){
				Page p = null;	
				FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(j) +".ser");
			    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
			    p = (Page)inP1.readObject();
			    inP1.close();
			    fileInP1.close();
				for(int k = 0; k <= p.records.size()-1; k++){

			    Enumeration <String> thekeyS = p.records.get(0).keys(); 
				while(thekeyS.hasMoreElements()){
					String key = thekeyS.nextElement();
					if(key == CKname){
						if(ckvalue.equals(p.records.get(k).get(key))){
							p.records.remove(k);
							htblColNameValue.put(CKname, ckvalue);
							p.records.insertElementAt(htblColNameValue, k);
							FileOutputStream fileOut =
						            new FileOutputStream(strTableName+Integer.toString(j)+".ser");
						            ObjectOutputStream out = new ObjectOutputStream(fileOut);
						            out.writeObject(p);
						            out.close();
						            fileOut.close();
						            System.out.println("The record has been inserted in" +strTableName+Integer.toString(j)+".ser");
						            return;
						}
				}
				
				}
				}
				}
			
			
		}
	
		
	}
	
	
	else{
		System.out.println("Record is empty, please re-enter");
	}
	}
}
public void deleteFromTable(String strTableName, Hashtable<String, Object>htblColNameValue)throws DBAppException, IOException, ClassNotFoundException{
	if(!getIndexedCol(strTableName).equals("Error")){
		Hashtable<String, Object> deleter = new Hashtable<String, Object>();

	    BPTree tree = (BPTree)readObject("data/"+strTableName+"/indices/"+getIndexedCol(strTableName)+"/Btree.class");
		TuplePointer tp = tree.find((Comparable)getIndexedCol(strTableName));
		
		Page p = null;	
		FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(tp.getIdx()) +".ser");
	    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
	    p = (Page)inP1.readObject();
	    inP1.close();
	    fileInP1.close();
	    
	    for(int i = 0; i<p.records.size(); i++){
	    	if(p.records.get(i).get(getIndexedCol(strTableName)).equals(htblColNameValue.get(getIndexedCol(strTableName)))){
	    	p.records.remove(i);
			p.records.insertElementAt(deleter, i);
	    	FileOutputStream fileOut =
		            new FileOutputStream(strTableName+Integer.toString(i)+".ser");
		            ObjectOutputStream out = new ObjectOutputStream(fileOut);
		            out.writeObject(p);
		            out.close();
		            fileOut.close();
	    	}
	    }
	}
	else{
		
	
	if(!htblColNameValue.isEmpty()){
		
		String row;
		ArrayList rowData = new ArrayList();
		ArrayList rowDataCellType = new ArrayList();
		ArrayList rowDataCellTname = new ArrayList();
		ArrayList rowDataCellCname = new ArrayList();
		ArrayList rowDataCellmaxN = new ArrayList();
		ArrayList rowDataCellCK = new ArrayList();
		Boolean correctFormat = true;

		BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split("/n");
		    rowData.add(data[0]);
		    String[] data2 = row.split(",");	
		    rowDataCellTname.add(data2[0]);
		    rowDataCellCname.add(data2[1]);
		    rowDataCellType.add(data2[2]);
		    rowDataCellCK.add(data2[3]);
			rowDataCellmaxN.add(data2[4]);

		    }
		int count = 0;
		
		for(int i = 0; i < rowData.size()-1;i++){
			if(rowDataCellTname.get(i).equals(strTableName)){
				break;
			}
			else{
				count += 1;
			}
			}
		Vector<String> typeData = new Vector<String>();
		Vector<String> nameData = new Vector<String>();
		Vector<String> CKData = new Vector<String>();
		int maxN = 0;
		for(int j = count;j<rowData.size();j++){
			if(!rowDataCellTname.get(j).equals(strTableName)){
				break;
			}
			else{
				typeData.add((String)rowDataCellType.get(j));
				nameData.add((String)rowDataCellCname.get(j));
				CKData.add((String)rowDataCellCK.get(j));
				maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

			}
		}
		int vectorCounter = 0;
		String ClusterKey = "none";
		String datatype = "none";
		 Enumeration <String> keys = htblColNameValue.keys();
		 while(keys.hasMoreElements()){
			 String key = keys.nextElement();
			 String dataType = (htblColNameValue.get(key).getClass().toString().substring(6));
//			 System.out.println(dataType);
//			 System.out.println(nameData.get(vectorCounter));
//			 System.out.println(typeData.get(vectorCounter));
//			 System.out.println(key);
			 if(CKData.get(vectorCounter).equals("True")){
				 ClusterKey = nameData.get(vectorCounter);
				 datatype = typeData.get(vectorCounter);
			 }
			 System.out.println(ClusterKey + "  " + maxN);
			 if(!key.equals(nameData.get(vectorCounter)) || !(typeData.get(vectorCounter)).equals(dataType)){
				 correctFormat = false;
				 break;
			 }
			 else{
			 vectorCounter++;
			 System.out.println("good so far");
			 }
			 
		 }
		 
		 if(!correctFormat){
			 System.out.println("wrong insertion format");
			 return;
		 }
		 else{
			 if(vectorCounter < nameData.size()){
				 System.out.println("record entered is incomplete");
				 return;
			 }
			 
			 else{
			//PHASE 2 CORRECT FORMAT
				 System.out.println("right format good job");

				    Date date= new Date();
				    htblColNameValue.put("TouchDate", date);
			 if(insertCheckerEmpty(strTableName)){ //the table is empty
				 	Page p = null;
					FileInputStream fileIn1 = new FileInputStream(strTableName +"1.ser");
				    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
				    p = (Page)in1.readObject();
				    in1.close();
				    fileIn1.close();
			         
				    p.records.add(htblColNameValue);
				    FileOutputStream fileOut =
				            new FileOutputStream(strTableName+"1.ser");
				            ObjectOutputStream out = new ObjectOutputStream(fileOut);
				            out.writeObject(p);
				            out.close();
				            fileOut.close();
				            System.out.println("The record has been inserted in page 1 of table " + strTableName);
				            return;
				    
			 }
			 
			 else if (!insertCheckerEmpty(strTableName)){ // the table is
				 
			
				 
				 
				 
				 
				 
			    	System.out.println("enters the not empty page checker");

				 Table t = null;
					
					FileInputStream fileIn = new FileInputStream(strTableName +".ser");
				    ObjectInputStream in = new ObjectInputStream(fileIn);
				    t = (Table)in.readObject();
				    in.close();
				    fileIn.close();
				    int decount = t.pageNo;
				    int pagecounter = 1;
				    
				    int ckvalue = 0;    //value of clusterkey from inserted record
				    String ckvalue1 = "";
				    double ckvalue2 = 0;
				    String pattern = "yyyy-MM-dd";
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				    String ckvalue3 = simpleDateFormat.format(new Date());
				    ckvalue3 = "1999-10-10";
				    Polygon ckvalue4 = new Polygon();
				    Enumeration <String> thekey = htblColNameValue.keys(); 
					while(thekey.hasMoreElements()){
						String key = thekey.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								ckvalue = (Integer)htblColNameValue.get(key);
								System.out.println("integer ckvalue " + ckvalue);
							}
							if(datatype.equals("java.lang.String")){
								ckvalue1 = (String)htblColNameValue.get(key);
							}
							if(datatype.equals("java.lang.Double")){
								ckvalue2 = (Double)htblColNameValue.get(key);
							}
							if(datatype.equals("java.util.Date")){
								ckvalue3 = (String)htblColNameValue.get(key);
							}
							if(datatype.equals("java.awt.Polygon")){
								ckvalue4 = (Polygon)htblColNameValue.get(key);
							}
						}
						}//now i have the ckvalue of the entered htblColNameValue stored in whichever range from 0 to 5 not including 3
				for (int i = decount; i>0; i--){
					 Page p = null;
						
						FileInputStream fileInP = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
					    ObjectInputStream inP = new ObjectInputStream(fileInP);
					    p = (Page)inP.readObject();
					    inP.close();
					    fileInP.close();
				
						 if(ClusterKey.getClass().toString().substring(6).equals("java.lang.Boolean")){
							 Enumeration <String> thekeyS = p.records.get(0).keys(); 
								while(thekeyS.hasMoreElements()){
									String key = thekeyS.nextElement();
									if(key == ClusterKey){
										Enumeration <String> thekeyL = p.records.get(p.records.size()-1).keys(); 
										while(thekeyL.hasMoreElements()){
											if(key == ClusterKey){
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												
												p.records.insertElementAt(deleter, 0);
												p.records.remove(1);
												if(emptyPage(p.records)){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
											}
									}
									}
									else{
										pagecounter++;
									}
						 }
						 }
					    
					    
					int ckvalueS = 0;    //value of clusterkey of smallest in the page
				    String ckvalue1S = "";
				    double ckvalue2S = 0;
				    String ckvalue3S = simpleDateFormat.format(new Date());
				    ckvalue3S = "1999-10-10";
				    Polygon ckvalue4S = new Polygon();
				    Enumeration <String> thekeyS = p.records.get(0).keys(); 
					while(thekeyS.hasMoreElements()){
						String key = thekeyS.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								ckvalueS = (Integer)p.records.get(0).get(key);
								System.out.println("integer ckvalue smallest in the page " + ckvalueS);
							}
							if(datatype.equals("java.lang.String")){
								ckvalue1S = (String)p.records.get(0).get(key);
							}
							if(datatype.equals("java.lang.Double")){
								ckvalue2S = (Double)p.records.get(0).get(key);
							}
							if(datatype.equals("java.util.Date")){
								ckvalue3S = (String)p.records.get(0).get(key);
							}
							if(datatype.equals("java.awt.Polygon")){
								ckvalue4S = (Polygon)p.records.get(0).get(key);
							}
						}
				}
					
					
					int ckvalueL = 0;    //value of clusterkey from inserted record
				    String ckvalue1L = "";
				    double ckvalue2L = 0;
				    String ckvalue3L = simpleDateFormat.format(new Date());
				    ckvalue3L = "1999-10-10";
				    Polygon ckvalue4L = new Polygon();
				    Enumeration <String> thekeyL = p.records.get(0).keys(); 
					while(thekeyL.hasMoreElements()){
						String key = thekeyL.nextElement();
						if(key.equals(ClusterKey)){
							if(datatype.equals("java.lang.Integer")){
								for(int j = (p.records.size()-1); j>0;j--){
									if(!p.records.get(j).isEmpty()){
								ckvalueL = (Integer)p.records.get(j).get(key);
								System.out.println("integer ckvalue largest in the page " + ckvalueL);
								}
								}
							}
							if(datatype.equals("java.lang.String")){
								for(int j = p.records.size()-1; j>0;j++){
									if(!p.records.get(j).isEmpty()){
								ckvalue1L = (String)p.records.get(j).get(key);
									}}
							}
							if(datatype.equals("java.lang.Double")){
								for(int j = p.records.size()-1; j>0;j++){
									if(!p.records.get(j).isEmpty()){
								ckvalue2L = (Double)p.records.get(j).get(key);
									}}
							}
							if(datatype.equals("java.util.Date")){
								for(int j = p.records.size()-1; j>0;j++){
									if(!p.records.get(j).isEmpty()){
								ckvalue3L = (String)p.records.get(j).get(key);
									}}
							}
							if(datatype.equals("java.awt.Polygon")){
								for(int j = p.records.size()-1; j>0;j++){
									if(!p.records.get(j).isEmpty()){
								ckvalue4L = (Polygon)p.records.get(j).get(key);
									}}
							}
						}
					}
					if(ckvalue > ckvalueL || ckvalue1.compareTo(ckvalue1L)>0 || ckvalue2 > ckvalue2L || ckvalue3.compareTo(ckvalue3L)>0 || comparePP(ckvalue4, ckvalue4L) == 1){
						pagecounter++;
						System.out.println(ckvalueL);

					}
					else if(ckvalue <= ckvalueL || ckvalue1.compareTo(ckvalue1L)<=0 || ckvalue2 <= ckvalue2L || ckvalue3.compareTo(ckvalue3L)<=0 || comparePP(ckvalue4L, ckvalue4) == 1){
						 for (int i1 = 0; i1<=p.records.size()-1;i1++){
							 Enumeration <String> thekeyFind = p.records.get(i1).keys(); 
								while(thekeyFind.hasMoreElements()){
									String key = thekeyFind.nextElement();
									if(key.equals(ClusterKey) && !p.records.get(i1).isEmpty()){
										System.out.println("ooga");
										if(ckvalue != 0){
											System.out.println("ooga");
											if (ckvalue == ((Integer) p.records.get(i1).get(key))){
												System.out.println("ooga");
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												System.out.println(deleter);
												System.out.println(i1);
												System.out.println("chaka");
												System.out.println(p.records.get(i1).get(key));
												p.records.insertElementAt(deleter, i1);
												p.records.remove(i1+1);
												if(emptyPage(p.records)){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();												            
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
											}
										}
										if(!ckvalue1.equals("")){
											if(ckvalue1.equals((String)p.records.get(i1).get(key))){
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												System.out.println(deleter);
												System.out.println(i1);
												System.out.println("chaka");
												System.out.println(p.records.get(i1).get(key));
												p.records.insertElementAt(deleter, i1);
												p.records.remove(i1+1);
												if(emptyPage(p.records)){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
											}
										}
										if(ckvalue2 != 0){
											if (ckvalue2 == (Double) p.records.get(i1).get(key)){
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												System.out.println(deleter);
												System.out.println(i1);
												System.out.println("chaka");
												System.out.println(p.records.get(i1).get(key));
												p.records.insertElementAt(deleter, i1);
												p.records.remove(i1+1);
												if(emptyPage(p.records)){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
											}
										}
										if(!ckvalue3.equals("1999-10-10")){
											if(ckvalue3.equals((Date)p.records.get(i1).get(key))){
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												System.out.println(deleter);
												System.out.println(i1);
												System.out.println("chaka");
												System.out.println(p.records.get(i1).get(key));
												p.records.insertElementAt(deleter, i1);
												p.records.remove(i1+1);
												if(emptyPage(p.records)){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
										}
										if(ckvalue4 != null){
											if(comparePP(ckvalue4,(Polygon)p.records.get(i1).get(key)) == 0){
												Hashtable<String, Object> deleter = new Hashtable<String, Object>();
												System.out.println(deleter);
												System.out.println(i1);
												System.out.println("chaka");
												System.out.println(p.records.get(i1).get(key));
												p.records.insertElementAt(deleter, i1);
												p.records.remove(i1+1);
												if(emptyPage(p.records)){
													if(pagecounter < t.pageNo){
													pagecounter++;
													for(int i11 = pagecounter; i11 < t.pageNo; i11++){
													Page p1 = null;
													FileInputStream fileInP1 = new FileInputStream(strTableName+Integer.toString(pagecounter) +".ser");
												    ObjectInputStream inP1 = new ObjectInputStream(fileInP1);
												    p1 = (Page)inP1.readObject();
												    inP1.close();
												    fileInP1.close();
												    FileOutputStream fileOut =
												            new FileOutputStream(strTableName + Integer.toString(pagecounter-1)+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
													}
													}
													else{
														
														File f = new File(strTableName+Integer.toString(pagecounter) +".ser");
													    f.delete();
													}
											         t.pageNo--; 
											         FileOutputStream fileOut =
													            new FileOutputStream(strTableName +".ser");
													            ObjectOutputStream out = new ObjectOutputStream(fileOut);
													            out.writeObject(p);
													            out.close();
													            fileOut.close();
												return;
												}
												else{
													FileOutputStream fileOut =
												            new FileOutputStream(p.name+".ser");
												            ObjectOutputStream out = new ObjectOutputStream(fileOut);
												            out.writeObject(p);
												            out.close();
												            fileOut.close();
												            System.out.println("The record has been deleted from " + p.name);
													return;	
												}
											}
										}
											
										
									}
								}
						    }
						 
					}
					}
					else if(ckvalue < ckvalueS || ckvalue1.compareTo(ckvalue1S)<0 || ckvalue2 < ckvalue2S || ckvalue3.compareTo(ckvalue3S)<0 || comparePP(ckvalue4, ckvalue4L) == 1){
						System.out.println(comparePP(ckvalue4,ckvalue4L));
						System.out.println("Record doesn't exist, cannot delete.");
						return;
					}
					
				}
			 }
			 }
		 }
	}
	}
}

private boolean emptyPage(Vector<Hashtable<String, Object>> records) {
	for(int i = 0; i<records.size();i++){
		if(!records.get(i).isEmpty()){
			return false;
		}
	}
	return true;
}
public Iterator selectFromTable(SQLTerm[] arrSQLTerms,    String[]  strarrOperators)  throws DBAppException, IOException, ClassNotFoundException {
	Vector<Hashtable<String, Object>> result = new Vector<Hashtable<String, Object>>();//tuples matching query
for(int i=0;i<arrSQLTerms.length;i++){
	if(checkTable(arrSQLTerms[i].strTableName)){//check for table existence
		if(checkColName(arrSQLTerms[i].strTableName,arrSQLTerms[i].strColumnName)){//check for col name existence
			if(i == 0){
							Table t = null;//table deserialization
							FileInputStream fileIn = new FileInputStream(arrSQLTerms[i].strTableName +".ser");
						    ObjectInputStream in = new ObjectInputStream(fileIn);
						    t= (Table)in.readObject();
						    in.close();
						    fileIn.close();
							for(int ii = 1; ii<=t.pageNo;ii++){
								Page p = null;
								FileInputStream fileIn1 = new FileInputStream(arrSQLTerms[i].strTableName+ Integer.toString(ii) +".ser");
							    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
							    p = (Page)in1.readObject();
							    in1.close();
							    fileIn.close();
							    for(int recNo = 0; recNo < p.records.size();recNo++){
							    	
							    	if(arrSQLTerms[i].strOperator.equals("=")){
							    	if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
							    	}
							    	if(arrSQLTerms[i].strOperator.equals("!=")){
								    	if(arrSQLTerms[i].objValue != p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
								    		result.add(p.records.get(recNo));
								    	}
								    	}
							    	else if(arrSQLTerms[i].strOperator.equals(">")){
							    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
							    		if((Integer)arrSQLTerms[i].objValue > (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
								    		result.add(p.records.get(recNo));
								    	}
									}
							    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
							    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())<0){
									    		result.add(p.records.get(recNo));
							    			}
							    		}
							    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
							    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==-1){
									    		result.add(p.records.get(recNo));
							    			}
							    		}
							    		
							    	}
							    	else if(arrSQLTerms[i].strOperator.equals("<")){
							    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
								    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
									    		result.add(p.records.get(recNo));
									    	}
										}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
								    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
								    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
									}
							    	else if(arrSQLTerms[i].strOperator.equals(">=")){
							    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
								    		result.add(p.records.get(recNo));
								    	}
							    	
							    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
								    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
									    		result.add(p.records.get(recNo));
									    	}
										}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
								    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
								    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
									}
							    	else if(arrSQLTerms[i].strOperator.equals("<=")){
							    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
								    		result.add(p.records.get(recNo));
								    	}
							    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
								    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
									    		result.add(p.records.get(recNo));
									    	}
										}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
								    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
								    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
								    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
										    		result.add(p.records.get(recNo));
								    			}
								    		}
									}

							    }//end of one page
							    
							}//end of table first query
		}//end of first query
			else{
				if(strarrOperators[i-1].equals("AND")){
					Vector<Hashtable<String, Object>> resultTemp = new Vector<Hashtable<String, Object>>();
					for(int recNo = 0; recNo<result.size();recNo++){
						
				    	if(arrSQLTerms[i].strOperator.equals("=")){
				    	if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
				    		resultTemp.add(result.get(recNo));
				    	}
				    	}
				    	if(arrSQLTerms[i].strOperator.equals("!=")){
					    	if(arrSQLTerms[i].objValue != resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
					    	}
				    	else if(arrSQLTerms[i].strOperator.equals(">")){
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
				    		if((Integer)arrSQLTerms[i].objValue > (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
						}
				    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
				    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())<0){
						    		resultTemp.add(result.get(recNo));
				    			}
				    		}
				    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
				    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==-1){
						    		resultTemp.add(result.get(recNo));
				    			}
				    		}
				    		
				    	}
				    	else if(arrSQLTerms[i].strOperator.equals("<")){
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				    	else if(arrSQLTerms[i].strOperator.equals(">=")){
				    		if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
				    	
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				    	else if(arrSQLTerms[i].strOperator.equals("<=")){
				    		if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				}
					result = resultTemp;
			}//AND conjunction
				if(strarrOperators[i-1].equals("OR")){
					Table t = null;//table deserialization
					FileInputStream fileIn = new FileInputStream(arrSQLTerms[i].strTableName +".ser");
				    ObjectInputStream in = new ObjectInputStream(fileIn);
				    t= (Table)in.readObject();
				    in.close();
				    fileIn.close();
					for(int ii = 1; ii<=t.pageNo;ii++){
						Page p = null;
						FileInputStream fileIn1 = new FileInputStream(arrSQLTerms[i].strTableName+ Integer.toString(ii) +".ser");
					    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
					    p = (Page)in1.readObject();
					    in1.close();
					    fileIn.close();
					    for(int recNo = 0; recNo < p.records.size();recNo++){
					    	
					    	if(arrSQLTerms[i].strOperator.equals("=")){
					    	if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		result.add(p.records.get(recNo));
					    	}
					    	}
					    	if(arrSQLTerms[i].strOperator.equals("!=")){
						    	if(arrSQLTerms[i].objValue != p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
						    	}
					    	else if(arrSQLTerms[i].strOperator.equals(">")){
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue > (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())<0){
							    		result.add(p.records.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==-1){
							    		result.add(p.records.get(recNo));
					    			}
					    		}
					    		
					    	}
					    	else if(arrSQLTerms[i].strOperator.equals("<")){
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}
					    	else if(arrSQLTerms[i].strOperator.equals(">=")){
					    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
					    	
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}
					    	else if(arrSQLTerms[i].strOperator.equals("<=")){
					    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}

					    }//end of one page
					    
					}//end of OR
				}
				if(strarrOperators[i-1].equals("XOR")){
					Vector<Hashtable<String, Object>> resultTemp = new Vector<Hashtable<String, Object>>();
					for(int recNo = 0; recNo<result.size();recNo++){
						
				    	if(arrSQLTerms[i].strOperator.equals("=")){
				    	if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
				    		resultTemp.add(result.get(recNo));
				    	}
				    	}
				    	if(arrSQLTerms[i].strOperator.equals("!=")){
					    	if(arrSQLTerms[i].objValue != resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
					    	}
				    	else if(arrSQLTerms[i].strOperator.equals(">")){
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
				    		if((Integer)arrSQLTerms[i].objValue > (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
						}
				    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
				    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())<0){
						    		resultTemp.add(result.get(recNo));
				    			}
				    		}
				    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
				    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==-1){
						    		resultTemp.add(result.get(recNo));
				    			}
				    		}
				    		
				    	}
				    	else if(arrSQLTerms[i].strOperator.equals("<")){
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				    	else if(arrSQLTerms[i].strOperator.equals(">=")){
				    		if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
				    	
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				    	else if(arrSQLTerms[i].strOperator.equals("<=")){
				    		if(arrSQLTerms[i].objValue == resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		resultTemp.add(result.get(recNo));
					    	}
				    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue < (Integer)resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		resultTemp.add(result.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(resultTemp.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
							    		resultTemp.add(result.get(recNo));
					    			}
					    		}
						}
				}
					
					Table t = null;//table deserialization
					FileInputStream fileIn = new FileInputStream(arrSQLTerms[i].strTableName +".ser");
				    ObjectInputStream in = new ObjectInputStream(fileIn);
				    t= (Table)in.readObject();
				    in.close();
				    fileIn.close();
					for(int ii = 1; ii<=t.pageNo;ii++){
						Page p = null;
						FileInputStream fileIn1 = new FileInputStream(arrSQLTerms[i].strTableName+ Integer.toString(ii) +".ser");
					    ObjectInputStream in1 = new ObjectInputStream(fileIn1);
					    p = (Page)in1.readObject();
					    in1.close();
					    fileIn.close();
					    for(int recNo = 0; recNo < p.records.size();recNo++){
					    	
					    	if(arrSQLTerms[i].strOperator.equals("=")){
					    	if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
					    		
					    		result.add(p.records.get(recNo));
					    	}
					    	}
					    	if(arrSQLTerms[i].strOperator.equals("!=")){
						    	if(arrSQLTerms[i].objValue != p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
						    	}
					    	else if(arrSQLTerms[i].strOperator.equals(">")){
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
					    		if((Integer)arrSQLTerms[i].objValue > (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
							}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
					    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())<0){
							    		result.add(p.records.get(recNo));
					    			}
					    		}
					    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
					    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==-1){
							    		result.add(p.records.get(recNo));
					    			}
					    		}
					    		
					    	}
					    	else if(arrSQLTerms[i].strOperator.equals("<")){
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}
					    	else if(arrSQLTerms[i].strOperator.equals(">=")){
					    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
					    	
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}
					    	else if(arrSQLTerms[i].strOperator.equals("<=")){
					    		if(arrSQLTerms[i].objValue == p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
						    		result.add(p.records.get(recNo));
						    	}
					    		if(arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Integer")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Double")){
						    		if((Integer)arrSQLTerms[i].objValue < (Integer)p.records.get(recNo).get(arrSQLTerms[i].strColumnName)){
							    		result.add(p.records.get(recNo));
							    	}
								}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("String")||arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Date")){
						    			if(arrSQLTerms[i].objValue.toString().compareTo(p.records.get(recNo).get(arrSQLTerms[i].strColumnName).toString())>0){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
						    		else if (arrSQLTerms[i].objValue.getClass().getSimpleName().toString().equals("Polygon")){
						    			if(comparePP((Polygon)(arrSQLTerms[i].objValue),(Polygon)(p.records.get(recNo).get(arrSQLTerms[i].strColumnName)))==1){
								    		result.add(p.records.get(recNo));
						    			}
						    		}
							}

					    }//end of one page
					    
					}
					for(int mainres = 0; mainres < result.size(); mainres++){
						for(int tempres = 0; tempres < resultTemp.size(); tempres++){
							if(result.get(mainres).equals(resultTemp.get(tempres))){
								result.remove(mainres);
							}
						}
					}
			
				}
			}
		}
	
		
			
		
		else{throw new DBAppException("column does not exist");}
	}
	else {throw new DBAppException("Table does not exist");}
	
	}
return result.iterator();
}


public static String getIndexedCol(String strTableName) throws IOException{
	String row;
	ArrayList rowData = new ArrayList();
	ArrayList rowDataCellType = new ArrayList();
	ArrayList rowDataCellTname = new ArrayList();
	ArrayList rowDataCellCname = new ArrayList();
	ArrayList rowDataCellmaxN = new ArrayList();
	ArrayList rowDataCellCK = new ArrayList();
	ArrayList rowDataIndex = new ArrayList();
	Hashtable<String, Object> empty = new Hashtable();
	Boolean correctFormat = true;

	BufferedReader csvReader = new BufferedReader(new FileReader("metadata.csv"));
	while ((row = csvReader.readLine()) != null) {
	    String[] data = row.split("/n");
	    rowData.add(data[0]);
	    String[] data2 = row.split(",");	
	    rowDataCellTname.add(data2[0]);
	    rowDataCellCname.add(data2[1]);
	    rowDataCellType.add(data2[2]);
	    rowDataCellCK.add(data2[3]);
		rowDataCellmaxN.add(data2[4]);
		rowDataIndex.add(data2[5]);

	    }
	int count = 0;
	
	for(int i = 0; i < rowData.size()-1;i++){
		if(rowDataCellTname.get(i).equals(strTableName)){
			break;
		}
		else{
			count += 1;
		}
		}
	Vector<String> typeData = new Vector<String>();
	Vector<String> nameData = new Vector<String>();
	Vector<String> CKData = new Vector<String>();
	Vector<String> indexData = new Vector<String>();
	int maxN = 0;
	for(int j = count;j<rowData.size();j++){
		if(!rowDataCellTname.get(j).equals(strTableName)){
			break;
		}
		else{
			typeData.add((String)rowDataCellType.get(j));
			nameData.add((String)rowDataCellCname.get(j));
			CKData.add((String)rowDataCellCK.get(j));
			indexData.add((String)rowDataIndex.get(j));
			maxN = (Integer.parseInt((String)rowDataCellmaxN.get(j)));

		}
	}
	for(int k = 0; k < nameData.size(); k++){
		if(indexData.get(k) == "True"){
			return nameData.get(k);
		}
	}
	return "Error";
}

public static void main(String [] args) throws IOException, DBAppException, ClassNotFoundException {
	String strTableName = "Student"; 
	DBApp dbApp = new DBApp( ); 
	Hashtable htblColNameType = new Hashtable( ); 
	htblColNameType.put("id", "java.lang.Integer"); 
	htblColNameType.put("name", "java.lang.String"); 
	htblColNameType.put("gpa", "java.lang.double"); 
	dbApp.createTable( strTableName, "id", htblColNameType, 20 ); 
	dbApp.createBIndex( strTableName, "name"  ); 
	 
	Hashtable htblColNameValue = new Hashtable( ); htblColNameValue.put("id", new Integer( 2343432 )); htblColNameValue.put("name", new String("Ahmed Noor" ) ); htblColNameValue.put("gpa", new Double( 0.95 ) ); dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	 
	htblColNameValue.clear( ); htblColNameValue.put("id", new Integer( 453455 )); htblColNameValue.put("name", new String("Ahmed Noor" ) ); htblColNameValue.put("gpa", new Double( 0.95 ) ); dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	 
	htblColNameValue.clear( ); htblColNameValue.put("id", new Integer( 5674567 )); htblColNameValue.put("name", new String("Dalia Noor" ) ); htblColNameValue.put("gpa", new Double( 1.25 ) ); dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	 
	htblColNameValue.clear( ); htblColNameValue.put("id", new Integer( 23498 )); htblColNameValue.put("name", new String("John Noor" ) ); htblColNameValue.put("gpa", new Double( 1.5 ) ); dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	 
	htblColNameValue.clear( ); htblColNameValue.put("id", new Integer( 78452 )); htblColNameValue.put("name", new String("Zaky Noor" ) ); htblColNameValue.put("gpa", new Double( 0.88 ) ); dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	
	 
	 

	 

    
    


    
 }
}
