package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.WeakEntityLink;
import org.etlforge.util.id.KongaGuid;

public final class ScriptActivity extends EntityActivity<Script> {
   public ScriptActivity(String var1) {
      super(PipelineActivityType.SCRIPT, Script.class, var1);
   }

   public ScriptActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.SCRIPT, Script.class, var1, var2);
   }

   private ScriptActivity(Script var1, String var2) {
      super(PipelineActivityType.SCRIPT, Script.class, var1, var2);
   }

   public static ScriptActivity newInstance(Script var0, String var1) {
      return var0 != null ? new ScriptActivity(var0, var1) : new ScriptActivity(var1);
   }

   public ScriptActivity clone() {
      return (ScriptActivity)super.clone();
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new WeakEntityLink(this.getContent(), var1);
   }

   @Override
   public boolean canHaveScriptedLinks() {
      return true;
   }

   @Override
   public String getTypeName() {
      return EntityType.Script.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.Script.getPlural();
   }
}
