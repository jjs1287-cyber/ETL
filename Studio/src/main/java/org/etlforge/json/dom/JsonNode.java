package org.etlforge.json.dom;

public interface JsonNode {
   JsonNode getParent();

   JsonNode getChildAt(int var1);

   int getFingerprint();
}
