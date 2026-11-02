package org.etlforge.integration.client.ui.project.recent;

import java.util.Comparator;

public interface ProjectNodeComparator extends Comparator<ProjectNode> {
   ProjectNodeComparator NAME_ORDER = new ProjectNodeComparator() {
      @Override
      public String getName() {
         return Strings.get("Project.Name");
      }

      public int compare(ProjectNode var1, ProjectNode var2) {
         return var1.getDisplayName().compareToIgnoreCase(var2.getDisplayName());
      }
   };
   ProjectNodeComparator LOCATION_ORDER = new ProjectNodeComparator() {
      @Override
      public String getName() {
         return Strings.get("Project.Location");
      }

      public int compare(ProjectNode var1, ProjectNode var2) {
         return var1.getUserObject().getLocationPath().compareToIgnoreCase(var2.getUserObject().getLocationPath());
      }
   };
   ProjectNodeComparator LAST_ACCESSED_ORDER = new ProjectNodeComparator() {
      @Override
      public String getName() {
         return Strings.get("Project.LastAccessed");
      }

      public int compare(ProjectNode var1, ProjectNode var2) {
         return -var1.getUserObject().getLastAccessed().compareTo(var2.getUserObject().getLastAccessed());
      }
   };
   ProjectNodeComparator LAST_MODIFIED_ORDER = new ProjectNodeComparator() {
      @Override
      public String getName() {
         return Strings.get("Project.LastModified");
      }

      public int compare(ProjectNode var1, ProjectNode var2) {
         return -var1.getUserObject().getLastModified().compareTo(var2.getUserObject().getLastModified());
      }
   };

   String getName();
}
