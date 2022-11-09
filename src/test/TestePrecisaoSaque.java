package test;

import caixa_eletronico.CaixaEletronico;

public class TestePrecisaoSaque {
	public static void main(String[] args) {
		int falha = 0;
		for(int i = 0; i <= 3000; i++) {
			CaixaEletronico cx = new CaixaEletronico();
			
			String message = cx.sacar(i);
			if(message.trim().equals("Saque não realizado por falta de cédulas")
					|| message.trim().equals("Não é possível sacar esse valor")) {
				System.out.println(i);
				++falha;
			}
			
		}
		System.out.println(falha);
	}
}
