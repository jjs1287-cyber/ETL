package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.integration.client.ui.interchange.entity.webservice.resources.WebServiceResources;

final class PackageResources {
   private static String getString(String var0) {
      return WebServiceResources.STRINGS.getString(var0);
   }

   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   static class ExistingWsdlFileDefiner {
      static final String NO_FILES_AVAILABLE = PackageResources.getString("ExistingWsdlFileDefiner.NoFilesAvailable");
   }

   static class GetWsdlFilesJob {
      static final String NAME = PackageResources.getString("GetWsdlFilesJob.Name");
      static final String STARTED = PackageResources.getString("GetWsdlFilesJob.Started");
      static final String UPDATING_UI = PackageResources.getString("GetWsdlFilesJob.UpdatingUi");
      static final String ERROR_STATUS = PackageResources.getString("GetWsdlFilesJob.ErrorStatus");
   }

   static class LocalWsdlFileDefiner {
      static final String BROWSE_DIALOG_TITLE = PackageResources.getString("LocalWsdlFileDefiner.BrowseDialogTitle");
   }

   static class ProcessExistingFileJob {
      static final String STATUS_DOWNLOAD = PackageResources.getString("ProcessExistingWsdlFileJob.Status.Download");
      static final String STATUS_PARSING = PackageResources.getString("ProcessExistingWsdlFileJob.Status.Parsing");
   }

   static class ProcessLocalFileJob {
      static final String STATUS_PARSING = PackageResources.getString("ProcessLocalWsdlFileJob.Status.Parsing");
      static final String STATUS_UPLOAD = PackageResources.getString("ProcessLocalWsdlFileJob.Status.Upload");
   }

   static class ProcessRemoteFileJob {
      static final String STATUS_PARSING = PackageResources.getString("ProcessRemoteWsdlFileJob.Status.Parsing");
      static final String STATUS_UPLOAD = PackageResources.getString("ProcessRemoteWsdlFileJob.Status.Upload");
   }

   static class WsdlSelectorPanel {
      static class Choices {
         static final String EXISTING = PackageResources.getString("WsdlSelectorPanel.Choice.Existing");
         static final String URL = PackageResources.getString("WsdlSelectorPanel.Choice.URL");
         static final String LOCAL = PackageResources.getString("WsdlSelectorPanel.Choice.Local");
      }
   }
}
