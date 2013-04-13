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
 * Mapper worker Interface .
 * @author C.H Li
 *
 * @param <K> key type.
 * @param <V> value type.
 * @param <T> data object type.
 */
public interface MapperWorker<K, V, T> extends Worker {
    /**
     * execute map action.
     * the method should execute parallel.
     * @param ds      data source.
     * @param mapper  mapper.
     * @return mapped result. this will be used as reducer's input.
     */
	public Collector<Map<K, V>> execute(DataSource<T> ds, Mapper<K, V, T> mapper);
	/**
	 * Create a mapper worker.
	 * @param name
	 * @return
	 */
	public MapperWorker<K, V, T> create(String name);
}
