public class EditDistance {

    public int calculateDistance(String source, String destination) {
        int sourceLength = source.length();
        int destinationLength = destination.length();
        int[][] d = new int[sourceLength + 1][destinationLength + 1];

        for (int i = 0; i <= sourceLength; i++)
            d[i][0] = i;
        for (int j = 0; j <= destinationLength; j++)
            d[0][j] = j;

        for (int j = 0; j < destinationLength; j++)
            for (int i = 0; i < sourceLength; i++)
                d[i + 1][j + 1] = (source.charAt(i) == destination.charAt(j)) ?
                        d[i][j] :
                        Math.min(d[i][j + 1], Math.min(d[i + 1][j], d[i][j])) + 1;
        return d[sourceLength][destinationLength];
    }
}
