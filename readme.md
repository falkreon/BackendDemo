# BackendDemo

## About
This is a backend development exercise, specifically [this one](https://github.com/QuickBase/interview-demos/tree/master/backend).
It's intended to showcase the merging of data from different sources, and output a well-specified list.


## Project Structure and Building
There are two gradle projects: "`backend`", which is the original exercise code; and "`demo`" (may show up as "`dev-interview-materials`"),
which is the main project and the bulk of the exercise. Where gradle commands are listed here, they should be run inside "`demo`".


The build.gradle is set up for "`gradle -q`" to Just Work, but if it doesn't, "`gradle clean shadowJar`" should do the trick.


## Running
UnitTests should probably be run with "`gradle test`". I did most testing using the JUnit Runner in Eclipse, but verified that
gradle can run the tests successfully as well.

The artifact to be run is "`BackendDemo-1.0-all.jar`", which has backend and all other dependencies shaded. It will still
expect the resources folder to be available in the directory where it's run from.
 
 
## Notes
The original exercise seemed intended to be completed inside one project, but several pieces of the original project
wind up being proprietary and unchangeable in practice, so I decided to split the project and treat "`backend`" as unchangeable
except where changes were explicitly required by the exercise.


The `com.quickbase.Main` class in backend is ommitted by shadow because one is supplied by the primary `demo` project.
