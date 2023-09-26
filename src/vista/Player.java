/**  
* Reproductor de la simulación creada.
* Lee un registro y lo reproduce a la velocidad indicada con una barra de tiempos
* y aquellos otros controles necesarios para una correcta visualización y análisis
* del registro de modo visual.    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 3 ago. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFormattedTextField;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import controlador.ControladorModulos;
import modelo.DCVS;
import modelo.ImagesList;
import modelo.Labels;
import modelo.Labels_GUI;
import modelo.OperationsType;
import modelo.TypesFiles;

import java.awt.Dimension;
import java.awt.Font;

/**
 * Esta clase representa la vista del reproductor, puede ser invocada de manera
 *  independiente (por defecto) o dentro de otro marco.  
 * @author Silverio Manuel Rosales Santana
 * @date 3 ago. 2021
 * @version versión 4.6
 */
public class Player extends JPanel implements ActionListener{
	/** serialVersionUID*/  
	private static final long serialVersionUID = 1L;
	private ControladorModulos cm;
	private JFrame frame;
	private JButton btnPlayPause;
	private Timer timer;
	private final FondoPanel fondo = new FondoPanel(ImagesList.BCKGND_PLAYER);
	private TitledBorder tb;
	private JSlider slider;
	private JProgressBar progressBar;
	private JLabel lblBarraDeProgresin, lblEscalaDeTiempos, lblFecha, lblHora, lblX;
	private JFormattedTextField frmtdtxtfldHoraMinuto;
	private JDateChooser dateChooser;
	private GroupLayout groupLayout;
	private int contador;
	/** Primera columna a leer del histórico. */
	private int primera;
	/** Última columna a leer del histórico. */
	private int ultima;
	private HashMap<String,Integer> names;
	private Mapa mapa;
	private DCVS historico;
	private boolean activo;														//Establece si el reproducctor esta activo o pausado.
	private Date d1,d2;

	/**
	 * Constructor del deproductor de secuencias en línea de tiempo.
	 * Inicializa los parámetros iniciales.  
	 * @param cm Controlador de módulos, necesario para integrarse con el resto de la aplicación.
	 * @param mapa Vista del mapa donde se hará la representación gráfica secuenciada.
	 */
	public Player(ControladorModulos cm, Mapa mapa) {
		super();
		this.cm = cm;
		this.mapa = mapa;
		activo = false;
		//Border: configuración de estilo
		setOpaque(false);
		setBackground(Color.LIGHT_GRAY);
		tb = BorderFactory.createTitledBorder(Labels_GUI.W_PLAYER_TITLE);
		tb.setTitleColor(Color.BLUE);
		setBorder(tb);
		//Controles: creación.
		btnPlayPause = new JButton(OperationsType.PLAY.toString());
		slider = new JSlider(0,2000,1000);
		slider.setBackground(new Color(255, 228, 225));
		progressBar = new JProgressBar();
		dateChooser = new JDateChooser();
		frmtdtxtfldHoraMinuto = new JFormattedTextField();
		//Etiquetas: Creación.
		lblBarraDeProgresin = new JLabel(Labels_GUI.L_PROGRESS);	
		lblEscalaDeTiempos = new JLabel(Labels_GUI.L_TIME_SCALE);
		lblX = new JLabel(Labels_GUI.L_SPEED);
		lblFecha = new JLabel(Labels_GUI.L_DATE);
		lblHora = new JLabel(Labels_GUI.L_TIME);
		//Frame
		frame = new JFrame();
		configuracion();
        repaint();
	}
	
