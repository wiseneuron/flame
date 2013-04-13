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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default keeper. Based on BlockingQueue.
 * @author C.H Li
 *
 */
public class DefaultKeeper<T> implements Keeper<T> {
	/**
	 * log
	 */
	private static Log log = LogFactory.getLog(Helios.class);
	/**
	 * block queue.
	 */
	BlockingQueue<T> blockQueue = new LinkedBlockingQueue<T>();
	
	private int counter = 0;
	
	private Lock lock = new ReentrantLock();
	
	
	public void add(T o) {
		blockQueue.add(o);
		lock.lock();
		try {
			counter++;
		} finally {
			lock.unlock();
		}
	}

	public T poll() {
		return blockQueue.poll();
	}

	public T take() {
		try {
			return blockQueue.take();
		} catch (InterruptedException e) {
			log.error(e);
			return null;
		}
	}

	public int size() {
		return blockQueue.size();
	}

	public boolean isEmpty() {
		return blockQueue.isEmpty();
	}

	public int count() {
		return this.counter;
	}

}
