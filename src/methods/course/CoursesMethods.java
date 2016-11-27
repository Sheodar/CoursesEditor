package methods.course;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static methods.other.Patterns.checkIDforValid;
import static methods.dataBase.ConnectionDB.*;

public class CoursesMethods {
    public void createCourse() throws SQLException {
        Scanner printOpt = new Scanner(System.in);
        System.out.println("Введите название курса:");
        String courseName = printOpt.nextLine();
        System.out.println("Введите описание курса:");
        String specification = printOpt.nextLine();
        System.out.println("Введите год начала курсов:");
        String dateYearStart = printOpt.nextLine();
        System.out.println("Введите месяц начала курсов:");
        String dateMonthStart = printOpt.nextLine();
        System.out.println("Введите день начала курсов:");
        String dateDayStart = printOpt.nextLine();
        System.out.println("Введите год окончания курсов:");
        String dateYearFinish = printOpt.nextLine();
        System.out.println("Введите месяц окончания курсов:");
        String dateMonthFinish = printOpt.nextLine();
        System.out.println("Введите день окончания курсов:");
        String dateDayFinish = printOpt.nextLine();

        stmt.execute("INSERT INTO Course (courseName, specification, dateDayStart,dateMonthStart,dateYearStart," +
                "dateDayFinish, dateMonthFinish,dateYearFinish) VALUES (" + "'" + courseName + "','" + specification + "','" + dateDayStart + "','"
                + dateMonthStart + "','" + dateYearStart + "','" + dateDayFinish + "','" + dateMonthFinish + "','" + dateYearFinish + "')");
        StmtDisconnect();
        stmt.execute("SELECT * FROM Course WHERE courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int index = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();
        System.out.println("ID курса: " + index);
        dayOfWeekCourseMethod(index);
    } // OK!


    public void printSoloCourse(String ID) throws SQLException {
        stmt.execute("SELECT * FROM Course");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Еще не создано курсов. Пожалуйста, создайте курс.");
        } else {
            if (checkIDforValid(ID)) {
                Integer id = new Integer(ID);
                stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
                res = stmt.getResultSet();
                int maxCourse = res.getInt("courseId");
                StmtDisconnect();
                ResDisconnect();

                if (id > maxCourse | id < 0) {
                    System.out.println("[Invalid command]");
                } else {
                    stmt.execute("SELECT * FROM Course WHERE courseId = " + id);
                    res = stmt.getResultSet();
                    int Id = res.getInt("courseId");
                    String name = res.getString("courseName");
                    String specification = res.getString("specification");
                    String dateDayStart = res.getString("dateDayStart");
                    String dateMonthStart = res.getString("dateMonthStart");
                    String dateYearStart = res.getString("dateYearStart");
                    String dateDayFinish = res.getString("dateDayFinish");
                    String dateMonthFinish = res.getString("dateMonthFinish");
                    String dateYearFinish = res.getString("dateYearFinish");
                    int quantityStudents = res.getInt("quantityStudents");
                    StmtDisconnect();
                    ResDisconnect();
                    System.out.println("ID: [" + Id + "]; " + "Количество студентов: [" + quantityStudents + "];" +
                            "\nНазвание: " + name + ";" +
                            "\nОписание: " + specification +
                            "\nДата начала: " + dateDayStart + "." + dateMonthStart + "." + dateYearStart + "." +
                            "\nДата окончания: " + dateDayFinish + "." + dateMonthFinish + "." + dateYearFinish + ".");
                    System.out.println("\tТренеры курса: ");
                    stmt.execute("SELECT Trainer.*, TrainerCourse.* FROM Trainer INNER JOIN TrainerCourse  ON Trainer.trainerId = TrainerCourse.idTrainer AND TrainerCourse.idCourse = " + id);
                    res = stmt.getResultSet();

                    while (res.next()) {
                        String trainerName = res.getString("trainerName");
                        System.out.println("\t\t ‣ " + trainerName + ".");
                    }
                    StmtDisconnect();
                    ResDisconnect();
                }
            } else {
                System.out.println("[Invalid command]");
            }
        }
    } //OK!

