package part1;

public class Instance {

	private double sl;
	private double sw;
	private double pl;
	private double pw;
	private String c;
	private String knnClass;
	private double distance = Double.MAX_VALUE;

	public Instance(double sl, double sw, double pl, double pw, String c){
		this.sl = sl;
		this.sw = sw;
		this.pl = pl;
		this.pw = pw;
		this.c = c;
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		this.sl = sl;
	}

	public double getSw() {
		return sw;
	}

	public void setSw(double sw) {
		this.sw = sw;
	}

	public double getPl() {
		return pl;
	}

	public void setPl(double pl) {
		this.pl = pl;
	}

	public double getPw() {
		return pw;
	}

	public void setPw(double pw) {
		this.pw = pw;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}
	
	public String getKnnClass() {
		return knnClass;
	}

	public void setKnnClass(String knnClass) {
		this.knnClass = knnClass;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
