/**  
* Concentra las etiquetas relacionadas con operaciones. Su objetivo es homogenizar
*  las llamadas a dichas operaciones desde diferentes partes de la aplicación, así
*   como permitir desacoplar las accionos del idioma.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 7 dic. 2021  
* @version 1.0  
*/  
package modelo;

/**  
 * @author Silverio Manuel Rosales Santana
 * @date 7 dic. 2021
 * @version versión 1.0
 */
public enum OperationsType {
	/** Operación de actualizar datos o vista.*/ 
	UPDATE(Labels_GUI.UPDATE),
	/** Operación de abrir un componente o un fichero*/
	OPEN(Labels_GUI.OPEN),
	/** Guardar un fichero*/
	SAVE(Labels_GUI.SAVE),
	/** Guardar como un fichero en donde se especifique*/
	SAVE_AS(Labels_GUI.SAVE_AS),
	/** Eliminar un dato o fichero*/
	DELETE(Labels_GUI.DELETE),
	/** Editar un dato o fichero*/
	EDIT(Labels_GUI.EDIT),
	/** Ejecutar una acción*/  
	EXECUTE(Labels_GUI.EXECUTE),
	/** Acción de reproducir en el player*/
	PLAY(Labels_GUI.PLAY),
	/** Acción de pausar la reproducción en el player*/
	PAUSE(Labels_GUI.PAUSE),
	/** Acción de repetir la reproducción del player*/
	REPLAY(Labels_GUI.REPLAY),
	/** Acción de notificación de cambios*/
	CHANGES(Labels_GUI.CHANGES),
	/** Acción de aplicar un cambio*/
	APPLY(Labels_GUI.APPLY),
	/** Acción de cerrar una ventana*/
	CLOSE(Labels_GUI.CLOSE),
	/** Etiqueta final a eliminar cuando este todo hecho*/
	END("Final");

    private final String display;
    private OperationsType(String s) { display = s; }

    @Override
    public String toString() { return display; }
    
    /**
     * Devuelve el enumerado al que pertenece la descripción.
     * @param value Valor del enúmerado (descripción)
     * @return Enumerado. Null en otro caso.
     */
    public static OperationsType getNum(String value) {
    	OperationsType[] values = values();
    	OperationsType op = null;
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
