/**
 * ParserSVG para la conversión de datos gráficos, a fin de poder usar dichos
 * gráficos en la representación gráfica de los datos de la aplicación.
 */
package modelo;

import java.awt.Polygon;

/**
 * Clase encargada de convertir los datos gráficos en alguno de los formatos
 * sorportados en poligonos.
 * @author Silverio Manuel Rosales Santana
 * @date 16/07/2021
 * @version 1.0
 */
public class ParserSVG {

	/**
	 * Crear un poligono cerrado a partir de los datos que lo conforman.
	 * Dichos datos estarán organizados en formato SVC.<p>
	 * Ejemplo:
	 * 		"M150 0 L75 200 L225 200 Z"<p>
	 * Donde:<br/>
		    M = moveto<br/>
		    L = lineto<br/>
		    H = horizontal lineto<br/>
		    V = vertical lineto<br/>
		    C = curveto<br/>
		    S = smooth curveto<br/>
		    Q = quadratic Bézier curve<br/>
		    T = smooth quadratic Bézier curveto<br/>
		    A = elliptical Arc<br/>
		    Z = closepath		    
	 * @see "https://www.w3schools.com/graphics/svg_path.asp"
	 * @param raw cadena de texto con cada uno de los puntos que conforman el poligono.
	 * @return poligono creado a partir de los puntos dados.
	 */
	public Polygon parser(String raw) {
		String[] raw2= raw.split(" ");
		int size = raw2.length;
		int[] polX = new int[size];
		int[] polY = new int[size];		
		//Lectura y almacenamiento de coordenadas.
		int escala = 3;
		for(int i=0; i<size;i++) {
			//Separación coordenadas.
			String[] raw3= raw2[i].split(",");
			comandos(raw3);
			//Obtener coordenadas convertidas a int.
			polX[i] = Integer.valueOf(raw3[0])/escala;
			polY[i] = Integer.valueOf(raw3[1])/escala;
		}
		
		return new Polygon(polX, polY, size);
	}
	
	
	/**
	 * <p>Title: comandos</p>  
	 * <p>Description: Realiza la conversión de los parámetros que componen
	 * el gráfico SVG y sus valores</p> 
	 * @param cadena Arreglo de los parámetros y sus valores.
	 */
	private void comandos(String[] cadena) {
		
	}
	
//Provincia de Murcia como ejemplo test del parser.
// d="m619.1 678.68l-1.72-.9-.06-.03-.59-.61-.47-.64-.35-.67-.7-.38-.42-.73-.54-.6-.36-.67-.6-.44-.22-.79-.61-.43-.06-1.51-.41-.76-.56-.52-.56-.67-.62-.52-.78-.29-.73-.44h-.001l.09-.47-.11-.75.53-.61.32-.96-.05-.79.38-.69.43-.61.64-.38.54-.76 1.74-1.83 1.06-1.28.42-.7 1.23-1.19-.07-.78.56-1.66 1.12-1.16.16-.96.33-.7.16-.9.49-.61.81-.47.76-.2.58-.61.78-1.54.74-.32 2.46-.29.71-.35.61-.52 1.38-.62.38-.69.59-.67.67-.47.15-.75.43-.76.49-.58.31-.7.43-.61 1.03-1.28.73.29.68.53.76-.38.58.52.38.76.69.52.58.67.81.06.65-.47.73-.2.7-.38.89-.17h.76l.76-.15.83.06 1.48-.61.64-.44.67-.32.61-.67.67-.58.98-1.25.63-.46 1.52-.18.63-.43.42-.7.61-.46 1.54-.5v-.75l-.5-.85 2.13-.38.09-.55.49-.23.56.12.49-.32 1.03.08.47.29.67.99.52.18.53-.18.07-.52.49-.23.52.14.76.73.31.43.25 1.08-.09.61-.49-.09-.05.61.22.56.09.52.7.96.44.29 1.7.81.99.29 1.47-.46.74-.09.65.41h.83l1.54-.7.6-.67.79-.38.53-1.51.45-.61 1.32-.84.8-.41.61-.61.76-.43.62-.61.45-.62.38-.72.23-1.69-.38-2.47-.29-.81.2-.93.09-.85-.25-.87-.07-.76.07-1.65-.6-2.36-.14-.84-.51-.76-.09-.75-.23-.73.79-1.54.62-1.66.65-.43 1.5-.53.2-.78.38-.64-.07-.93.12-.76-.21-.75.16-.76.27-.81.18-.99-.38-.67-.02-.84 1.2-.99.36-.7.23-.81.44-.61 2.73-1.95.76-.35.83-.12.74.03 1.02 2.18.68.29.73-.4 1.7-1.63.76-.09 2.24-1.1.45-.7.24-.79.41-.63 1.34-.93 2.28-.73 1.56.2.7-.29.44-.61.65-.38 1.28.9h.74l.8.24.74.32.85.2.58.58-.16.84-.06.82.51 1.42.69.38.56.55.61.44.42.67.69.38.68.61.71.32.52.61 1.97 1.47-.001.001.2 1.51.38.76.07 1.51.29 1.66.38.69.02.82.36.75-.62 2.65-.12.75-.76 1.57-.2.85-.09.75.67 1.51.23.82-.54.61-.36.7-.49.72-1.45.93-.45.61-1.32 1.2-.47.61-.65.43-.15.76.15.84-.2.73.23.9-.16.84.11.82-.18 1.01.27.73-.2.82.22.78.05.76.15.9.83-.09.96.12.76-.03 1.39.69.89.27.72.08.6.56.68.46.35.64.63.55.31.88-.02.93.42.61.07.81.45.64.16.78-.3.7.05.79-.27.72-.07.76-.33.76-.18.84-.25 2.47-.22.84-.49.82-.16.78-.51.58-.41.76-.44.61-.41.78-.67.59-.82 1.51-.36.84-.02.76.42.75.31.96-.29.7.52.61.09 1.74.33.67 1.3 1.05.4.84.85 1.43.58.58.43.75.15.93.39.82.78 1.37.31.9 1.25.93.14.75.47.67.27.87.44.67.14.79.44.64 1.44 1.36.29.7.49.58.78.44 1.12 1.22.4.7 1.3 1.13.45.81.67.47.78-.15.76.15 1.39 1.22.4.67.71.32.88-.41.82-.14.21 2.25.31 1.03.16 1.61.07.14.4.9v.39l-.24.26-.97-.45-.19-1.23-.83-1.69-.25-.2-.49.04-.76.81-.55 1.04.02 1.63-.43 1.17-.87.78-1.98 1.77-.61.95-.3 1.07-.66 1 .49 1.16 2 2.86 1.13 1.9 2.6 1.69 1.82.37.37.24.36.73.62.06.32.27.44-.04.38-.5.47-.26.19-.35-.32-1.35-.71-.78-.31-.71-.07-.46-.45-3.5.07-3.05.21-.45.24-.05.13.44-.31 2.01.45 3.34.03.22.91 2.65 1 2.18.23.2 1.07.11.16.22-.29.47-.89.44-.85 1.38-1.31.92-1.12.33-.16.05-1.44.12-.56.18-3.57 2.11-1.55-.001-.28-.14.17-.53-.16-.31-.34-.08-.28.14-.55.69-.51-.11-.51.33-.48-.25-.21.07-.08.3-.09.35-.52.35-3.26 1.46-1.38-.28-1.18.15-.19-.12-.06-.32.14-.26.67-.04.19-.33-.09-.29-.41-.37-.92-.13-.85-.78.03-.36.21-.47.03-.06.21-.47-.18-.41-.37-.17-.79.001-.55-.2-.16.21.37.52.17.23.22.92-.16.29-.49-.04-.37-.29-.55-.03-.49.35-.36.5-.81.04-.66-.49-.56.16-.76-.25-.84.18-.78-.66-2.05 1.73-.72.15-.49.36-.57.61-.23.48-.001.54v.001l.2.41.25.17 1.02.71-.24.17-1.36-.36-1.71-.06-.91-.16-.5-.08-.5-.24-.23-.34.26-.74-.04-.53-.34-.39-1.15-.47-2.65-.63-.1-.03-1.32.001-.61.19-.62.45-.15.33.04.43-.25.29-2.48.04-.81-.21-1.11-.29-.76.07-1.14.97-.68.18-.86.57-.25.26-2.6 2.73-.11.19-.36.65-1.46.32-1.07.49-1.98 1.38-.7.76-.84 1.57-.11.3-.67 1.76-.05.84.3.78.04.09-.17.28-.15.09-1.03.57-.35-.12-.2-.43-.35-.22-.71.04-2.16 1.54-.84.07-.53.64-1.02-.32-.14.18-.3.34-.53-.14-.34.06-1.96 1.84-1.13.49-.24.34.12.36-.12.13-.05.001v-.001l-1.03-1.22-1.41-1.05-.51-.58-.63-.46-.82-.47-1.19-1.13-.83-.53-1.92-1.59-2.28-.67-3.02-.67-.65.41.2.81.05.76-.61-.53-.76-.35-1.36-.9-.43-.7-.33-.84-.61-.52-.2-.73-.8-1.31-.52-.61-.47-.75-.64-.53-.5-.75-.62-.61-1.32-2.85-1.07-1.43-1.12-1.27-.27-.85-.38-.67.11-.75-.58-.67-.63-.38-.44-.67-.63-1.54-.2-.76-.45-.75-.6-.44-.58-.55-.09-.76-.23-.81v-.9l-.11-.76.25-.75-.18-1.75.18-.9.35-.69.18-.91.32-.69-.16-.82-.45-.78v-.76l-.18-1.63.03-.75-.12-.85.16-.75.45-.7.29-.73-.11-2.53.02-.81.45-.78.58-.53.96-1.28-.18-.75-1.41-.7-.71-.2-.74.14-.34.76-.78.08-1.5-.29-.62-.37-.83-.21-.72-.4-.76.23-.58.52-.71-.26-.7-.47-.36-.72-.53-.61-.58-.44h-.74l-1.03-.14-.76.37-.74.53-.71.38-1.28-1.11z"
	
}
