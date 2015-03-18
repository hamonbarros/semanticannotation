package br.com.sann.performance.test;

public class RemoveIdsIrrelevants {

	public static void main(String[] args) {
		String idsTotais = "29237,5208,5871,21431,31844,127463,175020,29436,160039,117848,41630,5870,13134,31467,33203,36026,162238,175019,167866,185953,15601,23375,205458,58557,172860,26821,167867,39167,133401,74238,5207,147672,186105,80136,57295,185952,5986,177509,5951,214,23374,48181";
		String idsIrrelevants = "186105,127463,147672,185952,185953,214,13134,23374,23375,31844,57295,58557";
		String[] idsIrrelevantsArray = idsIrrelevants.split(",");
		System.out.println(idsTotais);
		for (String id : idsIrrelevantsArray) {
			idsTotais = idsTotais.replace(id+",", "");
		}
		System.out.println(idsTotais);
	}
}
