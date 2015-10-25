package data.scheedule.nuold;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class Semester extends SemesterBase {
	
	private Map<String, Department> _departments;
	
	@Override public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }
	private Config getConfig() { return (Config) getBaseConfig(); }

	@Override
	public DepartmentBase findDepartment(String code) {
		return _departments.get(code);
	}

	
	public Semester(Config config) {
		super(config);
		
		loadDepartments();
	}
	
	private void loadDepartments() {
		_departments = new HashMap<String, Department>();

		getConfig().setSearchAction("CLASS_SRCH_WRK2_SSR_PB_SUBJ_SRCH");
		_searchPage = getConfig().submitSearchForm();
		HtmlForm form = _searchPage.getFormByName("win0");
		_searchAction = _searchPage.getElementByName("ICAction");
		_searchSubmit = (HtmlButton) _searchPage.createElement("button");
		_searchSubmit.setAttribute("type", "submit");
		form.appendChild(_searchSubmit);
		
		char letter = 'A';
		while (letter <= 'Z') {
			loadDepartmentPage(Character.toString(letter++));
		}
		
		letter = '1';
		while (letter <= '9') {
			loadDepartmentPage(Character.toString(letter++));
		}
	}
	
	private HtmlPage _searchPage;
	private HtmlElement _searchAction;
	private HtmlButton _searchSubmit;
	
	private void setSearchAction(String action) {
		_searchAction.setAttribute("value", action);
	}
	
	private HtmlPage submitSearchForm() {
		try {
			return _searchSubmit.click();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void loadDepartmentPage(String letter) {
		setSearchAction(String.format("SSR_CLSRCH_WRK2_SSR_ALPHANUM_%s", letter));
		HtmlPage page = submitSearchForm();
		parseDepartmentPage(page);
	}
	
	private void parseDepartmentPage(HtmlPage page) {
		HtmlTable table = (HtmlTable) page.getElementById("ACE_SSR_CLSRCH_SUBJ$0");
		
		if (table != null) {
			int index = 0;
			for (HtmlTableRow row : table.getRows()) {
				if ((++index % 3) == 0) {
					int cellCount = row.getCells().size();
					if (cellCount > 2) {
						String departmentCode = StringUtils.clean(row.getCell(cellCount - 2).asText());
						String departmentName = StringUtils.clean(row.getCell(cellCount - 1).asText());
						Department dept = new Department(getConfig(), departmentCode, departmentName);
						_departments.put(dept.getDepartmentCode(), dept);
					}
				}
			}
		}
	}
}
