#!/bin/bash
# Compilar y ejecutar sin Maven (requiere Java 17+ JDK)
set -e

SRC=src/main/java
OUT=target/classes
JAR=target/escaleras-serpientes.jar

echo "==> Creando directorios..."
mkdir -p $OUT

echo "==> Copiando recursos..."
cp src/main/resources/preguntas.txt $OUT/

echo "==> Compilando fuentes..."
find $SRC -name "*.java" > /tmp/sources.txt
javac -encoding UTF-8 -d $OUT @/tmp/sources.txt

echo "==> Empaquetando JAR..."
echo "Main-Class: com.juego.ui.MainApp" > /tmp/MANIFEST.MF
jar cfm $JAR /tmp/MANIFEST.MF -C $OUT .

echo "==> ¡Compilación exitosa!"
echo ""
echo "Ejecutar con: java -jar $JAR"
echo "O directamente: java -cp $OUT com.juego.ui.MainApp"
