/**  
* <p>Title: ParametrosGrupos.java</p>  
* <p>Description: Vista destinada a contener las vistas de los grupos de población
* 	en formato de pestañas laterales. Además garantiza un ordenamiento correcto por
*  número de índice.</p>
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 15 oct. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import controlador.ControladorModulos;
import modelo.IO;
import modelo.Zona;

/**
 * <p>Title: ParametrosGrupos</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 15 oct. 2021
 * @version versión 1.1
 */
public class ParametrosGrupos extends JPanel {
	/** serialVersionUID*/  
	private static final long serialVersionUID = -4920717884036584918L;
	JFrame frame;
	private ControladorModulos cm;
	private JTabbedPane paneles;	
	private int contadorPaneles;
	private HashMap<Integer, VistaZona> vistasZonas;
	
	/**
	 * <p>Title: ParametrosGrupos del proyecto</p>  
	 * <p>Description: Constructor de la clase que recibe los valores esenciales
	 * del proyecto, como por ejemplo número inicial de grupos, nombre del proyecto,
	 * etcétera. </p>  
	 * @param cm Controlador de los módulos. Necesario para el correcto funcionamiento
	 * de la aplicación permitiendo la comunicación y el flujo de datos bidireccional.
	 */
	public ParametrosGrupos(ControladorModulos cm) {
		super();
		this.cm = cm;
		setMaximumSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(1000, 700));
		this.contadorPaneles = 0;
		this.vistasZonas = new HashMap<Integer,VistaZona>();
		this.setEnabled(false);
		setLayout(new BorderLayout(0, 0));
		paneles = new JTabbedPane(JTabbedPane.LEFT);
		paneles.setMinimumSize(new Dimension(800, 600));
		add(paneles, BorderLayout.CENTER);
		configura();
		configurarFrame();
	}

	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Reinicia la vista de este módulo.</p> 
	 *  Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y reinicia el resto de valores.
	 */
	public void reset() {
		//Eliminar por orden jerarquico.
		if(paneles.getTabCount() > 0) {
			paneles.removeAll();
			contadorPaneles = 0;
		}		
		
		vistasZonas.clear();															//Para correcto funcionamiento este valor inciial no puede ser 0.
		
		this.setEnabled(cm.hasZonas());
		updatePaneles();
		updateUI();
	}
	
	/**
	 * <p>Title: refresh</p>  
	 * <p>Description: Refresca los datos de las vistas.</p> 
	 */
	public void refresh() {
		vistasZonas.forEach((k,vista)->{
			vista.refresh();
		});
	}
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
	 */
	public void configurarFrame() {
	    frame = new JFrame("Parámetros de los Grupos");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth()+ 20;
	    int y = (int)m.getHeight()+15;
	    frame.setSize(new Dimension(x, y));
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(1024, 768));
		frame.setMinimumSize(new Dimension(800, 600));
	    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
	}
	
	/**
	 * <p>Title: toggleVisible</p>  
	 * <p>Description: Cambia la visibilidad del frame que contiene la vista</p>
	 * Pasa del estado anterior al opuesto. Si previamente estaba oculto pasa a 
	 * estar visible, de igual forma si estaba visible pasa a estar oculto. 
	 */
	public void toggleVisible() { frame.setVisible( !frame.isVisible()); }
	
	private void configura() {
		if(cm.getNumberZonas() > 0) updatePaneles();
	}
	
	/**
	 * <p>Title: updatePaneles</p>  
	 * <p>Description: Genera los paneles que representan cada una de las zonas
	 * que componen el proyecto </p>
	 * En caso de no haberse cargado unas zonas con datos, se generan tantas pestañas
	 * (vistas de zonas) como grupos/zonas compongan el proyecto.
	 */
	private void updatePaneles() {
		//Crear las pestañas de cada zona (grupo) con los datos correspondientes.
		//Creación ordenada de un árbol con los elementos.
		TreeMap<Integer, Zona> t = new TreeMap<>();
		t.putAll(cm.getZonas());
		//Reccorrer los elementos y añadir sus vistas.
		for(int i = 1; i <= cm.getNumberZonas(); i++) {
			Zona z = t.get(i);
			if(z != null && !vistasZonas.containsKey(i)) {
				iniciarTabZona(z,"/vista/imagenes/Iconos/sinImg_256px.png");
			}else if(z != null){
				vistasZonas.get(i).setZona(z);
			}
		}
	}

	/**
	 * <p>Title: iniciarTabZona</p>  
	 * <p>Description: Genera la vista para cada zona implicada.</p>
	 * @param zona Zona con los datos correspondientes.
	 * @param icono Imagen a mostrar como icono de la pestaña.
	 */
	private void iniciarTabZona(Zona zona, String icono) {
		int ID = zona.getID();
		String nombre = zona.getName();
		//Crea panel.
		VistaZona panelVZ = new VistaZona(zona,cm);
		//Configuración del borde.
		setBorder(nombre,panelVZ);
		//Añadir panel a las pestañas.
		paneles.addTab(nombre, panelVZ);
		//Añadir el la vista del panel al conjunto.
		vistasZonas.put(ID, panelVZ);
        // Configurar Icono para la pestaña particular
//		if(icono != null && !icono.equals("")) paneles.setIconAt(contadorPaneles, IO.getIcon(icono,15,15));
		if(!icono.isBlank()) paneles.setIconAt(contadorPaneles, IO.getIcon(icono,15,15));
		contadorPaneles++;
	}
	
	/**
	 * <p>Title: setBorder</p>  
	 * <p>Description: Añade un título y un borde a un panel.</p>
	 * Dicho borde rodea la vista de la zoa. 
	 * @param titulo Título que contendrá el borde.
	 * @param panel JPanel al que añadir el borde y su título.
	 * @return JPanel con los nuevos atributos añadidos.
	 */
	private JPanel setBorder(String titulo, JPanel panel) {
		//Configuración del borde.
		TitledBorder tb = BorderFactory.createTitledBorder(titulo);
		tb.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, 26));					//Tipo de la letra.
		tb.setTitleColor(Color.BLUE);											//Color de la letra.
		panel.setBorder(tb);
		return panel;
	}
	
	
	/* Funciones para realización de pruebas de funcionamiento */
	
