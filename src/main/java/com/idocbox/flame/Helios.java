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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helios can generate many flame to compute you want.
 * It is the main port.
 * @author C.H Li
 *
 * @param <K> key of data.
 * @param <V> value of data.
 * @param <T> data object type.
 */
public class Helios<K, V, T> {
	/**
	 * log
	 */
	private static Log log = LogFactory.getLog(Helios.class);
    /**
     * fire them!
     * @param ds       data source.
     * @param dsSpliter data source spliter.
     * @param mapper   mapper.
     * @param reducer  reducer.
     * @return
     */
	public Collector<Map<K, V>> fire(JobConfig<K, V, T> jobConfig) {
		
		long start = System.currentTimeMillis();
		
		Collector<Map<K, V>> resultCollector = null;
		
		// data source.
		DataSource<T> dataSource = jobConfig.getDataSource();
		// data source spliter.
		DataSourceSpliter<T> dataSourceSpliter = jobConfig.getDataSourceSpliter();
		// mapper worker. root mapper worker.
		MapperWorker<K, V, T> mapperWorker = jobConfig.getMapperWorker();
		// reducer worker. root reducer worker.
		ReducerWorker<K, V> reducerWorker = jobConfig.getReducerWorker();
		// mapper.
		Mapper<K, V, T> mapper = jobConfig.getMapper();
		// reducer.
		Reducer<K, V> reducer = jobConfig.getReducer();
		// keeper.
		Keeper<Collector<Map<K, V>>> keeper = jobConfig.getKeeper();

		// spliting phase.

		//split data source into serveral data source.
		log.info("spliting datasource ...");
		Map<String, DataSource<T>> dsMap = dataSourceSpliter.split(dataSource);
		
		long m1 = System.currentTimeMillis();
		long cost1 = m1 - start;
		double seconds1 = cost1/1000;
		log.info("spliting datasource: cost " + seconds1 + " s");
		
		// generate worker for mapper.create()
		if (null == dsMap || dsMap.isEmpty()) {
			log.info("Splited data source is empty! exit flame!");
			return null;
		}

		// mapping phase.
		
		// generate mapper worker.
		log.info("mapping && reducing ...");
		Set<String> dsKeys = dsMap.keySet();
		//mapper thread size.
		int mapperThreadSize = dsKeys.size() > jobConfig.getMaxMapperWorker() ? jobConfig.getMaxMapperWorker() : dsKeys.size();
		//create mapper worker thread pool.
		ExecutorService mapperWorkerThreadPool = Executors.newFixedThreadPool(mapperThreadSize);
		int dataSourceSize = 0;
		for (String key : dsKeys) {
			//create mapper worker baby.
			MapperWorker<K, V, T> mapperWorkerBaby = mapperWorker.create(key);
						
			//assign data source and run the worker.
			DataSource<T> dsUnit = dsMap.get(key);
			if(null != dsUnit){
				//execute mapper work in thread pool.
				mapperWorkerThreadPool.execute(new MapperWorkerRunable<K, V, T>(mapperWorkerBaby, dsUnit, mapper, keeper));
				
				dataSourceSize++;
			}
		}
        //shutdown executor service.
		mapperWorkerThreadPool.shutdown();
				
        // reduce phase.
        
		//generate reducer worker, assign mapper worker's compute result
		// to reducer worker.
        
		//mapper thread size.
		//create reducer worker thread pool.
		ExecutorService reducerWorkerThreadPool  =Executors.newFixedThreadPool(jobConfig.getMaxReducerWorker());
        
        //get 2 collector, merge them into one, then passed to reducer.
       Set<ReducerWorker<K, V>> reducerWorkers = new HashSet<ReducerWorker<K, V>>();
       int j = 0;
       int expectedReducTime = dataSourceSize - 1;
       while(true){//reduce while there is more than one element in set.
           if(mapperWorkerThreadPool.isTerminated()){
        	   int count = keeper.count();
        	   if(count== 0){//no mapped result.
        		   log.info("there is no result given by mapper. exit!");
        		   return null;
        	   }
           }
          if(j == expectedReducTime){
        	   log.info("complete reduce. exit flame.");
        	   break;
           }
          
    	   Set<Collector<Map<K, V>>> collectors = new HashSet<Collector<Map<K, V>>>(2);
           collectors.add(keeper.take());
           collectors.add(keeper.take());
           
		   // get an idle worker.
		   ReducerWorker<K, V> reducerWorkerBaby = chooseIdle(reducerWorkers, reducerWorker);

		   log.info("reducing, collector size = " + keeper.size());
			
		   reducerWorkerThreadPool.execute(new ReducerWorkerRunnable<K, V>(reducerWorkerBaby, collectors, reducer, keeper));
		   
		   j++;
        }
       
       //shutdown reducer worker thread pool.
       reducerWorkerThreadPool.shutdown();
       
		// collect result phase.
       while(!reducerWorkerThreadPool.isTerminated()){
    	  Thread.yield(); 
       }
		if (null != keeper && keeper.size() == 1) {
			resultCollector = keeper.poll();
		} else {// error occured.
			int size = 0;
			if (null != keeper) {
				size = keeper.size();
			}
			log.info("after reduce, the result collector is not expected! collector size is " + size);
		}
		
        //return result collector.
		long end = System.currentTimeMillis();
		long cost = end - m1;
		double seconds = cost/1000;
		log.info("mapping & reducing: cost " + seconds + " s");
		
		return resultCollector;
	}
	/**
	 * choose an idle reduce worker. if no idle worker in reducerWorkers, create a new worker.
	 * @param reducerWorkers
	 * @param reducerWorker
	 * @return
	 */
	private ReducerWorker<K, V> chooseIdle(	Set<ReducerWorker<K, V>> reducerWorkers, ReducerWorker<K, V> reducerWorker) {
		ReducerWorker<K, V> worker = null;
		
		if(!reducerWorkers.isEmpty()){
			for(ReducerWorker<K, V> tempWorker : reducerWorkers){
				if(tempWorker.isIdle()){
					worker = tempWorker;
					break;
				}
			}
		}
		
		if(null == worker){
			int i = reducerWorkers.size() + 1;
			worker = reducerWorker.create("reducer-worker-" + i);
			reducerWorkers.add(worker);
		}
		
		return worker;
	}
}
