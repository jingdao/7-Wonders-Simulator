7 Wonders Board Game Simulator
===========

Commmand line:

	# Building
	javac -d bin src/controller/* src/model/
	# Manual Simulation with debug printouts
	java -cp bin controller.Controller 
	# Automatic simulation
	java -cp bin controller.Simulator

Simulation results:

	(N=10000 T=5 MINS)
	
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
