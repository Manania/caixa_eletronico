public class CaixaEletronico implements ICaixaEletronico {
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
    private final static int VALOR = 0, QNTDE = 1;
    private int[][] cedulas;
    private int cotaMinima, maxCedulaSaque;

    public CaixaEletronico() {
        cedulas = new int[][]{
            {100, 0},
            { 50, 0},
            { 20, 0},
            { 10, 0},
            {  5, 0},
            {  2, 0}
        };
        cotaMinima = 0;
        maxCedulaSaque = 30;
    }
  
    public String pegaRelatorioCedulas() {
        //logica de fazer o relatorio de cedulas
        StringBuilder resposta = new StringBuilder();
        for(int[] cedula : cedulas) {
            resposta.append("\"R$ %1$-3d\": %2$-4d\n".formatted(cedula[VALOR], cedula[QNTDE]));
        }

        return resposta.toString();
    }

    public String pegaValorTotalDisponivel() {
        //logica de pega o valor total disponivel no caixa eletronico
        long valor = 0;
        for(int[] cedula : cedulas) { valor += cedula[VALOR] * cedula[QNTDE]; }
        return "R$ %d.00".formatted(valor);
    }

    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        //logica de fazer a reposicao de cedulas e criar uma mensagem
        //(resposta)ao usuario
        if(!cedulaExiste(cedula)) {
            return "Erro. Cédulas de valor R$ %d não são suportadas".formatted(cedula);
        }
        if(quantidade < 0){
            return "Erro. Reposição realizada com quantidade negativa de cédulas";
        }
        
        for (int[] ced : cedulas) {
            if(ced[VALOR] == cedula) {
                ced[QNTDE] += quantidade;
                break;
            }
        }
        return "Cédula: %d\nQuantidade adicionada: %d".formatted(cedula, quantidade);
    }

    public String sacar(Integer valor) {
        //logica de sacar do caixa eletronico e criar um mensagem(resposta) ao 
        //usuario
        if(valor <= 0) { return "Erro. Valor inválido"; }
        
        int[][] cedulaDisponiveis = new int[this.cedulas.length][];
        for(int i = 0; i < this.cedulas.length; i++) { 
            System.arraycopy(this.cedulas[i], 0, cedulaDisponiveis[i], 0, this.cedulas.length); 
            //combinacao[i] = Arrays.copyOf(this.cedulas[i], this.cedulas.length);
        }

        int[][] saque = combinacaoCedulas(valor, cedulaDisponiveis, this.maxCedulaSaque);
        if(saque == null) { return "Não é possivel realizar o saque"; }
        
        for(int i = 0; i < saque.length; i++){
            this.cedulas[i][QNTDE] -= saque[i][QNTDE];
        }
        
        StringBuilder str = new StringBuilder();
        for(int[] cedula : saque){
            str.append("%3d : %4d\n".formatted(cedula[VALOR], cedula[QNTDE]));
        }
        return str.toString();
    }

    public String armazenaCotaMinima(Integer minimo) {
        //logica de armazenar a cota minima para saque e criar um
        //mensagem(resposta)ao usuario
        if(minimo > 0) { this.cotaMinima = minimo; } 
        else { this.cotaMinima = 0; }
        return "Cota minima registrada com sucesso";
    }

    private boolean cedulaExiste(int valor_cedula) {
        for(int[] cedula : cedulas){
            if(valor_cedula == cedula[VALOR]) { return true; }
        }
        return false;
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
    private static int[][] combinacaoCedulas(int valor_saque, int[][] cedulas, int max_cedulas){
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

    public static void main(String arg[]){
/* 
        GUI janela = new GUI(CaixaEletronico.class);
        janela.show();
         */
    }
}
