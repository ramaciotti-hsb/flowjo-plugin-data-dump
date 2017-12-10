## FlowJo Data Dump Plugin

Note: This plugin is to be used in conjuction with [flowjo-plugin-testbed](https://github.com/nicbarker/flowjo-plugin-testbed)

## Usage
- Download the data-dump plugin .jar from the [releases page](https://github.com/nicbarker/flowjo-plugin-data-dump/releases)
- Place it in your `/Applications/plugins` directory or windows equivalent
- Start FlowJo
- Select a sample or node then click Workspace -> Plugins -> FlowJoPluginDataDump
- It will save files to the output folder and give you a command to copy paste that looks like this:
```Java
invokeAlgorithm(FlowJoPluginTestbed.getFcmlFromFile("/some/directory/FlowJo Plugin Data Dump/fcmlQueryElement.xml"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump/st_HM-1_CHECK192_001..ExtNode.csv"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump"));
```
- Download the flowjo-plugin-testbed `.jar` from the [releases page](https://github.com/nicbarker/flowjo-plugin-testbed/releases)
- Add [flowjo-plugin-testbed.jar](https://github.com/nicbarker/flowjo-plugin-testbed) to your classpath
- In your plugin, create a main function and call the `invokeAlgorithm()` template from the previous step
```Java
    public static void main(String[] args) {
        YourPlugin plugin = new YourPlugin();
        try {
            plugin.invokeAlgorithm(FlowJoPluginTestbed.getFcmlFromFile("/some/directory/FlowJo Plugin Data Dump/fcmlQueryElement.xml"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump/st_HM-1_CHECK192_001..ExtNode.csv"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

## Rationale
The standard cycle for developing and debugging [FlowJo](https://www.flowjo.com/) plugins can be quite painful, and usually involves some variation of the following:

- Build a fat .jar file including dependencies for your plugin
- Copy it into your `/Applications/plugins` directory or windows equivalent
- Start FlowJo from the command line
- Open your workspace
- Wait for FlowJo to finish scanning for .jar files
- Run the plugin and check the console for errors

Sometimes this process can take upwards of 5 minutes for a single round trip, which is not feasible if you're developing a plugin or have a programming style that requires a lot of back and forth debugging. By using this in combination with [flowjo-plugin-testbed](https://github.com/nicbarker/flowjo-plugin-testbed), you'll be able to get quick debugging.

## Details
If you're interested in creating plugins for Flowjo, chances are you've read the [FlowJo Plugin Development Guide](https://flowjollc.gitbooks.io/flowjo-plugin-developers-guide/content/).
If you're writing a population plugin, you'll know that the bulk of the work is done by the `invokeAlgorithm()` method (documented [here](https://flowjollc.gitbooks.io/flowjo-plugin-developers-guide/content/calculation_methods.html)).

The `invokeAlgorithm(SElement fcmlElem, File sampleFile, File outputFolder)` function takes 3 arguments:

#### SElement fcmlElem
This is basically an XML document in "FCML" (flow cytometry markup language) format. It contains metadata describing the __current node on which the plugin is being run__. Note: this means that you need to dump the plugin data specifically for the sample or node you're intending to test with.

It looks something like this:
```xml
<FCML version="3" >
  <FcmlQuery queryId="FCML" >
    <DataSet uri="/Users/nicbarker/Documents/test gating/st_HM-1_CHECK192_001.fcs" >
      <transforms:spilloverMatrix prefix="Comp-"  name="Compensation-copy"  editable="1"  color="#00ccff"  version="FlowJo-10.4"  status="FINALIZED"  transforms:id="ce3c3c11-eef9-4505-b2c6-54495cce1ac4"  suffix="" >
        <data-type:parameters>
          <data-type:parameter data-type:name="B 530_B-A" />
          <data-type:parameter data-type:name="B 695_A-A" />
          <data-type:parameter data-type:name="R 780_A-A" />
          <data-type:parameter data-type:name="UV 379_C-A" />
          <data-type:parameter data-type:name="V 450_F-A" />
          <data-type:parameter data-type:name="V 525_E-A" />
          <data-type:parameter data-type:name="YG 582_D-A" />
          <data-type:parameter data-type:name="YG 780_A-A" />
        </data-type:parameters>
```


#### File sampleFile
When you call your population plugin, most of the time FlowJo will actually write out a new file to the disk containing the subpopulation at that node so you can work on it without damaging the original file. The structure and data in this file depends on which version of the plugin you use (FCS_FILE, TRANSFORMED_VALUES_CSV or RAW_VALUES_CSV) You then do your processing on that file and return your results. This is a basic java file object and you can read it from the disk as normal.

#### File outputFolder
This is just a File object that points to the directory where the previous two files will be stored.


### How it works
All that `flowjo-plugin-data-dump` does is write the files supplied to it by FlowJo to the disk, then generates code that you can use with `flowjo-plugin-testbed` to run your plugin as if it was being called by FlowJo itself. This means that you can use your standard debugging workflow without having to compile a `.jar` file and restart FlowJo. In fact, you can even build and test plugins without FlowJo installed on your machine.
