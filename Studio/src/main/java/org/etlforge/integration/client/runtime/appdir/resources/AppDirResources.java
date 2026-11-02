package org.etlforge.integration.client.runtime.appdir.resources;

import java.util.ListResourceBundle;

public class AppDirResources extends ListResourceBundle {
   private static final Object[][] CONTENTS = new Object[][]{
      {"Error.CreateSubFolder", "Failed to create the application directory \"{0}\": {1}"},
      {"Reason.PathIsFile", "A file with this path already exists."},
      {"Reason.CreateDirectoryFailed", "Check that the current user has write access."},
      {"ProjectDir.NewProjectNameBase", "New Project"},
      {"ProjectDir.NewProjectName", "New Project ({0})"},
      {"ServerDir.Error.Registration", "Failed to register the server with the client."}
   };

   @Override
   protected Object[][] getContents() {
      return CONTENTS;
   }
}
