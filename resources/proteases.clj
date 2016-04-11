{"Arg-C"
 {:synonyms "Arginyl peptidase, Endoproteinase Arg-C, Tissue kallikrein"
  :pre [#{\R}]
  :post [aa-single-set]}
 "Asp-N"
 {:synonyms "Endoproteinase Asp-N, Peptidyl-Asp metalloendopeptidase"
  :pre [aa-single-set]
  :post [#{\D}]}
 "Asp-N (N-terminal Glu)"
 {:synonyms "Endoproteinase Asp-N, Peptidyl-Asp metalloendopeptidase"
  :pre [aa-single-set]
  :post [#{\D \E}]}
 "BNPS"
 {:synonyms "3-Bromo-3-methyl-2-(2-nitrophenylthio)-3H-indole, BNPS-skatol, N-chlorosuccinimide/urea"
  :pre [#{\W}]
  :post [aa-single-set]}
 "NCS/urea"
 {:synonyms ""
  :pre [#{\W}]
  :post [aa-single-set]}
 "Caspase-1"
 {:synonyms "ICE, Interleukin-1β-Converting Enzyme"
  :pre [#{\D} #{\A \H \T} aa-single-set #{\F \L \W \Y}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-10"
 {:synonyms "Flice2, Mch4"
  :pre [#{\D} #{\A} #{\E} #{\I}]
  :post [aa-single-set]}
 "Caspase-2"
 {:synonyms "Ich-1, Nedd2"
  :pre [#{\D} #{\A} #{\V} #{\D}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-2a"
 {:synonyms "Ich-1, Nedd2"
  :pre [#{\D} #{\H} #{\E} #{\D}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-3"
 {:synonyms "Apopain, CPP32, Yama"
  :pre [#{\D} #{\Q} #{\M} #{\D}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-3a"
 {:synonyms "Apopain, CPP32, Yama"
  :pre [#{\D} #{\V} #{\E} #{\D}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-4"
 {:synonyms "ICE(rel)II, Ich-2, TX"
  :pre [#{\D} #{\V} #{\E} #{\L}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-4a"
 {:synonyms "ICE(rel)II, Ich-2, TX"
  :pre [#{\D} #{\H} #{\E} #{\L \W}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-5"
 {:synonyms "ICE(rel)III, TY"
  :pre [#{\D} #{\H} #{\E} #{\L \W}]
  :post [aa-single-set]}
 "Caspase-6"
 {:synonyms "Mch2"
  :pre [#{\D} #{\H \I} #{\E} #{\V}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-7"
 {:synonyms "CMH-1, ICE-LAP3, Mch-3"
  :pre [#{\D} #{\V} #{\E} #{\D}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase 8"
 {:synonyms "FLICE, MASH, Mch5"
  :pre [#{\D} #{\T} #{\E} #{\I \L}]
  :post [(restrict-set #{\D \E \K \P \Q \R})]}
 "Caspase-9 "
 {:synonyms "ICE-Lap6, Mch6"
  :pre [#{\D} #{\H} #{\E} #{\L}]
  :post [aa-single-set]}
 "Chymotrypsin"
 {:synonyms ""
  :pre [#{\F \Y}]
  :post [(restrict-set #{\P})]}
 "Chymotrypsin-a"
 {:synonyms ""
  :pre [#{\W}]
  :post [(restrict-set #{\P \M})]}
 "Chymotrypsin (low specificity)"
 {:synonyms ""
  :pre [#{\F \L \Y}]
  :post [(restrict-set #{\P})]}
 "Chymotrypsin (low specificity)-a"
 {:synonyms ""
  :pre [#{\M}]
  :post [(restrict-set #{\P \Y})]}
 "Chymotrypsin (low specificity)-b"
 {:synonyms ""
  :pre [#{\H}]
  :post [(restrict-set #{\D \M \P \W})]}
 "Chymotrypsin (low specificity)-c"
 {:synonyms ""
  :pre [#{\W}]
  :post [(restrict-set #{\P \M})]}
 "Clostripain"
 {:synonyms "Clostridiopeptidase B"
  :pre [#{\R}]
  :post [aa-single-set]}
 "CNBr"
 {:synonyms "Cyanogen bromide"
  :pre [#{\M}]
  :post [aa-single-set]}
 "CNBr (methyl-Cys)"
 {:synonyms "Cyanogen bromide"
  :pre [#{\M}]
  :post [aa-single-set]}
 "CNBr (methyl-Cys)-a"
 {:synonyms "Cyanogen bromide"
  :pre [aa-single-set]
  :post [#{\C}]}
 "CNBr (with acids)"
 {:synonyms "Cyanogen bromide"
  :pre [#{\M \W}]
  :post [aa-single-set]}
 "Enterokinase"
 {:synonyms "Enteropeptidase"
  :pre [#{\K} #{\D \E} #{\D \E} #{\D \E}]
  :post [aa-single-set]}
 "Factor Xa"
 {:synonyms "Coagulation factor Xa"
  :pre [#{\R} #{\G} #{\D \E} #{\A \F \G \I \L \T \V \M}]
  :post [aa-single-set]}
 "Formic acid"
 {:synonyms ""
  :pre [#{\D}]
  :post [aa-single-set]}
 "Glu-C (AmAc buffer)"
 {:synonyms "Endoproteinase Glu-C, V8 protease, Glutamyl endopeptidase"
  :pre [#{\E}]
  :post [aa-single-set]}
 "Glu-C (Phos buffer)"
 {:synonyms "Endoproteinase Glu-C, V8 protease, Glutamyl endopeptidase"
  :pre [#{\E \D}]
  :post [aa-single-set]}
 "Granzyme B"
 {:synonyms "Cytotoxic T-lymphocyte proteinase 2, Granzyme-2, GranzymeB, Lymphocyte protease, SECT, T-cell serine protease 1-3E"
  :pre [#{\D} #{\P} #{\E} #{\I}]
  :post [aa-single-set]}
 "HRV3C protease"
 {:synonyms "Human rhinovirus 3C protease, Picornain 3C, Protease 3C"
  :pre [#{\Q} #{\F} #{\L} #{\V}]
  :post [#{\G} #{\P}]}
 "Hydroxylamine"
 {:synonyms "Hydroxylammonium"
  :pre [#{\N}]
  :post [#{\G}]}
 "Iodosobenzoic acid"
 {:synonyms "2-Iodosobenzoic acid"
  :pre [#{\W}]
  :post [aa-single-set]}
 "Lys-C"
 {:synonyms "Endoproteinase Lys-C, Lysyl endopeptidase"
  :pre [#{\K}]
  :post [aa-single-set]}
 "Lys-N"
 {:synonyms "Endoproteinase Lys-N, Peptidyl-Lys metalloendopeptidase, Armillaria mellea neutral proteinase"
  :pre [aa-single-set]
  :post [#{\K}]}
 "Lys-N (Cys modified)"
 {:synonyms "Endoproteinase Lys-N, Peptidyl-Lys metalloendopeptidase, Armillaria mellea neutral proteinase"
  :pre [aa-single-set]
  :post [#{\C \K}]}
 "Mild acid hydrolysis"
 {:synonyms ""
  :pre [#{\D}]
  :post [#{\P}]}
 "NBS (long exposure)"
 {:synonyms "N-Bromosuccinimide"
  :pre [#{\H \W \Y}]
  :post [aa-single-set]}
 "NBS (short exposure)"
 {:synonyms "N-Bromosuccinimide"
  :pre [#{\W \Y}]
  :post [aa-single-set]}
 "NTCB"
 {:synonyms "2-Nitro-5-thiocyanatobenzoic acid, 2-Nitro-5-thiocyanobenzoic acid"
  :pre [aa-single-set]
  :post [#{\C}]}
 "Pancreatic elastase"
 {:synonyms "Pancreatopeptidase E, Elastase-1"
  :pre [#{\A \G \S \V}]
  :post [aa-single-set]}
 "Pepsin A"
 {:synonyms "Pepsin"
  :pre [(restrict-set #{\R}) (restrict-set #{\P}) (restrict-set #{\H \K \R})]
  :post [#{\F \L \W \Y} (restrict-set #{\P})]}
 "Pepsin A-a"
 {:synonyms "Pepsin"
  :pre [#{\F \L \W \Y} (restrict-set #{\P}) (restrict-set #{\H \K \R})]
  :post [aa-single-set (restrict-set #{\P})]}
 "Pepsin A (low specificity)"
 {:synonyms "Pepsin"
  :pre [(restrict-set #{\R}) (restrict-set #{\P}) (restrict-set #{\H \K \R})]
  :post [#{\F \L} (restrict-set #{\P})]}
 "Pepsin A (low specificity)-a"
 {:synonyms "Pepsin"
  :pre [#{\F \L} (restrict-set #{\P}) (restrict-set #{\H \K \R})]
  :post [aa-single-set (restrict-set #{\P})]}
 "Prolyl endopeptidase"
 {:synonyms "Prolyl oligopeptidase, Post-proline cleaving enzyme"
  :pre [#{\P} #{\H \K \R}]
  :post [(restrict-set #{\P})]}
 "Proteinase K"
 {:synonyms "Endopeptidase K, Peptidase K"
  :pre [#{\A \E \F \I \L \T \V \W \Y}]
  :post [aa-single-set]}
 "TEV protease"
 {:synonyms "Tobacco etch virus protease, Nuclear-inclusion-a endopeptidase"
  :pre [#{\Q} aa-single-set #{\Y} aa-single-set aa-single-set #{\E}]
  :post [#{\G \S}]}
 "Thermolysin"
 {:synonyms "Thermophilic-bacterial protease"
  :pre [(restrict-set #{\D \E})]
  :post [#{\A \F \I \L \M \V} (restrict-set #{\P})]}
 "Thrombin"
 {:synonyms "Factor IIa"
  :pre [#{\R} #{\G}]
  :post [#{\G}]}
 "Thrombin-a"
 {:synonyms "Factor IIa"
  :pre [#{\R} #{\P} #{\A \F \G \I \L \T \V \W} #{\A \F \G \I \L \T \V \W}]
  :post [(restrict-set #{\D \E}) (restrict-set #{\D \E})]}
 "Trypsin"
 {:synonyms "Trypsin-1"
  :pre [#{\R \K}]
  :post [(restrict-set #{\P})]
  :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]}
 "Trypsin-a"
 {:synonyms "Trypsin-1"
  :pre [#{\K} #{\W}]
  :post [#{\P}]}
 "Trypsin-b"
 {:synonyms "Trypsin-1"
  :pre [#{\R} #{\M}]
  :post [#{\P}]}
 "Trypsin (Arg blocked)"
 {:synonyms "Trypsin-1"
  :pre [#{\K}]
  :post [(restrict-set #{\P})]
  :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]}
 "Trypsin (Cys modified)"
 {:synonyms "Trypsin-1"
  :pre [#{\K \R \C}]
  :post [(restrict-set #{\P})]
  :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]}
 "Trypsin (Lys blocked)"
 {:synonyms "Trypsin-1"
  :pre [#{\R}]
  :post [(restrict-set #{\P})]
  :not [#"[CD]KD" #"CK[HY]" #"RR[HR]"]}}
