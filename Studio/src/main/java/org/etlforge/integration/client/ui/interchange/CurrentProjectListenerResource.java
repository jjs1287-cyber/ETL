package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementResource;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.data.project.IntegrationProject;

public abstract class CurrentProjectListenerResource implements CurrentProjectServiceListener, WindowElementResource {
   private final CurrentProjectService service;
   private final WindowElementStateSpan span;

   public CurrentProjectListenerResource(CurrentProjectService var1) {
      this(var1, WindowElementStateSpan.WHEN_OPEN);
   }

   public CurrentProjectListenerResource(CurrentProjectService var1, WindowElementStateSpan var2) {
      this.service = Preconditions.checkNotNull(var1, "service");
      this.span = Preconditions.checkNotNull(var2, "span");
   }

   @Override
   public final WindowElementStateSpan getLifeSpan() {
      return this.span;
   }

   @Override
   public final void create(WindowElement var1) {
      this.service.addListener(this);
   }

   @Override
   public final void dispose(WindowElement var1) {
      this.service.removeListener(this);
   }

   @Override
   public void projectOpened(CurrentProjectEvent var1) {
   }

   @Override
   public void projectClosed(CurrentProjectEvent var1) {
   }

   @Override
   public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
   }
}
