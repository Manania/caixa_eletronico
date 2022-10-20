public class CaixaEletronico implements ICaixaEletronico {
    private static final int VALOR = 0, QNTDE = 1;
    private static final String MSG_VALOR_ABAIXO_MINIMO = "Caixa Vazio: Chame o Operador",
        MSG_SAQUE_INDISPONIVEL = "Saque não realizado por falta de cédulas";

    /**
     * Cada cedula possui um vetor próprio de comprimento dois. O primeiro elemento é o valor e o segundo a quantidade. 
     * <pre>
     * Ex:
     * new int[][]{
     *  {200, 123}
     *  {100, 212},
     *  { 50, 123},
     *  { 20, 159},
     *  { 10, 819},
     *  {  5, 332},
     *  {  2, 543}
     * };
     * </pre>
     */
    private int[][] cedulas;
    private int cotaMinima, maxCedulaSaque;
    private ICombinador combinador;

    public CaixaEletronico() {
        this(new int[][] { {100, 0}, { 50, 0}, { 20, 0}, { 10, 0}, {  5, 0}, {  2, 0} }, 
        0, 30, new CombinadorRecusivo());       
    }

    private CaixaEletronico(int[][] cedulas, int cotaMinima, int maxCedulaSaque, ICombinador combinador){
        this.cedulas = cedulas;
        this.cotaMinima = cotaMinima;
        this.maxCedulaSaque = maxCedulaSaque;
        this.combinador = combinador;
    }
  
    public String pegaRelatorioCedulas() {
        StringBuilder resposta = new StringBuilder( 9 * cedulas.length  );
        for(int[] cedula : this.cedulas) {
            resposta.append("\"%d\": %d\n".formatted(cedula[VALOR], cedula[QNTDE]));
        }
        return resposta.toString();
    }

    public String pegaValorTotalDisponivel() {
        return "R$ %.2d".formatted(valorTotalDiposnivel());
    }

    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        if(!cedulaExiste(cedula)) {
            return "Erro. Cédulas de valor R$ %d não são suportadas".formatted(cedula);
        }
        if(quantidade < 0){
            return "Erro. Reposição realizada com quantidade negativa de cédulas";
        }
        
        for (int[] ced : this.cedulas) {
            if(ced[VALOR] == cedula) {
                ced[QNTDE] += quantidade;
                break;
            }
        }
        return "Cédula: %d\nQuantidade adicionada: %d".formatted(cedula, quantidade);
    }

    public String sacar(Integer valor) {
        if(valor <= 0) { return "Erro. Valor de saque invalido"; }
        if((valorTotalDiposnivel() - valor) < cotaMinima) { return MSG_VALOR_ABAIXO_MINIMO; }

        int[][] saque = combinador.calcularCombinacao(valor, this.cedulas, this.maxCedulaSaque);
        if(saque == null) { return MSG_SAQUE_INDISPONIVEL; }
        
        for(int i = 0; i < saque.length; i++) {
            this.cedulas[i][QNTDE] -= saque[i][QNTDE];
        }
        
        StringBuilder str = new StringBuilder();
        for(int[] cedula : saque) {
            str.append("%3d : %4d\n".formatted(cedula[VALOR], cedula[QNTDE]));
        }
        return str.toString();
    }

    public String armazenaCotaMinima(Integer minimo) {
        if(minimo > 0) { this.cotaMinima = minimo; } 
        else { this.cotaMinima = 0; }
        return "Cota minima registrada com sucesso";
    }

    private boolean cedulaExiste(int valor_cedula) {
        for(int[] cedula : this.cedulas){
            if(valor_cedula == cedula[VALOR]) { return true; }
        }
        return false;
    }

    private int valorTotalDiposnivel() {
        int total = 0;
        for(int[] cedula : this.cedulas) { total += cedula[VALOR] * cedula[QNTDE]; }
        return total;
    }
   
    public interface ICombinador {
        /**
         * <p>Retorna uma "tabela" com a combinação de cédulas cuja soma é igual a valor.</p>
         * <p>Se não houver combinação real, o retorno deve ser null.</p>
         * <p>Cada subvetor da tabela deve seguir o seguinte formato: {valor, quantidade}.</p>
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
         * <p>O comportamento do método para valor <= 0 é indefinido.</p>
         * <p>O comportamento do método para max_cedulas <= 0 é indefinido</p>
         * <p>O objeto referenciado por cedulas NÃO pode ser modificado.
         * @param valor Valor total da combinação
         * @param cedulas Matriz com o número de cédulas disponiveis
         * @param max_cedulas Número máximo de cédulas em uma combinação
         * @return Combinação de cédulas cuja soma é igual a valor
         */
        int[][] calcularCombinacao(int valor, int[][] cedulas, int max_cedulas);
    }

    public static void main(String arg[]){
        //GUI janela = new GUI(CaixaEletronico.class);
        //janela.show();
    }
}

class CombinadorRecusivo implements CaixaEletronico.ICombinador {
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
        final int[] result = findSubset(valor, 0, set, 0, subset, 0, max_cedulas);
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
    private static int[] findSubset(int target, int total, int set[], int set_pos, int subset[], int subset_len, int max_size) {
        int[] result = new int[2];
        if(subset_len == max_size || set_pos >= set.length || subset_len >= subset.length || total == target) {
            result[0] = total;
            result[1] = subset_len;
            return result;
        }

        subset[subset_len] = set[set_pos];
        if((result = findSubset(target, total + set[set_pos], set, set_pos +1, subset, subset_len +1, max_size))[0] == target) {
            return result;
        }
        
        while(set_pos < (set.length -1) && set[set_pos] == set[set_pos + 1]) { set_pos++; }

        if((result = findSubset(target, total, set, set_pos + 1, subset, subset_len, max_size))[0] == target) {
            return result;
        }
        return result;
    }
}
