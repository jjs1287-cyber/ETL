package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.XmlFileFilter;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.util.preference.PreferencesKey;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.ExpandedNameBag;
import org.etlforge.xml.KongaXmlException;
import org.etlforge.xml.RootNodeExtractor;
import org.etlforge.xml.XmlNameFactory;
import org.w3c.dom.Element;

final class XmlSampleRootSelector implements RootSelector {
   private static final String LATEST_SAMPLE_FILE = "SampleFile";
   private final RootSelectorContainer container;
   private final FileSelectorPanel filePanel;
   private final ExpandedNameBag availableRoots;

   public XmlSampleRootSelector(RootSelectorContainer var1, ExpandedNameBag var2) {
      assert var1 != null;
      assert var2 != null;
      this.container = var1;
      this.availableRoots = var2;
      this.filePanel = this.createFilePanel();
      this.addFilePanelListeners();
   }

   private void addFilePanelListeners() {
      this.filePanel.addFileSelectionListener(new FileSelectionListener() {
         @Override
         public void selectionChanged(File var1) {
            boolean var2 = var1 != null;
            XmlSampleRootSelector.this.container.setSelectionExists(XmlSampleRootSelector.this, var2);
         }
      });
      this.filePanel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            XmlSampleRootSelector.this.container.applySelectionRequested(XmlSampleRootSelector.this);
         }
      });
   }

   private FileSelectorPanel createFilePanel() {
      DefaultFileChooser var1 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      RecentFileStore var2 = this.getRecentFileStore();
      FileSelectorPanelFactory var3 = FileSelectorFactory.getFactory();
      FileSelectorPanel var4 = var3.singleFilePanel(var1, FileSelectorPanel.OPEN, PackageResources.RootSelector.BROWSE_TITLE, var2);
      var4.setFileFilter(new XmlFileFilter());
      var4.setUseTitleLabel(false);
      var4.setActive(false);
      return var4;
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(XmlSampleRootSelector.class, "SampleFile");
      return new PreferencesFileStore(var1, 10);
   }

   @Override
   public ExpandedName getSelectedRoot() throws InvalidRootException {
      File var1 = this.filePanel.getSelectedFile();
      if (var1 != null) {
         ExpandedName var2 = this.getRootNameFromFile(var1);
         return this.getFinalRoot(var2);
      } else {
         return null;
      }
   }

   @Override
   public void setSelectedRoot(ExpandedName var1) {
   }

   private ExpandedName getRootNameFromFile(File var1) throws InvalidRootException {
      try {
         RootNodeExtractor var2 = RootNodeExtractor.getSaxExtractor();
         Element var3 = var2.extractRoot(var1);
         XmlNameFactory var4 = XmlNameFactory.conforming();
         return var4.newExpandedName(var3);
      } catch (KongaXmlException var5) {
         throw new InvalidRootException(var5.getMessage(), var5);
      } catch (RuntimeException var6) {
         throw new InvalidRootException(PackageResources.ROOT_EXTRACTION_FAILED, var6);
      }
   }

   private ExpandedName getFinalRoot(ExpandedName var1) throws InvalidRootException {
      if (this.availableRoots.contains(var1)) {
         return var1;
      }

      String var2 = var1.getLocalName();
      if (this.availableRoots.containsLocalName(var2)) {
         ExpandedName[] var3 = this.availableRoots.getNamesFromLocalName(var2);
         if (var3.length == 1) {
            return var3[0];
         } else {
            throw new InvalidRootException(PackageResources.RootSelector.getAmbiguousRootMessage(var2));
         }
      } else {
         throw new InvalidRootException(PackageResources.RootSelector.getInvalidRootMessage(var1));
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.filePanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.filePanel.requestFocus();
   }

   @Override
   public void setActive(boolean var1) {
      this.filePanel.setActive(var1);
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(PackageResources.RootSelector.SAMPLE_FILE_LABEL);
   }
}
