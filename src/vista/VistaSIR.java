/**  
* <p>Title: VistaSIR.java</p>  
* <p>Description: Vista dedicada a mostrar los parámetros del modelo de 
* predicción/cálculo SIR</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 nov. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import controlador.ControladorModulos;
import modelo.DCVS;
import modelo.IO;
import modelo.Labels;
import modelo.TypesFiles;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JCheckBox;


/**
 * <p>Title: VistaSIR</p>  
 * <p>Description: Vista dedicada a mostrar los parámetros del modelo de 
 * predicción/cálculo SIR</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 17 nov. 2021
 * @version versión 1.2
 */
public class VistaSIR extends JPanel{
	/** serialVersionUID*/  
	private static final long serialVersionUID = -4611485519498921021L;
	private HashMap<String,JTextField> mapaFields;
	private JButton btnAplicar;
	private JCheckBox chckbxIP;
	//
	private final int hi;														//Altura de los elementos (principalmente iconos).
	private final int wi;														//Anchura de los iconos.
	private final int lineaBase = 120;											//Primera línea a partir de la cual se dibujan todos los elementos.
	private int contador = 0;													//Contador de elementos que se han adjuntado de un grupo.
	private JPanel panelCentral;
	private ControladorModulos cm;												//Controlador de módulos.
	private boolean IP;

	/**
	 * <p>Title: VistaSIR</p>  
	 * <p>Description: Constructor de la vista del módulo SIR</p>  
	 * @param cm Controlador de módulos, necesario para permitir el flujo de datos
	 * entre las vistas, el módelo y el controlador.
	 */
	public VistaSIR(ControladorModulos cm) {
		super();
		this.cm = cm;
		this.mapaFields = new HashMap<String,JTextField>();
		this.IP = true;
		setLayout(null);
		panelCentral = new JPanel();
		panelCentral.setBackground(Color.LIGHT_GRAY);
		hi = wi =  20;
		configurar();
	}
	
	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y activa el valor de Inmunidad Permanente.</p>
	 */
	public void reset() {
		IP = true;
		//Limpiar datos anteriores.
		mapaFields.forEach((tipo,elemento) -> {	elemento.setText("");});
		//Realizar lectura de los datos en el propio módulo (si hay).
		refresh();
	}
	
	
	/**
	 * <p>Title: refresh</p>  
	 * <p>Description: Actualiza los campos de la vista. No realiza borrado previo.</p>
	 * Una vez actualizados, actualiza los controles y refresca el dibujado. En caso
	 *  de no disponer del módulo correspondiente en el sistema, no realiza acción alguna.
	 */
	public void refresh() {
		if(cm.getModule(TypesFiles.DEF) != null) {
			updateFields();
			refreshControls();
			updateUI();
		}
	}
	
	/**
	 * <p>Title: updateFields</p>  
	 * <p>Description: Actualiza los campos de la vista.</p>
	 * Solicita al controlador los datos necesarios para actualizar la vista,
	 *  en caso de estar disponibles, procede a la acción. No actualiza aquellas
	 *   partes que no estén definidas. Es decir, no elimina datos previos, los 
	 *    sobreescribe si hay un nuevo dato disponible.
	 */
	private void updateFields() {
		DCVS dcvs = cm.getModule(TypesFiles.DEF);
		int nrows = dcvs.getRowCount();
		for(int i = 0; i < nrows; i++) {
			String label = (String) dcvs.getValueAt(i, 0);
			String data = (String) dcvs.getValueAt(i, 1);
			if(!label.equals(Labels.IP)) {setLabel(label,data);}
			else {
				//Cuando la etiqueta es IP, recibe el tratamiento específico.
				//Convertirlo a int sino es null, en otro caso dar valor 1 (activo).
				int a = (data == null || data.equals(""))? 1 : Integer.parseInt(data);				
				IP = a == 1;													//Obtener el resultado si es activo o no.
			}
		}
	}
	
	/**
	 * <p>Title: setLabel</p>  
	 * <p>Description: Establece el texto al campo que se le indique.</p>
	 * @param label Etiqueta o campo a escribir.
	 * @param text Texto a escribir en el campo.
	 */
	public void setLabel(String label, String text) {
		if(mapaFields.containsKey(label)) {
			mapaFields.get(label).setText(text);
		}
	}
	
