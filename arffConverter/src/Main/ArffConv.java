package Main;

import ConexaoBD.BDHandler;
import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.XRFFSaver;
import weka.core.xml.XMLInstances;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Main.Constant.*;
import static Main.Utils.*;
import static Model.DBTable.initTable;
import static Model.SelectionMethod.NO_DATA;

public class ArffConv extends Application {

    private static DBTable.Dataset dataset;
    private static SelectionMethod selectionMethod;
    protected static boolean removeStopWords;
    private static int limitQuerySelect = 0;

    private static ResultSet BDRs = null;

    private static Map<String, Attribute> attributeMap;
    private static List<Comment> commentList;
    private static List<String> singleOccurrence;

    private static DBTable mainTable;
    private static Dictionary dictionary;

    private static Controller controller;

    public static void setController(Controller controller) {
        ArffConv.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle("ArffConverter");
        primaryStage.setScene(new Scene(root, 600, 675));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void finishConvert() {
        System.exit(0);
    }

    public static void validate() {
        if (dataset != null && dataset != DBTable.Dataset.NO_DATA
                && selectionMethod != null && selectionMethod != NO_DATA) {
            startConversion();
        }
    }

    public static void startConversion() {
        System.out.println("Starting conversion!");
        controller.setTextArea("Starting conversion!");

        Connection BD = BDHandler.getMySQLConnection();
        mainTable = initTable(dataset);

        initVariables();

        try {
            Statement BDStmt = BD.createStatement();

            BDRs = BDStmt.executeQuery(mainTable.getDictionaryTableQuery());
            setDictionaryList();

            BDRs = BDStmt.executeQuery(mainTable.getQuery(limitQuerySelect));
            arffFile = getNewFileWriter(mainTable.getArffFile());

            if(BDRs != null){
                BDRs.beforeFirst();

                System.out.println("Building lists...");
                controller.setTextArea("Building lists...");

                setCommentAttributeList();

                setAttributeVariables();

                System.out.println("Writing header...");
                controller.setTextArea("Writing header...");

                writeHeader();

                System.out.println("End of header. Writing data...");
                controller.setTextArea("End of header. Writing data...");

                writeData();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            System.out.println("Finish!");
            controller.setTextArea("Finish!");

            closeFileWriter(arffFile);
            convertXrff();
        }
    }

    private static void convertXrff() {
        try {
            DataSource source = new DataSource(mainTable.getArffFile());
            Instances data = source.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }

            double factor = 0.1 / ((double) data.numAttributes() - 1);
            for (int i = 0; i < data.numAttributes() - 1; i++) {
                data.attribute(i).setWeight(factor);
            }

            data.attribute(data.numAttributes() - 1).setWeight(0.9);

            xrffFile = getNewFileWriter(mainTable.getArffFile().replace("arff", "xrff"));

            XRFFSaver saver = new XRFFSaver();
            saver.setInstances(data);
            XMLInstances m_XMLInstances = new XMLInstances();
            m_XMLInstances.setInstances(saver.getInstances());

            String header;

            header = PI + "\n\n";
            if (m_XMLInstances.getDocType() != null)
                header += m_XMLInstances.getDocType() + "\n\n";

            writeXrff(header);

            toString(m_XMLInstances.getDocument().getDocumentElement(), 0);

            closeFileWriter(xrffFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void toString(Node parent, int depth) throws IOException {
        NodeList list;
        Node node;
        int i;
        int n;
        String indent;
        NamedNodeMap atts;

        // build indent
        indent = "";
        for (i = 0; i < depth; i++)
            indent += "   ";

        if (parent.getNodeType() == Node.TEXT_NODE) {
            if (!parent.getNodeValue().trim().equals(""))
                writeXrff(indent + parent.getNodeValue().trim() + "\n");
        } else if (parent.getNodeType() == Node.COMMENT_NODE) {
            writeXrff(indent + "<!--" + parent.getNodeValue() + "-->\n");
        } else {
            writeXrff(indent + "<" + parent.getNodeName());
            // attributes?
            if (parent.hasAttributes()) {
                atts = parent.getAttributes();
                for (n = 0; n < atts.getLength(); n++) {
                    node = atts.item(n);
                    writeXrff(" " + node.getNodeName() + "=\"" + node.getNodeValue() + "\"");
                }
            }
            // children?
            if (parent.hasChildNodes()) {
                list = parent.getChildNodes();
                // just a text node?
                if ( (list.getLength() == 1) && (list.item(0).getNodeType() == Node.TEXT_NODE) ) {
                    writeXrff(">");
                    writeXrff(list.item(0).getNodeValue().trim());
                    writeXrff("</" + parent.getNodeName() + ">\n");
                } else {
                    writeXrff(">\n");
                    for (n = 0; n < list.getLength(); n++) {
                        node = list.item(n);
                        toString(node, depth + 1);
                    }
                    writeXrff(indent + "</" + parent.getNodeName() + ">\n");
                }
            } else {
                writeXrff("/>\n");
            }
        }
    }

    private static void setAttributeVariables() {
        switch (selectionMethod) {
            case SINGLE_OCCURRENCE:
                setAttributesOccurrences();
                break;

            case DICTIONARY_VALUE:
                setAttributesPolarity();
                break;
        }
    }

    private static void initVariables() {
        dictionary = new Dictionary();
        attributeMap = new HashMap<>();
        commentList = new ArrayList<>();
        singleOccurrence = new ArrayList<>();
    }

    private static void setDictionaryList() throws SQLException {
        if (BDRs != null) {
            BDRs.beforeFirst();

            while(BDRs.next()){
                dictionary.insertDictionaryWord(
                        new DictionaryWord(BDRs.getString(mainTable.getDictionaryTable().getColumnText()),
                                BDRs.getInt(mainTable.getDictionaryTable().getColumnValue())));
            }
        }
    }

    private static void setCommentAttributeList() throws SQLException {
        while(BDRs.next()){
            Comment comment = new Comment(BDRs.getString(mainTable.getColumnText()), BDRs.getInt(mainTable.getColumnValue()));

            commentList.add(comment);

            for (String word : comment.getWords()) {
                if (word.trim().length() > 1 && !attributeMap.containsKey(word.trim())) {
                    attributeMap.put(word.trim(), new Attribute(word));
                }
            }
        }
    }

    private static void setAttributesOccurrences() {
        for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
            Attribute attribute = entry.getValue();
            for (Comment comment : commentList) {
                if (comment.getWords().contains(attribute.getWord())) {
                    attribute.increaseRatingOccurrence(comment.getValue());
                }
            }
        }
    }

    private static void setAttributesPolarity() {
        for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
            Attribute attribute = entry.getValue();
            DictionaryWord dictionaryWord = dictionary.getDictionaryWord(attribute.getWord());

            if (dictionaryWord != null) {
                attribute.setPolarity(dictionaryWord.getPolarity());
            } else {
                attribute.setPolarity(null);
            }
        }
    }


    private static void writeHeader() throws IOException, SQLException {
        writeArff("@relation 'palavras x value'\n");

        switch (selectionMethod) {
            case SINGLE_OCCURRENCE:
                writeHeaderSingleOccurrence();
                break;
            case DICTIONARY_VALUE:
                writeHeaderDictionaryValue();
                break;
        }

        writeArff("@attribute class {" + getValuesRatio(mainTable) + "}\n");
    }

    private static void writeHeaderSingleOccurrence() throws IOException {
        for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
            Attribute attribute = entry.getValue();

            if (attribute.getTotalOccurrence() > MINIMUM_OCCURRENCE){
                writeArff(headerAttribute(attribute.getWord()));
            } else if (attribute.getTotalOccurrence() == MINIMUM_OCCURRENCE) {
                singleOccurrence.add(attribute.getWord());
            }
        }

        writeArff(headerAttribute(SINGLE_OCCURRENCE_ATTRIBUTE));
    }

    private static void writeHeaderDictionaryValue() throws IOException {
        for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
            Attribute attribute = entry.getValue();
            writeArff(headerAttribute(attribute.getWord()));
        }

        writeArff(headerAttribute(DICTIONARY_VALUE_ATTRIBUTE));
    }

