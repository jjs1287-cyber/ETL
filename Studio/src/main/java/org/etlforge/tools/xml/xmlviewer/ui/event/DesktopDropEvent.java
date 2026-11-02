package org.etlforge.tools.xml.xmlviewer.ui.event;

import java.io.File;
import java.util.EventObject;
import org.etlforge.tools.xml.xmlviewer.ui.Desktop;

public class DesktopDropEvent extends EventObject {
   private File m_file;

   public DesktopDropEvent(Desktop var1, File var2) {
      super(var1);
      this.m_file = var2;
   }

   public File getDroppedFile() {
      return this.m_file;
   }
}
