package SystemViewer.SystemViewerMain;

import SystemViewer.SystemViewerMain.SystemViewer.SearchTypes;

public class SearchGroup {
	private SearchTypes searchType;
	private String value;
	
	public SearchGroup (SearchTypes searchType, String value){
		this.searchType = searchType;
		this.value = value;
	}

	public SearchTypes getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchTypes searchType) {
		this.searchType = searchType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
