# Getting started with OpenCompare

Examples for using OpenCompare API and services

## Import a PCM
1- Define the file that you want to load.

2- Create a loader that can handle the file format. 
In our example, we use the KMFJSONLoader which supports the internal representation of OpenCompare.

3- Load the file. 
A loader returns a list of PCM containers as a file may contain multiple PCMs.
A PCM container encapsulates a PCM and its associated metadata (e.g. position of the products and features, source of the information, author of the PCM).

4- Iterate over the PCM containers to get the loaded PCMs

```java
File pcmFile = new File("pcms/example.pcm");
PCMLoader loader = new KMFJSONLoader();
List<PCMContainer> pcmContainers = loader.load(pcmFile);
for (PCMContainer pcmContainer : pcmContainers) {
  PCM pcm = pcmContainer.getPcm();
}
```

## Browse a PCM

### Using the API
```java
// Browse the cells of the PCM
for (Product product : pcm.getProducts()) {
  for (Feature feature : pcm.getConcreteFeatures()) {
    // Find the cell corresponding to the current feature and product
    Cell cell = product.findCell(feature);

    // Get information contained in the cell
    String content = cell.getContent();
    String rawContent = cell.getRawContent();
    Value interpretation = cell.getInterpretation();

    // Print the content of the cell
    System.out.println("(" + product.getName() + ", " + feature.getName() + ") = " + content);
  }
}
```
### Using a visitor
See src/test/java/org.opencompare/VisitorTest.java

## Export
If we want to export or serialize the PCM to a specific format, we can use a PCMExporter.
In this example, we export our PCM to a CSV file.

```java
CSVExporter csvExporter = new CSVExporter();
String csv = csvExporter.export(pcmContainer);
```

## Import 

If we want to import a PCM from a CSV file, we can use a CSVLoader.

```java
 CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()));
List<PCMContainer> pcms = csvL.load(new File("pcms/pokemon.csv"));
...
```

