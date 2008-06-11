package simkit.stat;

/**
 *  <P>A quick hack to provide Student-t quantiles for a few useful values of p
 *  and n.  For more than 100 df, the normal approximation is used.  This
 *  class just looks up the quantiles in a table, which is give n as input data.
 *
 *  <P>An immediate improvement approach would be to linearly interpolate for values
 *  not listed in the pvalues array.  There could be more degrees of freedom covered,
 *  as there is currently a "gap" of approximately 0.02 between 100 df (the last
 *  row) and the normal limit.
 *  @version $Id: StudentT.java 476 2003-12-09 00:27:33Z jlruck $
 *
**/
public class StudentT {

/**
* Holds the pvalues corresponding to the columns of the array quantiles.
**/
public static final double[] pvalues = {0.970, 0.975, 0.980, 0.985, 0.990, 0.995};

/**
* Holds the quantiles for the Student t. Columns correspond to the
* pvalues contained in pvalues, rows correspond to degrees of freedom from 1-100.
**/
public static final double[][] quantiles =
{{10.57889, 12.7062, 15.89454, 21.20495, 31.82052, 63.65674}, 
{3.896425, 4.302653, 4.848732, 5.642778, 6.964557, 9.924843}, 
{2.95051, 3.182446, 3.481909, 3.896046, 4.540703, 5.840909}, 
{2.600762, 2.776445, 2.998528, 3.29763, 3.746947, 4.604095}, 
{2.421585, 2.570582, 2.756509, 3.002875, 3.36493, 4.032143}, 
{2.313263, 2.446912, 2.612242, 2.828928, 3.142668, 3.707428}, 
{2.240879, 2.364624, 2.516752, 2.714573, 2.997952, 3.499483}, 
{2.189155, 2.306004, 2.448985, 2.633814, 2.896459, 3.355387}, 
{2.150375, 2.262157, 2.398441, 2.573804, 2.821438, 3.249836}, 
{2.120234, 2.228139, 2.359315, 2.527484, 2.763769, 3.169273}, 
{2.096139, 2.200985, 2.32814, 2.490664, 2.718079, 3.105807}, 
{2.076441, 2.178813, 2.302722, 2.4607, 2.680998, 3.05454}, 
{2.060038, 2.160369, 2.281604, 2.435845, 2.650309, 3.012276}, 
{2.046169, 2.144787, 2.263781, 2.414898, 2.624494, 2.976843}, 
{2.034289, 2.13145, 2.24854, 2.397005, 2.60248, 2.946713}, 
{2.024, 2.119905, 2.235358, 2.381545, 2.583487, 2.920782}, 
{2.015002, 2.109816, 2.223845, 2.368055, 2.566934, 2.898231}, 
{2.007067, 2.100922, 2.213703, 2.35618, 2.55238, 2.87844}, 
{2.000017, 2.093024, 2.204701, 2.345648, 2.539483, 2.860935}, 
{1.993713, 2.085963, 2.196658, 2.336242, 2.527977, 2.84534}, 
{1.988041, 2.079614, 2.189427, 2.327792, 2.517648, 2.83136}, 
{1.982911, 2.073873, 2.182893, 2.32016, 2.508325, 2.818756}, 
{1.978249, 2.068658, 2.176958, 2.313231, 2.499867, 2.807336}, 
{1.973994, 2.063899, 2.171545, 2.306913, 2.492159, 2.79694}, 
{1.970095, 2.059539, 2.166587, 2.30113, 2.485107, 2.787436}, 
{1.966509, 2.055529, 2.162029, 2.295815, 2.47863, 2.778715}, 
{1.9632, 2.051831, 2.157825, 2.290914, 2.47266, 2.770683}, 
{1.960136, 2.048407, 2.153935, 2.28638, 2.46714, 2.763262}, 
{1.957293, 2.04523, 2.150325, 2.282175, 2.462021, 2.756386}, 
{1.954645, 2.042272, 2.146966, 2.278262, 2.457262, 2.749996}, 
{1.952175, 2.039513, 2.143833, 2.274614, 2.452824, 2.744042}, 
{1.949865, 2.036933, 2.140904, 2.271203, 2.448678, 2.738481}, 
{1.9477, 2.034515, 2.138159, 2.268008, 2.444794, 2.733277}, 
{1.945666, 2.032245, 2.135581, 2.265009, 2.44115, 2.728394}, 
{1.943752, 2.030108, 2.133157, 2.262188, 2.437723, 2.723806}, 
{1.941948, 2.028094, 2.130871, 2.259529, 2.434494, 2.719485}, 
{1.940244, 2.026192, 2.128714, 2.25702, 2.431447, 2.715409}, 
{1.938633, 2.024394, 2.126674, 2.254648, 2.428568, 2.711558}, 
{1.937106, 2.022691, 2.124742, 2.252401, 2.425841, 2.707913}, 
{1.935659, 2.021075, 2.12291, 2.250271, 2.423257, 2.704459}, 
{1.934283, 2.019541, 2.12117, 2.248249, 2.420803, 2.701181}, 
{1.932975, 2.018082, 2.119515, 2.246326, 2.41847, 2.698066}, 
{1.93173, 2.016692, 2.11794, 2.244495, 2.41625, 2.695102}, 
{1.930542, 2.015368, 2.116438, 2.24275, 2.414134, 2.692278}, 
{1.929409, 2.014103, 2.115005, 2.241085, 2.412116, 2.689585}, 
{1.928326, 2.012896, 2.113636, 2.239494, 2.410188, 2.687013}, 
{1.92729, 2.011741, 2.112327, 2.237974, 2.408345, 2.684556}, 
{1.926298, 2.010635, 2.111073, 2.236518, 2.406581, 2.682204}, 
{1.925348, 2.009575, 2.109873, 2.235124, 2.404892, 2.679952}, 
{1.924437, 2.008559, 2.108721, 2.233787, 2.403272, 2.677793}, 
{1.923562, 2.007584, 2.107616, 2.232503, 2.401718, 2.675722}, 
{1.922722, 2.006647, 2.106555, 2.231271, 2.400225, 2.673734}, 
{1.921914, 2.005746, 2.105534, 2.230086, 2.39879, 2.671823}, 
{1.921136, 2.004879, 2.104552, 2.228946, 2.39741, 2.669985}, 
{1.920388, 2.004045, 2.103607, 2.227849, 2.396081, 2.668216}, 
{1.919666, 2.003241, 2.102696, 2.226792, 2.394801, 2.666512}, 
{1.918971, 2.002465, 2.101818, 2.225772, 2.393568, 2.66487}, 
{1.9183, 2.001717, 2.100971, 2.224789, 2.392377, 2.663287}, 
{1.917652, 2.000995, 2.100153, 2.22384, 2.391229, 2.661759}, 
{1.917026, 2.000298, 2.099363, 2.222923, 2.390119, 2.660283}, 
{1.916421, 1.999624, 2.098599, 2.222038, 2.389047, 2.658857}, 
{1.915836, 1.998972, 2.097861, 2.221181, 2.388011, 2.657479}, 
{1.915269, 1.998341, 2.097146, 2.220352, 2.387008, 2.656145}, 
{1.914721, 1.99773, 2.096455, 2.219549, 2.386037, 2.654854}, 
{1.91419, 1.997138, 2.095785, 2.218772, 2.385097, 2.653604}, 
{1.913676, 1.996564, 2.095135, 2.218019, 2.384186, 2.652394}, 
{1.913176, 1.996008, 2.094506, 2.217289, 2.383302, 2.65122}, 
{1.912692, 1.995469, 2.093895, 2.21658, 2.382446, 2.650081}, 
{1.912222, 1.994945, 2.093302, 2.215893, 2.381615, 2.648977}, 
{1.911766, 1.994437, 2.092727, 2.215226, 2.380807, 2.647905}, 
{1.911323, 1.993943, 2.092168, 2.214577, 2.380024, 2.646863}, 
{1.910892, 1.993464, 2.091625, 2.213948, 2.379262, 2.645852}, 
{1.910474, 1.992997, 2.091097, 2.213335, 2.378522, 2.644869}, 
{1.910066, 1.992543, 2.090584, 2.21274, 2.377802, 2.643913}, 
{1.90967, 1.992102, 2.090084, 2.212161, 2.377102, 2.642983}, 
{1.909285, 1.991673, 2.089598, 2.211597, 2.37642, 2.642078}, 
{1.908909, 1.991254, 2.089124, 2.211048, 2.375757, 2.641198}, 
{1.908544, 1.990847, 2.088663, 2.210514, 2.375111, 2.64034}, 
{1.908187, 1.99045, 2.088214, 2.209993, 2.374482, 2.639505}, 
{1.90784, 1.990063, 2.087777, 2.209485, 2.373868, 2.638691}, 
{1.907501, 1.989686, 2.08735, 2.208991, 2.37327, 2.637897}, 
{1.907171, 1.989319, 2.086934, 2.208508, 2.372687, 2.637123}, 
{1.906849, 1.98896, 2.086528, 2.208038, 2.372119, 2.636369}, 
{1.906535, 1.98861, 2.086131, 2.207578, 2.371564, 2.635632}, 
{1.906228, 1.988268, 2.085745, 2.20713, 2.371022, 2.634914}, 
{1.905928, 1.987934, 2.085367, 2.206692, 2.370493, 2.634212}, 
{1.905636, 1.987608, 2.084998, 2.206265, 2.369977, 2.633527}, 
{1.90535, 1.98729, 2.084638, 2.205847, 2.369472, 2.632858}, 
{1.90507, 1.986979, 2.084286, 2.205439, 2.368979, 2.632204}, 
{1.904797, 1.986675, 2.083942, 2.205041, 2.368497, 2.631565}, 
{1.90453, 1.986377, 2.083605, 2.204651, 2.368026, 2.63094}, 
{1.904269, 1.986086, 2.083276, 2.204269, 2.367566, 2.63033}, 
{1.904013, 1.985802, 2.082954, 2.203896, 2.367115, 2.629732}, 
{1.903763, 1.985523, 2.08264, 2.203531, 2.366674, 2.629148}, 
{1.903519, 1.985251, 2.082331, 2.203174, 2.366243, 2.628576}, 
{1.903279, 1.984984, 2.08203, 2.202824, 2.365821, 2.628016}, 
{1.903045, 1.984723, 2.081734, 2.202482, 2.365407, 2.627468}, 
{1.902815, 1.984467, 2.081445, 2.202147, 2.365002, 2.626931}, 
{1.90259, 1.984217, 2.081162, 2.201819, 2.364606, 2.626405}, 
{1.90237, 1.983972, 2.080884, 2.201497, 2.364217, 2.625891}};

/**
* Holds the quantiles of the normal distribution corresponding to
* the pvalues in pvalues.
**/
public static double[] normal = {1.880794, 1.959964, 2.053749, 2.170090, 2.326348, 2.575829};

/**
* Holds the difference between the pvalues contained in pvalues. Currently = 0.005
**/
private static double delta = (pvalues[pvalues.length-1] - pvalues[0]) / (pvalues.length - 1);

/**
* Should never be instantiated.
**/
    protected StudentT() { }

/**
* Gets the quantile for the given degrees of freedom and pvalue. 
* p is rounded off to the nearest pvalue in the internal table.
* The pvalues in the table are: 0.970, 0.975, 0.980, 0.985,
* 0.990, 0.995.
* If df is greater than 100, then uses the normal approximation.
* @param p The desired pvalue.
* @param df The degrees of freedom.
**/
    public static double getQuantile(double p, int df) {
        if (df <= 0) { return Double.NaN; }
        int row = df - 1;
        int column = (int) Math.round((p - pvalues[0])/delta );
        column = (column >= 0) ? column : 0;
        column = (column < pvalues.length) ? column : pvalues.length - 1; 
        return (row < quantiles.length) ? quantiles[row][column] : normal[column]; 
    }

} 
