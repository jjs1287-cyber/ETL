package com.etlforge.integration.server.engine.bulkload.batch;

interface BatchSender {
   boolean isOpen();

   void forceClose();

   void sendBundle(String var1, int var2, int var3) throws Exception;

   BatchResponse terminateRequest() throws Exception;
}
