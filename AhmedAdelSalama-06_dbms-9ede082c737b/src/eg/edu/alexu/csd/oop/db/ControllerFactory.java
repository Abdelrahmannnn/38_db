package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;

public class ControllerFactory {





        private String[] command;


        private  DBMS database;
        private Table table;


        public void addCommand(String command) {
            if(command.endsWith(";")) {
                command = command.substring(0,command.length()-1);

                this.command =command.split("[ ]+");
                switch (this.command[0].toLowerCase()) {
                    case "create":
                        if (this.command[1].toLowerCase().equals("database")&&this.command.length==3) {
                            database = new DBMS(this.command[2]);
                            try {
                                database.createDatabase(this.command[2] , database.executeStructureQuery(this.command[2]));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        } else if (this.command[1].toLowerCase().equals("table")) {
                            String columns = command.substring(command.indexOf("("), command.indexOf(")"));
                            table = new Table(database.getDatabaseName(), this.command[2], columns);
                            try {
                                table.createTable(table.executeStructureQuery(database.getDatabaseName()+"\\"+this.command[2]+".xml"));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "drop":
                        if (this.command[1].toLowerCase().equals("database")&&this.command.length==3) {
                            try {
                                database.dropDatabase(this.command[2] , database.executeStructureQuery(this.command[2]));
                                System.out.println("database deleted");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        } else if (this.command[1].toLowerCase().equals("table")&&this.command.length==3) {
                            try {
                                table.dropTable(table.executeStructureQuery(database.getDatabaseName()+"\\"+this.command[2]+".xml"));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case "select":
                        String word = "from" ;

                        if(this.command[1].equals("*")&&this.command.length==4&&this.command[2].toLowerCase().equals("from")) {
                            String tablename = this.command[3] ;
                            try {
                                database.executeQuery(command) ;
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        } else if(!this.command[1].equals("*")&&!this.command[1].toLowerCase().equals("from")&&command.toLowerCase().contains(word.toLowerCase())){
                            //command+=";";
                           // command=command.substring(command.indexOf(" ")+1) ;
                            try {
                                database.executeQuery(command) ;
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        }else{
                            try {
                                database.executeQuery(command) ;
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        break ;
                    case "insert":


                        command+=";";
                        if (this.command[1].toLowerCase().equals("into")) {

                            String columns = command.substring(command.indexOf("("), command.indexOf(")"));

                            String values = command.substring(command.indexOf(")"), command.indexOf(";"));
                            if((values.length()==1)) {
                                values = columns;
                                columns=null;
                            }
                            else {
                                values = values.substring(values.indexOf("("));}

                            String filename1=null;

                            if (columns!=null){
                                String[] filename = this.command[2].split("[( , )]+");
                                filename1=filename[0];
                            }
                            else{
                                String[] filename = this.command[2].split("[ v]+");
                                filename1=filename[0];
                            }
                            table.insert_to_table( columns, values, filename1);






//                        System.out.println(columns);
//                        System.out.println(column[2]);
//                        System.out.println(value[2]);

                        }
                        break;
                    case "delete":
                    case "update":

                        break ;







                    default:System.out.println("Syntax Error");
                }
            }
        }
    }


