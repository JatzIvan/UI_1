package krizovatka;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.IOException;

import krizovatka.CarDetail;
import krizovatka.Node;

public class Main {
    static Stack Front = new Stack();
    static ArrayList<Node> FinalPath = new ArrayList<Node>();
    static HashMap<String, Integer> VisitedState = new HashMap<String,Integer>();
    static int NumberOfNodes = 0;
  
/*
 * funkcia zisti, ci dany uzol uz nebol navstiveny. Ak bol navstiveny tak skontroluje v akej hlbke.
 * Ak je teraz vytvoreny v nizsej hlbke, tak zaradi do fronty a pouzije, hlbka v HashMape sa prepise. 
 * Ak je vytvoreny vo vacsej hlbke, tak sa uzol zahodi. Takto s pouzitim minimalnej pomocnej pamate dokazem algoritmus
 * zefektivnit.
 */
    
    public static boolean checkDub(Node NewNode, String carColor) {
    	String HashKey = "";
    	CarDetail CarStatus[]=NewNode.getCarStatus();
    	for(int k=0;k<CarStatus.length;k++) {
    		HashKey += CarStatus[k].getPositionX();
    		HashKey += CarStatus[k].getPositionY();
    	}
    	if(VisitedState.containsKey(HashKey)) {
    		int NodeDepth = VisitedState.get(HashKey);
    		if(NodeDepth > NewNode.getDepth()) {
    			VisitedState.replace(HashKey, NewNode.getDepth());
    			Front.push(NewNode);
    		}
    		return false;
    	}
    	return true;
    }
    
/*
 * Funkcia zisti, ci sa na danom mieste v mape nachadza nejake vozidlo  
 */
    
    public static boolean isThere(CarDetail Car, int x, int y) {
    	for(int i=0;i<Car.getLength();i++) {
    		if(Car.getOrientation() == 'v') {
    			if((Car.getPositionX() == x+1)&&(Car.getPositionY() +i ==y+1))
    				return true;
    		}
    		if(Car.getOrientation() =='h') {
    			if((Car.getPositionX() +i ==x+1)&&(Car.getPositionY() ==y+1))
    				return true;
    		}
    	}
    	return false;
    }
    
/*
 * Funkcia vypise reprezentaciu mapy v danom stave na konzolu pre jednoduchsiu kontrolu.
 * Miesta bez vozidla su zaznacene '*' a miesta s vozidlom su zaznacene pismenkom(je to 1. pismenko z farby)
 */
    
    public static void PrintMap(Node currentNode) 
    {
    	char Map[][]=new char[6][6];
    	for(int i=0;i<6;i++) {
    		for(int j=0;j<6;j++) {
    			Map[i][j]='*';
    		}
    	}
    	CarDetail Cars[]=currentNode.getCarStatus();
    	for(int i=0;i<6;i++) {
    		for(int j=0;j<6;j++) {
    			for(int k=0;k<Cars.length;k++) {
    				if(isThere(Cars[k],j,i)) {
    					Map[i][j]=Cars[k].getColor().charAt(0);
    					break;
    				}
    			}
    		}
    	}
    	for(int i=0;i<6;i++) {
    		for(int j=0;j<6;j++) {
    			System.out.print(Map[i][j]+" ");
    		}
    		System.out.println("");
    	}
    }
    
 /*
  * Funckia zisti, ci vozidlo nenarazi na koliziu v bitmape. Funkcia kontroluje koliziu pri pohybe dole a vpravo.
  * Vzdy sa snazi danym vozidlom pohnut az po jeho zadany pohyb a pritom testuje ci v ceste nie je ine vozilo. 
  * Ak je v ceste vozilo, vrati false. Inak vrati true
  */
    
