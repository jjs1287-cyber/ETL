package org.etlforge.integration.client.ldap.ui.auxiliary;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ldap.ui.AttributeNode;
import org.etlforge.integration.client.ldap.ui.ClassStructureToolTip;
import org.etlforge.integration.client.ldap.ui.ReadOnlyAttributesDisabledCaption;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.ClassStructureComparators;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.check.CheckBoxTree;
import org.etlforge.ui.widget.tree.check.DefaultCheckBoxNode;

public final class AuxiliaryClassSelectionTree implements UiProvider {
   private final AuxiliaryClassSelectionTree.Model model;
   private final CheckBoxTree tree;
   private final String title;
   private final boolean disableReadOnlyAttrs;

   public AuxiliaryClassSelectionTree(ObjectClassStructure[] var1, boolean var2, String var3) {
      Preconditions.checkNotNull(var1, "auxStructs");
      this.title = var3;
      this.disableReadOnlyAttrs = var2;
      this.model = new AuxiliaryClassSelectionTree.Model(var1, var2);
      this.tree = this.createTree(this.model);
   }

   public void setSelectedClasses(NumericOid[] var1) {
      AuxiliaryClassSelectionTree.ClassNode[] var2 = this.model.getNodes(var1);
      this.tree.setCheckedNodes(var2);
   }

   public List<ObjectClassStructure> getSelectedStructures() {
      ArrayList var1 = new ArrayList();

      for (KongaTreeNode var5 : this.tree.getCheckedNodes()) {
         var1.add(((AuxiliaryClassSelectionTree.ClassNode)var5).getUserObject());
      }

      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      TreePanel var2 = new TreePanel(this.title, this.tree.getAdaptee());
      var2.setDropdownActions(new Action[]{this.tree.getCheckAllAction(null), this.tree.getUncheckAllAction(null)});
      if (this.disableReadOnlyAttrs) {
         this.decorateWithReadOnlyInfo(var2, var1);
      } else {
         var2.decorate(var1);
      }
   }

   private void decorateWithReadOnlyInfo(TreePanel var1, UiCanvas var2) {
      JPanel var3 = BorderLayoutBuilder.fillWith(var1, 0, 5);
      ReadOnlyAttributesDisabledCaption.addTo(var3, "South");
      var2.display(var3);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   private CheckBoxTree createTree(KongaTreeModel var1) {
      AuxiliaryClassSelectionTree.TreeImpl var2 = new AuxiliaryClassSelectionTree.TreeImpl(var1);
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.setCellRenderer(new TreeNodeDecorRenderer());
      return new CheckBoxTree(var2);
   }

   private static final class ClassNode extends DefaultCheckBoxNode implements TreeNodeDecorator {
      private final String display;
      private final String tooltip;
      private TextDecorator textStyle;

      private ClassNode(KongaTreeNode var1, ObjectClassStructure var2, boolean var3) {
         super(var1, var2, true);
         this.display = LdapUtils.getRepresentation(var2);
         this.tooltip = new ClassStructureToolTip(var2).get();
         this.addAttributeNodes(var2, var3);
      }

      private void addAttributeNodes(ObjectClassStructure var1, boolean var2) {
         LdapStructureAttribute[] var3 = var1.getAttributes();
         Arrays.sort(var3, AttributeComparators.nameOrder(true));

         for (LdapStructureAttribute var7 : var3) {
            AttributeNode var8 = AttributeNode.create(this, var7);
            if (var2 && var7.isReadOnly()) {
               var8.setEnabled(false);
            }
         }
      }

      public ObjectClassStructure getUserObject() {
         return (ObjectClassStructure)super.getUserObject();
      }

      @Override
      public void decorate(final TreeNodeDecor var1) {
         var1.setText(this.display);
         var1.setToolTip(this.tooltip);
         if (this.textStyle == null) {
            this.textStyle = new TextDecorator() {
               @Override
               public Font getFont() {
                  return var1.getTree().getFont();
               }

               @Override
               public Color getTextColor() {
                  return new Color(0, 0, 125);
               }
            };
         }

         var1.setTextStyle(this.textStyle);
         var1.setIcon(null);
      }
   }

   private static class Model extends DefaultKongaTreeModel {
      private final Map<NumericOid, AuxiliaryClassSelectionTree.ClassNode> classNodes = Maps.newHashMap();

      public Model(ObjectClassStructure[] var1, boolean var2) {
         super(new DefaultKongaTreeNode("root", true));
         this.addClassNodes(var1, var2);
      }

      private void addClassNodes(ObjectClassStructure[] var1, boolean var2) {
         KongaTreeNode var3 = this.getRoot();
         Arrays.sort(var1, ClassStructureComparators.BY_NAME_IGNORE_CASE);

         for (ObjectClassStructure var7 : var1) {
            AuxiliaryClassSelectionTree.ClassNode var8 = new AuxiliaryClassSelectionTree.ClassNode(var3, var7, var2);
            this.classNodes.put(var7.getOid(), var8);
         }
      }

      public AuxiliaryClassSelectionTree.ClassNode[] getNodes(NumericOid[] var1) {
         ArrayList var2 = new ArrayList();

         for (NumericOid var6 : var1) {
            AuxiliaryClassSelectionTree.ClassNode var7 = this.classNodes.get(var6);
            if (var7 != null) {
               var2.add(var7);
            }
         }

         return (AuxiliaryClassSelectionTree.ClassNode[]) var2.toArray(new AuxiliaryClassSelectionTree.ClassNode[var2.size()]);
      }
   }

   private static class TreeImpl extends KongaTree {
      TreeImpl(KongaTreeModel var1) {
         super(var1);
      }

      @Override
      public Dimension getPreferredSize() {
         return DimensionUtils.withWidthAtLeast(super.getPreferredSize(), 600);
      }
   }
}
