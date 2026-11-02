package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.FillDataElementKit;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.lang.KongaStringUtils;

public final class SourceDataElementCreator {
   private final String baseName;
   private final Node node;
   private final String dataTypeStr;
   private final String formatStr;
   private final int dbColType;
   private final FillDataElementKit kit;

   public SourceDataElementCreator(String var1, TreeComponent var2, FillDataElementKit var3) {
      this.baseName = var1;
      this.node = var2.m_deHashtable.get(ScriptUtils.stripInstanceTag(var1));
      if (this.node != null) {
         this.dataTypeStr = this.getDataTypeString();
         this.formatStr = this.getFormatString();
         this.dbColType = this.node.getDbColType();
      } else {
         this.dataTypeStr = null;
         this.formatStr = null;
         this.dbColType = 0;
      }

      this.kit = var3;
   }

   public DataElement build() {
      if (this.node == null) {
         return null;
      }

      String var1 = this.kit.getDEValue(this.baseName);
      DataElement var2 = new DataElement(this.baseName, DataElementScope.SOURCE, this.dataTypeStr);
      var2.setValue(var1 != null ? var1 : "");
      var2.setDisplayName(this.createName());
      return var2;
   }

   private String getDataTypeString() {
      String var1 = this.node.getDataType();
      if (var1 == null || var1.equals("")) {
         var1 = "String";
      }

      return var1;
   }

   private String getFormatString() {
      return StringUtils.isNotEmpty(this.node.getDomainString()) ? this.node.getDomainString() : this.node.getFormatString();
   }

   private String createName() {
      String var1 = this.baseName;
      var1 = var1 + "(" + this.dataTypeStr;
      if (this.dbColType != 0) {
         var1 = var1 + "<" + this.dbColType + ">";
      }

      if (StringUtils.isNotEmpty(this.formatStr)) {
         var1 = var1 + ",\"" + KongaStringUtils.escapeDoubleQuotes(this.formatStr) + "\"";
      }

      return var1 + ")";
   }
}
