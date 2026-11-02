package org.etlforge.integration.client.ldap.ui;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.ui.util.KeyValueToolTip;

public final class ClassDefinitionToolTip {
   private final String tooltip;

   public ClassDefinitionToolTip(ObjectClassDefinition var1) {
      this(var1, null);
   }

   public ClassDefinitionToolTip(ObjectClassDefinition var1, String var2) {
      this.tooltip = create(var1, var2);
   }

   public String get() {
      return this.tooltip;
   }

   private static String create(ObjectClassDefinition var0, String var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();
      var2.put(PackageResources.AttributeViewer.NAME, var0.getName());
      var2.put(PackageResources.AttributeViewer.OID, var0.getOid());
      String var3 = var0.getDescription();
      if (var3 != null && var3.length() > 0) {
         var2.put(PackageResources.AttributeViewer.DESC, var3);
      }

      return KeyValueToolTip.createTable(var2, var1);
   }
}
