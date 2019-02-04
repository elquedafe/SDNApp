package arquitectura;

public class Link {
	private String src;
	private String dst;
	private String srcPort;
	private String dstPort;
	private double cost;
	
	public Link(String src, String dst, String srcPort, String dstPort) {
		this.src = src;
		this.dst = dst;
		this.srcPort = srcPort;
		this.dstPort = dstPort;
		this.cost = 0;
	}
	
	public Link(){
		this.src = "";
		this.dst = "";
		this.srcPort = "";
		this.dstPort = "";
		this.cost = 0;
	}
	
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	public String getDstPort() {
		return dstPort;
	}
	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String toString() {
		return this.src + "(port: " + this.srcPort + ") ----> " + " (port: " + this.dstPort + ")" + this.dst + "\t Coste: " + this.cost;
	}
	
}
