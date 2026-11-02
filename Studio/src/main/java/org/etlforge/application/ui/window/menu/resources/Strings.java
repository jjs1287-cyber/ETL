package org.etlforge.application.ui.window.menu.resources;

import java.util.ListResourceBundle;

public class Strings extends ListResourceBundle {
   private static final String NAME = ".Name";
   private static final Object[][] contents = new Object[][]{
      {"menu.Debug.Name", "&Debug"},
      {"menu.File.Name", "&File"},
      {"menu.Edit.Name", "&Edit"},
      {"menu.View.Name", "&View"},
      {"menu.Help.Name", "&Help"},
      {"menu.Windows.Name", "&Windows"},
      {"menu.Actions.Name", "&Actions"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