	/**
	 * Inicia las propiedades del marco, tal como la imagen de fondo, el título,
	 *  las dimensiones, bloqueo de redimensionado y la operación por defecto al 
	 *   cerrarse.
	 */
	private void iniciarFrame() {
		frame.setContentPane(fondo);
		frame.setTitle(Labels_GUI.W_PLAYER_TITLE.toString());							//Establecimiento del título.
		frame.setSize(344,310);													//Establecimiento de las dimensiones.
		frame.setResizable(false); 												//Dimesiones fijas.
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);					//Comportamiento al cerrar el frame.
		frame.setAlwaysOnTop(false);		
	}
	
	/**
	 * <p>Elimina los datos temporales internos que pudieran estar
	 *  etablecidos.</p>
	 *  Función requerida antes de la carga de un nuevo proyecto. 
	 */
	public void clear() {
		contador = primera = ultima = 0;
		historico = null;
		activo = false;
		d1  = null;
		d2 = null;
		frame.setVisible(false);
	}

	/**
	 * Agrupación del código a fin de mejorar la legibilidad del código del constructor.
	 */
	private void configuracion() {
		//Timer
		contador = 0;
		timer =  new Timer(1000, new Temporizador()); 
		
		btnPlayPause.addMouseListener(new BtnPlay());
		progressBar.setStringPainted(true);
		slider.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.BLACK));
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(250);
		slider.setMajorTickSpacing(500);
		slider.setPaintLabels(true);
		slider.setFont(new Font("Liberation Sans", Font.ITALIC, 10));
		slider.setPreferredSize(new Dimension(200, 14));
		slider.addChangeListener(new SliderListener());
		//Desactivar la edición de fechas.
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setEditable(false);
		dateChooser.addPropertyChangeListener(new DateChooserListener());
		
		//Establecimiento de los textos tooltips
		btnPlayPause.setToolTipText(Labels_GUI.TT_BTNPLAY);
		dateChooser.setToolTipText(Labels_GUI.TT_GO_DATE);
		dateChooser.getCalendarButton().setToolTipText(Labels_GUI.TT_BTN_DATECHOOSER);
		slider.setToolTipText(Labels_GUI.TT_SLIDER);	
		progressBar.setToolTipText(Labels_GUI.TT_PROGRESS);
		lblFecha.setToolTipText(Labels_GUI.TT_GO_DATE);		
		lblHora.setToolTipText(Labels_GUI.TT_TIME1);
		frmtdtxtfldHoraMinuto.setToolTipText(Labels_GUI.TT_TIME2);
		frmtdtxtfldHoraMinuto.setText(Labels_GUI.L_TIME2);	
		
		//Establecimiento de los Layouts
		setLO();
		iniciarFrame();
	}
	
	/**
	 * Controla el dibujado de los componentes de la vista.
	 *  Se ha sobrescrito para forzar el redibujado del mapa constante.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		mapa.updateUI();
	}
	
	/**
	 * Establece la visibilidad de la ventana del reproductor.
	 * @param ver TRUE para visibilidad activaida, FALSE en otro caso.
	 */
	@Override
	public void setVisible(boolean ver) {frame.setVisible(ver);}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Obtención del botón pulsado	
	}
	
	 /** 
	 * Establece la posición para el frame. 
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}
	
	/**
	 * Establece el tiempo inicial y el tiempo final absoluto del histórico.
	 *  Para ello realiza búsqueda de las etiquetas IT e FT del histórico.
	 */
	private void setIFT() {
		String sIT = (String) cm.getModule(TypesFiles.DEF).getDataFromRowLabel(Labels.IT);
		String sFT = (String) cm.getModule(TypesFiles.DEF).getDataFromRowLabel(Labels.FT);
		//Establecer primera posición a leer, se debe sumar una columna extra para comenzar después de las etiquetas.
		primera = Integer.parseInt(sIT) +1;
		//Establecer última columna. Tener en cuenta que el tiempo inicial (Time 0) y FT empieza de 1.
		ultima = Integer.parseInt(sFT);
		contador = primera;
	}
	
	/**
	 * <p>Establece las fechas iniciales y final del histórico</p>
	 * En caso de no estar incluídas en el histórico, establece la actual y final
	 *  en relación a la fecha actual.
	 */
	private void setDates() {
		//Obtener la fecha inicial de la etiqueta del histórico
		d1 = stringToDate(historico.getColumnName(primera));					//Obtener primera fecha.
		//Obtener la fecha final búscando la etiqueta en el histórico.
		d2 = stringToDate(historico.getColumnName(ultima));						//Obtener segunda fecha.
		//Establecer los rangos de fechas del calendario.
		activo = true;															//Activación temporal del player para evitar activación erronéa del búscador.
		dateChooser.setSelectableDateRange(d1, d2);								//Establece rango de fechas.
		dateChooser.setDate(d1);												//Establece la fecha de comienzo.
		activo = !activo;														//Vuelta al estado desactivado del reproductor.
	}
	
	/**
	 * Establece la Reprodución del historico con el mapa de las zonas y la leyenda cargadas. 
	 */
	public void setPlay() {
		//Activar visualizar mapa y leyenda.
		this.historico = cm.getModule(TypesFiles.HST);
		//Leer tiempos iniciales y finales.
		setIFT();
		//Con los tiempos iniciales se configura la barra de progreso.
		progressBar.setMinimum(contador);
		progressBar.setMaximum(ultima);
		//Borrado de todas las gráficas dibujadas previamente.
		cm.getZonas().forEach((k,v)->{
			v.getGrafica().reset();
		});
		//Inicializar mapa con los nombres de las zonas por ID
		int NG = cm.getNumberZonas();											//Obtención del número de zonas.
		//Inicializar aquí para forzar la limpieza de datos
		//En cada nueva reproducción, por si carga nuevos HST.
		names = new HashMap<String,Integer>();									//Inicializar HashMap
		for(int i= 1; i<=NG; i++ ) {
			String name = cm.getZonas().get(i).getName();						//Obtener el nombre.
			names.put(name,i);													//Guardado del nombre por posición del ID.
		}
		//Configuración del rango de fechas.
		setDates();
	}
	
	/**
	 * Reproduce los datos almacenados en una línea de datos, actualiza la barra
	 * de progresión, la fecha del calendario, la hora y manda al mapa los datos
	 *  del slot de tiempo leído para su tratamiento.
	 * @param pos Número de columna a leer de la entrada de datos.
	 */
	private void play(int pos) {
		SimpleDateFormat formato = new SimpleDateFormat("hh:mm");				//Formato de la fecha.
		//Actualizar fecha
		String f = historico.getColumnName(pos);								//Obtención de la fecha almacenada en la posición 0 de la línea.
		Date d = stringToDate(f);	
		frmtdtxtfldHoraMinuto.setText(formato.format(d));
		dateChooser.setDate(d);													//Establecimiento de la fecha leida para mostrar en el dateChooser.
		int rows = cm.getModule(TypesFiles.HST).getRowCount();
		for(int row = 0; row < rows; row++) {
			double nivel;
			String serie = (String) historico.getValueAt(row, 0);
			int id = getID(serie);												//Obtener ID columna.
			String v = (String) historico.getValueAt(row, pos);					//Obtener valor.
			if(v != null) {
				nivel = getValor(v);											//Realizar conversión.
				mapa.addZonaNivel(id, serie,pos, nivel);						//Otorgar nivel al mapa/zona
				this.updateUI();
			}
		}
	
		progressBar.setValue(pos);												//Actualización del la barra de progreso.
		
		//En caso de cierre del reproductor, pausar la reproducción
		//y dejarlo listo para reanudar.
		if(!frame.isVisible()) {
			btnPlayPause.setText(OperationsType.PLAY.toString());				//Estado siguiente: Parado.
			timer.stop();
		}
	}
	
	/**
	 * Realiza la búsqueda de una fecha utilizando una búsqueda binaria. 
	 * @param d Date contenedora de la fecha seleccionada.
	 * @return número de línea del historico de fechas que contiene dicha fecha, 
	 * la fecha inferior más cercana en otro caso.
	 */
	private int getLineaDate(Date d) {
		int linea = 0;															//Línea candidata
		//Límete inferior de búsqueda.
		int inferior = this.primera;											
		//Límite superior de búsqueda,
		// se le suma 1 para que entre en la búsqueda la última fecha.
		int superior = this.ultima+1;
		int aux = superior/2;													//Punto medio de búsqueda.
		Date dAux;
		boolean encontrado = false;
		if(d != null) {
			while(!encontrado) {
				String f = historico.getColumnName(aux);						//Obtención de la fecha almacenada en la posición 0 de la línea.
				dAux = stringToDate(f);
				int resultado = 0;
				if(dAux != null) resultado = dAux.compareTo(d);
				if(resultado < 0) {												//Si Aux es anterior a la fecha búscada
					inferior = linea = aux;										//Ajuste de margenes inferiores de la búsqueda.
					aux += (superior -aux)/2;									//Nuevo punto de búsqueda.
					if(aux == inferior) {encontrado = true;}					//Caso de no encontrado pero se tiene la mayor aproximación inferior. 
				}else if(resultado > 0) {										//Caso de ser posterior.
					superior = aux;												//Auste margen superior.
					aux = superior/2;
				}else if (resultado == 0){										//Caso fecha búscada.
					encontrado = true;
					linea = aux;
				}
			}		
		}
		return linea;
	}
	
	/**
	 * Convierte una cadena de texto que contiene una fecha en un objeto Date. 
	 * @param fecha Grupo fecha/hora en formato: "dd/MM/yyyy hh:mm"
	 * @return Date con los valores leidos almacenados.
	 */
	@SuppressWarnings("finally")
	private Date stringToDate(String fecha){
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");	//Formato de la fecha.
		 Date date = new Date();
		 try { date = formato.parse(fecha);	}									//Conversión tipo de datos.
		 catch (ParseException ex) {
			System.out.println("Player > stringToDate > Valor de fecha incorrecto: " + fecha );
		 }
		 finally {return date;}
	}

	/**
	 * <p>Devuelve el valor entero correspondiente como identificador.</p>
	 * El identificador de un grupo de población es númerico, al contrario que el nombre, 
	 *  el cual puede ser texto.
	 * @param s Cadena de texto que representa al número entero.
	 * @return Identificador (número entero) del nombre que se le ha pasado. -1 en otro caso.
	 */
	private int getID(String s) {
		int id = -1;
		//Separar el nombre del operador.
		int opL = s.split(" ")[0].length();										//Medir la longitud del operador
		String name = s.substring(opL +1);										//Extraer el operador + un espacio.
		//Comprobar que dicho nombre está contenido en el mapa de nombres.
		//y en tal caso obtener ID mediante el nombre. 
		if(names.containsKey(name)) id = names.get(name);
		return id;
	}
	
	/**
	 * <p>Convierte una cadena de texto, la cual contiene un valor
	 * númerico en un valor de tipo Double.</p>
	 * El número representado debe usar como separador decimales la coma ','. 
	 * @param v Valor a convertir de formato texto a double.
	 * @return el valor de la conversión. 0 en otro caso.
	 */
	private double getValor(String v) {
		double vd = 0.0;
		//Cambio de símbolo decimal y conversion a Double.
		try{
			vd = Double.parseDouble(v.replace(",", "."));
		}catch (Exception e) {
			System.out.println("Error, Player > getValor: " + v);
			e.printStackTrace();
		}
		return vd;
	}
	
	/**
	 * Devuelve el JPanel que contiene el reproductor, de esta manera puede
	 *  facilitarse su inclusión dentro otros Frames o JPanels 
	 * @return JPanel contendor de este reproductor y su configuración.
	 */
	public JPanel getPanel() {return this;}
	
	/**
	 * Clase controladora de los estados de la reproducción del player.
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.0
	 */
	private class BtnPlay extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			String estado = btnPlayPause.getText();
			OperationsType state = OperationsType.getNum(estado);
			
			switch(state){
				case PLAY:														//Estado pausado, muestra 'Reproducir'
					btnPlayPause.setText(OperationsType.PAUSE.toString());		//Estado siguiente: reproducir.
					timer.start();
					break;
				case REPLAY:													//Estado parado en final muestra y etiqueta 'Repetir'.
					btnPlayPause.setText(OperationsType.PAUSE.toString());		//Estado siguiente: reproducir.				
					contador = primera;
					setPlay();
					timer.restart();
					break;
				default:														//Estado pausado/parado, muestra 'Reproducir'
					btnPlayPause.setText(OperationsType.PLAY.toString());		//Estado siguiente: Parado.
					timer.stop();						
			}		
			activo = !activo;
			dateChooser.getCalendarButton().setEnabled(!activo);				//Activa boton del dateChooser cuando la reproducción no está activa.	
		}
	}
	
	/**
	 * <p>Clase que maneja el selector de fechas y se encarga de
	 *  avanzar o retroceder en el histórico hasta la fecha que se pueda seleccionar
	 *   mediante el DateChooser.</p>
	 * Actual únicamente cuando está activo y realiza cambios tanto en el progressBar
	 *  como en el contador.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 2.0
	 */
	private class DateChooserListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			int linea = contador;
