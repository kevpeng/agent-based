main:
	javac *.java

run:
	java SimulationManager

input:
	java SimulationManager -i
clean:
	rm -rf *.class
