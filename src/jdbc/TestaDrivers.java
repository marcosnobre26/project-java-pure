package jdbc;

import java.sql.DriverManager;

public class TestaDrivers {
    public static void main(String[] args) {
        System.out.println("Drivers disponíveis:");
        java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            System.out.println(drivers.nextElement().getClass().getName());
        }
    }
}
