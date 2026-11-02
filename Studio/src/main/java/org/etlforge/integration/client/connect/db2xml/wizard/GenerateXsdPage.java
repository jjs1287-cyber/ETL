package org.etlforge.integration.client.connect.db2xml.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaToggleButton;

final class GenerateXsdPage extends AbstractWizardPage {
   private final XsdFileStore xsdFiles;
   volatile DatabaseStructure dbStructure;
   private final JToggleButton yes;
   private final JToggleButton no;
   private final CardPanel cards;
   volatile DbToXsdFactory.XSD xsd;
   private final XsdGeneratorControlPanel controlPanel;
   private volatile GenerateXsdPageCompleter process;
   private final XsdGeneratorModel model;
   private final Binding nextPageBinding;

   public GenerateXsdPage(Wizard<?> var1, XsdFileStore var2) {
      super(var1, "Generate XSD From Source Table");
      this.xsdFiles = var2;
      this.model = new XsdGeneratorModel();
      GenerateXsdAction var3 = new GenerateXsdAction(this.model, new GenerateXsdPage.XsdReceiver());
      this.controlPanel = new XsdGeneratorControlPanel(this.model, var3);
      this.yes = new KongaToggleButton("&Yes");
      this.no = new KongaToggleButton("&No");
      ButtonUtils.makeMutuallyExclusive(this.yes, this.no);
      ButtonUtils.addActionListener(new GenerateXsdPage.ButtonListener(), this.yes, this.no);
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(this.yes, this.no);
      this.cards = new CardPanel();
      this.cards.add(new JLabel(), "no");
      this.cards.add(this.controlPanel, "yes");
      this.cards.show("no");
      this.setCanContinue(true);
      this.nextPageBinding = new GenerateXsdPage.NextPageEnabledBinding();
      this.nextPageBinding.syncUi();
   }

   public boolean isYesSelected() {
      return this.yes.isSelected();
   }

   public void setDatabaseStructure(DatabaseStructure var1) {
      this.model.setDatabaseStructure(var1);
      this.dbStructure = var1;
   }

   public void applyTo(XmlStructure var1) {
      String var2 = this.model.getFileName();
      var1.setFilePath(var2);
      var1.setRootNodeName(this.xsd.rootName);
   }

   @Override
   public boolean prepareForForwardNavigation() {
      if (this.no.isSelected()) {
         return true;
      }

      try {
         this.process = new GenerateXsdPageCompleter(this.model, this.xsdFiles, this.getWaitService());
         this.process.start();
         this.process.waitUntilDone();
         boolean var1 = this.process.isSuccess();
         this.process = null;
         return var1;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   @Override
   protected void dispose() {
      if (this.process != null) {
         this.process.cancel();
         this.process = null;
      }
   }

   @Override
   protected void onFirstDisplay() {
      this.controlPanel.setWaitService(this.getWaitService());
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Generate XSD", "Do you want to generate an XSD based on the source table?");
      var1.add(this.layoutComponents(), true, true);
      return var1.asCenterBorderLayout();
   }

   private JComponent layoutComponents() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 20);
      var1.north(BoxBuilder.horizontal().addAllWithSpace(10, this.yes, this.no));
      var1.center(this.cards);
      return var1.container();
   }

   private class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == GenerateXsdPage.this.yes) {
            GenerateXsdPage.this.cards.show("yes");
            GenerateXsdPage.this.controlPanel.requestFocus();
            GenerateXsdPage.this.nextPageBinding.syncUi();
         } else {
            GenerateXsdPage.this.cards.show("no");
            GenerateXsdPage.this.setCanContinue(true);
            GenerateXsdPage.this.getWizard().nextPageRequested();
         }
      }
   }

   private class NextPageEnabledBinding extends AbstractBinding<Boolean> {
      public NextPageEnabledBinding() {
         super(GenerateXsdPage.this.model, "ready");
      }

      protected void updateUiWithValue(Boolean var1) {
         GenerateXsdPage.this.setCanContinue(var1);
      }
   }

   private class XsdReceiver implements Receiver<DbToXsdFactory.XSD> {
      private XsdReceiver() {
      }

      public void handle(DbToXsdFactory.XSD var1) {
         GenerateXsdPage.this.xsd = var1;
         GenerateXsdPage.this.cards.show("xsd");
      }
   }
}
