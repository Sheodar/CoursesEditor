package main;

import  methods.course.CoursesMethods;
import  methods.course.QuestMethod;
import methods.other.WorkingFiles;
import methods.student.StudentMethods;
import methods.train.TrainerMethods;

import java.sql.SQLException;
import java.util.Scanner;

import static methods.other.Helper.helper;
import static methods.other.Patterns.check;
import static methods.other.Patterns.checkIDforValid;
import static methods.dataBase.ConnectionDB.DBConnect;
import static methods.dataBase.ConnectionDB.DBDisconnect;

public class StartAndClosing {
    private static TrainerMethods startTrainer = new TrainerMethods();
    private static StudentMethods startStudent = new StudentMethods();
    private static CoursesMethods startCourse = new CoursesMethods();
    private static QuestMethod startQuest = new QuestMethod();
    private static WorkingFiles startWork = new WorkingFiles();

    private void closeProgram() {
        Scanner printOpt = new Scanner(System.in);
        metka1:
        while (true) {
            System.out.println("Sure? [y/n]");
            String quit = printOpt.nextLine();
            switch (quit) {
                case "y":
                    DBDisconnect();
                    System.exit(0);
                    break;
                case "n":
                    break metka1;
                default:
                    break;
            }
        }
    }

    public void startProgram() throws SQLException {
        DBConnect();
        Scanner commands = new Scanner(System.in);
        System.out.println("Welcome!\n____________________________");
        while (true) {
            System.out.println("Enter the command");
            String star = commands.nextLine();
            String[] test = new String[4];
            if (check(star)) {
                test = star.trim().split(" ");
            }
            try {
                switch (test[0]) {
                    case "exit":
                        try {
                            if (test[1] != null) {
                                System.out.println("[Invalid command]");
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            closeProgram();
                            break;
                        }
                    case "help":
                        try {
                            if (test[1] != null) {
                                System.out.println("[Invalid command]");
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            helper();
                            break;
                        }
                    case "create":
                        try {
                            switch (test[1]) {
                                case "course":
                                    startCourse.createCourse();
                                    break;
                                case "student":
                                    startStudent.createStudent();
                                    break;
                                case "task":
                                    try {
                                        Integer ID = new Integer(test[2]);
                                        QuestMethod.createQuest(ID);
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }

                                    break;
                                case "trainer":
                                    startTrainer.createTrainer();
                                    break;
                                default:
                                    System.out.println("[Invalid command]");
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("[Invalid command]");
                        }
                        break;
                    case "save":
                        switch (test[1]) {
                            case "journal":
                                try {
                                    startWork.Write(test[2]);
                                } catch (Exception e) {
                                    System.out.println("[Invalid command]");
                                }
                                break;
                        }
                        break;
                    case "show":
                        try {
                            switch (test[1]) {
                                case "courses":
                                    try {
                                        if (test[2] != null) {
                                            System.out.println("[Invalid command]");
                                            break;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        startCourse.printAllCourse();
                                        break;
                                    }
                                case "allstudents":
                                    try {
                                        if (test[2] != null) {
                                            System.out.println("[Invalid command]");
                                            break;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        startStudent.printAllStudents();
                                        break;
                                    }
                                case "alltrainers":
                                    try {
                                        if (test[2] != null) {
                                            System.out.println("[Invalid command]");
                                            break;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        startTrainer.printAllTrainer();
                                        break;
                                    }
                                case "students":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startCourse.printStudentsInCourse(test[2]);
                                        } else {
                                            System.out.println("[Invalid command]");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "course":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startCourse.printSoloCourse(test[2]);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "trainer":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startTrainer.printSoloTrainer(test[2]);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "student":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startStudent.printSoloStudent(test[2]);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "journal":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startQuest.printJournal(test[2]);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                default:
                                    System.out.println("[Invalid command]");
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("[Invalid command]");
                        }
                        break;
                    case "change":
                        try {
                            switch (test[1]) {
                                case "student":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startStudent.changeStudent(test[2]);
                                        } else {
                                            System.out.println("[Invalid command]");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "course":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            Integer q = new Integer(test[2]);
                                            startCourse.changeCourse(q);
                                        } else {
                                            System.out.println("[Invalid command]");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                case "trainer":
                                    try {
                                        if (checkIDforValid(test[2])) {
                                            startTrainer.changeTrainer(test[2]);
                                        } else {
                                            System.out.println("[Invalid command]");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("[Invalid command]");
                                    }
                                    break;
                                default:
                                    System.out.println("[Invalid command]");
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("[Invalid command]");
                        }
                        break;
                    default:
                        System.out.println("[Invalid command]");
                        break;
                }
            } catch (Exception e) {
                System.out.println("[Invalid command]");
            }


        }
    }
}