//			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");	//Formato de la fecha.
			if(!activo && arg0.getPropertyName().equals("date") ) {
				Date f = dateChooser.getDate();									//Obtención del valor establecido.				
				linea = getLineaDate(f);
				
				String s = historico.getColumnName(linea);						//Obtención de la fecha almacenadala línea obtenida.
				System.out.println("Player > DateChooserL > s: " + s);
				Date d = stringToDate(s);
				progressBar.setValue(linea);									//Actualización del la barra de progreso.
				contador = linea;												//Actualizar contador
				play(contador);
				//Caso tener la fecha, actualiza con la más cercana inferior..
				if(d != null && d.compareTo(f) < 0) {dateChooser.setDate(d);}	
			}
		}
	}
	
	/**
	 * <p>Clase controladora de la barra de deslizamiento del
	 *  reproductor.</p> 
	 *  Su función principal es incrementar o reducir el tiempo de reproducción.
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.2
	 */
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int valor = slider.getValue();										//Obtención del valor establecido.
			lblX.setText("x " + valor + Labels_GUI.L_TIME2);					//Mostrar valor de la escala en el slider.			
			timer.setDelay(valor);												//Establece el timer con el nuevo valor.
		}
	}
	
	/**
	 * <p>Temporizador encargado de realizar una reproducción a cada
	 *  intervalo de tiempo.</p>
	 *  Controla además cuando se debe parar la reproducción o termina su tarea
	 *   en función de los parámetros iniciales de tiempo inicial y tiempo final.
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.0
	 */
	private class Temporizador implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Comprobar si existe el módulo histórico.
			boolean isHST = cm.hasModule(TypesFiles.HST);
			//Si es la última línea -> parar reproducción.
			//Además debe cumplir que el histórico sigue existiendo.
			if(contador <= ultima && isHST) {
				play(contador);
				contador++;
			}else if (!isHST){
				//Ocultar el reproductor en caso de no haber un HST.
				setVisible(isHST);
			}else {		
				timer.stop();
				btnPlayPause.setText(OperationsType.REPLAY.toString());
				activo = !activo;
				dateChooser.getCalendarButton().setEnabled(!activo);			//Activa boton del dateChooser cuando la reproducción no está activa.
			}
	    }
	}
	
	/**
	 * <p>Establece los LayOuts por defecto del reproductor.</p>
	 * Principalmente su función es aliviar de código innecesario las funciones 
	 * principales.
	 */
	private void setLO() {
		groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEscalaDeTiempos)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblX))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
								.addComponent(lblBarraDeProgresin)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(2)
									.addComponent(lblHora)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(frmtdtxtfldHoraMinuto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(57)))
							.addGap(14))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblFecha)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
						.addComponent(slider, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(60)
					.addComponent(btnPlayPause, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(78, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBarraDeProgresin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEscalaDeTiempos)
						.addComponent(lblX))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblFecha)
						.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(frmtdtxtfldHoraMinuto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHora))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnPlayPause)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		//Layout de la imagen de fondo.
		GroupLayout gl_fondo = new GroupLayout(fondo);
		gl_fondo.setHorizontalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addComponent(this, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		gl_fondo.setVerticalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addComponent(this, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
		);
	}
}
