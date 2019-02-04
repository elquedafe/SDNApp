package arquitectura;

public class Cluster {
	private String id;
	private String puerto;
	private String estado;
	
	public Cluster(String id, String puerto, String estado) {
		this.id = id;
		this.puerto = puerto;
		this.estado = estado;
	}

	public Cluster(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String toString() {
		return this.estado + "\t" + this.id + ":"+ this.puerto;
	}
	
}
