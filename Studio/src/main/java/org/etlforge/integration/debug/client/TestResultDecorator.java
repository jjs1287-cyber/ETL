package org.etlforge.integration.debug.client;

import com.google.common.base.Preconditions;
import java.util.Collection;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.de.DataElement;

public abstract class TestResultDecorator implements TestResult {
   private final TestResult decoratee;

   protected TestResultDecorator(TestResult var1) {
      this.decoratee = Preconditions.checkNotNull(var1);
   }

   @Override
   public DebugSession getSession() {
      return this.decoratee.getSession();
   }

   @Override
   public boolean isDone() {
      return this.decoratee.isDone();
   }

   @Override
   public IntegrationEntityId getDebuggeeId() {
      return this.decoratee.getDebuggeeId();
   }

   @Override
   public IntegrationEntityId getIdOfEntityToDisplay() {
      return this.decoratee.getIdOfEntityToDisplay();
   }

   @Override
   public Collection<DataElement> getDataElements() {
      return this.decoratee.getDataElements();
   }

   @Override
   public BreakLocation getCurrentLocation() {
      return this.decoratee.getCurrentLocation();
   }

   @Override
   public BreakLocation[] getCallStack() {
      return this.decoratee.getCallStack();
   }

   @Override
   public String getResult() {
      return this.decoratee.getResult();
   }

   @Override
   public String getWarnings() {
      return this.decoratee.getWarnings();
   }

   @Override
   public String getErrors() {
      return this.decoratee.getErrors();
   }
}
