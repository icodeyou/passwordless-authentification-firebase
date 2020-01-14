#!/bin/bash

PATH_UTIL=app/src/main/java/com/dupuis/webtoonfactory/util
PATH_REPO_MAIN=app/src/main/java/com/dupuis/webtoonfactory/data/repository
PATH_REPO_MOCK=app/src/mock/java/com/dupuis/webtoonfactory/data/repository
PATH_REPO_CONNECTED=app/src/connected/java/com/dupuis/webtoonfactory/data/repository

# Deplacement dans le repertoire du script
cd "$(dirname $0 )"
cd ../
echo $PWD
NUMBER_TOTAL_DOC=0
NUMBER_TOTAL_CLASS_AND_FUN=0

FILES=/Users/jack/Documents/projets/*.kt

analyzeFile() {
     echo File : $1
     NUMBER_DOC=$(grep -c "/\*\*" < $1)
     TODO=$(grep -c "\* TODO" < $1)
     NUMBER_DOC=$(($NUMBER_DOC-$TODO))
     NUMBER_FUN=$(grep -c "fun " < $1)
     NUMBER_CLASS=$(grep -c "class " < $1)

    NUMBER_CLASS_AND_FUN=$(($NUMBER_FUN+$NUMBER_CLASS))

    if [ $NUMBER_CLASS_AND_FUN != 0 ]; then
        echo Functions and classes : $NUMBER_CLASS_AND_FUN
    else
        echo "No function, no class. Expecting 1 documentation"
        NUMBER_CLASS_AND_FUN=1
    fi
    echo Documentation : $NUMBER_DOC

    #Calculate coverage 
    echo Coverage : $(( 100 * $NUMBER_DOC/NUMBER_CLASS_AND_FUN ))"%"

    #Increment total numbers
    NUMBER_TOTAL_DOC=$(($NUMBER_TOTAL_DOC+$NUMBER_DOC))
    NUMBER_TOTAL_CLASS_AND_FUN=$(($NUMBER_TOTAL_CLASS_AND_FUN+$NUMBER_CLASS_AND_FUN))
    echo ""
}

filesInPackages=()
while IFS=  read -r -d $'\0'; do
    filesInPackages+=("$REPLY")
done < <(find $PATH_UTIL $PATH_REPO_MAIN $PATH_REPO_MOCK $PATH_REPO_CONNECTED \( -name "*.kt" -or -name "*.java" \) -print0)

viewmodelFiles=()
while IFS=  read -r -d $'\0'; do
    viewmodelFiles+=("$REPLY")
done < <(find app/src \( -iname "*ViewModel.kt" -or -iname "*ViewModel.java" \) -print0)

export -f analyzeFile

for i in "${filesInPackages[@]}"
do
    analyzeFile $i
done

for i in "${viewmodelFiles[@]}"
do
    analyzeFile $i
done

# Directly execute function analyzeFile after find
#find app/src -type f \( -iname "*ViewModel.kt" -or -iname "*ViewModel.java" \) -exec bash -c 'analyzeFile "$0"' $f {} \;

echo ""
echo ----- TOTAL COVERAGE -----
echo Functions and classes : $NUMBER_TOTAL_CLASS_AND_FUN
echo Documentation : $NUMBER_TOTAL_DOC

if [ $NUMBER_TOTAL_CLASS_AND_FUN != 0 ]; then
    echo $(( 100 * $NUMBER_TOTAL_DOC/NUMBER_TOTAL_CLASS_AND_FUN ))"%"
else
    echo "There wasn't any function or any class to document."
fi