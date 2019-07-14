package app.prepoznavanjeGovora;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

public class Main {
	
	public static void main(String[] args) {
//		Chart chart = new Chart("Prepoznavanje Govora - Chart" , "Chart");
//		 chart.pack( );
//		 RefineryUtilities.centerFrameOnScreen( chart );
//		 chart.setVisible( true );
		 LoadModels c = new LoadModels();
			c.loadModels();
//		 EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						MainWindow window = new MainWindow(c);
//						RefineryUtilities.centerFrameOnScreen(window.frame);
//						window.frame.setVisible(true);
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});	 
	}
}
