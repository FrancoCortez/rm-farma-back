package owl.tree.rmfarma.shared.comparer;

import java.util.Comparator;

public class FractionComparator implements Comparator<String> {
    @Override
    public int compare(String f1, String f2) {
        return Double.compare(parse(f1), parse(f2));
    }

    private double parse(String fraction) {
        try {
            String[] parts = fraction.split("/");
            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
