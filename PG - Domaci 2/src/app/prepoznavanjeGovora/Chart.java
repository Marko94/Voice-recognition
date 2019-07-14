package app.prepoznavanjeGovora;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
public class Chart extends ApplicationFrame
	{
	private XYSeries original = new XYSeries("Original");
	private XYSeries trainingSet = new XYSeries("Training Set");
	final XYSeriesCollection dataset = new XYSeriesCollection( );
	public Chart()
		{
		 super("Prepoznavanje govora - Chart");
		 setPreferredSize(new java.awt.Dimension(560, 367));
		 }
	 
	 public void plot(Vector v, String name){
//		 System.out.println("Vektor: " + name + " " + v.getValue(0) + " " +  v.getValue(1));
		 if(name.equals("Original"))
				original.add(v.getValue(0), v.getValue(1));
		 else
				trainingSet.add(v.getValue(0), v.getValue(1));
		 
		 
	 }
	 public void finishPlot(String name){
		 dataset.addSeries( trainingSet );
		 dataset.addSeries( original );
		 JFreeChart xylineChart = ChartFactory.createXYLineChart(
				 "Rec: " + name ,
				 "Y" ,
				 "X" ,
				 dataset ,
				 PlotOrientation.VERTICAL ,
				 true , true , false
				 );
				 ChartPanel chartPanel = new ChartPanel( xylineChart );
				 chartPanel.setPreferredSize( new java.awt.Dimension(560, 367));
				 final XYPlot plot = xylineChart.getXYPlot( );
				 XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
				 renderer.setSeriesPaint( 0 , Color.RED );
				 renderer.setSeriesPaint( 1 , Color.BLUE );
				 renderer.setSeriesPaint( 2 , new Color(0, 153, 51) );
				 renderer.setSeriesStroke( 0 , new BasicStroke( 0.3f ) );
				 renderer.setSeriesStroke( 1 , new BasicStroke( 0.3f ) );
				 renderer.setSeriesStroke( 2 , new BasicStroke( 0.3f ) );
				 plot.setRenderer( renderer );
				 setContentPane( chartPanel );
	 }
	}