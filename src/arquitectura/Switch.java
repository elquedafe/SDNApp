package arquitectura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch {
	private String id;
	private List<Port> listPorts;
	private Map<String, Flow> mapFlows;
	
	public Switch(String id) {
		this.id = id;
		this.listPorts = new ArrayList<Port>();
		this.mapFlows = new HashMap<String, Flow>();
	}

	public List<Port> getListPorts() {
		return listPorts;
	}

	public void setListPorts(List<Port> listPorts) {
		this.listPorts = listPorts;
	}

	public Map<String, Flow> getMapFlows() {
		return mapFlows;
	}

	public void setMapFlows(Map<String, Flow> listFlows) {
		this.mapFlows = listFlows;
	}

	public String getId() {
		return id;
	}
	
	public void addFlow(Flow flow) {
		this.mapFlows.put(flow.getId(), flow);
	}
}
