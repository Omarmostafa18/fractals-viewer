import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame  {
	Mypanel panel;
	
	Frame() {
		
		 panel = new Mypanel() {
			 
			 @Override
			 public void mousePressed(MouseEvent e) {
				
			 }
			 
			 @Override
			 public void mouseReleased(MouseEvent e) {
				
			 }
			 
			 @Override
			 public void mouseEntered(MouseEvent e) {
				
			 }
			 
			 @Override
			 public void mouseExited(MouseEvent e) {
				
			 }
		 };
		 //panel.setBackground(Color.darkGray);
		
		this.add(panel);
		this.setTitle("Mandelbrot Viewer");
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	
}

