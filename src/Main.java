import applications.HandleApplications;
import applications.Position;
import applications.Skill;
import applications.ApplicationException;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        try {
            HandleApplications handler = new HandleApplications();
            
            handler.addSkills("java", "python", "sql", "uml");
            
            handler.addPosition("Developer", "java", "sql");
            handler.addPosition("Analyst", "uml", "python");
            
            handler.addApplicant("John", "java:8,sql:7,uml:5");
            handler.addApplicant("Mary", "java:9,python:8,sql:6,uml:7");
            handler.addApplicant("Bob", "python:9,uml:8");
            
            handler.enterApplication("John", "Developer");
            handler.enterApplication("Mary", "Developer");
            handler.enterApplication("Bob", "Analyst");
            
            int johnScore = handler.setWinner("John", "Developer");
            System.out.println("John's score for Developer: " + johnScore);
            
            int bobScore = handler.setWinner("Bob", "Analyst");
            System.out.println("Bob's score for Analyst: " + bobScore);
            
            System.out.println("Max position: " + handler.maxPosition());
            
            SortedMap<String, Long> skillStats = handler.skill_nApplicants();
            for (Map.Entry<String, Long> entry : skillStats.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            
        } catch (ApplicationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
