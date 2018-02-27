PWD=$(pwd)
parseDocument="ruby $PWD/target/classes/cran/split_document.rb"
eval $parseDocument
answer="$PWD/target/classes/cran/parsed_answer"
searchDocument="java -jar picasso-1.0-SNAPSHOT.jar"
eval $searchDocument
results="$PWD/target/classes/cran/test_results"
cd "trec_eval"
evaluate="./trec_eval $answer $results"
eval $evaluate
deleteAnswer="rm $answer && rm $results"
eval $deleteAnswer
