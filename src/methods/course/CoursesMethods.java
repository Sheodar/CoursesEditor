package methods.course;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static methods.other.Patterns.checkIDforValid;
import static methods.dataBase.ConnectionDB.*;

public class CoursesMethods {
    /**
     *  Create new <b>course.</b>
     *  <br><b>Immediately</b> added to the database.
     */
    public void createCourse() throws SQLException {
        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter name course:");
        String courseName = printOpt.nextLine();
        System.out.println("Enter specification course:");
        String specification = printOpt.nextLine();
        System.out.println("Enter year start course:");
        String dateYearStart = printOpt.nextLine();
        System.out.println("Enter month start course:");
        String dateMonthStart = printOpt.nextLine();
        System.out.println("Enter day start course:");
        String dateDayStart = printOpt.nextLine();
        System.out.println("Enter year finish course:");
        String dateYearFinish = printOpt.nextLine();
        System.out.println("Enter month finish course:");
        String dateMonthFinish = printOpt.nextLine();
        System.out.println("Enter day finish course:");
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
        System.out.println("ID course: " + index);
        dayOfWeekCourseMethod(index);
    } // OK!

    /**
     *  Print one course on ID.
     */
    public void printSoloCourse(String ID) throws SQLException {
        stmt.execute("SELECT * FROM Course");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created courses. Please, create course.");
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
                    System.out.println("ID: [" + Id + "]; " + "Quantity students: [" + quantityStudents + "];" +
                            "\nName course: " + name + ";" +
                            "\nSpecification: " + specification +
                            "\nDate start: " + dateDayStart + "." + dateMonthStart + "." + dateYearStart + "." +
                            "\nDate finish: " + dateDayFinish + "." + dateMonthFinish + "." + dateYearFinish + ".");
                    System.out.println("\tCourses trainers: ");
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
            System.out.println("Not created courses. Please, create course.");
        } else {
            String changeCourse;
            do {
                System.out.println("[1]- Change date; [2]- Change name; [3]- Change specification; [4]- Back;");
                changeCourse = printOpt.nextLine();
                switch (changeCourse) {
                    case "1":
                        String changeCourseDate;
                        do {
                            System.out.println("[1]- Change start date; [2]- Change finish date; [3]- Change weekday; [4]- Back;");
                            changeCourseDate = printOpt.nextLine();
                            switch (changeCourseDate) {
                                case "1":
                                    System.out.println("Enter year start course:");
                                    String newStartYear = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateYearStart = '" + newStartYear + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Enter month start course:");
                                    String newStartMouth = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateMonthStart = '" + newStartMouth + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Enter day start course:");
                                    String newStartDay = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateDayStart = '" + newStartDay + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    break;
                                case "2":
                                    System.out.println("Enter year finish course:");
                                    String newFinishYear = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateYearFinish = '" + newFinishYear + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Enter month finish course:");
                                    String newFinishMouth = printOpt.nextLine();
                                    stmt.execute("UPDATE Course SET dateMonthFinish = '" + newFinishMouth + "' WHERE courseId = " + ID);
                                    StmtDisconnect();
                                    System.out.println("Enter day finish course:");
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
                        System.out.println("Enter name course:");
                        String newName = printOpt.nextLine();
                        stmt.execute("UPDATE Course SET courseName = '" + newName + "' WHERE courseId = " + ID);
                        StmtDisconnect();
                        break;
                    case "3":
                        System.out.println("Enter specification course:");
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
            System.out.println("Not created courses. Please, create course.");
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
                        System.out.println("______________________________\nStudents on the course [" + courseName + "]: ");

                        stmt.execute("SELECT Student.*, StudentCourse.* FROM Student INNER JOIN StudentCourse  ON StudentCourse.idCourse = " + idCourse + " AND Student.studentId = StudentCourse.idStudent");
                        res = stmt.getResultSet();
                        while (res.next()) {
                            String nameStudent = res.getString("studentName");
                            String surnameStudent = res.getString("studentSurname");
                            System.out.println("\t\t " + list + ") Name: " + nameStudent + " Surname: " + surnameStudent + ".");
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
            System.out.println("Enter weekday for course");
            String dayOfWeek = printOpt.nextLine();
            stmt.execute("INSERT INTO CourseWeekDay (idCourse, day) VALUES(" + index + ",'" + dayOfWeek + "')");
            while (true) {
                System.out.println("More?[y/n]");
                String addDayOfWeekWork = printOpt.nextLine();
                switch (addDayOfWeekWork) {
                    case "y":
                        System.out.println("Enter weekday for course");
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
