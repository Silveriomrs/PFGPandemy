/**  
 * Definición de mensajes y textos de la aplicación 
* @author Silverio Manuel Rosales Santana
* @date 24 jul. 2022  
* @version 1.0  
*/  
package modelo;

/**
 * Contenedor de todas los textos que componen la interfaz gráfica.
 * Idioma usado: Español
 * @author Silverio Manuel Rosales Santana
 * @date 24 jul. 2022
 * @version versión 1.0
 */
public class Labels_GUI {
	
	/*Archivos*/
	public final static String W_FILES_TITLE = "Módulo de Archivos";
	public final static String W_MODULES_TITLE = "Modulos";
	public final static String FILES_PANEL_NAME = "Vista de módulos cargados";
	public final static String TT_FILES_PANEL = "Selección archivos asignados a los módulos.";
	public final static String TT_W_FILES = "Visulación de los ficheros asignados a cada módulo.";
	public final static String TT_FILE_SELECTED = "Archivo seleccionado";
	public final static String TT_L_PRJ = "Archivo con los parámetros del modelo.";
	public final static String TT_L_DEF = "Archivo con la definición propia de la enfermedad.";
	public final static String TT_L_REL = "Matriz de contactos. Refleja las relaciones entre los grupos";
	public final static String TT_L_PAL = "Paleta de colores personalizada.";
	public final static String TT_L_HST = "Histórico de evolución con los datos de la simulación.";
	public final static String PATH_NO_DEFINED = "Datos sin destino definido.";
	public final static String ERR_FILE_UNKNOWN = "Archivo seleccionado no reconocido.";
	
	/*ControladorModulos*/
	/** Mensaje de error de proyecto que hace referencia a un módulo no encontrado*/
	public final static String ERR_MSG_1_CM = "Archivo de proyecto incorrecto. \nReferencia un módulo que no está contenido en la misma carpeta:\n ";
	public final static String WARNING_1_DATA_LOSS = "Los cambios no guardados se perderán";
	public final static String REQUEST_EXIT_CONFIRM = "¿Desea salir del programa?";
	public final static String ERR = "Error";
	public final static String INF = "Información";
	public final static String WARN = "¡Antención!";
	public final static String QST = "Consulta";
	public final static String WARNING_2_OVERWRITE = "Se escribirán los datos en disco\n¿continuar?";
	public final static String ERR_LOAD_MODULE = "No se puede cargar el módulo: ";
	public final static String ERR_MODULE_LESS_NG = "dicho módulo no tiene definido suficientes parámetros para\nel número de grupos de población indicados en el modelo";
	public final static String L_BORDER_PANEL_TE = "Editor General";
	public final static String L_BORDER_PANEL_MAP = "Visor de Mapa";
	public final static String L_BORDER_PANEL_GRP = "Visor Grupos de Poblaci\u00F3n";
	public final static String L_BORDER_PANEL_PRJ = "Parámetros del Modelo";
	public final static String L_BORDER_PANEL_DEF = "Configuraci\u00F3n SIR";
	/** Usado en el CM cuando se edita un módulo de manera externa en el TE*/
	public final static String TE_TITLE_EXTERNAL = "Editando modulo: ";
	public final static String DEFAULT_NAME_GRP = "Grupo ";
	
	/*Graficas Chart*/
	public final static String M_VER = "Ver";
	public final static String M_CASOS = "Casos";
	public final static String M_TASAS = "Tasas";
	public final static String M_TASA = "Tasa";
	public final static String L_X = "Nivel";
	public final static String L_Y = "Tiempo  = días)";
	public final static String L_TITLE = "Zona: ";
	public final static String L_TITLE_FRAME = "Evolución pandemica.";
	
	/*Paleta*/
	public final static String SEL_COLOR = "Seleccione nuevo color";
	
