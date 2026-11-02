package org.etlforge.integration.client.ui.interchange.entity.document;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.Segment;

final class TextDocumentSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      TextStructure var2 = ((TextDocument)var1).getDataStructure();
      ArrayList var3 = Lists.newArrayList();

      for (Segment var5 : var2.getSegmentList()) {
         var3.add(var5.getName());

         for (Field var7 : var5) {
            var3.add(var7.getName());
         }
      }

      return var3;
   }
}
