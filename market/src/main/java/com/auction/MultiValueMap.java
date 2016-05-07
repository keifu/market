package com.auction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Simple implementation of multi-value map where key can be mapped to a list of values
 * 
 * @author Keith
 *
 */
public class MultiValueMap<K, V> {

	private Map<K,List<V>> map = new HashMap<>();
	
	public MultiValueMap(){}
		 
	public synchronized void put(K key, V value){
		
		List<V> values = map.get(key);
		if(values == null){
			values = new ArrayList<>();
			map.put(key, values);
		}
		values.add(value);
	}
	
	public synchronized List<V> get(K key){
		List<V> list =  map.get(key);
		if(list == null){
			return Collections.emptyList();
		}
		return list;
	}
	
	public synchronized void remove(K key){
		map.remove(key);
	}
	
	public synchronized void removeValue(K key, V value){
		List<V> list = map.get(key);
		if(list != null){
			list.remove(value);
			if(list.isEmpty()){
				remove(key);
			}
		}
	}
	
}
