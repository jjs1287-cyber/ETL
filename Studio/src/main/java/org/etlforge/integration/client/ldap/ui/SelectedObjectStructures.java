package org.etlforge.integration.client.ldap.ui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public final class SelectedObjectStructures {
   private final SelectedObjectClasses classes;
   private final Map<ObjectClassDefinition, ObjectClassStructure> structures;

   public SelectedObjectStructures(SelectedObjectClasses var1) {
      Preconditions.checkNotNull(var1, "classes");
      this.classes = var1;
      this.structures = Maps.newHashMap();
   }

   public void addStructure(NumericOid var1, ObjectClassStructure var2) {
      NumericOid var3 = var2 != null ? var2.getOid() : var1;
      ObjectClassDefinition var4 = this.classes.getClassDefinition(var3);
      Preconditions.checkArgument(var4 != null, "Unknown class: " + var3);
      this.structures.put(var4, var2);
   }

   public List<Entry<ObjectClassDefinition, ObjectClassStructure>> getStructures() {
      LinkedHashMap var1 = Maps.newLinkedHashMap();

      for (ObjectClassDefinition var5 : this.classes.getClassDefinitions()) {
         var1.put(var5, this.structures.get(var5));
      }

      return new ArrayList<>(var1.entrySet());
   }

   public List<Entry<ObjectClassDefinition, ObjectClassStructure>> getStructuresAlphabetically() {
      Comparator var1 = new Comparator<ObjectClassDefinition>() {
         public int compare(ObjectClassDefinition var1, ObjectClassDefinition var2) {
            String var3 = var1.getName();
            if (var3 == null) {
               var3 = var1.getOid().toString();
            }

            String var4 = var2.getName();
            if (var4 == null) {
               var4 = var2.getOid().toString();
            }

            return var3.compareToIgnoreCase(var4);
         }
      };
      TreeMap var2 = Maps.newTreeMap(var1);

      for (ObjectClassDefinition var6 : this.classes.getClassDefinitions()) {
         var2.put(var6, this.structures.get(var6));
      }

      return new ArrayList<>(var2.entrySet());
   }

   public ObjectClassStructure getStructure(NumericOid var1) {
      ObjectClassDefinition var2 = this.classes.getClassDefinition(var1);
      return var2 != null ? this.structures.get(var2) : null;
   }
}
