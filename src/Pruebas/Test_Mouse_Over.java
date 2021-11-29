/**  
* <p>Title: Test_Mouse_Over.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 20 sept. 2021  
* @version 1.0  
*/  
package Pruebas;


import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import vista.GraficasChart;

/**
 * <p>Title: Test_Mouse_Over</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 20 sept. 2021
 * @version versión
 */
public class Test_Mouse_Over
{
    @SuppressWarnings("javadoc")
	public static void main(String[] args)
    {
        PolygonPanel polygonPanel = new PolygonPanel();
        PolygonSelector selector = new PolygonSelector(polygonPanel);
        polygonPanel.addMouseListener(selector);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(polygonPanel);                         // j2se 1.5
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
  
class PolygonPanel extends JPanel
{
    /** serialVersionUID*/  
	private static final long serialVersionUID = -4832440883971624804L;
	Polygon[] polygons;
    Color[] colors;
  
    public PolygonPanel()
    {
        setBackground(Color.white);
    }
  
    @Override
	protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        if(polygons == null)
            initPolygons();
        for(int j = 0; j < polygons.length; j++)
        {
            g2.setPaint(colors[j]);
            g2.fill(polygons[j]);
        }
    }
  
    private void initPolygons()
    {
        polygons = new Polygon[4];
        int w = getWidth();
        int h = getHeight();
        int R = Math.min(w,h)/6;
        int[][] xy = generateShapeArrays(w*3/12, h*7/24, R, 3);        
        polygons[0] = new Polygon(xy[0], xy[1], 3);
        xy = generateShapeArrays(w*9/12, h*6/24, R, 5);
        polygons[1] = new Polygon(xy[0], xy[1], 5);
        xy = generateShapeArrays(w*3/12, h*18/24, R, 7);
        polygons[2] = new Polygon(xy[0], xy[1], 7);
        xy = generateShapeArrays(w*9/12, h*18/24, R, 9);
        polygons[3] = new Polygon(xy[0], xy[1], 9);
        colors = new Color[] {
            Color.orange, Color.blue, Color.red, Color.magenta
        };
    }
  
    private int[][] generateShapeArrays(int cx, int cy, int R, int sides)
    {
        int radInc = 0;
        if(sides % 2 == 0)
            radInc = 1;
        int[] x = new int[sides];
        int[] y = new int[sides];
        for(int i = 0; i < sides; i++)
        {
            x[i] = cx + (int)(R * Math.sin(radInc*Math.PI/sides));
            y[i] = cy - (int)(R * Math.cos(radInc*Math.PI/sides));
            radInc += 2;
        }
        // keep base of triangle level
        if(sides == 3)
            y[2] = y[1];
        return new int[][] { x, y };
    }
}
  
class PolygonSelector extends MouseAdapter {
    PolygonPanel polygonPanel;
    Random seed;
    GraficasChart chart;
    
    public PolygonSelector(PolygonPanel pp){
        polygonPanel = pp;
        seed = new Random();   
    }
  
    @Override
	public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        Polygon[] polys = polygonPanel.polygons;
        for(int j = 0; j < polys.length; j++)
            if(polys[j].contains(p))
            {
                polygonPanel.colors[j] = getColor();
                polygonPanel.repaint();
        		GraficasChart chart = new GraficasChart("Tiempo (días)","Nivel","Poligono: " + j,"Ejemplo Grafica Lineal");
        		chart.addSerie("serie1");
        		chart.addSerie("serie2");
        		chart.addPuntos();
//        		chart.genera();
        		chart.setVisible(true);
                break;
            }
    }
  
    private Color getColor(){
        return new Color(seed.nextInt(0xffffff));
    }
}