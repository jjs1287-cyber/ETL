package org.etlforge.integration.client.connect.db2xml.wizard;

import java.util.concurrent.Callable;

final class XsdProducer implements Callable<DbToXsdFactory.XSD> {
   private final XsdGeneratorModel model;

   public XsdProducer(XsdGeneratorModel var1) {
      this.model = var1;
   }

   public DbToXsdFactory.XSD call() throws Exception {
      DbToXsdFactory var1 = new DbToXsdFactory(this.model.getRootName());
      var1.setTargetNamespace(this.model.getTargetNamespace());
      var1.setIncludeDefaultValues(this.model.isIncludeDefaultsSelected());
      DbToXsdFactory.XSD var2 = var1.buildXsd(this.model.getDbStructure());
      this.model.setContent(var2.contents);
      return var2;
   }
}
