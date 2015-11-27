import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.*;
import java.util.Map.Entry; 

public class CSE535Assignment {

	static HashMap<String,LinkedList> postings_mapT = new HashMap();
	static HashMap<String,LinkedList> postings_mapD= new HashMap();
	static String outputName;
	static PrintWriter writer;
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		outputName = args[1];
		int topKValue = Integer.parseInt(args[2]);
		String inputQueryFile = args[3];
		writer = new PrintWriter(outputName, "UTF-8");
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		
		try{	
			while ((line=br.readLine()) != null) {
				String arr[]=line.split("\\\\");
				LinkedList<PostingNode> lList = new LinkedList<PostingNode>();

				//Get rid of the delimiter
				if(arr[2].contains("m")){
					arr[2] = arr[2].replace("m", "");
				}

				//Split the product ids into individual ids
				String prodIds[]=arr[2].split(", ");
				for(int i=0;i<prodIds.length;i++){
					//Get rid of the starting or ending 
					// [ ] if needed
					if(prodIds[i].contains("[")){
						prodIds[i]= prodIds[i].replace("[","");
					}
					if(prodIds[i].contains("]")){
						prodIds[i]= prodIds[i].replace("]","");
					}
					
					if(prodIds[i].contains("/")){
						String[] nodeArray = prodIds[i].split("/");
						lList.add(new PostingNode(nodeArray[0], nodeArray[1]));
					}
					
				}

				postings_mapT.put(arr[0],lList);  

			}
			writer.write("FUNCTION: getTopK "+ topKValue );
			writer.println();;
			writer.write("Result: ");
			getTopK(topKValue);
			
			//TODO - Read row from file to get all query terms
			
			File file = new File(inputQueryFile);
			BufferedReader br1 = new BufferedReader(new FileReader(file));
			String linea = null;
			while( (linea = br1.readLine())!= null ){
			        // \\s+ means any number of whitespaces between tokens
			    String [] tokens = linea.split("\\s+");
			    for(int i =0 ;i< tokens.length;i++){
			    	getPostings(tokens[i]);
			    }
			}		
			
		}
		finally {
			br.close();
			writer.close();
		}
	}
	private static Map<String, LinkedList> sortHashMapDescending(Map<String, LinkedList> unsortMap)
	{
		List<Entry<String, LinkedList>> list = new LinkedList<Entry<String, LinkedList>>(unsortMap.entrySet());

		// Sorting the list based on linked list number of elements
		Collections.sort(list, new Comparator<Entry<String, LinkedList>>()
		{
			public int compare(Entry<String, LinkedList> o1,
					Entry<String, LinkedList> o2)
			{
				return ((Integer) o2.getValue().size()).compareTo(o1.getValue().size());
			}
		});

		Map<String, LinkedList> sortedHashMap = new LinkedHashMap<String, LinkedList>();
		for (Entry<String, LinkedList> entry : list)
		{
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}

		return sortedHashMap;
	}

	
	public static void getTopK(int i) throws IOException{
		// Function to get top K largest 
		// TODO - For now passing input from main, eventually move to args
		
		Map<String, LinkedList> sortedMapDesc = sortHashMapDescending(postings_mapT);
		int count =0 ;
		String content="";
		for (Entry<String, LinkedList> entry : sortedMapDesc.entrySet())
		{
			System.out.print(entry.getKey());
			content+=(entry.getKey());
			count++;
			if(count == i){
				break;
			}
			System.out.print(", ");
			content+=(", ");
			
		}
		writer.write(content);
	}
	
	public static void getPostings(String query_term){
		//get document id in increasing order of doc Id
		LinkedList<PostingNode> sortedDocList = postings_mapT.get(query_term);
		LinkedList<PostingNode> sortedFreqList = null; 
		if(sortedDocList != null){
			sortedFreqList = (LinkedList<PostingNode>) sortedDocList.clone();
			Collections.sort(sortedDocList, new Comparator<PostingNode>() {
	
				@Override
				public int compare(PostingNode o1, PostingNode o2) {
					return Collator.getInstance().compare(o1.docId, o2.docId);
				}
				
			});
			System.out.print("DocList:");
			writer.println();
			writer.write("FUNCTION: getPostings "+ query_term);
			writer.println();
			
			writer.write("Ordered by doc IDs: ");
			for (PostingNode node : sortedDocList) {
				System.out.print(node.docId + ",");
				writer.write(node.docId + ",");
			}
			Collections.sort(sortedFreqList, new Comparator<PostingNode>() {
				
				@Override
				public int compare(PostingNode o1, PostingNode o2) {
					return Collator.getInstance().compare(o2.termFrequency, o1.termFrequency);
				}
				
			});
			
			System.out.println();
			System.out.print("Term Frequency:");
			writer.println();
			writer.write("Ordered by TF: ");
			for (PostingNode node : sortedFreqList) {
				System.out.print(node.docId + ",");
				writer.write(node.docId + ",");
			}
		}else{
			System.out.println("term not found");
		}
		
		
		System.out.println();
	}

	//TODO- Remove this function, not needed.
	public static void printMap(Map<String, LinkedList> sortedMapDesc) throws IOException
	{
		FileWriter fstream = new FileWriter("out.txt");
		BufferedWriter out = new BufferedWriter(fstream);

		for (Entry<String, LinkedList> entry : sortedMapDesc.entrySet())
		{
			out.write("Key : " + entry.getKey() + " Value : "+ entry.getValue());			
		}
		out.close();
	}

}
