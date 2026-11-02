package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import org.etlforge.tools.xml.xmlviewer.ui.event.DesktopDropEvent;
import org.etlforge.tools.xml.xmlviewer.ui.event.DesktopDropListener;

public class Desktop extends JDesktopPane {
   private List<DesktopDropListener> m_liDesktopDropListeners = new ArrayList<>();

   public Desktop() {
      this.setDesktopManager(new DefaultDesktopManager());
      new DropTarget(this, new Desktop.FileDropper());
   }

   public void addDesktopDropListener(DesktopDropListener var1) {
      if (!this.m_liDesktopDropListeners.contains(var1)) {
         this.m_liDesktopDropListeners.add(var1);
      }
   }

   public void removeDesktopDropListener(DesktopDropListener var1) {
      this.m_liDesktopDropListeners.remove(var1);
   }

   protected final void fireFileDropped(File var1) {
      DesktopDropEvent var2 = null;

      for (DesktopDropListener var4 : this.m_liDesktopDropListeners) {
         if (var2 == null) {
            var2 = new DesktopDropEvent(this, var1);
         }

         var4.fileDroppedOnDesktop(var2);
      }
   }

   public XmlDocumentFrame getSelectedFrame() {
      return (XmlDocumentFrame)super.getSelectedFrame();
   }

   private class FileDropper extends DropTargetAdapter {
      private FileDropper() {
      }

      @Override
      public void drop(DropTargetDropEvent var1) {
         try {
            DropTargetContext var2 = var1.getDropTargetContext();
            var1.acceptDrop(3);
            Transferable var3 = var1.getTransferable();
            Object var4 = var3.getTransferData(DataFlavor.javaFileListFlavor);
            if (var4 instanceof List) {
               for (Object var6 : (List)var4) {
                  if (var6 instanceof File) {
                     Desktop.this.fireFileDropped((File)var6);
                  }
               }
            }

            var2.dropComplete(true);
         } catch (InvalidDnDOperationException var7) {
            var7.printStackTrace();
         } catch (UnsupportedFlavorException var8) {
            var8.printStackTrace();
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }
   }
}
