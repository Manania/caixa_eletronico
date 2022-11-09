package caixa_eletronico.combinadores;

import caixa_eletronico.*;

public class CombinadorRecursivo implements CaixaEletronico.ICombinador {
    /**
     * <p>Detalhes da implementação:</p>
     * <p>Se valor <= 0, o returno é null.</p>
     * <p>Se max_cedulas <= 0, o limite de cédulas é: trunc(alvo / valor_menor_cedula)</p>
     */
    public int[][] calcularCombinacao(int valor, int[][] cedulas, int max_cedulas) {
        if(valor <= 0) { return null; }
        return calcularCedulasMutavel(valor, arrayDeepCopy(cedulas), max_cedulas);
    }

    private static int[][] arrayDeepCopy(int[][] src) {
        int[][] copy = new int[src.length][];
        for(int i = 0; i < src.length; i++) { 
            copy[i] = new int[src[i].length];
            System.arraycopy(src[i], 0, copy[i], 0, src[i].length); 
        }
        return copy;
    }

    private static int[][] calcularCedulasMutavel(int valor, int[][] cedulas, int max_cedulas) {
        final int VALOR = 0, QNTD = 1;
        
        if(valor <= 0) { return null; }
        if(max_cedulas <= 0) { max_cedulas = valor / cedulas[cedulas.length -1][VALOR]; } 

        int set_lenght = 0;
        for(int i = 0; i < cedulas.length; i++){
            cedulas[i][QNTD] = Math.min(cedulas[i][QNTD], (int)Math.ceil(valor / cedulas[i][VALOR]));
            cedulas[i][QNTD] = Math.min(cedulas[i][QNTD], max_cedulas);
            set_lenght += cedulas[i][QNTD];
        }

        final int[] set = new int[set_lenght];
        for(int i = 0, added = 0; i < cedulas.length; i++) {
            for(int j = 0; j < cedulas[i][QNTD]; j++) {
                set[added++] = cedulas[i][VALOR];
            }
        }
                
        final int[] subset = new int[set_lenght];
        final int[] result = findSubset_r(valor, 0, set, 0, subset, 0, max_cedulas);
        if(result[0] != valor){
            return null;
        }

        final int SUBSET_LEN = result[1];
        for(int i = 0, j = 0; i < cedulas.length; i++) {
            cedulas[i][QNTD] = 0;
            while(j < SUBSET_LEN && subset[j] == cedulas[i][VALOR]) {
                cedulas[i][QNTD]++;
                j++;
            }
        }
        return cedulas;
    }

    /**
     * <p>Função para encontrar um subconjunto em set, cuja soma dos elementos é igual a target.</p>
     * <p>Os elementos do subconjunto encontrado são retornados pelo parametro subset.</p>
     * <p>Retorna um vetor com a soma dos elementos e comprimento do subconjunto.</p>
     * <p>O tamanho máximo do subconjunto é especificado pelo parametro max_size (inclusivo).</p>
     * <p>Para garantir que o menor subconjunto seja encontrado, set deve estar ordenado em ordem decrescente.</p>
     * <p>O comportamento da função é indefinido para target <= 0</p>
     * @param target soma dos elementos do subconjunto
     * @param total deve ser 0
     * @param set conjunto a ser explorado
     * @param set_pos deve ser 0
     * @param subset vetor de comprimento igual a set
     * @param subset_len deve ser 0
     * @param max_size v
     * @return {total_subconjunto, comprimento_subconjuto}
     */
    private static int[] findSubset_r(int target, int total, int set[], int set_pos, int subset[], int subset_len, int max_size) {
        int[] result = new int[2];
        if(subset_len == max_size || set_pos >= set.length || total == target) {
            result[0] = total;
            result[1] = subset_len;
            return result;
        }

        subset[subset_len] = set[set_pos];
        if((result = findSubset_r(target, total + set[set_pos], set, set_pos +1, subset, subset_len +1, max_size))[0] == target) {
            return result;
        }
        
        while(set_pos < (set.length -1) && set[set_pos] == set[set_pos + 1]) { ++set_pos; }

        if((result = findSubset_r(target, total, set, set_pos + 1, subset, subset_len, max_size))[0] == target) {
            return result;
        }
        return result;
    }
}
