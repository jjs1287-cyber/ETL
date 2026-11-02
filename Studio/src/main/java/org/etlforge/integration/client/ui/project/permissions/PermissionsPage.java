package org.etlforge.integration.client.ui.project.permissions;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.editor.AbstractEditorBase;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.permission.PermissionsStorageException;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;

final class PermissionsPage extends AbstractEditorBase {
   public static final String ACCESSED_ENTITY_PROPERTY = "PermissionsPage#AccessedEntity";
   private final PermissionsViewImpl view;
   private IntegrationEntity displayedEntity;
   private final UsersPanel usersPanel;
   private final PermissionsPanel permissionsPanel;
   private final PermissionsCache cache;
   private final Buttons buttons;
   private JComponent displayer;
   private ExecutorService savingService;
   private final PermissionsPage.EntityListener entityListener;
   private final Object lock;

   public PermissionsPage(PermissionsViewImpl var1, UserManager var2) {
      super(EditorIdGenerator.get(PermissionsPage.class));
      this.view = var1;
      this.lock = new Object();
      this.cache = new PermissionsCache(var1);
      this.permissionsPanel = new PermissionsPanel();
      this.usersPanel = new UsersPanel(var1, this.cache, var2, this.permissionsPanel);
      this.buttons = new Buttons(this);
      this.entityListener = new PermissionsPage.EntityListener();
      this.setIcon(ClientIcons.PERMISSIONS_16);
      this.registerListeners();
   }

   public boolean setAccessedEntity(IntegrationEntity var1) {
      IntegrationEntity var2;
      synchronized (this.lock) {
         if (!this.checkAllowAccessedEntity(var1)) {
            return false;
         }

         if (var1 == null) {
            this.setPageInactive();
         } else {
            this.registerNewEntity(var1);
            this.setPageActive(var1);
         }

         var2 = this.unregisterOldEntity();
         this.displayedEntity = var1;
         this.updateUiWithNewEntity();
      }

      this.firePropertyChange("PermissionsPage#AccessedEntity", var2, var1);
      return true;
   }

   private boolean checkAllowAccessedEntity(IntegrationEntity var1) {
      if (this.displayedEntity == null || this.displayedEntity.isDeleted()) {
         return true;
      } else if (var1 == this.displayedEntity) {
         return false;
      } else {
         return this.isDirty() ? this.promptSave() : true;
      }
   }

   public void updateUsersAndGroup() {
      this.usersPanel.updateList();
   }

   private void setPageInactive() {
      this.cache.setAccessedEntity(null);
      this.permissionsPanel.setRecursiveEnabled(false);
      this.usersPanel.setActive(false);
      this.permissionsPanel.setActive(false);
      this.buttons.setActive(false);
   }

   private void registerNewEntity(IntegrationEntity var1) {
      var1.addPropertyChangeListener("Name", this.entityListener);
      var1.addPropertyChangeListener("Deleted", this.entityListener);
   }

   private void setPageActive(IntegrationEntity var1) {
      this.cache.setAccessedEntity(var1);
      this.usersPanel.setActive(true);
      this.permissionsPanel.setRecursiveEnabled(var1.getNumberOfChildren() > 0);
      boolean var2 = this.usersPanel.hasSelection();
      this.permissionsPanel.setActive(var2);
      this.buttons.setActive(var2);
   }

   private IntegrationEntity unregisterOldEntity() {
      IntegrationEntity var1 = this.displayedEntity;
      if (var1 != null) {
         var1.removePropertyChangeListener("Name", this.entityListener);
         var1.removePropertyChangeListener("Deleted", this.entityListener);
      }

      return var1;
   }

   private void updateUiWithNewEntity() {
      this.updatePermissionsPanelWithNewEntity();
      this.buttons.update();
   }

   private void updatePermissionsPanelWithNewEntity() {
      Role var1 = this.usersPanel.getSelectedUserOrGroup();
      if (this.displayedEntity != null && var1 != null) {
         this.permissionsPanel.update(var1, this.cache.getPermissions(var1));
      } else {
         this.permissionsPanel.clear();
      }
   }

   private boolean promptSave() {
      Answer var1 = Ask.yesNoOrCancel(getSaveQuestion(this.displayedEntity), Strings.get("Permissions.Page.Save.Title"));
      if (var1.isYes()) {
         this.onApply();
         return true;
      } else if (var1.isNo()) {
         this.onReset();
         return true;
      } else if (var1.isCancel()) {
         return false;
      } else {
         throw new IllegalStateException("Unexpected answer: " + var1);
      }
   }

   private static final String getSaveQuestion(IntegrationEntity var0) {
      return Strings.format("Permissions.Page.Save.Question", var0.getName());
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.displayer == null) {
         this.displayer = this.createDisplayer();
      }

