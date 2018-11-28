#!/usr/bin/env bash
echo "Runing Trec Eval with $1"
trec_eval.9.0/trec_eval qrels/$1 results/results.txt > "evaluation/trec_eval_result"
echo "Output generated at evaluation/trec_eval_result"