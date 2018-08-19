package part1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class K_NearestNeighbour {
	
	public static int k = 3;
	public static List<Instance> training = new ArrayList<Instance>();
	public static List<Instance> test = new ArrayList<Instance>();
	//max and mins from iris.names file
	public static double rangeSL = 7.9-4.3;
	public static double rangeSW = 4.4-2.0;
	public static double rangePL = 6.9-1.0;
	public static double rangePW = 2.5-0.1;
	

	public static void main(String[] args){
		
		if(args.length == 3){
			k = Integer.parseInt(args[2]);
		}

		readData(args);
		
		kNearestNeighbourAlgorithm();
		
		calculateAccuracy();
	}

	private static void calculateAccuracy() {
		double count = 0;
		for(Instance i : test){
			if(i.getC().equals(i.getKnnClass())){
				count++;
			}
		}
		double accuracy = (count/test.size())*100;
		
		System.out.println("K = "+k+"\n");
		
		System.out.println("Accuracy: "+accuracy+"%\n");
		
		int count2 = 1;
		for(Instance te : test){
			System.out.println(count2 + ": Originally: "+te.getC()+" , KNN Classified: "+te.getKnnClass());
			count2++;
		}
		
	}

	/**
	 * uses distance to classify every test instance using training data
	 */
	private static void kNearestNeighbourAlgorithm() {
		for(Instance te : test){
			
			ArrayList<Instance> kNN = new ArrayList<Instance>();
			
			double sl = te.getSl();
			double sw = te.getSw();
			double pl = te.getPl();
			double pw = te.getPw();
			
			for(Instance tr : training){
				
				double sl2 = tr.getSl();
				double sw2 = tr.getSw();
				double pl2 = tr.getPl();
				double pw2 = tr.getPw();
				
				double dist = Math.sqrt( ((sl-sl2)*(sl-sl2))/rangeSL + ((sw-sw2)*(sw-sw2))/rangeSW + ((pl-pl2)*(pl-pl2))/rangePL + ((pw-pw2)*(pw-pw2))/rangePW);
				
				tr.setDistance(dist);
				
				if(kNN.size() < k){
					kNN.add(tr);
				}else{
					Collections.sort(kNN, new distanceComp());
					if(dist < kNN.get(0).getDistance()){
						kNN.set(0, tr);
					}
				}
			}
			
			classifyInstance(te, kNN);
		}
	}

	/**
	 * classifies instance using nearest neighbours, if two classes have same count use average distance to choose class
	 * @param te
	 * @param kNN
	 */
	private static void classifyInstance(Instance te, ArrayList<Instance> kNN) {
		//count classes
		int iSet = 0;
		int iVer = 0;
		int iVir = 0;
		
		//distance for mean
		double iSetDistance = 0;
		double iVerDistance = 0;
		double iVirDistance = 0;
		
		//loop through k nearest neighbours
		for(Instance i : kNN){
			if(i.getC().equals("Iris-setosa")){
				iSetDistance += i.getDistance();
				iSet++;
			}else if(i.getC().equals("Iris-versicolor")){
				iVerDistance += i.getDistance();
				iVer++;
			}else if(i.getC().equals("Iris-virginica")){
				iVirDistance += i.getDistance();
				iVir++;
			}
		}
		
		//calculate means
		double iSetMean = iSetDistance/iSet;
		double iVerMean = iVerDistance/iVer;
		double iVirMean = iVirDistance/iVir;
		
		//if equal number of classes choose one with lowest mean distance
		if((iSet == iVer && iSet > 0) || (iSet == iVir && iSet > 0) || (iVer == iVir && iVer >0)){
			//if all 3 equal pick one with lowest mean distance
			if(iSet == iVer && iSet == iVir){
				if(Math.min(Math.min(iSetMean,iVerMean),iVirMean) == iSetMean){
					te.setKnnClass("Iris-setosa");
				}else if(Math.min(Math.min(iSetMean,iVerMean),iVirMean) == iVerMean){
					te.setKnnClass("Iris-versicolor");
				}else if(Math.min(Math.min(iSetMean,iVerMean),iVirMean) == iVirMean){
					te.setKnnClass("Iris-virginica");
				}
			}else if(iSet == iVer && iSet > 0){
				if(Math.min(iSetMean, iVerMean) == iSetMean){
					te.setKnnClass("Iris-setosa");
				}else{
					te.setKnnClass("Iris-versicolor");
				}
			}else if(iSet == iVir && iSet > 0){
				if(Math.min(iSetMean, iVirMean) == iSetMean){
					te.setKnnClass("Iris-setosa");
				}else{
					te.setKnnClass("Iris-virginica");
				}
			}else if(iVer == iVir && iSet > 0){
				if(Math.min(iVerMean, iVirMean) == iVerMean){
					te.setKnnClass("Iris-versicolor");
				}else{
					te.setKnnClass("Iris-virginica");
				}
			}
		//else just pick one with highest occurrence
		}else{
			if(Math.max(Math.max(iSet,iVer),iVir) == iSet){
				te.setKnnClass("Iris-setosa");
			}else if(Math.max(Math.max(iSet,iVer),iVir) == iVer){
				te.setKnnClass("Iris-versicolor");
			}else if(Math.max(Math.max(iSet,iVer),iVir) == iVir){
				te.setKnnClass("Iris-virginica");
			}
		}
	}

	/**
	 * read in data from files
	 * @param args
	 */
	private static void readData(String[] args) {
		try {
            File file = new File(args[0]);

            Scanner input = new Scanner(file);

            while (input.hasNextLine() && input.hasNextDouble()) {
            	double sl = input.nextDouble();
				double sw = input.nextDouble();
				double pl = input.nextDouble();
				double pw = input.nextDouble();
				String c = input.next();
				training.add(new Instance(sl,sw,pl,pw,c));
            }
            input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
            File file = new File(args[1]);

            Scanner input = new Scanner(file);

            while (input.hasNextLine() && input.hasNextDouble()) {
            	double sl = input.nextDouble();
				double sw = input.nextDouble();
				double pl = input.nextDouble();
				double pw = input.nextDouble();
				String c = input.next();
				test.add(new Instance(sl,sw,pl,pw,c));
            }
            input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}

//distance comparator for sorting kNN list
class distanceComp implements Comparator<Instance>{

	@Override
	public int compare(Instance arg0, Instance arg1) {
		if(arg0.getDistance() < arg1.getDistance()){
			return 1;
		}else if(arg0.getDistance() > arg1.getDistance()){
			return -1;
		}else{
			return 0;
		}
	}
	
}