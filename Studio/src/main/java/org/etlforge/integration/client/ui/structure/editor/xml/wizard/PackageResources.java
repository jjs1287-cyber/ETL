package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.xml.ExpandedName;

final class PackageResources {
   static final String TITLE = getString("XmlStructureWizard.Title");
   static final String NAMESPACE_URI = getString("XmlStructureWizard.NamespaceUri");
   static final String LOCAL_NAME = getString("XmlStructureWizard.LocalName");
   static final String NO_ROOT_SELECTED = getString("XmlStructureWizard.NoRootSelected");
   static final String ROOT_EXTRACTION_FAILED = getString("XmlStructureWizard.RootExtractionFailed");

   private static String getString(String var0) {
      return StructureEditorResources.STRINGS.getString(var0);
   }

   static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class GetRootsJob {
      static final String NAME = PackageResources.getString("XmlStructureWizard.GetRootsJob.Name");
      static final String STARTED = PackageResources.getString("XmlStructureWizard.GetRootsJob.Started");
      static final String ERROR_MESSAGE = PackageResources.getString("XmlStructureWizard.GetRootsJob.ErrorMessage");
   }

   static class OptionsPage {
      static final String MAIN_CAPTION = PackageResources.getString("XmlStructureWizard.OptionsPage.MainCaption");
      static final String DEFAULT_SUB_TITLE = PackageResources.getString("XmlStructureWizard.OptionsPage.SubTitle");
   }

   static class RootNodePage {
      static final String MAIN_CAPTION = PackageResources.getString("XmlStructureWizard.RootNodePage.MainCaption");
      static final String DEFAULT_SUB_TITLE = PackageResources.getString("XmlStructureWizard.RootNodePage.SubTitle");
      static final String MULTIPLE_ROOTS_MESSAGE = PackageResources.getString("XmlStructureWizard.RootNodePage.MultipleRootsMessage");
      static final String NO_ROOTS_ERROR = PackageResources.getString("XmlStructureWizard.RootNodePage.NoRootsMessage");

      static class Labels {
         static final String PICK_FROM_LIST = PackageResources.getString("XmlStructureWizard.RootNodePage.Label.PickFromList");
         static final String PICK_ROOT = PackageResources.getString("XmlStructureWizard.RootNodePage.Label.PickRoot");
         static final String ROOT_NAME = PackageResources.getString("XmlStructureWizard.RootNodePage.Label.RootName");
         static final String SORT = PackageResources.getString("XmlStructureWizard.RootNodePage.Label.Sort");
      }
   }

   static class RootSelector {
      static final String SINGLE_NAMESPACE_LABEL = PackageResources.getString("XmlStructureWizard.RootSelector.SingleNamespaceLabel");
      static final String MULTIPLE_NAMESPACE_LABEL = PackageResources.getString("XmlStructureWizard.RootSelector.MultipleNamespaceLabel");
      static final String SAMPLE_FILE_LABEL = PackageResources.getString("XmlStructureWizard.RootSelector.SampleFileLabel");
      static final String BROWSE_TITLE = PackageResources.getString("XmlStructureWizard.RootSelector.FileBrowsingTitle");

      static String getAmbiguousRootMessage(String var0) {
         return PackageResources.getFormattedMessage("XmlStructureWizard.RootSelector.AmbiguousRoot", new Object[]{var0});
      }

      static String getInvalidRootMessage(ExpandedName var0) {
         return PackageResources.getFormattedMessage("XmlStructureWizard.RootSelector.InvalidRoot", new Object[]{var0});
      }
   }

   static class StartPage {
      static final String DEFAULT_SUB_TITLE = PackageResources.getString("XmlStructureWizard.StartPage.SubTitle");
      static final String NO_FILES_AVAILABLE_ON_SERVER = PackageResources.getString("XmlStructureWizard.StartPage.NoFilesAvailableOnServer");
      static final String NO_FILES_AVAILABLE_ON_SERVER_TOOLTIP = PackageResources.getString("XmlStructureWizard.StartPage.NoFilesAvailableOnServer.ToolTip");
      static final String UPLOAD_FILE_COMMENT = PackageResources.getString("XmlStructureWizard.StartPage.Label.UploadFileComment");

      static class Browse {
         static final String LABEL = PackageResources.getString("XmlStructureWizard.StartPage.Browse.Label");
         static final Icon ICON = null;
         static final String DIALOG_TITLE = PackageResources.getString("XmlStructureWizard.StartPage.Browse.DialogTitle");
      }

      static class Choices {
         static final String SELECT_EXISTING = PackageResources.getString("XmlStructureWizard.StartPage.Choice.SelectExisting");
         static final String UPLOAD_FILE = PackageResources.getString("XmlStructureWizard.StartPage.Choice.UploadFile");
      }
   }
}
