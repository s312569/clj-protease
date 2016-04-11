(ns clj-protease.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :refer [resource]]
            [clojure.string :as str]))

(declare protease)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; data
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defonce amino-acids (edn/read-string (slurp (resource "aa-iupac.clj"))))
(defonce aa-single-set (set (remove #{\*} (keys amino-acids))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; utilities
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- restrict-set
  [s]
  (-> (remove s aa-single-set)
      set))

(defn- matches?
  [f & args]
  (->> (apply map f args)
       (remove nil?)
       seq
       ((complement nil?))))

(defn- get-protease
  [s]
  (if-let [p (protease s)]
    p
    (throw (Exception. (str "No enzyme found for: '" s "'")))))

(defn- clean-string
  [s]
  (str/replace s #"\s" ""))

(defn- re-pos [re s]
  (loop [m (re-matcher re s)
         res {}]
    (if (.find m)
      (recur m (assoc res (.start m) (.group m 1)))
      res)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; api
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn check-motif
  "Takes a pre and a post cleavage sequences and a protease name and
  returns true if cleavage is consistent with specified protease,
  false otherwise. Useful for analysing peptides derived from mass
  spec data where the full motif may not be present from the search
  results."
  [pre post prot]
  (let [p (protease prot)
        r (and (matches? (fn [s c] (s c)) (:pre p) (reverse pre))
               (matches? (fn [s c] (s c)) (:post p) (seq post)))]
    (if (and r (:not p))
      (let [cs (str (apply str (->> (reverse pre) (take 2) reverse))
                    (first post))]
        (->> (matches? #(re-find % cs) (:not p))
             not))
      r)))

(defn scan-sequence
  "Takes a sequence string and a protease name and returns a
  collection of maps specifying cleavage sites. Maps contain indices
  for the recognition motif start and cleavage site and the sequence
  of the recognition motif."
  [s prot]
  (let [p (protease prot)
        r (map (fn [[k v]]
                 {:motif-start k
                  :motif v
                  :cleave-after (if (and (:nterm (protease prot))
                                         (= (count v) 2)
                                         (= k 0))
                                  0
                                  (+ k (:cleave-after (protease prot))))})
               (re-pos (:re p) (clean-string s)))]
    (if-not (:not p)
      r
      (filter #(not (matches? (fn [x] (re-find x (:motif %)))
                              (:not p)))
              r))))

(defn digest
  "Returns a collection of strings representing digestion of a
  sequence with the specified protease."
  [s prot]
  (let [cs (clean-string s)
        cl (->> (scan-sequence cs prot)
                (map :cleave-after)
                sort
                (partition 2 1))]
    (concat (list (subs cs 0 (+ 1 (first (first cl)))))
            (map (fn [[from to]] (subs cs (+ 1 from) (+ 1 to))) cl)
            (list (subs cs (+ 1 (last (last cl))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; protease data
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def tsf "KTCSRQFTSSSSMKGSCGIGGGIGGGSSRISSVLAGGSCRAPSTYGGGLSVSSSRFSSG
GACGLGGGYGGGFSSSSSSFGSGFGGGYGGGLGAGLGGGFGGGFAGGDGLLVGSEKVTMQ
NLNDRLASYLDKVRALEEANADLEVKIRDWYQRQRPAEIKDYSPYFKTIEDLRNKILTAT
VDNANVLLQIDNARLAADDFRTKYETELNLRMSVEADINGLRRVLDELTLARADLEMQIE
SLKEELAYLKKNHEEEMNALRGQVGGDVNVEMDAAPGVDLSRILNEMRDQYEKMAEKNRK
DAEEWFFTKTEELNREVATNSELVQSGKSEISELRRTMQNLEIELQSQLSMKASLENSLE
ETKGRYCMQLAQIQEMIGSVEEQLAQLRCEMEQQNQEYKILLDVKTRLEQEIATYRRLLE
GEDAHLSSSQFSSGSQSSRDVTSSSRQIRTKVMDVHDGKVVSTHEQVLRTKN")

(def protease
  {"Arg-C"
   {:synonyms "Arginyl peptidase, Endoproteinase Arg-C, Tissue kallikrein"
    :re #"(?=(R[A-Z]))"
    :cleave-after 0
    :pre [#{\R}]
    :post [aa-single-set]}
   "Asp-N"
   {:synonyms "Endoproteinase Asp-N, Peptidyl-Asp metalloendopeptidase"
    :re #"(?=([A-Z]D))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\D}]}
   "Asp-N (N-terminal Glu)"
   {:synonyms "Endoproteinase Asp-N, Peptidyl-Asp metalloendopeptidase"
    :re #"(?=([A-Z][D E]))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\D \E}]}
   "BNPS"
   {:synonyms "3-Bromo-3-methyl-2-(2-nitrophenylthio)-3H-indole, BNPS-skatol, N-chlorosuccinimide/urea"
    :re #"(?=(W[A-Z]))"
    :cleave-after 0
    :pre [#{\W}]
    :post [aa-single-set]}
   "NCS/urea"
   {:synonyms ""
    :re #"(?=(W[A-Z]))"
    :cleave-after 0
    :pre [#{\W}]
    :post [aa-single-set]}
   "Caspase-1"
   {:synonyms "ICE, Interleukin-1β-Converting Enzyme"
    :re #"(?=([F L W Y][A-Z][A H T]D[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\A \H \T} aa-single-set #{\F \L \W \Y}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-10"
   {:synonyms "Flice2, Mch4"
    :re #"(?=(IEAD[A-Z]))"
    :cleave-after 3
    :pre [#{\D} #{\A} #{\E} #{\I}]
    :post [aa-single-set]}
   "Caspase-2"
   {:synonyms "Ich-1, Nedd2"
    :re #"(?=(DVAD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\A} #{\V} #{\D}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-2a"
   {:synonyms "Ich-1, Nedd2"
    :re #"(?=(DEHD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\H} #{\E} #{\D}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-3"
   {:synonyms "Apopain, CPP32, Yama"
    :re #"(?=(DMQD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\Q} #{\M} #{\D}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-3a"
   {:synonyms "Apopain, CPP32, Yama"
    :re #"(?=(DEVD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\V} #{\E} #{\D}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-4"
   {:synonyms "ICE(rel)II, Ich-2, TX"
    :re #"(?=(LEVD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\V} #{\E} #{\L}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-4a"
   {:synonyms "ICE(rel)II, Ich-2, TX"
    :re #"(?=([^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\H} #{\E} #{\L \W}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-5"
   {:synonyms "ICE(rel)III, TY"
    :re #"(?=([L W]EHD[A-Z]))"
    :cleave-after 3
    :pre [#{\D} #{\H} #{\E} #{\L \W}]
    :post [aa-single-set]}
   "Caspase-6"
   {:synonyms "Mch2"
    :re #"(?=(VE[H I]D[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\H \I} #{\E} #{\V}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-7"
   {:synonyms "CMH-1, ICE-LAP3, Mch-3"
    :re #"(?=(DEVD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\V} #{\E} #{\D}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase 8"
   {:synonyms "FLICE, MASH, Mch5"
    :re #"(?=([I L]ETD[^ D E K P Q R]))"
    :cleave-after 3
    :pre [#{\D} #{\T} #{\E} #{\I \L}]
    :post [(restrict-set #{\D \E \K \P \Q \R})]}
   "Caspase-9 "
   {:synonyms "ICE-Lap6, Mch6"
    :re #"(?=(LEHD[A-Z]))"
    :cleave-after 3
    :pre [#{\D} #{\H} #{\E} #{\L}]
    :post [aa-single-set]}
   "Chymotrypsin"
   {:synonyms ""
    :re #"(?=([F Y][^ P]))"
    :cleave-after 0
    :pre [#{\F \Y}]
    :post [(restrict-set #{\P})]}
   "Chymotrypsin-a"
   {:synonyms ""
    :re #"(?=(W[^ P M]))"
    :cleave-after 0
    :pre [#{\W}]
    :post [(restrict-set #{\P \M})]}
   "Chymotrypsin (low specificity)"
   {:synonyms ""
    :re #"(?=([F L Y][^ P]))"
    :cleave-after 0
    :pre [#{\F \L \Y}]
    :post [(restrict-set #{\P})]}
   "Chymotrypsin (low specificity)-a"
   {:synonyms ""
    :re #"(?=(M[^ P Y]))"
    :cleave-after 0
    :pre [#{\M}]
    :post [(restrict-set #{\P \Y})]}
   "Chymotrypsin (low specificity)-b"
   {:synonyms ""
    :re #"(?=(H[^ D M P W]))"
    :cleave-after 0
    :pre [#{\H}]
    :post [(restrict-set #{\D \M \P \W})]}
   "Chymotrypsin (low specificity)-c"
   {:synonyms ""
    :re #"(?=(W[^ M P]))"
    :cleave-after 0
    :pre [#{\W}]
    :post [(restrict-set #{\P \M})]}
   "Clostripain"
   {:synonyms "Clostridiopeptidase B"
    :re #"(?=(R[A-Z]))"
    :cleave-after 0
    :pre [#{\R}]
    :post [aa-single-set]}
   "CNBr"
   {:synonyms "Cyanogen bromide"
    :re #"(?=(M[A-Z]))"
    :cleave-after 0
    :pre [#{\M}]
    :post [aa-single-set]}
   "CNBr (methyl-Cys)"
   {:synonyms "Cyanogen bromide"
    :re #"(?=(M[A-Z]))"
    :cleave-after 0
    :pre [#{\M}]
    :post [aa-single-set]}
   "CNBr (methyl-Cys)a"
   {:synonyms "Cyanogen bromide"
    :re #"(?=([A-Z]C]))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\C}]}
   "CNBr (with acids)"
   {:synonyms "Cyanogen bromide"
    :re #"(?=([M W][A-Z]))"
    :cleave-after 0
    :pre [#{\M \W}]
    :post [aa-single-set]}
   "Enterokinase"
   {:synonyms "Enteropeptidase"
    :re #"(?=([D E][D E][D E]K[A-Z]))"
    :cleave-after 3
    :pre [#{\K} #{\D \E} #{\D \E} #{\D \E}]
    :post [aa-single-set]}
   "Factor Xa"
   {:synonyms "Coagulation factor Xa"
    :re #"(?=([A F G I L T V M][D E]GR[A-Z]))"
    :cleave-after 3
    :pre [#{\R} #{\G} #{\D \E} #{\A \F \G \I \L \T \V \M}]
    :post [aa-single-set]}
   "Formic acid"
   {:synonyms ""
    :re #"(?=(D[A-Z]))"
    :cleave-after 0
    :pre [#{\D}]
    :post [aa-single-set]}
   "Glu-C (AmAc buffer)"
   {:synonyms "Endoproteinase Glu-C, V8 protease, Glutamyl endopeptidase"
    :re #"(?=(E[A-Z]))"
    :cleave-after 0
    :pre [#{\E}]
    :post [aa-single-set]}
   "Glu-C (Phos buffer)"
   {:synonyms "Endoproteinase Glu-C, V8 protease, Glutamyl endopeptidase"
    :re #"(?=([E D][A-Z]))"
    :cleave-after 0
    :pre [#{\E \D}]
    :post [aa-single-set]}
   "Granzyme B"
   {:synonyms "Cytotoxic T-lymphocyte proteinase 2, Granzyme-2, GranzymeB, Lymphocyte protease, SECT, T-cell serine protease 1-3E"
    :re #"(?=(IEPD[A-Z]))"
    :cleave-after 3
    :pre [#{\D} #{\P} #{\E} #{\I}]
    :post [aa-single-set]}
   "HRV3C protease"
   {:synonyms "Human rhinovirus 3C protease, Picornain 3C, Protease 3C"
    :re #"(?=(VLFQ[^ P G]))"
    :cleave-after 3
    :pre [#{\Q} #{\F} #{\L} #{\V}]
    :post [#{\G} #{\P}]}
   "Hydroxylamine"
   {:synonyms "Hydroxylammonium"
    :re #"(?=(NG))"
    :cleave-after 0
    :pre [#{\N}]
    :post [#{\G}]}
   "Iodosobenzoic acid"
   {:synonyms "2-Iodosobenzoic acid"
    :re #"(?=(W[A-Z]))"
    :cleave-after 0
    :pre [#{\W}]
    :post [aa-single-set]}
   "Lys-C"
   {:synonyms "Endoproteinase Lys-C, Lysyl endopeptidase"
    :re #"(?=(K[A-Z]))"
    :cleave-after 0
    :pre [#{\K}]
    :post [aa-single-set]}
   "Lys-N"
   {:synonyms "Endoproteinase Lys-N, Peptidyl-Lys metalloendopeptidase, Armillaria mellea neutral proteinase"
    :re #"(?=([A-Z]K))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\K}]}
   "Lys-N (Cys modified)"
   {:synonyms "Endoproteinase Lys-N, Peptidyl-Lys metalloendopeptidase, Armillaria mellea neutral proteinase"
    :re #"(?=([A-Z][C K]))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\C \K}]}
   "Mild acid hydrolysis"
   {:synonyms ""
    :re #"(?=(DP))"
    :cleave-after 0
    :pre [#{\D}]
    :post [#{\P}]}
   "NBS (long exposure)"
   {:synonyms "N-Bromosuccinimide"
    :re #"(?=([H W Y][A-Z]))"
    :cleave-after 0
    :pre [#{\H \W \Y}]
    :post [aa-single-set]}
   "NBS (short exposure)"
   {:synonyms "N-Bromosuccinimide"
    :re #"(?=([W Y][A-Z]))"
    :cleave-after 0
    :pre [#{\W \Y}]
    :post [aa-single-set]}
   "NTCB"
   {:synonyms "2-Nitro-5-thiocyanatobenzoic acid, 2-Nitro-5-thiocyanobenzoic acid"
    :re #"(?=([A-Z]C))"
    :cleave-after 0
    :pre [aa-single-set]
    :post [#{\C}]}
   "Pancreatic elastase"
   {:synonyms "Pancreatopeptidase E, Elastase-1"
    :re #"(?=([A G S V][A-Z]))"
    :cleave-after 0
    :pre [#{\A \G \S \V}]
    :post [aa-single-set]}
   "Pepsin A"
   {:synonyms "Pepsin"
    :re #"(?=([^ H K R][^ P][^ R][F L W Y][^ P]))"
    :cleave-after 2
    :pre [(restrict-set #{\R}) (restrict-set #{\P}) (restrict-set #{\H \K \R})]
    :post [#{\F \L \W \Y} (restrict-set #{\P})]}
   "Pepsin A-a"
   {:synonyms "Pepsin"
    :re #"(?=([^ H K R][^ P][F L W Y][A-Z][^ P]))"
    :cleave-after 2
    :pre [#{\F \L \W \Y} (restrict-set #{\P}) (restrict-set #{\H \K \R})]
    :post [aa-single-set (restrict-set #{\P})]}
   "Pepsin A (low specificity)"
   {:synonyms "Pepsin"
    :re #"(?=([^ H K R][^ P][^ R][F L][^ P]))"
    :cleave-after 2
    :pre [(restrict-set #{\R}) (restrict-set #{\P}) (restrict-set #{\H \K \R})]
    :post [#{\F \L} (restrict-set #{\P})]}
   "Pepsin A (low specificity)-a"
   {:synonyms "Pepsin"
    :re #"(?=([^ H K R][^ P][F L][A-Z][^ P]))"
    :cleave-after 2
    :pre [#{\F \L} (restrict-set #{\P}) (restrict-set #{\H \K \R})]
    :post [aa-single-set (restrict-set #{\P})]}
   "Prolyl endopeptidase"
   {:synonyms "Prolyl oligopeptidase, Post-proline cleaving enzyme"
    :re #"(?=([H K R]P[^ P]))"
    :cleave-after 1
    :pre [#{\P} #{\H \K \R}]
    :post [(restrict-set #{\P})]}
   "Proteinase K"
   {:synonyms "Endopeptidase K, Peptidase K"
    :re #"(?=([A E F I L T V W Y][A-Z]))"
    :cleave-after 0
    :pre [#{\A \E \F \I \L \T \V \W \Y}]
    :post [aa-single-set]}
   "TEV protease"
   {:synonyms "Tobacco etch virus protease, Nuclear-inclusion-a endopeptidase"
    :re #"(?=(E[A-Z][A-Z]Y[A-Z]Q[G S]))"
    :cleave-after 5
    :pre [#{\Q} aa-single-set #{\Y} aa-single-set aa-single-set #{\E}]
    :post [#{\G \S}]}
   "Thermolysin"
   {:synonyms "Thermophilic-bacterial protease"
    :re #"(?=([^ D E][A F I L M V][^ P]))"
    :cleave-after 0
    :pre [(restrict-set #{\D \E})]
    :post [#{\A \F \I \L \M \V} (restrict-set #{\P})]}
   "Thrombin"
   {:synonyms "Factor IIa"
    :re #"(?=(GRG))"
    :cleave-after 1
    :pre [#{\R} #{\P} #{\A \F \G \I \L \T \V \W} #{\A \F \G \I \L \T \V \W}]
    :post [(restrict-set #{\D \E}) (restrict-set #{\D \E})]}
   "Thrombin-a"
   {:synonyms "Factor IIa"
    :re #"(?=([A F G I L T V W][A F G I L T V W]PR[^ D E][^ D E]))"
    :cleave-after 3
    :pre [#{\R} #{\P} #{\A \F \G \I \L \T \V \W} #{\A \F \G \I \L \T \V \W}]
    :post [(restrict-set #{\D \E}) (restrict-set #{\D \E})]}
   "Trypsin"
   {:synonyms "Trypsin-1"
    :re #"(?=(([A-Z]|^)[R K][^P]))"
    :nterm true
    :cleave-after 1
    :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]
    :pre [#{\R \K}]
    :post [(restrict-set #{\P})]}
   "Trypsin-a"
   {:synonyms "Trypsin-1"
    :re [#"(?=(WKP))"]
    :cleave-after 1
    :pre [#{\K} #{\W}]
    :post [#{\P}]}
   "Trypsin-b"
   {:synonyms "Trypsin-1"
    :re #"(?=(MRP))"
    :cleave-after 1
    :pre [#{\R} #{\M}]
    :post [#{\P}]}
   "Trypsin (Arg blocked)"
   {:synonyms "Trypsin-1"
    :re #"(?=(([A-Z]|^)K[^ P]))"
    :nterm true
    :cleave-after 1
    :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]
    :pre [#{\K}]
    :post [(restrict-set #{\P})]}
   "Trypsin (Cys modified)"
   {:synonyms "Trypsin-1"
    :re #"(?=(([A-Z]|^)[K R C][^ P]))"
    :nterm true
    :cleave-after 1
    :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]
    :pre [#{\K \R \C}]
    :post [(restrict-set #{\P})]}
   "Trypsin (Lys blocked)"
   {:synonyms "Trypsin-1"
    :re #"(?=(([A-Z]|^)R[^ P]))"
    :nterm true
    :cleave-after 1
    :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]
    :pre [#{\R}]
    :post [(restrict-set #{\P})]}})