	/**
	 * <p>Title: getLabel</p>  
	 * <p>Description: Devuelve el texto al campo que se le indique.</p>
	 * @param label Etiqueta o campo a leer.
	 * @return El dato almacenado en tal campo.
	 */
	public String getLabel(String label) {
		String data = null;
		if(mapaFields.containsKey(label)) {
			data = mapaFields.get(label).getText() ;
		}else if(label.equals(Labels.IP)) data = "" + ((IP == true)? 1:0);
		return data;
	}
	
	
	/**
	 * <p>Title: addIconL</p>  
	 * <p>Description: Añade un icono a una etiqueta</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su ruta.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconL(JLabel componente, String ruta, int w, int h) {
		componente.setIconTextGap(5);
		componente.setIcon(IO.getIcon(ruta,w,h));	
	}
	
	/**
	 * <p>Title: iniciarLabels</p>  
	 * <p>Description: Establece las facetas de las etiquetas descripticas</p> 
	 * @param label Nombre en la etiqueta.
	 * @param ruta Ruta al icono de la etiqueta.
	 * @return La propia etiqueta configurada a efectos de permitir modificaciones
	 * particulares.
	 */
	private JLabel iniciarLabels(String label, String ruta) {
		Labels labels = new Labels();
		JLabel jl = new JLabel();
		int posY = lineaBase + 30*contador;
		int w = 360;
		int posX = 150;
		jl = new JLabel(labels.getWord(label));
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		if(label != null) jl.setToolTipText(labels.getWord(label));
		panelCentral.add(jl);
		return jl;
	}
	
	/**
	 * <p>Title: generateControls</p>  
	 * <p>Description: Genera todos los JTextFields necesarios para cubrir
	 * tantos tipos de módulos como se añadan al grupo de elementos.</p>
	 * Los tipos de elementos están almacenados inicialmente en el mapaFields interno.
	 * <p>Para crear los controles ver {@link #createFieldsInMap} </p>
	 * @param ext Tipo de datos al que generar el control.
	 * @param posX Posición en las coordenadas X donde colocar el control.
	 * @see #createFieldsInMap()
	 */
	private void generateControls(String ext, int posX){
		Labels labels = new Labels();
		int posY = lineaBase + 30*contador;
		JTextField jtf = mapaFields.get(ext);
		jtf.setEditable(true);
		jtf.setEnabled(true);
		jtf.setBounds(30,posY,100,hi);
		jtf.setColumns(10);
		jtf.setToolTipText(labels.getWord(ext));
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		jtf.setBorder(new LineBorder(Color.BLACK, 1, true));
		jtf.addFocusListener(new FocusJTextField(jtf));
		panelCentral.add(jtf);
	}
	
	/**
	 * <p>Title: createFieldsInMap</p>  
	 * <p>Description: Añade todas las funcionalidades y controles necesarias.</p>
	 * Dede esta función se añaden cada una de las líneas de control necesarias.
	 * <p>Para mas información sobre crear los controles
	 *  ver {@link #generateControls(String ext, int posX)} .</p>
	 * @see #generateControls(String ext, int posX)
	 */
	private void createFieldsInMap() {
		// Generación de sus nombres e iconos particulares.
		addNewControlLine(Labels.PTE,"/vista/imagenes/Iconos/probabilidad_64px.png");
		addNewControlLine(Labels.DME,"/vista/imagenes/Iconos/duracion_64px.png");
		addNewControlLine(Labels.DMI,"/vista/imagenes/Iconos/duracionMedia_64px.png");
		addNewControlLine(Labels.IT,"/vista/imagenes/Iconos/startTime_64px.png");
		addNewControlLine(Labels.FT,"/vista/imagenes/Iconos/stopTime_64px.png");
		//Configuración específica para DMIP
		mapaFields.get(Labels.DMI).setDisabledTextColor(Color.RED);
		//Selector de IP (Checkbox) configuración específica.
		JLabel jl = iniciarLabels(null,"/vista/imagenes/Iconos/inmunidad_64px.png");
		int posY = lineaBase + 30*contador;
		jl.setBounds(30,posY, 25, hi);
		chckbxIP = new JCheckBox("Inmunidad Permanente");
		chckbxIP.setBorder(null);
		chckbxIP.setOpaque(false);
		chckbxIP.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxIP.setBounds(55, posY, 196, 23);	
		chckbxIP.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxIP.addMouseListener(new BotonL());
		chckbxIP.setActionCommand("IP");
		panelCentral.add(chckbxIP);
	}
	
