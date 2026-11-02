package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;

public final class ImporterFactoryImpl implements ImporterFactory {
   private final ProjectManager projectManager;

   private ImporterFactoryImpl(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public Importer newImporter(ImportParameters var1) {
      DefaultImporter var2 = new DefaultImporter(this.projectManager);
      return var1.createNewGUIDsWhenImporting() ? new NewGuidGeneratingImporter(this.projectManager, var2) : var2;
   }
}
