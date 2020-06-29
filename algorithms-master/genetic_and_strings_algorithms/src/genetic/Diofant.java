package genetic;

import java.util.Random;

public class Diofant {


    //the result of the function that we're looking for
    public static final int TARGET_VALUE = -50  ;

    //value returned when total success is reached(i.e.  target solution is found)
    public static final int TARGET_IS_REACHED_FLAG = -1  ;

    //value returned to indicate that target solution not found
    private static final int TARGET_NOT_REACHED_FLAG = -2  ;

    //number of individuals in population
    public static final int POPULATION_COUNT = 5;

    //number of genes in a chromosome
    public static final int GENES_COUNT = 5;

    //min. value a gene has
    public static final int GENE_MIN = -100;

    //		max. value a gene has
    public static final int GENE_MAX = 100;

    //likelihood (in percent) of the mutation
    public static final float MUTATION_LIKELIHOOD= 5.0F;

    //maximum number of "generations". If solution not found after
    //this number of iterations, the program will return.
    //But for sure it will not happen for MAX_ITERATIONS>=1000
    public static final int MAX_ITERATIONS = 10000;

    //number of individuals participating in tournament selection
    public static final int TOURNAMENT_PARTICIPANTS_COUNT = 5;



    //array of individuals (Chromosomes)
    private Chromosome population[]=new Chromosome[Diofant.POPULATION_COUNT];






    /*
     * Iterate through all chromosomes and fill their "fitness" property
     * */
    private int fillChromosomesWithFitnesses(){
        log( "***Started to create FITNESSES for all chromosomes. " );
        for ( int i=0; i<POPULATION_COUNT;++i ){
            log("Filling with fitness population number "+i);
            float currentFitness = population[i].calculateFitness();
            population[i].setFitness(  currentFitness  );
            log("Fitness: "+population[i].getFitness());

            //target solution is found
            if ( currentFitness == TARGET_IS_REACHED_FLAG  )
                return i;




        }

        log( "***Finished to create FITNESSES for all chromosomes. " );
        return TARGET_NOT_REACHED_FLAG;
    }


    /*
     * Function of the equalization we're solving
     * */
    public static int function( int u, int w, int x, int y, int z ) {
        return  u^2 * w^2 * x^1 * y^2 * z^2 + w * x * z + w^2 * x * y + z + u * w * x^2 * z^2 ;
    }

    /*
     * Returns sum of fitnesses of all chromosomes.
     * This value is used when calculating likelihood
     * */
    private float getAllFitnessesSum(){
        float allFitnessesSum = .0F;
        for ( int i=0; i<POPULATION_COUNT;++i ){
            allFitnessesSum+=population[i].getFitness();
        }
        return allFitnessesSum;
    }

    /*
     * Iterate through all chromosomes and fill their "likelihood" property
     * */
    private void fillChromosomeWithLikelihoods(){
        float allFitnessesSum = getAllFitnessesSum();
        log( "***Started to create LIKELIHOODS for all chromosomes. allFitnessesSum="+allFitnessesSum );
        float last=.0F;

        int i;
        for ( i=0; i<POPULATION_COUNT;++i ){

            float likelihood = last + (100* population[i].getFitness()/allFitnessesSum );
            last=likelihood;
            population[i].setLikelihood( likelihood  );
            log( "Created likelihood for chromosome number "+i+". Likelihood  value:  "+likelihood );
        }

        //setting last chromosome's likeliness to 100 by hand.
        //because sometimes it's 99.9999 and that's not good
        population[i-1].setLikelihood( 100  );

        log( "***Finished to create LIKELIHOODS for all chromosomes. " );
    }

    /*
     * Prints all chromosomes to the log using toString() of Chromosome objects
     * */
    private void printAllChromosomes(){
        log("Here is the current state of all chromosomes:");
        for ( int i=0; i<POPULATION_COUNT;++i ){
            log("**********  Chromosome "+i+"  ********");
            log ( population[i].toString() );
        }
    }

