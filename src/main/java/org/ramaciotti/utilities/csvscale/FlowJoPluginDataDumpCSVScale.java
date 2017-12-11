package org.ramaciotti.utilities.csvscale;

import com.treestar.lib.core.ExportFileTypes;
import com.treestar.lib.core.ExternalAlgorithmResults;
import com.treestar.lib.core.PopulationPluginInterface;
import com.treestar.lib.fjml.FJML;
import com.treestar.lib.xml.SElement;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class will dump the 3 arguments that flowjo will use to call externalAlgorithmResults() to disk.
 * To be used with flowjo-plugin-testbed for testing flowjo plugins without flowjo itself.
 */
public class FlowJoPluginDataDumpCSVScale implements PopulationPluginInterface {

    private List<String> fParameters = new ArrayList<String>(); // the list of $PnN parameter names to be used
    private SElement fOptions; // an XML element that can hold any additional options used by the algorithm
    /*
     * Function returns name of plugin that will be output to the cell inside the workspace
     *
     * (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#getName()
     */
    @Override
    public String getName() {
        return ("FlowJo Plugin Data Dump - CSV_SCALE (Raw Values)");
    }
    /*
     * Return an XML element that fully describes the algorithm object and can be used to
     * reconstitute the state of the object.
     *
     * (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#getElement()
     */
    @Override
    public SElement getElement() {
        SElement result = new SElement(getClass().getSimpleName());
        if (fOptions != null)
            result.addContent(new SElement(fOptions)); // create a copy of the XML element
        // construct an XML element for each parameter name
        for (String pName : fParameters)
        {
            SElement pElem = new SElement(FJML.Parameter);
            pElem.setString(FJML.name, pName);
            result.addContent(pElem);
        }
        return result;
    }

    /*
     * Use the input XML element to set the state of the algorithm object
     *
     *  could be null
     *  clear the parameter list and re-create from the XML element
     * (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#setElement(com.treestar.lib.xml.SElement)
     */
    @Override
    public void setElement(SElement elem) {
        fOptions = elem.getChild("Option");
        fParameters.clear();
        for (SElement child : elem.getChildren(FJML.Parameter))
            fParameters.add(child.getString(FJML.name));
    }
    /* (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#getParameters()
     */
    @Override
    public List<String> getParameters() {
        return new ArrayList<String>();
    }
    /* (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#getIcon()
     */
    @Override
    public Icon getIcon() {
        return null;
    }

    /*
     * This method uses class ClusterPrompter to prompt the user for a list of parameters and number of clusters.
     *
     * (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#promptForOptions(com.treestar.lib.xml.SElement, java.util.List)
     */
    @Override
    public boolean promptForOptions(SElement fcmlQueryElement, List<String> parameterNames)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#invokeAlgorithm(com.treestar.lib.xml.SElement, java.io.File, java.io.File)
     */
    @Override
    public ExternalAlgorithmResults invokeAlgorithm(SElement fcmlElem, File sampleFile, File outputFolder) {
        ExternalAlgorithmResults result = new ExternalAlgorithmResults();
        // Write the FCML (flow cytometry markup language) Element to disk
        Path file = Paths.get(outputFolder + "/fcmlQueryElement.xml");
        List<String> lines = Arrays.asList(fcmlElem.toString().split("\n"));
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JDialog window = new JDialog(new JFrame(), "FlowJo Plugin Data Dump - CSV_SCALE (Raw Values)", true);
        window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JTextPane description = new JTextPane();
        description.setContentType("text/html"); // let the text pane know this is what you want
        description.setText(
                "<html>" +
                    "Import flowjo-plugin-testbed.jar and call the following on your plugin: <br />" +
                    "invokeAlgorithm(FlowJoPluginTestbed.getFcmlFromFile(\"" + outputFolder + "/fcmlQueryElement.xml\"), FlowJoPluginTestbed.createFileObject(\"" + sampleFile.toPath() + "\"), FlowJoPluginTestbed.createFileObject(\"" + outputFolder.toPath() + "\"));" +
                "</html>"
        ); // showing off
        description.setEditable(false); // as before
        description.setBackground(null); // this is the same as a JLabel
        description.setBorder(null); // remove the border
        description.setBounds(5, 10, 1024, 768);

        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(0, 0, 1024, 768);
        contentPanel.setLayout(null);
        contentPanel.add(description);
        window.add(contentPanel);
        window.setLayout(null);
        window.setSize(1024, 768);
        window.setVisible(true);
        return result;
    }

    /*
     * This method shows how to return different kinds of values using the ExternalAlgorithmResults object.
     *
     * This method allows the algorithm to specify if the input file should be a CSV file (CSV_SCALE OR CSV_CHANNEL), or an FCS file based on algorithm's requirements.
     * @return ExportFileTypes One of three values (ExportFileTypes.FCS, ExportFileTypes.CSV_SCALE, ExportFileTypes.CSV_CHANNEL) will be returned according to algorithm's requirement.
     *
     *  (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#useExportFileType()
     */
    @Override
    public ExportFileTypes useExportFileType() {
        return ExportFileTypes.CSV_SCALE;
    }

    /* (non-Javadoc)
     * @see com.treestar.lib.core.ExternalPopulationAlgorithmInterface#getVersion()
     */
    @Override
    public String getVersion() {   	 return "1.0";    }

}