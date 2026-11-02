package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import javax.swing.Icon;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.client.ui.interchange.entity.EntityPageFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.uidelegate.entity.IconStore;

public class EntityPageHistoryPoint implements EditorHistoryPoint {
   private final CurrentProjectService currentProjectService;
   private final EntityPageLocator pageLocator;
   private final EntityPageFactory pageFactory;
   private final String id;
   private final WeakReference<IntegrationEntity> entityRef;
   private final Icon icon;
   private final CurrentProjectServiceListener listener;
   private boolean disposed;

   public EntityPageHistoryPoint(IntegrationEntity var1, CurrentProjectService var2, EntityPageLocator var3, EntityPageFactory var4) {
      this.currentProjectService = var2;
      this.pageLocator = var3;
      this.pageFactory = var4;
      this.entityRef = new WeakReference<>(var1);
      this.id = var1.getID().toString();
      this.icon = IconStore.defaultIcon(var1);
      this.listener = this.registerProjectListener(var1);
   }

   @Override
   public String getDisplayString() {
      IntegrationEntity var1 = this.entityRef.get();
      return var1 != null ? var1.getName() : "[]";
   }

   @Override
   public Icon getIcon() {
      return this.icon;
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public boolean isAvailable() {
      if (this.disposed) {
         return false;
      }

      IntegrationEntity var1 = this.entityRef.get();
      return var1 != null && !var1.isDeleted();
   }

   @Override
   public boolean isDisposed() {
      return this.disposed || this.entityRef.get() == null;
   }

   @Override
   public void restore(EditorService var1) {
      Preconditions.checkState(!this.disposed, "Cannot call open() on a disposed history point");
      IntegrationEntity var2 = this.entityRef.get();
      if (var2 == null) {
         ErrorLog.log(this.getClass(), null, new Exception("Trying to restore the editor for an entity that no longer exists in memory. This is a bug."));
      } else {
         Editor var3 = this.pageLocator.findOpenPage(var2);
         if (var3 == null) {
            var3 = this.pageFactory.createPage(var2);
            String var4 = ProjectPageGroup.get(var2);
            var1.openEditor(var3, var4);
         } else {
            var1.openEditor(var3);
         }
      }
   }

   @Override
   public void dispose() {
      if (!this.disposed) {
         this.removeProjectListener();
         this.disposed = true;
      }
   }

   private CurrentProjectServiceListener registerProjectListener(IntegrationEntity var1) {
      final IntegrationProject var2 = ProjectUtils.getProject(var1);
      CurrentProjectServiceListener.Adapter var3 = new CurrentProjectServiceListener.Adapter() {
         @Override
         public void projectClosed(CurrentProjectEvent var1) {
            if (var1.getProject() == var2) {
               EntityPageHistoryPoint.this.dispose();
            }
         }
      };
      this.currentProjectService.addListener(var3);
      return var3;
   }

   private void removeProjectListener() {
      this.currentProjectService.removeListener(this.listener);
   }
}
