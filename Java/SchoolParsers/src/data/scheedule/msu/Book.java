package data.scheedule.msu;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.utils.StringUtils;

public class Book {
	
	private List<HtmlTableRow> _rows;

	private String _title;
	private String _author;
	private String _isbn;
	private String _publisher;
	private boolean _required;
	private String _usedDuring;
	private String _lastUpdated;
	
	
	public Book(List<HtmlTableRow> rows) {
		_rows = rows;
		
		parseBookInfo();
	}
	
	private void parseBookInfo() {
		_title = getCellText(0, 3);
		_author = getCellText(1, 3);
		_isbn = getCellText(2, 3);
		_publisher = getCellText(3, 3);
		_required = getCellText(4, 3).equalsIgnoreCase("yes");
		_usedDuring = getCellText(5, 3);
		_lastUpdated = getCellText(6, 3);
	}
	
	private String getCellText(int row, int cell) {
		return StringUtils.clean(_rows.get(row).getCell(cell).asText());
	}
}