    public static boolean checkcollisionDR(int posX, int posY, char orientation, int move, int length, int[][] CurrentMap) {
    	if(orientation=='h') {
    		for(int i=1;i<move+1;i++) {
    			if(CurrentMap[posX-1+i+length-1][posY-1]==1)
    				return false;
    		}
    		return true;
    	}if(orientation=='v') {
    		for(int i=1;i<move+1;i++) {
    			if(CurrentMap[posX-1][posY-1+i+length-1]==1)
    				return false;
    		}
    		return true;
    	}
    	return true;
    }
    
/*
 * Funckia zisti, ci vozidlo nenarazi na koliziu v bitmape. Funkcia kontroluje koliziu pri pohybe hore a vlavo.
 * Vzdy sa snazi danym vozidlom pohnut az po jeho zadany pohyb a pritom testuje ci v ceste nie je ine vozilo. 
 * Ak je v ceste vozilo, vrati false. Inak vrati true
 */
    
    public static boolean checkcollisionUL(int posX, int posY, char orientation, int move, int length, int[][] CurrentMap) {
    	if(orientation=='h') {
    		for(int i=1;i<move+1;i++) {
    			if(CurrentMap[posX-1-i][posY-1]==1)
    				return false;
    		}
    		return true;
    	}if(orientation=='v') {
    		for(int i=1;i<move+1;i++) {
    			if(CurrentMap[posX-1][posY-1-i]==1)
    				return false;
    		}
    		return true;
    	}
    	return true;
    }
    
/*
 * Funkcia sluzi na vytvorenie mapy pre dany stav. Ak sa mieste v mape (Map[x][y]) nachadza nejake vozidlo, je tam vlozena 1
 * ak sa tam vozidlo nenachadza tak je tam vlozena 0. Tuto mapu budem pouzivat pri rieseni kolizii, tvoreni nasledujucich stavov.
 */
    
    public static int[][] CreateMap(Node currNode){
              int Map[][]=new int[6][6];
              for(int i=0;i<6;i++) {
            	  for(int j=0;j<6;j++) {
            		  Map[i][j]=0;
            	  }
              }
              CarDetail CarStatus[] = currNode.getCarStatus();
              for(int i=0;i<CarStatus.length;i++) {
            	  for(int j=0;j<CarStatus[i].getLength();j++) {
            		  if(CarStatus[i].getOrientation() == 'h'){
            			  Map[CarStatus[i].getPositionX()-1+j][CarStatus[i].getPositionY()-1]=1;
            		  }if(CarStatus[i].getOrientation() == 'v'){
            			  Map[CarStatus[i].getPositionX()-1][CarStatus[i].getPositionY()-1+j]=1;
            		  }
            	  }
              }
              return Map;
      }

/*
 * Funkcia sluzi na zoskladanie finalnej cesty pre vypis. Vyberie cestu od posledneho stavu az po prvy (zaciatocny) stav.
 * Cesta sa ulozi do pola s nazvom FinalPath.    
 */
    
    public static void printPath(Node currNode, int depth) {
          Node AktNode = currNode;
          while(true) {
        	  FinalPath.add(AktNode);
        	  if(AktNode.getNodeSpec() == 'F')
        		  break;
        	  AktNode = AktNode.getParentNode();
          }
     }

/*
 * Funkcia sluzi na vytvorenie vsetkych moznych nasledujucich stavov z aktualneho stavu.
 * Vzdy tvori pohyby postupne, zacina od pohybu dlzky 1 az po maximum. Vzdy kontroluje ci je dany pohyb validny.
 * Skontroluje kolizie a duplikaty    
 */
    
