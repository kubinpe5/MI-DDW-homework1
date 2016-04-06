package homework1.GATE;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

/**
 *
 * @author Petr Kubín
 */
public class GateClient {

    public GateClient() {
    }

    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;

    // whether the GATE is initialised
    private static boolean isGateInitilised = false;

    public Map<String, ItemFeatures> run(String text) throws FileNotFoundException, IOException, GateException {
        Map<String, ItemFeatures> output = new HashMap<>();

        if (!isGateInitilised) {
            initialiseGate();
        }

        ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");
        ProcessingResource annieSentenceSpilitting = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
        ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
        ProcessingResource posTagger = (ProcessingResource) Factory.createResource("gate.creole.POSTagger");

        FeatureMap keywordExtractionFeatureMap = Factory.newFeatureMap();
        keywordExtractionFeatureMap.put("apiKey", "38abe3658e4b5af734d2c829670200138aef9265");
        ProcessingResource keywordExtraction = (ProcessingResource) Factory.createResource("gate.alchemyAPI.KeywordExtraction", keywordExtractionFeatureMap);

        //ProcessingResource keywordExtraction = (ProcessingResource) Factory.createResource("gate.alchemyAPI.KeywordExtraction");
        annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");
        annotationPipeline.add(documentResetPR);
        annotationPipeline.add(annieSentenceSpilitting);
        annotationPipeline.add(tokenizerPR);
        annotationPipeline.add(posTagger);
        annotationPipeline.add(keywordExtraction);

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Petr\\Praha\\Magistr\\2.semestr\\DDW\\homework1\\filename.txt"));

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String everything = sb.toString();
        Document document = Factory.newDocument(everything);

        Corpus corpus = Factory.newCorpus("");
        corpus.add(document);
        annotationPipeline.setCorpus(corpus);
        annotationPipeline.execute();

        for (int i = 0; i < corpus.size(); i++) {
            Document doc = corpus.get(i);
            AnnotationSet as_default = doc.getAnnotations();
            FeatureMap futureMap = null;
            AnnotationSet annSetTokens = as_default.get("Keyword", futureMap);
            //System.out.println("Number of keywords: " + annSetTokens.size());
            ArrayList tokenAnnotations = new ArrayList(annSetTokens);

            for (int j = 0; j < tokenAnnotations.size(); ++j) {
                Annotation token = (Annotation) tokenAnnotations.get(j);
                Node isaStart = token.getStartNode();
                Node isaEnd = token.getEndNode();
                String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();

                if (output.containsKey(underlyingString)) {
                    output.get(underlyingString).increment();
                } else {
                    output.put(underlyingString, new ItemFeatures(Double.parseDouble((String) token.getFeatures().get("relevance")), 1));
                }

                //System.out.println("Keyword: " + underlyingString + " with relevance: " + token.getFeatures().get("relevance") );
                FeatureMap annFM = token.getFeatures();
            }
            System.out.println(output);
            //System.out.println("Number of Keyword annotations: " + annSetTokens.size());
        }

        return output;
    }

    public Map<String, ItemFeatures> run1(String html) throws FileNotFoundException, IOException, GateException {
        Map<String, ItemFeatures> output = new HashMap<>();

        if (!isGateInitilised) {
            initialiseGate();
        }

        ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");
        ProcessingResource annieSentenceSpilitting = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
        ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
        ProcessingResource posTagger = (ProcessingResource) Factory.createResource("gate.creole.POSTagger");

        FeatureMap keywordExtractionFeatureMap = Factory.newFeatureMap();
        keywordExtractionFeatureMap.put("apiKey", "38abe3658e4b5af734d2c829670200138aef9265");
        ProcessingResource keywordExtraction = (ProcessingResource) Factory.createResource("gate.alchemyAPI.KeywordExtraction", keywordExtractionFeatureMap);

        //ProcessingResource keywordExtraction = (ProcessingResource) Factory.createResource("gate.alchemyAPI.KeywordExtraction");
        annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");
        annotationPipeline.add(documentResetPR);
        annotationPipeline.add(annieSentenceSpilitting);
        annotationPipeline.add(tokenizerPR);
        annotationPipeline.add(posTagger);
        annotationPipeline.add(keywordExtraction);

        URL url = new URL(html);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);
        System.out.println(body);

        //org.jsoup.nodes.Document doc1 = Jsoup.parse(html);
        //String content = doc1.body().text(); // "An example link"
        //System.out.println(content);
        Document document = Factory.newDocument(body);

        Corpus corpus = Factory.newCorpus("");
        corpus.add(document);
        annotationPipeline.setCorpus(corpus);
        annotationPipeline.execute();

        for (int i = 0; i < corpus.size(); i++) {
            Document doc = corpus.get(i);
            AnnotationSet as_default = doc.getAnnotations();
            FeatureMap futureMap = null;
            AnnotationSet annSetTokens = as_default.get("Keyword", futureMap);
            //System.out.println("Number of keywords: " + annSetTokens.size());
            ArrayList tokenAnnotations = new ArrayList(annSetTokens);

            for (int j = 0; j < tokenAnnotations.size(); ++j) {
                Annotation token = (Annotation) tokenAnnotations.get(j);
                Node isaStart = token.getStartNode();
                Node isaEnd = token.getEndNode();
                String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();

                if (output.containsKey(underlyingString)) {
                    output.get(underlyingString).increment();
                } else {
                    output.put(underlyingString, new ItemFeatures(Double.parseDouble((String) token.getFeatures().get("relevance")), 1));
                }

                //System.out.println("Keyword: " + underlyingString + " with relevance: " + token.getFeatures().get("relevance") );
                FeatureMap annFM = token.getFeatures();
            }
            System.out.println(output);
            //System.out.println("Number of Keyword annotations: " + annSetTokens.size());
        }

        return output;
    }

    private void initialiseGate() {

        try {
            // set GATE home folder
            // Eg. /Applications/GATE_Developer_7.0
            File gateHomeFile = new File("C:\\Program Files (x86)\\GATE_Developer_8.1");
            Gate.setGateHome(gateHomeFile);

            // set GATE plugins folder
            // Eg. /Applications/GATE_Developer_7.0/plugins            
            File pluginsHome = new File("C:\\Program Files (x86)\\GATE_Developer_8.1\\plugins");
            Gate.setPluginsHome(pluginsHome);

            // set user config file (optional)
            // Eg. /Applications/GATE_Developer_7.0/user.xml
            Gate.setUserConfigFile(new File("C:\\Program Files (x86)\\GATE_Developer_8.1", "user.xml"));

            // initialise the GATE library
            Gate.init();

            // load alchemy plugin
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURL();
            register.registerDirectories(annieHome);

            // load alchemy plugin
            CreoleRegister alchemy = Gate.getCreoleRegister();
            URL alchemyAPIHome = new File(pluginsHome, "alchemyAPI").toURL();
            register.registerDirectories(alchemyAPIHome);

            // flag that GATE was successfuly initialised
            isGateInitilised = true;

        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
