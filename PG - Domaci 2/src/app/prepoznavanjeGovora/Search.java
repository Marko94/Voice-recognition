package app.prepoznavanjeGovora;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Search {
	private ArrayList<Vector> searchVectors;
	private int count;
	private int sequenceCount = 3;
	private int	generatedCount = 3;
	private	Random rGen;
	private Training trainingSeq;
	private ArrayList<Training> trainingSet;
	private ArrayList<Vector> tempVectors;
	private Vector[][] searchPoints;
	
	public Search() {}
	
	// loaduje modele iz xml file-a
	public void search()
	{
		trainingSet = new ArrayList<>();
		if(searchVectors == null)
			searchVectors = new ArrayList<>();
		searchVectors.clear();
		try 
		{	
			File ifile = new File("search.xml");
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(ifile);
			document.getDocumentElement().normalize();
			
			Element elementData = document.getDocumentElement();
			
			Node vectorLengthNode = elementData.getElementsByTagName("vectorDimensions").item(0);
			Vector.size = Integer.parseInt(vectorLengthNode.getFirstChild().getTextContent());
			Node modelsNode = elementData.getElementsByTagName("searchVectors").item(0);
			count = Integer.parseInt(modelsNode.getAttributes().getNamedItem("count").getNodeValue());
			
			NodeList modelList = elementData.getElementsByTagName("searchVector");
			
			rGen = new Random(System.currentTimeMillis());
			
			for(int i = 0 ; i < modelList.getLength() ; i++)
			{
				Node modelNode = modelList.item(i);
				
				int stateCount = Integer.parseInt(modelNode.getAttributes().getNamedItem("stateCount").getNodeValue());
				double variance = 0;
				ArrayList<Vector> vectorList = new ArrayList<>();
				Chart chart = new Chart();
				
				NodeList modelChilds = modelNode.getChildNodes();
				for(int j = 0 ; j < modelChilds.getLength() ; j++)
				{
					if(modelChilds.item(j).getNodeType() == Node.ELEMENT_NODE)
					{
						Element element = (Element)modelChilds.item(j);
						if(element.getTagName() == "vector")
							vectorList.add(new Vector(element.getFirstChild().getTextContent()));
						else if(element.getTagName() == "variance")
							variance = Double.parseDouble(element.getFirstChild().getTextContent());
					}
				}
				
				startSearch(vectorList, variance, stateCount);
				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Vector> getSearchVectors(){
		return searchVectors;
	}
	
	@Override
	public String toString() 
	{
		String buffer = "Loading search model" + count + "\n";
		for(Vector v : searchVectors)
			buffer += v + "\n";
		return buffer;
	}
	
	public void startSearch(ArrayList<Vector> vectorList, double variance, int stateCount){
		searchVectors = new ArrayList<>(vectorList);
//		ArrayList<Training> temp = generateTrainingSet(searchVectors, variance, stateCount);
		for(Vector v : searchVectors)
				tempVectors.add(v.getSample(variance));
//		for(int i = 0; i <searchVectors.size(); i++)
//			for(int j = 0; i <searchVectors.size(); i++)
//				searchPoints[i][j]= 0;
		
	}
	
	public ArrayList<Training> generateTrainingSet(ArrayList<Vector> searchVectors, double variance, int stateCount)
	{
		for(int i = 0 ; i < sequenceCount ; i++)
		{
			trainingSeq = new Training();
			for(Vector v : searchVectors)
			{
				ArrayList<Vector> generated = v.getSamples(variance, generatedCount);
				int rLength = rGen.nextInt(generatedCount - 1) + 1;
				int sIndex = rLength == generatedCount ? 0 : rGen.nextInt(generatedCount - rLength);
				for(int j = sIndex ; j < rLength + sIndex ; j++)
					trainingSeq.add(generated.get(j));
			}
			trainingSeq.initializeMarkers(stateCount);
			trainingSet.add(trainingSeq);
		}
		return(trainingSet);
	}
}
