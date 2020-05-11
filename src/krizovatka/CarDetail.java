package krizovatka;

/*
 * Objekt reprezentujuci jedno vozidlo. Ukladam tu vsetky potrebne informacie pre pouzitie v algoritme.
 * Nachadza sa tu orientacia, dlzka, x a y pozicia a farba vozidla.
 */

public class CarDetail {

	private char orientation;
	private int length;
	private int positionX;
	private int positionY;
	private String color;
	
	public CarDetail(char orientation, String color, int length, int positionX, int positionY){
		this.orientation = orientation;
		this.color = color;
		this.length = length;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public char getOrientation() {
		return orientation;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getPositionX() {
		return positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionX(int newP) {
		this.positionX = newP;
	}
	public void setPositionY(int newP) {
		this.positionY = newP;
	}
}
