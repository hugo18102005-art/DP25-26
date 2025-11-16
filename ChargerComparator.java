import java.util.*;
/**
 * Write a description of class ChargerComparator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChargerComparator implements Comparator<Charger> {

    /**
     * Compara dos cargadores (c1 y c2) para determinar su orden.
     */
    @Override
    public int compare(Charger c1, Charger c2) {
        
        // 1. Criterio: Decreciente por velocidad de carga
        //    Comparamos c2 contra c1 para orden descendente.
        int speedCompare = Double.compare(c2.getSpeed(), c1.getSpeed());
        if (speedCompare != 0) {
            return speedCompare;
        }

        // 2. Empate (velocidades iguales): Creciente por tarifa
        //    Comparamos c1 contra c2 para orden ascendente.
        int rateCompare = Double.compare(c1.getRate(), c2.getRate());
        if (rateCompare != 0) {
            return rateCompare;
        }

        // 3. Empate (velocidad y tarifa iguales): Creciente por ID
        //    Comparamos c1 contra c2 para orden ascendente (alfabético).
        return c1.getId().compareTo(c2.getId());
    }
}