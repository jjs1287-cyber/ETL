package org.etlforge.integration.debug.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.util.id.KongaGuid;

public final class DebuggingParametersBuilder {
   private KongaGuid sessionId;
   private String command;
   private List<DataElement> dataElements;
   private final List<Breakpoint> breakpoints;
   private boolean observeBreakpoints;
   private boolean breakOnWarnings;
   private boolean breakOnErrors;

   public DebuggingParametersBuilder() {
      this.observeBreakpoints = true;
      this.dataElements = Lists.newArrayList();
      this.breakpoints = Lists.newArrayList();
   }

   public DebuggingParametersBuilder(DebuggingParameters var1) {
      this.sessionId = var1.getSessionId();
      this.command = var1.getCommand();
      this.observeBreakpoints = var1.isObservingBreakpoints();
      this.breakOnErrors = var1.isBreakingOnErrors();
      this.breakOnWarnings = var1.isBreakingOnWarnings();
      this.dataElements = Lists.newArrayList(var1.getDataElements());
      this.breakpoints = Lists.newArrayList(var1.getBreakpoints());
   }

   public DebuggingParameters build() {
      Preconditions.checkState(this.sessionId != null, "A session ID must be provided first");
      Preconditions.checkState(this.command != null, "A command must be provided first");
      return new DebuggingParameters() {
         @Override
         public Breakpoint[] getBreakpoints() {
            return DebuggingParametersBuilder.this.breakpoints.toArray(new Breakpoint[DebuggingParametersBuilder.this.breakpoints.size()]);
         }

         @Override
         public String getCommand() {
            return DebuggingParametersBuilder.this.command;
         }

         @Override
         public DataElement[] getDataElements() {
            return DebuggingParametersBuilder.this.dataElements.toArray(new DataElement[DebuggingParametersBuilder.this.dataElements.size()]);
         }

         @Override
         public DataElement[] getGlobalDataElements() {
            Collection var1 = Collections2.filter(DebuggingParametersBuilder.this.dataElements, DataElementScope.GLOBAL);
            return (DataElement[]) var1.toArray(new DataElement[var1.size()]);
         }

         @Override
         public KongaGuid getSessionId() {
            return DebuggingParametersBuilder.this.sessionId;
         }

         @Override
         public boolean isBreakingOnErrors() {
            return DebuggingParametersBuilder.this.breakOnErrors;
         }

         @Override
         public boolean isBreakingOnWarnings() {
            return DebuggingParametersBuilder.this.breakOnWarnings;
         }

         @Override
         public boolean isObservingBreakpoints() {
            return DebuggingParametersBuilder.this.observeBreakpoints;
         }
      };
   }

   public final DebuggingParametersBuilder setSessionId(KongaGuid var1) {
      this.sessionId = var1;
      return this;
   }

   public final DebuggingParametersBuilder setCommand(String var1) {
      this.command = var1;
      return this;
   }

   public final DebuggingParametersBuilder setObserveBreakpoints(boolean var1) {
      this.observeBreakpoints = var1;
      return this;
   }

   public final DebuggingParametersBuilder setBreakOnWarnings(boolean var1) {
      this.breakOnWarnings = var1;
      return this;
   }

   public final DebuggingParametersBuilder setBreakOnErrors(boolean var1) {
      this.breakOnErrors = var1;
      return this;
   }

   public void setDataElements(Collection<DataElement> var1) {
      this.dataElements = Lists.newArrayList(var1);
   }

   public DebuggingParametersBuilder addBreakpoint(Breakpoint var1) {
      this.breakpoints.add(var1);
      return this;
   }

   public DebuggingParametersBuilder removeAllBreakpoints() {
      this.breakpoints.clear();
      return this;
   }
}
