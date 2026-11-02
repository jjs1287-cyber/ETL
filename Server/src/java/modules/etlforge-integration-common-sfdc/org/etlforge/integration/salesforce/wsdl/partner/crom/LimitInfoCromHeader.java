package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.collect.ImmutableList;

public final class LimitInfoCromHeader extends CromHeaderElement {
   public LimitInfoCromHeader() {
      super(createPath(), CromHeaderValue.integer("current"), CromHeaderValue.integer("limit"), CromHeaderValue.string("type"));
   }

   private static ImmutableList<CromHeaderElement.Folder> createPath() {
      CromHeaderElement.Folder var0 = new CromHeaderElement.Folder("LimitInfoHeader", CromHeaderElement.Cardinality.SINGLE);
      CromHeaderElement.Folder var1 = new CromHeaderElement.Folder("limitInfo", CromHeaderElement.Cardinality.LOOP);
      return ImmutableList.of(var0, var1);
   }
}
