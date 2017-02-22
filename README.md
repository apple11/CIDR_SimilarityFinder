# CIDR_SimilarityFinder
To find relation between subnet blocks with CIDR format
##Outline
####1. Install
####2. Function

##Install
You need to install Java 8 first, since the project is based on Java 8 version. <br><br>
You can download Java 8 via `http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html` and install it with 
its instuctions. <br>

Then You can execute the project with the following guides. The Project include two version of executed files.

###JavaScript Version
Included in JavaScript Folder. Only one html file: `Similarity Exercise_ Ping Lin.html`, written in JavaScript, HTML/CSS. <br>
You can execute it in web browser (just double click and executed in web browser, e.g. Chrome).

###Java Version
Included in Java Folder, including src Folder, lib Folder and a runnable jar File `SimilarityFinder.jar`.<br>
You can executed in command line as:`java -jar SimailarityFinder.jar`. You can see the source code in src folder and this project include 
two external jar file listed in lib folder

##Function
####1. Generate sets of CIDR blocks with two parameters:<br>
1) number of blocks you want to generate.<br>
2) subnet number limit of each blocks. For example, if you input 4, then subnet number limit of each blocks is from 0 to 4 that randomly 
generated.<br>
####2 Calculate similarity of interaction between the blocks, i.e. intersect, contain, adjacent, none.<br>
####3 Data representation <br>
Pie Chart indicating the proportion of relataion between blocks.<br>
List Pairs of blocks in each relation categories.<br>
Also, you can choose any two blocks to see their relations. <br>