    /*
     * Writes a string to the log
     * */
    public static void log(String message){
        //System.out.println( message );
    }

    /*
     * Returns random integer number between min and max ( all included :)  )
     * */
    public static int getRandomInt( int min, int max ){
        Random randomGenerator;
        randomGenerator = new Random();
        return  randomGenerator.nextInt( max+1 ) + min ;
    }

    /*
     * Returns random float number between min (included) and max ( NOT included :)  )
     * */
    public static float getRandomFloat( float min, float max ){
        return  (float) (Math.random()*max + min) ;
    }


    /*
     * Returns a correct random value for a gene
     * */
    public static int getRandomGene(){
        return getRandomInt( GENE_MIN , GENE_MAX);
    }


    /*
     * Fills a chromosome with random genes.
     * */
    private void fillChromosomeWithRandomGenes( Chromosome chromosome ){
        log("Filling chromosome with random genes....");
        for (int i=0;i<GENES_COUNT;++i){
            chromosome.getGenes()[i]=getRandomGene();
            log("Gene number:"+i+"; value:"+chromosome.getGenes()[i]);

        }
        log("I'm done filling chromosome with random genes..");

    }



    /*
     * Creates an initial population
     * */
    private void createInitialPopulation(){
        log("*** Started creating initial population...");
        for (int i = 0; i<POPULATION_COUNT;++i){
            log("Creating chromosome number "+i);
            population[i]=new Chromosome();
            fillChromosomeWithRandomGenes(population[i]);
        }
        log("*** FINISHED creating initial population...");

    }


    /*
     * Returns pairs for the crossover operations.
     * [0][0] with [0][1]
     * [1][0] with [1][1]
     * etc. etc.
     * */
    private int[][] getPairsForCrossover(){
        log("*** Started looking for pairs for crossover");

        int[][] pairs = new int[POPULATION_COUNT][2];

        for (int i = 0; i<POPULATION_COUNT;++i){
            log("Looking for pair number "+i+"...");
            float rand=getRandomFloat(0, 100);
            int firstChromosome = findIndividualForCrossoverByTournament();
            log("First individual... Random number: "+rand+"; corresponding chromosome:"+firstChromosome+
                    "; chromosome's fitness*100: "+population[firstChromosome].getFitness()*100);

            int secondChromosome;
            do{
                rand=getRandomFloat(0, 100);
                secondChromosome = findIndividualForCrossoverByTournament();

            }while (firstChromosome==secondChromosome) ;  //prevent individual's crossover with itself :)


            log("Second individual... Random number: "+rand+"; corresponding chromosome:"+secondChromosome+
                    "; chromosome's fitness*100: "+population[secondChromosome].getFitness()*100);

            pairs[i][0] = firstChromosome;
            pairs[i][1] = secondChromosome;

        }

        log("*** Finished looking for pairs for crossover");

        return pairs;
    }


    /*
     * For testing only.
     * Check if the individuals selected for crossover are really the best :)
     * */
    private void analizePairs(int[][] pairs){
        log( "*** Started analyzing totals (for testing only)" );

        int[] totals = new int[POPULATION_COUNT];

        //fill totals array with zeroes
        for (int i = 0; i<POPULATION_COUNT;++i){
            totals[i] = 0;
        }

        //calculate how many times each individual will do the crossover
        for (int i = 0; i<POPULATION_COUNT;++i){
            for (int j = 0; j<2;++j){
                totals [ pairs[i][j]  ] ++;
            }

        }

        //printing totals
        for (int i = 0; i<POPULATION_COUNT;++i){
            log( "Individual #"+i+"; fitness:"+population[i].getFitness()+"; number of crossovers:"+totals[i] );
        }

        log( "*** Finished analyzing totals (for testing only)" );

    }


    /*
     * Returns number of chromosome in population[] array
     * corrresponding to the randomly generated number rand
     * */
    private int getChromosomeNumberForThisRand(float rand){

        //looks like a little optimiztion would be great here

        int i;
        for ( i = 0; i<POPULATION_COUNT;++i){

            if (  rand<=population[i].getLikelihood() ){
                return i;
            }
        }
        return i-1; //unreachable code imho :) But without this it doesn't compile

    }

