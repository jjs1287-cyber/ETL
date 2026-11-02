package org.etlforge.application.ui.window.action;

import java.io.File;
import org.etlforge.application.Application;

public final class OpenApplicationFolderAction extends OpenLocalFolderAction {
   public static final String ID = "action.OpenAppDir";

   public OpenApplicationFolderAction() {
      super("action.OpenAppDir", WindowActionDictionary.DICTIONARY);
   }

   @Override
   protected File getFolderToOpen() {
      return Application.getApplicationDirectory().getLocation();
   }
}
