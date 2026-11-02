package org.etlforge.integration.data.entity.operation.pipeline;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

public class PipelineActivityEvent extends EventObject {
   private final PropertyChangeEvent propChangeEvent;

   public PipelineActivityEvent(PipelineActivity var1) {
      this(var1, null);
   }

   public PipelineActivityEvent(PipelineActivity var1, PropertyChangeEvent var2) {
      super(var1);
      this.propChangeEvent = var2;
   }

   public PipelineActivity getSource() {
      return (PipelineActivity)super.getSource();
   }

   public PropertyChangeEvent getChange() {
      return this.propChangeEvent;
   }
}
