package org.etlforge.integration.client.project.test;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.LoadSourceHistory;
import org.etlforge.integration.data.test.LoadSourceResult;
import org.etlforge.io.Folder;

final class LoadSourceResultXmlStore extends AbstractTestResultXmlStore<LoadSourceResult> implements LoadSourceHistory {
   LoadSourceResultXmlStore(Folder var1) {
      super(var1, "LoadSource", new LoadSourceResult.Factory());
   }

   @Override
   public void removeAll(Transformation var1) {
      this.deleteAllResultsForSubject(var1.getID());
   }
}
