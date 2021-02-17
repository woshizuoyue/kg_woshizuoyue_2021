import java.io.PrintWriter;
import java.util.Scanner;

public class BiGaussAutoThrSelection {

    public int numRows,numCols,minVal, maxVal,maxHeight, offSet,thrVal, grayScaleCount, bestThrVal;
    public double mean,var;
    public int []histAry;
    public int [][]histGraph;
    public int []GaussAry;
    public int [][]GaussGraph;
    public int [][]gapGraph;
    public PrintWriter outFile1;
    public PrintWriter outFile2;
    public PrintWriter outFile3;

    static int[][] a;

    public BiGaussAutoThrSelection(Scanner inFile, PrintWriter outFile1, PrintWriter outFile2, PrintWriter outFile3){

        this.outFile1 = outFile1;
        this.outFile2 = outFile2;
        this.outFile3 = outFile3;

        numRows = numCols = minVal = maxVal = maxHeight = offSet = thrVal =  0;

        mean = var = 0.0;

        numRows = inFile.nextInt();
        numCols = inFile.nextInt();
        minVal = inFile.nextInt();
        maxVal = inFile.nextInt();

        offSet = (maxVal-minVal)/10;

        thrVal = offSet;

        grayScaleCount = maxVal + 1;

        histAry = new int[maxVal+1];
        histGraph = new int[maxVal+1][maxHeight+1];
        GaussAry = new int[maxVal+1];
        GaussGraph = new int[maxVal+1][maxHeight+1];
        gapGraph = new int[maxVal+1][maxHeight+1];

        for (int i = 0;i < (maxVal + 1); i++){
            for (int j = 0; j< (maxHeight + 1); j++){
                histGraph[i][j] = 0;
                GaussGraph[i][j] = 0;
                gapGraph[i][j] = 0;
            }
        }

        for (int i = 0; i < (maxVal + 1); i++){
            histAry[i] = 0;
            GaussAry[i] = 0;
        }


    }

    public void set1DZero(int[] ary){
        for(int i = 0;i <= maxVal; i++ ){
            ary[i] = 0;
        }
    }

    public void set2DZero( int[][] matrix){
        for(int i = 0; i<= maxVal; i++){
            for( int j = 0; j <=maxHeight; j++){
                matrix[i][j]=0;
            }
        }
    }

    public void loadHist(Scanner inFile){
        for(int i = 0; i < grayScaleCount; i++){
            histAry[i] = inFile.nextInt();
        }
    }


    public void plotHistGraph(int [][]histGraph){
        for (int i = 0; i< grayScaleCount; i++){
            histGraph[i][histAry[i]]++;//这没有assign hist为 1
        }
        prettyPrint(histGraph, outFile1);//有问
    }

    public double computeMean(int leftIndex, int rightIndex, int maxHeight){
        double numPixels=0.0;
        int sum = 0;
        maxHeight = 0;
        for(int i = leftIndex; i<= rightIndex; i++){
            //mean = mean+(i*(double)histAry[i]);
            sum=histAry[i]*i;
            numPixels=histAry[i];
            if(histAry[i]>maxHeight){
                maxHeight = histAry[i];
            }

        }
        return sum/numPixels;
    }


    public double computeVar(int leftIndex, int rightIndex, double mean){
        double x = 0.0;
        double numPixels = 0.0;
        for(int i = leftIndex; i<= rightIndex; i++){
            x+=(Math.pow(i-mean, 2)*histAry[i]);
            numPixels +=histAry[i];
        }
        if(numPixels > 0)
            return x/numPixels;
        return 0;
    }

    public double modifiedGauss(int i, double mean, double var, int maxHeight){
        if(var == 0.0)
            return 0.0;
        return maxHeight *(Math.exp(-1*(Math.pow(i - mean,2)/(2*var))));

    }

    public int biMeanGauss(int thrVal){
        double sum1, sum2,total = 0.0;
        int bestThr = thrVal;
        double minSumDiff = 999999.0;
        while(thrVal<(maxVal-offSet)){
            set1DZero(GaussAry);
            set2DZero(GaussGraph);
            set2DZero(gapGraph);

            sum1 = fitGauss(0, thrVal,GaussAry, GaussGraph);
            sum2 = fitGauss(thrVal, maxVal,GaussAry, GaussGraph);
            total = sum1+sum2;
            if(total < minSumDiff){
                minSumDiff = total;
                bestThr = thrVal;
            }
            thrVal++;
            prettyPrint(GaussGraph,outFile2);
            plotGaps(histAry, GaussGraph,gapGraph);
            prettyPrint(gapGraph, outFile3);


        }
        return bestThr;

    }

    public double fitGauss(int leftIndex, int rightIndex, int[]GaussAry, int[][]GaussGraph){
        double gVal, maxGval, sum= 0.0;
        double temp = 0.0;
        int maxHeight = 0;
        double mean = computeMean(leftIndex, rightIndex, maxHeight);
        double var = computeVar(leftIndex, rightIndex,mean);
        for(int i = leftIndex; i <= rightIndex; i++){
            gVal = modifiedGauss(i,mean,var,maxHeight);
            temp = Math.abs(gVal- (double)histAry[i]);
            sum +=temp;
            GaussAry[i] =(int)gVal;
            GaussGraph[i][(int)gVal] = 1;

        }
        return sum;

    }

    public void bestThrPlot(int bestThrVal){
        double sum1, sum2 = 0.0;
        set1DZero(GaussAry);
        set2DZero(GaussGraph);
        set2DZero(gapGraph);

        sum1 = fitGauss(0,bestThrVal,GaussAry,GaussGraph);
        sum2 = fitGauss(bestThrVal,maxVal, GaussAry,GaussGraph);

        plotGaps(histAry, GaussGraph, gapGraph);
    }





    public void prettyPrint(int [][]graph, PrintWriter outFile ){
        for(int i = 0; i <= maxVal; i++){
            for(int j = 0; j<= maxHeight; j++){
                if(graph[i][j]<= 0)
                    outFile.format(" ");
                else
                    outFile.format("*");

            }
        }
    }

    public void plotGaps(int[] histAry,int[][]GaussGraph,int[][]gapGraph){
        int first, last = 0;
        for(int i = 0; i <= maxVal; i++){
            if(histAry[i]<GaussAry[i]){
                first = histAry[i];
                last = GaussAry[i];
            }
            else{
                first = GaussAry[i];
                last = histAry[i];
            }
            while(first <= last){
                gapGraph[i][first] = 1;
                first++;
            }
        }
    }



}
