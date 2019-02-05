package arquitectura;

public class Flow {
	private String id;
	private int idTable;
	private int idGrupo;
	private int prioridad;
	private String estado;
	private int nPaquetes;
	private int nBytes;
	private String ovs;
	
	public Flow(String id) {
		this.id = id;
		this.idTable = -1;
		this.idGrupo = -1;
		this.prioridad = -1;
		this.estado = "";
		this.nPaquetes = 0;
		this.nBytes = 0;
		this.ovs = "";
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getnPaquetes() {
		return nPaquetes;
	}

	public void setnPaquetes(int nPaquetes) {
		this.nPaquetes = nPaquetes;
	}

	public int getnBytes() {
		return nBytes;
	}

	public void setnBytes(int nBytes) {
		this.nBytes = nBytes;
	}
	
	public int getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	public String getSwitch() {
		return this.ovs;
	}

	public void setSwitch(String ovs) {
		this.ovs = ovs;
	}
	
	public String toString() {
		return this.ovs + "\t--> " + "\t| " + this.nPaquetes+ " paquetes\t|\t" + this.nBytes + " bytes";
	}
}
