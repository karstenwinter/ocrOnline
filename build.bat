set mvn=C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\plugins\maven\lib\maven3

echo change maven if necessary

"%JAVA_HOME%/bin/java" "-Dmaven.home=%mvn%" -classpath "%mvn%\boot\plexus-classworlds-2.5.2.jar" -Dmaven.multiModuleProjectDirectory=. "-Dclassworlds.conf=%mvn%\bin\m2.conf" org.codehaus.classworlds.Launcher -Didea.version=2018.3.4 -e clean compile assembly:single
