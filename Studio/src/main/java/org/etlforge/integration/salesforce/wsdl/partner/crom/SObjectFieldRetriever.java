package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class SObjectFieldRetriever {
   private final CROM_Tree cromTree;

   public SObjectFieldRetriever(CROM_Tree var1) {
      this.cromTree = var1;
   }

   public List<CROM> listFields(String var1) {
      String var2 = this.getExpandedName(var1);
      CROM var3 = this.cromTree.getTypeDefinition(var2);
      return var3 == null ? Lists.newArrayList() : Lists.newArrayList(var3.getChildren());
   }

   private String getExpandedName(String var1) {
      ExpandedName var2 = XmlNameFactory.nonConforming().newExpandedName("urn:sobject.partner.soap.sforce.com", var1);
      return var2.toString();
   }
}
