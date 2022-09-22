package vn.com.telsoft.controller;

import org.fluttercode.datafactory.impl.DataFactory;
import vn.com.telsoft.entity.Employee;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class EmployeeBean implements Serializable {
    private List<String> selectedColumns = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();
    private Map<String, String> columnMap = new LinkedHashMap<>();

    @PostConstruct
    private void postConstruct() {
        initColumnProperties();
        initEmployeeList();
    }

    private void initColumnProperties() {
        addColumn("id", "ID");
        addColumn("name", "Name");
        addColumn("phoneNumber", "Phone Number");
        addColumn("address", "Address");
        selectedColumns.addAll(columnMap.keySet());
    }

    private void addColumn(String propertyName, String displayName) {
        columnMap.put(propertyName, displayName);
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

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }
}