package testobjectmanager.aspect.collector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.javatuples.Quartet;

public class PerformanceCollector {
    private static PerformanceCollector _instance;

    private static List<Quartet<String, String, Long, Date>> history;

    public static PerformanceCollector getInstance() {
        if (_instance == null) {
            _instance = new PerformanceCollector();
        }
        return _instance;
    }

    private PerformanceCollector() {
        history = new ArrayList<Quartet<String, String, Long, Date>>();
    }

    public void addToHistory(String action, String value, Long object, Date date) {
        history.add(new Quartet<String, String, Long, Date>(action, value, object, date));
    }

    public List<Quartet<String, String, Long, Date>> getHistory() {
        return history;
    }

}
