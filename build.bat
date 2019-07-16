set mvn=C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\plugins\maven\lib\maven3
echo change maven if necessary
java "-Dmaven.home=%mvn%" assembly:single
