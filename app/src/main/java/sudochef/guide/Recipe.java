package sudochef.guide;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class provides holds a list of steps and provides methods for iterating through them
 */
public class Recipe {

    LinkedList<Step> steps;
    Iterator<Step> current;

    public Recipe() {
        steps = new LinkedList<Step>();
    }

    public void begin() {
        current = steps.iterator();
    }

    public void addStep(Step s) {
        steps.add(s);
    }

    public Step nextStep() {
        return current.next();
    }

    public Boolean hasNext() {
        return current.hasNext();
    }
}