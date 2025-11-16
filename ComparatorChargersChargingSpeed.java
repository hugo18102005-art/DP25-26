import java.util.*;

/**
 * 
 */
class ComparatorChargersChargingSpeed implements Comparator<Charger> {
    /**
     * Compara dos cargadores (c1 y c2) para determinar su orden.
     */
    @Override
    public int compare(Charger c1, Charger c2) {
        
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("Null Charger in compare(ComparatorChargersChargingSpeed)");
        }

        int aux = Integer.compare(c2.getChargingSpeed(), c1.getChargingSpeed());
        
        if (aux != 0) return aux;

        aux = Float.compare(c1.getChargingFee(), c2.getChargingFee());
        if (aux != 0) return aux;

        return c1.getId().compareTo(c2.getId());
    }
}