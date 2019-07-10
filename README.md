# MapReduce Framework
Cameron Morales

cam.morales93@gmail.com

## Discussion
This project contains a simple implementation of the MapReduce framework. In
this case, generality is expressed with the use of consistent Map and Reduce
actors that require plugins to achieve the desired function. These plugins must
implement a PluginMap and PluginReduce interface to be successfully swapped
into the application. The MasterActor communicates to the map and reduce actors
which plugin type they should utilize during initialization. Each actor accepts
input of the form (Key : Any, Value :  Any), however it is the responsibility
of the plugins to interpret the actual expected type.


## Instructions to run
1. Open three terminal windows
2. In each window, cd into the root directory:
```
> cd .../MapReduce
```
3. Start the scala built tool:
```
> sbt
```
4. Start each server (in separate windows):
```
> run
> 2
```
```
> run
> 3
```
5. Start the client:
```
> run
> 1
```
5. On the client, enter the number to select a demo:
* 1 - Word Count
* 2 - Plural Names
* 3 - Hyperlinks
