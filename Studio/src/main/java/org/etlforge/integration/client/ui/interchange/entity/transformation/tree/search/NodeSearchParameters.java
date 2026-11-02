package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search;

import com.google.common.base.Preconditions;
import org.etlforge.ui.util.find.SearchDirection;

final class NodeSearchParameters {
   private final String searchString;
   private final String searchString_lowercase;
   private final NodeSearchType nodeType;
   private final SearchDirection direction;

   public NodeSearchParameters(String var1, NodeSearchType var2, SearchDirection var3) {
      Preconditions.checkNotNull(var1, "searchString");
      Preconditions.checkNotNull(var2, "nodeType");
      Preconditions.checkNotNull(var3, "direction");
      this.searchString = var1;
      this.searchString_lowercase = var1.toLowerCase();
      this.nodeType = var2;
      this.direction = var3;
   }

   public String getSearchString() {
      return this.searchString;
   }

   public String getSearchString_lowercase() {
      return this.searchString_lowercase;
   }

   public NodeSearchType getNodeType() {
      return this.nodeType;
   }

   public SearchDirection getDirection() {
      return this.direction;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof NodeSearchParameters ? this.equals((NodeSearchParameters)var1) : false;
      }
   }

   private boolean equals(NodeSearchParameters var1) {
      return this.searchString.equalsIgnoreCase(var1.searchString) && this.nodeType == var1.nodeType && this.direction == var1.direction;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.searchString.toLowerCase().hashCode();
      var1 = 31 * var1 + this.nodeType.hashCode();
      return 31 * var1 + this.direction.hashCode();
   }

   @Override
   public String toString() {
      return "direction: " + this.direction + ", nodeType:" + this.nodeType + ", searchString:" + this.searchString;
   }
}
