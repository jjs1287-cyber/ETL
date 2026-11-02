import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.etlforge.integration.json.xml.JsonToXmlGenerator;
import org.etlforge.integration.json.xml.XmlToJsonGenerator;
import org.etlforge.integration.json.xml.JsonToXsdGenerator;
import org.etlforge.io.Folder;

public final class NativeJsonParityTest {
    static final Path ROOT = Paths.get("C:/ETL/Samples/NativeJsonParity");
    static final String CLI = "C:/ETL/Server/build-probe/native-json/JsonConversionCli.exe";
    static final ObjectMapper MAPPER = new ObjectMapper();
    static int checks;
    static void write(Path file, String text) throws Exception { Files.write(file, text.getBytes(StandardCharsets.UTF_8)); }
    static void check(boolean ok, String description) { if (!ok) throw new AssertionError(description); checks++; }
    static int nativeRun(String mode, Path input, Path xsd, Path output) throws Exception {
        Process process = new ProcessBuilder(CLI, mode, input.toString(), xsd.toString(), output.toString()).redirectErrorStream(true).start();
        ByteArrayOutputStream log = new ByteArrayOutputStream(); byte[] bytes = new byte[4096]; int n;
        while ((n = process.getInputStream().read(bytes)) != -1) log.write(bytes, 0, n);
        int status = process.waitFor();
        if (status != 0) System.out.println("Native rejection: " + new String(log.toByteArray(), StandardCharsets.UTF_8));
        return status;
    }
    static Element xml(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(file.toFile()).getDocumentElement();
    }
    static List<Element> children(Element element) {
        List<Element> result = new ArrayList<>();
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
            if (child instanceof Element) result.add((Element)child);
        return result;
    }
    static String canonical(Element element) {
        StringBuilder out = new StringBuilder(element.getTagName()).append('{');
        TreeMap<String,String> attributes = new TreeMap<>();
        for (int i=0; i<element.getAttributes().getLength(); i++) {
            Node attr = element.getAttributes().item(i); attributes.put(attr.getNodeName(), attr.getNodeValue());
        }
        out.append(attributes).append('}');
        List<Element> items = children(element);
        if (items.isEmpty()) out.append(MAPPER.valueToTree(element.hasAttribute("object") || element.hasAttribute("array") ? "" : element.getTextContent()).toString());
        else for (Element child : items) out.append('[').append(canonical(child)).append(']');
        return out.toString();
    }
    static JsonNode json(Path file) throws Exception { return MAPPER.readTree(file.toFile()); }
    static void javaToXml(Path input, Path xsd, Path output) throws Exception {
        JsonToXmlGenerator generator = new JsonToXmlGenerator(); generator.setXsdFile(xsd.toFile()); generator.generateXml(input.toFile(), output.toFile());
    }
    static void javaToJson(Path input, Path xsd, Path output) throws Exception {
        XmlToJsonGenerator generator = new XmlToJsonGenerator(); generator.setOutputFile(output.toFile()); generator.convertToJson(input.toFile(), xsd.toFile());
    }
    static String repeated() {
        StringBuilder out = new StringBuilder("{\"rows\":[");
        for(int i=0;i<2500;i++) {
            if(i>0) out.append(',');
            out.append("{\"id\":").append(i).append(",\"description\":\"Order & item ").append(i).append("\",\"amount\":12.5,\"active\":true}");
        }
        return out.append("]}").toString();
    }
    static String schemaElement(String name, JsonNode value) {
        String open="<xs:element name=\""+name+"\"";
        if(value.isContainerNode()) {
            StringBuilder out=new StringBuilder(open+"><xs:complexType><xs:sequence>");
            if(value.isObject()) {
                Iterator<Map.Entry<String,JsonNode>> fields=value.fields();
                while(fields.hasNext()) { Map.Entry<String,JsonNode> field=fields.next(); out.append(schemaElement(field.getKey(),field.getValue())); }
            } else for(JsonNode child:value) out.append(schemaElement(child.isArray()?"array":"item",child));
            return out.append("</xs:sequence></xs:complexType></xs:element>").toString();
        }
        return open+" type=\"xs:"+(value.isBoolean()?"boolean":value.isNumber()?"double":"string")+"\"/>";
    }
    public static void main(String[] args) throws Exception {
        Files.createDirectories(ROOT);
        String[][] cases = {
            {"01-flat-scalars", "{\"name\":\"Ada\",\"age\":42,\"active\":true,\"disabled\":false,\"balance\":12.5,\"missing\":null}"},
            {"02-nested-objects", "{\"customer\":{\"id\":7,\"address\":{\"city\":\"Austin\",\"zip\":\"00767\"}},\"metadata\":{\"version\":1}}"},
            {"03-record-array", "[{\"id\":1,\"name\":\"A\"},{\"id\":2,\"name\":\"B\"},{\"id\":3,\"name\":\"C\"}]"},
            {"04-scalar-arrays", "{\"strings\":[\"a\",\"b\",\"\"],\"numbers\":[1,2,3],\"flags\":[true,false],\"nullable\":[null,\"value\",null]}"},
            {"05-nested-arrays", "{\"matrix\":[[1,2],[3,4]],\"mixed\":[\"text\",7,true,null,{\"code\":\"x\"}]}"},
            {"06-empty-values", "{\"emptyString\":\"\",\"emptyObject\":{},\"emptyArray\":[],\"nil\":null,\"records\":[{},{}]}"},
            {"07-unicode-escapes", "{\"text\":\"caf\u00e9 \u4e2d\u6587 & < > \\\" ' \\\\ line\\nnext\\tcol\",\"spaced\":\"  keep spaces  \"}"},
            {"08-field-names", "{\"first name\":\"Ada\",\"123\":\"digits\",\"a:b\":7,\"xmlValue\":true,\"a-b.c_d\":\"ok\",\"\u00e9cole\":\"yes\"}"},
            {"09-numeric-boundaries", "{\"minLong\":-9223372036854775808,\"maxLong\":9223372036854775807,\"big\":123456789012345678901234567890,\"fraction\":0.125,\"small\":1e-8,\"large\":1e20,\"negativeZero\":-0.0,\"scaled\":12.500}"},
            {"10-large-record-set", repeated()}
        };
        StringBuilder report = new StringBuilder("Fixture,JSON_to_XML,XML_to_JSON,Cross_roundtrip,Bytes\r\n");
        for(String[] item : cases) {
            Path dir = ROOT.resolve(item[0]); Files.createDirectories(dir);
            Path input=dir.resolve("input.json"), xsd=dir.resolve("format.xsd"), javaXml=dir.resolve("java.xml"), cppXml=dir.resolve("cpp.xml"),
                javaJson=dir.resolve("java.json"), cppJson=dir.resolve("cpp.json"), cross=dir.resolve("java-from-cpp.json");
            write(input, item[1]);
            if (!Files.exists(xsd)) {
                JsonToXsdGenerator schema = new JsonToXsdGenerator(); schema.setXsdName("format.xsd");
                Path schemaDir = dir.resolve("generated-schema"); Files.createDirectories(schemaDir);
                try {
                    File generated = schema.generateXsd(input.toFile(), new Folder(schemaDir.toFile()));
                    Files.copy(generated.toPath(), xsd, StandardCopyOption.REPLACE_EXISTING);
                } catch(UnsupportedOperationException unsupported) {
                    write(xsd,"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"+schemaElement("json",json(input))+"</xs:schema>");
                    write(dir.resolve("schema-generation-note.txt"),"Legacy schema inference rejects this heterogeneous array. Explicit path/type test schema supplied to both conversion implementations.\n");
                }
            }
            javaToXml(input,xsd,javaXml);
            check(nativeRun("json-to-xml",input,xsd,cppXml)==0,item[0]+" native JSON-to-XML failed");
            String expected=canonical(xml(javaXml)), actual=canonical(xml(cppXml));
            if (!expected.equals(actual)) {
                write(dir.resolve("xml-comparison.txt"), "JAVA\n"+expected+"\nCPP\n"+actual);
            } else Files.deleteIfExists(dir.resolve("xml-comparison.txt"));
            check(expected.equals(actual),item[0]+" XML mismatch (see xml-comparison.txt)");
            javaToJson(javaXml,xsd,javaJson);
            check(nativeRun("xml-to-json",javaXml,xsd,cppJson)==0,item[0]+" native XML-to-JSON failed");
            check(json(javaJson).equals(json(cppJson)),item[0]+" JSON mismatch");
            javaToJson(cppXml,xsd,cross);
            check(json(javaJson).equals(json(cross)),item[0]+" Java cannot consume native XML equivalently");
            check(nativeRun("xml-to-json",cppXml,xsd,cppJson)==0,item[0]+" native roundtrip failed");
            check(json(javaJson).equals(json(cppJson)),item[0]+" native roundtrip mismatch");
            report.append(item[0]).append(",PASS,PASS,PASS,").append(Files.size(input)).append("\r\n");
            System.out.println("PASS " + item[0]);
        }
        Path extra=ROOT.resolve("edge-contracts"); Files.createDirectories(extra);
        Path xsd=ROOT.resolve("01-flat-scalars/format.xsd"), input=extra.resolve("bad.json"), output=extra.resolve("result.xml");
        String[] invalid={"{\"x\":}","[1,]","{\"x\":1", "{\"x\":1} garbage"};
        for(String bad:invalid) {
            write(input,bad); boolean rejected=false;
            try { javaToXml(input,xsd,extra.resolve("java-bad.xml")); } catch(Exception expected) { rejected=true; }
            check(rejected,"Java unexpectedly accepted invalid fixture");
            check(nativeRun("json-to-xml",input,xsd,output)!=0,"Native accepted invalid JSON");
        }
        write(extra.resolve("external.xml"), "<!DOCTYPE json [<!ENTITY secret SYSTEM 'file:///C:/Windows/win.ini'>]><json object=\"true\"><name>&secret;</name></json>");
        check(nativeRun("xml-to-json",extra.resolve("external.xml"),xsd,extra.resolve("external.json"))!=0,"Native expanded external entity");
        Path duplicate=extra.resolve("duplicate.json"); write(duplicate,"{\"name\":\"first\",\"name\":\"second\"}");
        javaToXml(duplicate,xsd,extra.resolve("duplicate-java.xml"));
        check(nativeRun("json-to-xml",duplicate,xsd,extra.resolve("duplicate-cpp.xml"))==0,"Duplicate key parse failed");
        check(canonical(xml(extra.resolve("duplicate-java.xml"))).equals(canonical(xml(extra.resolve("duplicate-cpp.xml")))),"Duplicate key ordering lost");
        Path simpleSchema=extra.resolve("simple-array.xsd"), simpleInput=extra.resolve("simple-array.json");
        write(simpleSchema,"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><xs:element name=\"json\"><xs:complexType><xs:sequence><xs:element name=\"id\" type=\"xs:long\"/><xs:element name=\"name\" type=\"xs:string\"/></xs:sequence><xs:attribute name=\"simple\" type=\"xs:boolean\"/></xs:complexType></xs:element></xs:schema>");
        write(simpleInput,"[{\"id\":1,\"name\":\"A\"},{\"id\":2,\"name\":\"B\"}]");
        javaToXml(simpleInput,simpleSchema,extra.resolve("simple-java.xml"));
        check(nativeRun("json-to-xml",simpleInput,simpleSchema,extra.resolve("simple-cpp.xml"))==0,"Simple array conversion failed");
        check(canonical(xml(extra.resolve("simple-java.xml"))).equals(canonical(xml(extra.resolve("simple-cpp.xml")))),"Simple array XML mismatch");
        javaToJson(extra.resolve("simple-java.xml"),simpleSchema,extra.resolve("simple-java.json"));
        check(nativeRun("xml-to-json",extra.resolve("simple-java.xml"),simpleSchema,extra.resolve("simple-cpp.json"))==0,"Simple array reverse conversion failed");
        check(json(extra.resolve("simple-java.json")).equals(json(extra.resolve("simple-cpp.json"))),"Simple array reverse mismatch");
        Path edited=extra.resolve("edited.xml");
        write(edited,"<json object=\"1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><name>  spaced &amp; value  </name><age/><active>1</active><disabled>0</disabled><balance>12.500</balance><missing xsi:nil=\"1\"/></json>");
        javaToJson(edited,xsd,extra.resolve("edited-java.json"));
        check(nativeRun("xml-to-json",edited,xsd,extra.resolve("edited-cpp.json"))==0,"Edited XML reverse conversion failed");
        check(json(extra.resolve("edited-java.json")).equals(json(extra.resolve("edited-cpp.json"))),"Schema types/nulls/boolean lexical forms mismatch");
        Path emptyKey=extra.resolve("empty-key.json"); write(emptyKey,"{\"\":\"empty key\"}");
        javaToXml(emptyKey,xsd,extra.resolve("empty-key-java.xml"));
        check(nativeRun("json-to-xml",emptyKey,xsd,extra.resolve("empty-key-cpp.xml"))==0,"Empty field name conversion failed");
        check(canonical(xml(extra.resolve("empty-key-java.xml"))).equals(canonical(xml(extra.resolve("empty-key-cpp.xml")))),"Empty field name mismatch");
        Path supplementary=extra.resolve("supplementary.json"); write(supplementary,"{\"name\":\"\uD83D\uDE03\"}");
        javaToXml(supplementary,xsd,extra.resolve("supplementary-java.xml"));
        boolean invalidJavaXml=false;
        try { xml(extra.resolve("supplementary-java.xml")); } catch(Exception legacyBug) { invalidJavaXml=true; }
        check(invalidJavaXml,"Legacy supplementary-character behavior changed; review native compatibility");
        check(nativeRun("json-to-xml",supplementary,xsd,extra.resolve("supplementary-cpp.xml"))==0,"Supplementary Unicode native conversion failed");
        check(children(xml(extra.resolve("supplementary-cpp.xml"))).get(0).getTextContent().equals("\uD83D\uDE03"),"Native supplementary Unicode was corrupted");
        Path tricky=extra.resolve("legacy-double-boundaries.json"); write(tricky,"{\"large\":1e23,\"tiny\":4.9e-324}");
        javaToXml(tricky,xsd,extra.resolve("legacy-double-java.xml"));
        check(nativeRun("json-to-xml",tricky,xsd,extra.resolve("legacy-double-cpp.xml"))==0,"Boundary double conversion failed");
        String javaBoundary=canonical(xml(extra.resolve("legacy-double-java.xml"))), cppBoundary=canonical(xml(extra.resolve("legacy-double-cpp.xml")));
        write(extra.resolve("legacy-double-compatibility.txt"),"Exact XML scalar text matches: "+javaBoundary.equals(cppBoundary)+"\nJAVA\n"+javaBoundary+"\nCPP\n"+cppBoundary+"\n");
        check(javaBoundary.equals(cppBoundary),"Legacy floating formatting mismatch (see legacy-double-compatibility.txt)");
        Random random=new Random(42);
        StringBuilder numericSweep=new StringBuilder("{\"values\":[");
        int generated=0;
        while(generated<2000) {
            double value=Double.longBitsToDouble(random.nextLong());
            if(Double.isNaN(value)||Double.isInfinite(value)) continue;
            if(generated++>0) numericSweep.append(',');
            numericSweep.append(Double.toString(value));
        }
        numericSweep.append("]}");
        Path sweep=extra.resolve("numeric-sweep.json"); write(sweep,numericSweep.toString());
        javaToXml(sweep,xsd,extra.resolve("numeric-sweep-java.xml"));
        check(nativeRun("json-to-xml",sweep,xsd,extra.resolve("numeric-sweep-cpp.xml"))==0,"Numeric sweep native conversion failed");
        List<Element> referenceNumbers=children(children(xml(extra.resolve("numeric-sweep-java.xml"))).get(0));
        List<Element> nativeNumbers=children(children(xml(extra.resolve("numeric-sweep-cpp.xml"))).get(0));
        StringBuilder numericDifferences=new StringBuilder();
        for(int i=0;i<referenceNumbers.size();i++) {
            String ref=referenceNumbers.get(i).getTextContent(), nat=nativeNumbers.get(i).getTextContent();
            if(!ref.equals(nat)) numericDifferences.append(i).append(": ").append(ref).append(" versus ").append(nat).append('\n');
        }
        write(extra.resolve("numeric-sweep-differences.txt"),numericDifferences.toString());
        check(numericDifferences.length()==0,"Numeric formatting sweep mismatch (see numeric-sweep-differences.txt)");
        StringBuilder largeText=new StringBuilder();for(int i=0;i<20000;i++)largeText.append("café 日本 😀 & ");
        String longValue=largeText.toString();Path splitXml=extra.resolve("sax-boundaries.xml"),splitJson=extra.resolve("sax-boundaries.json");
        write(splitXml,"<json object='true'><name>"+longValue.replace("&","&amp;")+"</name></json>");
        check(nativeRun("xml-to-json",splitXml,xsd,splitJson)==0,"SAX boundary conversion failed");
        check(json(splitJson).get("name").asText().equals(longValue),"SAX boundaries split Unicode characters");
        Path cdata=extra.resolve("sax-cdata.xml");write(cdata,"<json object='true'><name>before<![CDATA[ café 日本 ]]>&amp; after</name></json>");
        javaToJson(cdata,xsd,extra.resolve("sax-cdata-java.json"));
        check(nativeRun("xml-to-json",cdata,xsd,extra.resolve("sax-cdata-cpp.json"))==0,"CDATA conversion failed");
        check(json(extra.resolve("sax-cdata-java.json")).equals(json(extra.resolve("sax-cdata-cpp.json"))),"CDATA text mismatch");
        for(String encoding:new String[]{"UTF-16LE","UTF-16BE","UTF-32LE","UTF-32BE"}){
            Path dtd=extra.resolve("forbidden-dtd-"+encoding+".xml");Files.write(dtd,("<?xml version='1.0' encoding='"+encoding+"'?><!DOCTYPE json [<!ENTITY e 'secret'>]><json>&e;</json>").getBytes(java.nio.charset.Charset.forName(encoding)));
            check(nativeRun("xml-to-json",dtd,xsd,extra.resolve("forbidden.json"))!=0,"Encoded DTD was accepted: "+encoding);
        }
        Path nested=extra.resolve("too-deep.xml");StringBuilder deep=new StringBuilder();for(int i=0;i<520;i++)deep.append("<n>");for(int i=0;i<520;i++)deep.append("</n>");write(nested,deep.toString());
        check(nativeRun("xml-to-json",nested,xsd,extra.resolve("too-deep.json"))!=0,"Excessive XML nesting accepted");
        write(Paths.get("C:/ETL/Reports/native-json-parity.csv"),report.toString());
        System.out.println("PASS: "+checks+" checks; 10 formats, both directions, cross-consumption, malformed input, duplicate keys, external-entity rejection.");
    }
}
