package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Supplier;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.EnumSet;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.connect.text.NewFileStructureCallback;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.NonWizardObjectsFilter;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfquery.TargetHeaderMode;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusRoot;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class TextTargetPage extends AbstractWizardPage {
   private final KongaRadioButton autoCreateChoice = new KongaRadioButton(withDocumentDisplayName("&Create a {0} with the fields from your query"));
   private final KongaCheckBox includeHeaderChoice = new KongaCheckBox("&Include the field names as the first line of the file");
   private final KongaRadioButton manualChocie = new KongaRadioButton(withDocumentDisplayName("&Select an existing {0}, or create a new one"));
   private final ResponseTextStructureFactory autoCreationFactory;
   private final ProjectItemSelector<TextDocument> documentSelector;
   private final PropertyChangeListener autoCreationAvailableListener;
   private final JComponent documentSection;
   private final FocusRoot documentSectionFocus;
   private final JPanel panel = new JPanel(new BorderLayout(0, 10));

   private static String withDocumentDisplayName(String var0) {
      return MessageFormat.format(var0, EntityType.Document.getDisplayName());
   }

   public TextTargetPage(Wizard<?> var1, EntityExplorerSupport var2, ResponseTextStructureFactory var3, ApplicationWindow var4) {
      super(var1, withDocumentDisplayName("{0}"));
      this.autoCreationFactory = var3;
      this.documentSelector = this.createDocumentSelector(var2, new EditorPanel(var4));
      this.documentSection = BorderLayoutBuilder.fillWith(this.documentSelector);
      this.documentSectionFocus = FocusRoot.of(this.documentSection).withFocusReceiver(this.documentSelector);
      this.autoCreationAvailableListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            TextTargetPage.this.updateCapabilities((EnumSet<ResponseTextStructureFactory.Capabilities>)var1.getNewValue());
            TextTargetPage.this.buildLayout();
         }
      };
      this.setInitialState(var1);
      ButtonUtils.makeMutuallyExclusive(this.autoCreateChoice, this.manualChocie);
      this.listenToUi();
      this.installFocusListening();
      this.buildLayout();
      this.updateCanContinue();
      var3.addCapabilitiesChangeListener(this.autoCreationAvailableListener);
   }

   private ProjectItemSelector<TextDocument> createDocumentSelector(EntityExplorerSupport var1, EditorPanel var2) {
      ProjectItemSelector var3 = ProjectItemSelector.forTextDocuments(var1, var2);
      EntityFilter var4 = EntityFilters.and(EntityFilters.allOfType(EntityType.Document), new NonWizardObjectsFilter());
      var3.setFilter(var4);
      var3.setNewEntityCallback(new NewFileStructureCallback());
      return var3;
   }

   private void setInitialState(Wizard var1) {
      IncludeHeaderMemento.restore(var1, this.includeHeaderChoice);
      EnumSet var2 = this.autoCreationFactory.getCapabilities();
      this.updateCapabilities(var2);
   }

   private void updateCapabilities(EnumSet<ResponseTextStructureFactory.Capabilities> var1) {
      if (var1.contains(ResponseTextStructureFactory.Capabilities.CREATE_TARGET_STRUCTURE)) {
         this.autoCreateChoice.setSelected(true);
         this.includeHeaderChoice.setEnabled(var1.contains(ResponseTextStructureFactory.Capabilities.TARGET_HEADER));
      } else {
         this.manualChocie.setSelected(true);
         this.includeHeaderChoice.setEnabled(false);
      }
   }

   private void listenToUi() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TextTargetPage.this.updateCanContinue();
         }
      };
      ButtonUtils.addActionListener(var1, this.autoCreateChoice, this.manualChocie);
      this.autoCreateChoice.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            TextTargetPage.this.includeHeaderChoice.setEnabled(var1 && TextTargetPage.this.autoCreationFactory.isTargetHeaderSupported());
         }
      });
      this.documentSelector.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            TextTargetPage.this.updateCanContinue();
         }
      });
   }

   private void installFocusListening() {
      FocusUtils.bindChoiceComponents(this.autoCreateChoice, UiProviderAdapter.of(this.includeHeaderChoice));
      FocusUtils.bindChoiceComponents(this.manualChocie, this.documentSectionFocus);
   }

   private void buildLayout() {
      this.panel.removeAll();
      if (this.autoCreationFactory.canCreateStructure()) {
         byte var1 = 32;
         OneColumnPanel var2 = new OneColumnPanel(GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Fill.HORIZONTAL).withInsets(8, 0, 8, 0).setWeightX(1.0);
         var2.add(this.autoCreateChoice);
         var2.withInsets(0, var1, 16, 0).add(this.includeHeaderChoice);
         var2.withInsets(0, 0, 8, 0).add(this.manualChocie);
         var2.withInsets(0, var1, 0, 0).fillOut(this.documentSection);
         var2.addToParent(this.panel, "Center");
      } else {
         CaptionLayout var3 = CaptionLayout.of(withDocumentDisplayName("Select an existing {0}, or create a new one"), this.documentSection);
         ContainerCanvas.decorate(var3, this.panel, "Center");
      }

      UiUtils.revalidate(this.panel);
   }

   private void updateCanContinue() {
      boolean var1;
      if (this.autoCreateChoice.isSelected()) {
         var1 = true;
      } else {
         var1 = this.documentSelector.getSelectedItem() != null;
      }

      this.setCanContinue(var1);
   }

   public void applyTo(SalesforceQuery var1, Transformation var2) {
      TargetHeaderMode var3 = TargetHeaderMode.NONE;
      if (this.autoCreateChoice.isSelected()) {
         if (this.includeHeaderChoice.isSelected()) {
            var3 = TargetHeaderMode.SALESFORCE_FIELD_NAMES;
         }

         ResponseTextStructure var4 = this.autoCreationFactory.createStructure();
         var4.applyTo(var2);
         IncludeHeaderMemento.store(this.getWizard(), this.includeHeaderChoice);
      } else {
         TextDocument var5 = this.documentSelector.getSelectedItem();
         var2.setTargetTextDocumentId(var5.getID());
         var2.setOutputStructure(var5.getDataStructure());
      }

      var1.setTargetHeaderMode(var3);
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return TextTargetPage.this.autoCreateChoice.isSelected() || TextTargetPage.this.documentSelector.save();
         }
      });
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      return this.autoCreateChoice.isSelected() || this.documentSelector.closeGracefully();
   }

   @Override
   protected void dispose() {
      this.documentSelector.dispose();
      this.documentSectionFocus.dispose();
      this.autoCreationFactory.removeCapabilitiesChangeListener(this.autoCreationAvailableListener);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         withDocumentDisplayName("Select A {0}"), withDocumentDisplayName("Create a new {0} or select an existing one:")
      );
      var1.add(this.panel, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (TextTargetPage.this.autoCreationFactory.canCreateStructure()) {
               TextTargetPage.this.autoCreateChoice.requestFocusInWindow();
            } else {
               TextTargetPage.this.documentSelector.requestFocus();
            }
         }
      });
   }
}