	/*ParametrosProyecto*/
	public final static String PROJECT_NAME = "Propiedades Proyecto";
	/** Aviso de eliminación de datos en el siguiente paso*/
	public final static String WARNING_1 = "El siguiente paso eliminará parcial o toltamente los datos almacenados previamente ¿desea continuar?";
	/** Aviso NG incorrecto*/
	public final static String WARNING_2 = "Número de grupos de estudio incorrecto: ";
	/** Aviso NG <= 0 */
	public final static String WARNING_3 = "El número de grupos debe ser mayor de cero.";
	public final static String WARNING_EMPTY_FIELD = "El campo nombre no puede estar vacio.";
	public final static String VERSION_TITLE = "Versión:";
	public final static String VERSION_NUMBER = "1.0";
	public final static String NAME_MODULES = "Módulos";
	public final static String L_AUTHOR = "Autor/a:";
	public final static String L_DATE_MODIFIED = "Fecha modificación:";
	public final static String L_DATE_CREATION = "Fecha de creación: ";
	public final static String L_MANDATORY_FIELD = "Campo obligatorio";
	public final static String L_DESCRIPTION_MODEL = "Descripción del modelo:";
	public final static String L_NG = "Número de Grupos de estudio:";
	public final static String L_FILES = "Archivos del modelo:";
	public final static String TT_DESCRIPTION = "Introduzca cualquier texto descriptivo que crea necesario acerca del modelo.";
	public final static String TT_DATE_MODIFIED = "Indica la última fecha de modificación.";
	public final static String TT_DATE_CREATION = "Indica la fecha de creación.";
	public final static String TT_BTN_APPLY = "Guarda los cambios efectuados.";
	public final static String TT_MODEL_NAME = "Nombre del modelo, se usará para dar nombre a los archivos que lo componen.";
	public final static String TT_AUTHOR = "Autor del modelo.";
	public final static String TT_VERSION_MODEL = "Número de versión del modelo.";
	public final static String TT_NG = "Número de grupos de población que componen el modelo.";
	public final static String BTN_APPLY = "Aplicar Cambios";
	public final static String PRJ_TITLE = "Título del Proyecto:";

	/*Pizarra*/
	public final static String L_DELETE = "Eliminar";
	public final static String L_ADD_TO = "Asignar a";
	public final static String BTN_CLEAR = "Limpiar";
	public final static String BTN_COMPOSE = "Componer";
	public final static String TT_COMPOSE = "Cierra la figura y crea el poligono.\nUna vez creado debe asignarse a una zona.";
	public final static String TT_APPLY = "Aplica las asignaciones seleccionadas.";
	public final static String TT_CLEAR = "Limpiar la pizarra y redibujar las zonas.";
	public final static String TT_OPEN = "Abre una imagen para establecer de papel de fondo.";
	public final static String TT_SAVE = "Guardar los cambios realizados.";
	public final static String TT_SELECT_ZONE = "Seleccionar una zona para asignar la figura.";
	public final static String TT_REMOVE_ZONE = "Seleccionar una zona a reasignar.";
	public final static String NAME_CANVAS = "pizarra";
	public final static String SAMPLE_TEXT = "Zona: XX";
	
	/*Player*/
	public final static String W_PLAYER_TITLE = "Reproductor";
	public final static String L_PROGRESS = "Barra de progresión";
	/**Etiqueta descriptiva de la escala de tiempos*/
	public final static String L_TIME_SCALE = "Escala de tiempos:";
	/**La escala de tiempos del reproductor*/
	public final static String L_TIME_SCALE2 = " mSec/día";
	public final static String L_SPEED = "x 50 mSec/día";
	public final static String L_DATE = "Fecha:";
	public final static String L_TIME = "Hora:";
	/**Texto del campo*/
	public final static String L_TIME2 = "Hora Minuto";
	public final static String TT_BTNPLAY = "Reproduce o Pausa la animación.";
	public final static String TT_DATECHOOSER1 = "Fecha representada";
	public final static String TT_BTN_DATECHOOSER = "Introducción de fecha concreta";
	public final static String TT_SLIDER = "Escala de tiempo en horas/segundo";
	public final static String TT_PROGRESS = "Porcentaje de progreso de la reproducción del registro.";
	public final static String TT_GO_DATE = "Ir a una fecha concreta";
	public final static String TT_TIME1 = "Hora de la representación";
	public final static String TT_TIME2 = "Saltar a una hora concreta";
	
