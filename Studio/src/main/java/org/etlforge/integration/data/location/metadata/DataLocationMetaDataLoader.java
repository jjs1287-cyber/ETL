package org.etlforge.integration.data.location.metadata;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class DataLocationMetaDataLoader {
   private static final String XML_RESOURCE = "Data.LocationTypes.xml";
   private LinkedHashSet<DataLocationType> sourceTypes;
   private LinkedHashSet<DataLocationType> targetTypes;

   public void load() throws DataLocationMetaDataXmlException {
      try {
         Document var1 = DomParser.parseResource("Data.LocationTypes.xml");
         this.sourceTypes = Sets.newLinkedHashSet();
         this.targetTypes = Sets.newLinkedHashSet();

         for (Element var3 : Elements.matching("DataLocations/DataLocation", var1)) {
            this.processType(var3);
         }
      } catch (Exception var4) {
         throw new DataLocationMetaDataXmlException(var4);
      }
   }

   private void processType(Element var1) {
      DataLocationType var2 = DataLocationType.valueOf(KongaXmlUtils.getChildElementValue(var1, "LocationType"));
      if (isYes(var1, "source")) {
         this.sourceTypes.add(var2);
      }

      if (isYes(var1, "target")) {
         this.targetTypes.add(var2);
      }
   }

   private static boolean isYes(Element var0, String var1) {
      return "yes".equals(var0.getAttribute(var1));
   }

   public DataLocationType[] getSupportedSourceTypes() {
      return toArray(this.sourceTypes);
   }

   private static DataLocationType[] toArray(Set<DataLocationType> var0) {
      return var0.toArray(new DataLocationType[var0.size()]);
   }

   public boolean isSupportedSourceType(DataLocationType var1) {
      return this.sourceTypes.contains(var1);
   }

   public DataLocationType[] getSupportedTargetTypes() {
      return toArray(this.targetTypes);
   }

   public boolean isSupportedTargetType(DataLocationType var1) {
      return this.targetTypes.contains(var1);
   }
}
