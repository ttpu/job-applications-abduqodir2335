package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Skill {
    private String name;
    private List<Position> positions = new ArrayList<>();

    public Skill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Position> getPositions() {
        Collections.sort(positions, Comparator.comparing(Position::getName));
        return positions;
    }
    
    public void addPosition(Position position) {
        if (!positions.contains(position)) {
            positions.add(position);
        }
    }
}
