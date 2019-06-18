package SystemViewer.SystemViewerMain;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {   
        JSONParser jsonParser = new JSONParser();
        try {        	 

        	String filePath = args[0];
        	//String filePath = "SystemViewController.json";
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
            SystemViewer systemViewer = new SystemViewer(jsonObject);
            Scanner scanner = new Scanner(System.in);
            String search = "";
            
            while(!search.equals("exit")) {
            	search = scanner.nextLine();
            	systemViewer.performAndPrintSearch(search);
            }

            
 

 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
