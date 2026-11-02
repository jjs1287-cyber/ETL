package org.etlforge.integration.client.ui.project.location;

import com.google.common.base.Predicate;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import org.etlforge.integration.client.project.ProjectLocation;

public class CompatibleProjectsFilter extends RowFilter<ProjectLocationsTableModel, Integer> {
   private final Predicate<ProjectLocation> predicate;

   public CompatibleProjectsFilter(Predicate<ProjectLocation> var1) {
      this.predicate = var1;
   }

   @Override
   public boolean include(Entry<? extends ProjectLocationsTableModel, ? extends Integer> var1) {
      ProjectLocation var2 = ((ProjectLocationsTableModel)var1.getModel()).getRowObjectAt((Integer)var1.getIdentifier());
      return this.predicate.apply(var2);
   }
}
