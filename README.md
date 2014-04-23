Mechanism
=========

A Java bio inspired optimization library. Mechanism supports versions of Differential Evolution, Genetic Algorithms,
Particle Swarm Optimization, Clonal Selection Algorithm and OPT-aiNet.

Example of use:

```
//PSO, 20 particles, 300 iterations, 2D problem
SwarmOptimizer optimizer = new SwarmOptimizer(20, 2, max, min, 5,this.function);
evolve(optimizer, 300);

//Differential Evolution, 20 individuals, 200 iterations, F = 0.7, Cr= 0.9, 2D problem
DEOptimizer optimizer = new DEOptimizer(2, 20, 0.7, 0.9, min, max,this.function, true);
evolve(optimizer, 200);

//Genetic Algorithm, 70 individuals, 50% probability of mutation per offspring individual, Stochastic Universal Sampling
//selection, Convex Combination crossover, Elitistic replacement, Gaussian mutation, 2D continuous problem
GeneticOptimizer optimizer = new GeneticOptimizer(70, 0.5, min, max,this.function,
				new SUSWithRankSelectionProvider(2, 40),
				new IntermediateRecombinationUniformCrossoverProvider(),
				new ElitismReplacementProvider(),
				new GaussianMutationProvider(0.4), new RandomRealPositionProvider());
evolve(optimizer, 200);
```
