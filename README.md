# max-msp-bucket

A bucket for MaxMSP java externals

I envisage making smaller libraries for specific patches. Until that requirement exists, I'll keep everything in this library

## Setup

- Git clone this library
- New empty project in IntelliJ, import maven module
- Add max.jar to project classpath
  - */Library/Application Support/Cycling '74/java/lib/max.jar*
- Build classes
  - Can this be done to a jar with Maven, or will Max only work with a path to compile .class files?
- Update Max to be able to find compiled classes
  - edit max.java.config.txt **[Todo add full file path here]**
  - add line ... **[TODO add correct value here]**