      var1.display(this.displayer);
   }

   public IntegrationEntity getObject() {
      synchronized (this.lock) {
         return this.displayedEntity;
      }
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      synchronized (this.lock) {
         boolean var3 = (var1 || this.allowsRemoval()) && super.removeFromWindow(var1);
         if (var3) {
            this.prepareForRemoval();
         }

         return var3;
      }
   }

   private boolean allowsRemoval() {
      return this.isDirty() ? this.promptSave() : true;
   }

   @Override
   public String getTitle() {
      return "";
   }

   private void prepareForRemoval() {
      if (this.savingService != null) {
         this.savingService.shutdown();
         this.savingService = null;
      }

      this.unregisterOldEntity();
   }

   @Override
   public boolean isDirty() {
      synchronized (this.lock) {
         return this.displayedEntity != null && (this.permissionsPanel.isDirty() || this.cache.isDirty());
      }
   }

   void onApply() {
      synchronized (this.lock) {
         Role var2 = this.usersPanel.getSelectedUserOrGroup();
         if (var2 != null && this.permissionsPanel.isDirty()) {
            this.cache.cachePermissions(var2, this.permissionsPanel.getPermissions());
         }

         boolean var3 = this.cache.applyAll(this.permissionsPanel.isRecursive());
         if (var3) {
            this.permissionsPanel.setClean();
            this.save();
         }

         this.buttons.update();
      }
   }

   @Override
   public void save() {
      synchronized (this.lock) {
         final PermissionsManager var2 = this.view.getPermissions();
         if (var2 != null) {
            if (this.savingService == null) {
               this.savingService = Executors.newFixedThreadPool(1);
            }

            this.savingService.submit(new Runnable() {
               @Override
               public void run() {
                  try {
                     var2.save();
                  } catch (PermissionsStorageException var2x) {
                     ErrorLog.attention(PermissionsPage.class, Strings.get("Permissions.Page.Save.Error"), var2x);
                  }
               }
            });
         }
      }
   }

   void onReset() {
      synchronized (this.lock) {
         this.cache.clear();
         Role var2 = this.usersPanel.getSelectedUserOrGroup();
         if (var2 == null) {
            this.permissionsPanel.clear();
         } else {
            this.permissionsPanel.update(var2, this.cache.getPermissions(var2));
         }

         this.buttons.update();
      }
   }

   void onViewEffective() {
      synchronized (this.lock) {
         EffectivePermissionsPanel var2 = null;
         User var3 = this.usersPanel.getSelectedUser();
         if (var3 != null) {
            var2 = new EffectivePermissionsPanel(this.view, this.getObject(), var3);
         } else {
            Group var4 = this.usersPanel.getSelectedGroup();
            if (var4 != null) {
               var2 = new EffectivePermissionsPanel(this.view, this.getObject(), var4);
            }
         }

         if (var2 != null) {
            Alert.message(null, var2.getDisplayer(), Strings.get("Permissions.Page.Effective"), Alert.Type.INFORMATION);
         }
      }
   }

   private void registerListeners() {
      PermissionsPage.PanelListener var1 = new PermissionsPage.PanelListener();
      this.usersPanel.addSelectionListener(var1);
      this.permissionsPanel.addChangeListener(var1);
   }

   private JComponent createDisplayer() {
      JPanel var1 = new JPanel(new GridBagLayout()) {
         @Override
         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      };
      GridBagConstraints var2 = new GridBagConstraints();
      var2.anchor = 18;
      var2.insets = new Insets(0, 10, 5, 20);
      var2.gridy = 0;
      var2.fill = 3;
      ComponentCanvasFactory.gridBag(var1, var2, this.usersPanel);
      var2.fill = 0;
      ComponentCanvasFactory.gridBag(var1, var2, this.permissionsPanel);
      ComponentCanvasFactory.gridBag(var1, var2, this.buttons);
      var1.setBorder(Empty.border(5, 5, 10, 10));
      UiUtils.setOpaqueRecursivelySkipDefaults(var1, false);
      JPanel var3 = new JPanel(new FlowLayout(3));
      var3.setTransferHandler(new PermissionsPage.DropHandlerImpl());
      var3.setBackground(Colors.NEUTRAL_BACKGROUND);
      var3.add(var1);
      return var3;
   }

   private class DropHandlerImpl extends EntityDropHandler {
      private DropHandlerImpl() {
      }

      @Override
      protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2) {
         PermissionsPage.this.setAccessedEntity(var1);
         return true;
      }
   }

   private class EntityListener implements PropertyChangeListener {
      private EntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("Name".equals(var1.getPropertyName())) {
            PermissionsPage.this.firePropertyChange(var1);
         } else if ("Deleted".equals(var1.getPropertyName())) {
            this.removeEntity();
         }
      }

      private void removeEntity() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               PermissionsPage.this.setAccessedEntity(null);
            }
         });
      }
   }

   private class PanelListener implements SelectionListener, ChangeListener {
      private PanelListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               boolean var1x = PermissionsPage.this.usersPanel.hasSelection();
               PermissionsPage.this.buttons.setActive(var1x);
               PermissionsPage.this.permissionsPanel.setActive(var1x);
            }
         });
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         PermissionsPage.this.buttons.update();
      }
   }
}
