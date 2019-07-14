package app.prepoznavanjeGovora;

import java.util.ArrayList;

public class Training 
{
	private ArrayList<Vector> sequence;
	private int[] markers;
	
	public Training() 
	{
		sequence = new ArrayList<>();
	}
	
	public void initializeMarkers(int stateCount)
	{
		markers = new int[stateCount-1];
		int n = sequence.size() / stateCount;
		int p = sequence.size() % stateCount;
		int valBefore = 0;
		for(int i = 0 ; i < stateCount - 1 ; i++)
		{
			markers[i] = valBefore + n;
			if(p > 0)
			{
				markers[i]++;
				p--;
			}
			valBefore = markers[i];
		}
	}
	
	public ArrayList<Vector> getList(int index)
	{
		ArrayList<Vector> list = new ArrayList<>();
		int startIndex 	= 0;
		int endIndex 	= markers[0];
		if(index == markers.length)
		{
			startIndex = markers[markers.length-1];
			endIndex = sequence.size();
		}
		else if(index > 0)
		{
			startIndex 	= markers[index - 1];
			endIndex 	= markers[index];
		}
		
		for(int i = startIndex ; i < endIndex ; i++)
			list.add(sequence.get(i));
		return list;
	}
	
	public void add(Vector v)
	{
		sequence.add(v);
	}
	
	public void add(ArrayList<Vector> vectors)
	{
		sequence.addAll(vectors);
	}
	
	public void moveMarker(int markerIndex, int newPlace)
	{
		markers[markerIndex] = newPlace;
	}
	
	public ArrayList<Vector> getSequence() 
	{
		return sequence;
	}
	
	@Override
	public String toString() 
	{
		String buffer = "Training[" + sequence.size() + "]: ";
		for(Vector v : sequence)
			buffer += v;
		return buffer;
	}
}
