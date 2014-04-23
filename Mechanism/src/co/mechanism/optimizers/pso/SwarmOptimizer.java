package co.mechanism.optimizers.pso;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ICostFunction;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class SwarmOptimizer extends AbstractOptimizer<Particle> {

    
    
   
    private int informantsPerParticle;
   
    private final double phi;

    public SwarmOptimizer(int numberOfParticles, int dimensions, List<Double> upperBound,
            List<Double> lowerBound, int informantsPerParticle, ICostFunction costFunction) {
        super(lowerBound, upperBound, costFunction);
        this.informantsPerParticle = informantsPerParticle;
        this.init(numberOfParticles, dimensions);
        phi = 0;
    }

    public SwarmOptimizer(int numberOfParticles, int dimensions,List<Double> upperBound,
            List<Double> lowerBound, int informantsPerParticle, ICostFunction costFunction, double phi) {
    	super(lowerBound, upperBound, costFunction);
        this.phi = phi;
        this.informantsPerParticle = informantsPerParticle;
        this.init(numberOfParticles, dimensions);
    }

    private void init(int numberOfParticles, int dimensions) {
        //this.setCostFunction((ICostFunction) Utils.getInstanceByReflection(functionClassName));
        //this.particles = new FastList<Particle>(numberOfParticles);
        MersenneTwisterFast mt = Utils.getMTInstance();
        double pos;
        double vel;
        
            Particle p = null;
            for (int i = 0; i < numberOfParticles; i++) {


                List<Double> tempPosition = new ArrayList<Double>(dimensions);
                List<Double> tempVelocity = new ArrayList<Double>(dimensions);
                for (int j = 0; j < dimensions; j++) {
                    pos = this.getLowerBound().get(j) + (mt.nextDouble() * (this.getUpperBound().get(j) - this.getLowerBound().get(j)));
                    vel = (this.getLowerBound().get(j) - this.getUpperBound().get(j)) / 2 + (mt.nextDouble() * (((this.getUpperBound().get(j)
                            - this.getLowerBound().get(j)) / 2 - (this.getLowerBound().get(j) - this.getUpperBound().get(j)) / 2) + 1));
                    tempPosition.add(pos);
                    tempVelocity.add(vel);
                }
                if (this.phi == 0) {
                    p = new Particle(this, tempPosition, this.informantsPerParticle);
                } else {
                    p = new Particle(this, tempPosition, this.informantsPerParticle, this.phi);
                }

                p.setBestPosition(Utils.doubleListDeepCopy(tempPosition));
                p.setVelocity(tempVelocity);
                p.evaluate(getCostFunction());
                this.getPopulation().add(p);

            }
        
    }

    @Override
    public void evolve() {
    	
        for (Particle p : this.getPopulation()) {
            p.move();
        }
        for (Particle p : this.getPopulation()) {
            p.evaluate(getCostFunction());
        }
    }

    

    public boolean testForConvergence() {
        double dispersion = 1e-12;
        boolean[] convergence = new boolean[this.getUpperBound().size()];
        for (int i = 0; i < this.getUpperBound().size(); i++) {
            double sd = 0;
            double sc = 0;
            double s = 0;
            for (Particle p : this.getPopulation()) {
                sc += Math.pow(p.getPosition().get(i), 2);
                s += p.getPosition().get(i);
            }
            sd = (sc / this.getPopulation().size()) + Math.pow(s / this.getPopulation().size(), 2);
            if (sd < dispersion) {
                convergence[i] = true;
            } else {
                convergence[i] = false;
            }

        }
        for (boolean c : convergence) {
            if (!c) {
                return false;
            }
        }
        return true;
    }

   

    
    

    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        sb.append("***Swarm stats***");
        sb.append(System.getProperty("line.separator"));
        for (Particle p : this.getPopulation()) {
            sb.append("Particle: Result ");
            sb.append(p.getValue()).append(" Position");
            sb.append(p.getPosition());
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("Best Position Overall: ");
        sb.append(this.getBestSolution().getBestPosition());
        sb.append(System.getProperty("line.separator"));
        sb.append("Best Value Overall: ");
        sb.append(this.getBestSolution().getBestValue());
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

//    @Override
//    public ISearchAgent getBestSolution() {
//        return Collections.min(this.particles);
//    }
}
