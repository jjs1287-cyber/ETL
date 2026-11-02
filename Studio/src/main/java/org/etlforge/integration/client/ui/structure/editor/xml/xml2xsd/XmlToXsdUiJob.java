package org.etlforge.integration.client.ui.structure.editor.xml.xml2xsd;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdUiJob;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFile;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileFactory;
import org.etlforge.xml.xsd.GeneratedXsds;
import org.etlforge.xml.xsd.XsdGenerator;

public final class XmlToXsdUiJob extends CreateXsdUiJob {
   public XmlToXsdUiJob(SampleFileFactory var1) {
      super(var1);
   }

   @Override
   protected GeneratedXsds generateXsds(SampleFile var1) throws Exception {
      XsdGenerator var2 = new XsdGenerator(var1.getFile());
      String var3 = this.getXsdFileName();
      if (StringUtils.isBlank(var3)) {
         var3 = var1.getSuggestedXsdName();
         this.setXsdFileName(var3);
      }

      var2.setXsdFileName(var3);
      var2.setIncludeMainXsdNameInSecondarySchemaNames(true);
      return var2.generate();
   }
}
