package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.PropertyBasedEntityLink;
import org.etlforge.util.id.KongaGuid;

public final class SourceActivity extends EntityActivity<Source> {
   public SourceActivity(String var1) {
      super(PipelineActivityType.SOURCE, Source.class, var1);
   }

   public SourceActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.SOURCE, Source.class, var1, var2);
   }

   private SourceActivity(Source var1, String var2) {
      super(PipelineActivityType.SOURCE, Source.class, var1, var2);
   }

   public static SourceActivity newInstance(Source var0, String var1) {
      return var0 != null ? new SourceActivity(var0, var1) : new SourceActivity(var1);
   }

   public SourceActivity clone() {
      return (SourceActivity)super.clone();
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new PropertyBasedEntityLink(this.getContent(), var1, "source_id");
   }

   @Override
   public String getTypeName() {
      return EntityType.Source.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.Source.getPlural();
   }
}
