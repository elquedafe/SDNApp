package arquitectura;

public class Port {
	private String ovs;
	private String numeroPuerto;
	private double velocidad;
	private String mac;
	private String nombrePuerto;
	
	public Port() {
		this.ovs = "";
		this.numeroPuerto = "";
		this.velocidad = 0;
		this.mac = "";
		this.nombrePuerto = "";
	}

	public String getOvs() {
		return ovs;
	}

	public void setOvs(String ovs) {
		this.ovs = ovs;
	}

	public String getNumeroPuerto() {
		return numeroPuerto;
	}

	public void setNumeroPuerto(String numeroPuerto) {
		this.numeroPuerto = numeroPuerto;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getNombrePuerto() {
		return nombrePuerto;
	}

	public void setNombrePuerto(String nombrePuerto) {
		this.nombrePuerto = nombrePuerto;
	}

}
