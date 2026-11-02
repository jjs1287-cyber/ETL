package org.etlforge.application.ui.window.section;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.AddContentCallback;
import org.etlforge.application.ui.window.AddContentCallbackAdapter;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.event.WindowSectionEvent;
import org.etlforge.application.ui.window.event.WindowSectionListener;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

public final class MultiContentWindowSection extends AbstractWindowSection {
   private final Map<String, WindowSectionContent> idToContent = Maps.newHashMap();
   private final WindowMultiContentDisplayer contentDisplayer;
   private final MultiContentWindowSection.DetachedContentManager detachedContent;
   private final boolean closeable;
   private final Action detachAction;
   private WindowSectionContent activeContent;

   public MultiContentWindowSection(ApplicationWindow var1, boolean var2, boolean var3) {
      super(var1);
      if (var3) {
         throw new NotImplementedYetException("canCloseOthers has not been implemented");
      }

      this.closeable = var2;
      this.detachAction = new MultiContentWindowSection.DetachAction();
      this.contentDisplayer = new WindowMultiContentDisplayer(this, var2, this.detachAction);
      this.detachedContent = new MultiContentWindowSection.DetachedContentManager();
      this.addWindowSectionListener(new WindowSectionListener.Adapter() {
         @Override
         public void activeContentChanged(WindowSectionEvent var1) {
            WindowSectionContent var2x = var1.getContent();
            boolean var3x = var2x != null && var2x.isDetachable();
            MultiContentWindowSection.this.detachAction.setEnabled(var3x);
         }
      });
   }

   @Override
   public void addContent(WindowSectionContent var1, AddContentCallback var2) {
      AddContentCallback var3 = var2 != null ? var2 : new AddContentCallbackAdapter() {};
      if (this.idToContent.containsKey(var1.getId())) {
         this.setActiveContent(var1);
         var3.contentAdded(this, var1);
      } else {
         MultiContentWindowSection.AddContentJob var4 = new MultiContentWindowSection.AddContentJob(var1, var3);
         new BusyWorker(this.getWindow(), var4).run();
      }
   }

   @Override
   public boolean removeContent(WindowSectionContent var1) {
      if (this.idToContent.containsKey(var1.getId()) && var1.removeFromWindow(false)) {
         this.idToContent.remove(var1.getId());
         this.contentDisplayer.removeContent(var1);
         this.detachedContent.contentWasClosed(var1);
         var1.appendState(WindowElementState.CLOSED);
         this.fireContentRemoved(var1);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean containsContent(String var1) {
      return this.idToContent.containsKey(var1) || this.detachedContent.contains(var1);
   }

   @Override
   public WindowSectionContent getContent(String var1) {
      WindowSectionContent var2 = this.idToContent.get(var1);
      if (var2 == null) {
         var2 = this.detachedContent.getContent(var1);
      }

      return var2;
   }

   @Override
   public WindowSectionContent getActiveContent() {
      return this.contentDisplayer.getActiveContent();
   }

   @Override
   public void setActiveContent(WindowSectionContent var1) {
      if (this.idToContent.containsKey(var1.getId())) {
         if (this.detachedContent.contains(var1.getId())) {
            this.detachedContent.setActiveContent(var1);
         } else {
            this.contentDisplayer.setActiveContent(var1);
         }
      }
   }

   @Override
   public void ensureVisible() {
      if (!this.idToContent.isEmpty()) {
         super.ensureVisible();
      }
   }

   @Override
   public void decorate(JComponent var1) {
      this.contentDisplayer.decorate(var1);
   }

   void onActiveContentChanged(WindowSectionContent var1) {
      if (this.activeContent != var1) {
         this.activeContent = var1;
         this.detachAction.setEnabled(var1 != null && var1.isDetachable());
         this.fireActiveContentChanged(var1);
      }
   }

   private void addContentToDisplayer(WindowSectionContent var1) {
      JPanel var2 = new JPanel();
      var1.decorate(var2);
      if (this.closeable) {
         CloseWindowSectionContentAction var3 = new CloseWindowSectionContentAction(this, var1);
         ActionKeyBinding.install(var3, var2, 1);
      }

      this.contentDisplayer.addContent(var1, var2);
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
            this.content.addToWindow(MultiContentWindowSection.this.getWindow());
            MultiContentWindowSection.this.addContentToDisplayer(this.content);
            this.content.appendState(WindowElementState.OPEN);
            MultiContentWindowSection.this.idToContent.put(this.content.getId(), this.content);
            MultiContentWindowSection.this.fireContentAdded(this.content);
            MultiContentWindowSection.this.fireEnureVisibleRequested();
            this.callback.contentAdded(MultiContentWindowSection.this, this.content);
         } catch (Exception var2) {
            ErrorLog.attention(MultiContentWindowSection.class, ApplicationResources.STRINGS.getString("InternalError"), var2);
            this.callback.failed(var2);
         }
      }
   }

   private class DetachAction extends AbstractAction {
      public DetachAction() {
         super("Detach");
         this.putValue("AcceleratorKey", KeyUtils.F12);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         WindowSectionContent var2 = MultiContentWindowSection.this.getActiveContent();
         if (var2 != null && var2.isDetachable()) {
            this.detachContent(var2);
         }
      }

      private void detachContent(WindowSectionContent var1) {
         MultiContentWindowSection.this.detachedContent.detach(var1);
         MultiContentWindowSection.this.contentDisplayer.removeContent(var1);
         MultiContentWindowSection.this.fireContentRemoved(var1);
      }
   }

   private class DetachedContentManager {
      private final BiMap<String, DetachedContentDialog> dialogs = HashBiMap.create();

      public DetachedContentManager() {
      }

      public void detach(final WindowSectionContent var1) {
         DetachedContentDialog var2 = new DetachedContentDialog(var1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               DetachedContentManager.this.reattach(var1);
            }
         });
         var1.appendState(WindowElementState.DETACHED);
         this.dialogs.put(var1.getId(), var2);
         var2.open();
      }

      public void contentWasClosed(WindowSectionContent var1) {
         this.dialogs.remove(var1.getId());
      }

      public boolean contains(String var1) {
         return this.dialogs.containsKey(var1);
      }

      public WindowSectionContent getContent(String var1) {
         DetachedContentDialog var2 = this.dialogs.get(var1);
         return var2 != null ? var2.getContent() : null;
      }

      public void setActiveContent(WindowSectionContent var1) {
         DetachedContentDialog var2 = this.dialogs.get(var1.getId());
         if (var2 != null) {
            var2.open();
         }
      }

      public void reattach(WindowSectionContent var1) {
         this.dialogs.remove(var1.getId());
         MultiContentWindowSection.this.addContentToDisplayer(var1);
         var1.revokeState(WindowElementState.DETACHED);
         MultiContentWindowSection.this.fireContentAdded(var1);
         MultiContentWindowSection.this.fireEnureVisibleRequested();
         this.setActiveContent(var1);
      }
   }
}
