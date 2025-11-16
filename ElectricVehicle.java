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
    private float chargesCost;             // Coste total de recargas
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
    
    
    private Charger selectedCharger = null;
    /**
     * Bandera para saber si el vehículo ha terminado su simulación.
     * Se activa al llegar al 'targetLocation' o si se queda sin batería
     * o si no hay cargadores libres.
     */
    private boolean hasFinishedSimulation;
    
    private int arrivingStep;

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
        if (company == null || initialLocation == null || targetLocation == null) {
        throw new NullPointerException("Parametros no pueden ser null");
        }
        
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
        this.chargesCost = 0.0f;
        
        // La ruta se calculará en el primer 'act()'
        this.currentDestination = null; 
        this.targetChargingStation = null;
        this.hasFinishedSimulation = false;
        this.arrivingStep = -1;
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
        if (targetChargingStation == null) {
            
            return null;
            
        } else {
            
            return targetChargingStation.getLocation();
        }
    }
    
    /**
     * Get the simulation step when the vehicle arrived at its final target location.
     * @return The arriving step.
     */
    public int getArrivingStep()
    {
        return arrivingStep;
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        if (location == null) throw new IllegalArgumentException ("targetLocation must not be null // setTargetLocation (ElectricVehicle)");
        
        targetLocation = location;
    }
    
    /**
     * Calculates the optimal route for the vehicle. 
     * If there isn't enough battery to reach the target, it attempts to find an intermediate 
     * {@link ChargingStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute() {
        if (hasFinishedSimulation) {
            return;
        }

        int distanceToFinal = distanceToTheTargetLocation();

        if (enoughBattery(distanceToFinal)) {
            
            targetChargingStation = null;
            currentDestination = targetLocation;
            return;
        }

        calculateRechargingPosition();

        if (targetChargingStation == null) {
            
            hasFinishedSimulation = true;
            currentDestination = null;
            return;
        }

        currentDestination = targetChargingStation.getLocation();
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        if (getRechargingLocation() == null) {
            
            return location.toString() + ", " + targetLocation.toString();
            
        } else {
            
            return location.toString() + ", " + getRechargingLocation().toString() + ", " + targetLocation.toString();
        }
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
        targetChargingStation = null;

        List<ChargingStation> allStations = company.getCityStations();
        
        if (allStations == null || allStations.isEmpty()) {
            
            return;
        }

        ChargingStation bestStation = null;
        int bestDistance = Integer.MAX_VALUE;

        for (ChargingStation station : allStations) {
            
            Location stationLoc = station.getLocation();
            int distanceToStation = location.distance(stationLoc);

            
            if (enoughBattery(distanceToStation)) {
                
                if (distanceToStation < bestDistance) {
                    
                    bestDistance = distanceToStation;
                    bestStation = station;
                }
            }
        }

        targetChargingStation = bestStation;
    }
    
    
    /**
      * Checks if the vehicle has a planned recharging stop.
      * @return Whether or not this vehicle has a recharging location set.
      */
     public boolean hasRechargingLocation(){
         
         if (targetChargingStation == null) {
            return false;
        } else {
            return true;
        }
     }
    
     /**
      * @return The number of simulation steps this vehicle has been idle.
      */
    public int getIdleCount()
    {
        return idleCount;
    }

     /**
      * @return The count of total recharges performed by this vehicle.
      */
    public int getChargesCount()
    { 
        return chargesCount;
    }
    
    /**
     * Increment the number of steps on which this vehicle has been idle.
     */
    public void incrementIdleCount()
    {
        idleCount++;
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
    public void recharge(int step) {
        if(this.selectedCharger == null){
            System.out.println("Error: 'recharge' llamado sin cargador seleccionado");
            return;
        }
        //Calculamos cuanta energia necesita para llenarse
        int kwhToCharge = this.batteryCapacity - this.batteryLevel;
        
        //Usa el cargador guardado y llama a otro metodo
        float costOfThisCharge = this.selectedCharger.recharge(this, kwhToCharge);
        
        //Actualizar los campos de este vehiculo
        this.incrementCharges();
        this.incrementChargesCost(costOfThisCharge);
        this.batteryLevel = this.batteryCapacity;
        
        this.calculateRoute();
        
        String message = this.plate +  " recharges: " + kwhToCharge + " kwh at charger: "+
        this.selectedCharger.getId() + " with cost: "+ costOfThisCharge+ " euros";
        
        System.out.println( " (step: "+ step + " -ElectricVehicle: "+message);
        
        //Limpiar el cargador
        this.selectedCharger = null;
    } 
    
    /**
     * Increments the count of recharges performed by this vehicle.
     */
    public void incrementCharges()
    {
         chargesCount++;
    }
    
    /**
     * Adds a cost amount to the total charges cost.
     * @param cost The cost of the last recharge.
     */
    public void incrementChargesCost(float cost)
    {
         chargesCost += cost;
    }   
     
     /**
      * Carries out a single step of the vehicle's actions.
      * Moves one step towards the target (recharging or final) or stays idle.
      * @param step The current step of the simulation.
      */
     public void act(int step){
         
         if (hasFinishedSimulation) {
             
            return;
        }

        if (currentDestination == null) {
            
            calculateRoute();
            
            if (currentDestination == null) {
                
            hasFinishedSimulation = true;
            return;
            }
        }

        if (location.equals(targetLocation)) {
            
            if (arrivingStep == -1) {
                
                arrivingStep = step;
            }
            
            idleCount++;
            return;
        }

        Location next = location.nextLocation(currentDestination);
        setLocation(next);

        reduceBatteryLevel();

        if (batteryLevel <= 0) {
            
            hasFinishedSimulation = true;
            return;
        }

        if (location.equals(currentDestination)) {

            if (targetChargingStation != null && location.equals(targetChargingStation.getLocation())) {

                Charger freeCharger = targetChargingStation.getFreeCharger();

                if (freeCharger == null) {
                    
                    hasFinishedSimulation = true;
                    return;
                }

                this.selectedCharger = freeCharger;
                recharge(step);
                return;
            }

            if (location.equals(targetLocation)) {
                
                hasFinishedSimulation = true;
                arrivingStep = step;
                return;
            }

            calculateRoute();
        }
    }
     
    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EVCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel(){
        
        batteryLevel -= CONSUMPTION_PER_STEP;
        
        if (batteryLevel < 0) {
            
        batteryLevel = 0;
        }
    }

    /**
     * Returns a detailed string representation of the electric vehicle.
     * @return A string containing the vehicle's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString(){
        
        String result = "(ElectricVehicle: ";
        result += name + ", ";
        result += plate + ", ";
        result += batteryCapacity + "kwh, ";
        result += batteryLevel + "kwh, ";
        result += chargesCount + ", ";
        result += String.format("%.1f€", chargesCost) + ", ";
        result += idleCount + ", ";
        result += location.toString();

        Location rechargeLoc = getRechargingLocation();
        
        if (rechargeLoc != null) {
            
        result += ", " + rechargeLoc.toString();
        }

        result += ", " + targetLocation.toString();
        result += ")";

        return result;
    }

    /**
     * Generates a string containing the vehicle's details prefixed with the current step number.
     * @param step The current simulation step.
     * @return A formatted string for a step log.
     */
    public String getStepInfo(int step){
        
        String evInfo = name + ", "
                      + plate + ", "
                      + batteryCapacity + "kwh, "
                      + batteryLevel + "kwh, "
                      + chargesCount + ", "
                      + String.format("%.1f€", chargesCost) + ", "
                      + idleCount + ", "
                      + location.toString();

        Location rechargeLoc = getRechargingLocation();
        
        if (rechargeLoc != null) {
            
            evInfo += ", " + rechargeLoc.toString();
        }

        evInfo += ", " + targetLocation.toString();

        return "(step: " + step + " - ElectricVehicle: " + evInfo + ")";
    }
    
    /**
     * Generates a string of the vehicle's initial or final status for summary display.
     * @return The output of {@link #toString()} wrapped in parentheses.
     */
    public String getInitialFinalInfo(){
        
         return toString();
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