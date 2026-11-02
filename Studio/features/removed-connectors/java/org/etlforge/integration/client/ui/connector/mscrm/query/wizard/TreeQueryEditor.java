package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.xml.transform.TransformerException;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.xml.AttributeTreeNode;
import org.etlforge.ui.xml.ElementTreeNode;
import org.etlforge.ui.xml.XmlTree;
import org.etlforge.ui.xml.XmlTreeModelBuilder;
import org.etlforge.util.Property;
import org.etlforge.xml.XmlPrettyPrinter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public final class TreeQueryEditor implements QueryEditor {
   private final XmlTree tree;
   private final KongaTextField conditionValueEditor = new KongaTextField();
   private final TreeQueryEditor.ApplyNewConditionAction applyValueAction = new TreeQueryEditor.ApplyNewConditionAction();
   private AttributeTreeNode selectedATNode = null;

   public TreeQueryEditor(QuerySelectionModel var1) {
      this.tree = new XmlTree(emptyModel());
      this.conditionValueEditor.setLabel("Edit condition &value:");
      this.conditionValueEditor.setEnabled(false);
      this.applyValueAction.setEnabled(false);
      this.conditionValueEditor.addActionListener(this.applyValueAction);
      this.bindWithModel(var1);
      this.tree.asSelectionSource().addSelectionListener(new TreeQueryEditor.ConditionValueEditorEnabler());
   }

   private static KongaTreeModel emptyModel() {
      DefaultKongaTreeNode var0 = new DefaultKongaTreeNode("Select a query", false);
      return new DefaultKongaTreeModel(var0);
   }

   private void bindWithModel(QuerySelectionModel var1) {
      Property var2 = var1.getSelectedQueryProperty();
      AbstractBinding var3 = new AbstractBinding<FetchXml>(var2) {
         protected void updateUiWithValue(FetchXml var1) {
            if (var1 == null) {
               TreeQueryEditor.this.tree.setModel(TreeQueryEditor.emptyModel());
            } else {
               TreeQueryEditor.TreeModelBuilder var2x = TreeQueryEditor.this.new TreeModelBuilder(var1);
               var2x.execute();
            }
         }
      };
      var3.syncUi();
   }

   @Override
   public String getFetchXml() {
      ElementTreeNode var1 = (ElementTreeNode)this.tree.getModel().getRoot();
      Element var2 = var1.getXmlElement();
      XmlPrettyPrinter var3 = new XmlPrettyPrinter(2);

      try {
         String var4 = var3.prettyPrintToString(var2);
         System.out.println("XML value is: " + var4);
         return var4;
      } catch (TransformerException var5) {
         throw new RuntimeException(var5);
      }
   }

   @Override
   public boolean canDoForwardNavigation() {
      if (!this.conditionValueEditor.isEnabled()) {
         return true;
      }

      if (this.selectedATNode != null) {
         Attr var1 = this.selectedATNode.getXmlAttribute();
         if (var1 != null) {
            String var2 = var1.getValue();
            String var3 = this.conditionValueEditor.getText();
            if (var3 != null && !var3.equals(var2)) {
               Answer var4 = Ask.yesOrNo(
                  null, "You have chaned the values and not pressed apply.", "Do you want to continue?", Alert.Option.YES, Alert.Type.WARNING
               );
               if (var4.isNo()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box().addAllWithSpace(5, this.conditionValueEditor.getLabel(), this.conditionValueEditor, this.applyValueAction);
      new BorderLayoutBuilder(0, 10).center(this.tree.asUiProvider()).south(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   private class ApplyNewConditionAction extends AbstractAction {
      public ApplyNewConditionAction() {
         super("Apply");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ApplyNewConditionAction.this.applyValue();
            }
         });
      }

      private void applyValue() {
         if (this.isEnabled()) {
            String var1 = TreeQueryEditor.this.conditionValueEditor.getText();
            if (TreeQueryEditor.this.selectedATNode != null) {
               Attr var2 = TreeQueryEditor.this.selectedATNode.getXmlAttribute();
               var2.setValue(var1);
               System.out.println("Applied value to the " + TreeQueryEditor.this.selectedATNode.getDisplayName() + "  Vauue:" + var1);
               TreeQueryEditor.this.tree.getModel().nodeChanged(TreeQueryEditor.this.selectedATNode);
            } else {
               System.out.println("User pressed the apply with out selecting the node. This should not happen");
            }
         }
      }
   }

   private class ConditionValueEditorEnabler implements SelectionListener {
      private ConditionValueEditorEnabler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         List var2 = TreeQueryEditor.this.tree.getSelectedNodes();
         boolean var3 = false;
         TreeQueryEditor.this.selectedATNode = null;

         for (Object __etlItem161 : var2) {
            KongaTreeNode var5 = (KongaTreeNode) __etlItem161;
            if (!(var5 instanceof AttributeTreeNode)) {
               System.out.println("This is not a attribute node " + var5);
            } else {
               System.out.println("This is a attribute node " + var5);
               AttributeTreeNode var6 = (AttributeTreeNode)var5;
               Attr var7 = var6.getXmlAttribute();
               if (!var7.getName().equals("value")) {
                  System.out.println("Attribute name is not value " + var7);
               } else {
                  KongaTreeNode var8 = var5.getParent();
                  if (!(var8 instanceof ElementTreeNode)) {
                     System.out.println("This is not a ElementTreeNode " + var8);
                  } else {
                     System.out.println("This is a ElementTreeNode " + var8);
                     ElementTreeNode var9 = (ElementTreeNode)var8;
                     if (var9.getXmlElement().getNodeName().equals("condition")) {
                        var3 = true;
                        TreeQueryEditor.this.selectedATNode = var6;
                        var7 = var6.getXmlAttribute();
                        TreeQueryEditor.this.conditionValueEditor.setText(var7.getValue());
                     } else {
                        System.out.println("This is not a condition node " + var9.getXmlElement().getNodeName());
                     }
                  }
               }
            }
         }

         if (var3) {
            TreeQueryEditor.this.conditionValueEditor.setEnabled(true);
            TreeQueryEditor.this.applyValueAction.setEnabled(true);
         } else {
            TreeQueryEditor.this.conditionValueEditor.setText("");
            TreeQueryEditor.this.conditionValueEditor.setEnabled(false);
            TreeQueryEditor.this.applyValueAction.setEnabled(false);
         }
      }
   }

   private class TreeModelBuilder extends KongaSwingWorker<KongaTreeModel, Void> {
      private final FetchXml xml;

      public TreeModelBuilder(FetchXml var2) {
         this.xml = var2;
      }

      protected KongaTreeModel doInBackground() throws Exception {
         Element var1 = this.xml.getRootElement();
         return XmlTreeModelBuilder.createModel(var1);
      }

      protected void onSuccess(KongaTreeModel var1) {
         TreeQueryEditor.this.tree.setModel(var1);
         TreeQueryEditor.this.tree.expandAll();
      }
   }
}