    public static void CreateFront(Node currNode, int depth) 
    {
    	int CurrentMap[][]=new int[6][6];
    	CurrentMap = CreateMap(currNode);
    	CarDetail CarStatus[] = currNode.getCarStatus();
    	for(int i=0;i<CarStatus.length;i++) {
    		for(int j=1;j<6;j++) {
    			if(CarStatus[i].getOrientation() == 'h') {
    				if((CarStatus[i].getLength() -1 + (CarStatus[i].getPositionX()+j)) <=6) {
    					if(checkcollisionDR(CarStatus[i].getPositionX(), CarStatus[i].getPositionY(), 'h', j, CarStatus[i].getLength(), CurrentMap)) {
    						CarStatus[i].setPositionX(CarStatus[i].getPositionX()+j);
    						CarDetail NewCarStatus[] = new CarDetail[CarStatus.length];
    						for(int k=0;k<CarStatus.length;k++) {
    							NewCarStatus[k]=new CarDetail(CarStatus[k].getOrientation(), CarStatus[k].getColor(), CarStatus[k].getLength(), CarStatus[k].getPositionX(), CarStatus[k].getPositionY());
    						}
    						Node NewNode = new Node(NewCarStatus, currNode, depth+1);
					    	NewNode.setMove(CarStatus[i].getColor(), j, "VPRAVO");
    						if(checkDub(NewNode, CarStatus[i].getColor())) {
    							String HashKey = "";
    					    	CarDetail CarStatusS[]=NewNode.getCarStatus();
    					    	for(int k=0;k<CarStatusS.length;k++) {
    					    		HashKey += CarStatusS[k].getPositionX();
    					    		HashKey += CarStatusS[k].getPositionY();
    					    	}
    					    	VisitedState.put(HashKey, NewNode.getDepth());
    					    	Front.push(NewNode);
    						}
    						CarStatus[i].setPositionX(CarStatus[i].getPositionX()-j);
    					}
    				}
    				if((CarStatus[i].getPositionX()-j) >= 1) {
    					if(checkcollisionUL(CarStatus[i].getPositionX(), CarStatus[i].getPositionY(), 'h', j, CarStatus[i].getLength(), CurrentMap)) {
    						CarStatus[i].setPositionX(CarStatus[i].getPositionX()-j);
    						CarDetail NewCarStatus[] = new CarDetail[CarStatus.length];
    						for(int k=0;k<CarStatus.length;k++) {
    							NewCarStatus[k]=new CarDetail(CarStatus[k].getOrientation(), CarStatus[k].getColor(), CarStatus[k].getLength(), CarStatus[k].getPositionX(), CarStatus[k].getPositionY());
    						}
    						Node NewNode = new Node(NewCarStatus, currNode, depth+1);
    						NewNode.setMove(CarStatus[i].getColor(), j, "VLAVO");
    						if(checkDub(NewNode, CarStatus[i].getColor())) {
    							String HashKey = "";
    					    	CarDetail CarStatusS[]=NewNode.getCarStatus();
    					    	for(int k=0;k<CarStatusS.length;k++) {
    					    		HashKey += CarStatusS[k].getPositionX();
    					    		HashKey += CarStatusS[k].getPositionY();
    					    	}
    					    	VisitedState.put(HashKey, NewNode.getDepth());
    					    	Front.push(NewNode);
    						}
    						CarStatus[i].setPositionX(CarStatus[i].getPositionX()+j);
    					}
    				}
    			}
    			if(CarStatus[i].getOrientation() == 'v') {
    				if((CarStatus[i].getLength() -1 + (CarStatus[i].getPositionY()+j)) <= 6) {
    					if(checkcollisionDR(CarStatus[i].getPositionX(), CarStatus[i].getPositionY(), 'v', j, CarStatus[i].getLength(), CurrentMap)) {
    						CarStatus[i].setPositionY(CarStatus[i].getPositionY()+j);
    						CarDetail NewCarStatus[] = new CarDetail[CarStatus.length];
    						for(int k=0;k<CarStatus.length;k++) {
    							NewCarStatus[k]=new CarDetail(CarStatus[k].getOrientation(), CarStatus[k].getColor(), CarStatus[k].getLength(), CarStatus[k].getPositionX(), CarStatus[k].getPositionY());
    						}
    						Node NewNode = new Node(NewCarStatus, currNode, depth+1);
    						NewNode.setMove(CarStatus[i].getColor(), j, "DOLE");
    						if(checkDub(NewNode, CarStatus[i].getColor())) {
    							String HashKey = "";
    					    	CarDetail CarStatusS[]=NewNode.getCarStatus();
    					    	for(int k=0;k<CarStatusS.length;k++) {
    					    		HashKey += CarStatusS[k].getPositionX();
    					    		HashKey += CarStatusS[k].getPositionY();
    					    	}
    					    	VisitedState.put(HashKey, NewNode.getDepth());    	
    					    	Front.push(NewNode);
    						}
    						CarStatus[i].setPositionY(CarStatus[i].getPositionY()-j);
    					}
    				}
    				if((CarStatus[i].getPositionY()-j) >= 1) {
    					if(checkcollisionUL(CarStatus[i].getPositionX(), CarStatus[i].getPositionY(), 'v', j, CarStatus[i].getLength(), CurrentMap)) {
    						CarStatus[i].setPositionY(CarStatus[i].getPositionY()-j);
    						CarDetail NewCarStatus[] = new CarDetail[CarStatus.length];
    						for(int k=0;k<CarStatus.length;k++) {
    							NewCarStatus[k]=new CarDetail(CarStatus[k].getOrientation(), CarStatus[k].getColor(), CarStatus[k].getLength(), CarStatus[k].getPositionX(), CarStatus[k].getPositionY());
    						}
    						Node NewNode = new Node(NewCarStatus, currNode, depth+1);
    						NewNode.setMove(CarStatus[i].getColor(), j, "HORE");
    						if(checkDub(NewNode, CarStatus[i].getColor())) {
    							String HashKey = "";
    					    	CarDetail CarStatusS[]=NewNode.getCarStatus();
    					    	for(int k=0;k<CarStatusS.length;k++) {
    					    		HashKey += CarStatusS[k].getPositionX();
    					    		HashKey += CarStatusS[k].getPositionY();
    					    	}
    					    	VisitedState.put(HashKey, NewNode.getDepth());				    	
    					    	Front.push(NewNode);
    						}
    						CarStatus[i].setPositionY(CarStatus[i].getPositionY()+j);
    						
    					}
    				}
    			}
    		}
    	}
    }

/*
 * Funckia kontroluje, ci sa dany uzol(stav) nedostal do cielovej pozicie. Ak ano tak vrati true, inak vrati false.
 * Funkcia si naskor zisti, ktore suradnice patria cervenemu autu, a potom skontroluje ci sa dostal na kraj mapy.    
 */
    
