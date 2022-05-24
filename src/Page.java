import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;


public class Page implements Serializable{
String name;
Vector<Hashtable<String, Object>> records;
public Page(String name){
	this.name = name;
	records  = new Vector<Hashtable<String,Object>>();
}
}
