package classe;

public class Produto {
	String nome;
	double preco;
	double desconto;
	
	Produto() {}
	
	Produto(String nomeInicial, double precoInicial, double descontoInicial)
	{
		nome = nomeInicial;
		preco = precoInicial;
		desconto = descontoInicial;
	}
	
	double precocomDesconto() {
		return preco * (1 - desconto);
	}
	
	double precocomDesconto(double descontoDoGerente) {
		return preco * (1 - desconto + descontoDoGerente);
	}
}