    public static boolean ReachGoal(Node currNode, int depth) 
    {
    	CarDetail RedCar =  null;
    	CarDetail CarStatus[] = currNode.getCarStatus();
    	for(int i=0;i<CarStatus.length;i++) {
    		if(CarStatus[i].getColor().equals("cervene")) {
    			RedCar = CarStatus[i];
    			break;
    		}
    	}
    	if(RedCar.getOrientation() == 'h') {
    		if(RedCar.getLength() -1 + RedCar.getPositionX() == 6) {
    			printPath(currNode, currNode.getDepth());
    			return true;
    		}
    	}
    	if(RedCar.getOrientation() == 'v') {
    		if(RedCar.getLength() -1 + RedCar.getPositionY() == 6) {
    			printPath(currNode, currNode.getDepth());
    			return true;
    		}
    	}
    	if(currNode.getDepth() == depth) {
    		currNode = null;
    		return false;
    	}
    return false;	
    }
    
/*
 * Funkcia, ktora cyklicky zvyšuje hlbkovu hranicu. V cykle potom vola funkciu ReachGoal pre kazdy vrchol z fronty. Ak je vrchol finalny,
 * skonci cyklus a vypisuje cestu. Ak nenajde cestu pre vsetky mozne pohyby, tak prehlasi ze neexistuje riesenie.    
 */
    
