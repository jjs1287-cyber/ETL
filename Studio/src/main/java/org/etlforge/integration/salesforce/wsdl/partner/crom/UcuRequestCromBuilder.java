package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.AutoMappableFieldFilter;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.crom.CROM;

public final class UcuRequestCromBuilder extends AbstractCallStructureBuilder {
   private final SObjectSchema sobject;
   private final SfdcMethod sfdcMethod;
   private final boolean includeExternalField;
   private ImmutableList<RelatedObject> relatedObjects = ImmutableList.of();

   public UcuRequestCromBuilder(SfdcMethod var1, SObjectSchema var2) {
      super(AbstractCallStructureBuilder.Mode.REQUEST, getHeaderElements(var1));
      this.sfdcMethod = var1;
      this.includeExternalField = var1 == SfdcMethod.UPSERT;
      this.sobject = var2;
   }

   private static CromHeaderElement[] getHeaderElements(SfdcMethod var0) {
      ArrayList var1 = Lists.newArrayList(
         new SessionCromHeader(),
         new CallOptionsCromHeader(),
         new AssignmentRuleCromHeader(),
         new MruCromHeader(),
         new AllowFieldTruncationCromHeader(),
         new DisableFeedTrackingCromHeader(),
         new StreamingEnabledCromHeader(),
         new AllOrNoneCromHeader(),
         new DebuggingCromHeader(),
         new PackageVersionCromHeader(),
         new EmailCromHeader()
      );
      if (var0 == SfdcMethod.UPDATE || var0 == SfdcMethod.UPSERT) {
         var1.add(new OwnerChangeOptionsCromHeader());
      }

      return Iterables.toArray(var1, CromHeaderElement.class);
   }

   public void setRelatedObjects(List<RelatedObject> var1) {
      List var2 = sort(var1);
      this.relatedObjects = ImmutableList.copyOf(var2);
   }

   private static List<RelatedObject> sort(List<RelatedObject> var0) {
      ArrayList var1 = Lists.newArrayList(var0);
      Collections.sort(var1, RelatedObject.byRelationshipName());
      return var1;
   }

   @Override
   protected void populateBody(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, this.sfdcMethod.getMethodName());
      if (this.includeExternalField) {
         CROM var3 = createElementInPartnerNamespace(var2, "externalIDFieldName");
         createStringValueNode(var3);
      }

      CROM var4 = this.createObjectNode(var2);
      this.appendRelatedObjects(var4);
   }

   private CROM createObjectNode(CROM var1) {
      UcuRequestCromBuilder.RequestObjectBuilder var2 = new UcuRequestCromBuilder.RequestObjectBuilder(
         this.sobject, new AutoMappableFieldFilter(this.sfdcMethod)
      );
      return var2.build(var1);
   }

   private void appendRelatedObjects(CROM var1) {
      if (!this.relatedObjects.isEmpty()) {
         int var2 = 0;

         for (RelatedObject var4 : this.relatedObjects) {
            CROM var5 = RelatedObjectCromBuilder.build(var4);
            var1.insertChild(var2, var5);
            var2++;
         }
      }
   }

   private static class RequestObjectBuilder extends FlatObjectCromBuilder {
      private final Predicate<SObjectSchema.Field> autoMapCondition;

      public RequestObjectBuilder(SObjectSchema var1, Predicate<SObjectSchema.Field> var2) {
         super(var1);
         this.autoMapCondition = var2;
         this.setOptionalLoop(true);
      }

      @Override
      protected void customizeField(SObjectSchema.Field var1, CROM var2) {
         if (!this.autoMapCondition.apply(var1)) {
            long var3 = var2.getType();
            var3 |= 1024L;
            var2.setType(var3);
         }
      }
   }
}
