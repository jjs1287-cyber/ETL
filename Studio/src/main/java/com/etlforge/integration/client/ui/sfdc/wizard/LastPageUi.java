package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.connect.wizard.NameChangeUi;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.label.KongaLabel;

final class LastPageUi<T extends IntegrationEntity & SalesforceWizardEntity> implements UiProvider {
   private final NameChangeUi<T> nameChangeUi;
   private final ObjectSummaryUi<T> objectSummaryUi;
   private Optional<AutoMapperChoice> autoMapperChoice = Optional.absent();

   public LastPageUi(T var1, Folder var2) {
      this.nameChangeUi = new NameChangeUi<>((T)var1, var2, new LastPageUi.NameChangeHandler());
      this.objectSummaryUi = new ObjectSummaryUi<>();
   }

   public void setName(String var1) {
      this.nameChangeUi.setName(var1);
   }

   public void setParent(Folder var1) {
      this.nameChangeUi.setParent(var1);
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

   public void withAutoMaperChoice(AutoMapperChoice var1) {
      this.autoMapperChoice = Optional.of(var1);
   }

   public void setObjectSummary(ObjectSummary<T> var1) {
      this.objectSummaryUi.setObjects(var1);
   }

   public boolean isAutoMapSelected() {
      return this.autoMapperChoice.isPresent() && this.autoMapperChoice.get().isSelected();
   }

   @Override
   public void decorate(UiCanvas var1) {
      LastPageUi.Layout var2 = new LastPageUi.Layout();
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
         var2.north(LastPageUi.this.nameChangeUi).withBorder(this.sectionBorder);
         JLabel var3 = this.getCaption("ETLForge Connect\u2122 for Salesforce", ClientIcons.ETLFORGE_CONNECT_32);
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
            "When you hit \"Finish\", ETLForge Connect\u2122 will automatically create the following project definitions:", LastPageUi.this.objectSummaryUi
         );
         var1.center(var3.withBorder(this.sectionBorder));
         this.add(var1, 1.0);
      }

      private void layoutWhatsNext() {
         if (LastPageUi.this.autoMapperChoice.isPresent()) {
            LastPageUi.this.autoMapperChoice.get().setBorder(this.sectionBorder);
            this.add(LastPageUi.this.autoMapperChoice.get().getUi(), 0.0);
         }
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class NameChangeHandler implements Receiver<String> {
      private NameChangeHandler() {
      }

      public void handle(String var1) {
         LastPageUi.this.objectSummaryUi.setNewWizardName(var1);
      }
   }
}
