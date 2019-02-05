package gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import arquitectura.Entorno;
import arquitectura.Flow;
import arquitectura.Link;
import arquitectura.Switch;
import tools.JsonManager;

public class OnosGUI {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("UPM SDN App"); 
				JTabbedPane panelTab = new JTabbedPane();
				JPanel monitorizacion = new JPanel();
				JPanel flujos = new JPanel();
				JPanel estadisticas = new JPanel();
				JPanel topologia = new JPanel();
				
				
				
				//MENU
				JMenu barraMenu = new JMenu();
				JMenu archivo = new JMenu("Archivo");
				JMenuItem info = new JMenuItem("App Info");
				
				//PANEL TAB
				panelTab.addTab("Topologia",topologia);
				panelTab.addTab("Monitor",monitorizacion);
				panelTab.addTab("Flujos",flujos);
				panelTab.addTab("Estadisticas",estadisticas);
				panelTab.setSelectedIndex(0);
				
				//LISTAS
				DefaultListModel modeloListaFlows = new DefaultListModel();
				JList<Flow> listaFlows = new JList<Flow>(modeloListaFlows);
				estadisticas.add(listaFlows);
				
				DefaultListModel modeloListaLinks = new DefaultListModel();
				JList<Flow> listaLinks = new JList<Flow>(modeloListaLinks);
				topologia.add(listaLinks);
				
				
				//AÑADIR A LA BARRA
				archivo.add(info);
				barraMenu.add(archivo);
				
				//CODE
				final String endpoint = "http://192.168.56.101:8181/onos/v1";
				Entorno entorno = new Entorno();
				JsonManager parser = new JsonManager(entorno);
				
				//DESCUBRIR ENTORNO
				try {
					descubrirEntorno(entorno, parser);
				} catch (IOException e1) {
					//COMPLETAR VENTANA DE AVISO
					e1.printStackTrace();
				}
				
				//
				/*
				ExecutorService executor = Executors.newCachedThreadPool();
				Callable<Object> task = new Callable<Object>() {
				   public Object call() {
				      
					return this.hola();
				   }

				private Object hola() {
					System.out.println("HOLA");
					return null;
				}
				};
				Future<Object> future = executor.submit(task);
				try {
				   Object result = future.get(2, TimeUnit.SECONDS); 
				} catch (TimeoutException ex) {
				   // handle the timeout
				} catch (InterruptedException e) {
				   // handle the interrupts
				} catch (ExecutionException e) {
				   // handle other exceptions
				} finally {
				   //future.cancel(true); // may or may not desire this
				}
				*/
				//
				
				ActionListener topologiaTimeout = new ActionListener() {
					String json = "";
					public void actionPerformed(ActionEvent evt) {
		                //...Perform a task...

		                try {
		                	descubrirEntorno(entorno, parser);
		                	/*json = "";
		                	json = parser.getJSON(new URL(endpoint + "/links"));
		                	parser.parseoJsonLinks(json);
		                	System.out.println(json);*/
		                	
							/*for(Entry<String, Switch> auxswitch : entorno.getMapSwitches().entrySet()) {
								topologia.add(new JLabel(auxswitch.getValue().getId()));
							}*/
		                	modeloListaLinks.clear();
		                	for(Link link : entorno.getListLinks())
		                		modeloListaLinks.addElement(link);

		                	//topologia.updateUI();
		                	
		                	//ESTADISTICAS
		                	/*json = "";
		                	json = parser.getJSON(new URL(endpoint + "/flows"));
		                	parser.parseoJsonFlow(json);

		                	System.out.println(json);*/
		                	modeloListaFlows.clear();
		                	for (Switch auxswitch : entorno.getMapSwitches().values()) { 
		                		
		                		//listaFlows.setListData(new Vector(auxswitch.getMapFlows().values()));
		                		for(Flow flow : auxswitch.getMapFlows().values()) {
			                		//listaFlows.setListData(flow);
		                			modeloListaFlows.addElement(flow);
								}
		        			}
		                	//listaFlows.updateUI();
		                	//estadisticas.updateUI();
		                	
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        };
		        Timer timerTopologia = new Timer(5000 ,topologiaTimeout);
		        timerTopologia.setRepeats(true); //Se repite cuando TRUE
		        timerTopologia.start();
		        
				//AÑADIR COMPONENTES AL FRAME
				f.add(barraMenu);
				f.add(panelTab);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setMinimumSize(new Dimension(400, 300));
				f.pack();
				f.setVisible(true);
			}

			private void descubrirEntorno(Entorno entorno, JsonManager parser) throws IOException {
				String json = "";
				String endpoint = "http://192.168.56.101:8181/onos/v1";
				URL urlClusters = new URL(endpoint + "/cluster");
				URL urlTopology = new URL(endpoint + "/devices");
				URL urlLinks = new URL(endpoint + "/links");
				URL urlFlows = new URL(endpoint + "/flows");

				// CLUSTERS
				json = parser.getJSON(urlClusters);
				parser.parseoJsonClusters(json);
				System.out.println(json);
				System.out.println("***CLUSTERS CARGADOS***");
				
				// SWITCHES
				json = parser.getJSON(urlTopology);
				parser.parseoJsonTopologia(json);
				System.out.println(json);
				System.out.println("\n***SWITCHES CARGADOS***");
				
				//LINKS
				json = parser.getJSON(urlLinks);
				parser.parseoJsonLinks(json);
				System.out.println(json);
				System.out.println("\n***ENLACES CARGADOS***");
				
				//FLOWS
				json = parser.getJSON(urlFlows);
				parser.parseoJsonFlow(json);
				System.out.println(json);
				System.out.println("\n***FLUJOS CARGADOS***");

				System.out.println("\n***TOPOLOGIA CARGADA***");
				
			}
			
			
		});

	}
}
