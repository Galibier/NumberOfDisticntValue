import java.util.HashSet;

public class CountDistinctValue {
    int countOfField;
    private HyperLogLog[] evaluationOfDistinctValue;

    public CountDistinctValue(int fieldCount){
        countOfField = fieldCount;
        evaluationOfDistinctValue = new HyperLogLog[countOfField];

        for (int i=0; i<countOfField; i++){
            evaluationOfDistinctValue[i] = new HyperLogLog();
        }
    }

    public void addSingleData(byte[][] inputSingleData){
        if (inputSingleData.length != countOfField) {
            return;
        }

        for (int i=0; i<countOfField; i++){
            evaluationOfDistinctValue[i].addData(inputSingleData[i]);
        }
    }

    public int[] getDistinctValue(){
        int[] distinctValue = new int[countOfField];

        for (int i=0; i<countOfField; i++){
            distinctValue[i] = evaluationOfDistinctValue[i].getEvaluationResult();
        }

        return distinctValue;
    }

    public static void main(String[] args) {

        final int countOfFields = 8;
        final int singleValueSize = 8;

        CountDistinctValue res = new CountDistinctValue(countOfFields);
        byte[][] testData = new byte[countOfFields][];

        HashSet[] hs = new HashSet[countOfFields];
        for (int i=0; i<countOfFields; i++) {
            hs[i] = new HashSet();
        }

        for (int i=0; i<1000000; i++){
            for (int n = 0; n < countOfFields; n++) {
                int len = singleValueSize;
                testData[n] = new byte[len];
                for (int j = 0; j < len; j++) {
                    testData[n][j] = (byte) (Math.random() * 12354791);
                }
                hs[n].add(MurmurHash.hash64(testData[n]));
            }
            res.addSingleData(testData);
        }

        int[] distinctValue = res.getDistinctValue();

        for (int n = 0; n < countOfFields; n++){
            System.out.printf("Field == Actual: %d", hs[n].size());
            System.out.printf(" --- Evaluation: %d", distinctValue[n]);
            System.out.printf(" --- bias: %2f%%\n", (double)(hs[n].size()-distinctValue[n])/hs[n].size()*100.0);
        }
        /*
        Test output result:
            Field1 == Actual: 853251 --- Evaluation: 860565 --- bias: -0.857192%
            Field1 == Actual: 852688 --- Evaluation: 842960 --- bias: 1.140863%
            Field1 == Actual: 852940 --- Evaluation: 853177 --- bias: -0.027786%
            Field1 == Actual: 852304 --- Evaluation: 864177 --- bias: -1.393048%
            Field1 == Actual: 852527 --- Evaluation: 868852 --- bias: -1.914895%
            Field1 == Actual: 853520 --- Evaluation: 848698 --- bias: 0.564955%
            Field1 == Actual: 852513 --- Evaluation: 862404 --- bias: -1.160217%
            Field1 == Actual: 852981 --- Evaluation: 854454 --- bias: -0.172688%
         */
    }
}
