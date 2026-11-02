package org.etlforge.integration.client.plugin.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.Collection;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.dataelement.DataElementType;
import org.etlforge.integration.plugin.PluginDataElementTemplate;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.string.StringTransforms;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class PluginDataElementTableModel extends TypedAbstractKongaTableModel<PluginDataElementTemplate> {
   static final TableColumnMetaData NAME = new TableColumnMetaData(
      PluginUiResources.STRINGS.getString("Plugin.DataElementTable.Column.Name"), String.class, false, 200
   );
   static final TableColumnMetaData TYPE = new TableColumnMetaData(
      PluginUiResources.STRINGS.getString("Plugin.DataElementTable.Column.Type"), DataElementType.class, false, 100
   );
   static final TableColumnMetaData REQUIRED = new TableColumnMetaData(
      PluginUiResources.STRINGS.getString("Plugin.DataElementTable.Column.Required"), String.class, false, 100
   );
   static final TableColumnMetaData DOCUMENTATION = new TableColumnMetaData(
      PluginUiResources.STRINGS.getString("Plugin.DataElementTable.Column.Documentation"), String.class, false, 300
   );
   static final TableColumnMetaData DEFAULT_VALUE = new TableColumnMetaData(
      PluginUiResources.STRINGS.getString("Plugin.DataElementTable.Column.DefaultValue"), Object.class, false, 150
   );

   public PluginDataElementTableModel() {
      super(PluginDataElementTemplate.class, createColumnDescriptors());
   }

   public PluginDataElementTableModel(Collection<PluginDataElementTemplate> var1) {
      super(PluginDataElementTemplate.class, createColumnDescriptors(), var1);
   }

   private static TableColumnMetaData[] createColumnDescriptors() {
      return new TableColumnMetaData[]{NAME, TYPE, REQUIRED, DOCUMENTATION, DEFAULT_VALUE};
   }

   public PluginDataElementTableModel(PluginDataElementTemplate[] var1) {
      this();
      this.addRows(var1);
   }

   protected Object[] getColumnDataForTypedObject(PluginDataElementTemplate var1) {
      return new Object[]{
         var1.getName(),
         var1.getType(),
         var1.isRequired() ? UiResourceBundle.getString("Yes") : UiResourceBundle.getString("No"),
         getDescription(var1),
         var1.getDefaultValue()
      };
   }

   private static String getDescription(PluginDataElementTemplate var0) {
      String var1 = var0.getDescription();
      if (var1 != null && var1.length() != 0) {
         String var2 = KongaStringUtils.collapseSpaces(var1);
         Iterable var3 = Iterables.transform(Splitter.on("\n").split(var2), StringTransforms.TRIM);
         return Joiner.on("\n").join(var3);
      } else {
         return "";
      }
   }
}
