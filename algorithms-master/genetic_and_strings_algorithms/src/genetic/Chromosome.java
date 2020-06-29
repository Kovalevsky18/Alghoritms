package genetic;
public class Chromosome {

    //array of genes
    private int genes[]=new int[Diofant.GENES_COUNT];

    //fitness :)
    private float fitness;

    //likelihood :)
    private float likelihood;

    //setters and getters :)
    public float getFitness() {
        return fitness;
    }
    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
    public int[] getGenes() {
        return genes;
    }
    public void setGenes(int[] genes) {
        this.genes = genes;
    }
    public float getLikelihood() {
        return likelihood;
    }
    public void setLikelihood(float likelihood) {
        this.likelihood = likelihood;
    }




    /*
     *Calculates fitness for THIS chromosome and returns it.
     * */
    public float  calculateFitness(){
        int u = genes[0];
        int w = genes[1];
        int x = genes[2];
        int y = genes[3];
        int z = genes[4];

        int closeness = Math.abs(  Diofant.TARGET_VALUE -  Diofant.function( u,w,x,y,z  )   ) ;
        Diofant.log("Closeness: "+closeness);

        return  0!=closeness  ? 1/(float)closeness : Diofant.TARGET_IS_REACHED_FLAG ;
    }




    public Chromosome mutateWithGivenLikelihood(){
        Diofant.log( "Starting mutateWithGivenLikelihood().... Diofant.MUTATION_LIKELIHOOD=="+Diofant.MUTATION_LIKELIHOOD );

        Chromosome result =  (Chromosome ) this.clone();

        for (int i=0;i<Diofant.GENES_COUNT;++i){
            float randomPercent = Diofant.getRandomFloat(0,100);
            if ( randomPercent < Diofant.MUTATION_LIKELIHOOD ){
                int oldValue =  result.getGenes()[i];
                int newValue= 	Diofant.getRandomGene();
                Diofant.log( "Performing mutation for gene #"+i
                        +". ( randomPercent =="+randomPercent
                        +" ). Old value:"+oldValue +"; New value:"+newValue );
                result.getGenes()[i] = newValue;


            }
        }
        Diofant.log( "Finished mutateWithGivenLikelihood()...." );
        return result;
    }

    /*
       This crossover gives birth to 2 children
    */
    public Chromosome[] doubleCrossover(  Chromosome chromosome  ){

        Diofant.log( "Starting DOUBLE crossover operation..." );
        Diofant.log( "THIS chromo:"+this );
        Diofant.log( "ARG chromo:"+chromosome );



        int crossoverline = getRandomCrossoverLine();
        Chromosome[] result = new Chromosome[2];
        result[0]=new Chromosome();
        result[1]=new Chromosome();

        for (int i=0;i<Diofant.GENES_COUNT;++i){
            if ( i<=crossoverline){
                result[0].getGenes()[i] =  this.getGenes()[i];
                result[1].getGenes()[i] =  chromosome.getGenes()[i];
            }

            else {
                result[0].getGenes()[i] =  chromosome.getGenes()[i];
                result[1].getGenes()[i] =  this.getGenes()[i];
            }
        }

        Diofant.log( "RESULTING chromo #0:\n"+result[0] );
        Diofant.log( "RESULTING chromo #1:\n"+result[1] );
        Diofant.log( "DOUBLE crossover operation is finished..." );

        return result;

    }


    /*
       This crossover gives birth to 1 child.
        To perform that, it calls doubleCrossover() and then
         randomly selects one of the 2 children.
    */
    public Chromosome singleCrossover(  Chromosome chromosome  ){

        Diofant.log( "Starting SINGLE crossover operation...Calling DOUBLE crossover first...." );

        Chromosome[] children = doubleCrossover(  chromosome  ) ;

        Diofant.log( "Selecting ONE of the 2 children returned by DOUBLE crossover ...." );

        int childNumber = Diofant.getRandomInt(0, 1);

        Diofant.log( "Selected child #"+childNumber +", here it is:\n"+children[childNumber] );

        Diofant.log( "SINGLE crossover operation is finished" );
        return children[childNumber];
    }

    /*
     * Checks if "this" is equal to the arg. object, Not sure if this method is ever used.
     * */
    public boolean equals( Chromosome chromosome ){

        for ( int i = 0; i<Diofant.GENES_COUNT;++i ){
            if ( this.genes[i]!=chromosome.genes[i]  ){
                return false;
            }
        }
        return true;

    }

    /*
     * String representation of the object
     * */
    public String toString(){

        StringBuffer result = new StringBuffer();

        result.append(  "Genes: ("  ) ;

        for ( int i = 0; i<Diofant.GENES_COUNT;++i ){
            result.append(  ""+genes[i]  ) ;
            result.append(  i<Diofant.GENES_COUNT-1 ? ", ":""   );

        }

        result.append(  ")\n"  ) ;

        result.append( "Fitness:" + fitness+"\n" );
        result.append( "Likelihood:" + likelihood+"\n" );


        return result.toString();


    }

    /*
     * Returns number of gene AFTER which comes the crossover line
     * e.g. (1,20,3,4)
     * Position after "1" is 0
     * Position after "20" is 1 etc.
     * */
    private static int getRandomCrossoverLine(){
        int line = Diofant.getRandomInt(0, Diofant.GENES_COUNT - 2);  //-2 because we dn't need the position after the last gene
        Diofant.log("Generated random CrossoverLine at position "+line);
        return line;
    }



    protected Object clone()  {
        Chromosome resultChromosome = new Chromosome() ;
        resultChromosome.setFitness(  this.getFitness() );
        resultChromosome.setLikelihood(  this.getLikelihood() );

        int resultGenes[]=new int[Diofant.GENES_COUNT];
        resultGenes=this.genes.clone();

        resultChromosome.setGenes(resultGenes);

        return resultChromosome;
    }

    public static void main(String[] args) throws Exception{

        System.out.print( Integer.toBinaryString( 2 )  );


    }

}

