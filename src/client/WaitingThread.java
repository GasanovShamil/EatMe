package client;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitingThread extends Thread{
	JPanel pane;
	Dimension dim;
	public WaitingThread(JPanel pane, Dimension dim){
		this.pane=pane;
		this.dim=dim;
	}
	
	public void run(){
		pane.setPreferredSize(dim);

		ImageIcon image = new ImageIcon("img/loading.gif");
		JLabel jlImage = new JLabel(image);
		pane.add(jlImage);
	}
}
