#!/usr/bin/env bb
;; [a b c] [d e f] => [a d] [b e] [c f]
(doseq [[a b] (partition 2 (interleave (clojure.string/split-lines (slurp *in*))
                                       (clojure.string/split-lines (slurp (first *command-line-args*)))))]
         (println (str a " " b)))

