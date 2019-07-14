package app.prepoznavanjeGovora;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class Vector 
{
	public static int 	size;
	
	private double[] 	values;
	
	public Vector(double[] values) 
	{
		this.values = (double[])values.clone();
	}
	
	public Vector(String values)
	{
		this.values = new double[size];
		int index 	= 0;
		for(String s : values.split(","))
			this.values[index++] = Double.parseDouble(s);
	}
	
	public double getValue(int index)
	{
		if(index >= size)
			return 0;
		return values[index];
	}
	 
	public Vector getSample(double variance)
	{
		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(values, generateCovarianceMatrix(variance));
		return new Vector(distribution.sample());
	}
	
	public ArrayList<Vector> getSamples(double variance, int amount)
	{
		MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(values, generateCovarianceMatrix(variance));
		ArrayList<Vector> list = new ArrayList<>();
		for(int i = 0 ; i < amount ; i++)
			list.add(new Vector(distribution.sample()));
		return list;
	}
	
	private double[][] generateCovarianceMatrix(double variance)
	{
		double[][] covarianceMatrix = new double[size][size];
		for(int i = 0 ; i < size ; i++)
			for(int j = 0 ; j < size ; j++)
				if(i == j)
					covarianceMatrix[i][j] = variance;
				else
					covarianceMatrix[i][j] = 0;
		return covarianceMatrix;
	}
	
	@Override
	public String toString() 
	{
		String buffer = "{";
		for(int i = 0 ; i < values.length ; i++)
			if(i < values.length - 1)
				buffer += values[i] + ", ";
			else
				buffer += values[i] + "}";
		return buffer;
	}
}
