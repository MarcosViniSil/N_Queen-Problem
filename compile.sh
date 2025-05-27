NAME_MAIN_FILE=$1
cd src/
javac $NAME_MAIN_FILE.java
java $NAME_MAIN_FILE
find . -name "*.class" -delete


