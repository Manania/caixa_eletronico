package caixa_eletronico;

import caixa_eletronico.combinadores.*;

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
        this(new int[][] { 
            {100, 0}, 
            { 50, 0}, 
            { 20, 0}, 
            { 10, 0}, 
            {  5, 0}, 
            {  2, 0} 
        }, 
        0, 30, new CombinadorRecursivo());       
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
         * <p>Vetores em java são mutáveis. O objeto referenciado por cedulas NÃO deve ser modificado.</p>
         * @param valor Valor total da combinação
         * @param cedulas Matriz com o número de cédulas disponiveis
         * @param max_cedulas Número máximo de cédulas em uma combinação
         * @return Combinação de cédulas cuja soma é igual a valor
         */
        int[][] calcularCombinacao(int valor, int[][] cedulas, int max_cedulas);
    }

}

