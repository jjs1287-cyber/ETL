package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.system.Disposable;
import org.etlforge.util.id.KongaGuid;

public interface PipelineActivity<T> extends Cloneable, Disposable {
   String getRole();

   @Override
   boolean equals(Object var1);

   @Override
   int hashCode();

   KongaGuid getId();

   PipelineActivityType getType();

   boolean isEmpty();

   T getContent();

   void removeContent();

   PipelineActivity<T> clone();

   boolean isReadOnly();

   boolean canHaveScriptedLinks();

   void addListener(PipelineActivityListener var1);

   void removeListener(PipelineActivityListener var1);

   void removeAllListeners();
}
