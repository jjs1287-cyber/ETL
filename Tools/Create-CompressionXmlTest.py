from pathlib import Path
s=Path('C:/ETL/Tools/Add-CompressionXmlTest.py').read_text(encoding='utf-8');block=s.split("'''",2)[1];block=block[:block.rfind(' static String run')]
p=Path('C:/ETL/Server/tests/integration/EndpointCompressionXmlLiveTest.java');p.write_text('''import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;
import java.net.URL;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
public class EndpointCompressionXmlLiveTest extends EndpointCompressionLiveTest {
'''+block+'''
 public static void main(String[] args)throws Exception{LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;xmlMapping();System.out.println("Passed "+checks+" live XML compression checks");}
}
''',encoding='utf-8')
