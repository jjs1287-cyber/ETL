package org.etlforge.integration.client.server.webservice;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

class WebServiceAppender implements Appender {
   @Override
   public void addFilter(Filter var1) {
   }

   @Override
   public Filter getFilter() {
      return null;
   }

   @Override
   public void clearFilters() {
   }

   @Override
   public void close() {
   }

   @Override
   public void doAppend(LoggingEvent var1) {
   }

   @Override
   public String getName() {
      return null;
   }

   @Override
   public void setErrorHandler(ErrorHandler var1) {
   }

   @Override
   public ErrorHandler getErrorHandler() {
      return null;
   }

   @Override
   public void setLayout(Layout var1) {
   }

   @Override
   public Layout getLayout() {
      return null;
   }

   @Override
   public void setName(String var1) {
   }

   @Override
   public boolean requiresLayout() {
      return false;
   }
}
