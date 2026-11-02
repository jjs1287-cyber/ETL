package org.etlforge.integration.client.ldap.ui;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.util.KeyValueToolTip;

public final class AttributeToolTip {
   private final String tooltip;

   public AttributeToolTip(LdapStructureAttribute var1) {
      this.tooltip = create(var1);
   }

   public String get() {
      return this.tooltip;
   }

   private static String create(LdapStructureAttribute var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      var1.put(PackageResources.AttributeViewer.NAME, var0.getName());
      var1.put(PackageResources.AttributeViewer.OID, var0.getOid());
      String var2 = var0.getDescription();
      if (var2 != null && var2.length() > 0) {
         var1.put(PackageResources.AttributeViewer.DESC, var2);
      }

      var1.put(PackageResources.AttributeViewer.USE, var0.getUse());
      var1.put(PackageResources.AttributeViewer.CARDINALITY, var0.getCardinality());
      var1.put(PackageResources.AttributeViewer.READ_ONLY, PackageResources.AttributeViewer.getReadOnlyValue(var0));
      return KeyValueToolTip.createTable(var1);
   }
}
