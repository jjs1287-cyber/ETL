package org.etlforge.integration.activity.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.util.id.KongaGuid;

public final class EntireSystemSubject implements ActivitySubject {
   private static final String DISCONNECTED_NAME = "[not connected]";
   private static final String DISCONNECTED_JOB_NAME = "a ETLForge server";
   private final ActivitySubjectId id = new ActivitySubjectId(ActivitySubjectType.SERVER, KongaGuid.createGuid());
   private String name;
   private String newName;
   private ServerConnectionService connectionService;
   private final Object lock = new Object();
   private final ServerConnectionListener connectionListener;
   private final PropertyChangeSupport nameChangeListeners;

   public EntireSystemSubject() {
      this.name = "[not connected]";
      this.connectionListener = new EntireSystemSubject.ConnectionListener();
      this.nameChangeListeners = new PropertyChangeSupport(this);
   }

   @Override
   public ActivitySubjectId getId() {
      return this.id;
   }

   public void setServerConnectionService(ServerConnectionService var1) {
      synchronized (this.lock) {
         if (this.connectionService != var1) {
            if (this.connectionService != null) {
               this.connectionService.removeServerConnectionListener(this.connectionListener);
            }

            this.connectionService = var1;
            if (var1 != null) {
               var1.addServerConnectionListener(this.connectionListener);
            }

            this.updateNameImmediately(this.getCurrentServerName());
         }
      }
   }

   @Override
   public boolean isReady() {
      synchronized (this.lock) {
         String var2 = this.getName();
         return var2 != null && var2.equals(this.getCurrentServerName());
      }
   }

   @Override
   public String getName() {
      synchronized (this.lock) {
         return this.name;
      }
   }

   @Override
   public String getJobName() {
      synchronized (this.lock) {
         String var2 = this.getCurrentServerName();
         if (var2 == null) {
            var2 = "a ETLForge server";
         }

         return var2;
      }
   }

   @Override
   public void addNameChangeListener(PropertyChangeListener var1) {
      this.nameChangeListeners.addPropertyChangeListener(var1);
   }

   @Override
   public void hasBeenUpdated() {
      this.updateNameIfNeeded();
   }

   private void updateNameIfNeeded() {
      if (this.newName != null && !this.newName.equals(this.name)) {
         this.updateNameImmediately(this.newName);
      }
   }

   @Override
   public void dispose() {
      this.removeConnectionListener();
   }

   private void removeConnectionListener() {
      synchronized (this.lock) {
         if (this.connectionService != null) {
            this.connectionService.removeServerConnectionListener(this.connectionListener);
         }
      }
   }

   private boolean representsDisconnectedState() {
      return "[not connected]".equals(this.name);
   }

   private void updateNameImmediately(String var1) {
      String var2 = this.name;
      this.name = var1 != null ? var1 : "[not connected]";
      this.newName = null;
      this.nameChangeListeners.firePropertyChange("name", var2, this.name);
   }

   private void updateNameWhenRefreshed(String var1) {
      this.newName = var1;
   }

   private String getCurrentServerName() {
      return this.connectionService != null ? this.connectionService.getCurrentServerName() : null;
   }

   private class ConnectionListener implements ServerConnectionListener {
      private ConnectionListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         synchronized (EntireSystemSubject.this.lock) {
            if (var1.isConnected()) {
               String var3 = var1.getServerName();
               if (EntireSystemSubject.this.representsDisconnectedState()) {
                  EntireSystemSubject.this.updateNameImmediately(var3);
               } else {
                  EntireSystemSubject.this.updateNameWhenRefreshed(var3);
               }
            }
         }
      }
   }
}
