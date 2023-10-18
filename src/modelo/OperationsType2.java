/**  
* Concentra las etiquetas relacionadas con operaciones. Su objetivo es homogenizar
*  las llamadas a dichas operaciones desde diferentes partes de la aplicación, así
*   como permitir desacoplar las accionos del idioma.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 oct. 2023  
* @version 1.2  
*/  
package modelo;

/**  
 * @author Silverio Manuel Rosales Santana
 * @date 17 oct. 2023
 * @version versión 1.2
 */
public enum OperationsType2 {
	//TODO: Añadir comentarios JavaDOC a las nuevas entradas.
	/** Acción de aplicar un cambio*/
	APPLY(Labels_GUI.APPLY),
	/** Acción de notificación de cambios*/
	CHANGES(Labels_GUI.CHANGES),
	/** Acción de cerrar una ventana*/
	CLOSE(Labels_GUI.CLOSE),
	/** Eliminar un dato o fichero*/
	DELETE(Labels_GUI.DELETE),
	/** Editar un dato o fichero*/
	EDIT(Labels_GUI.EDIT),
	/** Editar gráfico*/
	EDIT_GRAPHIC(Labels_GUI.W_GE_TITLE),
	/** Editar Paleta de colores / Escala de valores*/
	EDIT_PAL(Labels_GUI.W_PE_TITLE),
	/** Ejecutar una acción*/  
	EXECUTE(Labels_GUI.EXECUTE),
	/** Acción de salir*/
	EXIT(Labels_GUI.M_EXIT),
	HELP_ABOUT(Labels_GUI.MHELP_ABOUT),
	HELP_TABLES(Labels_GUI.MHELP_TABLES),
	HELP_USER_GUIDE(Labels_GUI.MHELP_USER_GUIDE),
	/** Importar tabla Vensim tipo A*/
	IMPORT_A(Labels_GUI.M_IMPORT_PA),
	/** Importar tabla Vensim tipo B*/
	IMPORT_B(Labels_GUI.M_IMPORT_PB),
	/** Selección idioma Español*/
	LANG_ES(Labels_GUI.MPREFERENCES_ES),
	/** Selección idioma Inglés*/
	LANG_EN(Labels_GUI.MPREFERENCES_EN),
	/** Selección idioma Francés*/
	LANG_FR(Labels_GUI.MPREFERENCES_FR),
	/** Selección idioma Alemán*/
	LANG_DE(Labels_GUI.MPREFERENCES_DE),
	/** Selección idioma Ucraniano*/
	LANG_UR(Labels_GUI.MPREFERENCES_UR),
	/** Nuevo elemento (Módulo u otro)*/
	//TODO: Ojo! esto puede causar problemas a futuro.
	NEW(null),
	/** Operación de abrir un componente o un fichero*/
	OPEN(Labels_GUI.OPEN),
	/** Acción de reproducir en el player*/
	PLAY(Labels_GUI.PLAY),
	/** Acción de pausar la reproducción en el player*/
	PAUSE(Labels_GUI.PAUSE),
	/** Acción de repetir la reproducción del player*/
	REPLAY(Labels_GUI.REPLAY),
	/** Guardar un fichero*/
	SAVE(Labels_GUI.SAVE),
	/** Guardar como un fichero en donde se especifique*/
	SAVE_AS(Labels_GUI.SAVE_AS),
	/** Operación de actualizar datos o vista.*/ 
	UPDATE(Labels_GUI.UPDATE),
	VIEW_PRJ(Labels_GUI.MVER_PRJ),
	VIEW_DEF(Labels_GUI.W_DEF_TITLE),
	VIEW_GRP(Labels_GUI.W_GRP_TITLE),
	VIEW_MAP(Labels_GUI.W_MAP_TITLE),
	VIEW_PAL(Labels_GUI.W_PAL_TITLE),
	VIEW_REL(Labels_GUI.W_REL_TITLE),
	/** Etiqueta final a eliminar cuando este todo hecho*/
	END("Final");

    private final String display;
    private OperationsType2(String s) { display = s; }

    @Override
    public String toString() { return display; }
    
    /**
     * Devuelve el enumerado al que pertenece la descripción.
     * @param value Valor del enúmerado (descripción)
     * @return Enumerado. Null en otro caso.
     */
    public static OperationsType2 getNum(String value) {
    	OperationsType2[] values = values();
    	OperationsType2 op = null;
    	boolean done = false;	
    	int max = values.length;
    	int i = 0;
    	//Bucle para recorrer el array hasta encontrar la coincidencia.
    	while(!done && i < max) {
    		String aux = values[i].toString();
    		if(value.equals(aux)){
    			op = values[i];
    			done = true;
    		}		
    		i++;
    	}
    	if(op == null) System.out.println("OperationsType > getNum > Error, valor no encontrado para: " + value);
    	return op;
    }

}