	/*Principal*/
	public final static String W_MAIN_TITLE = "Simulación de enfermedades transmisibles en varios grupos de población";
	public final static String W_DEF_TITLE = "Parámetros Enfermedad";
	public final static String W_GRP_TITLE = "Grupos de Población";
	public final static String W_REL_TITLE = "Matriz de Contactos";
	public final static String W_MAP_TITLE = "Mapa";
	public final static String W_PAL_TITLE = "Leyenda";
	public final static String W_TE_TITLE = "Editor de Tablas";
	public final static String W_GE_TITLE = "Editor Gráfico";
	public final static String W_PE_TITLE = "Editor Paleta";
	public final static String M_NEW_PRJ = "Nuevo Proyecto";
	public final static String M_OPEN_PRJ = "Abrir Proyecto";
	public final static String M_IMPORT_PA = "Importar Modelo A";
	public final static String M_IMPORT_PB = "Importar Modelo B";
	public final static String M_SAVE_PRJ = "Guardar Proyecto";
	public final static String M_EXIT = "Salir";
	public final static String MVER_PRJ = "Proyecto";
	public final static String MM_MODELO = "Parámetros modelo";
	public final static String MM_FILES = "Archivo";
	public final static String MM_TOOLS = "Herramientas";
	public final static String MM_PREFERENCES = "Preferencias";
	public final static String MM_HELP = "Ayuda";
	public final static String MHELP_TABLES = "Formato Tablas";
	public final static String MHELP_USER_GUIDE = "Manual de Usuario";
	public final static String MHELP_ABOUT = "Acerca de...";
	public final static String MPREFERENCES_ES = "Español";
	public final static String MPREFERENCES_EN = "English";
	public final static String MPREFERENCES_FR = "Français";
	public final static String MPREFERENCES_DE = "Deutsch";
	public final static String MPREFERENCES_UR = "Українська";
	
	/*TablaEditor*/
	public final static String PANEL_NAME_TE = "panel_tabla";
	public final static String COMBO_NAME_TE = "Asignar tabla";
	public final static String TOOLBAR_NAME_TE = "Barra Herramientas";
	public final static String TT_SELECT_TYPE_TE = "Seleccione el tipo de tabla.";
	public final static String TT_NEW_TABLE_TE = "Crea una nueva tabla desde una plantilla";
	public final static String TT_NEW_ROW = "Crear fila nueva";
	public final static String TT_NEW_COL = "Crear columna nueva";
	public final static String TT_DEL_ROW = "Elimina las filas marcadas";
	public final static String TT_DEL_COL = "Elimina las columnas indicas";
	public final static String TT_DEL_TABLE = "Borrar tabla";
	public final static String TT_SAVE_TABLE = "Guardar tabla";
	public final static String TT_LOAD_TABLE = "Cargar tabla";
	public final static String TT_PRINT = "Imprimir";
	public final static String BTN_APPLY_TYPE_TE = "Aplicar tipo";
	public final static String BTN_SET_TO_TE = "Asignar a modulo:";
	/** Aviso de escritura de datos en módulo*/
	public final static String WARNING_1_TE = "Atención, esta acción sobreescribe los datos existentes en el módulo ";
	/** Notificación de datos aplicados al módulo*/
	public final static String NOTIFY_1_TE = "Datos aplicados al módulo: ";
	public final static String QUESTION_NEW_COL = "¿Nombre de la nueva columna?";
	/** Aviso de requerimiento de columna previa*/
	public final static String WARNING_2_TE = "Debe añadir alguna columna.";
	public final static String NOTIFY_FILE_LOADED = "Archivo Cargado";
	public final static String NOTIFY_FILED_SAVED = "Archivo guardado";
	/** Aviso de que no existen datos para guardar*/
	public final static String NOTIFY_NO_DATA_TO_SAVE = "No hay datos que guardar";
	/** Pregunta sobre eliminar la tabla actual*/
	public final static String WARNING_3_TE = "¿Desea eliminar la tabla actual con sus datos?";
	/** Aviso de eliminación de la tabla y sus datos*/
	public final static String WARNING_4_TE = "Eliminará la tabla actual con sus datos";
	public final static String REQUEST_CONFIRM = "¿Desea continuar?";
	/** Mensaje de selección de una plantilla*/
	public final static String MSG_CHOOSE_TEMPLATE_1 = "Escoja una plantilla";
	/** Título de ventana de selcción de plantilla*/
	public final static String MSG_CHOOSE_TEMPLATE_2 = "Selección de plantilla";

