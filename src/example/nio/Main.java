package example.nio;

import example.nio.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try(factory){
            saveEmployee(factory, new Employee("Roman", "Wozniak", "Mafia"));

            System.out.println(getEmployeeById(factory,4));

//            deleteEmpoyeeById(factory,1);

            List<Employee> employeeList = getEmployeesFromCompany(factory, "Eniro");

            employeeList.forEach(System.out::println);

            List<Employee> employeesToSave = new ArrayList<>();
            employeesToSave.add(new Employee("Jon", "Doe", "ISPOT"));
            employeesToSave.add(new Employee("Zbigniew", "Nowak", "Oszusci"));
            employeesToSave.add(new Employee("Vito", "Corleone", "Mafia"));
            saveEmployees(factory, employeesToSave);

            List<Employee> employees = getAllEmployees(factory);
            employees.forEach(System.out::println);


        }

    }

    public static List<Employee> getAllEmployees(SessionFactory factory){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List<Employee> employeesAll = session
                .createQuery("from Employee ", Employee.class)
                .getResultList();
        session.getTransaction().commit();
        return employeesAll;
    }

    public static void saveEmployees(SessionFactory factory, List<Employee> employeeList){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        employeeList.forEach(session::save);
        session.getTransaction().commit();
    }

    public static void saveEmployee(SessionFactory factory, Employee employee){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
        System.out.println("\nEmployee: " + employee + " saved!\n");
    }

    public static Employee getEmployeeById(SessionFactory factory, int id){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Employee employee = session.get(Employee.class, id);
        session.getTransaction().commit();
        return employee;
    }

    public static List<Employee> getEmployeesFromCompany(SessionFactory factory, String company){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List<Employee> employeesInCompany = session
                .createQuery("from Employee  e where e.company='" + company +"'", Employee.class)
                .getResultList();
        session.getTransaction().commit();
        return employeesInCompany;
    }

    public static void deleteEmpoyeeById(SessionFactory factory, int id){
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete from Employee where id=" + id);
        session.getTransaction().commit();
        System.out.println("\nEmployee id: " + id + " deleted!");
    }
}
