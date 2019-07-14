package app.prepoznavanjeGovora;

import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class Model 
{
	private String name;
	private int	stateCount;
	private ArrayList<State> states;	
	private ArrayList<Vector> givenMeans;
	private double givenVariance;
	private ArrayList<Training> trainingSet;
	private int	sequenceCount = 3;
	private int	generatedCount = 3;
	private	Random rGen;
	private Training trainingSeq;
	private Chart chart;
	final XYSeriesCollection dataset = new XYSeriesCollection( );
	
	// generisanje modela
	public Model(String name, int stateCount, ArrayList<Vector> givenMeans, double givenVariance, Chart chart) 
	{
		super();
		this.name = name;
		this.chart = chart;
		this.stateCount = stateCount;
		this.givenMeans = givenMeans;
		this.givenVariance = givenVariance;
		states = new ArrayList<>();
		trainingSet = new ArrayList<>();
		rGen = new Random(System.currentTimeMillis());
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
//		System.out.println("Name: " + name);
		generateTrainingSet();
	}

	// ucitavanje postojeceg modela
	public Model(String name, int stateCount, ArrayList<State> states) 
	{
		super();
		this.name = name;
		this.stateCount = stateCount;
		this.states = states;
		givenMeans = null;
		givenVariance = 0;
		trainingSet = new ArrayList<>();
		rGen = new Random(System.currentTimeMillis());
	}
	
	public void generateTrainingSet()
	{
		for(int i = 0 ; i < sequenceCount ; i++)
		{
			trainingSeq = new Training();
			for(Vector v : givenMeans)
			{
				ArrayList<Vector> generated = v.getSamples(givenVariance, generatedCount);
				int rLength = rGen.nextInt(generatedCount - 1) + 1;
				int sIndex = rLength == generatedCount ? 0 : rGen.nextInt(generatedCount - rLength);
				for(int j = sIndex ; j < rLength + sIndex ; j++)
					trainingSeq.add(generated.get(j));
			}
			trainingSeq.initializeMarkers(stateCount);
			trainingSet.add(trainingSeq);
		}
		plotModel();
	}
	
	@Override
	public String toString() 
	{
		String buffer = "Model : " + name + "\n";
		buffer += " + StateCount : " + stateCount + "\n";
		
		if(givenMeans != null)
		{
			buffer += " + Given means:\n";
			for(Vector v : givenMeans)
				buffer += "\t:" + v + "\n";
		}
		else
		{
			buffer += " + States:\n";
			for(State s : states)
				buffer += "\t:" + s + "\n";
		}
		
		return buffer;
	}	
	public void plotModel()
	{
		if(Vector.size == 2 && givenMeans != null)
		{
			
			for(Vector v : givenMeans)
				chart.plot(v, "Original");
			
			for(Training t : trainingSet)
				for(Vector v : t.getSequence())
					chart.plot(v, "Training set");
			
			chart.finishPlot(name);
		}
	}
}
