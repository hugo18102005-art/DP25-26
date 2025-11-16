import java.util.ArrayList;
import java.util.List;
/**
 * Model a charger unit within a {@link ChargingStation}.
 * It tracks its charging capabilities, fee, and the electric vehicles it has recharged.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2024.10.07 prueba
 */
public class Charger
{

    private final String id;
    private final int chargingSpeed; 
    private final float chargingFee;
    private final List<ElectricVehicle> evsRecharged;
    private float amountCollected;
    private boolean free;   

    /**
     * Constructor for objects of class Charger.
     * @param id The unique identifier of the charger.
     * @param speed The maximum charging speed in kWh.
     * @param fee The cost per kWh for charging.
     */
    public Charger(String id, int chargingSpeed, float chargingFee)
    {
        this.id = id;
        this.chargingSpeed = chargingSpeed;
        this.chargingFee = chargingFee;
        this.evsRecharged = new ArrayList<>();
        this.amountCollected = 0.0f;
    }

    /**
     * @return El ID único del cargador.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * @return La velocidad de carga (para el Comparator).
     */
    public double getSpeed() {
        // El comparator espera double, pero el constructor pide int.
        // Hacemos un cast (conversión).
        return (double) this.chargingSpeed;
    }

    /**
     * @return La tarifa de carga (céntimos/kWh).
     */
    public float getFee() {
        return this.chargingFee;
    }
    
    /**
     * @return La tarifa de carga (para el Comparator).
     * Nota: Añadido para coincidir con el Comparator (getRate).
     */
    public double getRate() {
        // El comparator espera double, pero el constructor pide float.
        // Hacemos un cast.
        return (double) this.chargingFee;
    }
    
    /**
     * @return La cantidad total de dinero recolectada.
     */
    public float getTotalAmountCollected() {
        return this.amountCollected;
    }
    /**
     * Returns a string representation of the charger, including its ID, speed, fee, and the number of EVs recharged.
     * @return A string representation of the charger.
     */
    @Override
    public String toString()
    {
        return "Charger[" + this.id +
               ", Speed: " + this.chargingSpeed + "kWh" +
               ", Fee: " + String.format("%.2f", this.chargingFee) + "€/kWh" + 
               ", Recharged: " + getNumerEVRecharged() + " EVs" +
               "]";
    }
    
    
    /**
     * Returns a complete string representation of the charger, including details of all {@link ElectricVehicle}s it has recharged.
     * @return A string containing complete information about the charger and its usage history.
     */
    public String getCompleteInfo()
    {
         //TODO: Complete this code
         return null;
    }
    
    /**
     * Adds an {@link ElectricVehicle} to the list of vehicles that have been recharged by this charger.
     * @param vehicle The electric vehicle that was recharged.
     */
    public void addEvRecharged(ElectricVehicle vehicle){
        //TODO: Complete this code
    }
    
    /**
     * @return The total number of {@link ElectricVehicle}s that have been recharged by this charger.
     */
    public int getNumerEVRecharged(){
        return this.evsRecharged.size();
    }
    
    
    /**
     * Simulates the charging process for an {@link ElectricVehicle}.
     * Increases the amount collected and registers the vehicle as recharged.
     * @param vehicle The vehicle to recharge.
     * @param kwsRecharging The amount of kWh to be recharged.
     * @return The cost of the recharge operation.
     */
    public float recharge(ElectricVehicle vehicle,int kwsRecharging){
        //TODO: Complete this code 
        return 0;
    }    
    /**
     * 
     */
    public int getChargingSpeed (){
        
        return chargingSpeed;
    }
    
    /**
     * 
     */
    public float getChargingFee (){
        
        return chargingFee;
    }
}