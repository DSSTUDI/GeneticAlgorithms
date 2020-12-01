import java.lang.Math;
import java.util.*;


class Example {

    //Problem 1 variables.
    static final int SOLSIZE = 20;
    static final int POPSIZE = 200;
    static double min = -5.0;
    static double max = 5.0;
    static Random r = new Random();

    //Problem 2 variables.
    static final int SOLSIZE2 = 100;
    static final int POPSIZE2 = 70;
    static double bestUtil = -5000;


    public static void main(String[] args){

        //Do not delete/alter the next line
        long startT=System.currentTimeMillis();

        //Edit this according to your name and login
        String name="Dominykas Sliuzas";
        String login = "ds725";

        System.out.println("These are the instructions of how to use the problem library.  Please make sure you follow the instructions carefully.");


        System.out.println("For the first problem, you need to use Assess.getTest1(double[]).");

        //An example solution consists of an array  of doubles of size 20
        //Allowed values are between -5 and +5 (this is not actually checked, but no point in going beyond these values)
        //Lower fitness is better. The optimal fitness is 0 (i.e. no negative fitness values).


        double[] finalSol1 = generateSolution();
        double[][] population = new double[POPSIZE][SOLSIZE];
        double[][] newPopulation = new double[POPSIZE][SOLSIZE];

        double[] sol1 = new double[SOLSIZE];
        double[] sol2 = new double[SOLSIZE];
        double[] newSol = new double[SOLSIZE];

        double[] fitness = new double[POPSIZE];

        //Generates the initial population.
        for(int c = 0; c < POPSIZE; c++){

            population[c] = generateSolution();

        }

        boolean end = false;
        while (!end){

            for(int c = 0; c < population.length ; c++){

                sol1 = population[r.nextInt(POPSIZE)];
                sol2 = population[r.nextInt(POPSIZE)];

                newPopulation[c] = tournament(sol1, sol2);

                newPopulation[c] = mutate(newPopulation[c]);

                fitness[c] = Assess.getTest1(newPopulation[c]);

                if (fitness[c] == 0){

                    finalSol1 = newPopulation[c].clone();
                    end = true;

                }

                //System.out.println(fitness[c]);

            }

            population = newPopulation;

        }

        System.out.println("The fitness of your example Solution is: "+ Assess.getTest1(finalSol1));


        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Now let us turn to the second problem:");
        System.out.println("A sample solution in this case is a boolean array of size 100.");
        System.out.println("I now create a random sample solution and get the weight and utility:");



        //Creating a sample solution for the second problem
        //The higher the fitness, the better, but be careful of the weight constraint!

        boolean[] finalSol2 = generateSolution2();
        boolean[][] population2 = new boolean[POPSIZE2][SOLSIZE2];
        boolean[][] newPopulation2 = new boolean[POPSIZE2][SOLSIZE2];

        boolean[] boolSol1 = new boolean[SOLSIZE2];
        boolean[] boolSol2 = new boolean[SOLSIZE2];
        boolean[] boolSol3 = new boolean[SOLSIZE2];
        boolean[] boolSol4 = new boolean[SOLSIZE2];

        int crossChance = 80;

        //Generates the initial population.
        for (int c = 0; c < population2.length; c++){

            population2[c] = generateSolution2();

        }

        double bestFit = -10000;

        while (bestFit < 207){

            for (int c = 0; c < population2.length; c++){

                //Selects 4 tournament contenders.
                boolSol1 = population2[r.nextInt(POPSIZE2)];
                boolSol2 = population2[r.nextInt(POPSIZE2)];
                boolSol3 = population2[r.nextInt(POPSIZE2)];
                boolSol4 = population2[r.nextInt(POPSIZE2)];

                boolean[] t1winner = tournament2(boolSol1, boolSol2);
                boolean[] t2winner = tournament2(boolSol3, boolSol4);

                if (r.nextInt(100) < crossChance){

                    newPopulation2[c] = crossover(t1winner, t2winner);

                }

                newPopulation2[c] = mutate2(newPopulation2[c]);

            }

            population2 = newPopulation2;

            //Calculates best fit
            for (int c = 0; c < population2.length; c++){

                if (getFitness(population2[c]) > bestFit){

                    bestFit = getFitness(population2[c]);
                    finalSol2 = population2[c];

                }

            }

        }



        //Once completed, your code must submit the results you generated, including your name and login:
        //Use and adapt  the function below:
        Assess.checkIn(name,login,finalSol1,finalSol2);

        //Do not delete or alter the next line
        long endT= System.currentTimeMillis();
        System.out.println("Total execution time was: " +  ((endT - startT)/1000.0) + " seconds");



    }

    public static double[] mutate(double[] set) {

        double[] newSet = set.clone();
        int regularChance = 90;
        int bigChance = 1;

        for (int c = 0; c < newSet.length; c++){

            if (r.nextInt(100) < regularChance){

                double change = r.nextDouble() * r.nextDouble() * r.nextDouble();
                change = change * change;

                if (r.nextInt(10) > 4){

                    newSet[c] = set[c] + change;

                    if (Assess.getTest1(newSet) > Assess.getTest1(set)){

                        newSet[c] = newSet[c] - change;

                    }

                }
                else{

                    newSet[c] = set[c] - change;

                    if (Assess.getTest1(newSet) > Assess.getTest1(set)){

                        newSet[c] = newSet[c] + change;

                    }

                }

            }
            else if (r.nextInt(100) < bigChance){

                newSet[c] = generateSolution()[c];

            }

        }

        return newSet;

    }

    public static double[] generateSolution(){

        double[] newSolution = new double[SOLSIZE];

        for (int c = 0; c < SOLSIZE; c++){

            newSolution[c] = min + (max - min) * r.nextDouble();

        }

        return newSolution;

    }

    public static double[] tournament(double[] set1, double[] set2){

        if (Assess.getTest1(set1) < Assess.getTest1(set2)){

            return set1;

        }
        else{

            return set2;

        }

    }

    public static boolean[] generateSolution2(){

        boolean[] newSol = new boolean[SOLSIZE2];

        for (int c = 0; c < newSol.length; c++){

            newSol[c] = (r.nextBoolean());

        }

        return newSol;

    }

    public static double getFitness(boolean[] set){

        double weight = Assess.getTest2(set)[0];
        double util = Assess.getTest2(set)[1];

        if (weight > 500){

            return -weight;

        }
        else{

            return util;

        }

    }

    public static boolean[] tournament2(boolean[] set1, boolean[] set2){

        if (getFitness(set1) > getFitness(set2)){

            return set1;

        }
        else {

            return set2;

        }

    }

    public static boolean[] crossover(boolean[] set1, boolean[] set2){

        int cut = r.nextInt(SOLSIZE2);
        boolean[] newSet = new boolean[SOLSIZE2];

        for (int c = 0; c < cut; c++){

            newSet[c] = set1[c];

        }
        for (int c = cut; c < newSet.length; c++){

            newSet[c] = set2[c];

        }

        return newSet;

    }

    public static boolean[] mutate2(boolean[] set){

        boolean[] newSet = set.clone();
        int chance = 1;

        for (int c = 0; c < newSet.length; c++){

            if (r.nextInt(100) < chance){

                if (r.nextInt(10) < 5){

                    newSet[c] = true;

                }
                else{

                    newSet[c] = false;

                }

            }

        }

        return newSet;

    }


}
