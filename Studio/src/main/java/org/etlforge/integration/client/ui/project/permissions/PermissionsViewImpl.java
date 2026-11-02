package org.etlforge.integration.client.ui.project.permissions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.util.name.Name;

final class PermissionsViewImpl extends AbstractWindowSectionContent implements PermissionsView {
   private final PermissionsPage page;
   private final TitlePanel titlePanel;
   private PermissionsManager permissions;
   private final Object lock = new Object();

   public PermissionsViewImpl(WindowSection var1, UserManager var2) {
      super("permissions_view", var1);
      this.page = new PermissionsPage(this, var2);
      this.page.addPropertyChangeListener("PermissionsPage#AccessedEntity", new PermissionsViewImpl.AccessedEntityListener());
      this.page.addPropertyChangeListener("Name", new PermissionsViewImpl.EntityRenamedListener());
      this.titlePanel = new TitlePanel(this, getTitlePanelColor(), getTitlePanelBorder());
   }

   @Override
   public String getTitle() {
      return Strings.get("Permissions.View.Title");
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.PERMISSIONS_16;
   }

   @Override
   public void setPermissions(PermissionsManager var1) {
      synchronized (this.lock) {
         this.permissions = var1;
         this.page.updateUsersAndGroup();
      }
   }

   public PermissionsManager getPermissions() {
      synchronized (this.lock) {
         return this.permissions;
      }
   }

   @Override
   public void decorate(JComponent var1) {
      KongaScrollPane var2 = new KongaScrollPane(BorderLayoutBuilder.fillWith(this.page));
      BorderLayoutBuilder.from(var1).north(this.titlePanel).center(var2);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.page.addToWindow(var1);
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      return this.page.removeFromWindow(var1) && super.removeFromWindow(var1);
   }

   @Override
   public boolean setAccessedEntity(IntegrationEntity var1) {
      return this.page.setAccessedEntity(var1);
   }

   private class AccessedEntityListener implements PropertyChangeListener {
      private AccessedEntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getNewValue();
         if (var2 instanceof IntegrationEntity) {
            this.setAccessedEntity((IntegrationEntity)var2);
         } else if (var2 == null) {
            this.setAccessedEntity(null);
         }
      }

      private void setAccessedEntity(final IntegrationEntity var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               PermissionsViewImpl.this.titlePanel.setAccessedEntity(var1);
            }
         });
      }
   }

   private class EntityRenamedListener implements PropertyChangeListener {
      private EntityRenamedListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getNewValue();
         if (var2 instanceof Name) {
            PermissionsViewImpl.this.titlePanel.onEntityRenamed(((Name)var2).getName());
         }
      }
   }
}
