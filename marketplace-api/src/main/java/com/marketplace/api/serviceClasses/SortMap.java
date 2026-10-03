package com.marketplace.api.serviceClasses;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SortMap {
	Map<String,Integer> sortByValue(boolean order,Map<String, Integer> unsortedMap)   
		{  
		//convert HashMap into List   
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortedMap.entrySet());  
		//sorting the list elements  
		Collections.sort(list, new Comparator<Entry<String, Integer>>()   
		{  
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)   
		{  
		if (order)   
		{  
		//compare two object and return an integer  
			return o1.getValue().compareTo(o2.getValue());
		}   
		else   
		{  
			return o2.getValue().compareTo(o1.getValue());  
		}  
		}  
		});  
		//prints the sorted HashMap  
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();  
		for (Entry<String, Integer> entry : list)   
		{  
			sortedMap.put(entry.getKey(), entry.getValue());  
		}  
		return sortedMap;  
	}  
	
	

	
	
	
}
