package org.etlforge.application.ui.viewer.log;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.MnemonicString;

final class PackageResources {
   private PackageResources() {
   }

   static class Dialog {
      static final String TITLE = ApplicationResources.STRINGS.getString("LogRepository.Dialog.Title");
   }

   static class OpenFile {
      static final MnemonicString NAME = ApplicationResources.getMnemonicString("Open.Action");
      static final Icon ICON = CommonIcons.OPEN_16;
      static final String TOOLTIP = ApplicationResources.STRINGS.getString("LogRepository.OpenFile.Tooltip");
   }

   static class RepositoryUi {
      static final String SELECTED_LOG = ApplicationResources.STRINGS.getString("LogRepository.Ui.SelectedLog");
      static final String EMPTY_LOG = ApplicationResources.STRINGS.getString("LogRepository.Ui.EmptyLog");
      static final String FAILED_TO_OPEN_LOG = ApplicationResources.STRINGS.getString("LogRepository.Ui.FailedToOpenLog");

      static class Open {
         static final MnemonicString NAME = ApplicationResources.getMnemonicString("Open.Action");
         static final Icon ICON = CommonIcons.OPEN_16;
         static final String TOOLTIP = ApplicationResources.STRINGS.getString("LogRepository.Ui.Open.ToolTip");
      }
   }

   static class View {
      static final String TITLE = ApplicationResources.STRINGS.getString("LogRepository.View.Title");
      static final Icon ICON = ApplicationResources.ICONS.getIcon("LogViewer.16");
      static final String PAGE_TITLE = ApplicationResources.STRINGS.getString("LogRepository.View.PageTitle");
   }
}
