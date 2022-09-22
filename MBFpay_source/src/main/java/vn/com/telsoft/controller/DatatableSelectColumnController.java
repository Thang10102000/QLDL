package vn.com.telsoft.controller;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.FilterEvent;
import vn.com.telsoft.entity.Employee;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class DatatableSelectColumnController implements Serializable {
    private List<Employee> employeeList = new ArrayList<>();
    private String selectedOption;
    private List<String> filterValues = new ArrayList<String>();

    @PostConstruct
    private void postConstruct() {
        initEmployeeList();
    }

    private void initEmployeeList() {
        DataFactory dataFactory = new DataFactory();
        for (int i = 1; i < 20; i++) {
            Employee employee = new Employee();
            employee.setId(i);
            employee.setName(dataFactory.getName());
            employee.setPhoneNumber(String.format("%s-%s-%s", dataFactory.getNumberText(3),
                    dataFactory.getNumberText(3),
                    dataFactory.getNumberText(4)));
            employee.setAddress(dataFactory.getAddress() + "," + dataFactory.getCity());
            employeeList.add(employee);
        }
    }

    public void valueChange() {
        try {
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance()
                    .getViewRoot().findComponent(":form_main:table");
            List<UIColumn> column = dataTable.getColumns();
            for (UIColumn uiColumn : column) {
                System.out.println(uiColumn.getHeaderText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("filterValues: " + filterValues);
        System.out.println("selectedOption: " + selectedOption);

    }

    public void listenFilter(FilterEvent event) {
        // update datasource
        Map<String, Object> tempString = event.getFilters();

        System.out.println("size filter: " + tempString.size());
        for (String key : tempString.keySet()) {
            System.out.println("key: " + key + " \t values: "
                    + tempString.get(key));
        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public List<String> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(List<String> filterValues) {
        this.filterValues = filterValues;
    }
}
