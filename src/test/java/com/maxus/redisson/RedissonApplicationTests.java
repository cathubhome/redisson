package com.maxus.redisson;

import com.maxus.redisson.redlock.AquiredLockWorker;
import com.maxus.redisson.redlock.RedisLocker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonApplicationTests {

	@Autowired
	RedisLocker distributedLocker;

	@Test
	public void contextLoads() throws InterruptedException {
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(5);
		for (int i = 0; i < 100; ++i) { // create and start threads
			new Thread(new Worker(startSignal, doneSignal)).start();
		}
		System.out.println("startSignal countdown execute");
		startSignal.countDown(); // let all threads proceed
		System.out.println("startSignal countdown execute finish");
		doneSignal.await();
		System.out.println("All processors done. Shutdown connection");
	}

	class Worker implements Runnable{

		private final CountDownLatch startSignal;
		private final CountDownLatch doneSignal;

		Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}

		public void run() {
			try {
				System.out.println("waiting....");
				startSignal.await();
				System.out.println("execute......");
				distributedLocker.lock("test",new AquiredLockWorker<Object>() {
					@Override
					public Object invokeAfterLockAquire() {
						doTask();
						return null;
					}

				});
			}catch (Exception e){

			}
		}

		void doTask() {
			System.out.println(Thread.currentThread().getName() + " start");
			Random random = new Random();
			int _int = random.nextInt(200);
			System.out.println(Thread.currentThread().getName() + " sleep " + _int + "millis");
			try {
				Thread.sleep(_int);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " end");
			doneSignal.countDown();
		}
	}

}
