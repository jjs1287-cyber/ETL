package org.etlforge.integration.xslt;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class EtlForgeXsltRuntime {
    private EtlForgeXsltRuntime() {}

    public static String[] executeStylesheet(Path stylesheet, String xml) throws Exception {
        if (stylesheet == null) {
            throw new IllegalArgumentException("Stylesheet is required.");
        }
        return executeStylesheet(new String(Files.readAllBytes(stylesheet.toRealPath()), StandardCharsets.UTF_8), xml);
    }

    public static String[] executeStylesheet(String stylesheet, String... documents) throws Exception {
        if (stylesheet == null || stylesheet.trim().isEmpty() || documents == null || documents.length == 0) {
            throw new IllegalArgumentException("RunXSLT requires a stylesheet and at least one XML document.");
        }
        final String[] inputs = documents.clone();
        for (String input : inputs) {
            if (input == null) throw new IllegalArgumentException("XML documents must not be null.");
        }
        TransformerFactory factory = TransformerFactory.newInstance(
            "net.sf.saxon.TransformerFactoryImpl", EtlForgeXsltRuntime.class.getClassLoader());
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute("http://saxon.sf.net/feature/allowedProtocols", "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        factory.setURIResolver((href, base) -> {
            throw new TransformerException("Stylesheet imports must be deployed project assets: " + href);
        });
        StreamSource style = new StreamSource(new StringReader(stylesheet));
        style.setSystemId("urn:etlforge:xslt:stylesheet");
        Transformer transformer = factory.newTransformer(style);
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
        transformer.setURIResolver((href, base) -> resolveInputDocument(href, inputs));
        if (inputs.length > 1) {
            transformer.getClass().getMethod("setInitialTemplate", String.class).invoke(transformer, "start_here");
        }
        StringWriter output = new StringWriter();
        transformer.transform(inputSource(inputs[0], 1), new StreamResult(output));
        return new String[] { output.toString() };
    }

    private static Source resolveInputDocument(String href, String[] inputs) throws TransformerException {
        for (int i = 0; i < inputs.length; i++) {
            if (("param" + (i + 1)).equals(href) || ("urn:etlforge:xslt:param" + (i + 1)).equals(href)) {
                return inputSource(inputs[i], i + 1);
            }
        }
        throw new TransformerException("XSLT document access is restricted to supplied param1..param" + inputs.length + ".");
    }

    private static StreamSource inputSource(String xml, int number) {
        StreamSource source = new StreamSource(new StringReader(xml));
        source.setSystemId("urn:etlforge:xslt:param" + number);
        return source;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: EtlForgeXsltRuntime stylesheet input.xml [input2.xml ...] output.xml [error.txt]");
        }
        int outputIndex = args.length == 3 ? 2 : args.length - 2;
        try {
            String[] inputs = new String[outputIndex - 1];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = new String(Files.readAllBytes(Paths.get(args[i + 1])), StandardCharsets.UTF_8);
            }
            String stylesheet = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
            String[] result = executeStylesheet(stylesheet, inputs);
            Files.write(Paths.get(args[outputIndex]), result[0].getBytes(StandardCharsets.UTF_8));
        } catch (Exception failure) {
            if (args.length > 3) {
                String message = failure.getMessage();
                if (message == null) message = failure.getClass().getSimpleName();
                Files.write(Paths.get(args[args.length - 1]), message.getBytes(StandardCharsets.UTF_8));
            }
            throw failure;
        }
    }
}
