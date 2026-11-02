import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.json.xml.*;
public final class NativeJsonBenchmark {
 public static void main(String[] args)throws Exception {
  Path root=Paths.get("C:/ETL/Samples/NativeJsonParity");List<String> rows=new ArrayList<>();rows.add("Fixture,Direction,Java_warm_median_ms,Native_warm_median_ms,Native_peak_working_set_bytes");
  try(java.util.stream.Stream<Path> files=Files.list(root)){
   for(Path dir:(Iterable<Path>)files.filter(p->p.getFileName().toString().matches("[0-9]{2}-.*" )).sorted()::iterator){
    Path schema=dir.resolve("format.xsd");
    for(boolean source:new boolean[]{true,false}){
     Path input=dir.resolve(source?"input.json":"java.xml"),output=dir.resolve(source?"bench-java.xml":"bench-java.json"),nativeOutput=dir.resolve(source?"bench-native.xml":"bench-native.json");
     if(!source && !Files.exists(input))input=dir.resolve("bench-java.xml");
     double[] samples=new double[7];
     for(int i=-1;i<7;i++){long start=System.nanoTime();if(source){JsonToXmlGenerator g=new JsonToXmlGenerator();g.setXsdFile(schema.toFile());g.generateXml(input.toFile(),output.toFile());}else{XmlToJsonGenerator g=new XmlToJsonGenerator();g.setOutputFile(output.toFile());g.convertToJson(input.toFile(),schema.toFile());}if(i>=0)samples[i]=(System.nanoTime()-start)/1000000.0;}
     Arrays.sort(samples);
     Process process=new ProcessBuilder("C:/ETL/Server/build-probe/native-json/JsonConversionCli.exe",source?"bench-json-to-xml":"bench-xml-to-json",input.toString(),schema.toString(),nativeOutput.toString()).redirectErrorStream(true).start();
     java.io.ByteArrayOutputStream log=new java.io.ByteArrayOutputStream();byte[] buffer=new byte[4096];int n;while((n=process.getInputStream().read(buffer))!=-1)log.write(buffer,0,n);if(process.waitFor()!=0)throw new AssertionError(log.toString("UTF-8"));String nativeValues=log.toString("UTF-8").trim();
     if(source){if(!NativeJsonParityTest.canonical(NativeJsonParityTest.xml(output)).equals(NativeJsonParityTest.canonical(NativeJsonParityTest.xml(nativeOutput))))throw new AssertionError("Benchmark XML mismatch");}else{com.fasterxml.jackson.databind.ObjectMapper mapper=new com.fasterxml.jackson.databind.ObjectMapper();if(!mapper.readTree(output.toFile()).equals(mapper.readTree(nativeOutput.toFile())))throw new AssertionError("Benchmark JSON mismatch");}
     rows.add(dir.getFileName()+","+(source?"JSON-to-XML":"XML-to-JSON")+","+samples[3]+","+nativeValues);
    }
   }
  }
  Files.write(Paths.get("C:/ETL/Reports/native-json-speed.csv"),rows,StandardCharsets.UTF_8);System.out.println("PASS: "+(rows.size()-1)+" warmed conversion benchmarks; every measured output matches Java.");
 }
}
