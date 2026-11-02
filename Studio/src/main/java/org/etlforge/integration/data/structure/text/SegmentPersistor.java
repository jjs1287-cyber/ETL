package org.etlforge.integration.data.structure.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.etlforge.util.persist.Persistor;

public class SegmentPersistor {
   private static final String NAME = "name";
   private static final String PARENT = "parent";
   private static final String ID = "segmentId";
   private static final String OCCURRENCE_TYPE = "occurenceType";
   private static final String FIELDS = "Fields";
   private static final String FIELD = "Field";
   private final Map<String, Segment> encounteredSegments = Maps.newHashMap();

   public void save(Segment var1, Persistor var2) {
      var2.putString("name", var1.getName());
      var2.putString("parent", var1.getParentName());
      var2.putInteger("segmentId", var1.getId());
      var2.putString("occurenceType", var1.getOccurrence());
      if (!this.encounteredSegments.containsKey(var1.getName())) {
         this.saveFields(var1, var2);
         this.encounteredSegments.put(var1.getName(), var1);
      }
   }

   private void saveFields(Segment var1, Persistor var2) {
      List var3 = var1.getFieldList();
      if (var3 != null) {
         Persistor var4 = var2.createChild("Fields");

         for (Object __etlItem36 : var3) {
            Field var6 = (Field) __etlItem36;
            FieldPersistor.save(var6, var4.createChild("Field"));
         }
      }
   }

   public Segment restoreFrom(Persistor var1) {
      Segment var2 = new Segment(var1.getString("name"));
      var2.setParentName(var1.getString("parent"));
      var2.setId(var1.getInteger("segmentId"));
      var2.setOccurrence(var1.getString("occurenceType"));
      Segment var3 = this.encounteredSegments.get(var2.getName());
      if (var3 == null) {
         var2.fieldList = this.restoreFields(var1);
         this.encounteredSegments.put(var2.getName(), var2);
      } else {
         var2.alias = var3;
         var2.fieldList = new Vector<>(var3.fieldList);
      }

      return var2;
   }

   private List<Field> restoreFields(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Fields");
      if (var2 == null) {
         return null;
      }

      ArrayList var3 = Lists.newArrayList();

      for (Persistor var5 : var2.getChildren("Field")) {
         var3.add(FieldPersistor.restoreFrom(var5));
      }

      return var3;
   }
}
