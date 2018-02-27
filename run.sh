ruby split_document.rb
PWD=$(pwd)
java -jar "$PWD/target/picasso-1.0-SNAPSHOT.jar"
answer="$PWD/QRelsCorrectedforTRECeval"
results="$PWD/target/classes/cran/test_results"
cd "trec_eval"
./trec_eval -l 3 -m all_trec $answer $results
rm $results
