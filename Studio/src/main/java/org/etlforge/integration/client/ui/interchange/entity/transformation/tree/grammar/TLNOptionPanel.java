package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public class TLNOptionPanel implements UiProvider, ItemListener {
   private final TreeComponent tree;
   private final Node tlnNode;
   private JRadioButton addMapSelector;
   private JRadioButton deleteSelector;
   private JRadioButton addConditionSelector;
   private KongaTextField numberOfFoldersField;
   private JComponent numberOfFoldersFieldBox;
   private String saved = "";
   private boolean valid;

   public TLNOptionPanel(TreeComponent var1, Node var2) {
      this.tree = var1;
      this.tlnNode = var2;
      this.createComponents();
      this.setInitialState();
   }

   public final void apply() {
      if (this.isValid() && this.addMapSelector.isSelected()) {
         int var1 = new Integer(this.numberOfFoldersField.getText());
         Node var2 = this.tlnNode.findLastMultipleMapping(-1);
         int var3 = var2.getMultipleMappingNumber();

         for (int var4 = 1; var4 <= var1; var4++) {
            this.tlnNode.insertMultipleMapping(var4 + var3);
         }
      } else if (this.deleteSelector.isSelected()) {
         if (this.tlnNode.isMultipleMap()) {
            this.tlnNode.deleteMultipleMapped();
         }
      } else if (this.addConditionSelector.isSelected()) {
         if (this.tlnNode.hasPrecondition()) {
            this.tlnNode.deletePreCondition();
         } else {
            this.tree.insertPreconditionNode(this.tlnNode);
         }
      }

      if (this.tlnNode != null) {
         this.tlnNode.getTreeComponent().refresh();
      }
   }

   public final boolean isValid() {
      return this.valid;
   }

   public void setValid(boolean var1) {
      this.valid = var1;
   }

   @Override
   public final void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.north(this.getCaption()).center(this.getOptions());
      var2.decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.addMapSelector.requestFocus();
   }

   private void createComponents() {
      this.createRadioButtons();
      this.createTextField();
   }

   private void createRadioButtons() {
      if (this.tlnNode.hasPrecondition()) {
         this.addConditionSelector = new KongaRadioButton("&Disable condition on current folder");
      } else {
         this.addConditionSelector = new KongaRadioButton("&Enable condition on current folder");
      }

      this.deleteSelector = new KongaRadioButton("&Remove current folder");
      this.deleteSelector.setEnabled(this.tlnNode.isMultipleMap());
      this.addMapSelector = new KongaRadioButton("&Create additional folders");
      ButtonUtils.makeMutuallyExclusive(this.addConditionSelector, this.deleteSelector, this.addMapSelector);
      ButtonUtils.addItemListener(this, this.addConditionSelector, this.deleteSelector, this.addMapSelector);
   }

   private void createTextField() {
      this.numberOfFoldersField = new KongaTextField(5);
      this.numberOfFoldersField.setSelectAllWhenFocused(true);
      this.numberOfFoldersField.getDocument().addDocumentListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            TLNOptionPanel.this.textValueChanged();
         }
      });
      this.numberOfFoldersFieldBox = this.getTextFieldBox();
   }

   private JComponent getTextFieldBox() {
      Box var1 = Box.createHorizontalBox();
      JLabel var2 = new JLabel("Number of folders to create: ");
      var1.add(var2);
      var1.add(this.numberOfFoldersField);
      return var1;
   }

   private void setInitialState() {
      if (this.tlnNode.isLoopNode()) {
         if (this.tlnNode.getTreeMapper().m_targetTreeComponent.m_flat && this.tlnNode.getTreeMapper().m_sourceTreeComponent.m_flat) {
            this.addMapSelector.setEnabled(false);
         } else {
            this.addMapSelector.setEnabled(true);
         }
      }

      UiUtils.setEnabledRecursively(this.numberOfFoldersFieldBox, false);
   }

   private JComponent getCaption() {
      KongaLabel var1 = TextStyles.InfoTextHeader.makeLabel("Multiple Folder Options:");
      Icon var2 = ApplicationResources.getIcon("FolderOpened.16");
      var1.setIcon(var2);
      return var1;
   }

   private UiProvider getOptions() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 10, 0);
      var1.add(this.addConditionSelector);
      var1.add(this.deleteSelector);
      var1.add(this.addMapSelector);
      int var2 = 22 + this.addMapSelector.getIconTextGap();
      var1.withInsets(0, var2, 5, 5);
      var1.add(this.numberOfFoldersFieldBox);
      return var1;
   }

   @Override
   public final void itemStateChanged(ItemEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.addMapSelector || var2 == this.deleteSelector || var2 == this.addConditionSelector) {
         this.numberOfFoldersField.setEditable(false);
         this.setValid(true);
         boolean var3 = false;
         if (var2 == this.addMapSelector) {
            this.numberOfFoldersField.setText("1");
            this.numberOfFoldersField.setEditable(true);
            var3 = true;
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  TLNOptionPanel.this.numberOfFoldersField.requestFocusInWindow();
               }
            });
         } else if (var2 == this.deleteSelector) {
         }

         UiUtils.setEnabledRecursively(this.numberOfFoldersFieldBox, var3);
      }
   }

   private void textValueChanged() {
      String var1 = this.numberOfFoldersField.getText();
      this.setValid(!var1.isEmpty());
      char var2 = 0;
      int var3 = 0;
      int var4 = var1.length();
      if (var4 > 0) {
         var2 = var1.charAt(0);
      }

      if (var2 == '_') {
         var3++;
      }

      while (var3 < var4 && Character.isDigit(var1.charAt(var3))) {
         var3++;
      }

      if (var3 < var4) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TLNOptionPanel.this.restoreSavedString();
            }
         });
      } else {
         this.saved = var1;
      }
   }

   private void restoreSavedString() {
      this.numberOfFoldersField.setText(this.saved);
      this.numberOfFoldersField.setCaretPosition(this.saved.length());
   }
}
