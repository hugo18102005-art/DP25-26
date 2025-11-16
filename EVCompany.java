import java.util.*;

/**
 * Models the operation of an Electric Vehicle (EV) Company. 
 * It manages a fleet of {@link ElectricVehicle}s and a network of {@link ChargingStation}s.
 * @author DP classes
 * @version 2024.10.07
 */
public class EVCompany  
{
    private final String name;
    private Set<ElectricVehicle> subscribedVehicles;
    private List<ChargingStation> stations;

    /**
     * Constructor for objects of class EVCompany.
     * @param name The name of the company.
     */
    public EVCompany(String name)
    {
        this.name = name;
        this.subscribedVehicles = new HashSet<>();
        this.stations = new ArrayList<>();
    }

     /**
     * @return The name of the company.
     */
    public String getName()
    {
        return name;
    }
    
    
    /**
     * @return An unmodifiable list of all {@link ElectricVehicle}s.
     */
    public List<ElectricVehicle> getVehicles()
    {       
        return null;
    }

    /**
     * Adds an {@link ElectricVehicle} to the company's fleet.
     * @param vehicle The electric vehicle to add.
     */
    public void addElectricVehicle(ElectricVehicle vehicle)
    {       
        //TODO: Complete this code
    }

    
    /**
     * Adds a {@link ChargingStation} to the company's network.
     * @param station The charging station to add.
     */
    public void addChargingStation(ChargingStation station)
    {       
       if (station != null && !this.stations.contains(station)){
           this.stations.add(station);
        }
    }
    
    
    /**
     * Retrieves a {@link ChargingStation} by its unique ID.
     * @param id The ID of the station to find.
     * @return The {@link ChargingStation} with the matching ID, or {@code null} if not found.
     */
    public ChargingStation getChargingStation(String id)
    {
        //TODO: Complete this code
        return null;
    }

    /**
     * Retrieves a {@link ChargingStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link ChargingStation} at the matching location, or {@code null} if not found.
     */
    public ChargingStation getChargingStation(Location location)
    {
        //TODO: Complete this code
        return null;
    }
    
    /**
     * @return An unmodifiable list of all managed {@link ChargingStation}s.
     */
    public List<ChargingStation> getCityStations()
    {
       //TODO: Complete this code
       return null;
    }
    
    
    /**
     * @return The total number of managed {@link ChargingStation}s.
     */
    public int getNumberOfStations(){
        //TODO: Complete this code
        return 0;
    }
    
    /**
     * Clears all managed vehicles and stations, resetting the company to an empty state.
     */
    public void reset(){
        //TODO: Complete this code
    }
    
}