    /*
     * Performs a tournament between TOURNAMENT_PARTICIPANTS_COUNT individuals,
     * selects the best of them and returns its index in population[] array
     * */
    private int findIndividualForCrossoverByTournament(){

        log( "starting findIndividualForCrossoverByTournament()" ) ;


        int bestIndividualNumber=0;
        double bestFitness=0;

        for ( int i=0;i<TOURNAMENT_PARTICIPANTS_COUNT;++i ){
            int currIndividualNumber = getRandomInt( 0 , POPULATION_COUNT-1);

            if ( population[ currIndividualNumber ].getFitness() > bestFitness    ){
                bestFitness = population[ currIndividualNumber ].getFitness();
                bestIndividualNumber = currIndividualNumber;
            }

            log( "i="+i+"; currIndividualNumber="+currIndividualNumber+
                    "; bestFitness="+bestFitness+";bestIndividualNumeber="+bestIndividualNumber   );

        }



        return bestIndividualNumber;


    }

    private  Chromosome[] performCrossoverAndMutationForThePopulationAndGetNextGeneration(  int[][] pairs ){

        Chromosome nextGeneration[]=new Chromosome[Diofant.POPULATION_COUNT];

        log("*******************************");
        log("Starting performing Crossover operation For The Population...");

        for (int i = 0; i<POPULATION_COUNT;++i){
            log("** Starting crossover #"+i);
            Chromosome firstParent = population[  pairs[i][0]  ];
            Chromosome secondParent = population[  pairs[i][1]  ];
            log("First parent (#"+pairs[i][0]+")\n" + firstParent );
            log("Second parent (#"+pairs[i][1]+")\n" + secondParent );

            Chromosome result = firstParent.singleCrossover( secondParent );
            nextGeneration[i]=result;
            log( "Resulting (child) chromosome BEFORE the mutation:\n"+ nextGeneration[i]);

            nextGeneration[i]=nextGeneration[i].mutateWithGivenLikelihood();

            log( "Resulting (child) chromosome AFTER the mutation:\n"+ nextGeneration[i]);
            log("** Finished crossover #"+i);
        }

        log("Finished performing Crossover operation For The Population...");
        return nextGeneration;
    }


    public Chromosome[] getPopulation() {
        return population;
    }


    public void setPopulation(Chromosome[] population) {
        this.population = population;
    }

    /*
     * main method :) :) :) :) :) :)
     * */
    public static void main(String[] args){
        log("main() is started");
        log("POPULATION_COUNT="+POPULATION_COUNT);
        log("GENES_COUNT="+GENES_COUNT);
        Diofant diofant = new Diofant();
        diofant.createInitialPopulation();

        long iterationsNumber = 0;
        do {

            int fillingWithFitnessesResult =  diofant.fillChromosomesWithFitnesses();

            //if a result was found... print results and finish the program
            if ( fillingWithFitnessesResult != TARGET_NOT_REACHED_FLAG ){
                System.out.println ("Solution is found: "+ diofant.getPopulation()[fillingWithFitnessesResult]  );
                return;
            }

            diofant.fillChromosomeWithLikelihoods();
            diofant.printAllChromosomes();
            int[][] pairs = diofant.getPairsForCrossover();
            diofant.analizePairs(pairs);


            Chromosome nextGeneration[]=new Chromosome[Diofant.POPULATION_COUNT];

            nextGeneration = diofant.performCrossoverAndMutationForThePopulationAndGetNextGeneration(  pairs );

            diofant.setPopulation(nextGeneration);

            System.out.println( "-=-=========== Finished iteration #"+iterationsNumber  );
        } while ( iterationsNumber++<MAX_ITERATIONS );

        System.out.println("SOLUTION NOT FOUND. Sad but true...");



    }
}

