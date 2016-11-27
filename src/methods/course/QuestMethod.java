package methods.course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static methods.other.Patterns.checkIDforValid;
import static methods.other.Randomaizer.random;
import static methods.dataBase.ConnectionDB.*;

public class QuestMethod {
    public static void createQuest(Integer idCourse) throws SQLException {
        Scanner printOpt = new Scanner(System.in);
        System.out.println("Enter Question: ");
        String question = printOpt.nextLine();

        stmt.execute("INSERT INTO Question (theQuest) VALUES (" + "'" + question + "')");
        StmtDisconnect();

        stmt.execute("SELECT * FROM Question WHERE  questionId = (SELECT MAX(questionId)  FROM Question);");
        res = stmt.getResultSet();
        int thisQuestion = res.getInt("questionId");
        StmtDisconnect();
        ResDisconnect();
        stmt.execute("INSERT INTO QuestionCourse (idQuestion, idCourse) VALUES (" + thisQuestion + "," + idCourse + ")");
        StmtDisconnect();

        ArrayList<Integer> idStudentForQuestion = new ArrayList<>();
        stmt.execute("SELECT Course.*, StudentCourse.* FROM Course INNER JOIN StudentCourse  ON Course.courseId = StudentCourse.idCourse AND Course.courseId = " + idCourse);
        res = stmt.getResultSet();

        while (res.next()) {
            int id = res.getInt("idStudent");
            idStudentForQuestion.add(id);

        }
        StmtDisconnect();
        ResDisconnect();
        for (Integer anIdStudentForQuestion : idStudentForQuestion) {
            int id = anIdStudentForQuestion;
            stmt.execute("INSERT INTO StudentQuestion (idStudent, idQuestion, studentEvalation) VALUES(" + id + "," + thisQuestion + "," + random() + ")");
        }
        ResDisconnect();
        StmtDisconnect();
        idStudentForQuestion.clear();
    } //OK !

    public static void evaluationStudentForCourse(Integer idStud, Integer idCourse) throws SQLException {
        stmt.execute("SELECT * FROM Student");
        res = stmt.getResultSet();
        if (!res.next()) {
            System.out.println("Not created students. Please, create student.");
        } else {
            stmt.execute("SELECT * FROM  QuestionCourse WHERE idCourse = " + idCourse);
            res = stmt.getResultSet();
            ArrayList<Integer> enableQuestions = new ArrayList<>();

            while (res.next()) {
                int idQuestion = res.getInt("idQuestion");
                enableQuestions.add(idQuestion);
            }
            StmtDisconnect();
            ResDisconnect();
            double sum = 0;
            int denominator = 0;
            for (Integer enableQuestion : enableQuestions) {
                stmt.execute("SELECT Student.*, StudentQuestion.* FROM Student INNER JOIN StudentQuestion  ON Student.studentId = StudentQuestion.idStudent  AND StudentQuestion.idQuestion =  " + enableQuestion + " AND Student.studentId = " + idStud);
                res = stmt.getResultSet();
                while (res.next()) {
                    int evalation = res.getInt("studentEvalation");
                    sum = sum + evalation;
                    denominator++;
                }
                StmtDisconnect();
                ResDisconnect();
            }
            if (denominator == 0) {
                System.out.println("Student recent enrolled on a course.");
            } else {
                String formSum = String.format("%.2f", +sum / denominator);
                System.out.println(" GPA: " + formSum + "%" + "(" + denominator + " questions)");
                enableQuestions.clear();
            }
        }
        StmtDisconnect();
        ResDisconnect();
    } // OK!

    public void printJournal(String id) throws SQLException {
        int list = 1;
        try {
            stmt.execute("SELECT * FROM Course");
            res = stmt.getResultSet();
            if (!res.next()) {
                System.out.println("Not created courses. Please, create course.");
            } else {
                if (checkIDforValid(id)) {
                    Integer idCourse = new Integer(id);
                    stmt.execute("SELECT * FROM    Course  WHERE  courseId = (SELECT MAX(courseId)  FROM Course);");
                    res = stmt.getResultSet();
                    int maxCourse = res.getInt("courseId");
                    StmtDisconnect();
                    ResDisconnect();
                    if (maxCourse >= idCourse) {
                        try {
                            stmt.execute("SELECT * FROM  QuestionCourse WHERE idCourse = " + idCourse);
                            res = stmt.getResultSet();
                            ArrayList<Integer> enableQuestions = new ArrayList<>();

                            while (res.next()) {
                                int idQuestion = res.getInt("idQuestion");
                                enableQuestions.add(idQuestion);
                            }
                            StmtDisconnect();
                            ResDisconnect();

                            for (Integer enableQuestion : enableQuestions) {
                                stmt.execute("SELECT * FROM  Question WHERE questionId = " + enableQuestion);
                                res = stmt.getResultSet();
                                String q = res.getString("theQuest");
                                System.out.println("Question " + list + ": " + q);
                                list++;
                                StmtDisconnect();
                                ResDisconnect();
                                stmt.execute("SELECT Student.*, StudentQuestion.* FROM Student INNER JOIN StudentQuestion  ON Student.studentId = StudentQuestion.idStudent AND StudentQuestion.idQuestion =  " + enableQuestion);
                                res = stmt.getResultSet();

                                while (res.next()) {
                                    String studentName = res.getString("studentName");
                                    int evalation = res.getInt("studentEvalation");
                                    System.out.println("\t\t" + studentName + ": " + evalation + "%");
                                }
                            }
                            enableQuestions.clear();
                        } catch (SQLException e) {
                            System.out.println("[Invalid command]");
                        }
                    } else {
                        System.out.println("[Invalid command]");
                    }
                } else {
                    System.out.println("[Invalid command]");
                }
            }
        } catch (SQLException e) {
            System.out.println("[Invalid command]");
        }
    } // OK !
}
