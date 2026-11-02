package com.etlforge.integration.client.ui.interchange.project.backup;

import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public interface ProjectBackupBrowser extends UiProvider, SelectionSource {
   AvailableBackup[] getSelectedBackups();

   void setBackups(AvailableBackup[] var1);

   void selectFirst();

   void setDefaultAction(SelectionAction var1);

   void setInspectBackupAction(SelectionAction var1);
}
