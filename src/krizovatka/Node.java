package krizovatka;

/*
 * Objekt reprezentujuci jeden uzol. Obsahuje vsetky dolezite informacie. Ukladam tu Rodicovsky uzol, hlbku uzla,
 * pozicie vozidiel v danom uzli(CarStatus[]), a pohyb, ktory sa vykonal pre vytvorenie tohto uzla.
 */

public class Node {
	
	private char NodeSpec = 'X';
	private Node ParentNode;
	private CarDetail CarStatus[];
	private int depth = 0;
	private String MoveC;
	private int MoveL;
	private String MoveD;
	
	public Node(CarDetail CarStatus[], Node Parent, int depth) {
		this.depth = depth;
		this.ParentNode = Parent;
		this.CarStatus = CarStatus.clone();
	}
	public void setMove(String MoveC, int MoveL,String MoveD) {
		this.MoveC = MoveC;
		this.MoveL = MoveL;
		this.MoveD = MoveD;
	}
	public char getNodeSpec() {
		return NodeSpec;
	}
	public Node getParentNode() {
		return ParentNode;
	}
	public int getDepth() {
		return depth;
	}
	public String getMoveC() {
		return MoveC;
	}
	public int getMoveL() {
		return MoveL;
	}
	public String getMoveD() {
		return MoveD;
	}
	public CarDetail[] getCarStatus(){
		return CarStatus;
	}
	public void setFirst(char x) {
		this.NodeSpec = x;
	}
}
