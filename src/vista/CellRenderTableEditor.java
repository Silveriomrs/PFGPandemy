/**  
* <p>Colorea las celdas según el tipo de datos editado en la tabla</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 3 dic. 2021  
* @version 1.1  
*/  
package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Render especial para el Editor de tablas.
 * @author Silverio Manuel Rosales Santana
 * @date 3 dic. 2021
 * @version versión 1.1
 */
public class CellRenderTableEditor extends DefaultTableCellRenderer{

	   /** serialVersionUID*/  
		private static final long serialVersionUID = -8109726241316743022L;
		private boolean editable;
		
		/**
		 * <p>Title: </p>  
		 * <p>Description: </p>  
		 * @param edit TRUE si es editable la tabla. FALSE en otro caso.
		 */
		public CellRenderTableEditor(boolean edit) {
			this.editable = edit;
		}

		/**
	     * Dibuja la celda si pertenece a la fila, columna o condición indicada.
	     * <P>Esta funcion es llamada internamente por la tabla que use esta clase como renderizados</P>
	     * @param table Tabla
	     * @param value Valor de la celda
	     * @param isSelected Celda selecionada
	     * @param hasFocus Celta tiene el foco
	     * @param row Fila de la celda
	     * @param column Columna de la celda
	     * @return Celda de la tabla
	     */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		      super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
		      //Inicialmente todas con valores por defecto.
		      setFont(getFont().deriveFont(Font.PLAIN));
		      setHorizontalAlignment(SwingConstants. CENTER);
		      setBackground(new Color(224, 255, 255));
	          setForeground(Color.BLACK);
	          
	          if(column == 0 && !editable) {
	        	  setFont(getFont().deriveFont(Font.BOLD));
			      setHorizontalAlignment(SwingConstants.LEFT);
	        	  setBackground(new Color(128, 0, 0));
		          setForeground(Color.WHITE);
	          }else if(row % 2 == 0) {
	        	  setBackground(new Color(224, 255, 255));
		          setForeground(Color.BLACK);
	          }else {
	        	  setBackground(new Color(204, 255, 230));
		          setForeground(Color.BLACK);
	          }
	          
	          if(isSelected && editable) {
	        	  setFont(getFont().deriveFont(Font.BOLD));
	        	  setBackground(new Color(255, 204, 255));
		          setForeground(Color.BLACK);
	          }else if(hasFocus) {
	        	  setBackground(new Color(204, 204, 255));
		          setForeground(Color.BLACK);
	          }
	          
		      return this;
		   }
	
}
