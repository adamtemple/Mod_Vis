Read Me for Exam code.

The code is included in a package called Exam. I have included all other packages as well just in case but Exam is the examination code.

This is a modified version of the ising package. The main methods are held within the program package.

I have found that to be able to run the programs it is best to load in eclipse. Otherwise the .class files are found in the bin folder. They can be run from the root directory (one above bin, where the bin, src and Exam_Plots folders are) with java/bin/PACKAGE/FILENAME. No runtime parameters should be needed.


For the interactive part I have a Model-Controller-Viewer set-up.

in the model package is all the modified code for the model of the system, and is used in the main methods to create the subsequent graphs.

The Controller is a class which communications between the viewer class (interactiveViewer for the interactive version and just a normal simple viewer) and the options and controls classes also found. This made it easier to make the interactive code.

The bottom panel with the stop and start controls is the controlPanel and the left hand side options is the optionsPanel.

I have each the system as a 2-d array of 'spins' each with different states as needed.
Some code may be left over from the ising model but hopefully I found and get rid of most of the necesscary code!

The packages are easier to see if it is loaded into eclipse. 

Programs are the main methods which should be labeled approrpirately.

The plots are also included in the plots folder. I used xmgrace to view these. The main corresponding to each plot should be fairly straight forward to identify.

I removed a large part of the code for doing file output and averages from the Potts class and just put these into the main methods for clarity. 

The source files can be found in src/Exam folder.
