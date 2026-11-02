package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;

enum NodeSearchType {
   DATA_ELEMENTS,
   DATA_ELEMENTS_AND_FOLDERS,
   MAPPED_DATA_ELEMENTS,
   DATA_NODES;

   @Override
   public String toString() {
      switch (this) {
         case DATA_ELEMENTS:
            return TreeStrings.get("Search.NodeType.DataElements");
         case DATA_ELEMENTS_AND_FOLDERS:
            return TreeStrings.get("Search.NodeType.DataElementsAndFolders");
         case MAPPED_DATA_ELEMENTS:
            return TreeStrings.get("Search.NodeType.MappedDataElements");
         case DATA_NODES:
            return TreeStrings.get("Search.NodeType.DataNodes");
         default:
            throw new AssertionError();
      }
   }
}
