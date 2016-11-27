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
            System.out.println("Еще не создано курсов. Пожалуйста, создайте курс.");
        } else {
            stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
            res = stmt.getResultSet();
            int maxCourse = res.getInt("courseId");
            StmtDisconnect();
            ResDisconnect();
            Scanner printOpt = new Scanner(System.in);
            System.out.println("Введите имя тренера:");
            String trainerName = printOpt.nextLine();
            System.out.println("Введите фамилию тренера:");
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
                System.out.println("Введите ID курса, который будет преподавать тренер:");
                String addTrainerID = printOpt.nextLine();
                if (checkIDforValid(addTrainerID)) {
                    Integer addTrainerIDint = new Integer(addTrainerID);
                    if (addTrainerIDint > maxCourse) {
                        System.out.println("Такого курса не найдено");
                        break;
                    }
                    if (addTrainerIDint <= maxCourse & addTrainerIDint > 0) {
                        stmt.execute("INSERT INTO TrainerCourse (idTrainer, idCourse) VALUES (" + "" + thisTrainer + "," + addTrainerIDint + ")");
                        StmtDisconnect();
                    }
                    while (true) {
                        System.out.println("Еще какие-то курсы? [y/n]");
                        String addTrainerCourse = printOpt.nextLine();
                        switch (addTrainerCourse) {
                            case "n":
                                break metka;
                            case "y":
                                while (true) {
                                    System.out.println("Введите ID курса, на который поступает студент:");
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
            System.out.println("Еще не создано Тренеров. Пожалуйста, создайте тренера.");
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
                        System.out.println("ID: [" + Id + "]; Имя: " + name + ", Фамилия: " + surname);
                        System.out.println("\tКурсы, которые преподает: ");
                        stmt.execute("SELECT Course.*, TrainerCourse.* FROM Course INNER JOIN TrainerCourse  ON Course.courseId = TrainerCourse.idCourse AND  TrainerCourse.idTrainer = " + id);
                        res = stmt.getResultSet();

                        while (res.next()) {
                            String nameCourse = res.getString("courseName");
                            System.out.println("\t\t ‣ Название: " + nameCourse + ".");
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
            System.out.println("Еще не создано Тренеров. Пожалуйста, создайте тренера.");
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
                    System.out.println("[1]- Изменить курсы; [2]- Изменить имя; [3]- Изменить фамилию [4]- Назад;");
                    metka:
                    while (true) {
                        String outConsoleCatOpt = printOpt.nextLine();
                        switch (outConsoleCatOpt) {
                            case "1":
                                System.out.println("[1]- Удалить курс; [2]- Добавить курс; [3]- Назад;");
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
                                System.out.println("Введите новое имя");
                                String newName = printOpt.nextLine();
                                stmt.execute("UPDATE Trainer SET trainerName = '" + newName + "' WHERE trainerId = " + id);
                                StmtDisconnect();
                                break metka;
                            case "3":
                                System.out.println("Введите новую фамилию");
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
            System.out.println("Еще не создано Тренеров. Пожалуйста, создайте студента.");
        } else {
            stmt.execute("SELECT * FROM Trainer");
            res = stmt.getResultSet();
            while (res.next()) {
                int id = res.getInt("trainerId");
                String name = res.getString("trainerName");
                String surname = res.getString("trainerSurname");
                System.out.println("\t" + list + ") ID [" + id + "]; " + "Имя: " + name + ", Фамилия: " + surname + ".");
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
        while (true) {
            System.out.println("Введите ID курса, который будет преподавать тренер:");
            String addTrainerID = printOpt.nextLine();
            if (checkIDforValid(addTrainerID)) {
                Integer addTrainerIDint = new Integer(addTrainerID);
                if (addTrainerIDint <= maxCourse) {
                    System.out.println("Такого курса не найдено");
                    break;
                }
                if (addTrainerIDint <= maxCourse & addTrainerIDint > 0) {
                    stmt.execute("INSERT INTO TrainerCourse (idTrainer, idCourse) VALUES (" + "" + id + "," + addTrainerIDint + ")");
                    StmtDisconnect();
                    break;
                }
            } else {
                System.out.println("[Invalid command]");
            }
        }
    } //OK!

    private void delTrainerCourse(Integer id) throws SQLException {
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();

        Scanner printOpt = new Scanner(System.in);
        while (true) {
            System.out.println("Введите ID курса, который хотите удалить:");
            String addTrainerID = printOpt.nextLine();
            if (checkIDforValid(addTrainerID)) {
                Integer addTrainerIDint = new Integer(addTrainerID);
                if (addTrainerIDint <= maxCourse) {
                    System.out.println("Такого курса не найдено");
                    break;
                }
                if (addTrainerIDint <= maxCourse & addTrainerIDint > 0) {
                    stmt.execute("DELETE FROM TrainerCourse WHERE idCourse = " + addTrainerIDint + "AND idTrainer = " + id);
                    StmtDisconnect();
                    break;
                }
            } else {
                System.out.println("[Invalid command]");
            }
        }
    } //OK!

}
