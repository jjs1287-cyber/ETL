package org.etlforge.json.dom;

public abstract class AbstractJsonNode implements JsonNode {
   private JsonNode parent;

   public final void setParent(JsonNode var1) {
      this.parent = var1;
   }

   @Override
   public final JsonNode getParent() {
      return this.parent;
   }
}
