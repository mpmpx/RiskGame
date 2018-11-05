package risk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Stream;

import risk.game.*;

public class ReadFileController {

	private HashMap<String, Continent> continentMap;
	private HashMap<String, Territory> territoryMap;
	private HashMap<String, LinkedList<String>> edgeMap;
	private String label;
	
	private boolean bMap, bContinent, bTerritory;
	
	public ReadFileController() {
		continentMap = new HashMap<String, Continent>();
		territoryMap = new HashMap<String, Territory>();
		edgeMap = new HashMap<String, LinkedList<String>>();
		
		bMap = false;
		bContinent = false;
		bTerritory = false;
	}
	
    private void validateBlockName() throws Exception  {
        if (!(bMap && bContinent && bTerritory)) {
            throw new Exception("bolck name is invalid!");
        }
    }

    /**
     * Method to check the value of continents and validation of the continent of each territory.
     * @throws IOException if the value of a continent is 0 or some countries contain
     * an invalid continent.
     */
    private void validateContinent() throws Exception {
    	boolean found = false;
    	
    	if (continentMap.size() > RiskMap.MAX_CONTINENT) {
    		throw new Exception("Number of the continents exceeds the Maximum " + RiskMap.MAX_CONTINENT + ".");
    	}
    	
    	for (Continent continent : continentMap.values()) {
    		if (continent.getValue() < 0) {
    			throw new Exception("Continent " + continent.getName() + " has invalid value");
    		}
    	}
    	
    	for (Territory territory : territoryMap.values()) {
    		found = false;
    		for (Continent continent : continentMap.values()) {
    			if (continent.getName().equals(territory.getContinentName())) {
    				found = true;
    			}
    		}
    		
    		if (!found) {
    			throw new Exception("Continent of " + territory.getName() + ": " + territory.getContinentName() + " is invalid.");
    		}
    	}
    }

    /**
     * Method to validate every territory and their adjacent countries.
     * @throws IOException if a territory's adjacent territory is not valid or a territory's adjacent
     * territory does not links to itself.
     */
    private void validateTerritory() throws Exception {
    	if (territoryMap.size() > RiskMap.MAX_COUNTRY) {
			throw new Exception("Number of countries exceeds the Maximum: " + RiskMap.MAX_COUNTRY + ".");
    	}
    	
    	for (Territory territory : territoryMap.values()) {
    		if (edgeMap.get(territory.getName()).size() > RiskMap.MAX_ADJACENT_COUNTRIES) {
    			throw new Exception(territory.getName() + " has more than 10 adjacent countries.");
    		}
    		for (String adjacent : edgeMap.get(territory.getName())) {
    			if (territoryMap.containsKey(adjacent) && edgeMap.containsKey(adjacent)) {
    				if (!edgeMap.get(adjacent).contains(territory.getName())) {
    					throw new Exception(territory.getName() + " and " + adjacent + " do not link to each other.");
    				}
    			}
    			else {
    				throw new Exception("Adjacent territory of " + territory.getName() + ": " + adjacent + " is invalid.");
    			}
    		}
    	}
    }
    
    /**
     * Check whether the map read from the file is a connected graph.
     * @throws IOException if the the map read from the file is not a connected graph.
     */
    private void validateConnectedGraph() throws Exception {
    	String firstTerritory = null;
    	for (String name : territoryMap.keySet()) {
    		firstTerritory = name;
    		break;
    	}
    	
    	HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    	for (Territory territory : territoryMap.values()) {
    		visited.put(territory.getName(), false);
    	}
  
    	Stack<String> stack = new Stack<String>();
    	stack.push(firstTerritory);
    	
    	while (!stack.isEmpty()) {
    		String currentTerritory = stack.pop();
    		if (visited.get(currentTerritory) == false) {
    			visited.replace(currentTerritory, true);
    			for (String adjacentTerritory : edgeMap.get(currentTerritory)) {
    				stack.push(adjacentTerritory);
    			}
    		}
    	}
    	    	
    	for (Boolean status : visited.values()) {
    		if (status == false) {
				throw new Exception("The map contained in the file is not a connected graph.");
    		}
    	}
    }
    
    /**
     * Method to read a .map file and store all information to an instance of RiskMap and check
     * correctness of every information.
     * @param pathName is the path name of the file to be read.
     * @return an instance of RiskMap.
     * @throws IOException if encounters IO error.
     */	
    public void readFile(String pathName) throws Exception {
    	RiskMap map = RiskMap.getInstance();
    	map.clear();
    
    	File file = new File(pathName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Stream<String> mapFile = bufferedReader.lines();
        mapFile = mapFile.filter(l -> !"".equals(l.trim()));

        mapFile.forEach(line -> {

            line = line.trim();
            if (line.equals("[Map]") || line.equals("[Territories]") || line.equals("[Continents]")) {
                label = line;
            } else {
                switch (label) {
                    case "[Map]": {
                    	String[] token = line.split("=");
                    	if (token[0].equals("image")) {
                    		map.setImagePath(new File(file.getParent(), token[1]).getPath());
                    	}
                    	
                    	bMap = true;
                        break;
                    }
                    case "[Continents]": {
                        String[] token = line.split("=");
                        Continent continent = new Continent(token[0], Integer.parseInt(token[1]));
                        continentMap.put(continent.getName(), continent) ;
                        
                        bContinent = true;
                        break;
                    }
                    case "[Territories]": {
                        String[] token = line.split(",");
                        
                        Territory territory = new Territory();                            
                        
                        // Set basic information of a territory.
                        territory.setName(token[0]);
                        territory.setLocation(Integer.parseInt(token[1]), Integer.parseInt(token[2]));        
                        territory.setContinentName(token[3]);
                        territoryMap.put(territory.getName(), territory);    
                        
                        edgeMap.put(territory.getName(), new LinkedList<String>());                          
                        for (int i = 4; i < token.length; i++) {
                        	edgeMap.get(territory.getName()).add(token[i]);
                        }

                        bTerritory = true;
                        break;
                    }
                    default:
                        break;
                }
            }

        });
        bufferedReader.close();
        
        // Map validation.
        validateBlockName();
        validateContinent();
        validateTerritory();
        validateConnectedGraph();
        
        
        for (Territory territory : territoryMap.values()) {
        	continentMap.get(territory.getContinentName()).addTerritory(territory);
        	map.addTerritory(territory);
        }
        
        for (Continent continent : continentMap.values()) {
        	map.addContinent(continent);
        }
        
        map.addLink(edgeMap);
    }
}