package eg.edu.alexu.csd.oop.db;

public class Main {
    public static void main(String[] args){
        ControllerFactory a = new ControllerFactory();


        // a.addCommand("create database abdo;");
        // a.addCommand("create table ieee (  name string ,weight int , age int);");
        a.addCommand("create database ahmed;");
        a.addCommand("create table ieee (name string ,weight int , age int);");
        String command="insert into ieee (name,weight,age)"
                + "values (eslam ,10,20);";
        String command1="insert into ieee"
                + "values ( moh11,30,20);";
        a.addCommand(command);
        a.addCommand(command1);
        // a.addCommand("SELECT name, weight" +
        // "from ieee;");
        a.addCommand("SELECT * FROM ieee;");
        a.addCommand("SELECT * FROM ieee where name = eslam;");
       // a.addCommand("SELECT name, age FROM ieee;");
       // a.addCommand("select name from ieee;");
       // a.addCommand("select name, weight, age from ieee;");
        //  a.addCommand("UPDATE Customers\n SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'\n WHERE CustomerID = 1;");










//        a.addCommand("drop table ieee;");
//        a.addCommand("drop database abdo;");

    }
}