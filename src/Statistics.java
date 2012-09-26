public class Statistics {
    public double TP, FP, TN, FN, TPR, FPR, TNR, FNR;
    public double precision, recall, f1measure, fpoint5measure, f2measure;
    
    public Statistics() {
        TP = FP = TN = FN = TPR = FPR = TNR = FNR = 0.0;
        precision = recall = f1measure = fpoint5measure = f2measure = 0.0;
    }

    public void calculateRates() {
        TPR = TP / (TP + FN);
        FPR = FP / (FP + TN);
        FNR = FN / (FN + TP);
        TNR = TN / (TN + FP);

        precision = TP / (TP + FP);
        recall = TPR;
        fpoint5measure = (1.25*precision*recall) / (0.25*precision + recall);
        f1measure = (2.0*precision*recall) / (precision + recall);
        f2measure = (5.0*precision*recall) / (4.0*precision + recall);
    }
}
