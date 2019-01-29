import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.stream.JsonReader;


public class ClientRest extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int nNodos = 0;
	private static int nSwitches = 0;
	private static int nHosts = 0;
	private static Map<Integer, String> mapSwitches = new HashMap<Integer, String>();
	public static void main(String[] args) {
		System.out.println("Ver topologia");
		/*Client cliente;
		WebTarget target;
		cliente = ClientBuilder.newClient();
		target = cliente.target("http://admin:admin@192.168.1.32:8181/restconf/operational"); //http://192.168.43.29:8181/restconf/operational/opendaylight-inventory:nodes
		WebTarget targetComponentes   = target.path("/opendaylight-inventory:nodes");
		Invocation.Builder invocationBuilder = targetComponentes.request(MediaType.APPLICATION_JSON_TYPE);
		Response response = invocationBuilder.get();
		System.out.println("Codigo respuesta: "+response.getStatus());
		String respuesta  = response.readEntity(String.class);
		System.out.println(respuesta);
		*/
		try {
            //URL url = new URL ("http://192.168.1.32:8181/restconf/operational/opendaylight-inventory:nodes");
            //apidocs: URL url = new URL ("http://192.168.1.32:8181/apidoc/explorer/index.html");
            //URL url = new URL ("http://192.168.1.32:8181/apidoc/apis");
			//URL url = new URL ("http://192.168.1.32:8181/restconf/config/network-topology:network-topology");
			URL url = new URL("http://192.168.56.102:8181/restconf/operational/network-topology:network-topology");
			URL urlStats = new URL("http://192.168.56.102:8181/restconf/operational/opendaylight-inventory:nodes/node");
			// URL url = new URL ("http://192.168.56.101:8080/restconf/operational/opendaylight-inventory:nodes/node/openflow:1");
			//URL url = new URL ("http://169.254.82.0:8181/restconf/operational/network-topology:network-topology/");
            /*String encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   = 
                new BufferedReader (new InputStreamReader (content));
            String line;
            String json="";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                json += line;
            }*/
			String json = getJSON(url);
            parseoJsonTopologia(json);
    
            
           //////////////////////////
            int switchElegido = -1;
            Scanner sc = new Scanner(System.in);
            
            System.out.println("\n\n***Selecciona el nodo que desea consultar estadísticas***");
            
            //mapSwitch.forEach((key, value) -> );
            /*for( String s : switches) {
            	i++;
            	System.out.println(i + ".- " + s);
            	
            	
            }*/
            for (Entry<Integer, String> auxswitch : mapSwitches.entrySet()) {      
            	System.out.println("\t" + auxswitch.getKey() + ".- " + auxswitch.getValue());
            }
            
            try {
            	switchElegido = Integer.parseInt(sc.nextLine());

                System.out.println("Switch elegido: " + mapSwitches.get(switchElegido));
            	
                
            }
            catch(NumberFormatException e) {
            	e.printStackTrace();
            }
            finally {
            	sc.close();
            }
            json = getJSON(new URL(urlStats + "/" + mapSwitches.get(switchElegido)));
            System.out.println(json);
            
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
		
	}

	private static void parseoJsonTopologia(String json) {
		String nombre = "";
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("network-topology")) {
					System.out.println("\n***TOPOLOGIA DE RED***");
					reader.beginObject();
					nombre = reader.nextName();
					if(nombre.equals("topology")){
						System.out.println("\n\tTopologia");
						reader.beginArray();
						while(reader.hasNext()){
							leerElementoArray(reader);
						}
						reader.endArray();
					}
					else
						reader.skipValue();
				}
				else
					reader.skipValue();
			}
			//FIN network-topology
			reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	private static void leerElementoArray(JsonReader reader) {
		String elemento = "";
		String aux;
		try {
			reader.beginObject();
			while(reader.hasNext()){
				elemento = reader.nextName();
				if(elemento.equals("topology-id")) {
					System.out.println("\t\tID topologia: " + reader.nextString());
				}
				else if(elemento.equals("node")) {
					System.out.print("\n\t\t**NODOS**");
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArray(reader);
					}
					System.out.print("\n\nHay " + nHosts + " hosts y " + nSwitches + " switches");
					reader.endArray();
				}
				else if(elemento.equals("node-id")) {
					aux = reader.nextString();
					nNodos++;
					if(aux.startsWith("host"))
						nHosts++;
					else if(aux.startsWith("openflow")){
						nSwitches++;
						mapSwitches.put(nSwitches, aux);
					}
					System.out.print("\n\n\t\t\tNodo-" + nNodos+": " + aux);
				}
				else if(elemento.equals("termination-point")) {
					System.out.print("\n\t\t\t\tInterfaces =>");
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArray(reader);
					}
					reader.endArray();					
				}
				else if(elemento.equals("tp-id")) {
					System.out.print(" " + reader.nextString());
				}
				else if(elemento.equals("host-tracker-service:addresses")){
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArray(reader);
					}
					reader.endArray();
				}
				else if(elemento.equals("ip")){
					System.out.print("\n\t\t\t\tIP: " + reader.nextString());
				}
				else
					reader.skipValue();
				
			}
			reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String getJSON(URL url) {
        String encoding;
        String line;
        String json="";
        HttpURLConnection connection;
		try {
			encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + encoding);
	        InputStream content = (InputStream)connection.getInputStream();
	        BufferedReader in   = 
	            new BufferedReader (new InputStreamReader (content));
	        while ((line = in.readLine()) != null) {
	            System.out.println(line);
	            json += line+"\n";
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return json;
	}
}
