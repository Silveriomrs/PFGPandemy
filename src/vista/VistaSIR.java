/**
* Vista dedicada a mostrar los parámetros del modelo de predicción/cálculo SIR.
* <p>Aplication: UNED</p>
* @author Silverio Manuel Rosales Santana
* @date 17 nov. 2021
* @version 1.0
*/
package vista;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import controlador.ControladorModulos;
import controlador.IO;
import modelo.DCVS;
import modelo.ImagesList;
import modelo.Labels;
import modelo.Labels_GUI;
import modelo.OperationsType;
import modelo.TypesFiles;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JCheckBox;
import java.awt.Point;


/**
 * Vista dedicada a mostrar los parámetros del modelo de predicción/cálculo SIR.
 * Esta clase hereda de JPanel.
 * @author Silverio Manuel Rosales Santana
 * @date 17 nov. 2021
 * @version versión 1.2
 */
public class VistaSIR extends JPanel{
	/** serialVersionUID*/
	private static final long serialVersionUID = -4611485519498921021L;
	private HashMap<String,JTextField> mapaFields;
	private JButton btnAplicar,btnExecute;
	private JCheckBox chckbxIP;
	//
	private final int hi;														//Altura de los elementos (principalmente iconos).
	private final int wi;														//Anchura de los iconos.
	private final int lineaBase = 120;											//Primera línea a partir de la cual se dibujan todos los elementos.
	private int contador = 0;													//Contador de elementos que se han adjuntado de un grupo.
	private JPanel panelCentral;
	private ControladorModulos cm;												//Controlador de módulos.
	private boolean IP;
	private String fondo = ImagesList.BCKGND_DEF;


	/**
	 * Constructor de la vista del módulo SIR.
	 * @param cm Controlador de módulos, necesario para permitir el flujo de datos
	 * entre las vistas, el módelo y el controlador.
	 */
	public VistaSIR(ControladorModulos cm) {
		super();
		this.cm = cm;
		this.mapaFields = new HashMap<String,JTextField>();						//Guardar los JTextFields con su etiqueta como ID
		this.IP = true;
		//Configuración panel superior.
		setLocation(new Point(10, 10));
		setLayout(new BorderLayout(0, 0));
		setBorder(null);
		setOpaque(false);
		setLayout(null);
		
		panelCentral = new JPanel();
		hi = wi =  20;															//Establecer dimensiones de los iconos.
		configurar();
	}

	/**
	 * Función general para crear la vista y sus controles.
	 */
	private void configurar() {
		//Botón aplicar.
		btnAplicar = new JButton(OperationsType.APPLY.toString());
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon( ImagesList.OK,64,64));
		btnAplicar.setToolTipText(Labels_GUI.TT_APPLY_CHANGES);
		btnAplicar.setBounds(334, 252, 150, 87);
		btnAplicar.setActionCommand(OperationsType.APPLY.toString());
		panelCentral.add(btnAplicar);
		//Botón ejecutar simulación
		btnExecute = new JButton(Labels_GUI.BTN_RUN_SIMULATION);
		btnExecute.setBounds(44, 302, 207, 37);
		btnExecute.setVisible(true);
		btnExecute.setEnabled(false);
		btnExecute.setActionCommand(OperationsType.EXECUTE.toString());
		btnExecute.setToolTipText(Labels_GUI.TT_RUN_SIMULATION);
		btnExecute.addMouseListener(new BotonL());
		panelCentral.add(btnExecute);

		//Configuración básica del panelCentral superior
		setToolTipText(Labels_GUI.TT_PANEL_SIR);
		panelCentral.setBounds(225, 200, 550, 400);								//Indicar donde colocar el panel central respecto al panel del que cuelga.
		this.setBounds(220, 195, 560, 410);
		setName(Labels_GUI.NAME_PANEL_SIR);
		setAutoscrolls(true);

		panelCentral.setToolTipText(Labels_GUI.NAME_PANEL_SIR);
		panelCentral.setLayout(null);
		add(panelCentral, BorderLayout.CENTER);

		//Muy importante iniciar este método antes de proseguir.
		createFieldsInMap();

		//Establecer el icono representación del módulo Archivos.
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(IO.getIcon(ImagesList.DEF_ICON,70,75));
		labelLogo.setBounds(12, 20, 70, 75);
		panelCentral.add(labelLogo);

