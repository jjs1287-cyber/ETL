package org.etlforge.integration.client.ui.core.window;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.HideableSplitPane;
import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.EditorServiceWindowContent;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.event.WindowSectionEvent;
import org.etlforge.application.ui.window.event.WindowSectionListener;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class MainWindowContentArea {
   private static final int DIVIDER_SIZE = 8;
   private final MainWindow appWin;
   private final BorderLayoutBuilder layout;
   private HideableSplitPane westCenterSplit;
   private HideableSplitPane centerEastSplit;
   private HideableSplitPane northSouthSplit;
   private boolean commandConsoleVisible;

   public MainWindowContentArea(MainWindow var1) {
      this.appWin = var1;
      this.layout = new BorderLayoutBuilder();
      this.createSplits(var1);
      this.registerListener();
   }

   private void createSplits(MainWindow var1) {
      this.createWestCenterSplit(var1);
      this.createCenterEastSplit(var1);
      this.createNorthSouthSplit(var1);
   }

   private void createWestCenterSplit(MainWindow var1) {
      this.westCenterSplit = createSplitPane(1, -1, false);
      JComponent var2 = createSplitPaneCanvas();
      var1.getWindowSection(ApplicationWindow.WEST).decorate(var2);
      this.westCenterSplit.setLeftComponent(var2);
   }

   private void createCenterEastSplit(MainWindow var1) {
      this.centerEastSplit = createSplitPane(1, 1, true);
      this.centerEastSplit.setBorder(null);
      JComponent var2 = createSplitPaneCanvas();
      WindowSection var3 = var1.getWindowSection(ApplicationWindow.CENTER);
      var3.decorate(var2);
      EditorServiceWindowContent var4 = new EditorServiceWindowContent(var3, var1.getEditorService());
      AddContentWaitIndicator var5 = new AddContentWaitIndicator(var1);
      var3.addContent(var4, var5);
      JComponent var6 = createSplitPaneCanvas();
      var1.getWindowSection(ApplicationWindow.EAST).decorate(var6);
      this.centerEastSplit.setRightComponent(var6);
      this.centerEastSplit.setLeftComponent(var2);
   }

   private void createNorthSouthSplit(MainWindow var1) {
      this.northSouthSplit = createSplitPane(0, 1, true);
      this.northSouthSplit.setBorder(null);
      this.northSouthSplit.setTopComponent(this.centerEastSplit);
      JComponent var2 = createSplitPaneCanvas();
      var1.getWindowSection(ApplicationWindow.SOUTH).decorate(var2);
      this.northSouthSplit.setBottomComponent(var2);
      this.westCenterSplit.setRightComponent(this.northSouthSplit);
   }

   private static JComponent createSplitPaneCanvas() {
      JPanel var0 = new JPanel();
      var0.setMinimumSize(new Dimension(1, 1));
      return var0;
   }

   private static HideableSplitPane createSplitPane(int var0, int var1, boolean var2) {
      HideableSplitPane var3 = new HideableSplitPane(var0, var1);
      var3.setOneTouchExpandable(true);
      var3.setDividerSize(8);
      var3.setDividerHidden(var2);
      return var3;
   }

   private void registerListener() {
      MainWindowContentArea.Listener var1 = new MainWindowContentArea.Listener();

      for (ApplicationWindow.SectionPosition var5 : ApplicationWindow.SectionPosition.values()) {
         WindowSection var6 = this.appWin.getWindowSection(var5);
         var6.addWindowSectionListener(var1);
      }
   }

   public void setCommandConsoleVisible(boolean var1) {
      if (var1 != this.commandConsoleVisible) {
         if (var1) {
            this.layout.south(this.appWin.getCommandConsole());
         } else {
            this.layout.removeSouth();
         }

         this.appWin.getFrame().getRootPane().validate();
         this.appWin.getFrame().getRootPane().repaint();
         this.commandConsoleVisible = var1;
      }
   }

   public boolean isCommandConsoleVisible() {
      return this.commandConsoleVisible;
   }

   public void decorate(JFrame var1) {
      int var2 = this.getInitialWestCenterSplitLocation(var1);
      this.westCenterSplit.setDividerLocation(var2);
      this.layout.center(this.westCenterSplit);
      if (this.commandConsoleVisible) {
         this.layout.south(this.appWin.getCommandConsole());
      }

      var1.getContentPane().add(this.layout.container(), "Center");
   }

   private int getInitialWestCenterSplitLocation(JFrame var1) {
      int var2 = 0;
      Dimension var3 = var1.getSize();
      if (var3 != null) {
         var2 = var3.width / 5;
      }

      return Math.max(var2, 250);
   }

   private class Listener extends WindowSectionListener.Adapter {
      private Listener() {
      }

      @Override
      public void contentAdded(WindowSectionEvent var1) {
         ApplicationWindow.SectionPosition var2 = MainWindowContentArea.this.appWin.getSectionPosition(var1.getSource());
         if (var2 != null) {
            switch (var2) {
               case WEST:
                  MainWindowContentArea.this.westCenterSplit.setDividerHidden(false);
                  break;
               case SOUTH:
                  MainWindowContentArea.this.northSouthSplit.setDividerHidden(false);
                  break;
               case EAST:
                  MainWindowContentArea.this.centerEastSplit.setDividerHidden(false);
               case CENTER:
            }
         }
      }

      @Override
      public void contentRemoved(WindowSectionEvent var1) {
         WindowSection var2 = var1.getSource();
         if (var2.getActiveContent() == null) {
            ApplicationWindow.SectionPosition var3 = MainWindowContentArea.this.appWin.getSectionPosition(var2);
            if (var3 == null) {
               return;
            }

            switch (var3) {
               case WEST:
                  MainWindowContentArea.this.westCenterSplit.setDividerHidden(true);
                  break;
               case SOUTH:
                  MainWindowContentArea.this.northSouthSplit.setDividerHidden(true);
                  break;
               case EAST:
                  MainWindowContentArea.this.centerEastSplit.setDividerHidden(true);
               case CENTER:
            }
         }
      }

      @Override
      public void ensureVisibleRequested(WindowSectionEvent var1) {
         ApplicationWindow.SectionPosition var2 = MainWindowContentArea.this.appWin.getSectionPosition(var1.getSource());
         if (var2 != null) {
            switch (var2) {
               case WEST:
                  this.ensureSectionVisible(MainWindowContentArea.this.westCenterSplit);
                  break;
               case SOUTH:
                  this.ensureSectionVisible(MainWindowContentArea.this.northSouthSplit);
                  break;
               case EAST:
                  this.ensureSectionVisible(MainWindowContentArea.this.centerEastSplit);
               case CENTER:
            }
         }
      }

      private void ensureSectionVisible(HideableSplitPane var1) {
         if (var1.isDividerHidden()) {
            var1.setDividerHidden(false);
         } else if (var1 == MainWindowContentArea.this.westCenterSplit) {
            new MainWindowContentArea.WestCenterSplitLocationHack(var1).ensureBothComponentsVisible();
         } else {
            var1.ensureBothComponentsVisible();
         }
      }
   }

   private static class WestCenterSplitLocationHack {
      private final HideableSplitPane pane;
      private int tries;

      public WestCenterSplitLocationHack(HideableSplitPane var1) {
         this.pane = var1;
      }

      public void ensureBothComponentsVisible() {
         if (this.pane.getWidth() != 0 && this.pane.getHeight() != 0) {
            this.pane.ensureBothComponentsVisible();
         } else if (this.tries < 10) {
            this.tries++;
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  WestCenterSplitLocationHack.this.ensureBothComponentsVisible();
               }
            });
         } else {
            DelayedJob.createAndSchedule(100, new Runnable() {
               @Override
               public void run() {
                  WestCenterSplitLocationHack.this.pane.ensureBothComponentsVisible();
               }
            });
         }
      }
   }
}
