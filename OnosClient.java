import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import com.google.gson.stream.JsonReader;


public class OnosClient extends HttpServlet{
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
		try {
            
			String endpoint = "http://192.168.56.101:8181/onos/v1";
			URL urlTopology = new URL(endpoint + "/devices");
			URL urlLinks = new URL(endpoint + "/links");
			
			String json = getJSON(urlTopology);
            parseoJsonTopologia(json);
           	
            json = getJSON(urlLinks);
            parseoJsonLinks(json);
            
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
            URL urlStats = null;
            json = getJSON(new URL(urlStats + "/" + mapSwitches.get(switchElegido)));
            
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
		
	}

	private static void parseoJsonLinks(String json) {
		String nombre = "";
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("links")) {
					System.out.println("\n***ENLACES DE RED***");
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArray(reader);
					}
					reader.endArray();
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

	private static void parseoJsonTopologia(String json) {
		String nombre = "";
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		try {
			reader.beginObject();
			while(reader.hasNext()){
				nombre = reader.nextName();
				if(nombre.equals("devices")) {
					System.out.println("\n***DISPOSITIVOS DE RED***");
					reader.beginArray();
					while(reader.hasNext()){
						leerElementoArray(reader);
					}
					reader.endArray();
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
				if(elemento.equals("id")) {
					aux = reader.nextString();
					nSwitches++;
					mapSwitches.put(nSwitches, aux);
				}
				else if(elemento.equals("src")) {
					reader.beginObject();
					
					reader.endObject();
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
	private static String getJSON(URL url) throws IOException{
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
	            System.out.println(line);
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
}