    private static void writeData() throws IOException {
        writeArff("@data\n");

        switch (selectionMethod) {
            case SINGLE_OCCURRENCE:
                writeDataSingleOccurrence();
                break;
            case DICTIONARY_VALUE:
                writeDataDictionaryValue();
                break;
        }
    }

    private static void writeDataSingleOccurrence() throws IOException {
        String singleOccurrenceValue = getSingleOccurrenceValue(singleOccurrence, dictionary);

        for (Comment comment : commentList) {
            boolean hasSingleOccurrence = false;
            String commentBinaryValues = "";

            arffFile.flush();

            for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
                Attribute attribute = entry.getValue();
                if (attribute.getTotalOccurrence() > MINIMUM_OCCURRENCE) {
                    commentBinaryValues += getBinaryValue(comment, attribute) + ",";
                } else if (!hasSingleOccurrence && Utils.converterArray(comment.getText()).contains(attribute.getWord()) && attribute.getTotalOccurrence() == MINIMUM_OCCURRENCE) {
                    hasSingleOccurrence = true;
                }
            }

            if (hasSingleOccurrence) {
                commentBinaryValues += getBinarySingleOccurrenceValue(hasSingleOccurrence, singleOccurrenceValue) + ",";
            } else {
                commentBinaryValues += "?,";
            }

            commentBinaryValues += comment.getValue() + "\n";
            writeArff(commentBinaryValues);
        }
    }

    private static void writeDataDictionaryValue() throws IOException {
        for (Comment comment : commentList) {
            int dictionaryValue = 0;
            String commentBinaryValues = "";

            arffFile.flush();

            for (Map.Entry<String, Attribute> entry : attributeMap.entrySet()) {
                Attribute attribute = entry.getValue();

                commentBinaryValues += getBinaryValue(comment, attribute) + ",";

                if (comment.getWords().contains(attribute.getWord())) {
                    dictionaryValue += attribute.getPolarityValue();
                }
            }

            if (dictionaryValue == 0) {
                commentBinaryValues += "?,";
            } else if (dictionaryValue > 0) {
                commentBinaryValues += "1,";
            } else {
                commentBinaryValues += "0,";
            }

            commentBinaryValues += comment.getValue() + "\n";
            writeArff(commentBinaryValues);
        }
    }

    public static String getStopWordsPath() {
        return mainTable.getStopWords();
    }

    public static DBTable.Dataset getDataset() {
        return dataset;
    }

    public static void setDataset(DBTable.Dataset dataset) {
        ArffConv.dataset = dataset;
    }

    public static SelectionMethod getSelectionMethod() {
        return selectionMethod;
    }

    public static void setSelectionMethod(SelectionMethod selectionMethod) {
        ArffConv.selectionMethod = selectionMethod;
    }

    public static boolean isRemoveStopWords() {
        return removeStopWords;
    }

    public static void setRemoveStopWords(boolean removeStopWords) {
        ArffConv.removeStopWords = removeStopWords;
    }

    public static void setLimitQuerySelect(int limitQuerySelect) {
        ArffConv.limitQuerySelect = limitQuerySelect;
    }

}
