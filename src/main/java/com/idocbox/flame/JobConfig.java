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
 * @author C.H Li
 *
 */
public class JobConfig<K, V, T> {
   /**
    * data source.
    */
	private DataSource<T> dataSource;
	/**
	 * data source spliter.
	 */
	private DataSourceSpliter<T> dataSourceSpliter;
	/**
	 * mapper worker.
	 */
	private MapperWorker<K, V, T> mapperWorker;
	/**
	 * reducer worker.
	 */
	private ReducerWorker<K, V> reducerWorker;
	/**
	 * mapper.
	 */
	private Mapper<K, V, T> mapper;
	/**
	 * reducer.
	 */
	private Reducer<K, V> reducer;
	/**
	 * keeper 
	 */
	private Keeper<Collector<Map<K, V>>> keeper;
	/**
	 * max mapper worker number.
	 * default value is 10.
	 */
	private int maxMapperWorker = 10;
	/**
	 * max reducer worker.
	 * default value is 12.
	 */
	private int maxReducerWorker = 12;
	/**
	 * @return the dataSource
	 */
	public DataSource<T> getDataSource() {
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource<T> dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * @return the dataSourceSpliter
	 */
	public DataSourceSpliter<T> getDataSourceSpliter() {
		return dataSourceSpliter;
	}
	/**
	 * @param dataSourceSpliter the dataSourceSpliter to set
	 */
	public void setDataSourceSpliter(DataSourceSpliter<T> dataSourceSpliter) {
		this.dataSourceSpliter = dataSourceSpliter;
	}
	/**
	 * @return the mapperWorker
	 */
	public MapperWorker<K, V, T> getMapperWorker() {
		return mapperWorker;
	}
	/**
	 * @param mapperWorker the mapperWorker to set
	 */
	public void setMapperWorker(MapperWorker<K, V, T> mapperWorker) {
		this.mapperWorker = mapperWorker;
	}
	/**
	 * @return the reducerWorker
	 */
	public ReducerWorker<K, V> getReducerWorker() {
		return reducerWorker;
	}
	/**
	 * @param reducerWorker the reducerWorker to set
	 */
	public void setReducerWorker(ReducerWorker<K, V> reducerWorker) {
		this.reducerWorker = reducerWorker;
	}
	/**
	 * @return the mapper
	 */
	public Mapper<K, V, T> getMapper() {
		return mapper;
	}
	/**
	 * @param mapper the mapper to set
	 */
	public void setMapper(Mapper<K, V, T> mapper) {
		this.mapper = mapper;
	}
	/**
	 * @return the reducer
	 */
	public Reducer<K, V> getReducer() {
		return reducer;
	}
	/**
	 * @param reducer the reducer to set
	 */
	public void setReducer(Reducer<K, V> reducer) {
		this.reducer = reducer;
	}
	/**
	 * @return the maxMapperWorker
	 */
	public int getMaxMapperWorker() {
		return maxMapperWorker;
	}
	
	
	/**
	 * @return the keeper
	 */
	public Keeper<Collector<Map<K, V>>> getKeeper() {
		return keeper;
	}
	/**
	 * @param keeper the keeper to set
	 */
	public void setKeeper(Keeper<Collector<Map<K, V>>> keeper) {
		this.keeper = keeper;
	}
	/**
	 * @param maxMapperWorker the maxMapperWorker to set
	 */
	public void setMaxMapperWorker(int maxMapperWorker) {
		this.maxMapperWorker = maxMapperWorker;
	}
	/**
	 * @return the maxReducerWorker
	 */
	public int getMaxReducerWorker() {
		return maxReducerWorker;
	}
	/**
	 * @param maxReducerWorker the maxReducerWorker to set
	 */
	public void setMaxReducerWorker(int maxReducerWorker) {
		this.maxReducerWorker = maxReducerWorker;
	}
	
}
