package com.gkudva.bart.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gkudva on 10/24/15.
 */
public class BartHashMap {
    private static Map<String, String> stationsMap;
    private static Map<String, String> reverseStationsMap;

    public static void initializeStationsMap()
    {
        stationsMap = new HashMap<>();
        reverseStationsMap = new HashMap<>();

        stationsMap.put("Anywhere", "Anywhere");
        stationsMap.put("12th St. Oakland City Center", "12TH");
        stationsMap.put("16th St. Mission (SF)", "16TH");
        stationsMap.put("19th St. Oakland", "19TH");
        stationsMap.put("24th St. Mission (SF)", "24TH");
        stationsMap.put("Ashby (Berkeley)", "ASHB");
        stationsMap.put("Balboa Park (SF)", "BALB");
        stationsMap.put("Bay Fair (San Leandro)", "BAYF");
        stationsMap.put("Castro Valley","CAST");
        stationsMap.put("Civic Center/UN Plaza (SF)", "CIVC");
        stationsMap.put("Coliseum", "COLS");
        stationsMap.put("Colma", "COLM");
        stationsMap.put("Concord", "CONC");
        stationsMap.put("Daly City", "DALY");
        stationsMap.put("Downtown Berkeley", "DBRK");
        stationsMap.put("Dublin/Pleasanton", "DUBL");
        stationsMap.put("El Cerrito del Norte", "DELN");
        stationsMap.put("El Cerrito Plaza", "PLZA");
        stationsMap.put("Embarcadero (SF)", "EMBR");
        stationsMap.put("Fremont", "FRMT");
        stationsMap.put("Fruitvale (Oakland)", "FTVL");
        stationsMap.put("Glen Park (SF)", "GLEN");
        stationsMap.put("Hayward", "HAYW");
        stationsMap.put("Lafayette", "LAFY");
        stationsMap.put("Lake Merritt (Oakland)", "LAKE");
        stationsMap.put("MacArthur (Oakland)", "MCAR");
        stationsMap.put("Millbrae", "MLBR");
        stationsMap.put("Montgomery St. (SF)", "MONT");
        stationsMap.put("North Berkeley", "NBRK");
        stationsMap.put("North Concord/Martinez", "NCON");
        stationsMap.put("Oakland Int'l Airport", "OAKL");
        stationsMap.put("Orinda", "ORIN");
        stationsMap.put("Pittsburg/Bay Point", "PITT");
        stationsMap.put("Pleasant Hill/Contra Costa Center", "PHIL");
        stationsMap.put("Powell St. (SF)", "POWL");
        stationsMap.put("Richmond", "RICH");
        stationsMap.put("Rockridge (Oakland)", "ROCK");
        stationsMap.put("San Bruno", "SBRN");
        stationsMap.put("San Francisco Int'l Airport", "SFIA");
        stationsMap.put("San Leandro", "SANL");
        stationsMap.put("South Hayward", "SHAY");
        stationsMap.put("South San Francisco", "SSAN");
        stationsMap.put("Union City", "UCTY");
        stationsMap.put("Walnut Creek", "WCRK");
        stationsMap.put("West Dublin", "WDUB");
        stationsMap.put("West Oakland", "WOAK");

        for (Map.Entry<String,String> entry: stationsMap.entrySet()) {
            reverseStationsMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static String getStationCodeFromStationName(String stationName)
    {
        return stationsMap.get(stationName);
    }

    public static String getStationNameFromStationCode(String stationCode)
    {
        return reverseStationsMap.get(stationCode);
    }
}
