package app.prepoznavanjeGovora;

import java.util.Vector;

public class State 
{
	private double stayProbability;
	private double moveProbability;
	private Vector mean;
	private Vector standardDeviation;
	
	public double getStayProbability() 
	{
		return stayProbability;
	}
	
	public void setStayProbability(double stayProbability) 
	{
		this.stayProbability = stayProbability;
	}
	
	public double getMoveProbability() 
	{
		return moveProbability;
	}
	
	public void setMoveProbability(double moveProbability) 
	{
		this.moveProbability = moveProbability;
	}
	
	public Vector getMean() 
	{
		return mean;
	}
	
	public void setMean(Vector mean) 
	{
		this.mean = mean;
	}
	
	public Vector getStandardDeviation() 
	{
		return standardDeviation;
	}
	
	public void setStandardDeviation(Vector standardDeviation) 
	{
		this.standardDeviation = standardDeviation;
	}
	
	
}
