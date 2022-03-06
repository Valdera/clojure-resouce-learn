(ns simple-rest-api.core
  (:require
   [dotenv :refer [env app-env]]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [org.httpkit.server :as server]
   [ring.middleware.json :as js]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
   [ring.middleware.reload :refer [wrap-reload]]
   [clojure.pprint :as pp]
   [clojure.string :as str]
   [clojure.data.json :as json]
   [simple-rest-api.lib.routes :as routes])
  (:gen-class))

(defroutes app-routes
  (GET "/" [] routes/echo-route)
  (GET "/friends" [] routes/get-friends-route)
  (POST "/friends" [] routes/add-friend-route))

(defn -main
  "Production"
  [& args]
  (let [port (Integer/parseInt (env :PORT))]
    (server/run-server (js/wrap-json-params (js/wrap-json-response (wrap-defaults #'app-routes api-defaults))) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))

(defn -dev-main
  "Dev/Test (auto reload watch enabled)"
  [& args]
  (let [port (Integer/parseInt (env :PORT))]
    (server/run-server (wrap-reload (js/wrap-json-params (js/wrap-json-response (wrap-defaults #'app-routes api-defaults)))) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))