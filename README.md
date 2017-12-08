## FlowJo Data Dump Plugin

Note: This plugin is to be used in conjuction with [flowjo-plugin-testbed](https://github.com/nicbarker/flowjo-plugin-testbed)

### Usage
- Download the plugin .jar from the releases page
- Place it in your `/Applications/plugins` directory or windows equivalent
- Start FlowJo
- Select a sample or node then click Workspace -> Plugins -> FlowJoPluginDataDump
- It will save files to the output folder and give you a popup window that says something like this:
- Import [flowjo-plugin-testbed.jar](https://github.com/nicbarker/flowjo-plugin-testbed) and call the following on your plugin:
```Java
invokeAlgorithm(FlowJoPluginTestbed.getFcmlFromFile("/some/directory/FlowJo Plugin Data Dump/fcmlQueryElement.xml"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump/st_HM-1_CHECK192_001..ExtNode.csv"), FlowJoPluginTestbed.createFileObject("/some/directory/FlowJo Plugin Data Dump"));
```
In your plugin, create a main function and call the `invokeAlgorithm()` from the previous step
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

- You'll now be able to use standard Java development and debugging practise to test if your plugin works.

### Rationale
The standard cycle for developing and debugging [FlowJo](https://www.flowjo.com/) plugins can be quite painful, and usually involves some variation of the following:

- Build a fat .jar file including dependencies for your plugin
- Copy it into your `/Applications/plugins` directory or windows equivalent
- Start FlowJo from the command line
- Open your workspace
- Wait for FlowJo to finish scanning for .jar files
- Run the plugin and check the console for errors

Sometimes this process can take upwards of 5 minutes for a single round trip, which is not feasible if you're developing a plugin or have a programming style that requires a lot of back and forth debugging. By using this in combination with [flowjo-plugin-testbed](https://github.com/nicbarker/flowjo-plugin-testbed), you'll be able to get quick debugging 