//	/**
//	 * @param zonas Conjunto de zonas cuyas vistas deben establecerse.
//	 */
//	public void setZonasTest(HashMap<Integer, Zona> zonas) {		
//		//Crear las pestañas de cada zona (grupo) con los datos correspondientes.
//		//Creación ordenada de un árbol con los elementos.
//		reset();
//		int nz = zonas.size();
//		TreeMap<Integer, Zona> t = new TreeMap<>();
//		t.putAll(zonas);
//		//Reccorrer los elementos y añadir sus vistas.
//		for(int i = 1; i <= nz; i++) {
//			Zona z = t.get(i);
//			if(z != null && !vistasZonas.containsKey(i)) {
//				iniciarTabZona(z,"/vista/imagenes/Iconos/sinImg_256px.png");
//			}else if(z != null){
//				vistasZonas.get(i).setZona(z);
//			}
//		}
//	}
//		
//	/**
//	 * <p>Title: main</p>  
//	 * <p>Description: funciona para pruebas</p> 
//	 * @param args argumentos
//	 */
//	public static void main(String[] args) {
//		// Trabajo con un grupo inicial de 4.
//		ParametrosGrupos parametrosGrupos = new ParametrosGrupos(new ControladorModulos());
//		HashMap<Integer, Zona> zonas = new HashMap<Integer,Zona>();
//		for(int i=1; i<=4 ;i++) {
//			zonas.put(i, new Zona(i,"Grupo " + i,15+i*10,200*(i+7),0,0,0,0,0,null));
//		}
//
//		parametrosGrupos.setZonasTest(zonas);
//		parametrosGrupos.toggleVisible();
//	}

}