    public void changeCourse(Integer ID) throws SQLException {
        Scanner printOpt = new Scanner(System.in);
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();
        if (ID > maxCourse | ID < 0) {
            System.out.println("ОШИБКА! Еще не создано курсов. Пожалуйста, создайте курс.");
        } else {
            String changeCourse;
            do {
                System.out.println("[1]- Изменить дату; [2]- Изменить название; [3]- Изменить описание; [4]- Назад;");
                changeCourse = printOpt.nextLine();
                switch (changeCourse) {
                    case "1":
                        String changeCourseDate;
                        do {
                            System.out.println("[1]- Изменить начало; [2]- Изменить конец; [3]- Изменить дни недели; [4]- Назад;");
                            changeCourseDate = printOpt.nextLine();
                            switch (changeCourseDate) {
                                case "1":
                                    System.out.println("Введите год начала курсов:");
                                    String newStartYear = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateYearStart = '" + newStartYear + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Введите месяц начала курсов:");
                                    String newStartMouth = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateMonthStart = '" + newStartMouth + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Введите год начала курсов:");
                                    String newStartDay = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateDayStart = '" + newStartDay + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    break;
                                case "2":
                                    System.out.println("Введите год начала курсов:");
                                    String newFinishYear = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateYearFinish = '" + newFinishYear + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Введите месяц начала курсов:");
                                    String newFinishMouth = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateMonthFinish = '" + newFinishMouth + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Введите год начала курсов:");
                                    String newFinishDay = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateDayFinish = '" + newFinishDay + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    break;
                                case "3":
                                    stmt.execute("SELECT * FROM    CourseWeekDay");
                                    res = stmt.getResultSet();
                                    while (res.next()) {
                                        stmt.execute("DELETE FROM CourseWeekDay WHERE idCourse = " + ID);
                                    }
                                    StmtDisconnect();
                                    ResDisconnect();
                                    dayOfWeekCourseMethod(ID);
                                    break;
                                default:
                                    if (!(Objects.equals(changeCourseDate, "4"))) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                            }
                        } while (!(Objects.equals(changeCourseDate, "4")));
                        break;
                    case "2":
                        System.out.println("Введите название курса:");
                        String newName = printOpt.nextLine();
                        stmt.execute("UPDATE Course SET courseName = '" + newName + "' WHERE courseId = " + ID);
                        StmtDisconnect();
                        break;
                    case "3":
                        System.out.println("Введите писание курса:");
                        String newSpecification = printOpt.nextLine();
                        stmt.execute("UPDATE Course SET specification = '" + newSpecification + "' WHERE courseId = " + ID);
                        StmtDisconnect();
                        break;
                    default:
                        if (!(Objects.equals(changeCourse, "4"))) {
                            System.out.println("[Invalid command]");
                        }
                        break;
                }
            } while (!(Objects.equals(changeCourse, "4")));
        }
    }

    public void printAllCourse() throws SQLException {
        stmt.execute("SELECT * FROM Course");
        res = stmt.getResultSet();

        while (res.next()) {
            int id = res.getInt("courseId");
            String name = res.getString("courseName");
            System.out.println("‣ ID [" + id + "]; Название: " + name + ".");
        }

        StmtDisconnect();
        ResDisconnect();
    } // OK!

    public void printStudentsInCourse(String id) throws SQLException {
        int list = 1;
        stmt.execute("SELECT * FROM Course");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Еще не создано курсов. Пожалуйста, создайте курс.");
        } else {
            try {
                if (checkIDforValid(id)) {
                    Integer idCourse = new Integer(id);
                    StmtDisconnect();
                    ResDisconnect();
                    try {
                        stmt.execute("SELECT * FROM Course WHERE courseId = " + idCourse);
                        res = stmt.getResultSet();
                        String courseName = res.getString("courseName");
                        System.out.println("______________________________\nСтудент(ы), учащиися на курсе [" + courseName + "]: ");

                        stmt.execute("SELECT Student.*, StudentCourse.* FROM Student INNER JOIN StudentCourse  ON StudentCourse.idCourse = " + idCourse + " AND Student.studentId = StudentCourse.idStudent");
                        res = stmt.getResultSet();
                        while (res.next()) {
                            String nameStudent = res.getString("studentName");
                            String surnameStudent = res.getString("studentSurname");
                            System.out.println("\t\t " + list + ") Имя: " + nameStudent + " Фамилия: " + surnameStudent + ".");
                            list++;
                        }
                        StmtDisconnect();
                        ResDisconnect();
                    } catch (SQLException e) {
                        System.out.println("[Invalid command]");
                    }
                } else {
                    System.out.println("[Invalid command]");
                }


            } catch (Exception q) {
                System.out.println("[Invalid command]");
            }

        }
    } // OK!

    private void dayOfWeekCourseMethod(int index) throws SQLException {
        Scanner printOpt = new Scanner(System.in);
        metka:
        while (true) {
            System.out.println("Задайте день недели для курса");
            String dayOfWeek = printOpt.nextLine();
            stmt.execute("INSERT INTO CourseWeekDay (idCourse, day) VALUES(" + index + ",'" + dayOfWeek + "')");
            while (true) {
                System.out.println("Еще день?[y/n]");
                String addDayOfWeekWork = printOpt.nextLine();
                switch (addDayOfWeekWork) {
                    case "y":
                        System.out.println("Задайте день недели для курса");
                        String addDayOfWeek = printOpt.nextLine();
                        stmt.execute("INSERT INTO CourseWeekDay (idCourse, day) VALUES(" + index + ",'" + addDayOfWeek + "')");
                        break;
                    case "n":
                        break metka;
                    default:
                        System.out.println("[Invalid command]");
                        break;
                }
            }
        }
    }  // OK!


}
