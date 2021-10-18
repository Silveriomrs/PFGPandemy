/**  
* <p>Title: PanelesDeslizante.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 22 sept. 2021  
* @version 1.0  
*/  
package Pruebas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("javadoc")
public class Test_Paneles_Deslizantes {

   JPanel panel1;
   JPanel panel2;

   public void makeUI() {
      panel1 = new JPanel();
      panel1.setBackground(Color.GRAY);
      panel1.setBounds(0, 0, 400, 400);
      //

      panel2 = new JPanel();
      panel2.setBackground(Color.CYAN);
      panel2.setBounds(400, 0, 400, 400);
      //
      JButton button = new JButton("Click 1");
      button.addActionListener(new BotonDeslizarListener());
      panel1.add(button);
      //
      JButton button2 = new JButton("Click 2");
      button2.addActionListener(new BotonDeslizarListener());
      panel2.add(button2);
      //
      
      JFrame frame = new JFrame("Sliding Panel");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 400);
      frame.setLocationRelativeTo(null);
      frame.setLayout(null);
      frame.add(panel1);
      frame.add(panel2);
      frame.setVisible(true);
   }
   
   
   private class TimerListener implements ActionListener{
	   int direccion;
	   
	   public TimerListener(JPanel jp, int direccion){
		   this.direccion = direccion;
	   }
	   
	   @Override
		public void actionPerformed(ActionEvent e) {
		   //
		   
		   if(direccion == 1) {
			   panel1.setLocation(panel1.getX() - 1, 0);
			   panel2.setLocation(panel2.getX() - 1, 0);
			   if (panel1.getX() + panel1.getWidth() == 0) {
		              ((Timer) e.getSource()).stop();
		              System.out.println("Deslizamiento izquierda terminado.");
		       }
		   }else if(direccion == 0) {
			   panel1.setLocation(panel1.getX() + 1, 0);
			   panel2.setLocation(panel2.getX() + 1, 0);
			   if (panel1.getX()  == 0) {
		              ((Timer) e.getSource()).stop();
		              System.out.println("Deslizamiento derecha terminado en PosX: " + panel1.getX());
		       }
		   }
		     
        }
   }
   
   private class BotonDeslizarListener implements ActionListener{
	   private Timer timer;
	   @Override
	   public void actionPerformed(ActionEvent e) {
	           JButton btn = ((JButton) e.getSource());
	           JPanel jp = getPanel(btn.getText());
	           timer = new Timer(1, new TimerListener(jp,getDireccion(btn.getText())));
	           timer.start();
	           System.out.println(btn.getName());
	   }
	   
	   private JPanel getPanel(String btnName) {
		   JPanel jp = null;
		   switch(btnName) {
		   case "Click 1":
			   jp = panel1;
			   break;
		   case "Click 2":
			   jp = panel2;
		   }
		   System.out.println(btnName);
		   return jp;
	   }
	   
	   private int getDireccion(String btn) {
		   int dir = 0;
		   if(btn.equals("Click 2")) dir = 0;
		   else dir = 1;		   
		   return dir;
	   }
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
    	  @Override
		  public void run() {
    		  new Test_Paneles_Deslizantes().makeUI();
    	  }
      });
   }
}