@echo off
echo Compiling Java files...
javac *.java

echo Creating JAR file...
jar cvfm SpiderWeb.jar manifest.txt *.class

echo Creating installer with IExpress...
iexpress /N setup.sed

echo Done!
pause 