package org.etlforge.integration.client.ldap.ui;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.util.KeyValueToolTip;

public final class ClassStructureToolTip {
   private final String tooltip;

   public ClassStructureToolTip(ObjectClassStructure var1) {
      this.tooltip = create(var1);
   }

   public String get() {
      return this.tooltip;
   }

   private static String create(ObjectClassStructure var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      var1.put(PackageResources.AttributeViewer.NAME, var0.getName());
      var1.put(PackageResources.AttributeViewer.OID, var0.getOid());
      return KeyValueToolTip.createTable(var1);
   }
}
