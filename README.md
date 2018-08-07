# max-msp-bucket

A bucket for MaxMSP java externals

I envisage making smaller libraries for specific patches. Until that requirement exists, I'll keep everything in this library

## Setup

- Git clone this library
- New empty project in IntelliJ, import maven module!
- Add max.jar to project classpath
  - */Applications/Max5/Cycling '74/java/lib/max.jar*
- Build classes/jar
  - Classes can be built directly by compiling in IntelliJ
  - Jar can be built with maven (requires specifying max.jar with a <scope>system</scope>)
- Update Max to be able to find compiled classes
  - edit */Applications/Max5/Cycling '74/java/max.java.config.txt*
  - add *max.dynamic.class.dir /Users/WillyC/Documents/max-msp-bucket/target/* for classes
  - add *max.dynamic.jar.dir /Users/WillyC/Documents/max-msp-bucket/target/* for jars
