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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author C.H Li
 *
 */
public class MapperWorkerRunable<K, V, T> implements Runnable{
	/**
	 * log
	 */
	private static Log log = LogFactory.getLog(MapperWorkerRunable.class);
    /**
     * mapper worker to run.	
     */
	private MapperWorker<K, V, T> mapperWorker;
	/**
	 * data source.
	 */
	private DataSource<T> dataSource;
	/**
	 * mapper.
	 */
	private Mapper<K, V, T> mapper;
	/**
	 * keeper.
	 */
	private Keeper<Collector<Map<K, V>>> keeper;
    /**
     * constructor .
     * @param mapperWorker
     * @param dataSource
     * @param mapper
     * @param keeper
     */
	public MapperWorkerRunable(MapperWorker<K, V, T> mapperWorker, DataSource<T> dataSource, Mapper<K, V, T> mapper, Keeper<Collector<Map<K, V>>> keeper){
		this.mapperWorker = mapperWorker;
		this.dataSource = dataSource;
		this.mapper = mapper;
		this.keeper = keeper;
	}
	/**
	 * thread port.
	 */
	public void run() {
		try{
			//flag the worker is busy.
			this.mapperWorker.setIdle(false);
			//move vernier to head.
			this.dataSource.head();
			//execute 
			Collector<Map<K, V>> collector = this.mapperWorker.execute(this.dataSource, this.mapper);
			//store collector to keeper.
			this.keeper.add(collector);
			//flag the worker is idle.
			this.mapperWorker.setIdle(true);
		} catch(Exception e){
			log.error("error occured in mapper worker thread:" + e);
		}
	}
	

}
