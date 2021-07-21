/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.util.ArrayList;

import modelo.DCVS;
import modelo.ParserPoly;
import modelo.ParserSVG;
import vista.Mapa;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 13/07/2021
 * @version 1.0
 *
 */
public class ControladorMapa {

	private ArrayList<Color> paleta;
	private DCVS datos;
	private Mapa mapa;
	private ParserSVG parserSVG;
	private ParserPoly parserPoly;
	/**
	 * Constructor de la clase.
	 * @param datos base con los datos de las figuras.
	 */
	public ControladorMapa() {
		datos = new DCVS(null);
		paleta = new ArrayList<Color>();
		mapa = new Mapa(850,600);
		parserSVG = new ParserSVG();
		parserPoly = new ParserPoly();
		paletaBase();															//Carga la paleta básica por defecto.
		pinta();
	}
	
	/**
	 * Clase privada con los datos de una configuración de mapa básica por
	 * CCAA de España.
	 */
	private void pinta() {	
		ArrayList<String> poligonos = new ArrayList<String>();
		poligonos.add("846,1435 890,1397 823,1413 846,1435 846,1424 846,1435");
		poligonos.add("1515,182 1413,153 1420,118 1379,106 1305,192 1303,239 1281,251 1344,280 1384,323 1353,335 1396,354 1429,354 1432,246 1489,182 1515,182");
		poligonos.add("1186,106 1210,103 1219,84 1301,113 1365,96 1379,108 1310,180 1301,239 1272,239 1289,263 1258,258 1186,201 1186,189 1198,161 1186,134 1148,134 1186,106");
		poligonos.add("1186,101 1131,77 1007,96 962,134 981,173 1024,156 1060,223 1100,211 1088,175 1126,134 1145,134 1145,120 1174,108 1186,101");
		poligonos.add("1384,320 1348,301 1329,278 1272,258 1260,266 1224,239 1198,239 1188,325 1227,349 1234,325 1258,337 1243,347 1265,349 1272,320 1344,363 1364,349 1346,332 1384,320");
		poligonos.add("1138,504 1162,542 1141,569 1191,643 1191,705 1093,719 1119,683 1005,664 962,667 897,683 890,719 900,750 921,740 916,788 955,822 983,795 983,829 983,862 964,855 935,934 1026,991 1265,962 1286,1010 1270,1039 1301,1051 1315,1013 1410,986 1410,924 1460,924 1472,936 1480,900 1451,898 1437,869 1439,834 1410,803 1410,764 1444,757 1449,719 1410,686 1364,635 1410,566 1348,514 1281,521 1215,478 1162,480 1143,504 1138,504");
		poligonos.add("692,669 662,662 675,707 659,781 587,771 663,896 620,934 623,993 685,1034 783,1065 809,1044 809,1029 842,1017 845,1036 861,1027 852,972 938,919 945,881 964,843 983,853 969,826 976,807 952,824 904,779 914,745 897,752 883,721 888,664 869,683 835,657 807,664 776,631 692,669");
		poligonos.add("1301,1053 1310,1022 1391,991 1410,1001 1410,931 1460,924 1472,946 1456,1001 1472,1010 1468,1036 1501,1087 1520,1129 1391,1158 1372,1149 1346,1118 1348,1079 1301,1053");
		poligonos.add("2124,581 1706,879 1833,1051 2217,738 2172,628 2157,585 2124,581");
		poligonos.add("14,1285 702,1285 699,1552 0,1552 14,1285");
		poligonos.add("1501,182 1434,256 1429,361 1364,359 1364,416 1346,433 1351,464 1329,466 1332,507 1351,504 1410,566 1396,614 1386,609 1375,640 1410,686 1434,686 1429,669 1451,688 1468,698 1465,709 1491,719 1532,664 1568,643 1561,588 1596,566 1639,578 1639,509 1659,466 1656,414 1639,406 1699,332 1694,227 1694,235 1501,182");
		poligonos.add("1704,196 1702,332 1651,411 1659,454 1649,507 1642,516 1639,588 1678,614 1728,583 1704,569 1759,516 1904,476 2050,356 2036,309 2064,294 2038,268 1985,268 1959,292 1911,275 1704,196");
		poligonos.add("1508,1087 1470,1044 1477,993 1465,991 1472,946 1487,893 1456,893 1444,869 1451,829 1410,805 1425,764 1444,762 1456,707 1494,731 1501,717 1539,662 1575,647 1565,597 1592,566 1616,583 1635,583 1678,612 1561,812 1604,910 1639,922 1546,996 1539,1024 1525,1022 1508,1087");
		poligonos.add("678,1027 659,1063 637,1053 592,1141 616,1196 673,1189 752,1268 799,1366 859,1397 919,1313 988,1313 1019,1268 1250,1275 1277,1246 1310,1251 1315,1273 1391,1156 1363,1146 1332,1110 1344,1079 1267,1041 1281,1005 1262,970 1029,991 924,934 859,984 866,1032 840,1041 835,1024 814,1032 807,1055 778,1067 678,1027");
		poligonos.add("1136,504 1153,538 1129,571 1186,640 1191,700 1103,719 1122,683 1014,657 993,667 1074,554 1136,504");
		poligonos.add("702,189 803,160 841,189 853,177 935,158 954,134 975,175 1019,163 1057,225 1107,206 1091,177 1122,144 1179,136 1191,172 1167,180 1165,189 1172,201 1218,232 1191,232 1182,326 1215,345 1263,355 1277,331 1316,350 1347,376 1357,362 1364,410 1337,422 1340,455 1325,446 1309,486 1328,510 1285,515 1220,472 1167,472 1086,532 1040,589 987,666 966,659 935,678 892,683 889,656 863,671 834,652 827,644 817,659 796,644 779,623 717,659 688,659 697,642 688,529 779,419 724,400 726,359 683,359 669,338 702,292 688,261 662,254 702,189");
		poligonos.add("662,69 824,48 1007,100 968,129 944,127 916,153 841,168 832,177 801,156 685,189 652,98 654,100 662,69");
		poligonos.add("654,70 602,44 534,35 340,176 417,375 503,327 525,344 499,370 506,387 676,358 664,332 702,291 678,260 657,255 688,185 664,130 645,94 659,80 654,70");
	
		//Llamar función parser.
		for(int i = 0; i<poligonos.size();i++) {parser(poligonos.get(i), "test");}
		mapa.setZonaColor("p2", Color.red);
	}
	