		//Configuración del estado de los botones según contexto.
		refreshControls();
	}

	/**
	 * Sobrescritura del método heredado de dibujado con el objetivo de permitir
	 *  colocar una imagen de fondo centrada y con efecto transparente.
	 */
	@Override
	public void paint(Graphics g) {
		if(fondo != null) {
			Image imagen = new ImageIcon(getClass().getResource(fondo)).getImage();
			g.drawImage(imagen,panelCentral.getX(),panelCentral.getY(),panelCentral.getWidth(),panelCentral.getHeight(),panelCentral);
			panelCentral.setOpaque(false);
			super.paint(g);
		}
	}


	/**
	 * Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y activa el valor de Inmunidad Permanente.
	 */
	public void reset() {
		IP = true;
		//Limpiar datos anteriores.
		mapaFields.forEach((tipo,elemento) -> {	elemento.setText("");});
		//Realizar lectura de los datos en el propio módulo (si hay).
		refresh();
	}

	/**
	 * <p>Actualiza los campos de la vista. No realiza borrado previo.</p>
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
	 * <p>Actualiza los campos de la vista.</p>
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
	 * Establece el texto al campo que se le indique.
	 * @param label Etiqueta o campo a escribir.
	 * @param text Texto a escribir en el campo.
	 */
	public void setLabel(String label, String text) {
		if(mapaFields.containsKey(label)) {
			mapaFields.get(label).setText(text);
		}
	}

	/**
	 * Devuelve el texto al campo que se le indique.
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
	 * <p>Añade un icono a una etiqueta.
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
	 * Establece las facetas de las etiquetas descripticas.
	 * @param label Nombre en la etiqueta.
	 * @param ruta Ruta al icono de la etiqueta.
	 * @return La propia etiqueta configurada a efectos de permitir modificaciones
	 * particulares.
	 */
	private JLabel iniciarLabels(String label, String ruta) {
		JLabel jl = new JLabel();
		int posY = lineaBase + 30*contador;
		int w = 360;
		int posX = 150;
		String text = Labels.getWord(label);
		jl = new JLabel(text);
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		if(label != null) jl.setToolTipText(text);
		panelCentral.add(jl);
		return jl;
	}

	/**
	 * <p>Genera todos los JTextFields necesarios para cubrir
	 * tantos tipos de módulos como se añadan al grupo de elementos.</p>
	 * Los tipos de elementos están almacenados inicialmente en el mapaFields interno.
	 * <p>Para crear los controles ver {@link #createFieldsInMap} </p>
	 * @param ext Tipo de datos al que generar el control.
	 * @param posX Posición en las coordenadas X donde colocar el control.
	 * @see #createFieldsInMap()
	 */
	private void generateControls(String ext, int posX){
		int posY = lineaBase + 30*contador;
		JTextField jtf = mapaFields.get(ext);
		jtf.setEditable(true);
		jtf.setEnabled(true);
		jtf.setBounds(30,posY,100,hi);
		jtf.setColumns(10);
		jtf.setToolTipText(Labels.getWord(ext));
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		jtf.setBorder(new LineBorder(Color.BLACK, 1, true));
		jtf.addFocusListener(new FocusJTextField(jtf));
		panelCentral.add(jtf);
	}

	/**
	 * <p>Añade todas las funcionalidades y controles necesarias.</p>
	 * Dede esta función se añaden cada una de las líneas de control necesarias.
	 * <p>Para mas información sobre crear los controles
	 *  ver {@link #generateControls(String ext, int posX)} .</p>
	 * @see #generateControls(String ext, int posX)
	 */
	private void createFieldsInMap() {
		// Generación de sus nombres e iconos particulares.
		addNewControlLine(Labels.PTE,ImagesList.ICON_PTE);
		addNewControlLine(Labels.DME,ImagesList.ICON_PTE);
		addNewControlLine(Labels.DMI,ImagesList.ICON_DMI);
		addNewControlLine(Labels.IT,ImagesList.ICON_IT);
		addNewControlLine(Labels.FT,ImagesList.ICON_FT);
		//Configuración específica para DMIP
		mapaFields.get(Labels.DMI).setDisabledTextColor(Color.RED);
		//Selector de IP (Checkbox) configuración específica.
		JLabel jl = iniciarLabels(null,ImagesList.ICON_IP);
		int posY = lineaBase + 30*contador;
		jl.setBounds(30,posY, 25, hi);
		chckbxIP = new JCheckBox(Labels.getWord(Labels.IP));
		chckbxIP.setBorder(null);
		chckbxIP.setOpaque(false);
		chckbxIP.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxIP.setBounds(55, posY, 196, 23);
		chckbxIP.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxIP.addMouseListener(new BotonL());
		chckbxIP.setActionCommand(Labels.IP);
		panelCentral.add(chckbxIP);
	}

	/**
	 * <p>Agrega los elementos JTextField al grupo mapaFields</p>
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
	 * Actualiza los controles de la vista.
	 */
	private void refreshControls() {
		mapaFields.get(Labels.DMI).setEnabled(!IP);
		chckbxIP.setSelected(IP);

		if( checkFields() && cm.hasZonas() && cm.hasModule(TypesFiles.REL)){
			btnExecute.setEnabled(true);
			btnExecute.setBackground(Color.GREEN);
		}
	}

	/**
	 * <p>Comprueba si un valor es un dato númerico correcto. </p>
	 * Admite valores del tipo entero o doble, no admite ',' (comas), ni otros carácters
	 *  no númericos. Tampoco admite números negativos.
	 * @param label Etiqueta del campo a evaluar
	 * @return TRUE si cumple las condiciones. FALSE en otro caso.
	 */
	private boolean checkValue(String label) {
		boolean resultado = true;
		String ve= getLabel(label);

		try {
			double valor = Double.parseDouble(ve);
			if(valor < 0) throw new ArithmeticException();
		}catch(ArithmeticException ae) {
			resultado = false;
		}catch(Exception e) {
			resultado = false;
		}
		return resultado;
	}

	/**
	 * <p>Realiza un chequeo de todos los campos.</p>
	 * En caso de que uno de los campos contenga un valor incorrecto retornará
	 *  FALSE.
	 * @return TRUE si los valores de todos los campos son correctos. FALSE en otro caso.
	 */
	private boolean checkFields() {
		boolean done = true;
		for (String clave:mapaFields.keySet()) {
		    if(!checkValue(clave)) done = false;
		}

		return done;
	}


	/* Clases privadas */

	/**
	 * <p>Clase encargada de detectar la pulsación del botón aplicar</p>
	 * Cuando es pulsado, intercambia la información necesaria con el controlador.
	 * @author Silverio Manuel Rosales Santana
	 * @date 19 nov. 2021
	 * @version versión 1.0
	 */
	private class BotonL extends MouseAdapter {
//TODO: Está acoplado en cierto modo. Quitar esos toString para OpeartionsType
		/**
		 * Sobrescribe la función heredada. Comprueba el operador asociado al
		 *  botón al que ha sido agregado el observador y en función del control
		 *   activado realiza las opciones correspondientes, previa comprobación de
		 *    los valores correctos.
		 * <p>Comunica al controlador la opción ejecutada a través de su doAction</p>
		 */
		@Override
		public void mouseClicked(MouseEvent evt) {
			String op = ((AbstractButton) evt.getSource()).getActionCommand();
			OperationsType opt = OperationsType.UPDATE;
			//Si se ha pulsado sobre el selector, se actualiza su vista.
			//Avisa al controlador de cambios.
			if(op.equals(OperationsType.APPLY.toString())){
				mapaFields.forEach((label,field) ->{
					String valor = getLabel(label);
					if(valor != null && !valor.equals("") && !checkValue(label)) {
						setLabel(label,null);
						cm.showMessage(Labels_GUI.VALUE_FIELD + Labels.getWord(label) + Labels_GUI.VALUE_WRONG + valor, 0);
					}
				});
			}else if(op.equals(Labels.IP)) {IP = chckbxIP.isSelected();}
			else if(((Component) evt.getSource()).isEnabled() ) opt = OperationsType.EXECUTE;
			//Llamada al controlador para ejectuar la acción de la opción elegida.
			cm.doActionVistaSIR(opt);
			//Actualiza los controles.
			refresh();
		}
	}

}