	/*VistaSIR*/
	public final static String BTN_RUN_SIMULATION = "Ejecutar simulación";
	public final static String NAME_PANEL_SIR = "Vista parámetros SIR";
	public final static String VALUE_FIELD = "El valor del campo ";
	public final static String VALUE_WRONG = "  es incorrecto: ";
	public final static String WRONG_VALUE = "Valor incorrecto";
	public final static String NO_ASSIGNED = "No asignado";
	public final static String NAME = "Nombre:";
	public final static String POPULATON = "Población:";
	public final static String AREA = "Superficie:";
	public final static String L_GRAPHIC_THUMB = "Representación Gráfica";
	public final static String L_NAME_Z = "Nombre del grupo de población";
	public final static String L_AREA = "Superficie en kilomentros cuadrados";
	public final static String L_S0 = "Número de personas susceptibles inicial";
	public final static String L_I0 = "Número de personas infectadas incial";
	public final static String L_R0 = "Número de personas recuperadas incialmente";
	public final static String L_P0 = "Prevalencia obtenida";
	public final static String TT_APPLY_CHANGES = "Aplica los cambios efectuados.";
	public final static String TT_RUN_SIMULATION = "Ejecuta el cálculo del modelo.";
	public final static String TT_PANEL_SIR = "Visulación de los parámetros particulares de la enfermedad.";
	
	/*About*/
	public final static String TXT_1_ABOUT = "<html><p style=\"text-align:center\">Simulación de Enfermedades Transmisibles";
	public final static String TXT_2_ABOUT = "<br>en";
	public final static String TXT_3_ABOUT = "<br>Varios Grupos de Población</p></html>";
	/** Nombre del director inicial*/
	public final static String NAME_DIRECTOR_INITIAL = "Dr. Fernando Morilla García";
	public final static String NAME_SILVERIO = "Silverio M.R.S.";
	public final static String TXT_5_ABOUT = "Dirección:";
	/** Texto "Acerca de..."*/
	public final static String TXT_6_ABOUT = "Acerca de esta aplicación";
	
	/* Operaciones */
	/** Operación de actualizar datos o vista.*/ 
	public final static String UPDATE = "Actualizar";
	/** Operación de abrir un componente o un fichero*/
	public final static String OPEN = "Abrir";
	/** Guardar un fichero*/
	public final static String SAVE = "Guardar";
	/** Guardar como un fichero en donde se especifique*/
	public final static String SAVE_AS = "Guardar como";
	/** Eliminar un dato o fichero*/
	public final static String DELETE = "Borrar";
	/** Editar un dato o fichero*/
	public final static String EDIT = "Editar";
	/** Ejecutar una acción*/  
	public final static String EXECUTE = "Ejecutar";
	/** Acción de reproducir en el player*/
	public final static String PLAY = "Reproducir";
	/** Acción de pausar la reproducción en el player*/
	public final static String PAUSE = "Pausar";
	/** Acción de repetir la reproducción del player*/
	public final static String REPLAY = "Repetir";
	/** Acción de notificación de cambios*/
	public final static String CHANGES = "Cambios";
	/** Acción de aplicar un cambio*/
	public final static String APPLY = "Aplicar";
	/** Acción de cerrar una ventana*/
	public final static String CLOSE = "Cerrar";
	
	/* Tipos de archivos y módulos */
	public final static String MDL = "Modelo";
	public final static String PAL = "Paleta";
	public final static String DEF = "Enfermedad";
	public final static String DEF_2 = "Definición";
	public final static String CSV = "Archivo general";
	public final static String CSV_2 = "General";
	public final static String HST = "Histórico";
	public final static String MAP = "Mapas";
	public final static String GRP = "Poblaciones";
	public final static String GRP_2 = "Grupos";
	public final static String PRJ = "Proyecto";
	public final static String REL = "Relaciones";
	public final static String PNG = "Archivos de imagen PNG";
	public final static String JPG = "Archivos de imagen JPG";
	public final static String JPEG = "Archivos de imagen JPEG";
	public final static String GIF = "Archivos de imagen GIF";
	public final static String IMG = "Archivos de imagen PNG, JPG, JPEG o GIF";
	public final static String PDF = "Archivo de manual PDF";
	public final static String MODEL_A = "Archivo Modelo A";
	public final static String MODEL_B = "Archivo Modelo B";
	public final static String PLAYER = "Player";
}
