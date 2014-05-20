Mechanism
=========

A Java bio inspired optimization library. Mechanism supports versions of Differential Evolution, Genetic Algorithms,
Particle Swarm Optimization, Clonal Selection Algorithm and OPT-aiNet.

Example of use:

```java
//The objective function to be optimized
public class RastriginFunction implements ICostFunction{

    public double evaluate(List<Double> inputs) throws Exception {
        return 20 + Math.pow(inputs.get(0),2) + Math.pow(inputs.get(1),2) - 10*(Math.cos(2*Math.PI*inputs.get(0))+Math.cos(2*Math.PI*inputs.get(1)));
    }

    public String toString() {
    	return "RastriginFunction";
    }
}
//Somewhere else
ICostFunction f = new RastriginFunction();
//PSO, 20 particles, 300 iterations, 2D problem
SwarmOptimizer optimizer = new SwarmOptimizer(20, 2, max, min, 5,f);
for (int i = 0; i < 300; i++) {
	optimizer.evolve();
		
}

//Differential Evolution, 20 individuals, 200 iterations, F = 0.7, Cr= 0.9, 2D problem
DEOptimizer optimizer = new DEOptimizer(2, 20, 0.7, 0.9, min, max,f, true);
for (int i = 0; i < 200; i++) {
	optimizer.evolve();
		
}

//Genetic Algorithm, 70 individuals, 50% probability of mutation per offspring individual, Stochastic Universal Sampling
//selection, Convex Combination crossover, Elitistic replacement, Gaussian mutation, 2D continuous problem
GeneticOptimizer optimizer = new GeneticOptimizer(70, 0.5, min, max,f,
				new SUSWithRankSelectionProvider(2, 40),
				new IntermediateRecombinationUniformCrossoverProvider(),
				new ElitismReplacementProvider(),
				new GaussianMutationProvider(0.4), new RandomRealPositionProvider());
for (int i = 0; i < 100; i++) {
	optimizer.evolve();
		
}
```
