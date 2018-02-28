ruby split_document.rb
PWD=$(pwd)
cran_docs="$PWD/target/classes/cran/parsed/docs"
cran_query="$PWD/target/classes/cran/parsed/query"
index_path="$PWD/target/classes/cran/index"
java -jar "$PWD/target/picasso-1.0-SNAPSHOT.jar" -index $index_path -docs $cran_docs -query $cran_query
answer="$PWD/QRelsCorrectedforTRECeval"
results="$PWD/target/classes/cran/test_results"
cd "trec_eval"
./trec_eval -l 3 -m all_trec $answer $results
rm $results
