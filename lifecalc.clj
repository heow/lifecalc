#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {cljc.java-time/cljc.java-time {:mvn/version "0.1.12"}}})

(ns lifcalc
  (:require [cljc.java-time.local-date :as ld]
            [cljc.java-time.temporal.chrono-unit :as cu]
            [clojure.string :as str]
            [clojure.tools.cli :as cli]
            [clojure.edn :as edn]))

(def default-user 
  {:name     "Borkdude" 
   :lifespan 81.1    
   :birthday "1981-02-08"})  

(defn max-days [max-years] (int (* 365.2564 max-years)))

(defn make-cal [bday max-years]
  (let [calc-from (partial cu/between cu/days bday)]
    (fn [& [s]]
      (cond (empty? s) (calc-from (ld/now))
            (str/includes? s "%") (int (* (max-days max-years) (/ (Float/parseFloat (str/replace s "%" "")) 100)))
            (str/includes? s "-") (calc-from (ld/parse s))
            :else (str (ld/plus-days bday (Integer/parseInt (str s))))))))

(defn describe [name max-days lifedays]
  (format "%s is %d days old, expiration in %d days. (%.2f%% remaining)"
          name lifedays (- max-days lifedays) (double (- 100 (* 100 (/ lifedays max-days)))) ))

(when (not (nil? (System/getProperty "babashka.file"))) ; run as script? 
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts *command-line-args*
                        [["-n" "--name NAME"           "Name of person"]
                         ["-l" "--lifespan YEARS"      "Lifespan in years" :parse-fn #(Float/parseFloat %)]
                         ["-b" "--birthday YYYY-MM-DD" "Birthday"]
                         ["-s" "--stdin"               "Read or pipe in from STDIN"]
                         ["-e" "--export"              "EXPORT state to bash"]
                         ["-h" "--help" ]] )
        all-opts (merge default-user (edn/read-string (System/getenv "LIFECALC")) options) ] ; merge all options
    (cond
      (or (:help options) (not (nil? errors))) (println (str summary "\n" errors))
      (:export options)   (println (str "export LIFECALC='" (pr-str (dissoc all-opts :export)) "'"))
      :else (let [user    (update all-opts :birthday ld/parse) ; parse bday
                  cal     (make-cal (:birthday user) (:lifespan user)) ]
              (doseq [s (if (:stdin options) (map (fn[x] (first (str/split x #" "))) (str/split-lines (slurp *in*)))
                            [(first arguments)])]
                (println (if (not (nil? s)) (cal s)
                             (describe (:name user) (max-days (:lifespan user)) (cal)) )))))))