	public static boolean IDF(Node firstNode) 
	{
		int depth = 0;
		while(true)
		{
			Front.clear();
			firstNode.setFirst('F');
			Front.push(firstNode);
			VisitedState.clear();
			Node NextNode = firstNode;
			int MaxDepth = 0;
			int pocNode = 0;
			String HashKey = "";
	    	CarDetail CarStatus[]=firstNode.getCarStatus();
	    	for(int k=0;k<CarStatus.length;k++) {
	    		HashKey += CarStatus[k].getPositionX();
	    		HashKey += CarStatus[k].getPositionY();
	    	}
	    	VisitedState.put(HashKey, 0);	
			while(true) 
			{
				NumberOfNodes++;
				if(ReachGoal(NextNode, depth))
					return true;
				if(Front.empty())
					break;
				NextNode = (Node)Front.pop();
				CarDetail s[]= NextNode.getCarStatus();
				
				if(NextNode.getDepth()<depth)
					CreateFront(NextNode, NextNode.getDepth());
				if(NextNode.getDepth() > MaxDepth)
					MaxDepth = NextNode.getDepth();
			}
			if(MaxDepth < depth) {
				System.out.println("Neexistuje");
				return false;
			}
			depth++;
		}
	}

/*
 * V maine citam subor, ziskavam poziciu vozidiel z konzoly. Potom si ulozim informacie do Objektu CarDetail a z toho vytvorim prvy uzol(FirstNode)
 * Na konci taktiez vypisujem pocet spracovanych stavov pocat behu programu.	
 */
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner s = new Scanner(System.in);
		System.out.println("Zadajte '1' pre citanie zo suboru, zadajte '2' pre citanie z konzoly");
		int ReadSwitch;
		while(true)
		{
			ReadSwitch = s.nextInt();
			if((ReadSwitch == 1)||(ReadSwitch == 2))
			break;
		}
		if(ReadSwitch == 1) 
		{
			System.out.println("Zadajte nazov suboru (.txt)");
            System.out.println("Subor musi mat tvar: '(farba dlzka Y X orientacia)'");
            String fileName;
            fileName = s.nextLine();
			while(true) 
			{
				fileName = s.nextLine();
				File myObj = new File(fileName);
				if(myObj.exists()) 
				{
					break;
				}
				System.out.println("Subor neexistuje");
			}
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			String data =  new String();
			//System.out.println("a");
			while (myReader.hasNextLine()) 
			{
		        data = myReader.nextLine();
		     }
		    myReader.close();
		    String CarData[]=data.split("\\)");
		    CarDetail CarArray[]=new CarDetail[CarData.length];
		    for(int i=0;i<CarData.length;i++) {
		    	String CurrentCar[] = CarData[i].split(" ");
		    	CurrentCar[0]=CurrentCar[0].replace("(", "");
		    	CarArray[i]= new CarDetail(CurrentCar[4].charAt(0), CurrentCar[0], Integer.parseInt(CurrentCar[1]), Integer.parseInt(CurrentCar[3]), Integer.parseInt(CurrentCar[2]));
		    }
		    Node FirstNode = new Node(CarArray, null, 0);
		    System.out.println("Starting Map");
		    PrintMap(FirstNode);
		    if(IDF(FirstNode)) {
		    	for(int i=FinalPath.size()-2;i>=0;i--) {
		    		System.out.println(FinalPath.get(i).getMoveD()+"("+FinalPath.get(i).getMoveC()+","+FinalPath.get(i).getMoveL()+")");
		    		PrintMap(FinalPath.get(i));
		    	}
		    	
		    }
		    System.out.println("Pocet spracovanych nodes: "+NumberOfNodes);
		}
		else 
		{
			System.out.println("Zadajte pocet aut");
			int numOfCars = s.nextInt();
			System.out.println("Zadajte vozidla v tvare: farba dlzka Y X orientacia");
			String Buff= "";
			CarDetail CarArrayR[]=new CarDetail[numOfCars];
			Buff=s.nextLine();
			for(int j=0;j<numOfCars;j++) {
				Buff=s.nextLine();
				String BuffS[]=Buff.split(" ");
				CarArrayR[j]=new CarDetail(BuffS[4].charAt(0), BuffS[0], Integer.parseInt(BuffS[1]), Integer.parseInt(BuffS[3]), Integer.parseInt(BuffS[2]));
			}
			Node FirstNodeR = new Node(CarArrayR, null, 0);
		    System.out.println("Starting Map");
		    PrintMap(FirstNodeR);
		    if(IDF(FirstNodeR)) {
		    	for(int i=FinalPath.size()-2;i>=0;i--) {
		    		System.out.println(FinalPath.get(i).getMoveD()+"("+FinalPath.get(i).getMoveC()+","+FinalPath.get(i).getMoveL()+")");
		    		PrintMap(FinalPath.get(i));
		    	}
		    	
		    }
		    System.out.println("Pocet spracovanych nodes: "+NumberOfNodes);
		}
	}
	
}
