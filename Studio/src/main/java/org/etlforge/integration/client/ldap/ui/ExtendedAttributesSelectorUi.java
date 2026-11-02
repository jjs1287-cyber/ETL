package org.etlforge.integration.client.ldap.ui;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.ldap.structure.AttributeCardinality;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.AttributeDataType;
import org.etlforge.integration.ldap.structure.AttributeSyntaxInfo;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureAttributeBuilder;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableEnumEditor;
import org.etlforge.ui.widget.table.TableStringEditor;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class ExtendedAttributesSelectorUi implements UiProvider {
   private ExtendedAttributesSelectorUi.Model model = new ExtendedAttributesSelectorUi.Model();
   private final KongaRowTable table = this.createTable();

   private KongaRowTable createTable() {
      KongaTable var1 = ComponentFactories.tableFactory().newTable(this.model, 10);
      var1.setTerminateEditOnFocusLost(true);
      this.installEditors(var1);
      TableStyles.wide().makeOver(var1);
      return new KongaRowTable(var1);
   }

   private void installEditors(KongaTable var1) {
      var1.setCellEditorFor(ExtendedAttributesSelectorUi.Model.NAME, new TableStringEditor());
      var1.setCellEditorFor(ExtendedAttributesSelectorUi.Model.DATA_TYPE, new TableEnumEditor(AttributeDataType.class));
      var1.setCellEditorFor(ExtendedAttributesSelectorUi.Model.USE, new TableEnumEditor(AttributeUse.class));
      var1.setCellEditorFor(ExtendedAttributesSelectorUi.Model.CARDINALITY, new TableEnumEditor(AttributeCardinality.class));
   }

   public void setAttributes(LdapStructureAttribute[] var1) {
      Arrays.sort(var1, AttributeComparators.nameOrder(true));
      this.model = new ExtendedAttributesSelectorUi.Model(var1);
      this.table.setModel(this.model);
   }

   public LdapStructureAttribute[] getAttributes() {
      this.stopEditing();
      return this.model.getDefinedAttributes();
   }

   public boolean isValid() {
      this.stopEditing();
      return this.model.isValid();
   }

   private void stopEditing() {
      if (this.table.getAdaptee().isEditing()) {
         this.table.getAdaptee().getCellEditor().stopCellEditing();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(this.controlPanel(), this.table).decorate(var1);
   }

   private JComponent controlPanel() {
      ExtendedAttributesSelectorUi.AddNewAttributeAction var1 = new ExtendedAttributesSelectorUi.AddNewAttributeAction();
      ExtendedAttributesSelectorUi.DeleteAttributesAction var2 = new ExtendedAttributesSelectorUi.DeleteAttributesAction();
      var2.setSelectionSource(this.table);
      return Horizontal.grid(10).of(var1, var2).invisible();
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private class AddNewAttributeAction extends AbstractAction {
      public AddNewAttributeAction() {
         super("Add &new attribute");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddNewAttributeAction.this.addAttribute();
            }
         });
      }

      private void addAttribute() {
         LdapStructureAttribute var1 = this.buildNewAttribute();
         ExtendedAttributesSelectorUi.this.model.addRow(var1);
         int var2 = ExtendedAttributesSelectorUi.this.model.getRowCount() - 1;
         ExtendedAttributesSelectorUi.this.table.getAdaptee().editCellAt(var2, ExtendedAttributesSelectorUi.Model.NAME);
      }

      private LdapStructureAttribute buildNewAttribute() {
         LdapStructureAttributeBuilder var1 = new LdapStructureAttributeBuilder();
         var1.setOid(NumericOid.UNKNOWN);
         var1.setName("");
         var1.setUse(AttributeUse.OPTIONAL);
         var1.setCardinality(AttributeCardinality.ONE);
         var1.setSyntaxInfo(new ExtendedAttributesSelectorUi.SyntaxInfoImpl(AttributeDataType.STRING));
         return var1.getAttribute();
      }
   }

   private class DeleteAttributesAction extends AbstractSelectionAction {
      public DeleteAttributesAction() {
         super("&Delete selected attributes");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DeleteAttributesAction.this.deleteSelectedRows();
            }
         });
      }

      private void deleteSelectedRows() {
         int[] var1 = ExtendedAttributesSelectorUi.this.table.getAdaptee().getSelectedRows();

         for (int var2 = var1.length - 1; var2 >= 0; var2--) {
            ExtendedAttributesSelectorUi.this.model.deleteRow(var1[var2]);
         }
      }
   }

   private static class Model extends TypedAbstractKongaTableModel<LdapStructureAttribute> {
      public static final TableColumnMetaData NAME = new TableColumnMetaData("Name", String.class, true, 200);
      public static final TableColumnMetaData DATA_TYPE = new TableColumnMetaData("Data Type", AttributeDataType.class, true, 100);
      public static final TableColumnMetaData USE = new TableColumnMetaData("Use", AttributeUse.class, true, 100);
      public static final TableColumnMetaData CARDINALITY = new TableColumnMetaData("Cardinality", AttributeCardinality.class, true, 100);

      public Model() {
         super(LdapStructureAttribute.class, NAME, DATA_TYPE, USE, CARDINALITY);
      }

      public Model(LdapStructureAttribute[] var1) {
         this();
         this.addRows(var1);
      }

      protected Object[] getColumnDataForTypedObject(LdapStructureAttribute var1) {
         return new Object[]{var1.getName(), var1.getSyntaxInfo().getDataType(), var1.getUse(), var1.getCardinality()};
      }

      public boolean isValid() {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            String var2 = this.getAttributeName(var1);
            if (StringUtils.isBlank(var2)) {
               return false;
            }
         }

         return true;
      }

      private String getAttributeName(int var1) {
         return (String)this.getValueAt(var1, NAME);
      }

      public LdapStructureAttribute[] getDefinedAttributes() {
         ArrayList var1 = Lists.newArrayList();

         for (int var2 = 0; var2 < this.getRowCount(); var2++) {
            LdapStructureAttribute var3 = this.buildAttributeFromRow(var2);
            var1.add(var3);
         }

         return (LdapStructureAttribute[]) var1.toArray(new LdapStructureAttribute[var1.size()]);
      }

      private LdapStructureAttribute buildAttributeFromRow(int var1) {
         LdapStructureAttributeBuilder var2 = new LdapStructureAttributeBuilder();
         var2.setOid(NumericOid.UNKNOWN);
         var2.setName(this.getAttributeName(var1));
         var2.setUse((AttributeUse)this.getValueAt(var1, USE));
         var2.setCardinality((AttributeCardinality)this.getValueAt(var1, CARDINALITY));
         AttributeDataType var3 = (AttributeDataType)this.getValueAt(var1, DATA_TYPE);
         var2.setSyntaxInfo(new ExtendedAttributesSelectorUi.SyntaxInfoImpl(var3));
         return var2.getAttribute();
      }
   }

   private static class SyntaxInfoImpl implements AttributeSyntaxInfo {
      private final AttributeDataType type;

      public SyntaxInfoImpl(AttributeDataType var1) {
         this.type = var1;
      }

      @Override
      public AttributeDataType getDataType() {
         return this.type;
      }

      @Override
      public String getDescription() {
         return null;
      }

      @Override
      public String getOid() {
         return NumericOid.UNKNOWN.toString();
      }
   }
}
