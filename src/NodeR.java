import java.io.Serializable;
import java.util.ArrayList;

public class NodeR implements Serializable{
	Rtree tree;
	int max;
	int min;
	String parent;
	int nodeNo;
	
	public NodeR(Rtree tree, String parent)
	{
		this.parent = parent;
		this.tree = tree;
		this.max = tree.n;
	}
}
