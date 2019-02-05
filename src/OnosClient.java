import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;

import arquitectura.Cluster;
import arquitectura.Entorno;
import arquitectura.Flow;
import arquitectura.Link;
import arquitectura.Switch;
import tools.JsonParser;


public class OnosClient extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		String json = "";
		final String endpoint = "http://192.168.56.101:8181/onos/v1";
		Entorno entorno = new Entorno();
		JsonParser parser = new JsonParser(entorno);
		try {
			URL urlClusters = new URL(endpoint + "/cluster");
			URL urlTopology = new URL(endpoint + "/devices");
			URL urlLinks = new URL(endpoint + "/links");

			// CLUSTERS
			json = parser.getJSON(urlClusters);
			parser.parseoJsonClusters(json);

			System.out.println("***CLUSTERS***");
			for(Cluster cluster: entorno.getListClusters()) {
				System.out.println("\t" + cluster.toString());
			}

			// SWITCHES
			json = parser.getJSON(urlTopology);
			parser.parseoJsonTopologia(json);

			System.out.println("\n***SWITCHES***");
			for (Entry<String, Switch> auxswitch : entorno.getMapSwitches().entrySet()) {      
				System.out.println("\t" + auxswitch.getKey());
			}

			//ENLACES
			json = parser.getJSON(urlLinks);
			parser.parseoJsonLinks(json);

			System.out.println("\n***ENLACES***");
			for(Link link: entorno.getListLinks()) {
				//COSTE
				json = parser.getJSON(new URL(endpoint + "/paths/" + link.getSrc() + "/" + link.getDst()));
				link.setCost(parser.parseoJsonPaths(json));
				System.out.println("\t" + link.toString());
			}

			//////////////////////////
			Scanner sc = new Scanner(System.in);
			int opcionMenu = 0;
			Map<Integer, String> menuSwitch;
			do {

				int i = 0;
				menuSwitch = new HashMap<Integer, String>();
				//Mapa auxiliar int,nombreSwitch para pintar el menu
				for(Entry<String, Switch> ovs : entorno.getMapSwitches().entrySet()) {
					menuSwitch.put(i, ovs.getValue().getId());
					i++;            	
				}

				System.out.println("\n***Selecciona el nodo del que desea consultar sus flujos***");
				for (Entry<Integer, String> auxswitch : menuSwitch.entrySet()) {      
					System.out.println("\t" + auxswitch.getKey() + ".- " + auxswitch.getValue());
				}

				Switch ovs = null;
				try {
					opcionMenu = Integer.parseInt(sc.nextLine());
					ovs = entorno.getMapSwitches().get(menuSwitch.get(opcionMenu));
					menuSwitch.clear();
					if(ovs != null) {
						System.out.println("Switch elegido: " + ovs.getId());

						//FLUJOS
						URL urlFlow = new URL("http://192.168.56.101:8181/onos/v1/flows/" + ovs.getId());
	
						json = parser.getJSON(urlFlow);
						parser.parseoJsonFlow(json);
	
						for(Entry<String, Flow> flow : entorno.getMapSwitches().get(ovs.getId()).getMapFlows().entrySet()) {
							System.out.println(flow.toString());
						}
					}

				}catch(NumberFormatException e ) {
					e.printStackTrace();
				}
			}while(opcionMenu >= 0);
			//URL urlStats = null;
			//json = parser.getJSON(new URL(urlStats + "/" + entorno.getListSwitches().get(switchElegido).getId()));
			sc.close();


		} catch(IOException e) {
			e.printStackTrace();
		}

	}
}

