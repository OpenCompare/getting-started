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

### Using a visitor

## Export
