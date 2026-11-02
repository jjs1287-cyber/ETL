package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.Map;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

abstract class TabNotificationService {
   private final EditorService editorService;
   private final EditorServiceUi editorServiceUi;
   private final Map<Editor, TabNotification> notifications;

   public TabNotificationService(EditorService var1, EditorServiceUi var2) {
      this.editorService = var1;
      this.editorServiceUi = var2;
      this.notifications = Maps.newIdentityHashMap();
   }

   public final void addNotification(Editor var1) {
      if (!this.notifications.containsKey(var1) && var1 != this.editorService.getActiveEditor() && !this.noNeedToNotify(var1)) {
         boolean var2 = this.notifications.isEmpty();
         TabNotification var3 = new TabNotification(this.editorServiceUi, var1) {
            @Override
            protected Component getTabComponent(Editor var1) {
               return TabNotificationService.this.getTabComponent(var1);
            }
         };
         this.notifications.put(var1, var3);
         if (var2) {
            this.editorService.addEditorServiceListener(new TabNotificationService.NotificationStopper());
         }

         this.notificationAdded(var1);
      }
   }

   protected abstract boolean noNeedToNotify(Editor var1);

   protected abstract Component getTabComponent(Editor var1);

   protected void notificationAdded(Editor var1) {
   }

   protected void notificationStopped(Editor var1) {
   }

   private class NotificationStopper extends EditorServiceAdapter {
      private NotificationStopper() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         this.stopNotification(var1.getEditor());
      }

      @Override
      public void activeEditorChanged(EditorServiceEvent var1) {
         this.stopNotification(var1.getEditor());
      }

      private void stopNotification(Editor var1) {
         TabNotification var2 = TabNotificationService.this.notifications.remove(var1);
         if (var2 != null) {
            var2.stop();
            if (TabNotificationService.this.notifications.isEmpty()) {
               TabNotificationService.this.editorService.removeEditorServiceListener(this);
            }

            TabNotificationService.this.notificationStopped(var1);
         }
      }
   }
}
