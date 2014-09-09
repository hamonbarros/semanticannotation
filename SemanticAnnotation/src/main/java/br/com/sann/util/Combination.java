package br.com.sann.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa a operação matemática de Combinação.
 * 
 * @author Hamon.
 * 
 */
public class Combination {
	private int amountCombinationPossible;
	private String[] valuesToBeCombined;
	private int countMax;
	private int count;

	/**
	 * Método construtor da entidade Combinação.
	 * 
	 * @param valuesToBeCombined Array de string dos valores a serem combinados.
	 * @param amountCombinationPossible Quantidade de possíveis combinações.
	 */
	public Combination(String[] valuesToBeCombined, int amountCombinationPossible) {
		this.amountCombinationPossible = amountCombinationPossible;
		this.valuesToBeCombined = valuesToBeCombined;
		this.countMax = ~(1 << valuesToBeCombined.length);
		this.count = 1;
	}

	/**
	 * Retorna true quando há pelo menos uma combinação disponível.
	 */
	public boolean hasNext() {
		if (amountCombinationPossible != 0) {
			while (((this.count & this.countMax) != 0) && (countbits() != amountCombinationPossible))
				count += 1;
		}
		return (this.count & this.countMax) != 0;
	}

	/**
	 * Retorna a quantidade de bits ativos (= 1) de N.
	 * 
	 * @return A quantidade de bits ativos.
	 */
	private int countbits() {
		int i;
		int c;

		i = 1;
		c = 0;
		while ((this.countMax & i) != 0) {
			if ((this.count & i) != 0) {
				c++;
			}
			i = i << 1;
		}

		return c;
	}

	/**
	 * Retorna o tamanho da saída.
	 * 
	 * @return O tamanho da saída.
	 */
	public int getOutLength() {
		if (amountCombinationPossible != 0) {
			return amountCombinationPossible;
		}
		return this.countbits();
	}

	/**
	 * Retorna uma combinacao das combinações possíveis.
	 * 
	 * @return	A próxima combinação disponível.
	 */
	public String[] next() {
		int out_index, in_index, i;
		String[] out = new String[this.getOutLength()];

		in_index = 0;
		out_index = 0;
		i = 1;
		while ((this.countMax & i) != 0) {
			if ((this.count & i) != 0) {
				out[out_index] = valuesToBeCombined[in_index];
				out_index += 1;
			}
			in_index += 1;
			i = i << 1;
		}
		count += 1;

		return out;
	}
	
	/**
	 * Método para realizar a combinação dos tokens de uma string de acordo com a quantidade passada.
	 * 
	 * @return As combinações possíveis.
	 */
	public List<String> combine() {
		List<String> combinations = new ArrayList<String>();
        String [] out;
        while (hasNext()) {
            out = next();
            String line = "";
            for (int i=0; i<amountCombinationPossible; i++) {
            	line += out[i] + " ";
            }
            combinations.add(line.trim());
        }
        return combinations;
	}
	
}
