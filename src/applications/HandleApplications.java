package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HandleApplications {
    private Map<String, Skill> skills = new HashMap<>();
    private Map<String, Position> positions = new HashMap<>();
    private Map<String, String> applicantsCapabilities = new HashMap<>();
    private Map<String, Map<String, Integer>> applicantSkillLevels = new HashMap<>();
    private Map<String, Boolean> applicantHasApplied = new HashMap<>();

    public void addSkills(String... names) throws ApplicationException {
        for (String name : names) {
            if (skills.containsKey(name)) {
                throw new ApplicationException();
            }
            skills.put(name, new Skill(name));
        }
    }

    public void addPosition(String name, String... skillNames) throws ApplicationException {
        if (positions.containsKey(name)) {
            throw new ApplicationException();
        }
        
        Position position = new Position(name);
        positions.put(name, position);
        
        for (String skillName : skillNames) {
            Skill skill = getSkill(skillName);
            if (skill == null) {
                throw new ApplicationException();
            }
            position.addRequiredSkill(skill);
        }
    }
    
    public Skill getSkill(String name) {
        return skills.get(name);
    }
    
    public Position getPosition(String name) {
        return positions.get(name);
    }

    public void addApplicant(String name, String capabilities) throws ApplicationException {
        if (applicantsCapabilities.containsKey(name)) {
            throw new ApplicationException();
        }
        
        Map<String, Integer> skillLevels = new HashMap<>();
        if (!capabilities.isEmpty()) {
            String[] capabilityArray = capabilities.split(",");
            for (String capability : capabilityArray) {
                String[] parts = capability.split(":");
                if (parts.length != 2) {
                    throw new ApplicationException();
                }
                
                String skillName = parts[0];
                if (!skills.containsKey(skillName)) {
                    throw new ApplicationException();
                }
                
                int level;
                try {
                    level = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    throw new ApplicationException();
                }
                
                if (level < 1 || level > 10) {
                    throw new ApplicationException();
                }
                
                skillLevels.put(skillName, level);
            }
        }
        
        applicantsCapabilities.put(name, capabilities);
        applicantSkillLevels.put(name, skillLevels);
        applicantHasApplied.put(name, false);
    }
    
    public String getCapabilities(String applicantName) throws ApplicationException {
        if (!applicantsCapabilities.containsKey(applicantName)) {
            throw new ApplicationException();
        }
        
        Map<String, Integer> skillLevels = applicantSkillLevels.get(applicantName);
        if (skillLevels.isEmpty()) {
            return "";
        }
        
        List<String> skillNames = new ArrayList<>(skillLevels.keySet());
        Collections.sort(skillNames);
        
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String skillName : skillNames) {
            if (!first) {
                result.append(",");
            }
            result.append(skillName).append(":").append(skillLevels.get(skillName));
            first = false;
        }
        
        return result.toString();
    }

    public void enterApplication(String applicantName, String positionName) throws ApplicationException {
        if (!applicantsCapabilities.containsKey(applicantName)) {
            throw new ApplicationException();
        }
        
        Position position = getPosition(positionName);
        if (position == null) {
            throw new ApplicationException();
        }
        
        if (Boolean.TRUE.equals(applicantHasApplied.get(applicantName))) {
            throw new ApplicationException();
        }
        
        Map<String, Integer> applicantSkills = applicantSkillLevels.get(applicantName);
        for (Skill requiredSkill : position.getRequiredSkills()) {
            String skillName = requiredSkill.getName();
            if (!applicantSkills.containsKey(skillName)) {
                throw new ApplicationException();
            }
        }
        
        position.addApplicant(applicantName);
        applicantHasApplied.put(applicantName, true);
    }

    public int setWinner(String applicantName, String positionName) throws ApplicationException {
        if (!applicantsCapabilities.containsKey(applicantName)) {
            throw new ApplicationException();
        }
        
        Position position = getPosition(positionName);
        if (position == null) {
            throw new ApplicationException();
        }
        
        if (position.getWinner() != null) {
            throw new ApplicationException();
        }
        
        if (!position.getApplicants().contains(applicantName)) {
            throw new ApplicationException();
        }
        
        Map<String, Integer> applicantSkills = applicantSkillLevels.get(applicantName);
        int totalLevel = 0;
        int requiredSkillCount = 0;
        
        for (Skill requiredSkill : position.getRequiredSkills()) {
            String skillName = requiredSkill.getName();
            totalLevel += applicantSkills.get(skillName);
            requiredSkillCount++;
        }
        
        if (totalLevel <= 6 * requiredSkillCount) {
            throw new ApplicationException();
        }
        
        position.setWinner(applicantName);
        return totalLevel;
    }

    public SortedMap<String, Long> skill_nApplicants() {
        SortedMap<String, Long> result = new TreeMap<>();
        
        for (String skillName : skills.keySet()) {
            long count = 0;
            for (Position position : positions.values()) {
                boolean hasSkill = false;
                for (Skill requiredSkill : position.getRequiredSkills()) {
                    if (requiredSkill.getName().equals(skillName)) {
                        hasSkill = true;
                        break;
                    }
                }
                if (hasSkill) {
                    count += position.getApplicants().size();
                }
            }
            result.put(skillName, count);
        }
        
        return result;
    }
    
    public String maxPosition() {
        String maxPositionName = null;
        int maxApplicants = -1;
        
        for (Position position : positions.values()) {
            int applicantCount = position.getApplicants().size();
            if (applicantCount > maxApplicants || 
                (applicantCount == maxApplicants && maxPositionName != null && 
                 position.getName().compareTo(maxPositionName) < 0)) {
                maxApplicants = applicantCount;
                maxPositionName = position.getName();
            }
        }
        
        return maxPositionName;
    }
}
