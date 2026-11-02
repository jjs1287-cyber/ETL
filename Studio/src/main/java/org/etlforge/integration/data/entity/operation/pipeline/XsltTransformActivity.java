package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.WeakEntityLink;
import org.etlforge.util.id.KongaGuid;

public final class XsltTransformActivity extends EntityActivity<XsltTransform> {
   public XsltTransformActivity(String var1) {
      super(PipelineActivityType.XSLT_TRANSFORM, XsltTransform.class, var1);
   }

   public XsltTransformActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.XSLT_TRANSFORM, XsltTransform.class, var1, var2);
   }

   private XsltTransformActivity(XsltTransform var1, String var2) {
      super(PipelineActivityType.XSLT_TRANSFORM, XsltTransform.class, var1, var2);
   }

   public static XsltTransformActivity newInstance(XsltTransform var0, String var1) {
      return var0 != null ? new XsltTransformActivity(var0, var1) : new XsltTransformActivity(var1);
   }

   public XsltTransformActivity clone() {
      return (XsltTransformActivity)super.clone();
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
      return EntityType.XsltTransform.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.XsltTransform.getPlural();
   }
}
