(ns passman.app
  (:require [clojure.tools.cli :refer [parse-opts]]
            [passman.db :refer [list-passwords]]
            [passman.password :refer [generate-password]]
            [table.core :as t]))

(def cli-options
  [["-l" "--length Length" "Password Length"
    :default 40
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-g" "--generate" "Generate new password"]
   [nil "--list"]])

(defn -main [& args]
  (let [parsed-options (parse-opts args cli-options)
        url (first (:arguments parsed-options))
        username (second (:arguments parsed-options))
        options (:options parsed-options)]
    (println options)
    (cond
      (:generate options) (let [password (generate-password (:length options))]
                            password)
      (:list options) (t/table (list-passwords)))))

(comment
  (-main)
  (t/table (list-passwords)))