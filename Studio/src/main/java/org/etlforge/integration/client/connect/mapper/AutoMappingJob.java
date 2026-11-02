package org.etlforge.integration.client.connect.mapper;

import java.util.concurrent.CountDownLatch;

public interface AutoMappingJob extends Runnable {
   void setCompletionLatch(CountDownLatch var1);
}
