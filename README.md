# clj-protease

Some simple utilities for analysing protease cleavage of protein sequences.

## Usage

Install through lein in project file:


Include by:

```clj
(:require [clj-protease.core])
```

`scan-sequence` takes a sequence string and a protease name and
returns a collection of maps describing cleavage sites:

```clj
clj-protease.core> (def tsf "MTCSRQFTSSSSMKGSCGIGGGIGGGSSRISSVLAGGSCRAPSTYGGGLSVSSSRFSSGGACGLGGGYGGGFSSSSSSFGSGFGGGYGGGLGAGLGGGFGGGFAGGDGLLVGSEKVTMQNLNDRLASYLDKVRALEEANADLEVKIRDWYQRQRPAEIKDYSPYFKTIEDLRNKILTATVDNANVLLQIDNARLAADDFRTKYETELNLRMSVEADINGLRRVLDELTLARADLEMQIESLKEELAYLKKNHEEEMNALRGQVGGDVNVEMDAAPGVDLSRILNEMRDQYEKMAEKNRKDAEEWFFTKTEELNREVATNSELVQSGKSEISELRRTMQNLEIELQSQLSMKASLENSLEETKGRYCMQLAQIQEMIGSVEEQLAQLRCEMEQQNQEYKILLDVKTRLEQEIATYRRLLEGEDAHLSSSQFSSGSQSSRDVTSSSRQIRTKVMDVHDGKVVSTHEQVLRTKN")
#'clj-protease.core/tsf
clj-protease.core> (scan-sequence tsf "Trypsin")
({:motif-start 443, :motif "SRQ", :cleave-after 444} {:motif-start 164, :motif "FKT", :cleave-after 165} {:motif-start 240, :motif "LKE", :cleave-after 241} {:motif-start 468, :motif "TKN", :cleave-after 469} {:motif-start 229, :motif "ARA", :cleave-after 230} {:motif-start 466, :motif "LRT", :cleave-after 467} {:motif-start 27, :motif "SRI", :cleave-after 28} {:motif-start 385, :motif "LRC", :cleave-after 386} {:motif-start 360, :motif "TKG", :cleave-after 361} {:motif-start 448, :motif "TKV", :cleave-after 449} {:motif-start 297, :motif "RKD", :cleave-after 298} {:motif-start 157, :motif "IKD", :cleave-after 158} {:motif-start 349, :motif "MKA", :cleave-after 350} {:motif-start 290, :motif "EKM", :cleave-after 291} {:motif-start 402, :motif "VKT", :cleave-after 403} ...)
clj-protease.core> 
```

Protease information is kept in map called `protease` so to see
protease names call:

```clj
clj-protease.core> (keys protease)
("Caspase-9 " "BNPS" "Caspase-1" "Caspase-3" "Thrombin-a" "Clostripain" "Pepsin A" "Trypsin (Cys modified)" "Trypsin" "CNBr (methyl-Cys)a" "Trypsin-b" "NTCB" "Asp-N (N-terminal Glu)" "Pepsin A-a" "Granzyme B" "Lys-N" "Trypsin-a" "Prolyl endopeptidase" "Proteinase K" "Caspase-2a" "Lys-N (Cys modified)" "NBS (short exposure)" "Thrombin" "CNBr (with acids)" "Iodosobenzoic acid" "NBS (long exposure)" "Factor Xa" "Caspase-2" "Chymotrypsin (low specificity)-b" "Glu-C (AmAc buffer)" "Thermolysin" "Trypsin (Arg blocked)" "Caspase-10" "Pancreatic elastase" "Formic acid" "CNBr" "Chymotrypsin-a" "Pepsin A (low specificity)" "Caspase-3a" "HRV3C protease" "Trypsin (Lys blocked)" "Mild acid hydrolysis" "Pepsin A (low specificity)-a" "TEV protease" "CNBr (methyl-Cys)" "NCS/urea" "Chymotrypsin" "Enterokinase" "Caspase-7" "Chymotrypsin (low specificity)-a" "Hydroxylamine" "Lys-C" "Asp-N" "Caspase-4a" "Caspase-5" "Caspase-6" "Caspase 8" "Glu-C (Phos buffer)" "Chymotrypsin (low specificity)" "Caspase-4" "Chymotrypsin (low specificity)-c" "Arg-C")
clj-protease.core>
```

Call `vals` to see the data, including regular expressions and
synonyms etc.

`digest` takes a sequence string and a protease name and returns a
collection of strings representing peptides after cleavage with the
protease.

```clj
clj-protease.core> (digest tsf "Trypsin")
("MTCSR" "QFTSSSSMK" "GSCGIGGGIGGGSSR" "ISSVLAGGSCR" "APSTYGGGLSVSSSR" "FSSGGACGLGGGYGGGFSSSSSSFGSGFGGGYGGGLGAGLGGGFGGGFAGGDGLLVGSEK" "VTMQNLNDR" "LASYLDK" "VR" "ALEEANADLEVK" "IR" "DWYQR" "QRPAEIK" "DYSPYFK" "TIEDLR" "NK" "ILTATVDNANVLLQIDNAR" "LAADDFR" "TK" "YETELNLR" "MSVEADINGLR" "R" "VLDELTLAR" "ADLEMQIESLK" "EELAYLK" "K" "NHEEEMNALR" "GQVGGDVNVEMDAAPGVDLSR" "ILNEMR" "DQYEK" "MAEK" "NR" "K" "DAEEWFFTK" "TEELNR" "EVATNSELVQSGK" "SEISELR" "R" "TMQNLEIELQSQLSMK" "ASLENSLEETK" "GR" "YCMQLAQIQEMIGSVEEQLAQLR" "CEMEQQNQEYK" "ILLDVK" "TR" "LEQEIATYR" "R" "LLEGEDAHLSSSQFSSGSQSSR" "DVTSSSR" "QIR" "TK" "VMDVHDGK" "VVSTHEQVLR" "TK" "N")
clj-protease.core>
```

`check-motif` Takes a pre and a post cleavage sequences and a protease
name and returns true if cleavage is consistent with specified
protease, false otherwise. Useful for analysing peptides derived from
mass spec data where the full motif may not be present in the search
results. Slower than scan-sequence.

```clj
clj-protease.core> (check-motif "SQWWR" "DWSWSWWSSWS" "Trypsin")
true
clj-protease.core> (check-motif "ASADDDF" "RKRKGRKGNNG" "Trypsin")
false
clj-protease.core>
```

## License

Copyright © 2016 Jason Mulvenna

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
