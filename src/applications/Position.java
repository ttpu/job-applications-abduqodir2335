package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Position {
    private String name;
    private Set<Skill> requiredSkills = new HashSet<>();
    private List<String> applicants = new ArrayList<>();
    private String winner = null;

    public Position(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void addRequiredSkill(Skill skill) {
        requiredSkills.add(skill);
        skill.addPosition(this);
    }
    
    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }
    
    public void addApplicant(String applicantName) {
        if (!applicants.contains(applicantName)) {
            applicants.add(applicantName);
            Collections.sort(applicants);
        }
    }

    public List<String> getApplicants() {
        return applicants;
    }
    
    public void setWinner(String applicantName) {
        this.winner = applicantName;
    }

    public String getWinner() {
        return winner;
    }
}
