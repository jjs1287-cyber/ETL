import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.DeployInterchangeDataServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataTable;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeIdHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControl;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControlServiceLocator;

public class EndpointCompressionLiveTest {
    static final Path ROOT = Paths.get("C:/ETL/Samples/EndpointCompression/live-" + UUID.randomUUID(), new String[0]);
    static int checks;
    static String hash;
    static final byte[] A;
    static final byte[] B;
    static final String[] ids;
    static ArrayList<String[]> props;

    static String id(int n) {
        if (ids[n] == null) {
            EndpointCompressionLiveTest.ids[n] = UUID.randomUUID().toString();
        }
        return ids[n];
    }

    static void check(boolean bl, String string) {
        if (!bl) {
            throw new AssertionError((Object)string);
        }
        ++checks;
        System.out.println("PASS " + string);
    }

    static InterchangeDataTable table(String[] stringArray, String[] ... stringArray2) {
        return StudioFileCopySmoke.table((String[])stringArray, (String[][])stringArray2);
    }

    static void tool(String ... stringArray) throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("C:/ETL/Tools/7zip/7z.exe");
        arrayList.addAll(Arrays.asList(stringArray));
        Process process = new ProcessBuilder(arrayList).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.to(new File("C:/ETL/Reports/endpoint-compression-independent-tool.txt"))).start();
        if (process.waitFor() != 0) {
            throw new AssertionError((Object)"Independent 7z tool failed");
        }
    }

    static void unpack(Path path, Path path2, String string) throws Exception {
        EndpointCompressionLiveTest.tool("x", "-y", "-p" + string, "-o" + path2, path.toString());
    }

    static Path out(int n) {
        return ROOT.resolve("output-" + n);
    }

    static void prop(String string, String string2, String string3, String string4) {
        props.add(new String[]{string, string2, string3, string4});
    }

    static void configure(int n, String string, boolean bl, String string2, String string3, String string4) {
        if (bl) {
            EndpointCompressionLiveTest.prop("2", EndpointCompressionLiveTest.id(10 + n), "compression_enabled", "true");
            if (!string2.isEmpty()) {
                EndpointCompressionLiveTest.prop("2", EndpointCompressionLiveTest.id(10 + n), "compression_password", Password.encrypt((String)string2).encrypted());
            }
        }
        if (!string3.isEmpty()) {
            EndpointCompressionLiveTest.prop("3", EndpointCompressionLiveTest.id(50 + n), "compression_enabled", "true");
            EndpointCompressionLiveTest.prop("3", EndpointCompressionLiveTest.id(50 + n), "compression_format", string3);
            EndpointCompressionLiveTest.prop("3", EndpointCompressionLiveTest.id(50 + n), "compression_mode", string4);
            EndpointCompressionLiveTest.prop("3", EndpointCompressionLiveTest.id(50 + n), "compression_archive_name", "bundle");
            if (n == 4) {
                EndpointCompressionLiveTest.prop("3", EndpointCompressionLiveTest.id(50 + n), "compression_password", Password.encrypt((String)"fixture-password").encrypted());
            }
        }
    }

    static void setup() throws Exception {
        String string;String[] stringArray;
        Files.createDirectories(ROOT);Files.write(ROOT.resolve("alpha.dat"),A);Files.write(ROOT.resolve("beta.dat"),B);
        try(ZipOutputStream z=new ZipOutputStream(Files.newOutputStream(ROOT.resolve("input.zip")))){z.putNextEntry(new ZipEntry("nested/alpha.dat"));z.write(A);z.closeEntry();z.putNextEntry(new ZipEntry("beta.dat"));z.write(B);z.closeEntry();}
        tool("a","-tzip","-mem=AES256","-pfixture-password",ROOT.resolve("encrypted.zip").toString(),ROOT.resolve("alpha.dat").toString(),ROOT.resolve("beta.dat").toString());
        tool("a","-t7z",ROOT.resolve("input.7z").toString(),ROOT.resolve("alpha.dat").toString(),ROOT.resolve("beta.dat").toString());
        try(GZIPOutputStream g=new GZIPOutputStream(Files.newOutputStream(ROOT.resolve("payload.txt.gz")))){g.write(A);}
        Files.copy(Paths.get("C:/ETL/Samples/EndpointCompression/native/leading-dot.tar.gz", new String[0]), ROOT.resolve("input.tar.gz"), new CopyOption[0]);
        String[] formats = new String[]{"", "zip", "zip", "7z", "zip", "tar.gz", "gz", "gz", "", "", "", "", "", "", ""};
        stringArray = new String[]{"input.zip", "input.zip", "input.zip", "input.zip", "input.zip", "input.zip", "input.zip", "input.zip", "encrypted.zip", "encrypted.zip", "alpha.dat", "input.zip", "input.7z", "payload.txt.gz", "input.tar.gz"};
        ArrayList<String[]> arrayList = new ArrayList<String[]>();
        ArrayList<String[]> arrayList2 = new ArrayList<String[]>();
        ArrayList<String[]> arrayList3 = new ArrayList<String[]>();
        ArrayList<String[]> arrayList4 = new ArrayList<String[]>();
        for (int i = 0; i < stringArray.length; ++i) {
            Files.createDirectories(EndpointCompressionLiveTest.out(i), new FileAttribute[0]);
            string = i == 2 || i == 6 ? "individual" : "combined";
            EndpointCompressionLiveTest.configure(i, stringArray[i], i != 11, i == 8 ? "fixture-password" : (i == 9 ? "wrong" : ""), formats[i], string);
            arrayList.add(new String[]{EndpointCompressionLiveTest.id(10 + i), "Compression source " + i, "16", ROOT.toString(), stringArray[i], null, null, null, null, null});
            arrayList2.add(new String[]{EndpointCompressionLiveTest.id(50 + i), "Compression target " + i, "16", EndpointCompressionLiveTest.out(i).toString(), i == 11 ? "copy.zip" : "entry_[file].[ext]", null, null, null, null, null, "-1"});
            arrayList3.add(new String[]{EndpointCompressionLiveTest.id(100 + i), "Compression verification " + i, "1", EndpointCompressionLiveTest.id(10 + i), EndpointCompressionLiveTest.id(50 + i), "1", null, null, null, null, null, null});
            arrayList4.add(new String[]{EndpointCompressionLiveTest.id(100 + i), "3", EndpointCompressionLiveTest.id(10 + i)});
            arrayList4.add(new String[]{EndpointCompressionLiveTest.id(100 + i), "4", EndpointCompressionLiveTest.id(50 + i)});
        }
        InterchangeDataHolder interchangeDataHolder = new InterchangeDataHolder();
        interchangeDataHolder.setInterchangeProjectsTab(EndpointCompressionLiveTest.table(new String[]{"InterchangeProject_ID", "InterchangeProject_Name"}, new String[][]{{EndpointCompressionLiveTest.id(1), "Compression verification " + ROOT.getFileName()}}));
        interchangeDataHolder.setSourcesTab(EndpointCompressionLiveTest.table(new String[]{"Source_ID", "Source_Name", "Source_Type_ID", "Locator", "File_Name", "Login", "Password", "Driver", "DriverType", "ConnectString"}, (String[][])arrayList.toArray(new String[0][])));
        interchangeDataHolder.setTargetsTab(EndpointCompressionLiveTest.table(new String[]{"Target_ID", "Target_Name", "Source_Type_ID", "Locator", "File_Name", "Login", "Password", "Driver", "DriverType", "ConnectString", "Target_Target_ID"}, (String[][])arrayList2.toArray(new String[0][])));
        interchangeDataHolder.setOperationsTab(EndpointCompressionLiveTest.table(new String[]{"Operation_ID", "Operation_Name", "Operation_Type_ID", "Source_ID", "Target_ID", "Ignore_History", "Schedule_ID", "Transformation_ID", "Success_Operation_ID", "Success_Message_ID", "Failure_Operation_ID", "Failure_Message_ID"}, (String[][])arrayList3.toArray(new String[0][])));
        interchangeDataHolder.setOperationPipelines(EndpointCompressionLiveTest.table(new String[]{"Operation_ID", "ActivityType_ID", "Activity_ID"}, (String[][])arrayList4.toArray(new String[0][])));
        interchangeDataHolder.setEntityProperties(EndpointCompressionLiveTest.table(new String[]{"EntityType_ID", "Entity_ID", "KeyName", "KeyValue"}, (String[][])props.toArray(new String[0][])));
        org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployResults deployed = new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin", hash, new InterchangeIdHolder(), interchangeDataHolder);
        EndpointCompressionLiveTest.check(deployed != null && deployed.getDeployStatus() <= 1, "deploy compression project and protected settings");
        Files.write(Paths.get("C:/ETL/Reports/endpoint-compression-live-root.txt", new String[0]), ROOT.toString().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
    }

    static String run(int n) throws Exception {
        JobControl jobControl = new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
        long l = jobControl.runOperationInstanceMD5Password("Admin", hash, EndpointCompressionLiveTest.id(100 + n)).getOperationInstanceId();
        for (int i = 0; i < 600; ++i) {
            String string = NativeJavaScriptMappingTest.status(l);
            if (!(string.isEmpty() || string.equals("0") || string.equals("2"))) {
                System.out.println("instance=" + l + " fixture=" + n + " status=" + string);
                return string;
            }
            Thread.sleep(100L);
        }
        throw new AssertionError((Object)("Operation timed out " + l));
    }

    static void bytes(Path path, byte[] byArray, String string) throws Exception {
        EndpointCompressionLiveTest.check(Files.exists(path, new LinkOption[0]) && Arrays.equals(Files.readAllBytes(path), byArray), string);
    }

 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;setup();
  for(int n=0;n<15;n++){
   String status=run(n);if(n==7||n==9||n==10){check(status.equals("6"),"invalid archive/password/combined GZIP fails cleanly "+n);try(java.util.stream.Stream<Path> f=Files.list(out(n))){check(f.count()==0,"failure delivered no partial output "+n);}continue;}
   check(status.equals("3"),"operation succeeded "+n);
   if(n==11){bytes(out(n).resolve("copy.zip"),Files.readAllBytes(ROOT.resolve("input.zip")),"compression disabled preserves original ZIP");continue;}
   if(n==13){bytes(out(n).resolve("entry_payload.txt"),A,"GZIP auto-detection and original filename");continue;}
   if(n==14){bytes(out(n).resolve("entry_input.txt"),"hello".getBytes(StandardCharsets.UTF_8),"TAR.GZ auto-detection handles ./ paths");continue;}
   if(n==0||n==8||n==12){bytes(out(n).resolve("entry_alpha.dat"),A,"extracted Unicode text "+n);bytes(out(n).resolve("entry_beta.dat"),B,"extracted binary bytes "+n);continue;}
   if(n==2||n==6){String ext=n==2?"zip":"gz";for(String leaf:new String[]{"alpha","beta"}){Path unpacked=ROOT.resolve("verify-"+n+"-"+leaf);unpack(out(n).resolve("entry_"+leaf+".dat."+ext),unpacked,"");bytes(unpacked.resolve("entry_"+leaf+".dat"),leaf.equals("alpha")?A:B,"independent individual archive bytes "+n+" "+leaf);}continue;}
   String ext=n==3?"7z":n==5?"tar.gz":"zip";Path unpacked=ROOT.resolve("verify-"+n);unpack(out(n).resolve("bundle."+ext),unpacked,n==4?"fixture-password":"");
   if(n==5){Path tar;try(java.util.stream.Stream<Path> f=Files.list(unpacked)){tar=f.findFirst().get();}Path second=ROOT.resolve("verify-tar");unpack(tar,second,"");unpacked=second;}
   bytes(unpacked.resolve("entry_alpha.dat"),A,"independent combined archive text "+n);bytes(unpacked.resolve("entry_beta.dat"),B,"independent combined archive binary "+n);
  }
  check(Files.exists(ROOT.resolve("input.zip")),"source archive preserved");System.out.println("Passed "+checks+" live compression checks; fixtures="+ROOT);
 }
    static {
        A = "caf\u00e9 \u4e16\u754c \ud83d\ude00\r\n".getBytes(StandardCharsets.UTF_8);
        B = new byte[]{0, 1, 2, -1, -128, 10, 13};
        ids = new String[200];
        props = new ArrayList();
    }
}
