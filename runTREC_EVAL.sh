for f in 0 1 2
do
  for a in 0 1 2 3 4
  do
    for s in 0 1 2
    do
      echo "Looping ... number f $f a $a s $s"
      #echo java -cp $CLASSPATH:target/classes ie.tcd.scss.cs7is3.xtrilyzers.App -f "$f" -a "$a" -s "$s"
      #java -cp $CLASSPATH:target/classes ie.tcd.scss.cs7is3.xtrilyzers.App -f "$f" -a "$a" -s "$s"

      echo mvn exec:java -Dexec.mainClass="ie.tcd.scss.cs7is3.xtrilyzers.App" -Dexec.args="-f $f -a $a -s $s"
      mvn exec:java -Dexec.mainClass="ie.tcd.scss.cs7is3.xtrilyzers.App" -Dexec.args="-f $f -a $a -s $s"
      
      trec_eval.9.0/trec_eval -l3 qrels/QRelsCorrectedforTRECeval results/results.txt > "results/trec_eval_f"$f"a"$a"s"$s""
    done
  done
done
