package methods.student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static methods.utils.Patterns.checkIDforValid;
import static methods.course.QuestMethod.evaluationStudentForCourse;
import static methods.dataBase.ConnectionDB.*;

public class StudentMethods {
    public void createStudent() throws SQLException {
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
            System.out.println("Enter name student:");
            String studentName = printOpt.nextLine();
            System.out.println("Enter surname student:");
            String studentSurname = printOpt.nextLine();
            System.out.println("Enter age student:");
            String studentAge = printOpt.nextLine();
            stmt.execute("INSERT INTO Student (studentName, studentSurname, studentAge) VALUES (" +
                    "'" + studentName + "','" + studentSurname + "','" + studentAge + "')");
            StmtDisconnect();
            stmt.execute("SELECT * FROM Student WHERE  studentId = (SELECT MAX(studentId)  FROM Student);");
            res = stmt.getResultSet();
            int thisStudent = res.getInt("studentId");
            StmtDisconnect();
            ResDisconnect();
            metka:
            while (true) {
                System.out.println("Enter ID course, on which go student:");
                String addStudID = printOpt.nextLine();
                if (checkIDforValid(addStudID)) {
                    Integer addStudIDint = new Integer(addStudID);
                    if (addStudIDint > maxCourse) {
                        System.out.println("This course not found.");
                        break;
                    }
                    stmt.execute("SELECT * FROM    Course  WHERE   courseId = " + addStudIDint);
                    res = stmt.getResultSet();
                    int courseQuantity = res.getInt("quantityStudents");
                    StmtDisconnect();
                    ResDisconnect();
                    if (courseQuantity < 12) {
                        if (addStudIDint <= maxCourse & addStudIDint > 0) {
                            stmt.execute("INSERT INTO StudentCourse (idStudent, idCourse) VALUES (" + "" + thisStudent + "," + addStudIDint + ")");
                            StmtDisconnect();
                            stmt.execute("UPDATE Course SET quantityStudents =" + (courseQuantity + 1) + " WHERE courseId = (" + addStudIDint + ")");
                            StmtDisconnect();
                        }
                    } else {
                        System.out.println("On this course more than 12 students");
                    }
                    while (true) {
                        System.out.println("More? [y/n]");
                        String addStudentCourse = printOpt.nextLine();
                        switch (addStudentCourse) {
                            case "n":
                                break metka;
                            case "y":
                                while (true) {
                                    System.out.println("Enter ID course, on which go student:");
                                    String addCourseStud = printOpt.nextLine();
                                    if (checkIDforValid(addCourseStud)) {
                                        Integer addCourseStuDint = new Integer(addCourseStud);
                                        if (addCourseStuDint >= maxCourse) {
                                            System.out.println("This course not found.");
                                            break;
                                        }
                                        stmt.execute("SELECT * FROM    Course  WHERE   courseId = " + addCourseStuDint);
                                        res = stmt.getResultSet();
                                        int addCourseQuantity = res.getInt("quantityStudents");
                                        StmtDisconnect();
                                        ResDisconnect();

                                        if (addCourseQuantity < 12) {
                                            if (addCourseStuDint <= maxCourse & addCourseStuDint > 0) {
                                                stmt.execute("INSERT INTO StudentCourse (idStudent, idCourse) VALUES (" + "" + thisStudent + "," + addCourseStuDint + ")");
                                                StmtDisconnect();
                                                stmt.execute("UPDATE Course SET quantityStudents =" + (addCourseQuantity + 1) + " WHERE courseId = (" + addCourseStuDint + ")");
                                                StmtDisconnect();
                                                break;
                                            }
                                        } else {
                                            System.out.println("On this course more than 12 students");
                                        }
                                    }
                                }
                        }
                    }
                } else {
                    System.out.println("ID enter incorrectly");
                }
            }
        }
    } //OK!

    public void printSoloStudent(String ID) throws SQLException {
        int list = 1;
        stmt.execute("SELECT * FROM Student");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created students. Please, create student.");
        } else {
            try {
                if (checkIDforValid(ID)) {
                    Integer idStud = new Integer(ID);
                    stmt.execute("SELECT * FROM    Student  WHERE  studentId = (SELECT MAX(studentId)  FROM Student);");
                    res = stmt.getResultSet();
                    int maxStudent = res.getInt("studentId");
                    StmtDisconnect();
                    ResDisconnect();
                    if (idStud > maxStudent | idStud < 0) {
                        System.out.println("[Invalid command]");
                    } else {
                        stmt.execute("SELECT * FROM Student WHERE studentId = " + idStud);
                        res = stmt.getResultSet();
                        String name = res.getString("studentName");
                        String surname = res.getString("studentSurname");
                        String age = res.getString("studentAge");
                        StmtDisconnect();
                        ResDisconnect();
                        System.out.println("ID: [" + idStud + "]; Name: " + name + ", Surname: " + surname + ". Age: " + age + ".");
                        System.out.println("\tCourses, on which the the student learns: ");
                        stmt.execute("SELECT Course.*, StudentCourse.* FROM Course INNER JOIN StudentCourse  ON Course.courseId = StudentCourse.idCourse AND StudentCourse.idStudent = " + idStud);
                        res = stmt.getResultSet();

                        ArrayList<Integer> collectionIdCourse = new ArrayList<>();
                        ArrayList<String> collectionNameCourse = new ArrayList<>();
                        while (res.next()) {
                            int idCourse = res.getInt("idCourse");
                            String nameCourse = res.getString("courseName");
                            collectionNameCourse.add(nameCourse);
                            collectionIdCourse.add(idCourse);
                        }
                        StmtDisconnect();
                        ResDisconnect();
                        for (int z = 0; z < collectionIdCourse.size(); z++) {
                            System.out.print("\t\t " + list + ") Name: " + collectionNameCourse.get(z) + ".");
                            evaluationStudentForCourse(idStud, collectionIdCourse.get(z));
                            list++;
                        }
                        collectionIdCourse.clear();
                        collectionNameCourse.clear();
                    }
                } else {
                    System.out.println("[Invalid command]");
                }
            } catch (SQLException e) {
                System.out.println("[Invalid command]");
            }
        }
    } // OK!

    public void changeStudent(String ID) throws SQLException {
        stmt.execute("SELECT * FROM Student");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created students. Please, create student.");
        } else {
            Scanner printOpt = new Scanner(System.in);
            if (checkIDforValid(ID)) {
                Integer id = new Integer(ID);
                stmt.execute("SELECT * FROM    Student  WHERE  studentId = (SELECT MAX(studentId)  FROM Student);");
                res = stmt.getResultSet();
                int maxStudent = res.getInt("studentId");
                StmtDisconnect();
                ResDisconnect();

                if (id > maxStudent | id < 0) {
                    System.out.println("[Invalid command]");
                } else {
                    String outConsoleCatOpt;
                    do {
                        System.out.println("[1]- Change courses; [2]- Change name; [3]- Change age; [4]- Change surname [5]- Back;");
                        outConsoleCatOpt = printOpt.nextLine();
                        switch (outConsoleCatOpt) {
                            case "1":
                                String changeStudentsCourse;
                                do {
                                    System.out.println("[1]- Delete course; [2]- Add course; [3]- Back;");
                                    changeStudentsCourse = printOpt.nextLine();
                                    switch (changeStudentsCourse) {
                                        case "1":
                                            delStudentCourse(id);
                                            break;
                                        case "2":
                                            addStudentCourse(id);
                                            break;
                                        default:
                                            if (!(Objects.equals(outConsoleCatOpt, "3"))) {
                                                System.out.println("[Invalid command]");
                                            }
                                            break;
                                    }
                                } while (!(Objects.equals(changeStudentsCourse, "3")));
                                break;
                            case "2":
                                System.out.println("Enter new name: ");
                                String newName = printOpt.nextLine();
                                stmt.execute("UPDATE Student SET studentName = '" + newName + "' WHERE studentId = " + id);
                                StmtDisconnect();
                                break;
                            case "3":
                                System.out.println("Enter new age: ");
                                String newAge = printOpt.nextLine();
                                stmt.execute("UPDATE Student SET studentAge = '" + newAge + "' WHERE studentId = " + id);
                                StmtDisconnect();
                                break;
                            case "4":
                                System.out.println("Enter new surname: ");
                                String newSurname = printOpt.nextLine();
                                stmt.execute("UPDATE Student SET studentSurname = '" + newSurname + "' WHERE studentId = " + id);
                                StmtDisconnect();
                                break;
                            default:
                                if (!(Objects.equals(outConsoleCatOpt, "5"))) {
                                    System.out.println("[Invalid command]");
                                }
                                break;
                        }
                    } while (!(Objects.equals(outConsoleCatOpt, "5")));
                }
            } else {
                System.out.println("[Invalid command]");
            }
        }
    } //OK!

    public void printAllStudents() throws SQLException {
        int list = 1;
        stmt.execute("SELECT * FROM Student");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created students. Please, create student.");
        } else {
            stmt.execute("SELECT * FROM Student");
            res = stmt.getResultSet();
            while (res.next()) {
                int id = res.getInt("studentId");
                String name = res.getString("studentName");
                String surname = res.getString("studentSurname");
                String age = res.getString("studentAge");
                System.out.println("\t" + list + ") ID [" + id + "]; " + "Name: " + name + ", Surname: " + surname + ", Age: " + age + ".");
                list++;
            }
        }
    } // OK!

    private void addStudentCourse(Integer id) throws SQLException {
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();

        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter ID course, on which go student:");
        String addStudID = printOpt.nextLine();
        if (checkIDforValid(addStudID)) {
            Integer addStudIDint = new Integer(addStudID);
            if (addStudIDint <= maxCourse) {
                try {
                    stmt.execute("SELECT * FROM    Course  WHERE   courseId = " + addStudIDint);
                    res = stmt.getResultSet();
                    int courseQuantity = res.getInt("quantityStudents");
                    StmtDisconnect();
                    ResDisconnect();
                    if (courseQuantity < 12) {
                        if (addStudIDint <= maxCourse & addStudIDint > 0) {
                            stmt.execute("INSERT INTO StudentCourse (idStudent, idCourse) VALUES (" + "" + id + "," + addStudIDint + ")");
                            StmtDisconnect();
                            stmt.execute("UPDATE Course SET quantityStudents =" + (courseQuantity + 1) + " WHERE courseId = (" + addStudIDint + ")");
                            StmtDisconnect();
                        }
                    } else {
                        System.out.println("On this course more than 12 students");
                    }
                } catch (SQLException e) {
                    System.out.println("[Invalid command]");
                }
            }
        } else {
            System.out.println("[Invalid command]");
        }
    } //OK!

    private void delStudentCourse(Integer id) throws SQLException {
        stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
        res = stmt.getResultSet();
        int maxCourse = res.getInt("courseId");
        StmtDisconnect();
        ResDisconnect();

        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter ID course, which the want to delete: ");
        String addStudID = printOpt.nextLine();
        if (checkIDforValid(addStudID)) {
            Integer addStudIDint = new Integer(addStudID);
            if (addStudIDint <= maxCourse) {
                try {
                    stmt.execute("SELECT * FROM  Course  WHERE   courseId = " + addStudIDint);
                    res = stmt.getResultSet();
                    int courseQuantity = res.getInt("quantityStudents");
                    StmtDisconnect();
                    ResDisconnect();
                    stmt.execute("SELECT * FROM  StudentCourse  WHERE   idCourse = " + addStudIDint + " AND idStudent = " + id);
                    res = stmt.getResultSet();
                    StmtDisconnect();
                    ResDisconnect();
                    if (addStudIDint <= maxCourse & addStudIDint > 0) {
                        stmt.execute("DELETE FROM StudentCourse WHERE idCourse = " + addStudIDint);
                        StmtDisconnect();
                        stmt.execute("UPDATE Course SET quantityStudents =" + (courseQuantity - 1) + " WHERE courseId = (" + addStudIDint + ")");
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
