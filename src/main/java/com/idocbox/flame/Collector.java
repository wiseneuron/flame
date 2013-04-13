/*
 * Copyright [2013] [C.H Li https://github.com/wiseneuron/flame/ e-mail:wiseneuron@gmail.com]
 * Licensed to the Chunhui Li(C.H Li) under one or more contributor license agreements.  
 * See the NOTICE file distributed with this work for additional information 
 * regarding copyright ownership.
 *
 * C.H Li licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.idocbox.flame;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * data collector.
 * @author C.H Li wiseneuron@gmail.com
 * 
 */
public class Collector<T> {
	/**
	 * concurrent map.
	 */
	private ConcurrentMap<String, Set<T>> dataSetMap;

    
    public Collector(){
    	this.dataSetMap = new ConcurrentHashMap<String, Set<T>>();
    }

    /**
	 * @return the dataSetMap
	 */
	public ConcurrentMap<String, Set<T>> getDataSetMap() {
		return dataSetMap;
	}
	/**
	 * put data into collector.
	 * @param key
	 * @param dataSet
	 */
	public void put(String key, Set<T> dataSet){
		this.dataSetMap.put(key, dataSet);
	}

	/**
     * add collector to the collector.
     * merge the same key's set to one set.
     * @param collector
     */
    public void merge(Collector<T> collector){
    	if(null != collector){
    		ConcurrentMap<String, Set<T>> map = collector.getDataSetMap();
    		if(null == map || map.isEmpty()){
    			return;
    		}
    		Set<String> keys = map.keySet();
    		for(String key : keys){
    			Set<T> s1 = this.dataSetMap.get(key);
    			if(null != s1){
    				s1.addAll(map.get(key));
    			} else {
    				Set<T> s2 = map.get(key);
    				if(null != s2 && !s2.isEmpty()){
    					this.dataSetMap.put(key, s2);
    				}
    			}
    		}
    	}
    }
}
