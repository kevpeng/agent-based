main:
	javac *.java

run:
	java SimulationManager

normal:
	java SimulationManager

walk:
	java SimulationManager -i

clean:
	rm -rf *.class
