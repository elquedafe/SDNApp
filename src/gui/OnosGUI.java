package gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import arquitectura.Entorno;
import arquitectura.Switch;
import tools.JsonParser;

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
				panelTab.addTab("Topologia",topologia);
				panelTab.addTab("Monitor",monitorizacion);
				panelTab.addTab("Flujos",flujos);
				panelTab.addTab("Estadisticas",estadisticas);
				panelTab.setSelectedIndex(2);
				//MENU
				JMenu barraMenu = new JMenu();
				JMenu archivo = new JMenu("Archivo");
				JMenuItem info = new JMenuItem("App Info");
				
				//AÑADIR A LA BARRA
				archivo.add(info);
				barraMenu.add(archivo);
				
				//CODE
				String json = "";
				final String endpoint = "http://192.168.56.101:8181/onos/v1";
				Entorno entorno = new Entorno();
				JsonParser parser = new JsonParser(entorno);
				
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
					String jsonAux = "";
					public void actionPerformed(ActionEvent evt) {
		                //...Perform a task...

		                try {
		                	json = "";
		                	json = parser.getJSON(new URL(endpoint + "/devices"));
		                	parser.parseoJsonTopologia(json);
		                	System.out.println(json);
							for(Entry<String, Switch> auxswitch : entorno.getMapSwitches().entrySet()) {
								topologia.add(new JLabel(auxswitch.getValue().getId()));
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        };
		        Timer timerTopologia = new Timer(1000 ,topologiaTimeout);
		        timerTopologia.setRepeats(true); //Se repite
		        timerTopologia.start();
		        
				//AÑADIR COMPONENTES AL FRAME
				f.add(barraMenu);
				f.add(panelTab);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setMinimumSize(new Dimension(400, 300));
				f.pack();
				f.setVisible(true);
			}
			
			
		});

	}
}
