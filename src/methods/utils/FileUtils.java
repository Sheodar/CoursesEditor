package methods.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static methods.dataBase.ConnectionDB.*;
import static methods.dataBase.ConnectionDB.res;
import static methods.dataBase.ConnectionDB.stmt;
import static methods.utils.Patterns.checkIDforValid;

public class FileUtils {
    public void saveJournalCourse(String id) throws SQLException {
        try {
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
                        if (idCourse <= maxCourse | idCourse > 0) {

                            stmt.execute("SELECT * FROM  QuestionCourse WHERE idCourse = " + idCourse);
                            res = stmt.getResultSet();
                            ArrayList<Integer> enableQuestions = new ArrayList<>();
                            while (res.next()) {
                                int idQuestion = res.getInt("idQuestion");
                                enableQuestions.add(idQuestion);
                            }
                            StmtDisconnect();
                            ResDisconnect();
                            stmt.execute("SELECT * FROM  Course WHERE courseId = " + idCourse);
                            res = stmt.getResultSet();
                            String nameCourse = res.getString("courseName");
                            String courseName = nameCourse + ": ";
                            String date = new java.util.Date().toString();
                            String[] dateMassive;
                            dateMassive = date.trim().split("[ :]");
                            String fileName = nameCourse + " " + dateMassive[7] + "_" + dateMassive[2] + dateMassive[1] + "_" + dateMassive[3] + "_" + dateMassive[4] + "_" + dateMassive[5] + ".txt";

                            File file = new File("Archive/" + fileName);

                            try {
                                boolean created = file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            FileWriter writer = new FileWriter("Archive/" + fileName, false);
                            writer.write(courseName);
                            writer.append("\n");
                            StmtDisconnect();
                            ResDisconnect();

                            for (Integer enableQuestion : enableQuestions) {
                                stmt.execute("SELECT * FROM  Question WHERE questionId = " + enableQuestion);
                                res = stmt.getResultSet();
                                String theQuest = res.getString("theQuest");
                                String str1 = "Question " + list + ": " + theQuest;
                                writer.write(str1);
                                list++;
                                StmtDisconnect();
                                ResDisconnect();
                                stmt.execute("SELECT Student.*, StudentQuestion.* FROM Student INNER JOIN StudentQuestion  ON Student.studentId = StudentQuestion.idStudent AND StudentQuestion.idQuestion =  " + enableQuestion);
                                res = stmt.getResultSet();

                                while (res.next()) {
                                    writer.append("\n");
                                    String studentName = res.getString("studentName");
                                    int evalation = res.getInt("studentEvalation");
                                    String str2 = "\t" + studentName + ": " + evalation + "%";
                                    writer.write(str2);
                                }
                                writer.append("\n");

                            }
                            writer.append("\n");
                            writer.write("Date: " + dateMassive[2] + " " + dateMassive[1] + "." + dateMassive[7] + ", " + dateMassive[3] + "." + dateMassive[4] + "." + dateMassive[5]);
                            writer.flush();
                            enableQuestions.clear();
                        } else {
                            System.out.println("[Invalid command]");
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("[Invalid command]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}