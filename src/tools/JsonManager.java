package tools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.stream.JsonReader;

import arquitectura.Cluster;
import arquitectura.Entorno;
import arquitectura.Flow;
import arquitectura.Link;
import arquitectura.Switch;

public class JsonManager {
	private Link auxLink = null;
	private Cluster auxCluster = null;
	private Entorno entorno;
	private JsonReader reader;
	public JsonManager(Entorno entorno) {
		this.entorno = entorno;
	}
	
	public String getJSON(URL url) throws IOException{
        String encoding;
        String line;
        String json="";
        HttpURLConnection connection = null;
		try {
			encoding = Base64.getEncoder().encodeToString(("onos:rocks").getBytes("UTF-8"));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        InputStream content = (InputStream)connection.getInputStream();
	        BufferedReader in   = 
	            new BufferedReader (new InputStreamReader (content));
	        while ((line = in.readLine()) != null) {
	            //System.out.println(line);
	            json += line+"\n";
	        }
		} catch (IOException e) {
			throw new IOException(e);
		}
		finally{
			if(connection != null)
				connection.disconnect();
		}
        
        return json;
	}
	
	public void parseoJsonLinks(String json) {
		String nombre = "";
		entorno.getListLinks().clear();
		reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("links")) {
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArrayLinks(reader);
					}
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void parseoJsonTopologia(String json) {
		entorno.getMapSwitches().clear();
		String nombre = "";
		reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("devices")) {
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArrayDevices(reader);
					}
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void leerElementoArrayDevices(JsonReader reader) {
		String elemento = "";
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("id")) {
					entorno.addSwitch(reader.nextString());
				}
				else
					reader.skipValue();
			}
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void leerElementoArrayLinks(JsonReader reader) {
		String elemento = "";
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("src")) {
					auxLink = new Link();
					reader.beginObject();
					while(reader.hasNext()){
						elemento = reader.nextName();
						if(elemento.equals("port")) {
							auxLink.setSrcPort(reader.nextString());
						}
						else if(elemento.equals("device")) {
							auxLink.setSrc(reader.nextString());
						}
						else
							reader.skipValue();
					}
					reader.endObject();
				}
				else if(elemento.equals("dst")) {
					reader.beginObject();
					while(reader.hasNext()){
						elemento = reader.nextName();
						if(elemento.equals("port")) {
							auxLink.setDstPort(reader.nextString());
						}
						else if(elemento.equals("device")) {
							auxLink.setDst(reader.nextString());
						}
						else
							reader.skipValue();
					}
					reader.endObject();
				}
				else if(elemento.equals("state") && reader.nextString().equals("ACTIVE")){
					if(!duplicado(auxLink))
						entorno.addLink(auxLink);
					auxLink = null;
				}
				else
					reader.skipValue();
			}
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	private boolean duplicado(Link nuevoLink) {
		boolean duplicado = false;
		for(Link link : entorno.getListLinks()) {
			if(link.getDst().equals(nuevoLink.getSrc()) && link.getDstPort().equals(nuevoLink.getSrcPort()) && link.getSrc().equals(nuevoLink.getDst()) && link.getSrcPort().equals(nuevoLink.getDstPort())) {
				duplicado = true;
				break;
			}
			else if(link.getDst().equals(nuevoLink.getDst()) && link.getDstPort().equals(nuevoLink.getDstPort()) && link.getSrc().equals(nuevoLink.getSrc()) && link.getSrcPort().equals(nuevoLink.getSrcPort())) {
				duplicado = true;
				break;
			}
		}
		return duplicado;
	}

	public void parseoJsonClusters(String json) {
		entorno.getListClusters().clear();
		String nombre = "";
		reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("nodes")) {
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArrayClusters(reader);
					}
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void leerElementoArrayClusters(JsonReader reader) {
		String elemento = "";
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("id")) {
					auxCluster = new Cluster();
					auxCluster.setId(reader.nextString());
				}
				else if(elemento.equals("tcpPort")) {
					auxCluster.setPuerto(reader.nextString());
				}
				else if(elemento.equals("status")) {
					auxCluster.setEstado(reader.nextString());
					entorno.addCluster(auxCluster);
					auxCluster = null;
				}
				else
					reader.skipValue();
			}
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double parseoJsonPaths(String json) {
		String nombre = "";
		double coste = 0;
		reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("paths")) {
					reader.beginArray();
					while(reader.hasNext()){
						coste = leerElementoArrayPaths(reader);
					}
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return coste;
		
	}
	
	private double leerElementoArrayPaths(JsonReader reader) {
		String elemento = "";
		double coste = 0;
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("cost")) {
					coste = Double.parseDouble(reader.nextString());
				}
				else
					reader.skipValue();
			}
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return coste;
	}

	public void parseoJsonFlow(String json) {
		for(Switch sw : entorno.getMapSwitches().values()){
			sw.getMapFlows().clear();
		}
		String nombre = "";
		reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("flows")) {
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArrayFlow(reader);
					}
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void leerElementoArrayFlow(JsonReader reader) {
		String elemento = "";
		Flow flow = null;
		String sw = "";
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("id")) {
					flow = new Flow(reader.nextString());
				}
				else if(elemento.equals("tableId")) {
					flow.setIdTable(Integer.parseInt(reader.nextString()));;
				}
				else if(elemento.equals("groupId")) {
					flow.setIdGrupo(Integer.parseInt(reader.nextString()));;
				}
				else if(elemento.equals("priority")) {
					flow.setPrioridad(Integer.parseInt(reader.nextString()));;
				}
				else if(elemento.equals("groupId")) {
					flow.setIdGrupo(Integer.parseInt(reader.nextString()));;
				}
				else if(elemento.equals("deviceId")) {
					sw = reader.nextString();
					flow.setSwitch(sw);
				}
				else if(elemento.equals("state")) {
					flow.setEstado(reader.nextString());
				}
				else if(elemento.equals("packets")) {
					flow.setnPaquetes(Integer.parseInt(reader.nextString()));;
				}
				else if(elemento.equals("bytes")) {
					flow.setnBytes(Integer.parseInt(reader.nextString()));;
				}
				else
					reader.skipValue();
			}
			reader.endObject();
			entorno.getMapSwitches().get(sw).addFlow(flow);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