	/**
	 * Establece el nivel de contagio de una zona con un color asociado a un
	 * nivel de contagio para su representación gráfica.
	 * @param z Nombre de la zona.
	 * @param l nivel de color para asignación.
	 */
	public void setColor(String z, int l) {mapa.setZonaColor(z,paleta.get(l));}
	
	/**
	 * Establece una nueva paleta de colores representativos de los grados de
	 * contagio. Hasta 10 niveles desde 0 a 9.
	 * @param paleta Colores representativos en formato sRGB(r,g,b).
	 */
	public void setPaleta(ArrayList<Color> paleta) {
		if(paleta != null && paleta.size()==10) {
			this.paleta = paleta;
		}
	}
	

	/**
	 * Genera una paleta base por defecto.
	 */
	private void paletaBase() {
		paleta.add(new Color( 82, 190, 128 ));
		paleta.add(new Color( 40, 180, 99 ));
		paleta.add(new Color( 174, 214, 241 ));
		paleta.add(new Color( 46, 134, 193 ));
		paleta.add(new Color( 247, 220, 111 ));
		paleta.add(new Color( 243, 156, 18 ));
		paleta.add(new Color( 210, 180, 222 ));
		paleta.add(new Color( 118, 68, 138 ));
		paleta.add(new Color( 230, 126, 34 ));
		paleta.add(new Color( 231, 76, 60 ));
		paleta.add(new Color( 178, 186, 187 ));
		
		//Paleta alternativa:
		//rgb(241, 196, 15) rgb(40, 180, 99) rgb(52, 152, 219) rgb(244, 208, 63)
		//rgb(236, 112, 99) rgb(203, 67, 53)
	}
		
	/**
	 * Realiza la conversión de datos de texto a un poligono cerrado.
	 * @param raw String con las coordenadas de los puntos componentes del poligono.
	 * @param n String con el nombre a asignar al poligono.
	 */
	private void parser(String raw, String n) {		
		String nombre = "p" + mapa.getNumZones();
		mapa.addZona(nombre,parserPoly.parser(raw));
	    mapa.setZonaColor(nombre, Color.BLUE);
	}
	
}
