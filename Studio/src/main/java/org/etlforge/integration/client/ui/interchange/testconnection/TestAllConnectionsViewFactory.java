package org.etlforge.integration.client.ui.interchange.testconnection;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.data.project.IntegrationProject;

public final class TestAllConnectionsViewFactory implements WindowSectionContentProvider {
   private final IntegrationProject project;
   private final AllEndpointsTester tester;

   public TestAllConnectionsViewFactory(IntegrationProject var1, AllEndpointsTester var2) {
      this.project = Preconditions.checkNotNull(var1, "project");
      this.tester = Preconditions.checkNotNull(var2, "tester");
   }

   public final TestAllConnectionsView getContent(WindowSection var1) {
      return new TestAllConnectionsViewImpl(var1, this.project, this.tester);
   }

   @Override
   public final String getContentId() {
      return TestAllConnectionsView.ID;
   }
}
