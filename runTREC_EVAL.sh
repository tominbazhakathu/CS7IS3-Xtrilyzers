#for f in 0 1 2
for f in 2
do
  #for a in 0 1 2 3 4
  for a in 3
  do
    #for s in 0 1 2
    for s in 0
    do
      echo "Looping ... number f $f a $a s $s"
      #echo java -cp $CLASSPATH:target/classes ie.tcd.scss.cs7is3.xtrilyzers.App -f "$f" -a "$a" -s "$s"
      #java -cp $CLASSPATH:target/classes ie.tcd.scss.cs7is3.xtrilyzers.App -f "$f" -a "$a" -s "$s"

      echo mvn exec:java -Dexec.mainClass="ie.tcd.scss.cs7is3.xtrilyzers.App" -Dexec.args="-f $f -a $a -s $s"
      mvn exec:java -Dexec.mainClass="ie.tcd.scss.cs7is3.xtrilyzers.App" -Dexec.args="-f $f -a $a -s $s"
      
      #flag -l3 seems to no longer be supported because of it is a boolean comparison
      #trec_eval.9.0/trec_eval -l3 qrels/qrel_part1.txt results/results.txt > "evaluation/trec_eval_f"$f"a"$a"s"$s""
      trec_eval.9.0/trec_eval qrels/qrel_part1.txt results/results.txt > "evaluation/trec_eval_f"$f"a"$a"s"$s""
    done
  done
done
