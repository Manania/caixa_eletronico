import java.util.Arrays;
import java.lang.Math;

public class Saque {
    /**
     * Retorna o número minimo de cédulas R$ 2.00 necessário para formar o montante
     * @param montante
     * @return
     */
    public static int minDois(int montante) {
        final int resto = montante % 5;
        if((resto & 1) == 0) {
            return resto / 2;
        } 
        return (resto + 5) / 2;
    }

    public static int minCinco(int montante) {
        final int resto = montante % 2;
        if((resto & 1) == 1){
            return 1;
        } 
        return 0;
    }

    public static int minCedulas(int montante) {
        return (int) Math.ceil(montante / 100);
    }

    /**
     * <p>Retorna uma matriz especificando a combinação de cédulas cuja soma seja igual ao alvo. 
     * Se não existir uma combinação, o retorno é null.</p>
     * <p>O número máximo de cédulas em uma combinação é especificado por max_cedulas. Se o valor for 0,
     * o limite é: ceil(alvo / valor_menor_cedula)</p>
     * <p>O valor e quantidade de cédulas deve ser especificado pelo parâmetro cédulas:
     * a matriz fornecida deve ser composta por vetores de dois elementos, sendo o primeiro o valor da cédula e o 
     * segundo a quantidade. As cédulas devem ser ordenadas pelo seu valor em ordem decrescente</p>
     * <pre>
     * Ex: new int[][]{
     *      {200, 50},
     *      {100, 32},
     *      {50, 130},
     *      {20, 150},
     *      {10, 20}, 
     *      {5, 42},
     *      {2, 31}
     * }      
     * @param montante
     * @param cedulas cedula[i][0] == VALOR, cedula[i][1] == QUANTIDADE
     * @return
     */
    public static int[][] melhorCombinacao(int montante, int cedulas[][]) {
        int[][] _cedulas = new int[cedulas.length][];
        for(int j = 0; j < cedulas.length; j++){
            _cedulas[j] = Arrays.copyOf(cedulas[j], cedulas[j].length);
        }
        for(int[] cedula : _cedulas){
            cedula[1] = 0;
        }

        //Arrays.sort(cedulas, (c1, c2) ->  c2[0] - c1[0]);
        _cedulas[5][1] = minDois(montante);
        _cedulas[4][1] = minCinco(montante);
        montante -= _cedulas[5][0] * _cedulas[5][1];
        montante -= _cedulas[4][0] * _cedulas[4][1];

        for(int i = 0; i < _cedulas.length; i++) {
            int valor_cedula = _cedulas[i][0];
            int quantidade = cedulas[i][1];

            _cedulas[i][1] += Math.min(montante / valor_cedula, quantidade);
            montante -= valor_cedula * _cedulas[i][1];
        }
        
        return montante == 0 ? _cedulas : null;
    }

    /**
     * Função para encontrar um subconjunto em set, cuja soma dos elementos é igual a target.
     * Os elementos do subconjunto encontrado são retornados pelo parametro subset.
     * Retorna um vetor com a soma dos elementos e comprimento do subconjunto.
     * O tamanho máximo do subconjunto é especificado pelo parametro max_size (inclusivo).
     * Para garantir que o menor subconjunto seja encontrado, set deve estar ordenado em ordem decrescente
     * @param target soma dos elementos do subconjunto
     * @param total deve ser 0
     * @param set conjunto a ser explorado
     * @param set_pos deve ser 0
     * @param subset vetor de comprimento igual a set
     * @param subset_len deve ser 0
     * @param max_size v
     * @return {[0]=total do subconjunto, [1]=comprimento do subconjuto}
     */
    public static int[] findSubset(int target, int total, int set[], int set_pos, int subset[], int subset_len, int max_size){
        int[] result = new int[2];
        if(subset_len == max_size || set_pos >= set.length || subset_len >= subset.length || total == target){
            result[0] = total;
            result[1] = subset_len;
            return result;
        }

        subset[subset_len] = set[set_pos];
        if((result = findSubset(target, total + set[set_pos], set, set_pos +1, subset, subset_len +1, max_size))[0] == target){
            return result;
        }
        
        while(set_pos < set.length -1 && set[set_pos] == set[set_pos + 1]) set_pos++;

        if((result = findSubset(target, total, set, set_pos + 1, subset, subset_len, max_size))[0] == target){
            return result;
        }
        return result;
    }

