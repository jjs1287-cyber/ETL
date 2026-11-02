package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.ui.interchange.entity.webservice.util.WebServiceOperationComboBox;
import org.etlforge.integration.client.ui.interchange.entity.webservice.util.WebServiceOperationInfoViewer;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.name.NameSorter;

final class ConflictUi implements UiProvider {
   private final KongaTree conflictTree;
   private final WebServiceOperationComboBox existingOperations;
   private final WebServiceOperationInfoViewer infoViewer;

   public static void show(List<WebServiceCall> var0, WebServiceInfo var1) {
      ConflictUi var2 = new ConflictUi(var0, var1);
      Alert.message(null, var2, Strings.get("EditWsdl.Conflicts.Title"), Alert.Type.ERROR);
   }

   public ConflictUi(List<WebServiceCall> var1, WebServiceInfo var2) {
      this.conflictTree = this.createTree(var1);
      this.infoViewer = new WebServiceOperationInfoViewer() {
         @Override
         protected boolean isIncluded(String var1) {
            return !var1.equals("SoapAction") && !var1.equals("SoapVersion");
         }
      };
      this.existingOperations = new WebServiceOperationComboBox(var2, Strings.get("EditWsdl.Conflicts.ExistingOps"));
      this.listenToOperationCombo();
   }

   private void listenToOperationCombo() {
      this.existingOperations.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ConflictUi.this.showOpInfo();
         }
      });
      this.existingOperations.selectFirst();
   }

   private void showOpInfo() {
      this.infoViewer.displayOperationInfo(this.existingOperations.getSelectedOperation());
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withBorder(new BorderBuilder().etched().empty(10)).withInsets(0, 0, 10, 0);
      this.layoutCaption(var2);
      this.layoutTree(var2);
      this.layoutExistingOperations(var2);
      var2.decorate(var1);
   }

   private void layoutExistingOperations(OneColumnPanel var1) {
      var1.setWeightY(0.0);
      var1.setWeightX(1.0);
      var1.setFill(OneColumnPanel.NONE);
      var1.add(this.existingOperations);
      TwoColumnPanel var2 = new TwoColumnPanel();
      this.infoViewer.layoutIn(var2);
      var1.add(var2);
   }

   private void layoutCaption(OneColumnPanel var1) {
      KongaLabel var2 = TextStyles.AdminHintBold.makeLabel(Strings.get("EditWsdl.Conflicts.Caption"));
      String var3 = Strings.get("EditWsdl.Conflicts.Message");
      OneColumnPanel var4 = new OneColumnPanel().withInsets(0, 0, 5, 0).withEmptyBorder(3).withBackground(javax.swing.UIManager.getColor("Panel.background")).of(var2, var3);
      var1.setWeightX(1.0);
      var1.add(var4.inScroll());
   }

   private void layoutTree(OneColumnPanel var1) {
      var1.fillOut(new JScrollPane(this.conflictTree));
   }

   @Override
   public void requestFocus() {
      this.conflictTree.requestFocus();
   }

   private KongaTree createTree(List<WebServiceCall> var1) {
      KongaTree var2 = new KongaTree(this.createTreeModel(var1));
      var2.setCellRenderer(new TreeNodeDecorRenderer());
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.expandAll();
      return var2;
   }

   private DefaultKongaTreeModel createTreeModel(List<WebServiceCall> var1) {
      DefaultKongaTreeNode var2 = new DefaultKongaTreeNode("Conflicts", true);

      for (WebServiceCall var4 : Iterables2.sortedCopy(var1, NameSorter.ignoreCase())) {
         IntegrationEntityTreeNode var5 = new IntegrationEntityTreeNode(var2, var4, true);
         this.addOperationInfoNodes(var5, var4.getOperationInfo());
      }

      return new DefaultKongaTreeModel(var2);
   }

   private void addOperationInfoNodes(KongaTreeNode var1, WebServiceOperationInfo var2) {
      String[] var3 = new String[]{"Operation", "Service", "Port", "Binding"};
      String[] var4 = new String[]{"Operation", "Service", "Port", "Binding"};

      for (int var5 = 0; var5 < var3.length; var5++) {
         new DefaultKongaTreeNode(var1, var4[var5] + ": " + var2.getProperty(var3[var5]), false);
      }
   }
}

