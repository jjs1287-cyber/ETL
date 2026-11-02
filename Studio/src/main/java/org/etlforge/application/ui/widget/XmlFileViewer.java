package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.tools.xml.xmlviewer.XmlViewer;
import org.etlforge.ui.wait.WaitLock;

public final class XmlFileViewer {
   private static final Map<ApplicationWindow, XmlFileViewer> instances = Maps.newIdentityHashMap();
   private final ApplicationWindow appWin;
   private XmlViewer viewer;
   private WindowListener viewerListener;

   private XmlFileViewer(ApplicationWindow var1) {
      this.appWin = var1;
   }

   public static synchronized XmlFileViewer getViewer(ApplicationWindow var0) {
      Preconditions.checkNotNull(var0, "window");
      XmlFileViewer var1 = instances.get(var0);
      if (var1 == null) {
         var1 = new XmlFileViewer(var0);
         instances.put(var0, var1);
      }

      return var1;
   }

   public void viewFile(File var1) {
      if (this.viewer == null) {
         WaitLock var2 = this.appWin.startWait();

         try {
            this.viewer = this.createViewer(var1);
            this.viewer.show();
         } finally {
            var2.release();
         }
      } else {
         this.viewer.addFile(var1);
         this.viewer.forceToFront();
      }
   }

   private XmlViewer createViewer(File var1) {
      XmlViewer var2 = new XmlViewer(new File[]{var1});
      if (this.viewerListener == null) {
         this.viewerListener = new XmlFileViewer.ViewerListener();
      }

      var2.addWindowListener(this.viewerListener);
      return var2;
   }

   private class ViewerListener extends WindowAdapter {
      private ViewerListener() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         XmlFileViewer.this.viewer.removeWindowListener(this);
         XmlFileViewer.this.viewer = null;
      }
   }
}
