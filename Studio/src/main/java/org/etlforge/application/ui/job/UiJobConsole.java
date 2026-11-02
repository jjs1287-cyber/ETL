package org.etlforge.application.ui.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JPanel;
import org.etlforge.application.ui.job.console.JobSnapshot;
import org.etlforge.application.ui.job.console.UiJobConsoleView;
import org.etlforge.application.ui.look.AlternateColors;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.border.UnderlineBorder;

public final class UiJobConsole {
   private static final Map<UiJobManager, UiJobConsole> CONSOLES = Maps.newHashMap();
   private final JobStatusListener jobListener = new UiJobConsole.JobStatusListenerImpl();
   private final LinkedList<JobSnapshot> snapshots = Lists.newLinkedList();
   private final Grid displayer = Grid.oneColumn().nonOpaque();

   private UiJobConsole() {
   }

   public static UiJobConsole getConsole(UiJobManager var0) {
      UiJobConsole var1 = CONSOLES.get(var0);
      if (var1 == null) {
         var1 = new UiJobConsole();
         var0.addGlobalJobStatusListener(var1.jobListener);
         CONSOLES.put(var0, var1);
      }

      return var1;
   }

   public WindowSectionContent getAsWindowSectionContent(WindowSection var1) {
      return new UiJobConsoleView(this, var1);
   }

   public UiProvider getUi() {
      return this.displayer;
   }

   public void removeCompletedJobs() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UiJobConsole.this.removeCompletedJobsImpl();
         }
      });
   }

   private void removeCompletedJobsImpl() {
      Iterator var1 = this.snapshots.iterator();

      while (var1.hasNext()) {
         if (((JobSnapshot)var1.next()).isDone()) {
            var1.remove();
         }
      }

      this.updateLayout();
   }

   private void updateLayout() {
      this.displayer.removeAll();
      int var1 = 0;

      for (JobSnapshot var3 : this.snapshots) {
         this.displayer.add(new UiJobConsole.Background(var3, var1++));
      }

      this.displayer.refresh();
   }

   private static class Background extends JPanel {
      public Background(UiProvider var1, int var2) {
         super(new BorderLayout());
         this.setBackground(AlternateColors.get(var2));
         ContainerCanvas.decorate(var1, this, "North");
         this.setBorder(new UnderlineBorder(new Color(185, 211, 238)));
      }

      @Override
      public boolean isOpaque() {
         return true;
      }
   }

   private class JobStatusListenerImpl extends JobStatusAdapter {
      private JobStatusListenerImpl() {
      }

      @Override
      public void jobStarted(final JobStatusChangedEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               UiJob var1x = var1.getJob();
               JobSnapshot var2 = new JobSnapshot(var1x);
               if (var1x.isRunning()) {
                  var2.started(var1x);
                  var1x.addProgressMonitor(var2);
               } else {
                  var2.ended();
               }

               UiJobConsole.this.snapshots.addFirst(var2);
               UiJobConsole.this.updateLayout();
            }
         });
      }
   }
}
