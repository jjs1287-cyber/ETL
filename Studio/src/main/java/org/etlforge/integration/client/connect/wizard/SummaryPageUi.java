package org.etlforge.integration.client.connect.wizard;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class SummaryPageUi implements UiProvider {
   private final NameChangeUi<Operation> nameChangeUi;
   private final ObjectSummaryUi objectSummary;
   private final KongaCheckBox autoMapChoice;

   public SummaryPageUi(Operation var1, Folders var2, String var3) {
      Folder var4 = var2.getOperationParent(false);
      Folder var5 = var2.getTransformationParent(false);
      this.objectSummary = new ObjectSummaryUi(var4, var5);
      this.nameChangeUi = new NameChangeUi<>(var1, var4, new Receiver<String>() {
         public void handle(String var1) {
            SummaryPageUi.this.objectSummary.setNewWizardName(var1);
         }
      });
      this.autoMapChoice = new KongaCheckBox("Use &Automapper", true).withBorder(Empty.border(10, 20, 0, 0));
      this.setInitialName(var4, var3);
   }

   private void setInitialName(Folder var1, String var2) {
      String var3 = this.createSuggestedName(var1, var2);
      this.nameChangeUi.setName(var3);
   }

   private String createSuggestedName(Folder var1, String var2) {
      return var1 == null ? var2 : EntityUtils.getAvailableChildName(var1, var2);
   }

   public String getName() {
      return this.nameChangeUi.getName();
   }

   public boolean isAutoMapSelected() {
      return this.autoMapChoice.isSelected();
   }

   public boolean isValid() {
      return this.nameChangeUi.isValid();
   }

   public void addValidationListener(InputChangeListener var1) {
      this.nameChangeUi.addValidationListener(var1);
   }

   public void removeValidationListener(InputChangeListener var1) {
      this.nameChangeUi.removeValidationListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      SummaryPageUi.Layout var2 = new SummaryPageUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.nameChangeUi.requestFocus();
   }

   private class Layout {
      private final OneColumnPanel layout = new OneColumnPanel();
      private final Border sectionBorder;

      public Layout() {
         this.layout.setFill(GridBagLayoutPanel.Fill.BOTH);
         this.layout.withInsets(0, 0, 20, 0);
         this.layout.setWeightX(1.0);
         this.sectionBorder = new BorderBuilder().empty(0, 25, 0, 0).titled("");
         this.layoutNameChangeUi();
         this.layoutObjectSummary();
         this.layoutWhatsNext();
      }

      private void layoutNameChangeUi() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         BorderLayoutBuilder var2 = new BorderLayoutBuilder();
         var2.north(SummaryPageUi.this.nameChangeUi).withBorder(this.sectionBorder);
         JLabel var3 = this.getCaption("ETLForge Connect\u2122 for Databases", ClientIcons.ETLFORGE_CONNECT_32);
         var1.north(var3).center(var2);
         this.add(var1, 0.0);
      }

      private JLabel getCaption(String var1, Icon var2) {
         KongaLabel var3 = WizardStyleSheet.caption(var1);
         var3.setIcon(var2);
         return var3;
      }

      private void add(Object var1, double var2) {
         this.layout.setWeightY(var2);
         this.layout.add(var1);
      }

      private void layoutObjectSummary() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         JLabel var2 = this.getCaption("Project Creator", ProjectIcons.PROJECT_32);
         var1.north(var2);
         CaptionLayout var3 = CaptionLayout.of(
               "When you hit \"Finish\", ETLForge Connect\u2122 will automatically create the following project definitions:",
               SummaryPageUi.this.objectSummary
            )
            .withBorder(this.sectionBorder);
         var1.center(var3);
         this.add(var1, 1.0);
      }

      private void layoutWhatsNext() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         JLabel var2 = this.getCaption("Automapper", ClientIcons.AUTO_MAPPER_32);
         var1.north(var2);
         String var3 = "<html>When you finish this wizard you will be asked to map the data from your source to your target.<br>To save time, ETLForge Connect\u2122 can automatically create mappings between similar field names.<br>You can edit these mappings.</html>";
         OneColumnPanel var4 = Vertical.column()
            .withInsets(0, 0, 3, 0)
            .withBorder(this.sectionBorder)
            .setWeightX(1.0)
            .of(var3, SummaryPageUi.this.autoMapChoice);
         var1.center(var4);
         this.add(var1, 0.0);
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }
}
