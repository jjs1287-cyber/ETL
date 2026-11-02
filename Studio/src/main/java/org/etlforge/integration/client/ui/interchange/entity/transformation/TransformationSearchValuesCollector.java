package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.XmlStructure;

final class TransformationSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      Transformation var2 = (Transformation)var1;
      ArrayList var3 = Lists.newArrayList();
      collect(var2.getInputStructure(), var3);
      collect(var2.getOutputStructure(), var3);
      return var3;
   }

   private static void collect(DataStructure var0, List<String> var1) {
      switch (var0.getStructureType()) {
         case XML:
            XmlStructure var2 = (XmlStructure)var0;
            var1.add(var2.getFilePath());
            var1.add(var2.getRootNodeName());
            break;
         case Database:
            DatabaseStructure var3 = (DatabaseStructure)var0;
            if (var3.isManualSqlStatement()) {
               var1.add(var3.getManualSqlStatement());
            }
            break;
         case LDAP:
            LdapStructure var4 = (LdapStructure)var0;
            var1.addAll(var4.getSelectedAttributeNames());
            break;
         case JSON:
            JsonStructure var5 = (JsonStructure)var0;
            var1.add(var5.getFileName());
         case Text:
         case WebServiceRequest:
         case WebServiceResponse:
      }
   }
}
