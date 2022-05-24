import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;


public class Table implements Serializable {
int pageNo;
String name;
int columnNo;
private transient String clusterKeyName;
private transient Hashtable<String, String> columnDataTypesNames;
Vector<Page> pages;
public Table(String name, String clusterKey, Hashtable<String, String> columnDataTypes){
	pageNo = 1;
	this.name = name;
	clusterKeyName = clusterKey;
	columnDataTypesNames = columnDataTypes;
	columnNo = columnDataTypes.size();
}
}
