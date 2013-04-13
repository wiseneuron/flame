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
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reducer worker runnable.
 * @author C.H Li
 *
 */
public class ReducerWorkerRunnable<K, V> implements Runnable {
	/**
	 * log
	 */
	private static Log log = LogFactory.getLog(ReducerWorkerRunnable.class);
	/**
	 * reducer worker.
	 */
	private ReducerWorker<K, V> reducerWorker;
	/**
	 * collectors to reduce.
	 */
	private Set<Collector<Map<K, V>>> collectors; 
	/**
	 * keeper.
	 */
	private Keeper<Collector<Map<K, V>>> keeper;
	/**
	 * reducer.
	 */
	private Reducer<K, V> reducer;
	/**
	 * constructor.
	 * @param reducerWorker
	 * @param collectors
	 * @param reducer
	 * @param keeper
	 */
	public ReducerWorkerRunnable(ReducerWorker<K, V> reducerWorker, Set<Collector<Map<K, V>>> collectors, Reducer<K, V> reducer, Keeper<Collector<Map<K, V>>> keeper){
		this.reducerWorker = reducerWorker;
		this.collectors = collectors;
		this.reducer = reducer;
		this.keeper = keeper;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try{
			//flag the worker is busy.
			this.reducerWorker.setIdle(false);
			// execute reducer
			Collector<Map<K, V>> reducedCollector = this.reducerWorker.execute(this.collectors, this.reducer);
			this.keeper.add(reducedCollector);
			//flag the worker is idle.
			this.reducerWorker.setIdle(true);
		} catch (Exception e){
			log.error("error occured in reducedWork thread:" + e);
		}
	}

}