    /**
     * <p>Retorna uma matriz especificando a combinação de cédulas cuja soma seja igual ao alvo. 
     * Se não existir uma combinação, o retorno é null.</p>
     * <p>O número máximo de cédulas em uma combinação é especificado por max_cedulas. Se o valor for 0,
     * o limite é: trunc(alvo / valor_menor_cedula)</p>
     * <p>O valor e quantidade de cédulas deve ser especificado pelo parâmetro cédulas:
     * a matriz fornecida deve ser composta por vetores de dois elementos, sendo o primeiro o valor da cédula e o 
     * segundo a quantidade. As cédulas devem ser ordenadas pelo seu valor em ordem decrescente</p>
     * <pre>
     * Ex: new int[][]{
     *      {200, 50},
     *      {100, 32},
     *      {50, 130},
     *      {20, 150},
     *      {10, 20}, 
     *      {5, 42},
     *      {2, 31}
     * }
     * </pre>
     * <p>Atente-se ao fato de que vetores em Java sempre são mutáveis, e cedulas[][] 
     * será diretamente modificado.</p>
     * 
     * @param alvo
     * @param cedulas
     * @param max_cedulas
     * @return
     */
    public static int[][] combinacaoCedulas(int valor_saque, int[][] cedulas, int max_cedulas){
        final int VALOR = 0, QUANTIDADE = 1;
        if(max_cedulas <= 0){
            max_cedulas = valor_saque / cedulas[cedulas.length -1][VALOR];
        } 

        int set_lenght = 0;
        for(int i = 0; i < cedulas.length; i++){
            cedulas[i][QUANTIDADE] = Math.min(cedulas[i][QUANTIDADE], (int)Math.ceil(valor_saque / cedulas[i][VALOR]));
            cedulas[i][QUANTIDADE] = Math.min(cedulas[i][QUANTIDADE], max_cedulas);
            set_lenght += cedulas[i][QUANTIDADE];
        }

        final int[] set = new int[set_lenght];
        for(int i = 0, added = 0; i < cedulas.length; i++)
        for(int j = 0; j < cedulas[i][QUANTIDADE]; j++)
            set[added++] = cedulas[i][VALOR];
        
        final int[] subset = new int[set_lenght];
        final int[] result = findSubset(valor_saque, 0, set, 0, subset, 0, max_cedulas);
        if(result[0] != valor_saque){
            return null;
        }

        final int SUBSET_LEN = result[1];
        for(int i = 0, j = 0; i < cedulas.length; i++){
            cedulas[i][QUANTIDADE] = 0;
            while(j < SUBSET_LEN && subset[j] == cedulas[i][VALOR]){
                cedulas[i][QUANTIDADE]++;
                j++;
            }
        }

        return cedulas;
    }


    public static void main(String[] args) {
        long latencia_copia = 0;
        long latencia_busca = 0;
        long latencia_processo = System.nanoTime();

        int max_size = 40;

        int[][] cedulas = new int[][]{
            {100, 10000},
            {50, 100000},
            {20, 100000},
            {10, 100000},
            {5, 1000000},
            {2, 1000000},
        };

        for(int target = 1; target < cedulas[0][0] * max_size +1; target++){
            long inicio_op = System.nanoTime();
            
            int[][] _cedulas = new int[cedulas.length][];
            for(int j = 0; j < cedulas.length; j++){
                _cedulas[j] = Arrays.copyOf(cedulas[j], cedulas[j].length);
            }
            
            //int[][] _cedulas = Arrays.stream(cedulas).map(cedula -> Arrays.copyOf(cedula, cedula.length)).toArray(int[][]::new);
            latencia_copia += System.nanoTime() - inicio_op;

            inicio_op = System.nanoTime();
            int[][] saque = combinacaoCedulas(target, _cedulas, max_size);
            //int[][] saque = melhorCombinacao(target, _cedulas);
            latencia_busca += System.nanoTime() - inicio_op;

            System.out.println("target:%3d".formatted(target));

            if(saque == null){
                System.out.println("Não é possivel realizar esse saque");
            } else {
                for(int [] cedula : saque)
                    System.out.println("%3d:%3d".formatted(cedula[0], cedula[1]));  
            }
        }

        latencia_processo =  System.nanoTime() - latencia_processo;
        System.out.println("latencia_copia:%,d".formatted(latencia_copia));
        System.out.println("latencia_busca:%,d".formatted(latencia_busca));
        System.out.println("latencia_processo:%,d".formatted(latencia_processo));
    }
}
