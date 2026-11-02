package org.etlforge.application.ui.window.section;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.AddContentCallback;
import org.etlforge.application.ui.window.AddContentCallbackAdapter;
import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowSectionContent;

public class SingleContentWindowSection extends AbstractWindowSection {
   private String contentID;
   private WindowSectionContent content;
   private final WindowSingleContentDisplayer displayer = new WindowSingleContentDisplayer(this);

   public SingleContentWindowSection(ApplicationWindow var1) {
      super(var1);
   }

   @Override
   public void addContent(WindowSectionContent var1, AddContentCallback var2) {
      AddContentCallback var3 = var2 != null ? var2 : new AddContentCallbackAdapter() {};
      if (var1.getId().equals(this.contentID)) {
         var3.contentAdded(this, var1);
         this.ensureVisible();
      } else {
         this.contentID = var1.getId();
         this.content = var1;
         SingleContentWindowSection.AddContentJob var4 = new SingleContentWindowSection.AddContentJob(var1, var3);
         new BusyWorker(this.getWindow(), var4).run();
      }
   }

   @Override
   public boolean removeContent(WindowSectionContent var1) {
      if (!var1.getId().equals(this.contentID)) {
         return false;
      }

      SingleContentWindowSection.RemoveContentJob var2 = new SingleContentWindowSection.RemoveContentJob(var1);
      var2.run();
      return var2.wasRemoved();
   }

   @Override
   public WindowSectionContent getContent(String var1) {
      return var1.equals(this.contentID) ? this.content : null;
   }

   @Override
   public boolean containsContent(String var1) {
      return var1.equals(this.contentID);
   }

   @Override
   public void setActiveContent(WindowSectionContent var1) {
      this.addContent(var1, new AddContentWaitIndicator(this.getWindow()));
   }

   @Override
   public WindowSectionContent getActiveContent() {
      return this.content;
   }

   @Override
   public void decorate(JComponent var1) {
      this.displayer.decorate(var1);
   }

   void activated() {
      if (this.content != null) {
         this.content.appendState(WindowElementState.ACTIVE);
         this.fireActiveContentChanged(this.content);
      }
   }

   void deactivated() {
      if (this.content != null) {
         this.content.revokeState(WindowElementState.ACTIVE);
         this.fireActiveContentChanged(null);
      }
   }

   private class AddContentJob implements Runnable {
      private final WindowSectionContent content;
      private final AddContentCallback callback;

      public AddContentJob(WindowSectionContent var2, AddContentCallback var3) {
         this.content = var2;
         this.callback = var3;
      }

      @Override
      public void run() {
         try {
            this.content.addToWindow(SingleContentWindowSection.this.getWindow());
            this.createView();
            this.content.appendState(WindowElementState.OPEN);
            SingleContentWindowSection.this.fireContentAdded(this.content);
            SingleContentWindowSection.this.ensureVisible();
            this.callback.contentAdded(SingleContentWindowSection.this, this.content);
         } catch (Exception var2) {
            ErrorLog.attention(SingleContentWindowSection.class, ApplicationResources.STRINGS.getString("SingleContentWindowSection.Error.Add"), var2);
            this.notifyCallbackOnError(var2);
         }
      }

      private void createView() {
         JPanel var1 = new JPanel();
         this.content.decorate(var1);
         SingleContentWindowSection.this.displayer.addContent(var1);
      }

      private void notifyCallbackOnError(Throwable var1) {
         Exception var2;
         if (var1 instanceof Exception) {
            var2 = (Exception)var1;
         } else {
            var2 = new Exception(var1);
         }

         this.callback.failed(var2);
      }
   }

   private class RemoveContentJob implements Runnable {
      private final WindowSectionContent content;
      private boolean wasRemoved;

      public RemoveContentJob(WindowSectionContent var2) {
         this.content = var2;
      }

      @Override
      public void run() {
         if (this.content.removeFromWindow(false)) {
            this.runUiJob();
         }
      }

      private void runUiJob() {
         SingleContentWindowSection.this.displayer.removeContent();
         this.content.appendState(WindowElementState.CLOSED);
         this.wasRemoved = true;
         SingleContentWindowSection.this.fireContentRemoved(this.content);
      }

      public boolean wasRemoved() {
         return this.wasRemoved;
      }
   }
}
