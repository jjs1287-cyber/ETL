package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

class WindowsMenu extends JMenu {
   private Map<File, XmlDocumentFrame> m_mapFileToFrame;
   private Map<XmlDocumentFrame, File> m_mapFrameToFile;
   private static final Comparator<File> ITEM_COMPARATOR = new Comparator<File>() {
      public int compare(File var1, File var2) {
         return String.CASE_INSENSITIVE_ORDER.compare(var1.getAbsolutePath(), var2.getAbsolutePath());
      }
   };

   public WindowsMenu() {
      super("Windows");
      this.m_mapFileToFrame = new TreeMap<>(ITEM_COMPARATOR);
      this.m_mapFrameToFile = new HashMap<>();
      this.setMnemonic('W');
   }

   private void refresh() {
      this.removeAll();

      for (File var2 : this.m_mapFileToFrame.keySet()) {
         XmlDocumentFrame var3 = this.m_mapFileToFrame.get(var2);
         this.add(getMenuItem(var2, var3));
      }
   }

   public void addFile(File var1, XmlDocumentFrame var2) {
      this.m_mapFileToFrame.put(var1, var2);
      this.m_mapFrameToFile.put(var2, var1);
      this.refresh();
   }

   public void onFrameClosed(XmlDocumentFrame var1) {
      File var2 = this.m_mapFrameToFile.remove(var1);
      this.m_mapFileToFrame.remove(var2);
      this.refresh();
   }

   private static JMenuItem getMenuItem(File var0, final XmlDocumentFrame var1) {
      JMenuItem var2 = new JMenuItem(var0.getName());
      var2.setToolTipText(var0.getAbsolutePath());
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            try {
               var1.setSelected(true);
            } catch (PropertyVetoException var3) {
               var3.printStackTrace();
            }
         }
      });
      return var2;
   }
}
