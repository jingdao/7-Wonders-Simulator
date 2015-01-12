7 Wonders Board Game Simulator
===========

Commmand line:

	# Building
	javac -d bin src/controller/* src/model/* src/view/*
	# Manual Simulation with debug printouts
	java -cp bin controller.Controller 
	# Automatic simulation
	java -cp bin controller.Simulator

Simulation results:

	(N=10000 T=5 MINS, shared wins also counted)
	
	Side A 
	Wonder        |  Avg StdDev Wins
	       RHODES | 38.6    7.8 1505
	   ALEXANDRIA | 38.9    7.9 1534
	      EPHESOS | 38.8    7.9 1478
	      BABYLON | 38.8    7.7 1493
	      OLYMPIA | 39.0    7.8 1532
    HALIKARNASSOS | 37.5    7.9 1168
            GIZAH | 38.1    7.9 1315
            
    Side B
	Wonder        |  Avg StdDev Wins
           RHODES | 38.7    7.9 1561
       ALEXANDRIA | 38.3    8.1 1492
          EPHESOS | 38.6    7.9 1522
          BABYLON | 38.6    7.8 1473
          OLYMPIA | 38.3    7.9 1390
    HALIKARNASSOS | 37.7    8.0 1299
            GIZAH | 37.9    7.9 1289    

More randomized simulation (random wonder side, number of players and sitting order):

	(N=10000 T=2 MINS, shared wins also counted)
	
	Wonder           | Trials  Avg StdDev Wins WinRate
	       RHODES(A) |   3504 42.8    7.7  737  0.2103
	       RHODES(B) |   3625 45.6    7.9 1120  0.3090
	   ALEXANDRIA(A) |   3640 42.9    7.6  716  0.1967
	   ALEXANDRIA(B) |   3539 42.1    7.7  658  0.1859
	      EPHESOS(A) |   3684 43.1    7.6  721  0.1957
	      EPHESOS(B) |   3482 43.8    7.8  733  0.2105
	      BABYLON(A) |   3540 43.5    7.6  738  0.2085
	      BABYLON(B) |   3562 43.4    8.3  794  0.2229
	      OLYMPIA(A) |   3640 42.4    7.6  635  0.1745
	      OLYMPIA(B) |   3479 42.4    7.9  654  0.1880
	HALIKARNASSOS(A) |   3503 41.8    7.6  578  0.1650
	HALIKARNASSOS(B) |   3616 39.8    8.0  431  0.1192
	        GIZAH(A) |   3590 43.6    7.6  748  0.2084
	        GIZAH(B) |   3557 43.8    7.8  769  0.2162
	
	Note that to investigate the comparative advantage of each wonder stage,
	players are forced to construct the next wonder stage whenever possible.
	This may explain the high score of Rhodes B which has only 2 wonder stages.
	Without forcing wonder construction:
	
	Wonder           | Trials  Avg StdDev Wins WinRate
	       RHODES(A) |   3509 37.5    7.5  695  0.1981
	       RHODES(B) |   3623 37.4    7.7  687  0.1896
	   ALEXANDRIA(A) |   3545 37.5    7.5  673  0.1898
	   ALEXANDRIA(B) |   3594 37.0    7.7  657  0.1828
	      EPHESOS(A) |   3572 37.9    7.5  757  0.2119
	      EPHESOS(B) |   3564 38.0    7.7  771  0.2163
	      BABYLON(A) |   3551 37.5    7.3  719  0.2025
	      BABYLON(B) |   3583 37.5    7.5  710  0.1982
	      OLYMPIA(A) |   3564 37.9    7.7  740  0.2076
	      OLYMPIA(B) |   3635 37.3    7.5  686  0.1887
	HALIKARNASSOS(A) |   3639 37.9    7.6  738  0.2028
	HALIKARNASSOS(B) |   3556 38.2    7.7  772  0.2171
	        GIZAH(A) |   3565 37.8    7.4  728  0.2042
	        GIZAH(B) |   3514 37.6    7.5  700  0.1992	
	
	
	
