package org.etlforge.integration.client.datalocation.connection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.List;
import org.etlforge.integration.data.test.LocationSpecificConnectionDetails;
import org.etlforge.util.persist.Persistor;

public final class MatchingFileList implements LocationSpecificConnectionDetails {
   private final ImmutableList<String> fileNames;

   public MatchingFileList(List<String> var1) {
      this.fileNames = Ordering.from(String.CASE_INSENSITIVE_ORDER).immutableSortedCopy(var1);
   }

   public int size() {
      return this.fileNames.size();
   }

   public Iterable<String> getFileNames() {
      return this.fileNames;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }

   @Override
   public void save(Persistor var1) {
      for (String var3 : this.fileNames) {
         var1.putString("file", var3);
      }
   }
}
