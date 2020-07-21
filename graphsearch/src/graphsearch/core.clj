(ns graphsearch.core
  (:gen-class))

  (require '[clojure.string :as str])

(defn print-grid [grid]0.0                                                                          ;print out matrix
  (doseq [row grid]
    (doseq [value row] (print value " "))
    (println)
  )
  (println)
  (Thread/sleep 60)
)


(defn get-dims-matrix [matrix]                                                                     ;get matrix size

  {:nrows (count matrix)
   :ncols (count (first matrix))}
)



(defn query-matrix [matrix query-val]                                                                     ;Query matrix for co-ords

  (let [index (.indexOf (flatten matrix) query-val)
            ncols (:ncols (get-dims-matrix matrix))]
    (when (>= index 0)
      [(mod index ncols )( int (/ index ncols ))]

  ))
)

(defn set-matrix [matrix col row value]                                                           ;insert value into matrix
  (assoc matrix row
    (assoc (nth matrix row)
      col value ))

)

(defn check-coord [matrix col row]                                                               ;return value at coord
  (get (get matrix row)col)
)

(defn draw-path [matrix steps]                                                                       ;draw path onto a matrix

      (let [addstep (set-matrix matrix (first (first steps)) (last (first steps)) "*")]
        (if (> (count steps) 1)
          (recur addstep (rest steps))
        addstep))
)


(defn euclid [step1 step2]                                                                               ;calculate euclidean value

  ;(Math/sqrt 6)

      (Math/sqrt (+(* (-(first step1)(first step2)) (-(first step1)(first step2)))  (* (-(last step1)(last step2)) (-(last step1)(last step2)))))

  )


(defn cost [steps cost]                                                                                 ;caluculate the cost of steps

  (if (> (count steps) 1)
    (recur (rest steps)(+ cost(euclid (first steps) (first(rest steps)))))
    cost
    )

  )


(defn get-neighbours [initial-grid matrix steps comps print-states]                                      ;get neighbours

(letfn [(step [initial-grid matrix col row steps comps]
        (if (not= (check-coord matrix col row) "v")
          (if (= (check-coord matrix col row) "_")
              (do (if print-states (print-grid (set-matrix matrix col row "*")))
                  (get-neighbours initial-grid (set-matrix matrix col row "*") (conj steps (vec [col row])) (inc comps) print-states)))

          (do(println "Victim found in" (count steps) "steps after" comps "Comparaisons with cost of" (cost steps 0) ) (do(print-grid(draw-path initial-grid steps)) (println "Ctrl + c to exit")) (Thread/sleep 999999)))
        )]

  (let [coord (last steps)
        col (first coord)
        row (last coord)]

    (if (empty? steps) (println "No Path Found!")
          (do(step initial-grid matrix col (dec row) steps comps)                                     ;Systematically try each neighbour in a clock wise fashion
          (step initial-grid matrix (inc col) (dec row) steps comps)
          (step initial-grid matrix (inc col) row steps comps)
          (step initial-grid matrix (inc col) (inc row) steps comps)
          (step initial-grid matrix col (inc row) steps comps)
          (step initial-grid matrix (dec col) (inc row) steps comps)
          (step initial-grid matrix (dec col) row steps comps)
          (step initial-grid matrix (dec col) (dec row) steps comps)
          (do
              (println "Dead end" col row )                                                           ;if no neighbours
                 (get-neighbours initial-grid matrix (pop steps) (inc comps) print-states)             ;go back to the next state that does
               )))

  )
)
  )


(defn dfs [scenario print-states]                                                                   ;run dfs
  (let [readfile (slurp (apply str [ scenario ".csv"]))
        splitrows (str/split readfile #"\n")
        initial-grid (vec (map #(str/split % #",") splitrows))
        robot (query-matrix initial-grid "*")
        victim (query-matrix initial-grid "v")]


       (get-neighbours initial-grid initial-grid [robot] 0 print-states)

  )
)




(defn do-astar [initial-grid matrix steps victim comps print-states front cum-cost]                     ;do the heuristic calculation for coord
(letfn [(astep [initial-grid matrix col row steps comps cum-cost]

               (if (= [col row] victim)(hash-map :hue (euclid [col row] victim), :coord [col row])       ;if victim found return
               (if (= (check-coord matrix col row) "_")                                                   ;else return the value
               (hash-map :hue (+(euclid [col row] victim) (long(cost steps cum-cost))), :coord [col row]) (hash-map :hue 999, :coord [col row])))

               )]


    (let [coord (last steps)
        col (first coord)
        row (last coord)]

      (if print-states(print-grid matrix))
      (if (empty? steps) (println "No Path Found!")
        (do

                                                                                                          ;get the heuristics for all nighbours of current coord
          (let [front
            (sort-by :hue (vec(distinct(flatten [(astep initial-grid matrix col (dec row) steps comps cum-cost)
           (astep initial-grid matrix (inc col) (dec row) steps comps cum-cost)
           (astep initial-grid matrix (inc col) row steps comps cum-cost)
           (astep initial-grid matrix (inc col) (inc row) steps comps cum-cost)
           (astep initial-grid matrix col (inc row) steps comps cum-cost)
           (astep initial-grid matrix (dec col) (inc row) steps comps cum-cost)
           (astep initial-grid matrix (dec col) row steps comps cum-cost)
           (astep initial-grid matrix (dec col) (dec row) steps comps cum-cost)front]))))
             nextstep (first front) ]
            ;;(println nextstep)

            ;;(println front)
            ;;(println steps)

            ;if victim found print information
            (if (= 0.0(get nextstep :hue))
              (do(println "Victim found in" (count steps) "steps after" (inc comps) "Comparaisons with cost of" (cost steps 1)
                                                      (do(print-grid(draw-path initial-grid steps))(println "Ctrl + c to exit")))(Thread/sleep 999999))
            ;else try from the next best coord
            (do-astar initial-grid (set-matrix matrix (first (get nextstep :coord)) (last (get nextstep :coord)) "*") (conj steps (get nextstep :coord)) victim (+ comps 5) print-states (rest front) cum-cost)
            )

            )

          )
        )

      ))
  )



(defn astar [scenario print-states]                                                                   ;run a star

    (let [readfile (slurp (apply str [ scenario ".csv"]))
        splitrows (str/split readfile #"\n")
        initial-grid (vec (map #(str/split % #",") splitrows))
        robot (query-matrix initial-grid "*")
        victim (query-matrix initial-grid "v")]

        (do-astar initial-grid initial-grid [robot] victim 0 print-states () 0)
      )
  )


