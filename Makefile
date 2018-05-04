main:
	javac *.java

normal:
	java SimulationManager

special:
	java SimulationManager -i

clean:
	rm -rf *.class