	/**
	 * <p>Title: addNewControlLine</p>  
	 * <p>Description: Agrega los elementos JTextField al grupo mapaFields</p>
	 * Todo elemento que se desea agregar se añade aquí se configuran automáticamente
	 * y se añaden a las diferentes partes de la vista.
	 * <p>Para crear los controles ver {@link #generateControls(String ext, int posX)} .</p>
	 * @param et Etiqueta que indentifica al control.
	 * @param rutaIcon Ruta a la imagen que permite añadir su icono.
	 * @see #generateControls(String ext, int posX)
	 */
	private void addNewControlLine(String et, String rutaIcon) {
		mapaFields.put(et, new JTextField());
		iniciarLabels(et,rutaIcon);
		generateControls(et,0);
		contador++;
	}
	
	/**
	 * <p>Title: configurar</p>  
	 * <p>Description: Función general para crear la vista y sus controles.</p>
	 */
	private void configurar() {
		//Botón aplicar.
		btnAplicar = new JButton("Aplicar");
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/ok_64px.png",64,64));
		btnAplicar.setToolTipText("Aplica los cambios efectuados.");
		btnAplicar.setBounds(334, 252, 150, 87);
		btnAplicar.setActionCommand("Aplicar");
		panelCentral.add(btnAplicar);
			
		//Configuración básica del panelCentral superior
		setToolTipText("Visulación de los parámetros particulares de la enfermedad.");
		setSize(new Dimension(650, 400));
		setName("Vista parámetros SIR");
		setMinimumSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(1024, 768));
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));	
		
		panelCentral.setToolTipText("Parámetros SIR");
		panelCentral.setLayout(null);
		add(panelCentral, BorderLayout.CENTER);	

		//Muy importante iniciar este método antes de proseguir.
		createFieldsInMap();

		//Establecer el icono representación del módulo Archivos.
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(IO.getIcon("/vista/imagenes/Iconos/motor_512px.png",70,75));
		labelLogo.setBounds(12, 20, 70, 75);
		panelCentral.add(labelLogo);
		//Configuración del estado de los botones según contexto.
		refreshControls();
	}
	
	/**
	 * <p>Title: refreshControls</p>  
	 * <p>Description: Actualiza los controles de la vista.</p>
	 */
	private void refreshControls() {
		mapaFields.get(Labels.DMI).setEnabled(!IP);
		chckbxIP.setSelected(IP);
	}
	
	/* Clases privadas */
	
	/**
	 * <p>Title: BotonL</p>  
	 * <p>Description: Clase encargada de detectar la pulsación del botón aplicar</p>
	 * Cuando es pulsado, intercambia la información necesaria con el controlador.  
	 * @author Silverio Manuel Rosales Santana
	 * @date 19 nov. 2021
	 * @version versión 1.0
	 */
	private class BotonL extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evt) {
			String op = ((AbstractButton) evt.getSource()).getActionCommand();
			//Si se ha pulsado sobre el selector, se actualiza su vista.
			if(op.equals("IP")) {IP = chckbxIP.isSelected();}
			//Avisa al controlador de cambios.
			if(cm != null && op.equals("Aplicar") ) cm.doActionVistaSIR();
			//Actualiza los controles.
			refreshControls();
		}
	}
	
	/* Funciones para pruebas */
	
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Permite visualizar el módulo en una ventana externa.</p> 
	 * @param posX Posición horizontal de la pantalla donde colocar la ventana. (Pizeles).
	 * @param posY Posición vertical en la pantalla donde colocar la ventana. (Pixeles).
	 * @param w Ancho de la ventana.
	 * @param h Alto de la ventana.
	 */
	public void abrirFrame(int posX, int posY, int w, int h) {
		JFrame frame = new JFrame("Vista SIR");
		frame.getContentPane().setLayout(null);
		frame.setSize(w,h);
		frame.setBounds(posX, posY, w, h);
	    frame.setPreferredSize(new Dimension(w, h));
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * <p>Title: main</p>  
	 * <p>Description: </p> 
	 * @param args sin argumentos.
	 */
	public static void main(String[] args) {
		VistaSIR vSIR = new VistaSIR(null);	
		JFrame frame = new JFrame("Vista SIR");
		frame.getContentPane().add(vSIR);	
		frame.setBounds(280, 150, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vSIR);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
//		vSIR.abrirFrame(280, 150, 600, 400);
	}
}
