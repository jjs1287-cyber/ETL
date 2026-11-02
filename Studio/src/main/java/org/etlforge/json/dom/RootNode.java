package org.etlforge.json.dom;

public interface RootNode extends JsonNode {
   Iterable<? extends JsonNode> getChildren();
}
