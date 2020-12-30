import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParseForm10K {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		ParseForm10K parse = new ParseForm10K();
		String actualContent = null;
		String indexBy = "CONSOLIDATED BALANCE SHEETS (USD $)";		
		String fileContent = parse.readFileAsString("./in/EMC-0000790070-13-000019.txt");
		String[] allHtml = fileContent.split("<html>");
		int htmlIndex = 0;
		for(String eachHtml:allHtml){
			if(eachHtml.toLowerCase().indexOf(indexBy.toLowerCase()) > 0){
				actualContent = new String(eachHtml);
				break;
			}
			htmlIndex++;
		}
		
		if(actualContent != null && actualContent.trim().length() > 0){
			//actualContent = actualContent.replaceAll("\\<.*?>","");
			//actualContent = actualContent.replaceAll("^(\\s*\\r\\n){2,}", "\r\n");
			Document doc = Jsoup.parse(actualContent);
			//Elements tables = doc.select("table");
			Element table = doc.select("table").first();
			String[][] trtd2D = null;
			//for (Element table : tables) {
			    Elements trs = table.select("tr");
			    trtd2D = new String[trs.size()][];
			    for (int i = 0; i < trs.size(); i++) {
			    	Elements ths = trs.get(i).select("th");
			    	if(ths.size() > 0){
			    		trtd2D[i] = new String[ths.size()];
				        for (int j = 0; j < ths.size(); j++) {
				        	trtd2D[i][j] = ths.get(j).text(); 
				        }
				        continue;
			    	}   	
			        Elements tds = trs.get(i).select("td");
			        trtd2D[i] = new String[tds.size()];
			        for (int j = 0; j < tds.size(); j++) {
			        	trtd2D[i][j] = tds.get(j).text(); 
			        }
			    }
			    // trtd now contains the desired array for this table
			//}
			for(String[] trtds:trtd2D){
				for(String trtd:trtds){
					//System.out.print(trtd.replaceAll(",", ""));
					System.out.print(trtd);
					System.out.print("\t");
				}
				System.out.println();
			}
			//System.out.println(actualContent);
		}

	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    } catch (IOException ignored) { System.out.println("File not found or invalid path.");}
	    return new String(buffer);
	}

}
