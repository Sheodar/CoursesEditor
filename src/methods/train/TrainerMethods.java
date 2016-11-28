package methods.train;

import java.sql.SQLException;
import java.util.Scanner;

import static methods.other.Patterns.checkIDforValid;
import static methods.dataBase.ConnectionDB.*;
import static methods.dataBase.ConnectionDB.StmtDisconnect;
import static methods.dataBase.ConnectionDB.stmt;

public class TrainerMethods {
    public void createTrainer() throws SQLException {
        stmt.execute("SELECT * FROM Course");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created courses. Please, create course.");
        } else {
            stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
            res = stmt.getResultSet();
            int maxCourse = res.getInt("courseId");
            StmtDisconnect();
            ResDisconnect();
            Scanner printOpt = new Scanner(System.in);
            System.out.println("Enter trainer name:");
            String trainerName = printOpt.nextLine();
            System.out.println("Enter trainer surname:");
            String trainerSurname = printOpt.nextLine();
            stmt.execute("INSERT INTO Trainer (trainerName, trainerSurname) VALUES (" +
                    "'" + trainerName + "','" + trainerSurname + "')");
            StmtDisconnect();
            stmt.execute("SELECT * FROM Trainer WHERE  trainerId = (SELECT MAX(trainerId)  FROM Trainer);");
            res = stmt.getResultSet();
            int thisTrainer = res.getInt("trainerId");
            StmtDisconnect();
            ResDisconnect();
            metka:
            while (true) {
                System.out.println("Enter ID course, on which will teach trainer:");
                String addTrainerID = printOpt.nextLine();
                if (checkIDforValid(addTrainerID)) {
                    Integer addTrainerIDint = new Integer(addTrainerID);
                    if (addTrainerIDint > maxCourse) {
                        System.out.println("This course not found.");
                        break;
                    }
                    if (addTrainerIDint <= maxCourse & addTrainerIDint > 0) {
                        stmt.execute("INSERT INTO TrainerCourse (idTrainer, idCourse) VALUES (" + "" + thisTrainer + "," + addTrainerIDint + ")");
                        StmtDisconnect();
                    }
                    while (true) {
                        System.out.println("More? [y/n]");
                        String addTrainerCourse = printOpt.nextLine();
                        switch (addTrainerCourse) {
                            case "n":
                                break metka;
                            case "y":
                                while (true) {
                                    System.out.println("Enter ID course, on which will teach trainer:");
                                    String addCourseTrainer = printOpt.nextLine();
                                    if (checkIDforValid(addCourseTrainer)) {
                                        Integer addCourseTrainerIDint = new Integer(addCourseTrainer);
                                        if (addCourseTrainerIDint >= maxCourse) {
                                            System.out.println("[Invalid command]");
                                            break;
                                        }
                                        if (addCourseTrainerIDint <= maxCourse & addCourseTrainerIDint > 0) {
                                            stmt.execute("INSERT INTO TrainerCourse (idTrainer, idCourse) VALUES (" + "" + thisTrainer + "," + addCourseTrainerIDint + ")");
                                            StmtDisconnect();
                                            break;
                                        }
                                    }
                                }
                        }
                    }
                } else {
                    System.out.println("[Invalid command]");
                }
            }
        }
    } //OK!

    public void printSoloTrainer(String ID) throws SQLException {
        stmt.execute("SELECT * FROM Trainer");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created trainers. Please, create trainer.");
        } else {
            try {
                if (checkIDforValid(ID)) {
                    Integer id = new Integer(ID);
                    stmt.execute("SELECT * FROM    Trainer  WHERE  trainerId = (SELECT MAX(trainerId)  FROM Trainer);");
                    res = stmt.getResultSet();
                    int maxTrainer = res.getInt("trainerId");
                    StmtDisconnect();
                    ResDisconnect();

                    if (id > maxTrainer | id < 0) {
                        System.out.println("[Invalid command]");
                    } else {
                        stmt.execute("SELECT * FROM Trainer WHERE trainerId = " + id);
                        res = stmt.getResultSet();
                        int Id = res.getInt("trainerId");
                        String name = res.getString("trainerName");
                        String surname = res.getString("trainerSurname");
                        StmtDisconnect();
                        ResDisconnect();
                        System.out.println("ID: [" + Id + "]; Name: " + name + ", Surname: " + surname);
                        System.out.println("\tCourse, which teaches: ");
                        stmt.execute("SELECT Course.*, TrainerCourse.* FROM Course INNER JOIN TrainerCourse  ON Course.courseId = TrainerCourse.idCourse AND  TrainerCourse.idTrainer = " + id);
                        res = stmt.getResultSet();

                        while (res.next()) {
                            String nameCourse = res.getString("courseName");
                            String idCourse = res.getString("courseId");
                            System.out.println("\t\t ‣ Name: " + nameCourse + ". ID ["+idCourse+"]");
                        }
                        StmtDisconnect();
                        ResDisconnect();

                    }
                } else {
                    System.out.println("[Invalid command]");
                }
            } catch (SQLException q) {
                System.out.println("[Invalid command]");
            }
        }


    } //OK!

    public void changeTrainer(String ID) throws SQLException {
        stmt.execute("SELECT * FROM Trainer");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created trainers. Please, create trainer.");
        } else {
            Scanner printOpt = new Scanner(System.in);
            if (checkIDforValid(ID)) {
                Integer id = new Integer(ID);
                stmt.execute("SELECT * FROM    Trainer  WHERE  trainerId = (SELECT MAX(trainerId)  FROM Trainer);");
                res = stmt.getResultSet();
                int maxTrainer = res.getInt("trainerId");
                StmtDisconnect();
                ResDisconnect();

                if (id > maxTrainer | id < 0) {
                    System.out.println("[Invalid command]");
                } else {
                    System.out.println("[1]- Change courses; [2]- Change name; [3]- Change surname [4]- Back;");
                    metka:
                    while (true) {
                        String outConsoleCatOpt = printOpt.nextLine();
                        switch (outConsoleCatOpt) {
                            case "1":
                                System.out.println("[1]- Delete course; [2]- Add course; [3]- Back;");
                                metka1:
                                while (true) {
                                    String changeTrainersCourse = printOpt.nextLine();
                                    switch (changeTrainersCourse) {
                                        case "1":
                                            delTrainerCourse(id);
                                            break metka;
                                        case "2":
                                            addTrainerCourse(id);
                                            break metka;
                                        case "3":
                                            break metka1;
                                    }
                                }
                                break;
                            case "2":
                                System.out.println("Enter new name: ");
                                String newName = printOpt.nextLine();
                                stmt.execute("UPDATE Trainer SET trainerName = '" + newName + "' WHERE trainerId = " + id);
                                StmtDisconnect();
                                break metka;
                            case "3":
                                System.out.println("Enter new surname: ");
                                String newSurname = printOpt.nextLine();
                                stmt.execute("UPDATE Trainer SET trainerSurname = '" + newSurname + "' WHERE trainerId = " + id);
                                StmtDisconnect();
                                break metka;
                            case "4":
                                break metka;
                            default:
                                System.out.println("[Invalid command]");
                                break;
                        }
                    }
                }
            } else {
                System.out.println("[Invalid command]");
            }
        }
    } //OK!

    public void printAllTrainer() throws SQLException {
        int list = 1;
        stmt.execute("SELECT * FROM Trainer");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created trainers. Please, create trainer.");
        } else {
            stmt.execute("SELECT * FROM Trainer");
            res = stmt.getResultSet();
            while (res.next()) {
                int id = res.getInt("trainerId");
                String name = res.getString("trainerName");
                String surname = res.getString("trainerSurname");
                System.out.println("\t" + list + ") ID [" + id + "]; " + "Name: " + name + ", Surname: " + surname + ".");
                list++;
            }
        }
    } // OK!

    private void addTrainerCourse(Integer id) throws SQLException {
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();

        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter ID course, on which will teach trainer:");
        String addTrainerID = printOpt.nextLine();
        if (checkIDforValid(addTrainerID)) {
            Integer addTrainerIDint = new Integer(addTrainerID);
            if (addTrainerIDint <= maxCourse) {
                try {
                        if (addTrainerIDint <= maxCourse & addTrainerIDint > 0) {
                            stmt.execute("INSERT INTO TrainerCourse (idTrainer, idCourse) VALUES (" + "" + id + "," + addTrainerIDint + ")");
                            StmtDisconnect();
                        }
                } catch (SQLException e) {
                    System.out.println("[Invalid command]");
                }
            }
        } else {
            System.out.println("[Invalid command]");
        }

    } //OK!

    private void delTrainerCourse(Integer id) throws SQLException {
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();

        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter ID course, which the want to delete: ");
        String delTrainerID = printOpt.nextLine();
        if (checkIDforValid(delTrainerID)) {
            Integer delTrainerIDint = new Integer(delTrainerID);
            if (delTrainerIDint <= maxCourse) {
                try {
                    stmt.execute("SELECT * FROM  TrainerCourse  WHERE   idCourse = " + delTrainerIDint + " AND idTrainer = " + id);
                    res = stmt.getResultSet();
                    StmtDisconnect();
                    ResDisconnect();

                    if (delTrainerIDint <= maxCourse & delTrainerIDint > 0) {
                        stmt.execute("DELETE FROM TrainerCourse WHERE idCourse = " + delTrainerIDint);
                        StmtDisconnect();
                    }
                } catch (SQLException w) {
                    System.out.println("[Invalid command]");
                }
            } else {
                System.out.println("[Invalid command]");
            }
        } else {
            System.out.println("[Invalid command]");
        }
    } //OK!

}
