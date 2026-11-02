package org.etlforge.json.dom;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.etlforge.json.parser.StreamingJsonParser;

public final class JsonDomParser {
   private RootNode root;
   private Stack<JsonNode> stack;
   private StreamingJsonParser parser;
   private boolean skipDuplicateStructures;

   public void setSkipDuplicateStructures(boolean var1) {
      this.skipDuplicateStructures = var1;
   }

   public RootNode parseJson(String var1) throws JsonParseException, IOException {
      this.parser = StreamingJsonParser.parseJson(var1);
      return this.parseImpl();
   }

   public RootNode parse(File var1) throws JsonParseException, IOException {
      this.parser = StreamingJsonParser.parseFile(var1);
      return this.parseImpl();
   }

   private RootNode parseImpl() throws JsonParseException, IOException {
      try {
         this.stack = new Stack<>();

         for (JsonToken var1 = this.parser.nextToken(); var1 != null; var1 = this.parser.nextToken()) {
            this.handleToken(var1);
         }

         return this.getRoot();
      } finally {
         this.parser.close();
      }
   }

   private void handleToken(JsonToken var1) throws JsonParseException, IOException {
      switch (var1) {
         case START_ARRAY:
            this.pushToStack(new ArrayNode(this.skipDuplicateStructures));
            break;
         case START_OBJECT:
            this.pushToStack(new ObjectNode(this.parser.getCurrentName()));
            break;
         case FIELD_NAME:
            this.handleFieldName();
            break;
         case END_OBJECT:
            this.handleEndObject();
            break;
         case END_ARRAY:
            this.handleEndArray();
            break;
         default:
            if (var1.isScalarValue()) {
               this.handleScalar(var1);
            }
      }
   }

   private void pushToStack(AbstractJsonNode var1) {
      this.stack.push(var1);
   }

   private void handleFieldName() throws JsonParseException, IOException {
      if (!this.stack.isEmpty() && this.stack.peek() instanceof ObjectNode) {
         FieldNode var1 = new FieldNode(this.parser.getCurrentName());
         this.pushToStack(var1);
      } else {
         throw new JsonParseException("Field in unexpected position", this.parser.getCurrentLocation());
      }
   }

   private void handleScalar(JsonToken var1) throws JsonParseException, IOException {
      ScalarValueNode var2 = this.getScalarValueNode(var1);
      this.addValueNodeToOwner(var2);
   }

   private ScalarValueNode getScalarValueNode(JsonToken var1) throws JsonParseException, IOException {
      assert var1.isScalarValue();
      switch (var1) {
         case VALUE_STRING:
            return ScalarValueNode.stringValue(this.parser.getText());
         case VALUE_NUMBER_INT:
         case VALUE_NUMBER_FLOAT:
            return ScalarValueNode.numberValue(this.parser.getNumberValue());
         case VALUE_NULL:
            return ScalarValueNode.NULL;
         case VALUE_FALSE:
            return ScalarValueNode.FALSE;
         case VALUE_TRUE:
            return ScalarValueNode.TRUE;
         default:
            throw new RuntimeException("Unepexted token: " + var1);
      }
   }

   private void addValueNodeToOwner(ValueNode var1) throws JsonParseException {
      if (!this.stack.isEmpty()) {
         JsonNode var2 = this.stack.peek();
         if (var2 instanceof FieldNode) {
            FieldNode var3 = (FieldNode)var2;
            var3.setValue(var1);
            this.stack.pop();
            ObjectNode var4 = (ObjectNode)this.stack.peek();
            var4.addField(var3);
            return;
         }

         if (var2 instanceof ArrayNode) {
            ((ArrayNode)var2).addItem(var1);
            return;
         }
      }

      throw new JsonParseException("The JSON document is invalid. Scalar value in unexpected position.", this.parser.getCurrentLocation());
   }

   private void handleEndObject() throws JsonParseException, IOException {
      ObjectNode var1 = (ObjectNode)this.stack.pop();
      if (this.stack.isEmpty()) {
         this.root = var1;
      } else {
         this.addValueNodeToOwner(var1);
      }
   }

   private void handleEndArray() throws JsonParseException, IOException {
      ArrayNode var1 = (ArrayNode)this.stack.pop();
      if (this.stack.isEmpty()) {
         this.root = var1;
      } else {
         this.addValueNodeToOwner(var1);
      }
   }

   private RootNode getRoot() throws RuntimeException {
      if (!this.stack.isEmpty()) {
         throw new RuntimeException("Multiple roots.");
      } else if (this.root == null) {
         throw new RuntimeException("No root found.");
      } else {
         return this.root;
      }
   }
}
