import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Employee {
    int id;
    String name;
    int salary;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && salary == employee.salary && Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary);
    }

    public Employee(int id, String name, int salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}

class Test2{
    public static void main(String[] args) {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1,"Adem",1000));
        employeeList.add(new Employee(2,"Vova",3000));
        employeeList.add(new Employee(3,"Senya",2000));
        employeeList.add(new Employee(4,"Vika",700));
        employeeList.add(new Employee(5,"Jenya",150));
        employeeList.add(new Employee(6,"Vera",8000));

        employeeList.sort((o1,o2)->o1.salary-o2.salary);
        System.out.println(employeeList);
        employeeList.sort((o1,o2)->o2.id-o1.id);
        System.out.println(employeeList);
        Collections.sort(employeeList,(o1,o2)->o2.salary-o1.salary);
        System.out.println(employeeList);
    }
}