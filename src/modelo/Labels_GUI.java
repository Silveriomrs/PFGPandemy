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
	//TODO: Desacoplar si es necesario, para poder usar multiples idiomas.
	//Opción 1... cargar desde exterior fichero de idioma y adjuntar a un HashMap.
	//Opción 2... cargar desde exterior fichero de idioma y asígnar leyendo etiqueta > dato asignado.
	
	/*Archivos*/
	public static String W_FILES_TITLE = "Módulo de Archivos";
	public static String W_MODULES_TITLE = "Modulos";
	public static String FILES_PANEL_NAME = "Vista de módulos cargados";
	public static String TT_FILES_PANEL = "Selección archivos asignados a los módulos.";
	public static String TT_W_FILES = "Visulación de los ficheros asignados a cada módulo.";
	public static String TT_FILE_SELECTED = "Archivo seleccionado";
	public static String TT_L_PRJ = "Archivo con los parámetros del modelo.";
	public static String TT_L_DEF = "Archivo con la definición propia de la enfermedad.";
	public static String TT_L_REL = "Matriz de contactos. Refleja las relaciones entre los grupos";
	public static String TT_L_PAL = "Paleta de colores personalizada.";
	public static String TT_L_HST = "Histórico de evolución con los datos de la simulación.";
	public static String PATH_NO_DEFINED = "Datos sin destino definido.";
	public static String ERR_FILE_UNKNOWN = "Archivo seleccionado no reconocido.";
	
	/*ControladorModulos*/
	/** Mensaje de error de proyecto que hace referencia a un módulo no encontrado*/
	public static String ERR_MSG_1_CM = "Archivo de proyecto incorrecto. \nReferencia un módulo que no está contenido en la misma carpeta:\n ";
	public static String WARNING_1_DATA_LOSS = "Los cambios no guardados se perderán";
	public static String REQUEST_EXIT_CONFIRM = "¿Desea salir del programa?";
	public static String ERR = "Error";
	public static String INF = "Información";
	public static String WARN = "¡Antención!";
	public static String QST = "Consulta";
	public static String WARNING_2_OVERWRITE = "Se escribirán los datos en disco\n¿continuar?";
	public static String ERR_LOAD_MODULE = "No se puede cargar el módulo: ";
	public static String ERR_MODULE_LESS_NG = "dicho módulo no tiene definido suficientes parámetros para\nel número de grupos de población indicados en el modelo";
	public static String L_BORDER_PANEL_TE = "Editor General";
	public static String L_BORDER_PANEL_MAP = "Visor de Mapa";
	public static String L_BORDER_PANEL_GRP = "Visor Grupos de Poblaci\u00F3n";
	public static String L_BORDER_PANEL_PRJ = "Parámetros del Modelo";
	public static String L_BORDER_PANEL_DEF = "Configuraci\u00F3n SIR";
	/** Usado en el CM cuando se edita un módulo de manera externa en el TE*/
	public static String TE_TITLE_EXTERNAL = "Editando modulo: ";
	public static String DEFAULT_NAME_GRP = "Grupo ";
	
	/*Graficas Chart*/
	public static String M_VER = "Ver";
	public static String M_CASOS = "Casos";
	public static String M_TASAS = "Tasas";
	public static String M_TASA = "Tasa";
	public static String L_X = "Nivel";
	public static String L_Y = "Tiempo  = días)";
	public static String L_TITLE = "Zona: ";
	public static String L_TITLE_FRAME = "Evolución pandemica.";
	
	/*Paleta*/
	public static String SEL_COLOR = "Seleccione nuevo color";
	
	/*ParametrosProyecto*/
	public static String PROJECT_NAME = "Propiedades Proyecto";
	/** Aviso de eliminación de datos en el siguiente paso*/
	public static String WARNING_1 = "El siguiente paso eliminará parcial o toltamente los datos almacenados previamente ¿desea continuar?";
	/** Aviso NG incorrecto*/
	public static String WARNING_2 = "Número de grupos de estudio incorrecto: ";
	/** Aviso NG <= 0 */
	public static String WARNING_3 = "El número de grupos debe ser mayor de cero.";
	public static String WARNING_EMPTY_FIELD = "El campo nombre no puede estar vacio.";
	public static String VERSION_TITLE = "Versi\u00F3n ";
	public static String VERSION_NUMBER = "1.2";
	public static String NAME_MODULES = "Módulos";
	public static String L_AUTHOR = "Autor/a:";
	public static String L_DATE_MODIFIED = "Fecha modificación:";
	public static String L_DATE_CREATION = "Fecha de creación: ";
	public static String L_MANDATORY_FIELD = "Campo obligatorio";
	public static String L_DESCRIPTION_MODEL = "Descripción del modelo:";
	public static String L_NG = "Número de Grupos de estudio:";
	public static String L_FILES = "Archivos del modelo:";
	public static String TT_DESCRIPTION = "Introduzca cualquier texto descriptivo que crea necesario acerca del modelo.";
	public static String TT_DATE_MODIFIED = "Indica la última fecha de modificación.";
	public static String TT_DATE_CREATION = "Indica la fecha de creación.";
	public static String TT_BTN_APPLY = "Guarda los cambios efectuados.";
	public static String TT_MODEL_NAME = "Nombre del modelo, se usará para dar nombre a los archivos que lo componen.";
	public static String TT_AUTHOR = "Autor del modelo.";
	public static String TT_VERSION_MODEL = "Número de versión del modelo.";
	public static String TT_NG = "Número de grupos de población que componen el modelo.";
	public static String BTN_APPLY = "Aplicar Cambios";
	public static String PRJ_TITLE = "Título del Proyecto:";

	/*Pizarra*/
	public static String L_DELETE = "Eliminar";
	public static String L_ADD_TO = "Asignar a";
	public static String BTN_CLEAR = "Limpiar";
	public static String BTN_COMPOSE = "Componer";
	public static String TT_COMPOSE = "Cierra la figura y crea el poligono.\nUna vez creado debe asignarse a una zona.";
	public static String TT_APPLY = "Aplica las asignaciones seleccionadas.";
	public static String TT_CLEAR = "Limpiar la pizarra y redibujar las zonas.";
	public static String TT_OPEN = "Abre una imagen para establecer de papel de fondo.";
	public static String TT_SAVE = "Guardar los cambios realizados.";
	public static String TT_SELECT_ZONE = "Seleccionar una zona para asignar la figura.";
	public static String TT_REMOVE_ZONE = "Seleccionar una zona a reasignar.";
	public static String NAME_CANVAS = "pizarra";
	public static String SAMPLE_TEXT = "Zona: XX";
	
	/*Player*/
	public static String W_PLAYER_TITLE = "Reproductor";
	public static String L_PROGRESS = "Barra de progresión";
	/**Etiqueta descriptiva de la escala de tiempos*/
	public static String L_TIME_SCALE = "Escala de tiempos:";
	/**La escala de tiempos del reproductor*/
	public static String L_TIME_SCALE2 = " mSec/día";
	public static String L_SPEED = "x 50 mSec/día";
	public static String L_DATE = "Fecha:";
	public static String L_TIME = "Hora:";
	/**Texto del campo*/
	public static String L_TIME2 = "Hora Minuto";
	public static String TT_BTNPLAY = "Reproduce o Pausa la animación.";
	public static String TT_DATECHOOSER1 = "Fecha representada";
	public static String TT_BTN_DATECHOOSER = "Introducción de fecha concreta";
	public static String TT_SLIDER = "Escala de tiempo en horas/segundo";
	public static String TT_PROGRESS = "Porcentaje de progreso de la reproducción del registro.";
	public static String TT_GO_DATE = "Ir a una fecha concreta";
	public static String TT_TIME1 = "Hora de la representación";
	public static String TT_TIME2 = "Saltar a una hora concreta";
	
	/*Principal*/
	public static String W_MAIN_TITLE = "Simulación de enfermedades transmisibles en varios grupos de población";
	public static String W_DEF_TITLE = "Parámetros Enfermedad";
	public static String W_GRP_TITLE = "Grupos de Población";
	public static String W_REL_TITLE = "Matriz de Contactos";
	public static String W_MAP_TITLE = "Mapa";
	public static String W_PAL_TITLE = "Leyenda";
	public static String W_TE_TITLE = "Editor de Tablas";
	public static String W_GE_TITLE = "Editor Gráfico";
	public static String W_PE_TITLE = "Editor Paleta";
	public static String M_NEW_PRJ = "Nuevo Proyecto";
	public static String M_OPEN_PRJ = "Abrir Proyecto";
	public static String M_IMPORT_PA = "Importar Modelo A";
	public static String M_IMPORT_PB = "Importar Modelo B";
	public static String M_SAVE_PRJ = "Guardar Proyecto";
	public static String M_EXIT = "Salir";
	public static String MVER_PRJ = "Proyecto";
	public static String MM_MODELO = "Parámetros modelo";
	public static String MM_FILES = "Archivo";
	public static String MM_TOOLS = "Herramientas";
	public static String MM_PREFERENCES = "Preferencias";
	public static String MM_HELP = "Ayuda";
	public static String MHELP_TABLES = "Formato Tablas";
	public static String MHELP_USER_GUIDE = "Manual de Usuario";
	public static String MHELP_ABOUT = "Acerca de...";
	public static String MPREFERENCES_ES = "Español";
	public static String MPREFERENCES_EN = "English";
	public static String MPREFERENCES_FR = "Français";
	public static String MPREFERENCES_DE = "Deutsch";
	public static String MPREFERENCES_UR = "Українська";
	
	/*TablaEditor*/
	public static String PANEL_NAME_TE = "panel_tabla";
	public static String COMBO_NAME_TE = "Asignar tabla";
	public static String TOOLBAR_NAME_TE = "Barra Herramientas";
	public static String TT_SELECT_TYPE_TE = "Seleccione el tipo de tabla.";
	public static String TT_NEW_TABLE_TE = "Crea una nueva tabla desde una plantilla";
	public static String TT_NEW_ROW = "Crear fila nueva";
	public static String TT_NEW_COL = "Crear columna nueva";
	public static String TT_DEL_ROW = "Elimina las filas marcadas";
	public static String TT_DEL_COL = "Elimina las columnas indicas";
	public static String TT_DEL_TABLE = "Borrar tabla";
	public static String TT_SAVE_TABLE = "Guardar tabla";
	public static String TT_LOAD_TABLE = "Cargar tabla";
	public static String TT_PRINT = "Imprimir";
	public static String BTN_APPLY_TYPE_TE = "Aplicar tipo";
	public static String BTN_SET_TO_TE = "Asignar a modulo:";
	/** Aviso de escritura de datos en módulo*/
	public static String WARNING_1_TE = "Atención, esta acción sobreescribe los datos existentes en el módulo ";
	/** Notificación de datos aplicados al módulo*/
	public static String NOTIFY_1_TE = "Datos aplicados al módulo: ";
	public static String QUESTION_NEW_COL = "¿Nombre de la nueva columna?";
	/** Aviso de requerimiento de columna previa*/
	public static String WARNING_2_TE = "Debe añadir alguna columna.";
	public static String NOTIFY_FILE_LOADED = "Archivo Cargado";
	public static String NOTIFY_FILED_SAVED = "Archivo guardado";
	/** Aviso de que no existen datos para guardar*/
	public static String NOTIFY_NO_DATA_TO_SAVE = "No hay datos que guardar";
	/** Pregunta sobre eliminar la tabla actual*/
	public static String WARNING_3_TE = "¿Desea eliminar la tabla actual con sus datos?";
	/** Aviso de eliminación de la tabla y sus datos*/
	public static String WARNING_4_TE = "Eliminará la tabla actual con sus datos";
	public static String REQUEST_CONFIRM = "¿Desea continuar?";
	/** Mensaje de selección de una plantilla*/
	public static String MSG_CHOOSE_TEMPLATE_1 = "Escoja una plantilla";
	/** Título de ventana de selcción de plantilla*/
	public static String MSG_CHOOSE_TEMPLATE_2 = "Selección de plantilla";

	/*VistaSIR*/
	public static String BTN_RUN_SIMULATION = "Ejecutar simulación";
	public static String NAME_PANEL_SIR = "Vista parámetros SIR";
	public static String VALUE_FIELD = "El valor del campo ";
	public static String VALUE_WRONG = " es incorrecto ";
	public static String WRONG_VALUE = "Valor incorrecto";
	public static String NO_ASSIGNED = "No asignado";
	public static String NAME = "Nombre:";
	public static String POPULATON = "Población:";
	public static String AREA = "Superficie:";
	public static String L_GRAPHIC_THUMB = "Representación Gráfica";
	public static String L_NAME_Z = "Nombre del grupo de población";
	public static String L_AREA = "Superficie en kilomentros cuadrados";
	public static String L_S0 = "Número de personas susceptibles inicial";
	public static String L_I0 = "Número de personas infectadas incial";
	public static String L_R0 = "Número de personas recuperadas incialmente";
	public static String L_P0 = "Prevalencia obtenida";
	public static String TT_APPLY_CHANGES = "Aplica los cambios efectuados.";
	public static String TT_RUN_SIMULATION = "Ejecuta el cálculo del modelo.";
	public static String TT_PANEL_SIR = "Visulación de los parámetros particulares de la enfermedad.";
	
	/*About*/
	public static String TXT_1_ABOUT = "<html><p style=\"text-align:center\">Simulación de Enfermedades Transmisibles";
	public static String TXT_2_ABOUT = "<br>en";
	public static String TXT_3_ABOUT = "<br>Varios Grupos de Población</p></html>";
	/** Nombre del director inicial*/
	public static String NAME_DIRECTOR_INITIAL = "Dr. Fernando Morilla García";
	public static String NAME_SILVERIO = "Silverio M.R.S.";
	public static String TXT_5_ABOUT = "Dirección:";
	/** Texto "Acerca de..."*/
	public static String TXT_6_ABOUT = "Acerca de esta aplicación";
	
	/* Operaciones */
	/** Operación de actualizar datos o vista.*/ 
	public static String UPDATE = "Actualizar";
	/** Operación de abrir un componente o un fichero*/
	public static String OPEN = "Abrir";
	/** Guardar un fichero*/
	public static String SAVE = "Guardar";
	/** Guardar como un fichero en donde se especifique*/
	public static String SAVE_AS = "Guardar como";
	/** Eliminar un dato o fichero*/
	public static String DELETE = "Borrar";
	/** Editar un dato o fichero*/
	public static String EDIT = "Editar";
	/** Ejecutar una acción*/  
	public static String EXECUTE = "Ejecutar";
	/** Acción de reproducir en el player*/
	public static String PLAY = "Reproducir";
	/** Acción de pausar la reproducción en el player*/
	public static String PAUSE = "Pausar";
	/** Acción de repetir la reproducción del player*/
	public static String REPLAY = "Repetir";
	/** Acción de notificación de cambios*/
	public static String CHANGES = "Cambios";
	/** Acción de aplicar un cambio*/
	public static String APPLY = "Aplicar";
	/** Acción de cerrar una ventana*/
	public static String CLOSE = "Cerrar";
	
	/* Tipos de archivos y módulos */
	public static String MDL = "Modelo";
	public static String PAL = "Paleta";
	public static String DEF = "Enfermedad";
	public static String DEF_2 = "Definición";
	public static String CSV = "Archivo general";
	public static String CSV_2 = "General";
	public static String HST = "Histórico";
	public static String MAP = "Mapas";
	public static String GRP = "Poblaciones";
	public static String GRP_2 = "Grupos";
	public static String PRJ = "Proyecto";
	public static String REL = "Relaciones";
	public static String ANY = "Cualquiera";
	public static String PNG = "Archivos de imagen PNG";
	public static String JPG = "Archivos de imagen JPG";
	public static String JPEG = "Archivos de imagen JPEG";
	public static String GIF = "Archivos de imagen GIF";
	public static String IMG = "Archivos de imagen PNG, JPG, JPEG o GIF";
	public static String PDF = "Archivo de manual PDF";
	public static String MODEL_A = "Archivo Modelo A";
	public static String MODEL_B = "Archivo Modelo B";
	public static String PLAYER = "Player";
}
