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

import java.util.Map;

/**
 * Mapper.Map data source into collector .
 * @author C.H Li
 *
 * @param <K> key after map.
 * @param <V> value after map.
 * @param <T> data object type.
 */
public interface Mapper<K, V, T> {
    /**
     * map data source into collector.
     * @param ds data source .
     * @return collector.
     */
	public Collector<Map<K, V>> map(DataSource<T> ds);
	/**
	 * create an instance of the mapper. 
	 * @return
	 */
	public Mapper<K, V, T> create();
}
