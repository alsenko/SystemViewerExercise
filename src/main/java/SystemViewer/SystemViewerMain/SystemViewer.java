package SystemViewer.SystemViewerMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SystemViewer {
	
	public enum SearchTypes{
		VIEW_CLASS,
		CLASS_NAME,
		ID
		
		
	}
	
	private JSONObject primaryJSON;
	
	private List<List<SearchGroup>> searchGroups;

	public SystemViewer(JSONObject primaryJSON){
		
		this.primaryJSON = primaryJSON;
			
	}


    
    //Search Group is (Type of Search, String) 
    // Types of Search are ViewClasses, ClassNames, and Identifiers
    // Inner list is compound selector, and condition between the search types
    // Outer list is a nested list for selector chain
	public void performAndPrintSearch(String search) {
		searchGroups = getNestedSearchGroups(search);
		searchJson(primaryJSON, 0);
				
			
		}
		
	private void searchJson(JSONObject jsonObject, Integer index) {
		if (index >= searchGroups.size()){
			System.out.println(jsonObject.toJSONString());
			return;
		}
		int matches = 0;
		List<SearchGroup> nestedSearch = searchGroups.get(index);
		for (String key : (Set<String>) jsonObject.keySet()){
        	Object val = jsonObject.get(key);    	
        	if (val instanceof String){
        		for (SearchGroup search : nestedSearch){
        			if (search.getSearchType() == SearchTypes.VIEW_CLASS ){
        				if (key.equals("class")){
        					if (search.getValue().equals((String) val)){
        						matches++;
        					}
        				}
        			}else if (search.getSearchType() == SearchTypes.ID ){
        				if (key.equals("identifier")){
        					if (search.getValue().equals((String) val)){
        						matches++;
        					}
        				}
        			}
        			
        		}
        	}else if (val instanceof JSONArray){
        				if (key.equals("classNames")){
        	        		matches =+ matchClassNames(val,nestedSearch);
        				}else {
        					for (Object obj : (JSONArray) val){
        						if (obj instanceof JSONObject){
        							searchJson((JSONObject) obj, index);
        						}
        					}
        				}
        	}else if (val instanceof JSONObject){
        		searchJson((JSONObject) val, index);
        	}
	}
		if (matches >= nestedSearch.size()){
			if (index > searchGroups.size()-1){
				System.out.println(jsonObject.toJSONString());
				return;
			}
			searchJson(jsonObject, ++index);
		}

	}

	private int matchClassNames(Object vals, List<SearchGroup> nestedSearch) {
		JSONArray valList = (JSONArray) vals;
		Set<String> classNames = new HashSet<String>();
		int matches = 0;
		for (Object val : valList){
			if (val instanceof String){
				classNames.add((String) val);
			}
		}
		for (SearchGroup search : nestedSearch){
			if (search.getSearchType() == SearchTypes.CLASS_NAME ){
				if (classNames.contains(search.getValue()))
					matches++;
			}
		}
		return matches;
	}

	private List<List<SearchGroup>> getNestedSearchGroups(String search) {
		List<List<SearchGroup>> searchGroups = new ArrayList<List<SearchGroup>>();
		for (String innerSearch : search.split(" ")){
			List<SearchGroup> innerSearchGroups = extractSearchTypes(innerSearch);
			searchGroups.add(innerSearchGroups);			
		}
		return searchGroups;
		
	}

	private List<SearchGroup> extractSearchTypes(String innerSearch) {
		List<SearchGroup> innerSearchGroups = new ArrayList<SearchGroup>();
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0; i<innerSearch.length(); i++){
			if (!(innerSearch.charAt(i) == '#' || innerSearch.charAt(i) == '.') || i == 0){
			tempBuffer.append(innerSearch.charAt(i));
			}else {
				innerSearchGroups.add(createSearchGroup(tempBuffer.toString()));
				tempBuffer.setLength(0);
				tempBuffer.append(innerSearch.charAt(i));

			}
		}
		innerSearchGroups.add(createSearchGroup(tempBuffer.toString()));
		return innerSearchGroups;
	}

	private SearchGroup createSearchGroup(String innerSearch) {
		SearchTypes searchType = SearchTypes.VIEW_CLASS;
		switch(innerSearch.charAt(0)){
		
		case '#' : searchType = SearchTypes.ID;
				   innerSearch = innerSearch.substring(1);
					break;
					
		case '.' : searchType = SearchTypes.CLASS_NAME;
					innerSearch = innerSearch.substring(1);
					break;
			
		}
		return new SearchGroup(searchType,innerSearch);
	}

}
