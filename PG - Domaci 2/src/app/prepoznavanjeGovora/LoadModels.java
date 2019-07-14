package app.prepoznavanjeGovora;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoadModels 
{
	private ArrayList<Model> models;
	private int count;
	
	public LoadModels() {}
	
	// loaduje modele iz xml file-a
	public void loadModels()
	{
		if(models == null)
			models = new ArrayList<>();
		models.clear();
		try 
		{	
			File ifile = new File("words.xml");
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(ifile);
			document.getDocumentElement().normalize();
			
			Element elementData = document.getDocumentElement();
			
			Node vectorLengthNode = elementData.getElementsByTagName("vectorDimensions").item(0);
			Vector.size = Integer.parseInt(vectorLengthNode.getFirstChild().getTextContent());
			Node modelsNode = elementData.getElementsByTagName("models").item(0);
			count = Integer.parseInt(modelsNode.getAttributes().getNamedItem("count").getNodeValue());
			
			NodeList modelList = elementData.getElementsByTagName("model");
			
			for(int i = 0 ; i < modelList.getLength() ; i++)
			{
				Node modelNode = modelList.item(i);
				
				String name = modelNode.getAttributes().getNamedItem("name").getNodeValue();
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
				models.add(new Model(name, stateCount, vectorList, variance, chart));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Model> getModels() 
	{
		return models;
	}
	
	@Override
	public String toString() 
	{
		String buffer = "Loading model - " + count + "\n";
		for(Model m : models)
			buffer += m + "\n";
		return buffer;
	}
}