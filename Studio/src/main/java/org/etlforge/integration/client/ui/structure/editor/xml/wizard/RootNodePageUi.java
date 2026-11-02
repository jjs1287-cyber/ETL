package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.option.InputOption;
import org.etlforge.ui.util.option.InputOptionPanel;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.ExpandedNameBag;
import org.etlforge.xml.XmlNameFactory;

final class RootNodePageUi implements UiProvider, RootSelectorContainer {
   private final XmlStructureWizardRootNodePage page;
   private final ExpandedNameBag availableRoots;
   private final RootSelector[] rootSelectors;
   private final InputOptionPanel<RootSelector> selectorsPanel;
   private ExpandedName selectedRoot;
   private JPanel displayPanel;

   public RootNodePageUi(XmlStructureWizardRootNodePage var1, Collection<String> var2) {
      this.page = var1;
      this.availableRoots = this.createBagOfAvailableRoots(var2);
      int var3 = this.availableRoots.getNumberOfNames();
      this.rootSelectors = var3 > 0 ? this.createRootSelectors() : null;
      this.selectorsPanel = var3 > 0 ? this.createOptionPanel() : null;
      this.setInitialState();
   }

   public void setSelectedRoot(XmlStructure var1) {
      if (this.rootSelectors != null) {
         String var2 = var1.getRootNodeName();
         if (var2 != null && var2.length() != 0) {
            XmlNameFactory var3 = getXmlNameFactory();
            ExpandedName var4 = var3.newExpandedName(var2);

            for (RootSelector var8 : this.rootSelectors) {
               var8.setSelectedRoot(var4);
            }
         }
      }
   }

   private ExpandedNameBag createBagOfAvailableRoots(Collection<String> var1) {
      ExpandedNameBag var2 = new ExpandedNameBag();
      XmlNameFactory var3 = getXmlNameFactory();
      var2.addAll(var1, var3);
      return var2;
   }

   private static XmlNameFactory getXmlNameFactory() {
      return XmlNameFactory.nonConforming();
   }

   private RootSelector[] createRootSelectors() {
      ArrayList var1 = Lists.newArrayList();
      int var2 = this.availableRoots.getNumberOfNamespaces();
      if (var2 == 1) {
         RootSelector var3 = this.createSingleNamespaceRootSelector();
         var1.add(var3);
      } else {
         RootSelector var4 = this.createMultipleNamespaceRootSelector();
         var1.add(var4);
      }

      var1.add(new XmlSampleRootSelector(this, this.availableRoots));
      return (RootSelector[]) var1.toArray(new RootSelector[var1.size()]);
   }

   private RootSelector createSingleNamespaceRootSelector() {
      assert this.availableRoots.getNumberOfNamespaces() == 1;
      String var1 = this.availableRoots.getNamespaces()[0];
      ExpandedName[] var2 = this.availableRoots.getNamesFromNamespace(var1);
      assert var2.length > 1;
      return new SingleNamespaceRootSelector(this, var2);
   }

   private RootSelector createMultipleNamespaceRootSelector() {
      assert this.availableRoots.getNumberOfNamespaces() > 1;
      return new MultipleNamespaceRootSelector(this, this.availableRoots);
   }

   private InputOptionPanel<RootSelector> createOptionPanel() {
      int var1 = this.rootSelectors.length;
      RootNodePageUi.RootSelectorOption[] var2 = new RootNodePageUi.RootSelectorOption[var1];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = new RootNodePageUi.RootSelectorOption(this.rootSelectors[var3]);
      }

      InputOptionPanel var4 = new InputOptionPanel<>(var2);
      var4.setSelectedOption(var2[0]);
      return var4;
   }

   @Override
   public void applySelectionRequested(RootSelector var1) {
      this.page.getWizard().nextPageRequested();
   }

   @Override
   public void setSelectionExists(RootSelector var1, boolean var2) {
      this.page.setCanContinue(var2);
   }

   public void dispose() {
      if (this.selectorsPanel != null) {
         this.selectorsPanel.dispose();
      }
   }

   public String getSelectedRoot() {
      return this.selectedRoot.toString();
   }

   public boolean isInputValid() {
      try {
         this.selectedRoot = this.selectorsPanel.getValue().getSelectedRoot();
         if (this.selectedRoot == null) {
            this.displayError(PackageResources.NO_ROOT_SELECTED);
            return false;
         } else {
            return true;
         }
      } catch (InvalidRootException var2) {
         ErrorLog.attention(RootNodePageUi.class, PackageResources.ROOT_EXTRACTION_FAILED, var2);
         return false;
      }
   }

   private void displayError(String var1) {
      Alert.error(this.displayPanel, var1, ApplicationResources.STRINGS.getString("Error"));
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createUi();
      var1.display(var2);
   }

   private JComponent createUi() {
      OneColumnPanel var1 = new OneColumnPanel();
      if (this.availableRoots.getNumberOfNames() == 0) {
         this.addNoRootsErrorMessage(var1);
      } else {
         this.addOptionPanel(var1);
      }

      return var1.deeplyNonOpaque().invisible();
   }

   private void addNoRootsErrorMessage(OneColumnPanel var1) {
      KongaLabel var2 = TextStyles.CaptionText.makeLabel(PackageResources.RootNodePage.NO_ROOTS_ERROR);
      var2.setIcon(CommonIcons.ERROR_16);
      var2.setVerticalTextPosition(1);
      var1.add(var2);
   }

   private void addOptionPanel(OneColumnPanel var1) {
      var1.withInsets(0, 5, 20, 0);
      KongaLabel var2 = TextStyles.AdminHint.makeLabel(PackageResources.RootNodePage.MULTIPLE_ROOTS_MESSAGE);
      var2.setIcon(CommonIcons.WARNING_16);
      var2.setVerticalTextPosition(1);
      var1.add(var2);
      var1.withInsets(0, 0, 20, 0);
      var1.add(this.selectorsPanel);
   }

   @Override
   public void requestFocus() {
      if (this.selectorsPanel != null) {
         this.selectorsPanel.requestFocus();
      }
   }

   private void setInitialState() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RootNodePageUi.this.page.setCanContinue(RootNodePageUi.this.availableRoots.getNumberOfNames() > 0);
         }
      });
   }

   private static final class RootSelectorOption implements InputOption<RootSelector> {
      private final RootSelector rootSelector;
      private boolean isActive;

      public RootSelectorOption(RootSelector var1) {
         assert var1 != null;
         this.rootSelector = var1;
      }

      public RootSelector getValue() {
         return this.rootSelector;
      }

      @Override
      public boolean isActive() {
         return this.isActive;
      }

      @Override
      public void setActive(boolean var1) {
         if (var1 != this.isActive) {
            this.isActive = var1;
            this.rootSelector.setActive(var1);
         }
      }

      @Override
      public void decorate(OptionSelector var1) {
         this.rootSelector.decorate(var1);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.rootSelector.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.rootSelector.requestFocus();
      }
   }
}
