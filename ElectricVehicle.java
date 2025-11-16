import java.util.List;
/**
 * Model the common elements of an Electric Vehicle (EV) that operates 
 * within the simulation, moving towards a target and potentially recharging.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2024.10.07
 */
public class ElectricVehicle 
{
    private static final int CONSUMPTION_PER_STEP = 5;

    private final String plate;             // Matrícula (7 caracteres)
    private final String name;              // Identificativo (ej. "Mercedes EQE")
    private final EVCompany company;       // Empresa a la que está suscrito
    
    private Location location;              // Posición actual
    private Location targetLocation;  // Destino FINAL
    
    private int idleCount;                  // Pasos parado en el destino
    private final int batteryCapacity;      // Capacidad máxima kWh
    private int batteryLevel;               // Carga actual kWh
    
    private int kwsCharged;                 // Total kWh cargados en su historia
    private int chargesCount;               // Total de recargas en su historia
    private float chargestCost;             // Coste total de recargas
    /**
     * El destino INMEDIATO al que se dirige el vehículo.
     * Puede ser el 'targetLocation' o la 'Location' de una ChargingStation.
     */
    private Location currentDestination;
    
    /**
     * La estación de carga específica a la que nos dirigimos (si aplica).
     * Se usa para saber qué estación interrogar al llegar.
     */
    private ChargingStation targetChargingStation;
    
    /**
     * Bandera para saber si el vehículo ha terminado su simulación.
     * Se activa al llegar al 'targetLocation' o si se queda sin batería
     * o si no hay cargadores libres.
     */
    private boolean hasFinishedSimulation;

    /**
     * Constructor of class ElectricVehicle.
     * @param company The EV's operating company. Must not be null.
     * @param location The EV's starting {@link Location}. Must not be null.
     * @param targetLocation The EV's final destination {@link Location}. Must not be null.
     * @param name The name of the vehicle.
     * @param plate The license plate of the vehicle.
     * @param batteryCapacity The maximum capacity of the battery.
     * @throws NullPointerException If company, location, or targetLocation is null.
     */
    public ElectricVehicle(String plate, String name, EVCompany company, 
                           Location initialLocation, Location targetLocation, 
                           int batteryCapacity) 
    {
        this.plate = plate;
        this.name = name;
        this.company = company;
        this.location = initialLocation;
        this.targetLocation = targetLocation;
        this.batteryCapacity = batteryCapacity;

        // Valores iniciales
        this.batteryLevel = batteryCapacity; // Asumimos que empieza lleno
        this.idleCount = 0;
        this.kwsCharged = 0;
        this.chargesCount = 0;
        this.chargestCost = 0.0f;
        
        // La ruta se calculará en el primer 'act()'
        this.currentDestination = null; 
        this.targetChargingStation = null;
        this.hasFinishedSimulation = false;
    }
        
        /**
     * Get the current location.
     * @return Where this vehicle is currently located.
     */
    public Location getLocation()
    {
        
        return location;
    }

    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        if (location != null) {
            this.location = location;
        } else {
            throw new NullPointerException("La location no puede ser nula");
        }
    }

    /**
     * Get the final target location.
     * @return Where this vehicle is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }

    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link ChargingStation} to visit, or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return null;
    }
    
    
    /**
     * Get the simulation step when the vehicle arrived at its final target location.
     * @return The arriving step.
     */
    public int getArrivingStep()
    {
        //TODO: Complete this code
        return 0;
    }
    
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        targetLocation = location;
    }

    
    /**
     * Calculates the optimal route for the vehicle. 
     * If there isn't enough battery to reach the target, it attempts to find an intermediate 
     * {@link ChargingStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute()
    {
        //TODO: Complete this code
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        //TODO: Complete this code
        return null;
    }
    

    /**
     * Checks if the current battery level is sufficient to cover a given distance.
     * @param distanceToTargetLocation The distance to check.
     * @return {@code true} if the battery level is enough, {@code false} otherwise.
     */
    public boolean enoughBattery(int distanceToTargetLocation)
    {
        int kwhNeeded = distanceToTargetLocation * CONSUMPTION_PER_STEP;
        return batteryLevel >= kwhNeeded;

    }
    
    
        /**
     * Determines the optimal intermediate {@link ChargingStation} to visit for recharging
     * if the vehicle cannot reach the final target directly.
     * Sets {@code rechargingLocation} to the chosen station's location.
     */
    public void calculateRechargingPosition()
    {
        //TODO: Complete this code
    }
    
    
     /**
      * Checks if the vehicle has a planned recharging stop.
      * @return Whether or not this vehicle has a recharging location set.
      */
     public boolean hasRechargingLocation(){
        //TODO: Complete this code
        return false;
     }

    
     /**
      * @return The number of simulation steps this vehicle has been idle.
      */
    public int getIdleCount()
    {
        //TODO: Complete this code
        return 0;
    }

     /**
      * @return The count of total recharges performed by this vehicle.
      */
    public int getChargesCount()
    {
        //TODO: Complete this code    
        return 0;
    }
    
    /**
     * Increment the number of steps on which this vehicle has been idle.
     */
    public void incrementIdleCount()
    {
        //TODO: Complete this code
    }

    
     /**
      * Get the Manhattan-like distance to the final target location from the current location.
      * @return The distance to the target location.
      */
     public int distanceToTheTargetLocation()
     {
        return this.location.distance(this.targetLocation);
     }

         /**
      * Simulates the recharging process when the vehicle arrives at a {@code rechargingLocation}.
      * The battery is charged to full capacity, the cost is calculated, and the route is recalculated.
      * @param step The current step of the simulation.
      */
    public void recharge(int step)
    {
        //TODO: Complete this code    
    } 
    
    /**
     * Increments the count of recharges performed by this vehicle.
     */
    public void incrementCharges()
    {
         //TODO: Complete this code
    }
    
    /**
     * Adds a cost amount to the total charges cost.
     * @param cost The cost of the last recharge.
     */
    public void incrementChargesCost(float cost)
    {
         //TODO: Complete this code
    }   
     
     /**
      * Carries out a single step of the vehicle's actions.
      * Moves one step towards the target (recharging or final) or stays idle.
      * @param step The current step of the simulation.
      */
     public void act(int step)
     {
        //TODO: Complete this code     
    }
     
    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EVCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel(){
        //TODO: Complete this code    
    }

    
    /**
     * Returns a detailed string representation of the electric vehicle.
     * @return A string containing the vehicle's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString(){
        //TODO: Complete this code
        return null;
    }

    /**
     * Generates a string containing the vehicle's details prefixed with the current step number.
     * @param step The current simulation step.
     * @return A formatted string for a step log.
     */
    public String getStepInfo(int step){
         //TODO: Complete this code
         return null;
    }
    
    /**
     * Generates a string of the vehicle's initial or final status for summary display.
     * @return The output of {@link #toString()} wrapped in parentheses.
     */
    public String getInitialFinalInfo(){
         //TODO: Complete this code
         return null;
    }
    public String getPlate() {
        return plate;
    }

    public String getName() {
        return name;
    }
    
    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }
    
    // Asumimos que el 'id' del que hablan los logs es la 'plate'
    public String getId() {
        return plate;
    }
}